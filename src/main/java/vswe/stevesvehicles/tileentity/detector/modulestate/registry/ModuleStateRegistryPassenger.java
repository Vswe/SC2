package vswe.stevesvehicles.tileentity.detector.modulestate.registry;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.tileentity.detector.modulestate.ModuleState;
import vswe.stevesvehicles.tileentity.detector.modulestate.ModuleStatePassenger;

public class ModuleStateRegistryPassenger extends ModuleStateRegistry {
    public ModuleStateRegistryPassenger() {
        super("passenger");

        createAndRegisterSimplePassenger("passenger", EntityLiving.class);
        createAndRegisterSimplePassenger("animal", IAnimals.class);
        createAndRegisterSimplePassenger("tameable", EntityTameable.class);
        createAndRegisterSimplePassenger("breedable", EntityAgeable.class);
        createAndRegisterSimplePassenger("hostile", IMob.class);
        createAndRegisterSimplePassenger("creeper", EntityCreeper.class);
        createAndRegisterSimplePassenger("skeleton", EntitySkeleton.class);
        createAndRegisterSimplePassenger("spider", EntitySpider.class);
        createAndRegisterSimplePassenger("zombie", EntityZombie.class);
        createAndRegisterSimplePassenger("pig_man", EntityPigZombie.class);
        createAndRegisterSimplePassenger("silver_fish", EntitySilverfish.class);
        createAndRegisterSimplePassenger("blaze", EntityBlaze.class);
        createAndRegisterSimplePassenger("bat", EntityBat.class);
        createAndRegisterSimplePassenger("witch", EntityWitch.class);
        createAndRegisterSimplePassenger("pig", EntityPig.class);
        createAndRegisterSimplePassenger("sheep", EntitySheep.class);
        createAndRegisterSimplePassenger("cow", EntityCow.class);
        createAndRegisterSimplePassenger("mooshroom", EntityMooshroom.class);
        createAndRegisterSimplePassenger("chicken", EntityChicken.class);
        createAndRegisterSimplePassenger("wolf", EntityWolf.class);
        createAndRegisterSimplePassenger("snow_golem", EntitySnowman.class);
        createAndRegisterSimplePassenger("ocelot", EntityOcelot.class);
        createAndRegisterSimplePassenger("villager", EntityVillager.class);
        createAndRegisterSimplePassenger("steve", EntityPlayer.class);

        ModuleState zombieVillager = new ModuleStatePassenger("zombie_villager", EntityZombie.class) {
            @Override
            public boolean isPassengerValid(Entity passenger) {
                return ((EntityZombie)passenger).isVillager();
            }
        };
        register(zombieVillager);

        ModuleState child = new ModuleStatePassenger("child", EntityAgeable.class) {
            @Override
            public boolean isPassengerValid(Entity passenger) {
                return ((EntityAgeable)passenger).isChild();
            }
        };
        register(child);

        ModuleState tamed = new ModuleStatePassenger("tamed", EntityTameable.class) {
            @Override
            public boolean isPassengerValid(Entity passenger) {
                return ((EntityTameable)passenger).isTamed();
            }
        };
        register(tamed);
    }

    private void createAndRegisterSimplePassenger(String unlocalizedName, Class clazz) {
        ModuleState passenger = new ModuleStatePassenger(unlocalizedName, clazz);
        register(passenger);
    }
}
