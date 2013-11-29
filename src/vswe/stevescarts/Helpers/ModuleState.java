package vswe.stevescarts.Helpers;

import java.util.Collection;
import java.util.HashMap;

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
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModulePowerObserver;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Realtimers.ModuleCage;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Modules.Workers.ModuleBridge;
import vswe.stevescarts.Modules.Workers.ModuleFertilizer;
import vswe.stevescarts.Modules.Workers.ModuleRailer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutter;
	
	
public class ModuleState {

	private static HashMap<Byte, ModuleState> states;
	
	public static HashMap<Byte, ModuleState> getStates() {
		return states;
	}
	
	public static Collection<ModuleState> getStateList() {
		return states.values();
	}
	
	static {
		states = new HashMap<Byte, ModuleState>();
		new ModuleState(0, ModuleRailer.class, "Have rail supplies", STATETYPE.SUPPLY);
		new ModuleState(1, ModuleTorch.class, "Have torch supplies", STATETYPE.SUPPLY);
		new ModuleState(2, ModuleWoodcutter.class, "Have sapling supplies", STATETYPE.SUPPLY);
		new ModuleState(3, ModuleFarmer.class, "Have seed supplies", STATETYPE.SUPPLY);
		//new ModuleState(4, ModuleHydrater.class, "Have water supplies", STATETYPE.SUPPLY);
		new ModuleState(5, ModuleBridge.class, "Have bridge supplies", STATETYPE.SUPPLY);
		new ModuleState(40, ModuleShooter.class, "Have projectile supplies", STATETYPE.SUPPLY);	
		new ModuleState(41, ModuleFertilizer.class, "Have fertilizing supplies", STATETYPE.SUPPLY);	
		
		new ModuleState(6, ModuleShield.class, "Is shield active", STATETYPE.ACTIVATION);
		new ModuleState(7, ModuleChunkLoader.class, "Is chunk loader active", STATETYPE.ACTIVATION);
		new ModuleState(8, ModuleInvisible.class, "Is invisibility core active", STATETYPE.ACTIVATION);
		new ModuleState(9, ModuleDrill.class, "Is drill active", STATETYPE.ACTIVATION);	
		new ModuleState(12, ModuleCage.class, "Is auto pick up active", STATETYPE.ACTIVATION);
		
		new ModuleStateInv(10, "Is storage completely full", true);		
		new ModuleStateInv(11, "Is storage completely empty", false);
		
		
		
		new ModuleStatePassenger(13, "Has passenger", EntityLiving.class);
		new ModuleStatePassenger(14, "Has animal passenger", IAnimals.class);
		new ModuleStatePassenger(15, "Has tameable passenger", EntityTameable.class);
		new ModuleStatePassenger(16, "Has breedable passenger", EntityAgeable.class);
		new ModuleStatePassenger(17, "Has hostile passenger", IMob.class);
		
		new ModuleStatePassenger(18, "Has creeper passenger", EntityCreeper.class);
		new ModuleStatePassenger(19, "Has skeleton passenger", EntitySkeleton.class);
		new ModuleStatePassenger(20, "Has spider passenger", EntitySpider.class);
		new ModuleStatePassenger(21, "Has zombie passenger", EntityZombie.class);
		new ModuleStatePassenger(22, "Has zombie pig man passenger", EntityPigZombie.class);	
		new ModuleStatePassenger(23, "Has silverfish passenger", EntitySilverfish.class);	
		new ModuleStatePassenger(24, "Has blaze passenger", EntityBlaze.class);
		new ModuleStatePassenger(25, "Has bat passenger", EntityBat.class);	
		new ModuleStatePassenger(26, "Has witch passenger", EntityWitch.class);	
		new ModuleStatePassenger(27, "Has pig passenger", EntityPig.class);	
		new ModuleStatePassenger(28, "Has sheep passenger", EntitySheep.class);	
		new ModuleStatePassenger(29, "Has cow passenger", EntityCow.class);	
		new ModuleStatePassenger(30, "Has mooshroom passenger", EntityMooshroom.class);			
		new ModuleStatePassenger(31, "Has chicken passenger", EntityChicken.class);	
		new ModuleStatePassenger(32, "Has wolf passenger", EntityWolf.class);
		new ModuleStatePassenger(33, "Has snow golem passenger", EntitySnowman.class);	
		new ModuleStatePassenger(34, "Has ocelot passenger", EntityOcelot.class);
		new ModuleStatePassenger(35, "Has villager passenger", EntityVillager.class);		
		new ModuleStatePassenger(36, "Has player passenger", EntityPlayer.class);		
		new ModuleStatePassenger(37, "Has zombie villager passenger", EntityZombie.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityZombie)passenger).isVillager();
			}
		};
		new ModuleStatePassenger(38, "Has child passenger", EntityAgeable.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityAgeable)passenger).isChild();
			}
		};	
		new ModuleStatePassenger(39, "Has tamed passenger", EntityTameable.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityTameable)passenger).isTamed();
			}		
		};
		
		new ModuleStatePower(42, "Is Power Observer[Red] active", 0);
		new ModuleStatePower(43, "Is Power Observer[Blue] active", 1);
		new ModuleStatePower(44, "Is Power Observer[Green] active", 2);
		new ModuleStatePower(45, "Is Power Observer[Yellow] active", 3);
		
		new ModuleStateTank(46, "Are the tanks completely full", true, false);		
		new ModuleStateTank(47, "Are the tanks completely empty", false, false);
		new ModuleStateTank(48, "Is there any completely empty tank", false, true);			
	}
	

	private Class<? extends ModuleBase> moduleClass;
	private String name;
	private byte id;
	private STATETYPE type;
	public ModuleState(int id, Class<? extends ModuleBase> moduleClass, String name, STATETYPE type) {
		this.moduleClass = moduleClass;
		this.name = name;
		this.id = (byte)id;
		this.type = type;

			
		states.put(this.id, this);
	}
	
	private static class ModuleStateInv extends ModuleState {
		private boolean full;
		public ModuleStateInv(int id, String name, boolean full) {
			super(id, ModuleChest.class, name, STATETYPE.INVENTORY);
			this.full = full;
		}
	}
	
	private static class ModuleStateTank extends ModuleState {
		private boolean full;
		private boolean individual;
		public ModuleStateTank(int id, String name, boolean full, boolean individual) {
			super(id, ModuleTank.class, name, STATETYPE.TANK);
			this.full = full;
			this.individual = individual;
		}
	}	
	
	private static class ModuleStatePassenger extends ModuleState {
		private Class passengerClass;
		public ModuleStatePassenger(int id, String name, Class passengerClass) {
			super(id, null, name, STATETYPE.PASSENGER);
			this.passengerClass = passengerClass;
		}
		
		public boolean isPassengerValid(Entity passenger) {
			return true;
		}
	}	
	
	private static class ModuleStatePower extends ModuleState {
		private int areaId;
		public ModuleStatePower(int id, String name, int areaId) {
			super(id, ModulePowerObserver.class, name, STATETYPE.POWER);
			this.areaId = areaId;
		}

	}		
	
	
	public boolean evaluate(MinecartModular cart) {
	
		switch (type) {
			case SUPPLY:
				for (ModuleBase module : cart.getModules()) {
					if (isModuleOfCorrectType(module) && module instanceof ISuppliesModule) {
						return ((ISuppliesModule)module).haveSupplies();
					}
				}
				break;
			case ACTIVATION:
				for (ModuleBase module : cart.getModules()) {
					if (isModuleOfCorrectType(module) && module instanceof IActivatorModule) {
						return ((IActivatorModule)module).isActive(0);
					}
				}				
				break;
			case INVENTORY:
				if (this instanceof ModuleStateInv) {
					boolean hasModule = false;
					for (ModuleBase module : cart.getModules()) {
						if (isModuleOfCorrectType(module)) {
							ModuleChest chest = (ModuleChest)module;
							
							if (((ModuleStateInv)this).full && !chest.isCompletelyFilled()) {
								return false;
							}else if (!((ModuleStateInv)this).full && !chest.isCompletelyEmpty()) {
								return false;
							}
							
							hasModule = true;
						}
					}
					return hasModule;
				}
				break;
			case PASSENGER:
				Entity passenger = cart.riddenByEntity;
				if (passenger != null) {
					return ((ModuleStatePassenger)this).passengerClass.isAssignableFrom(passenger.getClass()) && ((ModuleStatePassenger)this).isPassengerValid(passenger);
				}
				
				break;
			case POWER:
				for (ModuleBase module : cart.getModules()) {
					if (isModuleOfCorrectType(module)) {
						return ((ModulePowerObserver)module).isAreaActive(((ModuleStatePower)this).areaId);
					}
				}
				break;
			case TANK:
				if (this instanceof ModuleStateTank) {
					boolean hasModule = false;
					for (ModuleBase module : cart.getModules()) {
						if (isModuleOfCorrectType(module)) {
							ModuleTank tank = (ModuleTank)module;
							
							boolean result;
							if (((ModuleStateTank)this).full) {
								result = tank.isCompletelyFilled();
							}else{
								result = tank.isCompletelyEmpty();
							}
							
							if (result == ((ModuleStateTank)this).individual) {
								return result;
							}
							
							hasModule = !((ModuleStateTank)this).individual;
						}
					}
					return hasModule;
				}
				break;				
		}		

		return false;
	}
	
	private boolean isModuleOfCorrectType(ModuleBase module) {
		return moduleClass.isAssignableFrom(module.getClass());
	}
	
	public String getName() {
		return name;
	}
	
	public byte getID() {
		return id;
	}
	
	public enum STATETYPE {SUPPLY, ACTIVATION, INVENTORY, PASSENGER, POWER, TANK};

}