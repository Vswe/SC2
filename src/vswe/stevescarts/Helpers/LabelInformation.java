package vswe.stevescarts.Helpers;

public abstract class LabelInformation {

	private String name;
	private boolean isActive;
	public LabelInformation(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	public abstract String getLabel();

	public void setActive(boolean val) {
		isActive = val;
	}
	

	
}
