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
import vswe.stevesvehicles.localization.entry.block.LocalizationDetector;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.module.IActivatorModule;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.addon.ModuleChunkLoader;
import vswe.stevesvehicles.module.common.addon.ModuleInvisible;
import vswe.stevesvehicles.module.common.addon.ModulePowerObserver;
import vswe.stevesvehicles.module.common.addon.ModuleShield;
import vswe.stevesvehicles.module.common.attachment.ModuleCage;
import vswe.stevesvehicles.module.common.attachment.ModuleCakeServer;
import vswe.stevesvehicles.module.common.attachment.ModuleShooter;
import vswe.stevesvehicles.module.common.storage.chest.ModuleChest;
import vswe.stevesvehicles.module.common.storage.tank.ModuleTank;
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
		new ModuleState(0, ModuleRailer.class, LocalizationDetector.RAIL_SUPPLIES, STATETYPE.SUPPLY);
		new ModuleState(1, ModuleTorch.class, LocalizationDetector.TORCH_SUPPLIES, STATETYPE.SUPPLY);
		new ModuleState(2, ModuleWoodcutter.class, LocalizationDetector.SAPLING_SUPPLIES, STATETYPE.SUPPLY);
		new ModuleState(3, ModuleFarmer.class, LocalizationDetector.SEED_SUPPLIES, STATETYPE.SUPPLY);

		new ModuleState(5, ModuleBridge.class, LocalizationDetector.BRIDGE_SUPPLIES, STATETYPE.SUPPLY);
		new ModuleState(40, ModuleShooter.class, LocalizationDetector.PROJECTILE_SUPPLIES, STATETYPE.SUPPLY);
		new ModuleState(41, ModuleFertilizer.class,LocalizationDetector.FERTILIZER_SUPPLIES, STATETYPE.SUPPLY);
        new ModuleState(49, ModuleCakeServer.class, LocalizationDetector.CAKE_SUPPLIES, STATETYPE.SUPPLY);

		new ModuleState(6, ModuleShield.class, LocalizationDetector.SHIELD_ACTIVE, STATETYPE.ACTIVATION);
		new ModuleState(7, ModuleChunkLoader.class, LocalizationDetector.CHUNK_ACTIVE, STATETYPE.ACTIVATION);
		new ModuleState(8, ModuleInvisible.class, LocalizationDetector.INVISIBILITY_ACTIVE, STATETYPE.ACTIVATION);
		new ModuleState(9, ModuleDrill.class, LocalizationDetector.DRILL_ACTIVE, STATETYPE.ACTIVATION);
		new ModuleState(12, ModuleCage.class, LocalizationDetector.CAGE_ACTIVE, STATETYPE.ACTIVATION);
		
		new ModuleStateInv(10, LocalizationDetector.STORAGE_FULL, true);
		new ModuleStateInv(11, LocalizationDetector.STORAGE_EMPTY, false);
		
		
		
		new ModuleStatePassenger(13, LocalizationDetector.PASSENGER, EntityLiving.class);
		new ModuleStatePassenger(14, LocalizationDetector.ANIMAL_PASSENGER, IAnimals.class);
		new ModuleStatePassenger(15, LocalizationDetector.TAMEABLE_PASSENGER, EntityTameable.class);
		new ModuleStatePassenger(16, LocalizationDetector.BREEDABLE_PASSENGER, EntityAgeable.class);
		new ModuleStatePassenger(17, LocalizationDetector.HOSTILE_PASSENGER, IMob.class);
		
		new ModuleStatePassenger(18, LocalizationDetector.CREEPER_PASSENGER, EntityCreeper.class);
		new ModuleStatePassenger(19, LocalizationDetector.SKELETON_PASSENGER, EntitySkeleton.class);
		new ModuleStatePassenger(20, LocalizationDetector.SPIDER_PASSENGER, EntitySpider.class);
		new ModuleStatePassenger(21, LocalizationDetector.ZOMBIE_PASSENGER, EntityZombie.class);
		new ModuleStatePassenger(22, LocalizationDetector.ZOMBIE_PIG_MAN_PASSENGER, EntityPigZombie.class);
		new ModuleStatePassenger(23, LocalizationDetector.SILVERFISH_PASSENGER, EntitySilverfish.class);
		new ModuleStatePassenger(24, LocalizationDetector.BLAZE_PASSENGER, EntityBlaze.class);
		new ModuleStatePassenger(25, LocalizationDetector.BAT_PASSENGER, EntityBat.class);
		new ModuleStatePassenger(26, LocalizationDetector.WITCH_PASSENGER, EntityWitch.class);
		new ModuleStatePassenger(27, LocalizationDetector.PIG_PASSENGER, EntityPig.class);
		new ModuleStatePassenger(28, LocalizationDetector.SHEEP_PASSENGER, EntitySheep.class);
		new ModuleStatePassenger(29, LocalizationDetector.COW_PASSENGER, EntityCow.class);
		new ModuleStatePassenger(30, LocalizationDetector.MOOSHROOM_PASSENGER, EntityMooshroom.class);
		new ModuleStatePassenger(31, LocalizationDetector.CHICKEN_PASSENGER, EntityChicken.class);
		new ModuleStatePassenger(32, LocalizationDetector.WOLF_PASSENGER, EntityWolf.class);
		new ModuleStatePassenger(33, LocalizationDetector.SNOW_GOLEM_PASSENGER, EntitySnowman.class);
		new ModuleStatePassenger(34, LocalizationDetector.OCELOT_PASSENGER, EntityOcelot.class);
		new ModuleStatePassenger(35, LocalizationDetector.VILLAGER_PASSENGER, EntityVillager.class);
		new ModuleStatePassenger(36, LocalizationDetector.PLAYER_PASSENGER, EntityPlayer.class);
		new ModuleStatePassenger(37, LocalizationDetector.ZOMBIE_PASSENGER, EntityZombie.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityZombie)passenger).isVillager();
			}
		};
		new ModuleStatePassenger(38, LocalizationDetector.CHILD_PASSENGER, EntityAgeable.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityAgeable)passenger).isChild();
			}
		};	
		new ModuleStatePassenger(39, LocalizationDetector.TAMED_PASSENGER, EntityTameable.class) {
			public boolean isPassengerValid(Entity passenger) {
				return ((EntityTameable)passenger).isTamed();
			}		
		};
		
		new ModuleStatePower(42, LocalizationDetector.RED_OBSERVER, 0);
		new ModuleStatePower(43, LocalizationDetector.BLUE_OBSERVER, 1);
		new ModuleStatePower(44, LocalizationDetector.GREEN_OBSERVER, 2);
		new ModuleStatePower(45, LocalizationDetector.YELLOW_OBSERVER, 3);
		
		new ModuleStateTank(46, LocalizationDetector.TANKS_FULL, true, false);
		new ModuleStateTank(47, LocalizationDetector.TANKS_EMPTY, false, false);
		new ModuleStateTank(48, LocalizationDetector.TANKS_SPARE, false, true);


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