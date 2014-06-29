package vswe.stevesvehicles.old.Helpers;

import vswe.stevesvehicles.localization.ILocalizedText;

public abstract class LabelInformation {

	private ILocalizedText name;
	public LabelInformation(ILocalizedText name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name.translate();
	}
	
	public abstract String getLabel();


	

	
}
