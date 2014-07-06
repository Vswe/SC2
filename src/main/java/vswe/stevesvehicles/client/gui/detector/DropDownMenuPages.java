package vswe.stevesvehicles.client.gui.detector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;

@SideOnly(Side.CLIENT)
public abstract class DropDownMenuPages extends DropDownMenu {

	private int page;
	
	public DropDownMenuPages(int index) {
		super(index);
		this.page = 0;
	}

	@Override
	protected int getInternalIndex(int index) {
		return index - getObjectsPerPage() * page;
	}	
	
	private int[] leftArrow = new int[] {9, 20, 5, 7};
	private int[] rightArrow = new int[] {59, 20, 5, 7};

    private int cachedPageCount;
    private boolean hasCheckedPageCount;
	@Override
	public void drawMain(GuiDetector gui, int x, int y) {
		super.drawMain(gui, x, y);

        if (!hasCheckedPageCount) {
            cachedPageCount = getPageCount();
            hasCheckedPageCount = true;
        }

        if (cachedPageCount > 1) {
            drawObject(gui, x, y, new int[] {19, 20, 23, 7}, 1, 172, 0, 0);
            drawObject(gui, x, y, new int[] {49, 20, 5, 7}, 25 + 6 * page, 172, 0, 0);

            drawObject(gui, x, y, leftArrow, 1, 180, 6, 0);
            drawObject(gui, x, y, rightArrow, 1, 188, 6, 0);
        }
	}
	
	private void drawObject(GuiDetector gui, int x, int y, int[] rect, int srcX, int srcY, int hoverDifX, int hoverDifY) {
		rect = new int[] {11 + rect[0] + (TAB_HEADER_WIDTH + TAB_HEADER_SPACING) * index, rect[1], rect[2], rect[3]};
		rect[1] += SCROLL_TOP_MARGIN + getScroll() - SCROLL_HEIGHT;

		int gap = rect[1] - getMainRect()[1] + rect[3];

		if (gap > 0) {
			int height = Math.min(rect[3], gap);
			int offset = rect[3] - height;
			rect[3] = height;
			
			if (gui.inRect(x,  y,  rect)) {
				srcX += hoverDifX;
				srcY += hoverDifY;
			}
			
			gui.drawTexturedModalRect(gui.getGuiLeft()+rect[0],  gui.getGuiTop() + rect[1]+offset, srcX, srcY+offset, rect[2], rect[3]);
		}	
	}
	
	@Override
	public void onClick(GuiDetector gui, int x, int y) {
        if (cachedPageCount > 1) {
            if (clicked(gui, x, y, leftArrow)) {
                page--;
                if (page < 0) {
                    page = cachedPageCount - 1;
                }

            }else if(clicked(gui, x, y, rightArrow)) {
                page++;
                if (page >= cachedPageCount) {
                    page = 0;
                }
            }
        }
	}

    protected abstract int getPageCount();

	private boolean clicked(GuiDetector gui, int x, int y, int rect[]) {
		rect = new int[] {11 + rect[0] + (TAB_HEADER_WIDTH + TAB_HEADER_SPACING) * index, rect[1], rect[2], rect[3]};
		rect[1] += SCROLL_TOP_MARGIN + getScroll() - SCROLL_HEIGHT;
		
		int gap = rect[1] - getMainRect()[1] + rect[3];

		if (gap > 0) {
			rect[3] = Math.min(rect[3], gap);
			
			return gui.inRect(x,  y,  rect);

		}
		return false;			
	}


    protected int getObjectsPerRow() {
        return 11;
    }

    protected int getObjectRows() {
        return 7;
    }

    protected int getObjectWidth() {
        return 16;
    }

    protected int getObjectHeight() {
        return 16;
    }

    protected int getObjectStartY() {
        return 31;
    }

    protected int getObjectsPerPage() {
        return getObjectsPerRow() * getObjectRows();
    }
}
