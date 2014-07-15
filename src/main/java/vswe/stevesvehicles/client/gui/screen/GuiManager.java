package vswe.stevesvehicles.client.gui.screen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.localization.entry.block.LocalizationManager;
import vswe.stevesvehicles.container.ContainerManager;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.network.PacketType;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityManager;
import net.minecraft.block.Block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiManager extends GuiBase {
    public GuiManager(TileEntityManager manager, ContainerManager container) {
        super(container);
        this.manager = manager;
    }
	
	@Override
    public void drawGuiForeground(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);
	
		int[] coordinate = getCenterCoordinate();
        getFontRenderer().drawString(getManagerName(), coordinate[0] - 34, 65, 0x404040);
        getFontRenderer().drawString(LocalizationManager.TITLE.translate(), coordinate[0] + coordinate[2], 65, 0x404040);

        for (int i = 0; i < 4; i++) {
            coordinate = getTextCoordinate(i);
            String str = getMaxSizeText(i);

            getFontRenderer().drawString(str, coordinate[0], coordinate[1], 0x404040);
        }
		
		
        for (int i = 0; i < 4; i++) {
			drawExtraOverlay(i, x, y);

			drawMouseOver(LocalizationManager.CHANGE_TRANSFER_DIRECTION.translate() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " + (manager.toCart[i] ? LocalizationManager.TO_VEHICLE.translate() : LocalizationManager.FROM_VEHICLE.translate()),x,y, getArrowCoordinate(i));
			drawMouseOver(LocalizationManager.CHANGE_TURN_BACK.translate() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " + (manager.color[i] == 5 ? LocalizationManager.TURN_BACK_DISABLED.translate() : ( manager.doReturn[manager.color[i]-1] ? LocalizationManager.TURN_BACK.translate() : LocalizationManager.CONTINUE_FORWARD.translate())), x, y, getReturnCoordinate(i));
			drawMouseOver(LocalizationManager.CHANGE_TRANSFER_SIZE.translate() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " + getMaxSizeOverlay(i),x,y, getTextCoordinate(i));
			drawMouseOver(LocalizationManager.CHANGE_SIDE.translate() + "\n" + LocalizationManager.CURRENT_SIDE.translate() + ": " + (new String[] {LocalizationManager.RED_SIDE.translate(), LocalizationManager.BLUE_SIDE.translate(), LocalizationManager.YELLOW_SIDE.translate(), LocalizationManager.GREEN_SIDE.translate(), LocalizationManager.DISABLED_SIDE.translate()})[manager.color[i]-1], x, y, getColorSelectorCoordinate(i));
		}

		drawMouseOver(getLayoutString() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " +  getLayoutOption(manager.layoutType), x, y, getCenterCoordinate());
		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	protected void drawMouseOver(String str, int x, int y, int[] rect) {
		if (inRect(x-getGuiLeft(),y-getGuiTop(),rect)) {
			drawMouseOver(str, x-getGuiLeft(), y-getGuiTop());
		}	
	}

    protected static final ResourceLocation MANAGER_TEXTURE = ResourceHelper.getResource("/gui/manager.png");

	@Override
    public void drawGuiBackground(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
        int left = getGuiLeft();
        int top = getGuiTop();

	    drawBackground(left, top);

        ResourceHelper.bindResource(MANAGER_TEXTURE);

        setColor(ColorEffect.BLACK);
        int[] center = getCenterCoordinate();
        drawTexturedModalRect(left + center[0] - 2, top + center[1] - 2, BOX_SRC_X, BOX_SRC_Y, center[2], center[3]);

        setColor(ColorEffect.CLEAR);
        for (int i = 0; i < 4; i ++) {
			drawArrow(i, left, top);

			int color = manager.color[i] - 1;
			drawColors(i, color, left, top);
        }

		RenderItem renderitem = new RenderItem();
		int[] coordinate = getCenterCoordinate();
        renderitem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(getBlock(), 1), left + coordinate[0], top + coordinate[1]);
		for (int i = 0; i < 4; i ++) {
			drawItems(i, renderitem, left, top);
		}


		GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private static final int ARROW_SOURCE_X = 1;
    private static final int ARROW_SOURCE_Y = 22;
    private static final int ARROW_SIZE = 28;
    private static final int TEXTURE_SPACING = 1;
    private static final RenderRotation[] ARROW_ROTATIONS = {RenderRotation.NORMAL, RenderRotation.FLIP_HORIZONTAL, RenderRotation.FLIP_VERTICAL, RenderRotation.ROTATE_180};

    private static final int ARROW_BASE_OFFSET = 18;
    private static final int ARROW_BASE_SIZE = 19;
    private static final int ARROW_HEAD_SIZE = 22;
    private static final int ARROW_FULL_SIZE = 42;

	private void drawArrow(int id, int left, int top) {
		//the start position in the source image
		int sourceX = ARROW_SOURCE_X;
		int sourceY = ARROW_SOURCE_Y + ARROW_SIZE + TEXTURE_SPACING;
        RenderRotation rotation = ARROW_ROTATIONS[id];

		//flip the arrow if it goes in the other direction
		if (!manager.toCart[id]){
            rotation = id / 2 == id % 2 ? rotation.getNextRotation() : rotation.getPreviousRotation();
            rotation = rotation.getFlippedRotation();
		}

        int[] target = getArrowCoordinate(id);
		int targetX = target[0];
		int targetY = target[1];
		int sizeX = ARROW_SIZE;
		int sizeY = ARROW_SIZE;

		//draw the empty arrow
		drawRect(left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY, rotation);

		//if this is the active side it should have a white arrow too
		if (id == manager.getLastSetting() && manager.color[id] != 5) {
			sourceY -= ARROW_SIZE + TEXTURE_SPACING;
			int scaledProgress = manager.moveProgressScaled(ARROW_FULL_SIZE);
			int offsetX;
			int offsetY;

            offsetX = ARROW_BASE_OFFSET;
            sizeX = ARROW_SIZE - offsetX;


            sizeY = scaledProgress;
            if (sizeY > ARROW_BASE_SIZE) {
                sizeY = ARROW_BASE_SIZE;
            }
            offsetY = ARROW_SIZE - sizeY;


            drawRectWithSourceOffset(left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY, rotation, offsetX, offsetY, ARROW_SIZE, ARROW_SIZE);


			//draw the second part(with the head) of the white arrow
			if (scaledProgress > ARROW_BASE_SIZE) {
				scaledProgress -= ARROW_BASE_SIZE;

                sizeX = scaledProgress;
                if (sizeX > ARROW_HEAD_SIZE) {
                    sizeX = ARROW_HEAD_SIZE;
                }
                offsetX = ARROW_HEAD_SIZE - sizeX;

                offsetY = 0;
                sizeY = ARROW_SIZE;

                drawRectWithSourceOffset(left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY, rotation, offsetX, offsetY, ARROW_SIZE, ARROW_SIZE);
			}
		}	
	}

    private static final int BOX_SRC_X = 1;
    private static final int BOX_SRC_Y = 1;
    private static final int RETURN_SRC_X = 22;
    private static final int RETURN_SRC_X_OFFSET = 9;
    private static final int RETURN_SRC_Y = 1;

    private static final int COLOR_SELECTOR_UNIT_SIZE = 4;
    private static final int COLOR_SELECTOR_UNIT_SRC_X = 49;
    private static final int COLOR_SELECTOR_UNIT_SRC_Y = 1;


	protected void drawColors(int id, int color, int left, int top) {
        setColor(color);

        int texture;
        if (color == 4) {
            texture = 2;
        }else{
            texture = manager.doReturn[manager.color[id] - 1] ? 1 : 0;
        }

		int[] coordinate = getReturnCoordinate(id);
        drawTexturedModalRect(left + coordinate[0], top + coordinate[1], RETURN_SRC_X + RETURN_SRC_X_OFFSET * texture, RETURN_SRC_Y, coordinate[2], coordinate[3]);

		coordinate = getBoxCoordinate(id);
		drawTexturedModalRect(left + coordinate[0] - 2, top + coordinate[1] - 2, BOX_SRC_X, BOX_SRC_Y, coordinate[2], coordinate[3]);



        coordinate = getColorSelectorCoordinate(id);
        drawTexturedModalRect(left + coordinate[0], top + coordinate[1], RETURN_SRC_X + RETURN_SRC_X_OFFSET * 2, RETURN_SRC_Y, coordinate[2], coordinate[3]);
        for (int i = 0; i < 4; i++) {
            int unitColor = (color + i + 1) % 5;
            setColor(unitColor);

            int x = i % 2;
            int y = i / 2;
            drawTexturedModalRect(left + coordinate[0] + x * COLOR_SELECTOR_UNIT_SIZE, top + coordinate[1] + y * COLOR_SELECTOR_UNIT_SIZE, COLOR_SELECTOR_UNIT_SRC_X, COLOR_SELECTOR_UNIT_SRC_Y, COLOR_SELECTOR_UNIT_SIZE, COLOR_SELECTOR_UNIT_SIZE);
        }
        setColor(color);
        drawTexturedModalRect(left + coordinate[0] + COLOR_SELECTOR_UNIT_SIZE / 2, top + coordinate[1] + COLOR_SELECTOR_UNIT_SIZE / 2, COLOR_SELECTOR_UNIT_SRC_X, COLOR_SELECTOR_UNIT_SRC_Y, COLOR_SELECTOR_UNIT_SIZE, COLOR_SELECTOR_UNIT_SIZE);

        setColor(ColorEffect.CLEAR);
	}

    protected int[] getCenterCoordinate() {
        return new int[] {getCenterTargetX() + 45, 61, 20, 20};
    }

    protected int[] getBoxCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int targetX = getCenterTargetX() + 4 + x * 82;
        int targetY = 17 + y * 88;

		targetY += offsetObjectY(manager.layoutType, x, y);
		
        return new int[] {targetX, targetY, 20, 20};
    }

    protected int[] getArrowCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int targetX = getCenterTargetX() + 25 + x * 28;
        int targetY = 17 + y * 76;

		targetY += offsetObjectY(manager.layoutType, x, y);

        return new int[] {targetX, targetY, 28, 28};
    }

    protected int[] getTextCoordinate(int id){
        int[] coordinate = getBoxCoordinate(id);
        int targetX = coordinate[0];
        int targetY = coordinate[1];

        if (id >= 2) {
            targetY -= 12;
        }else{
            targetY += 20;
        }

        return new int[] {targetX, targetY, 20, 10};
    }

    protected int[] getColorSelectorCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int targetX = getCenterTargetX() + 3 + x * 92;
        int targetY = 49 + y * 32;

		targetY += offsetObjectY(manager.layoutType, x, y);

        return new int[] {targetX, targetY, 8, 8};
    }

	protected int[] getReturnCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int targetX = getCenterTargetX() + 14 + x * 70;
        int targetY = 49 + y * 32;

		targetY += offsetObjectY(manager.layoutType, x, y);

        return new int[] {targetX, targetY, 8, 8};
    }

	@Override
    public void mouseClick(int x, int y, int button) {
        super.mouseClick(x, y, button);

        if (button == 0 || button == 1) {
            x -= getGuiLeft();
            y -= getGuiTop();

            if (inRect(x, y, getCenterCoordinate())) {
                sendPacket(TileEntityManager.PacketId.LAYOUT_TYPE, button == 0);
            }else{

				for (int i = 0; i < 4; i++) {
					if (inRect(x, y, getArrowCoordinate(i))) {
                        sendPacket(TileEntityManager.PacketId.TRANSFER_DIRECTION, i);
						break;
					}else if (inRect(x, y, getTextCoordinate(i))) {
                        sendPacket(TileEntityManager.PacketId.AMOUNT, i, button == 0);
						break;
					}else if (inRect(x, y, getColorSelectorCoordinate(i))) {
						sendPacket(TileEntityManager.PacketId.COLOR, i, button == 0);
						break;
					}else if (inRect(x, y, getReturnCoordinate(i))) {
                        sendPacket(TileEntityManager.PacketId.RETURN_MODE, i);
						break;
					}else if (sendOnClick(i, button, x, y)) {
						break;
					}
				}
			}
			
        }
    }

    protected void sendPacket(TileEntityManager.PacketId id, boolean dif) {
        DataWriter dw = getDataWriter(id);
        dw.writeBoolean(dif);
        PacketHandler.sendPacketToServer(dw);
    }

    protected void sendPacket(TileEntityManager.PacketId id, int railId) {
        DataWriter dw = getDataWriter(id);
        dw.writeByte(railId);
        PacketHandler.sendPacketToServer(dw);
    }

    protected void sendPacket(TileEntityManager.PacketId id, int railId, boolean dif) {
        DataWriter dw = getDataWriter(id);
        dw.writeByte(railId);
        dw.writeBoolean(dif);
        PacketHandler.sendPacketToServer(dw);
    }

    protected void setColor(int color) {
        setColor(ColorEffect.values()[color]);
    }
    protected void setColor(ColorEffect color) {
        GL11.glColor4f(color.red, color.green, color.blue, 1F);
    }

    protected enum ColorEffect {
        RED(237, 28, 36),
        BLUE(0, 114, 188),
        YELLOW(255, 242, 0),
        GREEN(57, 181, 74),
        BLACK(48, 48, 48),
        CLEAR(255, 255, 255);

        private float red;
        private float green;
        private float blue;

        ColorEffect(int red, int green, int blue) {
            this.red = red / 255F;
            this.green = green / 255F;
            this.blue = blue / 255F;
        }
    }

    protected DataWriter getDataWriter(TileEntityManager.PacketId id) {
        DataWriter dw = PacketHandler.getDataWriter(PacketType.BLOCK);
        dw.writeEnum(id);
        return dw;
    }



    private TileEntityManager manager;
	
	protected void drawExtraOverlay(int id, int x, int y) {}
	protected boolean sendOnClick(int id, int button, int x, int y) {return false;}
	protected int offsetObjectY(int layout, int x, int y) {return 0;}
	protected void drawItems(int id, RenderItem renderitem, int left, int top) {}
	protected abstract String getMaxSizeOverlay(int id);
	protected abstract String getMaxSizeText(int id);
	protected abstract void drawBackground(int left, int top);
    protected abstract int getCenterTargetX();
	protected abstract Block getBlock();
	protected abstract String getManagerName();
	protected abstract String getLayoutOption(int id);
	protected abstract String getLayoutString();
	
	protected TileEntityManager getManager() {
		return manager;
	}
}
