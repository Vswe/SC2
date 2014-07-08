package vswe.stevesvehicles.old.Helpers;

import vswe.stevesvehicles.localization.ILocalizedText;

public class TitleBox {
	private ILocalizedText name;
	private int x;
	private int y;
	private int color;
	public TitleBox(ILocalizedText name, int y, int color) {
		this(name, 16, y, color);
	}
	public TitleBox(ILocalizedText name, int x, int y, int color) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public ILocalizedText getName() {
		return name;
	}
	public int getColor() {
		return color;
	}
	public int getX() {
		return x;
	}	
	public int getY() {
		return y;
	}
}