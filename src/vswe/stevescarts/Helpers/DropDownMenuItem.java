package vswe.stevescarts.Helpers;
import vswe.stevescarts.Modules.ModuleBase;


public class DropDownMenuItem {

	private String name;
	private int imageID;
	private VALUETYPE type;
	private boolean isLarge;
	private boolean subOpen;
	private byte value;
	private Class<? extends ModuleBase> moduleClass;
	private Class<? extends ModuleBase> excludedClass;
	private int multiCount;
	private int intMinValue;
	private int intMaxValue;

	public DropDownMenuItem(String name, int imageID, VALUETYPE type, Class<? extends ModuleBase> moduleClass) {
		this(name, imageID,type, moduleClass, null);
	}
	
	
	public DropDownMenuItem(String name, int imageID, VALUETYPE type, Class<? extends ModuleBase> moduleClass,  Class<? extends ModuleBase> excludedClass) {
		this.name = name;
		this.imageID = imageID;
		this.type = type;
		this.moduleClass = moduleClass;
		this.excludedClass = excludedClass;
		isLarge = false;
		subOpen = false;
		value = (byte)0;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends ModuleBase> getModuleClass() {
		return moduleClass;
	}
	
	public Class<? extends ModuleBase> getExcludedClass() {
		return excludedClass;
	}	
	
	public int getImageID() {
		return imageID;
	}
	
	public boolean hasSubmenu() {
		return type != VALUETYPE.BOOL;
	}
	
	public boolean getIsSubMenuOpen() {
		return subOpen;
	}
	
	public void setIsSubMenuOpen(boolean val) {
		subOpen = val;
	}	
	
	public boolean getIsLarge() {
		return isLarge;
	}
	
	public void setIsLarge(boolean val) {
		isLarge = val;
	}
	
	public int[] getRect(int menuX, int menuY, int id) {
		if (getIsLarge()) {
			return new int[] {menuX, menuY + id * 20, 130,20};
		}else{
			return new int[]{menuX, menuY + id * 20, 54,20};
		}
	}
	
	public int[] getSubRect(int menuX, int menuY, int id) {
		if (getIsSubMenuOpen()) {
			return new int[] {menuX - 43, menuY + id * 20 + 2, 52,16};
		}else{
			return new int[]{menuX, menuY + id * 20 + 2, 9,16};
		}
	}	
	
	public VALUETYPE getType() {
		return type;
	}
	
	public boolean getBOOL() {
		return value != 0;
	}
	
	public void setBOOL(boolean val) {
		value = (byte)(val ? 1 : 0);
	}
	
	public int getINT() {
		return value;
	}
	
	public void setINT(int val) {
		if (val < intMinValue) {
			val = intMinValue;
		}else if (val > intMaxValue) {
			val = intMaxValue;
		}
		value = (byte)val;
	}	
	
	public void setMULTIBOOL(byte val) {
		value = val;
	}
	
	public void setMULTIBOOL(int i, boolean val) {
		value = (byte)((value & ~(1<<i)) | ((val ? 1 : 0) << i));
	}
	
	public byte getMULTIBOOL() {
		return value;
	}
	
	public boolean getMULTIBOOL(int i) {
		return (value & (1<<i)) != 0;
	}
	
	public void setMULTIBOOLCount(int val) {
		if (val > 4) {
			val = 4;
		}else if (val < 2) {
			val = 2;
		}
		multiCount = val;
	}
	
	public int getMULTIBOOLCount() {
		return multiCount;
	}

	public void setINTLimit(int min, int max) {
		intMinValue = min;
		intMaxValue = max;
		
		setINT(getINT());
	}
	
	
	public enum VALUETYPE  {BOOL, INT, MULTIBOOL};


}