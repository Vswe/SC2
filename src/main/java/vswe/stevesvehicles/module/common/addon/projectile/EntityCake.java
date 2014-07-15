package vswe.stevesvehicles.module.common.addon.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
public class EntityCake extends EntityEgg {
    public EntityCake(World world) {
        super(world);
    }

    public EntityCake(World world, EntityLiving thrower) {
        super(world, thrower);
    }

    public EntityCake(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    @Override
    protected void onImpact(MovingObjectPosition data) {
        if (data.entityHit != null) {
            data.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0);
            if (data.entityHit instanceof EntityPlayer) {
            	EntityPlayer player = (EntityPlayer)data.entityHit;
            	
            	player.getFoodStats().addStats(14, 0.7F);
            }
        }else{
        	if (worldObj.isAirBlock((int)posX, (int)posY, (int)posZ) && worldObj.isSideSolid((int)posX, (int)posY - 1, (int) posZ, ForgeDirection.UP)) {
        		worldObj.setBlock((int)posX, (int)posY, (int)posZ, Blocks.cake);
        	}
        }




        for (int i = 0; i < 8; i++) {
            //noinspection SpellCheckingInspection
            worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!worldObj.isRemote) {
            setDead();
        }
    }
	
	
}
