package vswe.stevesvehicles.vehicle.entity;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public abstract class EntityBoatBase extends EntityBoat { //The only reason this extends EntityBoat is for vanilla and mods to actually think these are boats
    public EntityBoatBase(World world) {
        super(world);
        isBoatEmpty = true;
        preventEntitySpawning = true;
        setSize(0.98F, 0.7F);
        yOffset = height / 2;
    }

    public EntityBoatBase(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y + yOffset, z);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }



    /** true if no player in boat */
    private boolean isBoatEmpty;

    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;


    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit(){
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
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
    public boolean canBePushed() {
        return true;
    }

    @Override
    public double getMountedYOffset() {
        return 0.3;
    }

    @Override
    public boolean attackEntityFrom(DamageSource type, float dmg) {
        if (isEntityInvulnerable()) {
            return false;
        }

        if (!worldObj.isRemote && !isDead){
            setForwardDirection(-getForwardDirection());
            setTimeSinceHit(10);
            setDamageTaken(getDamageTaken() + dmg * 10.0F);
            setBeenAttacked();
            boolean creative = type.getEntity() instanceof EntityPlayer && ((EntityPlayer)type.getEntity()).capabilities.isCreativeMode;

            if (creative || this.getDamageTaken() > 40.0F) {
                if (riddenByEntity != null) {
                    riddenByEntity.mountEntity(null);
                }

                setDead();
            }

        }

        return true;
    }

    protected abstract ItemStack getBoatItem();

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return getBoatItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation() {
        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() * 11.0F);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int partialTick) {
        if (isBoatEmpty) {
            this.boatPosRotationIncrements = partialTick + 5;
        }else {
            double distanceX = x - this.posX;
            double distanceY = y - this.posY;
            double distanceZ = z - this.posZ;
            double distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

            if (distance <= 1) {
                return;
            }

            boatPosRotationIncrements = 3;
        }

        this.boatX = x;
        this.boatY = y;
        this.boatZ = z;
        this.boatYaw = (double)yaw;
        this.boatPitch = (double)pitch;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void setVelocity(double velocityX, double velocityY, double velocityZ)  {
        this.velocityX = this.motionX = velocityX;
        this.velocityY = this.motionY = velocityY;
        this.velocityZ = this.motionZ = velocityZ;
    }

    private static final int COLLISION_SLICES = 5;
    private static final double MAX_SPEED = 0.35; //depending on the throttle one can give in the handleSteering method this speed might not actually be achievable
    private static final double MAX_YAW_SPEED = 20;

    @Override
    public void onUpdate() {
        superSuperOnUpdate();

        if (getTimeSinceHit() > 0) {
            setTimeSinceHit(getTimeSinceHit() - 1);
        }

        if (getDamageTaken() > 0.0F) {
            this.setDamageTaken(getDamageTaken() - 1.0F);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;


        double horizontalSpeed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        spawnParticles(horizontalSpeed);


        if (worldObj.isRemote && isBoatEmpty) {
            updateClientSoloBoat();
        }else {
            handleFloating();
            handleSteering();
            handleSpeedLimits();
            handleBlockRemoval();
            handleMovement(horizontalSpeed);
            handleRotation();
            handleEntityInteraction();
        }
    }

    private static final double BOUNDING_BOX_EXPANSION = 0.8;

    protected void handleEntityInteraction() {
        if (!worldObj.isRemote) {
            List list = worldObj.getEntitiesWithinAABB(EntityBoat.class, boundingBox.expand(BOUNDING_BOX_EXPANSION, 0, BOUNDING_BOX_EXPANSION));

            if (list != null && !list.isEmpty()) {
                for (Object obj : list) {
                    Entity entity = (Entity)obj;

                    if (entity != this.riddenByEntity && entity.canBePushed() && entity != this) {
                        entity.applyEntityCollision(this);
                    }
                }
            }

            if (riddenByEntity != null && riddenByEntity.isDead) {
                riddenByEntity = null;
            }
        }
    }

    protected void handleRotation() {
        rotationPitch = 0;
        double yaw = (double)this.rotationYaw;
        double differenceX = prevPosX - posX;
        double differenceZ = prevPosZ - posZ;
        double differenceSquared = differenceX * differenceX + differenceZ * differenceZ;

        if (differenceSquared > 0.001D) {
            yaw = Math.atan2(differenceZ, differenceX) * 180 / Math.PI;
        }

        double yawDifference = MathHelper.wrapAngleTo180_double(yaw - rotationYaw);

        if (yawDifference > MAX_YAW_SPEED) {
            yawDifference = MAX_YAW_SPEED;
        }else if (yawDifference < -MAX_YAW_SPEED) {
            yawDifference = -MAX_YAW_SPEED;
        }

        rotationYaw += yawDifference;


        setRotation(rotationYaw, rotationPitch);
    }

    protected void handleMovement(double horizontalSpeed) {
        if (onGround) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveEntity(motionX, motionY, motionZ);

        if (hasCrashed(horizontalSpeed)) {
            if (!worldObj.isRemote && !isDead) {
                onCrash(false);
            }
        }else {
            motionX *= 0.95;
            motionY *= 0.95;
            motionZ *= 0.95;
        }
    }

    protected void onCrash(boolean fall) {
        setDead();
    }

    protected boolean hasCrashed(double horizontalSpeed) {
        return isCollidedHorizontally && horizontalSpeed > 0.2D;
    }

    protected boolean hasFallen(float fallDistance) {
        return fallDistance > 3;
    }

    protected void handleBlockRemoval() {
        for (int x = -1; x <= 1; x += 2) {
            double differenceX = x * 0.4;
            int targetX = MathHelper.floor_double(posX + differenceX);
            for (int y = 0; y <= 2; y += 1) {
                int targetY = MathHelper.floor_double(posY) + y;
                for (int z = -1; z <= 1; z += 2) {
                    double differenceZ = z * 0.4;
                    int targetZ = MathHelper.floor_double(posZ + differenceZ);

                    Block block = worldObj.getBlock(targetX, targetY, targetZ);
                    if (handleBlockRemoval(block, targetX, targetY, targetZ)) {
                        isCollidedHorizontally = false;
                    }
                }
            }
        }
    }

    protected boolean handleBlockRemoval(Block block, int x, int y, int z)  {
        if (block == Blocks.snow_layer) {
            worldObj.setBlockToAir(x, y, z);
            return true;
        }else if (block == Blocks.waterlily) {
            worldObj.func_147480_a(x, y, z, true);
            return true;
        }else{
            return false;
        }
    }

    private void handleSpeedLimits() {
        double horizontalSpeed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        if (horizontalSpeed > MAX_SPEED) {
            double yaw = Math.atan2(motionZ, motionX);

            motionX = Math.cos(yaw) * MAX_SPEED;
            motionZ = Math.sin(yaw) * MAX_SPEED;
        }

    }

    protected void handleSteering() {

    }


    protected void handleFloating() {
        int slicesInWater = getSlicesInWater();

        if (slicesInWater < COLLISION_SLICES) {
            motionY += 0.04 * (2D * slicesInWater / COLLISION_SLICES - 1);

        }else {
            if (motionY < 0) {
                motionY /= 2;
            }

            motionY += 0.007;
        }

    }


    protected void updateClientSoloBoat() {
        if (boatPosRotationIncrements > 0) {
            double targetX = posX + (boatX - posX) / (double)boatPosRotationIncrements;
            double targetY = posY + (boatY - posY) / (double)boatPosRotationIncrements;
            double targetZ = posZ + (boatZ - posZ) / (double)boatPosRotationIncrements;


            double yawDifference = MathHelper.wrapAngleTo180_double(boatYaw - rotationYaw);
            rotationYaw = (float)(rotationYaw + yawDifference / boatPosRotationIncrements);
            rotationPitch = (float)(rotationPitch + (boatPitch - rotationPitch) / boatPosRotationIncrements);
            --boatPosRotationIncrements;

            setPosition(targetX, targetY, targetZ);
            setRotation(rotationYaw, rotationPitch);

        }else {
            double targetX = posX + motionX;
            double targetY = posY + motionY;
            double targetZ = posZ + motionZ;
            setPosition(targetX, targetY, targetZ);

            if (onGround) {
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
            }

            motionX *= 0.95;
            motionY *= 0.95;
            motionZ *= 0.95;
        }
    }

    protected void spawnParticles(double horizontalSpeed) {
        if (horizontalSpeed > 0.2625D) {
            double multiplierX = Math.cos(rotationYaw * Math.PI / 180);
            double multiplierZ = Math.sin(rotationYaw * Math.PI / 180);

            for (int i = 0; i <= horizontalSpeed * 60; i++) {
                double distance = rand.nextFloat() * 2 - 1;
                double distance2 = (rand.nextInt(2) * 2 - 1) * 0.7;
                double targetX;
                double targetZ;

                if (this.rand.nextBoolean()) {
                    targetX = posX - multiplierX * distance * 0.8   + multiplierZ * distance2;
                    targetZ = posZ - multiplierZ * distance * 0.8   - multiplierX * distance2;
                }else {
                    targetX = posX + multiplierX                    + multiplierZ * distance * 0.7;
                    targetZ = posZ + multiplierZ                    - multiplierX * distance * 0.7;
                }

                worldObj.spawnParticle("splash", targetX, posY - 0.125, targetZ, motionX, motionY, motionZ);
            }
        }
    }


    protected int getSlicesInWater() {
        int slicesInWater = 0;

        for (int i = 0; i < COLLISION_SLICES; i++) {
            double sliceMinY = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * i / (double)COLLISION_SLICES - 0.125D;
            double sliceMaxY = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (i + 1) / (double)COLLISION_SLICES - 0.125D;

            AxisAlignedBB sliceBox = AxisAlignedBB.getAABBPool().getAABB(
                    boundingBox.minX, sliceMinY, boundingBox.minZ,
                    boundingBox.maxX, sliceMaxY, boundingBox.maxZ
            );

            if (worldObj.isAABBInMaterial(sliceBox, Material.water)) {
                slicesInWater++;
            }
        }
        return slicesInWater;
    }

    @Override
    public void applyEntityCollision(Entity other) {
        if (!(other instanceof EntityBoat)) {
            super.applyEntityCollision(other);
        }else if (other.riddenByEntity != this && other.ridingEntity != this) {
            double differenceX = other.posX - this.posX;
            double differenceZ = other.posZ - this.posZ;
            double difference = MathHelper.abs_max(differenceX, differenceZ);

            System.out.println(difference + " " + worldObj.isRemote);
            if (difference >= 0.01) {
                difference = (double)MathHelper.sqrt_double(difference);
                differenceX /= difference;
                differenceZ /= difference;
                double inverted = 1 / difference;

                if (inverted > 1) {
                    inverted = 1;
                }

                differenceX *= inverted;
                differenceZ *= inverted;
                differenceX *= 0.05;
                differenceZ *= 0.05;
                differenceX *= 1 - this.entityCollisionReduction;
                differenceZ *= 1 - this.entityCollisionReduction;
                this.addVelocity(-differenceX, 0D, -differenceZ);
                other.addVelocity(differenceX, 0D, differenceZ);
            }
        }
    }

    /**
     * One can't call a super class' super class' method and since we extend EntityBoat simply to make this a boat we
     * don't want to actually trigger its code. Fortunately the only thing the Entity's onUpdate does is to call
     * onEntityUpdate, this method is a copy of Entity's onUpdate.
     */
    private void superSuperOnUpdate() {
        onEntityUpdate();
    }

    @Override
    public void updateRiderPosition() {
        if (riddenByEntity != null) {
            riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize(){
        return 0.0F;
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != par1EntityPlayer) {
            return true;

        }else {
            if (!worldObj.isRemote) {
                par1EntityPlayer.mountEntity(this);
            }

            return true;
        }
    }

    @Override
    protected void updateFallState(double distance, boolean onGround) {
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);

        if (onGround) {
            if (hasFallen(fallDistance)) {
                fall(fallDistance);

                if (!worldObj.isRemote && !isDead) {
                    onCrash(true);
                }

                fallDistance = 0;
            }

        }else if (worldObj.getBlock(x, y - 1, z).getMaterial() != Material.water && distance < 0){
            fallDistance -= distance;
        }
    }

    @Override
    public void setDamageTaken(float val) {
        dataWatcher.updateObject(19, Float.valueOf(val));
    }

    @Override
    public float getDamageTaken() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    @Override
    public void setTimeSinceHit(int val) {
        dataWatcher.updateObject(17, Integer.valueOf(val));
    }

    @Override
    public int getTimeSinceHit() {
        return dataWatcher.getWatchableObjectInt(17);
    }


    @Override
    public void setForwardDirection(int val) {
        dataWatcher.updateObject(18, Integer.valueOf(val));
    }

    @Override
    public int getForwardDirection() {
        return dataWatcher.getWatchableObjectInt(18);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void setIsBoatEmpty(boolean val) {
        this.isBoatEmpty = val;
    }
}
