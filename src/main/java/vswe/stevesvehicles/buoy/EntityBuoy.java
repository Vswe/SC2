package vswe.stevesvehicles.buoy;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.item.ModItems;


public class EntityBuoy extends Entity implements IEntityAdditionalSpawnData {
    private int renderTick;
    private float renderMultiplier;
    private BuoyType buoyType = BuoyType.NORMAL;

    public EntityBuoy(World world) {
        super(world);
        preventEntitySpawning = true;
        yOffset = 0;
        setSize(1, 2);

        renderMultiplier = 0.8F + rand.nextFloat() * 0.4F;
    }

    public EntityBuoy(World world, int x, int y, int z, BuoyType buoyType) {
        this(world);
        this.posX = x + 0.5;
        this.posY = y ;
        this.posZ = z + 0.5;
        this.buoyType = buoyType;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    //TODO write a proper one
    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (!worldObj.isRemote && !isDead){
            setDead();
        }
        return true;
    }

    public ItemStack getBuoyItem() {
        return new ItemStack(ModItems.buoys, 1, buoyType.getMeta());
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return getBuoyItem();
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    private static final int DW_NEXT_BUOY = 3;
    private static final int DW_PREV_BUOY = 4;

    @SuppressWarnings("UnnecessaryBoxing")
    @Override
    protected void entityInit() {
        dataWatcher.addObject(DW_NEXT_BUOY, new Integer(-1));
        dataWatcher.addObject(DW_PREV_BUOY, new Integer(-1));
    }

    private static final String NBT_TYPE = "BuoyType";
    private static final String NBT_NEXT = "Next";
    private static final String NBT_PREV = "Prev";


    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        buoyType = BuoyType.getType(compound.getByte(NBT_TYPE));

        readBuoy(compound, NBT_NEXT, DW_NEXT_BUOY);
        readBuoy(compound, NBT_PREV, DW_PREV_BUOY);
    }


    private void readBuoy(NBTTagCompound compound, String nbt, int id) {
        //TODO entity ids aren't persistent from load to load so that can't be saved, however there may only be one buoy per block so that can be used to save and load. Can't probably look for the other buoys at this point (probably has to wait for the first tick or something).
    }

    private void writeBuoy(NBTTagCompound compound, String nbt, int id) {
        //TODO entity ids aren't persistent from load to load so that can't be saved, however there may only be one buoy per block so that can be used to save and load. Can't probably look for the other buoys at this point (probably has to wait for the first tick or something).
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setByte(NBT_TYPE, (byte)buoyType.getMeta());
        writeBuoy(compound, NBT_NEXT, DW_NEXT_BUOY);
        writeBuoy(compound, NBT_PREV, DW_PREV_BUOY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (worldObj.isRemote) {
            renderTick++;
        }
    }

    public float getLampColor() {
        return 0.5F;
    }

    public int getRenderTick() {
        return renderTick;
    }

    public float getRenderMultiplier() {
        return renderMultiplier;
    }

    public BuoyType getBuoyType() {
        return buoyType;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeByte(buoyType.getMeta());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        buoyType = BuoyType.getType(additionalData.readByte());
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (!worldObj.isRemote) {
            FMLNetworkHandler.openGui(player, StevesVehicles.instance, 2, worldObj, getEntityId(), 0, 0);
        }
        return true;
    }

    private EntityBuoy getBuoy(int id) {
        int entityId = dataWatcher.getWatchableObjectInt(id);
        Entity entity = worldObj.getEntityByID(entityId);
        if (entity instanceof EntityBuoy && !entity.isDead) {
            return (EntityBuoy)entity;
        }

        return null;
    }

    public EntityBuoy getNextBuoy() {
        return getBuoy(DW_NEXT_BUOY);
    }

    public EntityBuoy getPrevBuoy() {
        return getBuoy(DW_PREV_BUOY);
    }

    private void setBuoy(int id, int entityId) {
        dataWatcher.updateObject(id, entityId);
    }

    private void setBuoy(int id, EntityBuoy buoy) {
        Integer entityId = buoy != null && !buoy.isDead ? buoy.getEntityId() : -1;
        setBuoy(id, entityId);
    }

    public void setNextBuoy(EntityBuoy buoy) {
        setBuoy(DW_NEXT_BUOY, buoy);
    }

    public void setPrevBuoy(EntityBuoy buoy) {
        setBuoy(DW_PREV_BUOY, buoy);
    }






    public void setBuoy(EntityBuoy buoy, boolean next) {
        if (next) {
            setNextBuoy(buoy);
        }else{
            setPrevBuoy(buoy);
        }
    }

    public EntityBuoy getBuoy( boolean next) {
        return next ? getNextBuoy() : getPrevBuoy();
    }
}
