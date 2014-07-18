package vswe.stevesvehicles.vehicle.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.VehicleBoat;


public class EntityModularBoat extends EntityBoatBase implements IVehicleEntity {
    private VehicleBase vehicleBase;

    public EntityModularBoat(World world) {
        super(world);
        this.vehicleBase = new VehicleBoat(this);
        overrideDataWatcher();
    }

    public EntityModularBoat(World world, double x, double y, double z, NBTTagCompound info, String name) {
        super(world, x, y, z);
        this.vehicleBase = new VehicleBoat(this, info, name);
        overrideDataWatcher();
    }

    /**
     * The normal data watcher is overridden by a special one on the client side. This is to be able
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

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        super.writeToNBT(tagCompound);

        vehicleBase.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        super.readFromNBT(tagCompound);

        vehicleBase.readFromNBT(tagCompound);
    }

    @Override
    public boolean interactFirst(EntityPlayer player){
        if (!vehicleBase.canInteractWithEntity(player)) {
            return false;
        }else{
            vehicleBase.onInteractWith(player);
            return true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        vehicleBase.onUpdate();
    }


    @Override
    public void setDead() {
        vehicleBase.preDeath();
        super.setDead();
        vehicleBase.postDeath();
    }

    @Override
    public boolean attackEntityFrom(DamageSource type, float dmg) {
        return vehicleBase.canBeAttacked(type, dmg) && super.attackEntityFrom(type, dmg);
    }


    @Override
    protected ItemStack getBoatItem() {
        return  vehicleBase.getVehicleItem();
    }

    //Inventory, Tank and a few other methods. These methods are required due to the implementing interfaces, but all logic is handled in the VehicleBase.

    @Override
    public void markDirty() {vehicleBase.onInventoryUpdate();}

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {return entityplayer.getDistanceSq(posX, posY, posZ) <= 64D;}
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
    public boolean hasCustomInventoryName() {return false;}


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

    @Override
    public AxisAlignedBB getBoundingBox(){return vehicleBase.isPlaceholder ? null : super.getBoundingBox();}
    @Override
    public boolean canBeCollidedWith(){return !vehicleBase.isPlaceholder && super.canBeCollidedWith();}
    @Override
    public boolean canBePushed() {
        return !vehicleBase.isPlaceholder && super.canBePushed();
    }



    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public VehicleBase getVehicle() {
        return vehicleBase;
    }
}
