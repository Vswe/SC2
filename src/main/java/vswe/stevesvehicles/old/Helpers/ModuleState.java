package vswe.stevesvehicles.old.Helpers;

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
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Modules.IActivatorModule;
import vswe.stevesvehicles.old.Modules.ISuppliesModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.old.Modules.Addons.ModuleChunkLoader;
import vswe.stevesvehicles.old.Modules.Addons.ModuleInvisible;
import vswe.stevesvehicles.old.Modules.Addons.ModulePowerObserver;
import vswe.stevesvehicles.old.Modules.Addons.ModuleShield;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCage;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCakeServer;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooter;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleChest;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleTank;
import vswe.stevesvehicles.module.cart.attachment.ModuleBridge;
import vswe.stevesvehicles.module.cart.attachment.ModuleFertilizer;
import vswe.stevesvehicles.module.cart.attachment.ModuleRailer;
import vswe.stevesvehicles.module.cart.attachment.ModuleTorch;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;
import vswe.stevesvehicles.module.cart.tool.ModuleFarmer;
import vswe.stevesvehicles.module.cart.tool.ModuleWoodcutter;
	
	
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
		new ModuleState(0, ModuleRailer.class, Localization.GUI.DETECTOR.RAIL, STATETYPE.SUPPLY);
		new ModuleState(1, ModuleTorch.class, Localization.GUI.DETECTOR.TORCH, STATETYPE.SUPPLY);
		new ModuleState(2, ModuleWoodcutter.class, Localization.GUI.DETECTOR.SAPLING, STATETYPE.SUPPLY);
		new ModuleState(3, ModuleFarmer.class, Localization.GUI.DETECTOR.SEED, STATETYPE.SUPPLY);
		//new ModuleState(4, ModuleHydrater.class, "Have water supplies", STATETYPE.SUPPLY);
		new ModuleState(5, ModuleBridge.class, Localization.GUI.DETECTOR.BRIDGE, STATETYPE.SUPPLY);
		new ModuleState(40, ModuleShooter.class, Localization.GUI.DETECTOR.PROJECTILE, STATETYPE.SUPPLY);
		new ModuleState(41, ModuleFertilizer.class,Localization.GUI.DETECTOR.FERTILIZING, STATETYPE.SUPPLY);
        new ModuleState(49, ModuleCakeServer.class, Localization.GUI.DETECTOR.CAKE, STATETYPE.SUPPLY);

		new ModuleState(6, ModuleShield.class, Localization.GUI.DETECTOR.SHIELD, STATETYPE.ACTIVATION);
		new ModuleState(7, ModuleChunkLoader.class, Localization.GUI.DETECTOR.CHUNK, STATETYPE.ACTIVATION);
		new ModuleState(8, ModuleInvisible.class, Localization.GUI.DETECTOR.INVISIBILITY, STATETYPE.ACTIVATION);
		new ModuleState(9, ModuleDrill.class, Localization.GUI.DETECTOR.DRILL, STATETYPE.ACTIVATION);
		new ModuleState(12, ModuleCage.class, Localization.GUI.DETECTOR.CAGE, STATETYPE.ACTIVATION);
		
		new ModuleStateInv(10, Localization.GUI.DETECTOR.STORAGE_FULL, true);
		new ModuleStateInv(11, Localization.GUI.DETECTOR.STORAGE_EMPTY, false);
		
		
		
		new ModuleStatePassenger(13, Localization.GUI.DETECTOR.PASSENGER, EntityLiving.class);
		new ModuleStatePassenger(14, Localization.GUI.DETECTOR.ANIMAL, IAnimals.class);
		new ModuleStatePassenger(15, Localization.GUI.DETECTOR.TAMEABLE, EntityTameable.class);
		new ModuleStatePassenger(16, Localization.GUI.DETECTOR.BREEDABLE, EntityAgeable.class);
		new ModuleStatePassenger(17, Localization.GUI.DETECTOR.HOSTILE, IMob.class);
		
		new ModuleStatePassenger(18, Localization.GUI.DETECTOR.CREEPER, EntityCreeper.class);
		new ModuleStatePassenger(19, Localization.GUI.DETECTOR.SKELETON, EntitySkeleton.class);
		new ModuleStatePassenger(20, Localization.GUI.DETECTOR.SPIDER, EntitySpider.class);
		new ModuleStatePassenger(21, Localization.GUI.DETECTOR.ZOMBIE, EntityZombie.class);
		new ModuleStatePassenger(22, Localization.GUI.DETECTOR.PIG_MAN, EntityPigZombie.class);
		new ModuleStatePassenger(23, Localization.GUI.DETECTOR.SILVERFISH, EntitySilverfish.class);
		new ModuleStatePassenger(24, Localization.GUI.DETECTOR.BLAZE, EntityBlaze.class);
		new ModuleStatePassenger(25, Localization.GUI.DETECTOR.BAT, EntityBat.class);
		new ModuleStatePassenger(26, Localization.GUI.DETECTOR.WITCH, EntityWitch.class);
		new ModuleStatePassenger(27, Localization.GUI.DETECTOR.PIG, EntityPig.class);
		new ModuleStatePassenger(28, Localization.GUI.DETECTOR.SHEEP, EntitySheep.class);
		new ModuleStatePassenger(29, Localization.GUI.DETECTOR.COW, EntityCow.class);
		new ModuleStatePassenger(30, Localization.GUI.DETECTOR.MOOSHROOM, EntityMooshroom.class);
		new ModuleStatePassenger(31, Localization.GUI.DETECTOR.CHICKEN, EntityChicken.class);
		new ModuleStatePassenger(32, Localization.GUI.DETECTOR.WOLF, EntityWolf.class);
		new ModuleStatePassenger(33, Localization.GUI.DETECTOR.SNOW_GOLEM, EntitySnowman.class);
		new ModuleStatePassenger(34, Localization.GUI.DETECTOR.OCELOT, EntityOcelot.class);
		new ModuleStatePassenger(35, Localization.GUI.DETECTOR.VILLAGER, EntityVillager.class);
		new ModuleStatePassenger(36, Localization.GUI.DETECTOR.PLAYER, EntityPlayer.class);
		new ModuleStatePassenger(37, Localization.GUI.DETECTOR.ZOMBIE, EntityZombie.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityZombie)passenger).isVillager();
			}
		};
		new ModuleStatePassenger(38, Localization.GUI.DETECTOR.CHILD, EntityAgeable.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityAgeable)passenger).isChild();
			}
		};	
		new ModuleStatePassenger(39, Localization.GUI.DETECTOR.TAMED, EntityTameable.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityTameable)passenger).isTamed();
			}		
		};
		
		new ModuleStatePower(42, Localization.GUI.DETECTOR.POWER_RED, 0);
		new ModuleStatePower(43, Localization.GUI.DETECTOR.POWER_BLUE, 1);
		new ModuleStatePower(44, Localization.GUI.DETECTOR.POWER_GREEN, 2);
		new ModuleStatePower(45, Localization.GUI.DETECTOR.POWER_YELLOW, 3);
		
		new ModuleStateTank(46, Localization.GUI.DETECTOR.TANKS_FULL, true, false);
		new ModuleStateTank(47, Localization.GUI.DETECTOR.TANKS_EMPTY, false, false);
		new ModuleStateTank(48, Localization.GUI.DETECTOR.TANK_EMPTY, false, true);


	}
	

	private Class<? extends ModuleBase> moduleClass;
	private ILocalizedText name;
	private byte id;
	private STATETYPE type;
	public ModuleState(int id, Class<? extends ModuleBase> moduleClass, ILocalizedText name, STATETYPE type) {
		this.moduleClass = moduleClass;
		this.name = name;
		this.id = (byte)id;
		this.type = type;

			
		states.put(this.id, this);
	}
	
	private static class ModuleStateInv extends ModuleState {
		private boolean full;
		public ModuleStateInv(int id, ILocalizedText name, boolean full) {
			super(id, ModuleChest.class, name, STATETYPE.INVENTORY);
			this.full = full;
		}
	}
	
	private static class ModuleStateTank extends ModuleState {
		private boolean full;
		private boolean individual;
		public ModuleStateTank(int id, ILocalizedText name, boolean full, boolean individual) {
			super(id, ModuleTank.class, name, STATETYPE.TANK);
			this.full = full;
			this.individual = individual;
		}
	}	
	
	private static class ModuleStatePassenger extends ModuleState {
		private Class passengerClass;
		public ModuleStatePassenger(int id, ILocalizedText name, Class passengerClass) {
			super(id, null, name, STATETYPE.PASSENGER);
			this.passengerClass = passengerClass;
		}
		
		public boolean isPassengerValid(Entity passenger) {
			return true;
		}
	}	
	
	private static class ModuleStatePower extends ModuleState {
		private int areaId;
		public ModuleStatePower(int id, ILocalizedText name, int areaId) {
			super(id, ModulePowerObserver.class, name, STATETYPE.POWER);
			this.areaId = areaId;
		}

	}		
	
	
	public boolean evaluate(EntityModularCart cart) {
	
		switch (type) {
			case SUPPLY:
				for (ModuleBase module : cart.getVehicle().getModules()) {
					if (isModuleOfCorrectType(module) && module instanceof ISuppliesModule) {
						return ((ISuppliesModule)module).haveSupplies();
					}
				}
				break;
			case ACTIVATION:
				for (ModuleBase module : cart.getVehicle().getModules()) {
					if (isModuleOfCorrectType(module) && module instanceof IActivatorModule) {
						return ((IActivatorModule)module).isActive(0);
					}
				}				
				break;
			case INVENTORY:
				if (this instanceof ModuleStateInv) {
					boolean hasModule = false;
					for (ModuleBase module : cart.getVehicle().getModules()) {
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
				for (ModuleBase module : cart.getVehicle().getModules()) {
					if (isModuleOfCorrectType(module)) {
						return ((ModulePowerObserver)module).isAreaActive(((ModuleStatePower)this).areaId);
					}
				}
				break;
			case TANK:
				if (this instanceof ModuleStateTank) {
					boolean hasModule = false;
					for (ModuleBase module : cart.getVehicle().getModules()) {
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
		return name.translate();
	}
	
	public byte getID() {
		return id;
	}
	
	public enum STATETYPE {SUPPLY, ACTIVATION, INVENTORY, PASSENGER, POWER, TANK};

}