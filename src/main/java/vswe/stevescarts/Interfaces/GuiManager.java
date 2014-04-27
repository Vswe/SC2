package vswe.stevescarts.Interfaces;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.CargoItemSelection;
import vswe.stevescarts.TileEntities.TileEntityManager;
import net.minecraft.block.Block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiManager extends GuiBase
{
    public GuiManager(InventoryPlayer invPlayer, TileEntityManager manager, ContainerManager container)
    {
        super(container);
        this.manager = manager;
        this.invPlayer = invPlayer;
    }
	
	@Override
    public void drawGuiForeground(int x, int y)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
	
		int[] coords = getMiddleCoords();
        fontRenderer.drawString(getManagerName(), coords[0] - 34, 65, 0x404040);
        fontRenderer.drawString(Localization.GUI.MANAGER.TITLE.translate(), coords[0] + coords[2], 65, 0x404040);

        for (int i = 0; i < 4; i++)
        {
            coords = getTextCoords(i);
            String str = getMaxSizeText(i);

            fontRenderer.drawString(str, coords[0], coords[1], 0x404040);
        }
		
		
        for (int i = 0; i < 4; i++)
        {
			drawExtraOverlay(i, x, y);

			drawMouseOver(Localization.GUI.MANAGER.CHANGE_TRANSFER_DIRECTION.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + (manager.toCart[i] ? Localization.GUI.MANAGER.DIRECTION_TO_CART.translate() : Localization.GUI.MANAGER.DIRECTION_FROM_CART.translate()),x,y,getArrowCoords(i));
			drawMouseOver(Localization.GUI.MANAGER.CHANGE_TURN_BACK_SETTING.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + (manager.color[i] == 5 ? Localization.GUI.MANAGER.TURN_BACK_NOT_SELECTED.translate() : ( manager.doReturn[manager.color[i]-1] ? Localization.GUI.MANAGER.TURN_BACK_DO.translate() : Localization.GUI.MANAGER.TURN_BACK_DO_NOT.translate())),x,y,getReturnCoords(i));
			drawMouseOver(Localization.GUI.MANAGER.CHANGE_TRANSFER_SIZE.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + getMaxSizeOverlay(i),x,y,getTextCoords(i));
			drawMouseOver(Localization.GUI.MANAGER.CHANGE_SIDE.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SIDE.translate() + ": " + (new String[] {Localization.GUI.MANAGER.SIDE_RED.translate(), Localization.GUI.MANAGER.SIDE_BLUE.translate(), Localization.GUI.MANAGER.SIDE_YELLOW.translate(), Localization.GUI.MANAGER.SIDE_GREEN.translate(), Localization.GUI.MANAGER.SIDE_DISABLED.translate()})[manager.color[i]-1],x,y,getColorpickerCoords(i));
			
		}		
		drawMouseOver(getLayoutString() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " +  getLayoutOption(manager.layoutType) ,x,y, getMiddleCoords());
		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	protected void drawMouseOver(String str, int x, int y, int[] rect) {
		if (inRect(x-getGuiLeft(),y-getGuiTop(),rect)) {
			drawMouseOver(str, x-getGuiLeft(), y-getGuiTop());
		}	
	}

	@Override
    public void drawGuiBackground(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
        int left = getGuiLeft();
        int top = getGuiTop();

		drawBackground(left, top);
		
        for (int i = 0; i < 4; i ++) {
			drawArrow(i, left, top);

			int color = manager.color[i] - 1;
			if (color != 4) {
				drawColors(i, color, left, top);
			}
        }

		RenderItem renderitem = new RenderItem();
		int[] coords = getMiddleCoords();
        renderitem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(getBlock(), 1), left + coords[0], top + coords[1]);
		for (int i = 0; i < 4; i ++) {
			drawItems(i, renderitem, left, top);
		}


		GL11.glColor4f(1F, 1F, 1F, 1F);
    }
	
	private void drawArrow(int id, int left, int top) {
		//the start position in the source image
		int sourceX = getArrowSourceX();
		int sourceY = 28;
		//correct the y value depending on which box the arrow is connected to
		sourceY += 56 * id;

		//correct the x value depning on which direction the arrow is pointing
		if (!manager.toCart[id]){
			sourceX += 28;
		}

		int targetX = getArrowCoords(id)[0];
		int targetY = getArrowCoords(id)[1];
		int sizeX = 28;
		int sizeY = 28;
		//draw the empty arrow
		drawTexturedModalRect(left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY);

		//if this is the active side it should hava a white arrow too

		if (/*manager.color[id] - 1 == manager.side*/id == manager.getLastSetting() && manager.color[id] != 5) {
			//decrease the source Y to reach the white arrows
			sourceY -= 28;
			int scaledProgress = manager.moveProgressScaled(42);
			//draw the first part of the white arrow
			int offsetX = 0;
			int offsetY = 0;

			if (manager.toCart[id]) {
				//only draw half of it, this is to avoid getting any part of the arrow head
				sizeX = 14;

				//this is which half that should pe painted
				if (id % 2 == 0) {
					offsetX = 14;
				}

				//let it grow depending on the progress
				sizeY = scaledProgress;

				if (sizeY > 19) {
					sizeY = 19;
				}

				//some arrows should be painted from bottom to top
				if (id < 2) {
					offsetY = 28 - sizeY;
				}
			}else{
				//only draw half of it, this is to avoid getting any part of the arrow head
				sizeY = 14;

				//this is which half that should pe painted
				if (id >= 2) {
					offsetY = 14;
				}

				//let it grow depending on the progress
				sizeX = scaledProgress;

				if (sizeX > 19) {
					sizeX = 19;
				}

				//some arrows should be painted from bottom to top
				if (id % 2 == 1) {
					offsetX = 28 - sizeX;
				}
			}

			drawTexturedModalRect(left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY);
			//reset the values for next drawing
			offsetX = offsetY = 0;
			sizeX = sizeY = 28;

			//draw the second part(with the head) of the white arrow
			if (scaledProgress > 19) {
				//remove the first part from the total progress
				scaledProgress -= 19;

				if (manager.toCart[id]) {
					//let it grow depending on the progress
					sizeX = scaledProgress;

					if (sizeX > 23) {
						sizeX = 23;
					}

					//some arrows should be painted from bottom to top
					if (id % 2 == 0) {
						offsetX = 22 - sizeX;
					}else{
						offsetX = 6;
					}
				}else{
					//let it grow depending on the progress
					sizeY = scaledProgress;

					if (sizeY > 23) {
						sizeY = 23;
					}

					//some arrows should be painted from bottom to top
					if (id >= 2){
						offsetY = 22 - sizeY;
					}else{
						offsetY = 6;
					}
				}

				drawTexturedModalRect(left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY);
			}
		}	
	}
	
	protected void drawColors(int id, int color, int left, int top) {
		int[] coords = getReturnCoords(id);
		drawTexturedModalRect(left + coords[0], top + coords[1], getColorSourceX() + (manager.doReturn[manager.color[id]-1] ? 8 : 0), 80 + 8 * color, 8, 8);

		coords = getBoxCoords(id);
		drawTexturedModalRect(left + coords[0] - 2, top + coords[1] - 2, getColorSourceX(), 20 * color, 20, 20);
	}

    protected int[] getMiddleCoords() {
        return new int[] {getCenterTargetX() + 45, 61,20,20};
    }

    protected int[] getBoxCoords(int id) {
        int x = id % 2;
        int y = id / 2;
        int xCoord = getCenterTargetX() + 4 + x * 82;
        int yCoord = 17 + y * 88;

		yCoord += offsetObjectY(manager.layoutType, x, y);
		
        return new int[] {xCoord, yCoord,20,20};
    }

    protected int[] getArrowCoords(int id) {
        int x = id % 2;
        int y = id / 2;
        int xCoord = getCenterTargetX() + 25 + x * 28;
        int yCoord = 17 + y * 76;

		yCoord += offsetObjectY(manager.layoutType, x, y);

        return new int[] {xCoord, yCoord, 28, 28};
    }

    protected int[] getTextCoords(int id){
        int[] coords = getBoxCoords(id);
        int xCoord = coords[0];
        int yCoord = coords[1];

        if (id >= 2) {
            yCoord -= 12;
        }else{
            yCoord += 20;
        }

        return new int[] {xCoord, yCoord, 20, 10};
    }

    protected int[] getColorpickerCoords(int id) {
        int x = id % 2;
        int y = id / 2;
        int xCoord = getCenterTargetX() + 3 + x * 92;
        int yCoord = 49 + y * 32;

		yCoord += offsetObjectY(manager.layoutType, x, y);

        return new int[] {xCoord, yCoord, 8, 8};
    }

	protected int[] getReturnCoords(int id) {
        int x = id % 2;
        int y = id / 2;
        int xCoord = getCenterTargetX() + 14 + x * 70;
        int yCoord = 49 + y * 32;

		yCoord += offsetObjectY(manager.layoutType, x, y);

        return new int[] {xCoord, yCoord, 8, 8};
    }

	@Override
    public void mouseClick(int x, int y, int button) {
        super.mouseClick(x, y, button);

        if (button == 0 || button == 1)
        {

            x -= getGuiLeft();
            y -= getGuiTop();

            if (inRect(x, y, getMiddleCoords())) {
                manager.sendPacket(5, (byte)(button == 0 ? 1 : -1));
            }else{

				for (int i = 0; i < 4; i++) {
					byte data = (byte)i;
					data |= (button << 2);

					if (inRect(x, y, getArrowCoords(i))) {
						manager.sendPacket(0, (byte)i);
						break;
					}else if (inRect(x, y, getTextCoords(i))) {
						manager.sendPacket(2, data);
						break;
					}else if (inRect(x, y, getColorpickerCoords(i))) {
						manager.sendPacket(3, data);
						break;
					}else if (inRect(x, y, getReturnCoords(i))) {
						manager.sendPacket(4, (byte)i);
						break;
					}else if (sendOnClick(i, x, y, data)) {
						break;
					}
				}
			}
			
        }
    }
	
	

    private TileEntityManager manager;
    private InventoryPlayer invPlayer;
	
	protected void drawExtraOverlay(int id, int x, int y) {}
	protected boolean sendOnClick(int id, int x, int y, byte data) {return false;}
	protected int offsetObjectY(int layout, int x, int y) {return 0;}
	protected void drawItems(int id, RenderItem renderitem, int left, int top) {}
	protected abstract String getMaxSizeOverlay(int id);
	protected abstract String getMaxSizeText(int id);
	protected abstract void drawBackground(int left, int top);
	protected abstract int getArrowSourceX();
	protected abstract int getColorSourceX();
	protected abstract int getCenterTargetX();
	protected abstract Block getBlock();
	protected abstract String getManagerName();
	protected abstract String getLayoutOption(int id);
	protected abstract String getLayoutString();
	
	protected TileEntityManager getManager() {
		return manager;
	}
}
