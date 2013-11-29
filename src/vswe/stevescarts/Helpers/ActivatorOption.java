package vswe.stevescarts.Helpers;
import vswe.stevescarts.Modules.ModuleBase;
	
public class ActivatorOption {

	private Class<? extends ModuleBase> module;
	private int id;
	private String name;
	private int option;
	
	public ActivatorOption(String name, Class<? extends ModuleBase> module, int id) {
		this.name = name;
		this.module = module;
		this.id = id;
	}
	
	public ActivatorOption(String name, Class<? extends ModuleBase> module) {
		this(name, module, 0);
	}	
	
	public Class<? extends ModuleBase> getModule() {
		return module;
	}
		
	public String getName() {
		return name;
	}
	
	public int getOption() {
		return option;
	}
	
	public int getId() {
		return id;
	}
	
	public void setOption(int val) {
		option = val;
	}
	
	public void changeOption(boolean dif) {
		if (dif) {
			if (++option > 5) {
				option = 0;
			}
		}else if (--option < 0) {
			option = 5;
		}
	}
	
	public boolean isDisabled() {
		return option == 0;
	}
	
	public boolean shouldActivate(boolean isOrange) {
		return option == 2 || (option == 4 && !isOrange) || (option == 5 && isOrange);
	}
	
	public boolean shouldDeactivate(boolean isOrange) {
		return option == 1 || (option == 4 && isOrange) || (option == 5 && !isOrange);
	}	
	
	public boolean shouldToggle() {
		return option == 3;
	}	

	public String getInfo() {
		if (isDisabled()) {
			return "Disabled";
		}else{
			return "\u00a76Orange direction: " + (shouldActivate(true) ? "\u00a72Activate" : shouldDeactivate(true) ? "\u00a74Deactivate" : "\u00a7EToggle") + "\n" +
				   "\u00a71Blue direction: " + (shouldActivate(false) ? "\u00a72Activate" : shouldDeactivate(false) ? "\u00a74Deactivate" : "\u00a7EToggle");
		}
	}
}