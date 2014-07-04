package vswe.stevesvehicles.arcade.tracks;

import java.util.ArrayList;

import vswe.stevesvehicles.client.gui.GuiVehicle;

public class ScrollableList {
	
	private int x;
	private int y;
	private ArcadeTracks game;
	
	private ArrayList<String> items;
	
	private int scrollPosition;
	private boolean isScrolling;	
	
	private int selectedIndex = -1;

	public ScrollableList(ArcadeTracks game, int x, int y) {
		this.x = x;
		this.y = y;
		this.game = game;
		
		items = new ArrayList<String>();
	}
	
	public void clearList() {
		items.clear();
	}
	
	public void clear() {
		selectedIndex = -1;
		scrollPosition = 0;
	}
	
	public void add(String str) {
		items.add(str);
	}
	
	public boolean isVisible() {
		return true;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public void onClick() {
		
	}
	
	
	public void drawBackground(GuiVehicle gui, int x, int y) {
		if (!isVisible()) {
			return;
		}
		
		int [] menu = game.getMenuArea();
		game.getModule().drawImage(gui, menu[0] + this.x, menu[1] + this.y, 0, 256-64,  132, 64);
		
		
		for (int i = 0; i < items.size(); i++) {
			int[] rect = getLevelButtonArea(i);
			
			if (rect[3] > 0) {
				int srcY = 188 + (items.get(i) == null ? 34 : (game.getModule().inRect(x, y, rect) ? 17 : 0));
				int borderSrcY = 239;
				
				if (rect[4] < 0) {
					srcY -= rect[4];
					borderSrcY -= rect[4];
				}
				
				game.getModule().drawImage(gui, rect, 146, srcY);
				if (i == selectedIndex) {
					game.getModule().drawImage(gui, rect, 146, borderSrcY);	
				}
			}
		}
		
		int area[] = getScrollArea();
		game.getModule().drawImage(gui, area[0], area[1] + scrollPosition, 132, 256 - (items.size() >= 4 ? 32 : 16),  14, 16);	
	}
	
	public void drawForeground(GuiVehicle gui) {
		if (!isVisible()) {
			return;
		}		
		
		for (int i = 0; i < items.size(); i++) {
			int [] rect = getLevelButtonArea(i);
			
			int x = rect[0] + 4;
			int y = rect[1] + 5;
			
			if (rect[4] < 0) {
				y += rect[4];
			}
			
			if (rect[4] < -5 || rect[4] > 60 - 17 + 5) {
				continue;
			}
			
			game.getModule().drawString(gui, items.get(i) == null ? "<???>" : items.get(i), x, y, 0x404040);
		}		
	}
	

	public void mouseMovedOrUp(GuiVehicle gui, int x, int y, int button) {
		if (!isVisible()) {
			return;
		}		
		
		if (isScrolling) {
			if (button != -1) {
				isScrolling = false;
			}else{
				doScroll(y);
			}
		}	
	}
	
	private void doScroll(int y) {
		int[] area = getScrollArea();
		
		scrollPosition = y - area[1] - 8;
		if (scrollPosition < 0) {
			scrollPosition = 0;
		}else if(scrollPosition > 42) {
			scrollPosition = 42;
		}
	}
	
	private int getScrollLevel() {
		int totalSize = items.size() * 18;
		int availableSpace = 60;
		
		int canNotFit = totalSize - availableSpace;
		 
		int scrollLength = getScrollArea()[3] - 16;
		
		
		
		return (int)(canNotFit * (scrollPosition / (float)scrollLength));
	}
	
	private int[] getLevelButtonArea(int id) {
		int [] menu = game.getMenuArea();
		
		int offSetY = 18 * id - getScrollLevel();
		int height = 17;
		int y = menu[1] + this.y + 2 + offSetY;
		
		if (offSetY < 0) {
			height += offSetY;	
			y -= offSetY;
		}else if(offSetY + height > 60) {
			height = 60 - offSetY;
		}
		
		return new int[] {menu[0] + 2 + this.x, y, 108, height, offSetY};
	}
	
	private int[] getScrollArea() {
		int [] menu = game.getMenuArea();
		
		return new int[] {menu[0] + this.x + 116, menu[1] + this.y + 3, 14, 58};
	}

		
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (!isVisible()) {
			return;
		}		
		
		for (int i = 0; i <  items.size(); i++) {
			if (items.get(i) == null) {
				continue;
			}
			
			int[] rect = getLevelButtonArea(i);
				
			if (rect[3] > 0 && game.getModule().inRect(x, y, rect)) {
				if (selectedIndex == i) {
					selectedIndex = -1;
				}else{
					selectedIndex = i;
				}
				onClick();
				break;
			}
		}	
	
		if (items.size() >= 4 && game.getModule().inRect(x, y, getScrollArea())) {
			doScroll(y);
			isScrolling = true;
		}	
	}
	
}
