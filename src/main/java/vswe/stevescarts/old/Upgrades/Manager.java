package vswe.stevescarts.old.Upgrades;


import vswe.stevescarts.old.Helpers.Localization;

public class Manager extends BaseEffect {



	public Manager() {
		super();
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.BRIDGE.translate();
	}
	

}