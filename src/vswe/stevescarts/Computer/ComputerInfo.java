package vswe.stevescarts.Computer;

import java.util.Collection;
import java.util.HashMap;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleColorizer;
import vswe.stevescarts.Modules.Addons.ModuleHeightControl;
import vswe.stevescarts.Modules.Realtimers.ModuleDynamite;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevescarts.Buttons.ButtonBase;
import vswe.stevescarts.Buttons.ButtonInfoType;

public class ComputerInfo {
	private static HashMap<Byte, ComputerInfo> infos;
	
	public static HashMap<Byte, ComputerInfo> getMap() {
		return infos;
	}
	
	public static Collection<ComputerInfo> getList() {
		return infos.values();
	}
	
	public static void createButtons(MinecartModular cart, ModuleComputer assembly) {
		for (ComputerInfo info : getList()) {
			if (info.isInfoValid(cart)) {
				new ButtonInfoType(assembly, ButtonBase.LOCATION.TASK, info.id);
			}
		}
	}

	
	static {
		infos = new HashMap<Byte, ComputerInfo>();
	
		new ComputerInfo(1, "Light threshold [0-15]", 84, ModuleTorch.class) {
			protected int get(ModuleBase module) {
				return ((ModuleTorch)module).getThreshold();
			}			
		};	

		new ComputerInfo(2, "Light level [0-15]", 85, ModuleTorch.class) {
			protected int get(ModuleBase module) {
				return ((ModuleTorch)module).getLightLevel();
			}			
		};	

		new ComputerInfo(3, "Shield [0-1]", 86, ModuleShield.class) {
			protected int get(ModuleBase module) {
				return ((ModuleShield)module).isActive(0) ? 1 : 0;
			}			
		};	

		new ComputerInfo(4, "Drill [0-1]", 87, ModuleDrill.class) {
			protected int get(ModuleBase module) {
				return ((ModuleDrill)module).isActive(0) ? 1 : 0;
			}			
		};	

		new ComputerInfo(5, "Invisibility core [0-1]", 88, ModuleInvisible.class) {
			protected int get(ModuleBase module) {
				return ((ModuleInvisible)module).isActive(0) ? 1 : 0;
			}			
		};

		new ComputerInfo(6, "Chunk loader [0-1]", 89, ModuleChunkLoader.class) {
			protected int get(ModuleBase module) {
				return ((ModuleChunkLoader)module).isActive(0) ? 1 : 0;
			}			
		};	

		new ComputerInfo(7, "Fuse Length [2-127]", 90, ModuleDynamite.class) {
			protected int get(ModuleBase module) {
				return clamp((byte)((ModuleDynamite)module).getFuseLength(), 2, 127);
			}	
		};	

		new ComputerInfo(8, "Active Pipes", 91, ModuleShooter.class) {
			protected int get(ModuleBase module) {
				return ((ModuleShooter)module).getActivePipes();
			}
			
			protected boolean isValid(ModuleBase module) {
				return !(module instanceof ModuleShooterAdv);
			}			
		};		

		new ComputerInfo(9, "Selected Target", 92, ModuleShooterAdv.class) {
			protected int get(ModuleBase module) {
				return ((ModuleShooterAdv)module).selectedOptions();
			}					
		};

		new ComputerInfo(10, "Red [0-64]", 93, ModuleColorizer.class) {
			protected int get(ModuleBase module) {
				return processColor(((ModuleColorizer)module).getColorVal(0));
			}					
		};			
	
		new ComputerInfo(11, "Green [0-64]", 94, ModuleColorizer.class) {
			protected int get(ModuleBase module) {
				return processColor(((ModuleColorizer)module).getColorVal(1));
			}					
		};	
	
		new ComputerInfo(12, "Blue [0-64]", 95, ModuleColorizer.class) {
			protected int get(ModuleBase module) {
				return processColor(((ModuleColorizer)module).getColorVal(2));
			}					
		};	

		new ComputerInfo(13, "Y target [-128-127]", 96, ModuleHeightControl.class) {
			protected int get(ModuleBase module) {
				return ((ModuleHeightControl)module).getYTarget()-128;
			}					
		};

		new ComputerInfo(14, "Y level [-128-127]", 97, ModuleHeightControl.class) {
			protected int get(ModuleBase module) {
				return clamp((byte)(module.getCart().posY-128), -128, 127);
			}					
		};			
	}
	
	private static int processColor(int val) {
		if (val == 255) {
			return 64;
		}else{
			return val / 4;
		}	
	}
	
	private static byte clamp(byte val, int min, int max) {
		return (byte)Math.max((byte)min, (byte)Math.min(val, (byte)max));
	}	
	
	private Class<? extends ModuleBase> moduleClass;
	private byte id;
	private String name;
	private int texture;
	
	public ComputerInfo(int id, String name, int texture, Class<? extends ModuleBase> moduleClass){
		this.moduleClass = moduleClass;
		this.name = name;
		this.id = (byte)id;
		this.texture = texture;
		
		infos.put(this.id, this);
	}
	
	public boolean isInfoValid(MinecartModular cart) {
		for(ModuleBase module : cart.getModules()) {
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
	
	public void getHandler(MinecartModular cart, ComputerVar var) {
		for(ModuleBase module : cart.getModules()) {
			if (moduleClass.isAssignableFrom(module.getClass())) {
				if (isValid(module)) {
					var.setByteValue(get(module));
					break;
				}
			}
		}
	}
	

	protected int get(ModuleBase module) {
		return 0;
	}
	

	protected boolean isValid(ModuleBase module) {
		return true;
	}
	
	
}