package vswe.stevesvehicles.buoy;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class EntityBuoy extends Entity {
    private int renderTick;
    private float renderMultiplier;

    public EntityBuoy(World world) {
        super(world);
        preventEntitySpawning = true;
        yOffset = 0;
        setSize(1, 2);

        renderMultiplier = 0.8F + rand.nextFloat() * 0.4F;
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

    public EntityBuoy(World world, int x, int y, int z) {
        this(world);
        this.posX = x + 0.5;
        this.posY = y ;
        this.posZ = z + 0.5;
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

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

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
}
