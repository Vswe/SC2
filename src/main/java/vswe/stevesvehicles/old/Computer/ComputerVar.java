package vswe.stevesvehicles.old.Computer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;

public class ComputerVar implements IWriting
{
	private ModuleComputer module;
    public ComputerVar(ModuleComputer module)
    {
		name = "??????";
		this.module = module;
    }


	public String getText() {
		return name.replace("?","");
	}
	public int getMaxLength() {
		return 6;
	}
	public void addChar(char c) {
		name = name.replace("?", "");
		name += c;
		while (name.length() < getMaxLength()) {
			name += "?";
		}
	}
	public void removeChar() {
		name = name.replace("?", "");
		name = name.substring(0, name.length() - 1);
		while (name.length() < getMaxLength()) {
			name += "?";
		}
	}
	
	public int getByteValue() {
		byte val =  (byte)(this.val & 255);

		
		return val;
	}
	
	public void setByteValue(int val) {
		if (val < -128) {
			val = -128;
		}else if(val > 127) {
			val = 127;
		}
	
		if (val < 0) {
			val += 256;
		}
	
		this.val = (short)val;
	}	
	
	public String getFullInfo() {
		return getText() + " = " +  getByteValue();
	}

	private short info;
	/**	b0 - editing
	
	
	**/
	
	public boolean isEditing() {
		return (info & 1) != 0;
	}
	
	public void setEditing(boolean val) {
		info &= ~1;
		info |= val ? 1 : 0;
		if (val) {
			module.setWriting(this);
		}else if(module.getWriting() == this){
			module.setWriting(null);
		}
	}
	
	
	private short val;
	private String name;
	
	private static final String validChars = "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	public void setInfo(int id, short val) {
		if (id == 0) {
			info = val;
			setEditing(isEditing());
		}else if(id == 1) {
			this.val = val;
		}else{
			id -= 2;
			byte char1 = (byte)((val & (255 << 8)) >> 8);
			byte char2 = (byte)(val & 255);
			
			name = name.substring(0, id * 2) + getChar(char1) + getChar(char2) + name.substring(id *2 + 2);
		}
	}
	
	private char getChar(int index) {
		if (index < 0) {
			index += 256;
		}
	
		if(index >= validChars.length()) {
			index = 0;
		}
		return validChars.charAt(index);
	}
	
	public short getInfo(int id) {
		if (id == 0) {
			return info;
		}else if(id == 1) {
			return val;
		}else{
			id -= 2;
			int char1 = getCode(name.charAt(id * 2));
			int char2 = getCode(name.charAt(id * 2 + 1));
			
			return (short)((char1 << 8) | char2);
		}
	}
	
	private int getCode(char c) {
		int index = validChars.indexOf(c);
		if (index == -1) {
			index = 0;
		}
		return index;
	}
}
