package vswe.stevesvehicles.old.Computer;

import java.util.Collection;
import java.util.HashMap;

import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.module.cart.attachment.ModuleTorch;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;
import vswe.stevesvehicles.module.common.addon.ModuleShield;
import vswe.stevesvehicles.module.common.addon.ModuleInvisible;
import vswe.stevesvehicles.module.common.addon.ModuleChunkLoader;
import vswe.stevesvehicles.module.common.addon.ModuleColorizer;
import vswe.stevesvehicles.module.cart.addon.ModuleHeightControl;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleDynamite;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooter;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleFirework;
import vswe.stevesvehicles.old.Buttons.ButtonBase;
import vswe.stevesvehicles.old.Buttons.ButtonControlType;

public class ComputerControl {
	private static HashMap<Byte, ComputerControl> controls;
	
	public static HashMap<Byte, ComputerControl> getMap() {
		return controls;
	}
	
	public static Collection<ComputerControl> getList() {
		return controls.values();
	}
	
	public static void createButtons(EntityModularCart cart, ModuleComputer assembly) {
		for (ComputerControl control : getList()) {
			if (control.isControlValid(cart)) {
				new ButtonControlType(assembly, ButtonBase.LOCATION.TASK, control.id);
			}
		}
	}

	
	static {
		controls = new HashMap<Byte, ComputerControl>();
	
		new ComputerControl(1, "Light threshold [0-15]", 69, ModuleTorch.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleTorch)module).setThreshold(val);
			}
			protected int getMin() {
				return 0;
			}
			protected int getMax() {
				return 15;
			}			
		};
		
		new ComputerControl(2, "Shield", 70, ModuleShield.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleShield)module).setShieldStatus(val != 0);
			}
			protected boolean isBoolean() {
				return true;
			}			
		};	

		new ComputerControl(3, "Drill", 71, ModuleDrill.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleDrill)module).setDrillEnabled(val != 0);
			}
			protected boolean isBoolean() {
				return true;
			}			
		};	

		new ComputerControl(4, "Invisibility Core", 72, ModuleInvisible.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleInvisible)module).setIsVisible(val == 0);
			}
			protected boolean isBoolean() {
				return true;
			}			
		};			
		
		new ComputerControl(5, "Chunk loader", 73, ModuleChunkLoader.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleChunkLoader)module).setChunkLoading(val != 0);
			}
			protected boolean isBoolean() {
				return true;
			}
		};

		new ComputerControl(6, "Fuse length [2-127]", 74, ModuleDynamite.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleDynamite)module).setFuseLength(val);
			}
			protected int getMin() {
				return 2;
			}
		};	

		new ComputerControl(7, "Prime", 75, ModuleDynamite.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleDynamite)module).prime();
			}
			protected boolean isActivator() {
				return true;
			}
		};	

		new ComputerControl(8, "Active pipes", 76, ModuleShooter.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleShooter)module).setActivePipes(val);
			}
			
			protected boolean isValid(ModuleBase module) {
				return !(module instanceof ModuleShooterAdv);
			}			
		};

		new ComputerControl(9, "Selected targets", 77, ModuleShooterAdv.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleShooterAdv)module).setOptions(val);
			}						
		};	

		new ComputerControl(10, "Fire", 78, ModuleFirework.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleFirework)module).fire();
			}
			protected boolean isActivator() {
				return true;
			}
		};	

		new ComputerControl(11, "Red [0-64]", 79, ModuleColorizer.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleColorizer)module).setColorVal(0,(byte)Math.min(val*4,255));
			}
			protected int getMin() {
				return 0;
			}
			protected int getMax() {
				return 64;
			}	
		};	

		new ComputerControl(12, "Green [0-64]", 80, ModuleColorizer.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleColorizer)module).setColorVal(1,(byte)Math.min(val*4,255));
			}
			protected int getMin() {
				return 0;
			}
			protected int getMax() {
				return 64;
			}	
		};		

		new ComputerControl(13, "Blue [0-64]", 81, ModuleColorizer.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleColorizer)module).setColorVal(2,(byte)Math.min(val*4,255));
			}
			protected int getMin() {
				return 0;
			}
			protected int getMax() {
				return 64;
			}	
		};		

		new ComputerControl(14, "Y target [-128-127]", 85, ModuleHeightControl.class) {
			protected void run(ModuleBase module, byte val) {
				((ModuleHeightControl)module).setYTarget(val+128);
			}
		};			
	}
	
	private byte clamp(byte val, int min, int max) {
		return (byte)Math.max((byte)min, (byte)Math.min(val, (byte)max));
	}

	private Class<? extends ModuleBase> moduleClass;
	private byte id;
	private String name;
	private int texture;
	
	public ComputerControl(int id, String name, int texture, Class<? extends ModuleBase> moduleClass){
		this.moduleClass = moduleClass;
		this.name = name;
		this.id = (byte)id;
		this.texture = texture;
		
		controls.put(this.id, this);
	}
	
	public boolean isControlValid(EntityModularCart cart) {
		for(ModuleBase module : cart.getVehicle().getModules()) {
			if (moduleClass.isAssignableFrom(module.getClass())) {
				if (isValid(module)) {
					return true;
				}
			}
		}	
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public void runHandler(EntityModularCart cart, byte val) {
		for(ModuleBase module : cart.getVehicle().getModules()) {
			if (moduleClass.isAssignableFrom(module.getClass())) {
				if (isValid(module)) {
					run(module, clamp(val,(byte)getIntegerMin(), (byte)getIntegerMax()));
					break;
				}
			}
		}
	}
	
	public int getIntegerMin() {
		if (isBoolean()) {
			return 0;
		}else{
			return getMin();
		}
	}
	
	public int getIntegerMax() {
		if (isBoolean()) {
			return 1;
		}else{
			return getMax();
		}
	}

	public boolean useIntegerOfSize(int size) {
		return !(isBoolean() && size > 1);
	}
	
	
	protected boolean isBoolean() {
		return false;
	}	
	
	protected boolean isActivator() {
		return false;
	}		
	
	protected void run(ModuleBase module, byte val) {
	
	}
	
	protected int getMin() {
		return -127;
	}
	
	protected int getMax() {
		return 128;
	}
	
	protected boolean isValid(ModuleBase module) {
		return true;
	}
	
	
}