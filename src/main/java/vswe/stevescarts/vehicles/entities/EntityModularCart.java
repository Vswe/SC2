package vswe.stevescarts.vehicles.entities;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cpw.mods.fml.relauncher.ReflectionHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import vswe.stevescarts.old.Blocks.ModBlocks;
import vswe.stevescarts.old.Items.ModItems;
import vswe.stevescarts.old.StevesCarts;
import vswe.stevescarts.old.Containers.ContainerMinecart;
import vswe.stevescarts.old.Helpers.ActivatorOption;
import vswe.stevescarts.old.Helpers.CartVersion;
import vswe.stevescarts.old.Helpers.CompWorkModule;
import vswe.stevescarts.old.Helpers.DataWatcherLockable;
import vswe.stevescarts.old.Helpers.DetectorType;
import vswe.stevescarts.old.Helpers.GuiAllocationHelper;
import vswe.stevescarts.old.Helpers.ModuleCountPair;
import vswe.stevescarts.old.Helpers.TransferHandler;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Models.Cart.ModelCartbase;
import vswe.stevescarts.old.ModuleData.ModuleData;
import vswe.stevescarts.old.Modules.IActivatorModule;
import vswe.stevescarts.old.Modules.ModuleBase;
import vswe.stevescarts.old.Modules.Addons.ModuleCreativeSupplies;
import vswe.stevescarts.old.Modules.Engines.ModuleEngine;
import vswe.stevescarts.old.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.old.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.old.Modules.Workers.ModuleWorker;
import vswe.stevescarts.old.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.old.TileEntities.TileEntityCartAssembler;


import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevescarts.vehicles.VehicleBase;

/**
 * The modular minecart class, this is the cart. This is what controls all modules whereas 
 * the modules({@link ModuleBase} handles specific tasks. The cart the player can see in-game 
 * is a combination of the cart and its modules.
 * @author Vswe
 */
public class EntityModularCart extends EntityMinecart
    implements IVehicleEntity
{

	private VehicleBase vehicleBase;
    public int disabledX;
    public int disabledY;
    public int disabledZ;
    protected boolean wasDisabled;
    public double pushX;
    public double pushZ;		
    public double temppushX;
    public double temppushZ;
    protected boolean engineFlag = false;
    private int motorRotation;
	public boolean cornerFlip;
	private int fixedMeta = -1;
	private int fixedMX;
	private int fixedMY;
	private int fixedMZ;

	private int wrongRender;
	private boolean oldRender;
	private	float lastRenderYaw;
	private double lastMotionX;
	private double lastMotionZ;
	private int workingTime;

	public static final int MODULAR_SPACE_WIDTH = 443;
	public static final int MODULAR_SPACE_HEIGHT = 168;

	
	/**
	 * Information about how trails turn
	 */
	public static final int[][][] railDirectionCoordinates = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};



	
	public Random rand = new Random();


    @Override
    public VehicleBase getVehicle() {
        return vehicleBase;
    }

    /**
	 * Creates a cart in the world, this is used when the cart is spawned by a player(or something else)
	 * on the server side.
	 * @param world The world the cart is placed in
	 * @param x The X coordinate the cart is placed at
	 * @param y The Y coordinate the cart is placed at
	 * @param z The Z coordinate the cart is placed at
	 * @param info The tag compound with mostly the cart's modules.
	 * @param name The name of this cart
	 */
    public EntityModularCart(World world, double x, double y, double z, NBTTagCompound info, String name)
    {
        super(world, x, y, z);
        this.vehicleBase = new VehicleBase(this, info, name);
		overrideDatawatcher();
    }
	
	
    /**
     * Creates a cart in the world. This is used when a cart is loaded on the server, or when a cart is created on the client.
     * @param world The world the cart is created in
     */
    public EntityModularCart(World world)
    {
        super(world);
        this.vehicleBase = new VehicleBase(this);
		overrideDatawatcher();
    }
	
	/**
	 * Creates a PlaceHolder cart, 
	 * @param world The world this cart is created in
	 * @param assembler The CartAssembler this placeholder cart belongs to
	 * @param data The byte array containing the modules of this cart
	 */
    public EntityModularCart(World world, TileEntityCartAssembler assembler, byte[] data)
    {
        super(world);
        this.vehicleBase = new VehicleBase(this, assembler, data);
        overrideDatawatcher();
    }	
	
    /**
     * The normal datawatcher is overridden by a special one on the client side. This is to be able
     * to wait to process data in the beginning. Fixing the syncing at start up.
     */
	private void overrideDatawatcher() {
		if (worldObj.isRemote) {
			this.dataWatcher = new DataWatcherLockable(this);

			this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
			this.dataWatcher.addObject(1, Short.valueOf((short)300));
			
			this.entityInit();
		}
	}




    /**
     * Is called when the Entity is killed
     */
    @Override
    public void setDead() {
        vehicleBase.preDeath();
        super.setDead();
        vehicleBase.postDeath();
    }	
	
	/**
	 * Allows the modules to render overlays on the screen
	 * @param minecraft
	 */
	@SideOnly(Side.CLIENT)
	public void renderOverlay(Minecraft minecraft) {
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.renderOverlay(minecraft);
			}
		}		
	}
	
	/**
	 * Initiates the entity, used for initiating data watchers.
	 */
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }	
	

    
	/**
	 * Get the engine's state, if it's on or off.
	 * This should not be used to determine if a module
	 * that requires power should run or not.
	 * @return
	 */
	public boolean isEngineBurning() {
		return isCartFlag(0);
	}
	
	/**
	 * Set the engine's state, if it's on or off.
	 * @param on The state of the engine
	 */
    public void setEngineBurning(boolean on)
    {
		setCartFlag(0, on);
    }	
	
    /**
     * Returns one of up to 8 flags about the cart that is synchronized between the client and the server
     * @param id The Flag's id
     * @return If the flag is set or not
     */
	private boolean isCartFlag(int id) {
		return (dataWatcher.getWatchableObjectByte(16) & (1 << id)) != 0;
	}
	
	/**
	 * Sets one of up to 8 flags about the cart that is synchronized between the client and the server
	 * @param id The Flag's id
	 * @param val The new valie pf the flag
	 */
	private void setCartFlag(int id, boolean val) {
		if (worldObj.isRemote) {
			return;
		}
	
		byte data = (byte)((dataWatcher.getWatchableObjectByte(16) & ~(1 << id)) | ((val ? 1 : 0) << id));
	
		dataWatcher.updateObject(16, data);
	}
	
	


	
	/**
	 * Get the "Eye height" of the cart
	 */
	@Override
	public float getEyeHeight()
    {
        return 0.9F;
    }	
	
	/**
	 * Get the offset the riding entity should be rendered at
	 */
	@Override
    public double getMountedYOffset()
    {
		if (modules != null && riddenByEntity != null) {
			for (ModuleBase module : modules) {
				float offset = module.mountedOffset(riddenByEntity);
				if (offset != 0) {
					return offset;
				}
			}
		}	
		return super.getMountedYOffset();
    }	
	
	/**
	 * Get this cart as an item. Used when breaking the cart or "picking"(middle mouse button) it for instance.
	 */
	@Override
    public ItemStack getCartItem()
    {
		if (modules != null) {
			ItemStack cart = ModuleData.createModularCart(this);	
			if (name != null && !name.equals("") && !name.equals(ModItems.carts.getName())) {
				cart.setStackDisplayName(name);
			}

			return cart;
		}else{
			return new ItemStack(ModItems.carts);
		}
    }

	/**
	 * When the cart 
	 */
	@Override
	public void killMinecart(DamageSource dmg) {
		this.setDead();
		if (dropOnDeath()) {
			this.entityDropItem(getCartItem(), 0.0F);
			
		   for (int i = 0; i < this.getSizeInventory(); ++i)
			{
				ItemStack itemstack = this.getStackInSlot(i);

				if (itemstack != null)
				{
					float f = this.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0)
					{
						int j = this.rand.nextInt(21) + 10;

						if (j > itemstack.stackSize)
						{
							j = itemstack.stackSize;
						}

						itemstack.stackSize -= j;
						EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double)f, this.posY + (double)f1, this.posZ + (double)f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
						entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
						this.worldObj.spawnEntityInWorld(entityitem);
					}
				}
			}			
		}
	}
	

	



	/**
	 * Get the max speed this cart can move at
	 */
	@Override
    public float getMaxCartSpeedOnRail()
    {
		//the calculated maximum speed
		float maxSpeed = super.getMaxCartSpeedOnRail(); 
		if (modules != null) {
			for (ModuleBase module : modules) {
				float tempMax = module.getMaxSpeed();
				if (tempMax < maxSpeed) {
					maxSpeed = tempMax;
				}
			}	
		}		
        return maxSpeed;
    }

    /**
     * Returns if this cart can be powered
     */
	@Override
    public boolean isPoweredCart()
    {
        return engineModules.size() > 0;
    }
	
	@Override
	public int getDefaultDisplayTileData() {
		return -1;
	}



	@Override
	public int getMinecartType() {
		return -1;
	}
		
	

	
	
	/**
	 * Return the Y level in the world the cart is aiming for
	 * @return The target level
	 */
	public int getYTarget() {
		if (modules != null) {
			for (ModuleBase module : getModules()) {
				int yTarget = module.getYTarget();
				if (yTarget != -1) {
					return yTarget;
				}
			}
		}
		
		return (int)posY;
	}
	

	
	
	/**
	 * When the cart takes damage
	 */
	@Override
	public boolean attackEntityFrom(DamageSource dmg, float par2)
    {
		if (isPlaceholder) {
			return false;
		}
	
		if (modules != null) {
			for (ModuleBase module : getModules()) {
				if (!module.receiveDamage(dmg, par2)) {
					return false;
				}
			}
		}
	
		return super.attackEntityFrom(dmg,par2);		
	}
	
	/**
	 * When the cart passes a activator rail
	 * @param x The X coordinate of the rail
	 * @param y The Y coordinate of the rail
	 * @param z The Z coordinate of the rail
	 * @param active If the rail is active or not
	 */
	@Override
	public void onActivatorRailPass(int x, int y, int z, boolean active) {
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.activatedByRail(x,y,z, active);
			}
		}		
	}
	
	

	@Override
	/**
	 * Called when the cart is moving on top of a rail
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param z The Z coordinate
	 * @param acceleration seems like the acceleration
	 */
    public void moveMinecartOnRail(int x, int y, int z, double acceleration/*?*/)
    {
        super.moveMinecartOnRail(x, y, z, acceleration);
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.moveMinecartOnRail(x,y,z);
			}
		}
		
        Block block = worldObj.getBlock(x, y, z);
		Block blockBelow = worldObj.getBlock(x, y - 1, z);
		int metaBelow = worldObj.getBlockMetadata(x, y-1, z);
		int m = ((BlockRailBase)block).getBasicRailMetadata(worldObj, this, x, y, z);
		
	
		if (((m == 6 || m == 7) && motionX < 0) || ((m == 8 || m == 9) && motionX > 0)) {
			cornerFlip = true;
		}else{
			cornerFlip = false;
		}
		
       if (block != ModBlocks.ADVANCED_DETECTOR.getBlock() && isDisabled())
        {
            releaseCart();
        }

        boolean canBeDisabled = block == ModBlocks.ADVANCED_DETECTOR.getBlock() && (blockBelow != ModBlocks.DETECTOR_UNIT.getBlock() || !DetectorType.getTypeFromMeta(metaBelow).canInteractWithCart() || DetectorType.getTypeFromMeta(metaBelow).shouldStopCart());
        boolean forceUnDisable = (wasDisabled && disabledX == x && disabledY == y && disabledZ == z);

        if (!forceUnDisable && wasDisabled)
        {
            wasDisabled = false;
        }

        canBeDisabled = forceUnDisable ? false : canBeDisabled;

        if (canBeDisabled && !isDisabled())
        {
            setIsDisabled(true);

            if (pushX != 0 || pushZ != 0)
            {
                temppushX = pushX;
                temppushZ = pushZ;
                pushX = pushZ = 0;
            }

            disabledX = x;
            disabledY = y;
            disabledZ = z;
        }
		
		
		if (fixedMX != x || fixedMY != y || fixedMZ != z)
        {
            fixedMeta = -1;
			fixedMY = -1;
        }
    }

	/**
	 * Allows the cart to override the rail's meta data when traveling
	 * @param x The X coordinate of the rail
	 * @param y The Y coordinate of the rail
	 * @param z The Z coordinate of the rail
	 * @return The meta data of the rail
	 */
	public int getRailMeta(int x, int y, int z) {		
		ModuleBase.RAILDIRECTION dir = ModuleBase.RAILDIRECTION.DEFAULT;
		for (ModuleBase module : getModules()) {
			dir = module.getSpecialRailDirection(x,y,z);
			if (dir != ModuleBase.RAILDIRECTION.DEFAULT) {
				break;
			}
		}
	
		if (dir == ModuleBase.RAILDIRECTION.DEFAULT)  {
			return -1;
		}
	
		int Yaw = (int)(rotationYaw % 180);

        if (Yaw < 0)
        {
            Yaw += 180 ;
        }

        boolean flag = Yaw >= 45 && Yaw <= 135;
		
        if (fixedMeta == -1)
        {		
			switch (dir) {
				case FORWARD: 
					if (flag)
					{
						fixedMeta = 0;
					}
					else
					{
						fixedMeta = 1;
					}				
					break;
				case LEFT:
					if (flag)
					{
						if (motionZ > 0)
						{
							fixedMeta = 9;
						}
						else if (motionZ <= 0)
						{
							fixedMeta = 7;
						}
					}
					else
					{
						if (motionX > 0)
						{
							fixedMeta = 8;
						}
						else if (motionX < 0)
						{
							fixedMeta = 6;
						}
					}	
					break;
				case RIGHT:					
					if (flag)
					{
						if (motionZ > 0)
						{
							fixedMeta = 8;
						}
						else if (motionZ <= 0)
						{
							fixedMeta = 6;
						}
					}
					else
					{
						if (motionX > 0)
						{
							fixedMeta = 7;
						}
						else if (motionX < 0)
						{
							fixedMeta = 9;
						}
					}
					break;
					
					//doesn't work
				case NORTH: 
					if (flag)
					{
						if (motionZ > 0)
						{
							fixedMeta = 0;
						}
						
					}else {
						if (motionX > 0)
						{
							fixedMeta = 7;
						}else if (motionX < 0) {
							fixedMeta = 6;
						}
					}
					
					break;
				default:
					fixedMeta = -1;
			}

            if (fixedMeta == -1)
            {
                return -1;
            }

            fixedMX = x;
            fixedMY = y;
            fixedMZ = z;
        }

        return fixedMeta;			
	}
	
	/**
	 * Reset the modified meta data
	 */
	public void resetRailDirection() {
		fixedMeta = -1;
	}

    /**
    Turn the cart around
    **/
    public void turnback()
    {
        pushX *= -1;
        pushZ *= -1;
        temppushX *= -1;
        temppushZ *= -1;
        motionX *= -1;
        motionY *= -1;
        motionZ *= -1;
    }

    /**
     * Allows the cart to move again after being disabled
     */
    public void releaseCart()
    {
        wasDisabled = true;
        setIsDisabled(false);
        pushX = temppushX;
        pushZ = temppushZ;
    }

	

    /**
     * Lets the modules know when the inventory of the cart has been changed
     */
	@Override
	public void markDirty() {
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.onInventoryChanged();
			}
		}
	}




    /**
     Returns the number of slots in this cart
    **/
	@Override
    public int getSizeInventory()
    {
		int slotCount = 0;
		if (modules != null) {
			for (ModuleBase module : modules) {
				slotCount += module.getInventorySize();
			}
		}else{
			//slotCount = 100;
		}
	
        return slotCount;
    }

	
	


	@Override
    protected void func_145821_a(int par1, int par2, int par3, double par4, double par6, Block par8, int par9)
    {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
            float move = ((EntityLivingBase)this.riddenByEntity).moveForward;
            ((EntityLivingBase)this.riddenByEntity).moveForward = 0;
            super.func_145821_a(par1, par2, par3, par4, par6, par8, par9);
            ((EntityLivingBase)this.riddenByEntity).moveForward = move;
        }else{
            super.func_145821_a(par1, par2, par3, par4, par6, par8, par9);
        }

        double d2 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d2 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D)
        {
            d2 = (double)MathHelper.sqrt_double(d2);
            this.pushX /= d2;
            this.pushZ /= d2;

            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D)
            {
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            }
            else
            {
                this.pushX = this.motionX;
                this.pushZ = this.motionZ;
            }
        }
    }

	@Override
    protected void applyDrag()
    {
	
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
		engineFlag = d0 > 1.0E-4D;
		if (isDisabled()) {
			motionX = 0;
			motionY = 0;
			motionZ = 0;
        }else if (engineFlag) {
            d0 = (double)MathHelper.sqrt_double(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = getPushFactor();
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            this.motionX += this.pushX * d1;
            this.motionZ += this.pushZ * d1;
        }else{
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

	/**
	 * Get the factor this cart will push itself with when powered
	 * @return
	 */
    protected double getPushFactor()
    {
		if (modules != null) {
			for (ModuleBase module : modules) {
				double factor = module.getPushFactor();
				if (factor >= 0) {
					return factor;
				}							
			}
		}
	
        return 0.05D;
    }
	
    /**
     * Saves data of the cart, also allows all modules to save their data
     */
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
    {
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("cartName", name);

        tagCompound.setDouble("pushX", pushX);
        tagCompound.setDouble("pushZ", pushZ);		
		tagCompound.setDouble("temppushX", temppushX);
        tagCompound.setDouble("temppushZ", temppushZ);
		
		tagCompound.setShort("workingTime", (short)workingTime);	
			
		tagCompound.setByteArray("Modules", moduleLoadingData);
		
		tagCompound.setByte("CartVersion", cartVersion);
		
		if (modules != null) {
			for (int i = 0; i < modules.size(); i++) {
				ModuleBase module = modules.get(i);
				module.writeToNBT(tagCompound,i);
			}
		}
	}
	
	
	/**
	 * Loads the data of the cart, also allows the modules to load their data
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
    {
		super.readFromNBT(tagCompound);

        name = tagCompound.getString("cartName");
		
        pushX = tagCompound.getDouble("pushX");
        pushZ = tagCompound.getDouble("pushZ");	
        temppushX = tagCompound.getDouble("temppushX");
        temppushZ = tagCompound.getDouble("temppushZ");	
		
		workingTime = tagCompound.getShort("workingTime");	

		cartVersion = tagCompound.getByte("CartVersion");
		
		int oldVersion = cartVersion;
		

		
		loadModules(tagCompound);
			
		if (modules != null) {
			for (int i = 0; i < modules.size(); i++) {
				ModuleBase module = modules.get(i);
				module.readFromNBT(tagCompound, i);
			
			}
		}	
		
		
		
		if (oldVersion < 2) {
			int newSlot = -1;
			int slotCount = 0;
			for (ModuleBase module : modules) {
				if (module instanceof ModuleTool) {
					newSlot = slotCount;
					break;
				}else{
					slotCount += module.getInventorySize();
				}
			}
			if (newSlot != -1) {
				ItemStack lastitem = null;
				for (int i = newSlot; i < getSizeInventory(); i++) {
					ItemStack thisitem = getStackInSlot(i);
					setInventorySlotContents(i, lastitem);
					lastitem = thisitem;
				}
			}
		}		
		
	}
	

	/**
	 * Gets if a cart has been disabled by an ADR
	 * @return If it's disabled
	 */
	public boolean isDisabled() {
		return isCartFlag(1);
	}
	
	/**
	 * Sets if a cart has been disabled by an ADR
	 * @param disabled If it's disabled
	 */
	public void setIsDisabled(boolean disabled) {
		setCartFlag(1, disabled);
	}
	
	


	@Override
    public void onUpdate()
    {
		super.onUpdate();

		
		onCartUpdate();
        if (worldObj.isRemote) {
            updateSounds();
        }
	}	



	/**
	 * Returns if this inventory(the cart) is usuable by the specific player
	 */
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
       return entityplayer.getDistanceSq(x(), y(), z()) <= 64D;
    }	
	
    /**
     A method to be called when this cart is activated by the player
    **/
	@Override
    public boolean interactFirst(EntityPlayer entityplayer)
    {
		if (isPlaceholder) {
			return false;
		}
		

		if (modules != null && !entityplayer.isSneaking()) {
			boolean interupt = false;
			for (ModuleBase module : modules) {
				if (module.onInteractFirst(entityplayer)) {
					interupt = true;
				}
			}
			if (interupt) {
				return true;
			}
		}
		
		if (!worldObj.isRemote) {
	        //Saves which direction the player activated the cart from
	        if (!isDisabled() && riddenByEntity != entityplayer)
	        {
	            temppushX = posX - entityplayer.posX;
	            temppushZ = posZ - entityplayer.posZ;
	        }
	
	        //if the cart can move, start it in the desired direction.
	        if (!isDisabled() && hasFuel() && pushX == 0 && pushZ == 0)
	        {
	            pushX = temppushX;
	            pushZ = temppushZ;
	        }
	
	        
	        FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 0, worldObj, getEntityId(), 0, 0);
			openInventory();
		}
		
        return true;
    }

	

	



	
	public boolean getRenderFlippedYaw(float yaw) {		
		yaw = yaw % 360;
		if (yaw < 0) {
			yaw += 360;
		}
	
	
		if (!oldRender || Math.abs(yaw - lastRenderYaw) < 90 || Math.abs(yaw - lastRenderYaw) > 270 || (motionX > 0 && lastMotionX < 0) || (motionZ > 0 && lastMotionZ < 0) || (motionX < 0 && lastMotionX > 0) || (motionZ < 0 && lastMotionZ > 0) || wrongRender >= 50) {
			lastMotionX = motionX;
			lastMotionZ = motionZ;
			lastRenderYaw = yaw;
			oldRender = true;
			wrongRender = 0;
			return false;
		}else{
			wrongRender++;
			return true;		
		}

	}
	


	
	/**
	 * The x coordinate of the cart
	 * @return The x coordinate
	 */
    public int x()
    {
        return (int)MathHelper.floor_double(posX);
    }
    /**
     * The y coordinate of the cart
     * @return The y coordinate
     */
    public int y()
    {
        return (int)MathHelper.floor_double(posY);
    }
    /**
     * The z coordinate of the cart
     * @return The y coordinate
     */
    public int z()
    {
        return (int)MathHelper.floor_double(posZ);
    }







	
 
	
	/**
	 * Returns an item in a slot
	 */
	 @Override
    public ItemStack getStackInSlot(int i)
    {
		if (modules != null) {
			for(ModuleBase module : modules) {
				if (i < module.getInventorySize()) {
					return module.getStack(i);
				}else{
					i -= module.getInventorySize();
				}
			}
		}
	
        return null;
    }
	
	
    /**
     * Sets the given item stack to the specified slot in the inventory
     */
	 @Override
    public void setInventorySlotContents(int i, ItemStack item)
    {
		if (modules != null) {
			for(ModuleBase module : modules) {
				if (i < module.getInventorySize()) {
					module.setStack(i,item);
					break;
				}else{
					i -= module.getInventorySize();
				}
			}	
		}
    }	
	
	    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
	 @Override
    public ItemStack decrStackSize(int i, int n)
    {
		if (modules == null) {
			return null;
		}
	
        if (this.getStackInSlot(i) != null)
        {
            ItemStack item;

            if (this.getStackInSlot(i).stackSize <= n)
            {
                item = this.getStackInSlot(i);
                this.setInventorySlotContents(i,null);
                return item;
            }
            else
            {
                item = this.getStackInSlot(i).splitStack(n);

                if (this.getStackInSlot(i).stackSize == 0)
                {
                    this.setInventorySlotContents(i,null);
                }

                return item;
            }
        }
        else
        {
            return null;
        }
    }
	@Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.getStackInSlot(i) != null)
        {
            ItemStack var2 = this.getStackInSlot(i);
            this.setInventorySlotContents(i,null);
            return var2;
        }
        else
        {
            return null;
        }
    }	




	
	/**
	 * Called when a player interacts with the cart to open any chests
	 */
    @Override
    public void openInventory()
    {
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (module instanceof ModuleChest) {
					((ModuleChest)module).openChest();
				}
			}
		}
    }

    /**
     * Called when a player stops interacting with the cart to close any chests
     */
    @Override
    public void closeInventory()
    {
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (module instanceof ModuleChest) {
					((ModuleChest)module).closeChest();
				}
			}
		}
    }	



	


	/**
	 * Returns the bounding box of the cart.
	 */
	@Override
	public AxisAlignedBB getBoundingBox()
    {	
		if (isPlaceholder) {
			return null;
		}else{
			return super.getBoundingBox();
		}
    }
	
	/**
	 * Returns whether something can collide with the cart or not.
	 */
	@Override
	public boolean canBeCollidedWith()
    {
		if (isPlaceholder) {
			return false;
		}else{
			return super.canBeCollidedWith();
		}
    }
	
	/**
	 * Returns whether the cart can be pushed or not
	 */
	@Override
	public boolean canBePushed()
    {
		if (isPlaceholder) {
			return false;
		}else{
			return super.canBePushed();
		}
    }	
	

	

    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param data The packet data stream
     */
	 @Override
    public void writeSpawnData(ByteBuf data) {
		data.writeByte(moduleLoadingData.length);
		for (byte b : moduleLoadingData) {
			data.writeByte(b);
		}
		
		data.writeByte(name.getBytes().length);
        for (byte b : name.getBytes()) {
            data.writeByte(b);
        }
	}

    /**
     * Called by the client when it receives a Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param data The packet data stream
     */
	 @Override
    public void readSpawnData(ByteBuf data) {
		byte length = data.readByte();
		byte[] bytes  = new byte[length];
		data.readBytes(bytes);
		loadModules(bytes);
		
		int nameLength = data.readByte();
		byte[] nameBytes = new byte[nameLength];
		for (int i = 0; i < nameLength; i++) {
            nameBytes[i] = data.readByte();
		}
        name = new String(nameBytes);
		
		if (getDataWatcher() instanceof DataWatcherLockable) {
			((DataWatcherLockable)getDataWatcher()).release();
		}
		
	}


	


	public int fill(FluidStack resource, boolean doFill) {
		return fill(ForgeDirection.UNKNOWN, resource, doFill);
	}
	
	/**
	 * Fills fluid into internal tanks, distribution is left to the ITankContainer.
	 * @param from Orientation the fluid is pumped in from.
	 * @param resource FluidStack representing the maximum amount of fluid filled into the ITankContainer
	 * @param doFill If false filling will only be simulated.
	 * @return Amount of resource that was filled into internal tanks.
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int amount = 0;
		if (resource != null && resource.amount > 0) {
			FluidStack fluid = resource.copy();
			for (int i = 0; i < tankModules.size(); i++) {
				int tempAmount = tankModules.get(i).fill(fluid, doFill);
		
				amount += tempAmount;
				fluid.amount -= tempAmount;
				if (fluid.amount <= 0) {
					break;
				}
			}
		}
		return amount;			
	}


	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}
	
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, resource == null ? 0 : resource.amount, doDrain);
	}
	
	private FluidStack drain(ForgeDirection from, FluidStack resource, int maxDrain, boolean doDrain) {
		FluidStack ret = resource;
		if (ret != null) {
			ret = ret.copy();
			ret.amount = 0;
		}
		for (int i = 0; i < tankModules.size(); i++) {
			FluidStack temp = null;
			temp = tankModules.get(i).drain(maxDrain, doDrain);
			
			if (temp != null && (ret == null || ret.isFluidEqual(temp))) {
				if (ret == null) {
					ret = temp;
				}else{
					ret.amount += temp.amount;
				}
			
				maxDrain -= temp.amount;
				if (maxDrain <= 0) {
					break;
				}
			}
		}
		if (ret != null && ret.amount == 0) {
			return null;
		}
		return ret;			
	}
	
	public int drain(Fluid type, int maxDrain, boolean doDrain) {
		int amount = 0;
		if (type != null && maxDrain > 0) {
			for (ModuleTank tank : tankModules) {
				FluidStack drained = tank.drain(maxDrain, false);
				if (drained != null && type.equals(drained.getFluid())) {
					amount += drained.amount;
					maxDrain -= drained.amount;
					if (doDrain) {
						tank.drain(drained.amount,true);
					}
					
					if (maxDrain <= 0) {
						break;
					}
				}
			}
		}
		return amount;
	}		
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}	

	/**
	 * @param direction tank side: UNKNOWN for default tank set
	 * @return Array of {@link FluidTank}s contained in this ITankContainer for this direction
	 */
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection direction) {
		FluidTankInfo[] ret = new FluidTankInfo[tankModules.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new FluidTankInfo(tankModules.get(i).getFluid(), tankModules.get(i).getCapacity());
		}
		return ret;
	}

	
		@Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {	
		if (modules != null) {
			for(ModuleBase module : modules) {
				if (slot < module.getInventorySize()) {
					return module.getSlots().get(slot).isItemValid(item);
				}else{
					slot -= module.getInventorySize();
				}
			}
		}
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public String getInventoryName() {
		return "container.modularcart";
	}



	@Override
	public boolean canRiderInteract() {
		return true;
	}

    @SideOnly(Side.CLIENT)
    private MovingSound sound;
    @SideOnly(Side.CLIENT)
    private MovingSound soundRiding;
    @SideOnly(Side.CLIENT)
    private int keepSilent;
    @SideOnly(Side.CLIENT)
    public void setSound(MovingSound sound, boolean riding) {
        if (riding) {
            this.soundRiding = sound;
        }else{
            this.sound = sound;
        }
    }

    @SideOnly(Side.CLIENT)
    public void silent() {
        keepSilent = 6;
    }
    @SideOnly(Side.CLIENT)
    private void updateSounds() {
        if (keepSilent > 1) {
            keepSilent--;
            stopSound(sound);
            stopSound(soundRiding);
            sound = null;
            soundRiding = null;
        }else if(keepSilent == 1) {
            keepSilent = 0;
            Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundMinecart(this));
            Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundMinecartRiding(Minecraft.getMinecraft().thePlayer, this));
        }

    }
    @SideOnly(Side.CLIENT)
    private void stopSound(MovingSound sound) {
        if (sound != null) {
            ReflectionHelper.setPrivateValue(MovingSound.class, sound, true, 0);
        }
    }
}
