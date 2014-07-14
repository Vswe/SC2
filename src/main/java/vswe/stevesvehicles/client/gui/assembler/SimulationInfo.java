package vswe.stevesvehicles.client.gui.assembler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.client.gui.screen.GuiCartAssembler;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.StevesVehicles;


public abstract class SimulationInfo {

	private ILocalizedText name;
	private String texturePath;
	private boolean isLarge;
	private boolean isOpen;

	
	public SimulationInfo(ILocalizedText name, String texturePath) {
		this.name = name;
        this.texturePath = texturePath;
	}
	
	public String getName() {
		return name.translate();
	}

    public ResourceLocation getResource() {
        return texturePath == null ? null : ResourceHelper.getResource("/gui/simulation/" + texturePath + ".png");
    }

    public abstract boolean hasSubMenu();
	
	public boolean getIsSubMenuOpen() {
		return isOpen;
	}
	
	public void setIsSubMenuOpen(boolean val) {
		isOpen = val;
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

    private static final int DROP_DOWN_SRC_X = 1;
    private static final int DROP_DOWN_SHORT_SRC_Y = 82;
    private static final int DROP_DOWN_LARGE_SRC_Y = 103;

    private static final int DROP_DOWN_EXTRA_OPEN_SRC_X = 1;
    private static final int DROP_DOWN_EXTRA_CLOSED_SRC_X = 44;
    private static final int DROP_DOWN_EXTRA_SRC_Y = 124;

    private static final int STANDARD_BOX_SRC_X = 1;
    private static final int STANDARD_BOX_HOVER_SRC_X = 34;
    private static final int STANDARD_BOX_SRC_Y = 141;

    private static final int BOOLEAN_CHECK_SRC_X = 1;
    private static final int BOOLEAN_CHECK_SRC_Y = 152;

    private ResourceLocation texture;

    @SideOnly(Side.CLIENT)
    public void draw(GuiCartAssembler gui, int i, int x, int y) {
        int left = gui.getGuiLeft();
        int top = gui.getGuiTop();

        int rect[] = getRect(gui.getDropDownX(), gui.getDropDownY(), i);
        int srcX = DROP_DOWN_SRC_X;
        int srcY = getIsLarge() ? DROP_DOWN_LARGE_SRC_Y : DROP_DOWN_SHORT_SRC_Y;

        gui.drawTexturedModalRect(left + rect[0], top + rect[1], srcX, srcY, rect[2], rect[3]);


        if (hasSubMenu()) {
            int [] subRect = getSubRect(gui.getDropDownX(), gui.getDropDownY(), i);
            srcX = getIsSubMenuOpen() ? DROP_DOWN_EXTRA_OPEN_SRC_X : DROP_DOWN_EXTRA_CLOSED_SRC_X;
            srcY = DROP_DOWN_EXTRA_SRC_Y;

            gui.drawTexturedModalRect(left + subRect[0], top + subRect[1], srcX, srcY, subRect[2], subRect[3]);
        }

        if (getResource() != null) {
            if (texture == null) {
                texture = getResource();
            }
            ResourceHelper.bindResource(texture);
            gui.drawRectWithTextureSize(left + rect[0] + 34, top + rect[1] + 2, 0, 0, 16, 16, 16);
        }


        if (getIsLarge()) {
            gui.getFontRenderer().drawString(getName().toUpperCase(), left + rect[0] + 55, top + rect[1] + 7, 0x000000);
        }

        ResourceHelper.bindResource(GuiCartAssembler.TEXTURE_EXTRA);
        GL11.glColor4f(1, 1, 1, 1);
    }



    @SideOnly(Side.CLIENT)
    public void drawBooleanBox(GuiCartAssembler gui, int mouseX, int mouseY, int x, int y, boolean itemValue) {
        drawStandardBox(gui, mouseX, mouseY, x,y, STANDARD_BOX_SRC_X);
        if (itemValue) {
            gui.drawTexturedModalRect(gui.getGuiLeft() + x + 2, gui.getGuiTop() + y + 2, BOOLEAN_CHECK_SRC_X, BOOLEAN_CHECK_SRC_Y, 6, 6);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void drawStandardBox(GuiCartAssembler gui, int mouseX, int mouseY, int x, int y, int srcX) {
        int targetX = gui.getGuiLeft()+ x;
        int targetY = gui.getGuiTop()+ y;

        gui.drawTexturedModalRect(targetX, targetY, srcX, STANDARD_BOX_SRC_Y, 10, 10);
        if (gui.inRect(mouseX, mouseY, new int[]{targetX, targetY, 10, 10})) {
            gui.drawTexturedModalRect(targetX, targetY, STANDARD_BOX_HOVER_SRC_X, STANDARD_BOX_SRC_Y, 10, 10);
        }
    }

    @SideOnly(Side.CLIENT)
    protected int getOffSetXForSubMenuBox(int id, int count) {
        return 2 + (int)(20 + (id - count / 2F) * 10);
    }

    @SideOnly(Side.CLIENT)
    protected boolean clickBox(GuiCartAssembler gui, int mouseX, int mouseY, int x, int y) {
        return gui.inRect(mouseX, mouseY, new int[]{x, y, 10, 10});
    }

    @SideOnly(Side.CLIENT)
    public abstract void onMouseClick(GuiCartAssembler gui, int i, int x, int y);
}