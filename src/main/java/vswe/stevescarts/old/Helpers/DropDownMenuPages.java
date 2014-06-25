package vswe.stevescarts.old.Helpers;

import vswe.stevescarts.old.Interfaces.GuiDetector;

public class DropDownMenuPages extends DropDownMenu {

	private int page;
	private int maxPages;
	
	public DropDownMenuPages(int index, int max) {
		super(index);
		this.page = 0;
		this.maxPages = max;
	}

	@Override
	protected int getCurrentId(int index, int objects) {
		return index - objects * page;
	}	
	
	private int[] leftArrow = new int[] {20, 20, 5, 7};
	private int[] rightArrow = new int[] {70, 20, 5, 7};
	
	@Override
	public void drawMain(GuiDetector gui, int x, int y) {
		super.drawMain(gui, x, y);


		drawObject(gui, x, y, new int[] {30, 20, 23, 7}, 0, 170, 0, 0);
		drawObject(gui, x, y, new int[] {60, 20, 5, 7}, 24 + 6 * page, 170, 0, 0);
	
		drawObject(gui, x, y,  leftArrow, 0, 177, 5, 0);
		drawObject(gui, x, y, rightArrow, 0, 184, 5, 0);
	}
	
	private void drawObject(GuiDetector gui, int x, int y, int[] rect, int srcX, int srcY, int hoverDifX, int hoverDifY) {
		rect = new int[] {rect[0], rect[1], rect[2], rect[3]};
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
		if (clicked(gui, x, y, leftArrow)) {
			page--;
			if (page < 0) {
				page = maxPages - 1;
			}
			
		}else if(clicked(gui, x, y, rightArrow)) {
			page++;
			if (page >= maxPages) {
				page = 0;
			}
		}
	}
	
	private boolean clicked(GuiDetector gui, int x, int y, int rect[]) {
		rect = new int[] {rect[0], rect[1], rect[2], rect[3]};
		rect[1] += SCROLL_TOP_MARGIN + getScroll() - SCROLL_HEIGHT;
		
		int gap = rect[1] - getMainRect()[1] + rect[3];

		if (gap > 0) {
			rect[3] = Math.min(rect[3], gap);
			
			return gui.inRect(x,  y,  rect);

		}
		return false;			
	}
	
}
