package vswe.stevescarts.Listeners;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import vswe.stevescarts.Helpers.ComponentTypes;

public class MobDeathListener {

    public MobDeathListener()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

	@SubscribeEvent
	public void onEntityLivingDeath(LivingDeathEvent event) {
		EntityLivingBase monster = event.entityLiving;
		
		if (monster.worldObj.isRemote || !event.source.getDamageType().equals("player")) {
			return;
		}

        if (monster instanceof EntityMob) {

            if (Math.random() < 0.10d) {
                dropItem(monster, ComponentTypes.STOLEN_PRESENT.getItemStack());
            }
        }

		
		/*if (monster instanceof EntityWitch) {
			double rand = Math.random();
			if (rand < 0.10) {
				dropItem(monster, new ItemStack(StevesCarts.instance.component, 2, 52));			
			}else if(rand < 0.50){
				dropItem(monster, new ItemStack(StevesCarts.instance.component, 1, 52));				
			}
		}else*/ if(monster instanceof EntityBlaze) {
			if (Math.random() < 0.12/*0.05d*/) {
				dropItem(monster, ComponentTypes.RED_WRAPPING_PAPER.getItemStack());
			}
		}
	}
	
	private void dropItem(EntityLivingBase monster, ItemStack item) {
		EntityItem obj = new EntityItem(monster.worldObj, monster.posX, monster.posY, monster.posZ, item);

		obj.motionX = (double)(monster.worldObj.rand.nextGaussian() * 0.05F);
		obj.motionY = (double)(monster.worldObj.rand.nextGaussian() * 0.05F + 0.2F);
		obj.motionZ = (double)(monster.worldObj.rand.nextGaussian() * 0.05F);

		monster.worldObj.spawnEntityInWorld(obj);		
	}

}