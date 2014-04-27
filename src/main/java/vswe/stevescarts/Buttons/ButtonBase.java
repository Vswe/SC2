package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ButtonBase
{
    protected final LOCATION loc;
	protected final ModuleBase module;
	private boolean lastVisibility;
	
    public ButtonBase(ModuleBase module, LOCATION loc)
    {
		this.module = module;
		module.addButton(this);
        this.loc = loc;
    }

	private int currentID;
	private int moduleID;
	public void setCurrentID(int id) {
		currentID = id;
	}
	
	public void setIdInModule(int id) {
		moduleID = id;
	}	
	
	public int getIdInModule() {
		return moduleID;
	}
	
    public String toString()
    {
        return "";
    }

    public boolean isEnabled()
    {
        return false;
    }

    public boolean hasText()
    {
        return false;
    }

    public boolean isVisible()
    {
        return false;
    }

    public final void computeOnClick(GuiMinecart gui, int mousebutton)
    {
		if (this.isVisible() && this.isEnabled()) {
			onClientClick(mousebutton, gui.isCtrlKeyDown(), gui.isShiftKeyDown());
		
			if (handleClickOnServer()) {
				byte clickinformation = (byte)(mousebutton & 63);
				clickinformation |= (gui.isCtrlKeyDown() ? 1 : 0) << 6;
				clickinformation |= (gui.isShiftKeyDown() ? 1 : 0) << 7;
				
				module.sendButtonPacket(this, clickinformation);
			}
		}
    }
	
	public void onClientClick(int mousebutton, boolean ctrlKey, boolean shiftKey) {}
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {}
	
	public boolean handleClickOnServer() {
		return true;
	}

    private boolean useTexture()
    {
        return texture() != -1;
    }

    public int ColorCode()
    {
        return 0;
    }

	private boolean hasBorder() {
		return borderID() != -1;
	}
	
	public int borderID() {
		return -1;
	}
	
    public int texture()
    {
        return -1;
    }

    public int textureX()
    {
        return (texture() % 21) * 12;
    }
    public int textureY()
    {
        return 60 + (texture() / 21) * 12;
    }


	public void drawButtonText(GuiMinecart gui, ModuleBase module) {
		if (this.isVisible() && this.hasText())
		{
			module.drawString(gui, this.toString(), X() + 8, Y() + 7, 0xFFFFFF);
		}
	}	
	
	@SideOnly(Side.CLIENT)	
	private static ResourceLocation texture = ResourceHelper.getResource("/gui/buttons.png");	
	
	public void drawButton(GuiMinecart gui, ModuleBase module, int x, int y) {
	    boolean visibility = this.isVisible();
		if (visibility != lastVisibility) {
			module.buttonVisibilityChanged();			
		}		
		lastVisibility = visibility;
		
		ResourceHelper.bindResource(texture);

		if (!visibility) {
			return;
		}

		int sourceX = 0;
		int sourceY = 20;
		
		if (this.isEnabled()) {
			sourceX = 20 * (this.ColorCode() + 1);
		}
		
		if (module.inRect(x,y,getBounds())) {
			sourceY += 20;
		}		

		module.drawImage(gui, getBounds(), sourceX, sourceY);
		
		
		if (this.useTexture()) {
			module.drawImage(gui, X() + 4, Y() + 4, textureX(), textureY(), 12, 12);
		}


		if (this.hasBorder())
		{
			module.drawImage(gui, getBounds(), this.borderID() * 20, 0);
		}      
	}
	
	public int[] getBounds() {
		return new int[] {X(), Y(), 20, 20};
	}
	
    public int X()
    {
        switch (loc)
        {
            case OVERVIEW:
                return 15 + currentID * 25;

            case PROGRAM:
                return 125 + (currentID % 6) * 25;

            case TASK:
                return 306 + (currentID % 5) * 25;

            case DEFINED:
                return 0;

            case FLOATING:
                return 115 + (currentID % 7) * 25;
				
			case VARIABLE:
                return 400 + (currentID % 3) * 25;
				
            case BUILD:
                return 366 + (currentID % 5) * 25;		

			case MODEL:
				return 111 + (currentID % 6) * 22;

            default:
                return -1;
        }
    }

    public int Y()
    {
        switch (loc)
        {
            case OVERVIEW:
                return 143;

            case PROGRAM:
                return 118 + (currentID / 6) * 25;

            case TASK:
                return 32 + (currentID / 5) * 25;
				
            case DEFINED:
                return 0;

            case FLOATING:
                return 32 + (currentID / 7) * 25;
				
			case VARIABLE:
                return 32 + (currentID / 3) * 25;
				
            case BUILD:
                return 118 + (currentID / 5) * 25;	
				
			case MODEL:
				return 19 + (currentID / 6) * 22;
				
            default:
                return -1;
        }
    }

    public enum LOCATION {OVERVIEW, PROGRAM, TASK, DEFINED, FLOATING, VARIABLE, BUILD, MODEL};
	
	public LOCATION getLocation() {
		return loc;
	}
	
	public int getLocationID()
    {
        for (int i = 0; i < LOCATION.values().length; i++)
        {
            if (LOCATION.values()[i] == loc)
            {
                return i;
            }
        }

        return 0;
    }
}
