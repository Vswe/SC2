package vswe.stevesvehicles.client.gui;

public enum ColorHelper {
	BLACK,
	BLUE,
	GREEN,
	CYAN,
	RED,
	PURPLE,
	ORANGE,
	LIGHT_GRAY,
	GRAY,
	LIGHT_BLUE,
	LIME,
	TURQUOISE,
	PINK,
	MAGENTA,
	YELLOW,
	WHITE;

	@Override
	public String toString() {
		return "§" + Integer.toHexString(ordinal());
	}
	
}
