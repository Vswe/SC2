package vswe.stevesvehicles.old.ModuleData;

import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.modules.ModuleBase;

public class ModuleDataTool extends ModuleData {

	public ModuleDataTool(int id, String name, Class<? extends ModuleBase> moduleClass, int modularCost, boolean unbreakable) {
		super(id, name, moduleClass, modularCost);

		useExtraData((byte)100);
		this.unbreakable = unbreakable;
	}

	private boolean unbreakable;
	
	
	@Override
	public String getModuleInfoText(byte b) {
		if (unbreakable) {
			return Localization.MODULE_INFO.TOOL_UNBREAKABLE.translate();
		}else{
			return Localization.MODULE_INFO.TOOL_DURABILITY.translate(String.valueOf(b));
		}
	}
	
	@Override
	public String getCartInfoText(String name, byte b) {
		if (unbreakable) {
			return super.getCartInfoText(name, b);
		}else{
			return name + " [" + b + "%]";
		}
	}
	
	
}
