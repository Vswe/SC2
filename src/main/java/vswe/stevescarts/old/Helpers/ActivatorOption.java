package vswe.stevescarts.old.Helpers;
import vswe.stevescarts.modules.ModuleBase;
	
public class ActivatorOption {

	private Class<? extends ModuleBase> module;
	private int id;
	private Localization.GUI.TOGGLER name;
	private int option;
	
	public ActivatorOption(Localization.GUI.TOGGLER name, Class<? extends ModuleBase> module, int id) {
		this.name = name;
		this.module = module;
		this.id = id;
	}
	
	public ActivatorOption(Localization.GUI.TOGGLER name, Class<? extends ModuleBase> module) {
		this(name, module, 0);
	}	
	
	public Class<? extends ModuleBase> getModule() {
		return module;
	}
		
	public String getName() {
		return name.translate();
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
			return Localization.GUI.TOGGLER.SETTING_DISABLED.translate();
		}else{
			return "\u00a76" + Localization.GUI.TOGGLER.SETTING_ORANGE.translate() + ": " + (shouldActivate(true) ? "\u00a72" + Localization.GUI.TOGGLER.STATE_ACTIVATE.translate() : shouldDeactivate(true) ? "\u00a74" + Localization.GUI.TOGGLER.STATE_DEACTIVATE.translate() : "\u00a7E" + Localization.GUI.TOGGLER.STATE_TOGGLE.translate()) + "\n" +
				   "\u00a71" + Localization.GUI.TOGGLER.SETTING_BLUE.translate() + ": " + (shouldActivate(false) ? "\u00a72" + Localization.GUI.TOGGLER.STATE_ACTIVATE.translate() : shouldDeactivate(false) ? "\u00a74" + Localization.GUI.TOGGLER.STATE_DEACTIVATE.translate() : "\u00a7E" + Localization.GUI.TOGGLER.STATE_TOGGLE.translate());
		}
	}
}