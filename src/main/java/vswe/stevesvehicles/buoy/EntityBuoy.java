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


    @Override
    protected void entityInit() {

    }

    private static final String NBT_TYPE = "BuoyType";

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        buoyType = BuoyType.getType(compound.getByte(NBT_TYPE));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setByte(NBT_TYPE, (byte)buoyType.getMeta());
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
}
