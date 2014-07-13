package vswe.stevesvehicles.vehicle.entity;

import cpw.mods.fml.relauncher.ReflectionHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.item.ModItems;
import vswe.stevesvehicles.old.Helpers.DataWatcherLockable;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.module.ModuleBase;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.vehicle.VehicleCart;

/**
 * The modular minecart class, this is the cart. This is what controls all modules whereas 
 * the modules({@link ModuleBase} handles specific tasks. The cart the player can see in-game 
 * is a combination of the cart and its modules.
 * @author Vswe
 */
public class EntityModularCart extends EntityMinecart
    implements IVehicleEntity
{

	private vswe.stevesvehicles.vehicle.VehicleBase vehicleBase;
    public int disabledX;
    public int disabledY;
    public int disabledZ;
    protected boolean wasDisabled;
    public double pushX;
    public double pushZ;		
    public double temppushX;
    public double temppushZ;
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

	
	/**
	 * Information about how trails turn
	 */
	public static final int[][][] railDirectionCoordinates = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};



    @Override
    public vswe.stevesvehicles.vehicle.VehicleBase getVehicle() {
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
    public EntityModularCart(World world, double x, double y, double z, NBTTagCompound info, String name) {
        super(world, x, y, z);
        this.vehicleBase = new VehicleCart(this, info, name);
		overrideDataWatcher();
    }
	
	
    /**
     * Creates a cart in the world. This is used when a cart is loaded on the server, or when a cart is created on the client.
     * @param world The world the cart is created in
     */
    public EntityModularCart(World world) {
        super(world);
        this.vehicleBase = new VehicleCart(this);
		overrideDataWatcher();
    }

	
    /**
     * The normal datawatcher is overridden by a special one on the client side. This is to be able
     * to wait to process data in the beginning. Fixing the syncing at start up.
     */
	private void overrideDataWatcher() {
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
	 * Initiates the entity, used for initiating data watchers.
	 */
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
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
    public double getMountedYOffset() {
        float offset = vehicleBase.getMountedYOffset();
        if (offset != 0) {
            return offset;
        }

		return super.getMountedYOffset();
    }	
	
	/**
	 * Get this cart as an item. Used when breaking the cart or "picking"(middle mouse button) it for instance.
	 */
	@Override
    public ItemStack getCartItem() {
		if (vehicleBase.getModules() != null) {
			ItemStack cart = ModuleDataItemHandler.createModularVehicle(vehicleBase);
			if (vehicleBase.getVehicleRawName() != null && !vehicleBase.getVehicleRawName().isEmpty()) {
				cart.setStackDisplayName(vehicleBase.getVehicleRawName());
			}

			return cart;
		}else{
			return new ItemStack(ModItems.vehicles);
		}
    }

	/**
	 * When the cart 
	 */
	@Override
	public void killMinecart(DamageSource dmg) {
		this.setDead();
		if (vehicleBase.dropOnDeath()) {
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
    public float getMaxCartSpeedOnRail() {
        return vehicleBase == null ? super.getMaxCartSpeedOnRail() : vehicleBase.getMaxSpeed(super.getMaxCartSpeedOnRail());
    }

    /**
     * Returns if this cart can be powered
     */
	@Override
    public boolean isPoweredCart() {
        return vehicleBase.isPoweredEntity();
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
		if (vehicleBase.getModules() != null) {
			for (ModuleBase module : vehicleBase.getModules()) {
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
	public boolean attackEntityFrom(DamageSource type, float dmg) {
        if (vehicleBase.canBeAttacked(type, dmg)) {
		    return super.attackEntityFrom(type,dmg);
        }else{
            return false;
        }
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
		if (vehicleBase.getModules() != null) {
			for (ModuleBase module : vehicleBase.getModules()) {
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
    public void moveMinecartOnRail(int x, int y, int z, double acceleration/*?*/){
        super.moveMinecartOnRail(x, y, z, acceleration);
		if (vehicleBase.getModules() != null) {
			for (ModuleBase module : vehicleBase.getModules()) {
				module.moveMinecartOnRail(x, y, z);
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
		
       if (block != ModBlocks.ADVANCED_DETECTOR.getBlock() && vehicleBase.isDisabled()){
            releaseCart();
        }

        boolean canBeDisabled = block == ModBlocks.ADVANCED_DETECTOR.getBlock() && (blockBelow != ModBlocks.DETECTOR_UNIT.getBlock() || !DetectorType.getTypeFromMeta(metaBelow).canInteractWithCart() || DetectorType.getTypeFromMeta(metaBelow).shouldStopCart());
        boolean forceUnDisable = (wasDisabled && disabledX == x && disabledY == y && disabledZ == z);

        if (!forceUnDisable && wasDisabled){
            wasDisabled = false;
        }

        canBeDisabled = forceUnDisable ? false : canBeDisabled;

        if (canBeDisabled && !vehicleBase.isDisabled()){
            vehicleBase.setIsDisabled(true);

            if (pushX != 0 || pushZ != 0){
                temppushX = pushX;
                temppushZ = pushZ;
                pushX = pushZ = 0;
            }

            disabledX = x;
            disabledY = y;
            disabledZ = z;
        }
		
		
		if (fixedMX != x || fixedMY != y || fixedMZ != z){
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
		ModuleBase.RAIL_DIRECTION dir = ModuleBase.RAIL_DIRECTION.DEFAULT;
		for (ModuleBase module : vehicleBase.getModules()) {
			dir = module.getSpecialRailDirection(x,y,z);
			if (dir != ModuleBase.RAIL_DIRECTION.DEFAULT) {
				break;
			}
		}
	
		if (dir == ModuleBase.RAIL_DIRECTION.DEFAULT)  {
			return -1;
		}
	
		int Yaw = (int)(rotationYaw % 180);

        if (Yaw < 0) {
            Yaw += 180 ;
        }

        boolean flag = Yaw >= 45 && Yaw <= 135;
		
        if (fixedMeta == -1){
			switch (dir) {
				case FORWARD:
                    fixedMeta = flag ? 0 : 1;
					break;
				case LEFT:
					if (flag) {
                        fixedMeta = motionZ > 0 ? 9 : 7;
					}else {
                        fixedMeta = motionX > 0 ? 8 : 6;
					}	
					break;
				case RIGHT:					
					if (flag) {
                        fixedMeta = motionZ > 0 ? 8 : 6;
					}else{
                        fixedMeta = motionX > 0 ? 7 : 9;
					}
					break;
					
					//doesn't work
				case NORTH: 
					if (flag){
                        fixedMeta = motionZ > 0 ? 0 : -1;
					}else {
                        fixedMeta = motionX > 0 ? 7 : 6;
					}
					
					break;
				default:
					fixedMeta = -1;
			}

            if (fixedMeta == -1){
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
    public void releaseCart(){
        wasDisabled = true;
        vehicleBase.setIsDisabled(false);
        pushX = temppushX;
        pushZ = temppushZ;
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
    protected void applyDrag() {
	
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
		vehicleBase.setEngineFlag(d0 > 1.0E-4D);
		if (vehicleBase.isDisabled()) {
			motionX = 0;
			motionY = 0;
			motionZ = 0;
        }else if (vehicleBase.getEngineFlag()) {
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
    protected double getPushFactor() {
		if (vehicleBase.getModules() != null) {
			for (ModuleBase module : vehicleBase.getModules()) {
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
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
        vehicleBase.writeToNBT(tagCompound);

        tagCompound.setDouble("pushX", pushX);
        tagCompound.setDouble("pushZ", pushZ);
        tagCompound.setDouble("temppushX", temppushX);
        tagCompound.setDouble("temppushZ", temppushZ);
	}
	
	
	/**
	 * Loads the data of the cart, also allows the modules to load their data
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);

        vehicleBase.readFromNBT(tagCompound);

        pushX = tagCompound.getDouble("pushX");
        pushZ = tagCompound.getDouble("pushZ");	
        temppushX = tagCompound.getDouble("temppushX");
        temppushZ = tagCompound.getDouble("temppushZ");
	}
	



	@Override
    public void onUpdate() {
		super.onUpdate();

		vehicleBase.onUpdate();
        if (worldObj.isRemote) {
            updateSounds();
        }
        setCurrentCartSpeedCapOnRail(getMaxCartSpeedOnRail());
	}	

	/**
	 * Returns if this inventory(the cart) is usuable by the specific player
	 */
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
       return entityplayer.getDistanceSq(posX, posY, posZ) <= 64D;
    }	
	
    /**
     A method to be called when this cart is activated by the player
    **/
	@Override
    public boolean interactFirst(EntityPlayer player){
        if (!vehicleBase.canInteractWithEntity(player)) {
            return false;
        }

		if (!worldObj.isRemote) {
	        //Saves which direction the player activated the cart from
	        if (!vehicleBase.isDisabled() && riddenByEntity != player){
	            temppushX = posX - player.posX;
	            temppushZ = posZ - player.posZ;
	        }
	
	        //if the cart can move, start it in the desired direction.
	        if (!vehicleBase.isDisabled() && vehicleBase.hasFuel() && pushX == 0 && pushZ == 0){
	            pushX = temppushX;
	            pushZ = temppushZ;
	        }
		}

        vehicleBase.onInteractWith(player);
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












    //Inventory, Tank and a few other methods. These methods are required due to the implementing interfaces, but all logic is handled in the VehicleBase.

    @Override
    public void markDirty() {vehicleBase.onInventoryUpdate();}
    @Override
    public int getSizeInventory(){return vehicleBase.getInventorySize();}
    @Override
    public ItemStack getStackInSlot(int id) {return vehicleBase.getStack(id);}
	@Override
    public void setInventorySlotContents(int id, ItemStack item) { vehicleBase.setStack(id, item);}
    @Override
    public ItemStack decrStackSize(int id, int count) {return vehicleBase.decreaseStack(id, count);}
	@Override
    public ItemStack getStackInSlotOnClosing(int id) {return vehicleBase.getStackOnCloseing(id);}
    @Override
    public void openInventory(){vehicleBase.openInventory();}
    @Override
    public void closeInventory() {vehicleBase.closeInventory();}
    @Override
    public boolean isItemValidForSlot(int id, ItemStack item) {return vehicleBase.isItemValid(id, item);}
    @Override
    public int getInventoryStackLimit() {return vehicleBase.getInventoryStackLimit();}
    @Override
    public String getInventoryName() {return vehicleBase.getInventoryName();}

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {return vehicleBase.fill(from, resource, doFill);}
    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {return vehicleBase.drain(from, maxDrain, doDrain);}
    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {return vehicleBase.drain(from, resource, doDrain);}
    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {return vehicleBase.canFill(from, fluid);}
    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {return vehicleBase.canDrain(from, fluid);}
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection direction) {return vehicleBase.getTankInfo(direction);}

    @Override
    public void writeSpawnData(ByteBuf data) {vehicleBase.writeSpawnData(data);}
    @Override
    public void readSpawnData(ByteBuf data) {vehicleBase.readSpawnData(data);}



	/**
	 * Returns the bounding box of the cart.
	 */
	@Override
	public AxisAlignedBB getBoundingBox(){
		if (vehicleBase.isPlaceholder) {
			return null;
		}else{
			return super.getBoundingBox();
		}
    }
	
	/**
	 * Returns whether something can collide with the cart or not.
	 */
	@Override
	public boolean canBeCollidedWith(){
		if (vehicleBase.isPlaceholder) {
			return false;
		}else{
			return super.canBeCollidedWith();
		}
    }
	
	/**
	 * Returns whether the cart can be pushed or not
	 */
	@Override
	public boolean canBePushed() {
		if (vehicleBase.isPlaceholder) {
			return false;
		}else{
			return super.canBePushed();
		}
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
