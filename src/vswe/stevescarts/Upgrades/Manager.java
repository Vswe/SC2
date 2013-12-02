package vswe.stevescarts.Upgrades;


import vswe.stevescarts.Helpers.Localization;

public class Manager extends BaseEffect {



	public Manager() {
		super();
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.BRIDGE.translate();
	}
	

}