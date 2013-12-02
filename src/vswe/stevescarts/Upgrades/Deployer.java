package vswe.stevescarts.Upgrades;


import vswe.stevescarts.Helpers.Localization;

public class Deployer extends BaseEffect {



	public Deployer() {
		super();
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.DEPLOYER.translate();
	}
	

}