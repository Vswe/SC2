package vswe.stevesvehicles.module.cart.tool;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.Blocks.ModBlocks;
import vswe.stevesvehicles.old.Helpers.BlockCoordinate;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.IActivatorModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.addon.ModuleDrillIntelligence;
import vswe.stevesvehicles.module.cart.addon.ModuleIncinerator;
import vswe.stevesvehicles.module.cart.addon.ModuleLiquidSensors;
import vswe.stevesvehicles.module.cart.addon.ModuleOreTracker;
import vswe.stevesvehicles.module.common.storage.chest.ModuleChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModuleDrill extends ModuleTool implements IActivatorModule {
	public ModuleDrill(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 50;
	}


	
	
	private ModuleDrillIntelligence intelligence;
	private ModuleLiquidSensors liquidSensors;
	private ModuleOreTracker tracker;
	@Override
	public void init() {
		super.init();
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleDrillIntelligence) {
				intelligence = (ModuleDrillIntelligence)module;
			}
			if (module instanceof ModuleLiquidSensors) {
				liquidSensors = (ModuleLiquidSensors)module;
			}	
			if (module instanceof ModuleOreTracker) {
				tracker = (ModuleOreTracker)module;
			}			
		}		
	}
	
	
	//return true when the work is done, false allow other modules to continue the work
	@Override
	public boolean work() {
		if (!isDrillEnabled()) {
			stopDrill();
			stopWorking();	
			return false;
		}else if (!doPreWork()) {
			stopDrill();
			stopWorking();
		}

        //get the next block so the cart knows where to mine
        Vec3 next = getNextBlock();
        //save the coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;
        //retrieve the height range of the hole
        int[] range = mineRange();

        //loop through the blocks in the "hole" in front of the cart
        for (int holeY = range[1]; holeY >= range[0]; holeY--)
        {
            for (int holeX = -blocksOnSide(); holeX <= blocksOnSide(); holeX++)
            {
				if (intelligence != null && !intelligence.isActive(holeX + blocksOnSide(), holeY, range[2], x > getVehicle().x() || z < getVehicle().z())) {
					continue;
				}
			
                //calculate the coordinates of this "hole"
                int targetX = x + (getVehicle().z() != z ? holeX : 0);
                int targetY = y + holeY;
                int targetZ = z + (getVehicle().x() != x ? holeX : 0);

				
				
                if (mineBlockAndRevive(targetX, targetY, targetZ, next, holeX, holeY)) {
					return true;
				}
            }
        }
		
		if (countsAsAir(x, y + range[0], z) && !isValidForTrack(x, y + range[0], z, true) && mineBlockAndRevive(x, y + (range[0] - 1), z, next, 0, range[0] - 1)) {
			return true;
		}	

        //if the code goes all the way to here, the cart is still ready for action. Return false.
		stopWorking();
		stopDrill();
        return false;
	}

	

	/**
    Return the height range of the mined hole
    **/
    protected int[] mineRange() {
        //the first element is the start index, the last is the end index
        //get the next block
        Vec3 next = getNextBlock();
        //save the next block's coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

		int yTarget = getModularCart().getYTarget();

        if (BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y, z) || BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y - 1, z))
        {
            return new int[] {0, blocksOnTop() - 1, 1};
        }
        else if (y > yTarget)
        {
            return new int[] { -1, blocksOnTop() - 1, 1};
        }
        else if (y < yTarget)
        {
            return new int[] {1, blocksOnTop() + 1, 0};
        }
        else
        {
            return new int[] {0, blocksOnTop() - 1, 1};
        }
    }

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
	protected abstract int blocksOnTop();

	//returns how far the drill should drill on each side
	protected abstract int blocksOnSide();

	
	public int getAreaWidth() {
		return blocksOnSide() * 2 + 1;
	}
	
	public int getAreaHeight() {
		return blocksOnTop();
	}
	
	
	private boolean mineBlockAndRevive(int targetX, int targetY, int targetZ, Vec3 next, int holeX, int holeY) {
		if (mineBlock(targetX, targetY, targetZ, next, holeX, holeY, false)) {
			return true;
		}else if(isDead()) {
			revive();
			return true;
		}else{
			return false;
		}
	}	
	

	
    protected boolean mineBlock(int targetX, int targetY, int targetZ, Vec3 next, int holeX, int holeY, boolean flag)
    {
    	if (tracker != null) {
    		BlockCoordinate start = new BlockCoordinate(targetX, targetY, targetZ);
    		BlockCoordinate target = tracker.findBlockToMine(this, start);
    		if (target != null) {
	    		targetX = target.getX();
	    		targetY = target.getY();
	    		targetZ = target.getZ();
    		}
    	}
    	
        //test whether this block is valid or not
        Object valid = isValidBlock(targetX, targetY, targetZ, holeX, holeY, flag);
        TileEntity storage = null;

        if (valid instanceof TileEntity){
            //if the test code found the block to be valid but it returned a TileEntity it means we have to remove all items inside its inventory too(therefore save the TileEntity)
            storage = (TileEntity)valid;
        }else if (valid == null){
            //if this block wasn't valid, try the next one
            return false;
        }

        //retrieve some information about the block
        Block b = getVehicle().getWorld().getBlock(targetX, targetY, targetZ);
        int m = getVehicle().getWorld().getBlockMetadata(targetX, targetY, targetZ);

        //if the cart hasn't been working, tell it to work and it's therefore done for this tick
        //if (doPreWork())
        //{
            //calculate the working time
            float h = b.getBlockHardness(getVehicle().getWorld(), targetX, targetY, targetZ);

            if (h < 0)
            {
                h = 0;
            }

			//CrumbleBlocksHook.startCrumblingBlock(this, coordX, coordY, coordZ, workingTime);
            //return true;
        //}

        //if the cart is already working, the code below should be run
        ItemStack item;

        //if the block is a block with an inventory, empty it first
        if (storage != null) {
            //loop through the inventory
            for (int i = 0; i < ((IInventory)(storage)).getSizeInventory(); i++) {
                //pick out an item from the next slot
                item = ((IInventory)(storage)).getStackInSlot(i);

                //check if there was anything there
                if (item == null) {
                    continue;
                }

                //let the cart do its thing with the item, on success remove it from the inventory.
                if (minedItem(item, next)){
                    ((IInventory)(storage)).setInventorySlotContents(i, null);
                } else {
                    /*turnback();
					stopWorking();
                    return false;*/
					return false;
                }
            }
        }

        int fortune = enchanter != null ? enchanter.getFortuneLevel() : 0;
        

        	
        if (shouldSilkTouch(b, targetX, targetY, targetZ, m)) {
        	ItemStack silkTouchedItem = getSilkTouchedItem(b, m);
        	
        	if (silkTouchedItem == null || minedItem(silkTouchedItem, next)) {
        		getVehicle().getWorld().setBlockToAir(targetX, targetY, targetZ);
        	}else{
        		return false;
        	}                  

        //if the block drops anything, drop it
        }else if (b.getDrops(getVehicle().getWorld(), targetX, targetY, targetZ, m, fortune).size() != 0) {
            //iStack = new ItemStack(b.idDropped(0, rand, 0), b.quantityDropped(rand), b.damageDropped(m));
			ArrayList<ItemStack> stacks = b.getDrops(getVehicle().getWorld(), targetX, targetY, targetZ, m, fortune);
            boolean shouldRemove = false;
            for (ItemStack stack : stacks) {
                if (minedItem(stack, next)) {
                    shouldRemove = true;
                } else {
                    /*turnback();
					stopWorking();
					return false
					*/
                    return false;
                }
            }

			if (shouldRemove) {
				getVehicle().getWorld().setBlockToAir(targetX, targetY, targetZ);
			}
        }else{
            //mark this cart as idle and remove the block
            getVehicle().getWorld().setBlockToAir(targetX, targetY, targetZ);
        }

		startWorking(getTimeToMine(h));
		startDrill();
        return true;
    }

    /**
    Let the cart handle a mined item, return true upon success.
    **/
    protected boolean minedItem(ItemStack item, Vec3 coordinate) {
		if (item == null || item.stackSize <= 0) {
			return true;
		}
	
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleIncinerator) {
				((ModuleIncinerator)module).incinerate(item);
				if (item.stackSize <= 0) {
					return true;
				}
			}
		}      
		
		int size = item.stackSize;
        getVehicle().addItemToChest(item);

        if (item.stackSize == 0){
            //everything worked fine
            return true;
        }else {
			boolean hasChest = false;
			for (ModuleBase module : getVehicle().getModules()) {
				if (module instanceof ModuleChest) {
					hasChest = true;
					break;
				}
			}

			if (hasChest) {
				if (item.stackSize != size) {
					//if only some items did fit in the chest we have no other choice than spitting out the rest
					//but don't do it the normal way, that would only make the Miningcart w/ chest get stuck and
					//the whole point with it is to avoid that, spit it out to its side instead
					EntityItem entityitem = new EntityItem(getVehicle().getWorld(), getVehicle().getEntity().posX, getVehicle().getEntity().posY, getVehicle().getEntity().posZ , item);
					//observe that the motion for X uses the Z coords and vice-versa
					entityitem.motionX = (float)(getVehicle().z() - coordinate.zCoord) / 10;
					entityitem.motionY = 0.15F;
					entityitem.motionZ = (float)(getVehicle().x() - coordinate.xCoord) / 10;
					getVehicle().getWorld().spawnEntityInWorld(entityitem);
					return true;
				}else{
					 //let's get out of here!
					return false;
				}
			}else{
			     //pop out the item out of the cart's back
				EntityItem entityitem = new EntityItem(getVehicle().getWorld(), getVehicle().getEntity().posX, getVehicle().getEntity().posY, getVehicle().getEntity().posZ , item);
				entityitem.motionX = (float)(getVehicle().x() - coordinate.xCoord) / 10;
				entityitem.motionY = 0.15F;
				entityitem.motionZ = (float)(getVehicle().z() - coordinate.zCoord) / 10;
				getVehicle().getWorld().spawnEntityInWorld(entityitem);
				return true;
			}
		}
    }

    private int getTimeToMine(float hardness) {
    	int efficiency = enchanter != null ? enchanter.getEfficiencyLevel() : 0;
        return (int)((getTimeMultiplier() * hardness) / Math.pow(1.3F, efficiency)) + (liquidSensors != null ? 2 : 0);
    }

	protected abstract float getTimeMultiplier();

    /**
    Method to check if a block is a valid block to remove by the miner

    x, y and z is the coordinates of the block while i and j is offset location compared to the minecart

    if this returns null the block is not valid, all other values are valid.
    **/
    public Object isValidBlock(int x, int y, int z, int i, int j, boolean flag) {
        //do not remove rail blocks or block which will cause rail blocks to be removed
        if ((!flag && BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y, z)) || BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y + 1, z)) {
            return null;
        }else {
            //retrieve the needed values, block id and the like
            Block b = getVehicle().getWorld().getBlock(x, y, z);
            int m = getVehicle().getWorld().getBlockMetadata(x, y, z);

            //there need to be a block to remove
            if (b == null){
                return null;

                //don't try to remove air
            }else if (getVehicle().getWorld().isAirBlock(x, y, z)) {
	            return null;

                //don't remove bedrock
            }else if (b == Blocks.bedrock) {
                return null;

                //don't remove fluids either
            }else if (b instanceof BlockLiquid) {
                return null;

                //nor things which can't be removed
            }else if (b.getBlockHardness(getVehicle().getWorld(), x, y, z) < 0){
                return null;

                //some special things are just allowed to be removed when in font of the cart, like torches
            } else if ((i != 0 || j > 0) && (
                    b == Blocks.torch ||
                    b == Blocks.redstone_wire ||
                    b == Blocks.redstone_torch ||
                    b == Blocks.unlit_redstone_torch ||
                    b == Blocks.powered_repeater ||
                    b == Blocks.unpowered_repeater ||
                    b == Blocks.powered_comparator ||
                    b == Blocks.unpowered_comparator ||
					b == ModBlocks.MODULE_TOGGLER.getBlock()
                    )) {

                return null;
                //for containers like chest a special rule apply, therefore test if this is a container
            }else if (b instanceof BlockContainer) {

                //if so load its tileentity to check if it has an inventory or not
                TileEntity tileentity = getVehicle().getWorld().getTileEntity(x, y, z);

                if (tileentity != null && IInventory.class.isInstance(tileentity)) {
                    //depending on its position it's either invalid or we should return the tileentity to be able to remove its items
                    if (i != 0 || j > 0) {
                        return null;
                    }else {
                        return tileentity;
                    }
                }
            }

			
			if (liquidSensors != null) {
				//check all five directions for danger(no need to check below since liquids can't flow upwards ^^)
				if (liquidSensors.isDangerous(this, x, y, z, 0, +1, 0) ||
						liquidSensors.isDangerous(this, x, y, z, +1, 0, 0) ||
						liquidSensors.isDangerous(this, x, y, z, -1, 0, 0) ||
						liquidSensors.isDangerous(this, x, y, z, 0, 0, +1) ||
						liquidSensors.isDangerous(this, x, y, z, 0, 0, -1)) {
					sensorLight = (byte)3;
					return null;
				}
				sensorLight = (byte)2;
			}

            //if the code goes all the way the block is valid to remove
            return false;
        }
    }

	
	
	@Override
    public void update() {
        super.update();

        if ((getVehicle().hasFuel() && isMining()) || miningCoolDown < 10){
			drillRotation = (float)((drillRotation + 0.03F * (10 - miningCoolDown)) % (Math.PI * 2));
			if (isMining()) {
				miningCoolDown = 0;
			}else{
				miningCoolDown++;
			}
        }

		if (!getVehicle().getWorld().isRemote && liquidSensors != null) {
			byte data = sensorLight;
			if (isDrillSpinning()) {
				data |= 4;
			}
			liquidSensors.getInfoFromDrill(data);
			sensorLight = (byte)1;
		}
    }

	protected void startDrill() {
		updateDw(0,1);
	}

	protected void stopDrill() {
		updateDw(0,0);
	}

	protected boolean isMining() {
		if (isPlaceholder()) {
			return getSimInfo().getDrillSpinning();
		}else{
			return getDw(0) != 0;
		}
	}

	protected boolean isDrillSpinning() {
		return isMining() || miningCoolDown < 10;
	}
	@Override
	public void initDw() {
		addDw(0,0);
		addDw(1,1);
	}
	@Override
	public int numberOfDataWatchers() {
		return 2;
	}

	private byte sensorLight  = (byte)1;
	private float drillRotation;
	private int miningCoolDown;

	public float getDrillRotation() {
		return drillRotation;
	}
			
	private boolean isDrillEnabled() {
		return getDw(1) != 0;
	}	
	
	public void setDrillEnabled(boolean val) {
		updateDw(1, val ? 1 : 0);
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, buttonRect)) {
				sendPacket(0);
			}
		}
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			setDrillEnabled(!isDrillEnabled());
		}
	}

	@Override
	public int numberOfPackets() {
		return 1;
	}	
	
	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.TOOLS.DRILL.translate(), 8, 6, 0x404040);
	}


	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		super.drawBackground(gui, x, y);
		
		ResourceHelper.bindResource("/gui/drill.png");

		int imageID = isDrillEnabled() ? 1 : 0;
		int borderID = 0;
		if (inRect(x,y, buttonRect)) {
			borderID = 1;			
		}

		drawImage(gui,buttonRect, 0, buttonRect[3] * borderID);

		int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
		drawImage(gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
	}

	private int[] buttonRect = new int[] {15,30, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		super.drawMouseOver(gui,x, y);
		drawStringOnMouseOver(gui, getStateName(), x,y,buttonRect);
	}	
	
	private String getStateName() {
        return Localization.MODULES.TOOLS.TOGGLE.translate(isDrillEnabled() ? "1" : "0");
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		super.save(tagCompound);
		tagCompound.setBoolean("DrillEnabled", isDrillEnabled());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		super.load(tagCompound);
		setDrillEnabled(tagCompound.getBoolean("DrillEnabled"));
	}	

	public void doActivate(int id) {
		setDrillEnabled(true);
	}
	public void doDeActivate(int id) {
		setDrillEnabled(false);
	}
	public boolean isActive(int id) {
		return isDrillEnabled();
	}	
}