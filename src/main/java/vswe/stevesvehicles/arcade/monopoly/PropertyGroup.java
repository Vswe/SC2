package vswe.stevesvehicles.arcade.monopoly;

import java.util.ArrayList;

public class PropertyGroup {

	private ArrayList<Property> properties;
	
	public PropertyGroup() {
		properties = new ArrayList<Property>();
	}
	
	public ArrayList<Property> getProperties() {
		return properties;
	}

	public void add(Property property) {
		properties.add(property);
	}
					

}
