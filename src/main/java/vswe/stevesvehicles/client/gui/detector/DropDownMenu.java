package vswe.stevesvehicles.client.gui.detector;

import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class DropDownMenu {
	public static final int SCROLL_HEIGHT = 170;
	public static final int SCROLL_HEADER_HEIGHT = 14;	
	public static final int SCROLL_TOP_MARGIN = 20;
    public static final int TAB_HEADER_WIDTH = 76;
    public static final int TAB_HEADER_SPACING = 2;

    private int moduleScroll;
	protected int index;
	public DropDownMenu(int index) {
		this.index = index;
		this.moduleScroll = 0;
	}


	private boolean forceGoUp;
	public void update(boolean hasFocus) {
		if (!forceGoUp && hasFocus) {
			if (moduleScroll < SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT) {
				moduleScroll += 10;
				if (moduleScroll > SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT) {
					moduleScroll = SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT;
				}
			}
		}else{
			if (moduleScroll > 0) {
				moduleScroll -= 25;
				if (moduleScroll <= 0) {
					moduleScroll = 0;
					forceGoUp = false;
				}
			}
		}		
	}



	public void drawMain(GuiDetector gui, int x, int y) {
		ResourceHelper.bindResource(GuiDetector.DROP_DOWN_TEXTURE);
	
		int [] rect = getMainRect();
		gui.drawTexturedModalRect(gui.getGuiLeft()+rect[0], gui.getGuiTop() + rect[1], 0, SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT - moduleScroll, rect[2], rect[3]);
	}

    public void drawHeader(GuiDetector gui) {
		ResourceHelper.bindResource(GuiDetector.DROP_DOWN_TEXTURE);
	
		int [] rect = getHeaderRect();
		gui.drawTexturedModalRect(gui.getGuiLeft()+rect[0], gui.getGuiTop() + rect[1], (TAB_HEADER_WIDTH + TAB_HEADER_SPACING) * index, SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT + 1, rect[2], rect[3]);
	}



	protected void drawContent(GuiDetector gui, int index, int srcX, int srcY, int textureSize) {
		int[] rect = getContentRect(index);
		if (rect == null) {
			return;
		}		
		
		int gap = rect[1] - getMainRect()[1] + rect[3];

		if (gap > 0) {
			int height = Math.min(rect[3], gap);
			int offset = rect[3] - height;

			gui.drawRectWithTextureSize(gui.getGuiLeft()+rect[0],  gui.getGuiTop() + rect[1]+offset, srcX, srcY+offset, rect[2], height, textureSize);
		}
	}

    protected abstract int getObjectsPerRow();
    protected abstract int getObjectRows();
    protected abstract int getObjectWidth();
    protected abstract int getObjectHeight();
    protected abstract int getObjectStartY();

    public int[] getContentRect(int posId) {
		int objectsPerRow = getObjectsPerRow();
		int objectsRows = getObjectRows();
		int objectWidth = getObjectWidth();
		int objectHeight = getObjectHeight();

		posId = getInternalIndex(posId);
		if (posId < 0 || posId >= objectsPerRow * objectsRows) {
			return null;
		}
		
		int x = posId % objectsPerRow;
		int y = posId / objectsPerRow;

		int targetX = x * (objectWidth + 3) + 25;
		int targetY = y * (objectHeight + 3) + SCROLL_TOP_MARGIN + getObjectStartY() + getScroll() - SCROLL_HEIGHT;
		return new int[] {targetX, targetY, objectWidth, objectHeight};
	}	
	
	
	
	public int [] getMainRect() {
		return new int[] {11, SCROLL_TOP_MARGIN, 232, moduleScroll};		
	}
	
	public int [] getHeaderRect() {
		return new int[] {11 + (TAB_HEADER_WIDTH + TAB_HEADER_SPACING) * index, SCROLL_TOP_MARGIN + moduleScroll, TAB_HEADER_WIDTH, SCROLL_HEADER_HEIGHT};
	}
	
	
	public int getScroll() {
		return moduleScroll;
	}
	
	
	protected int getInternalIndex(int index) {
		return index;
	}
	
	
	public void onClick(GuiDetector gui, int x, int y) {
	
	}

    public void goUp() {
        forceGoUp = true;
    }

    public void release() {
        forceGoUp = false;
    }

    public void drawMouseOver(GuiDetector gui, int x, int y) {

    }
}