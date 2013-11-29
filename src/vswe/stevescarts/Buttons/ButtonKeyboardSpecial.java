package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerVar;

import java.util.ArrayList;
public class ButtonKeyboardSpecial extends ButtonKeyboard {
	
	private KEY key;
	
    protected ButtonKeyboardSpecial(ModuleComputer module, int x, int y, KEY key)
    {
		super(module, x, y, ' ');	
		this.key = key;
	}
	
	@Override
	public String toString() {
		return key.toString();
	}
		
		
	@Override
	public boolean isEnabled() {
		if (key == KEY.BACKSPACE || key == KEY.ENTER) {
			return ((ModuleComputer)module).getWriting().getText().length() > 0;
		}else{
			return super.isEnabled();
		}
	}
	
	@Override
	public int texture()
    {
		if (key == KEY.CAPS) {
			return 26;
		}else if(key == KEY.SHIFT) {
			return 27;
		}else if(key == KEY.BACKSPACE) {
			return 28;
		}else if(key == KEY.ENTER) {
			return 29;
		}
        return super.texture();
    }
	
	@Override
	public int X()
    {
        int temp = y;
        y = 0;
        int temp2 = super.X();
        y = temp;
        return temp2;
    }
	
	@Override
    public boolean hasText()
    {
        return false;
    }	
	
	@Override
	public int borderID() {
		if ((key == KEY.SHIFT && ((ModuleComputer)module).getShift()) || (key == KEY.CAPS && ((ModuleComputer)module).getCaps())) {
			return 3;
		}
	
		return super.borderID();
	}
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		if (key == KEY.BACKSPACE) {
			((ModuleComputer)module).getWriting().removeChar();
		}else if(key == KEY.ENTER) {
			if (((ModuleComputer)module).getWriting() instanceof ComputerVar) {
				((ComputerVar)((ModuleComputer)module).getWriting()).setEditing(false);
			}
		}else if(key == KEY.SHIFT) {
			((ModuleComputer)module).flipShift();
		}else if(key == KEY.CAPS) {
			((ModuleComputer)module).flipCaps();
		}
	}	
	

	public enum KEY {SHIFT, CAPS, BACKSPACE, ENTER}
	

}