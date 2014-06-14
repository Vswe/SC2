package vswe.stevescarts.Modules.Workers.Tools;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleDrillIntelligence;
import vswe.stevescarts.Modules.Addons.ModuleIncinerator;
import vswe.stevescarts.Modules.Addons.ModuleLiquidSensors;
import vswe.stevescarts.Modules.Addons.ModuleOreTracker;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public abstract class ModuleDrill extends ModuleTool implements IActivatorModule {
	public ModuleDrill(MinecartModular cart) {
		super(cart);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 50;
	}


	
	
	private ModuleDrillIntelligence intelligence;
	private ModuleLiquidSensors liquidsensors;
	private ModuleOreTracker tracker;
	@Override
	public void init() {
		super.init();
		for (ModuleBase module : getCart().getModules()) {
			if (module instanceof ModuleDrillIntelligence) {
				intelligence = (ModuleDrillIntelligence)module;
			}
			if (module instanceof ModuleLiquidSensors) {
				liquidsensors = (ModuleLiquidSensors)module;
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
		
		if (isBroken()) {
			return false;
		}
	
        //get the next block so the cart knows where to mine
        Vec3 next = getNextblock();
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
				if (intelligence != null && !intelligence.isActive(holeX + blocksOnSide(), holeY, range[2], x > getCart().x() || z < getCart().z())) {
					continue;
				}
			
                //calculate the coordinates of this "hole"
                int coordX = x + (getCart().z() != z ? holeX : 0);
                int coordY = y + holeY;
                int coordZ = z + (getCart().x() != x ? holeX : 0);

				
				
                if (mineBlockAndRevive(coordX, coordY, coordZ, next, holeX, holeY)) {
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
    protected int[] mineRange()
    {
        //the first element is the start index, the last is the end index
        //get the next block
        Vec3 next = getNextblock();
        //save the next block's coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

		int yTarget = getCart().getYTarget();

        if (BlockRailBase.isRailBlockAt(getCart().worldObj, x, y, z) || BlockRailBase.isRailBlockAt(getCart().worldObj, x, y - 1, z))
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
	
	
	private boolean mineBlockAndRevive(int coordX, int coordY, int coordZ, Vec3 next, int holeX, int holeY) {
		if (mineBlock(coordX, coordY, coordZ, next, holeX, holeY, false)) {
			return true;
		}else if(isDead()) {
			revive();
			return true;
		}else{
			return false;
		}
	}	
	

	
    protected boolean mineBlock(int coordX, int coordY, int coordZ, Vec3 next, int holeX, int holeY, boolean flag)
    {
    	if (tracker != null) {
    		BlockCoord start = new BlockCoord(coordX, coordY, coordZ);
    		BlockCoord target = tracker.findBlockToMine(this, start);
    		if (target != null) {
	    		coordX = target.getX();
	    		coordY = target.getY();
	    		coordZ = target.getZ();
    		}
    	}
    	
        //test whether this block is valid or not
        Object valid = isValidBlock(coordX, coordY, coordZ, holeX, holeY, flag);
        TileEntity storage = null;

        if (valid instanceof TileEntity)
        {
            //if the test code found the block to be valid but it returned a TileEntity it means we have to remove all items inside its inventory too(therefore save the TileEntity)
            storage = (TileEntity)valid;
        }
        else if (valid == null)
        {
            //if this block wasn't valid, try the next one
            return false;
        }

        //retrieve some information about the block
        int id = getCart().worldObj.getBlockId(coordX, coordY, coordZ);
        Block b = Block.blocksList[id];
        int m = getCart().worldObj.getBlockMetadata(coordX, coordY, coordZ);

        //if the cart hasn't been working, tell it to work and it's therefore done for this tick
        //if (doPreWork())
        //{
            //calculate the working time
            float h = b.getBlockHardness(getCart().worldObj,coordX,coordY,coordZ);

            if (h < 0)
            {
                h = 0;
            }

			//CrumbleBlocksHook.startCrumblingBlock(this, coordX, coordY, coordZ, workingTime);
            //return true;
        //}

        //if the cart is already working, the code below should be run
        ItemStack iStack;

        //if the block is a block with an inventory, empty it first
        if (storage != null)
        {
            //loop through the inventory
            for (int i = 0; i < ((IInventory)(storage)).getSizeInventory(); i++)
            {
                //pick out an item from the next slot
                iStack = ((IInventory)(storage)).getStackInSlot(i);

                //check if there was anything there
                if (iStack == null)
                {
                    continue;
                }

                //let the cart do its thing with the item, on success remove it from the inventory.
                if (minedItem(iStack, next))
                {
                    ((IInventory)(storage)).setInventorySlotContents(i, null);
                }
                else
                {
                    /*turnback();
					stopWorking();
                    return false;*/
					return false;
                }
            }
        }

        int fortune = enchanter != null ? enchanter.getFortuneLevel() : 0;
        

        	
        if (shouldSilkTouch(b, coordX, coordY, coordZ, m)) {
        	ItemStack item = getSilkTouchedItem(b, m);
        	
        	if (item == null || minedItem(item, next)) {
        		getCart().worldObj.setBlockToAir(coordX, coordY, coordZ);
        	}else{
        		return false;
        	}                  

        //if the block drops anything, drop it
        }else if (b.getBlockDropped(getCart().worldObj,coordX,coordY,coordZ,m,fortune).size() != 0) {       
            //iStack = new ItemStack(b.idDropped(0, rand, 0), b.quantityDropped(rand), b.damageDropped(m));
			ArrayList<ItemStack> stacks = b.getBlockDropped(getCart().worldObj,coordX,coordY,coordZ,m,fortune);
            boolean shouldRemove = false;
			for (int i = 0; i < stacks.size(); i++) {
				if (minedItem(stacks.get(i), next))
				{
					shouldRemove = true;
				}else{
					/*turnback();
					stopWorking();
					return false
					*/
					return false;
				}
			}
			if (shouldRemove) {
				//mark this cart as idle and remove the block
				try{
					//worldObj.playAuxSFX(2001, coordX, coordY, coordZ, id + m * 256);
				}catch(Exception e) {
				}
				getCart().worldObj.setBlockToAir(coordX, coordY, coordZ);
			}
        }
        else
        {
            //mark this cart as idle and remove the block
			try{
				//worldObj.playAuxSFX(2001, coordX, coordY, coordZ, id + m * 256);
			}catch(Exception e) {
			}
            getCart().worldObj.setBlockToAir(coordX, coordY, coordZ);
        }

        damageTool(1 + (int)h);
		startWorking(getTimeToMine(h));
		startDrill();
        return true;
    }

    /**
    Let the cart handle a mined item, return true upon success.
    **/
    protected boolean minedItem(ItemStack iStack, Vec3 Coords)
    {
		if (iStack == null || iStack.stackSize <= 0) {
			return true;
		}
	
		for (ModuleBase module : getCart().getModules()) {
			if (module instanceof ModuleIncinerator) {
				((ModuleIncinerator)module).incinerate(iStack);
				if (iStack.stackSize <= 0) {
					return true;
				}
			}
		}      
		
		int size = iStack.stackSize;
        getCart().addItemToChest(iStack);

        if (iStack.stackSize == 0)
        {
            //everything worked fine
            return true;
        }else {
			boolean hasChest = false;
			for (ModuleBase module : getCart().getModules()) {
				if (module instanceof ModuleChest) {
					hasChest = true;
					break;
				}
			}

			if (hasChest) {
				if (iStack.stackSize != size) {
					//if only some items did fit in the chest we have no other choice than spitting out the rest
					//but don't do it the normal way, that woul only make the Miningcart w/ chest get stuck and
					//the whole point with it is to avoid that, spit it out to its side instead
					EntityItem entityitem = new EntityItem(getCart().worldObj, getCart().posX, getCart().posY, getCart().posZ , iStack);
					//observe that the motion for X uses the Z coords and vice-versa
					entityitem.motionX = (float)(getCart().z() - Coords.zCoord) / 10;
					entityitem.motionY = 0.15F;
					entityitem.motionZ = (float)(getCart().x() - Coords.xCoord) / 10;
					getCart().worldObj.spawnEntityInWorld(entityitem);
					return true;
				}else{
					 //let's get out of here!
					return false;
				}
			}else{
			     //pop out the item out of the cart's back
				EntityItem entityitem = new EntityItem(getCart().worldObj, getCart().posX, getCart().posY, getCart().posZ , iStack);
				entityitem.motionX = (float)(getCart().x() - Coords.xCoord) / 10;
				entityitem.motionY = 0.15F;
				entityitem.motionZ = (float)(getCart().z() - Coords.zCoord) / 10;
				getCart().worldObj.spawnEntityInWorld(entityitem);
				return true;
			}
		}
    }

    private int getTimeToMine(float hardness)
    {
    	int efficiency = enchanter != null ? enchanter.getEfficiencyLevel() : 0;
        return (int)((getTimeMult() * hardness) / Math.pow(1.3F, efficiency)) + (liquidsensors != null ? 2 : 0);
    }

	protected abstract float getTimeMult();

    /**
    Method to check if a block is a valid block to remove by the miner

    x, y and z is the coordinates of the block while i and j is offset location compared to the minecart

    if this returns null the block is not valid, all other values are valid.
    **/
    public Object isValidBlock(int x, int y, int z, int i, int j, boolean flag)
    {
        //do not remove rail blocks or block which will cause rail blocks to be removed
        if ((!flag && BlockRailBase.isRailBlockAt(getCart().worldObj, x, y, z)) || BlockRailBase.isRailBlockAt(getCart().worldObj, x, y + 1, z))
        {
            return null;
        }
        else
        {
            //retrieve the needed values, block id and the like
            int id = getCart().worldObj.getBlockId(x, y, z);
            Block b = Block.blocksList[id];
            int m = getCart().worldObj.getBlockMetadata(x, y, z);

            //there need to be a block to remove
            if (b == null)
            {
                return null;
                //don't remove bedrock
            }
            else if (id == Block.bedrock.blockID)
            {
                return null;
                //don't remove fluids either
            }
            else if (b instanceof BlockFluid)
            {
                return null;
                //nor things which can't be removed
            }
            else if (b.getBlockHardness(getCart().worldObj,x,y,z) < 0)
            {
                return null;
                //some special things are just allowed to be removed when in font of the cart, like torches
            }
            else if ((i != 0 || j > 0) && (
                    id == Block.torchWood.blockID ||
                    id == Block.redstoneWire.blockID ||
                    id == Block.torchRedstoneActive.blockID ||
                    id == Block.torchRedstoneIdle.blockID ||
                    id == Block.redstoneRepeaterIdle.blockID ||
                    id == Block.redstoneRepeaterActive.blockID ||
					id == ModBlocks.MODULE_TOGGLER.getId()
                    ))
            {
                return null;
                //for containers like chest a special rule apply, therefore test if this is a container
            }
            else if (b instanceof BlockContainer)
            {
                //if so load its tileentity to check if it has an inventory or not
                TileEntity tileentity = getCart().worldObj.getBlockTileEntity(x, y, z);

                if (tileentity != null && IInventory.class.isInstance(tileentity))
                {
                    //depending on its position it's either invalid or we should return the tileentity to be able to remove its items
                    if (i != 0 || j > 0)
                    {
                        return null;
                    }
                    else
                    {
                        return tileentity;
                    }
                }
            }

			
			if (liquidsensors != null) {
				//check all five directions for danger(no need to check below since liquids can't flow upwards ^^)
				if (liquidsensors.isDangerous(this, x, y, z, 0, +1, 0) ||
						liquidsensors.isDangerous(this, x, y, z, +1, 0, 0) ||
						liquidsensors.isDangerous(this, x, y, z, -1, 0, 0) ||
						liquidsensors.isDangerous(this, x, y, z, 0, 0, +1) ||
						liquidsensors.isDangerous(this, x, y, z, 0, 0, -1))
				{
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
    public void update()
    {
        super.update();

        if ((getCart().hasFuel() && isMining()) || miningCoolDown < 10)
        {
			drillRotation = (float)((drillRotation + 0.03F * (10 - miningCoolDown)) % (Math.PI * 2));
			if (isMining()) {
				miningCoolDown = 0;
				//CrumbleBlocksHook.updateCrumblingBlock(this, workingTime);
			}else{
				//CrumbleBlocksHook.doneCrumblingBlock(this);
				miningCoolDown++;
			}
        }
        else
        {
            //CrumbleBlocksHook.doneCrumblingBlock(this);
        }

		if (!getCart().worldObj.isRemote && liquidsensors != null) {
			byte data = sensorLight;
			if (isDrillSpinning()) {
				data |= 4;
			}
			liquidsensors.getInfoFromDrill(data);
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

	private byte sensorLight  = (byte)1;;
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
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
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
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.TOOLS.DRILL.translate(), 8, 6, 0x404040);
	}


	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
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
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		super.drawMouseOver(gui,x, y);
		drawStringOnMouseOver(gui, getStateName(), x,y,buttonRect);
	}	
	
	private String getStateName() {
        return Localization.MODULES.TOOLS.TOGGLE.translate(isDrillEnabled() ? "1" : "0");
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);
		tagCompound.setBoolean(generateNBTName("DrillEnabled",id), isDrillEnabled());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		setDrillEnabled(tagCompound.getBoolean(generateNBTName("DrillEnabled",id)));
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