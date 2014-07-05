package vswe.stevesvehicles.old.Helpers;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.entry.gui.block.LocalizationToggler;
import vswe.stevesvehicles.module.ModuleBase;
	
public class ActivatorOption {

	private Class<? extends ModuleBase> module;
	private int id;
	private ILocalizedText name;
	private int option;
	
	public ActivatorOption(ILocalizedText name, Class<? extends ModuleBase> module, int id) {
		this.name = name;
		this.module = module;
		this.id = id;
	}
	
	public ActivatorOption(ILocalizedText name, Class<? extends ModuleBase> module) {
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
			return LocalizationToggler.DISABLED_SETTING.translate();
		}else{
			return "\u00a76" + LocalizationToggler.ORANGE_SETTING.translate() + ": " + (shouldActivate(true) ? "\u00a72" + LocalizationToggler.ACTIVATE_STATE.translate() : shouldDeactivate(true) ? "\u00a74" + LocalizationToggler.DEACTIVATE_STATE.translate() : "\u00a7E" + LocalizationToggler.TOGGLE_STATE.translate()) + "\n" +
				   "\u00a71" + LocalizationToggler.BLUE_SETTING.translate() + ": " + (shouldActivate(false) ? "\u00a72" + LocalizationToggler.ACTIVATE_STATE.translate() : shouldDeactivate(false) ? "\u00a74" + LocalizationToggler.DEACTIVATE_STATE.translate() : "\u00a7E" + LocalizationToggler.TOGGLE_STATE.translate());

		}
	}
}