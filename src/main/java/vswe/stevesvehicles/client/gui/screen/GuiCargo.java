package vswe.stevesvehicles.client.gui.screen;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.localization.entry.block.LocalizationCargo;
import vswe.stevesvehicles.localization.entry.block.LocalizationManager;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.container.ContainerCargo;
import vswe.stevesvehicles.old.Helpers.CargoItemSelection;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityCargo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tileentity.TileEntityManager;

@SideOnly(Side.CLIENT)
public class GuiCargo extends GuiManager {
    public GuiCargo(InventoryPlayer invPlayer, TileEntityCargo cargo) {
        super(cargo, new ContainerCargo(invPlayer, cargo));
        setXSize(305);
        setYSize(222);
    }

	@Override
	protected String getMaxSizeOverlay(int id) {
		int amount = getCargo().getAmount(id);
		int type = getCargo().getAmountType(id);
	
		if (type == 0) {
			return LocalizationCargo.TRANSFER_ALL.translate();
		}else if (type == 1) {
			return LocalizationCargo.TRANSFER_ITEMS.translate(String.valueOf(amount), String.valueOf(amount));
		}else{
			return LocalizationCargo.TRANSFER_STACKS.translate(String.valueOf(amount), String.valueOf(amount));
		}
	}
	
	@Override
	protected String getMaxSizeText(int id) {
		String s;
		int type = getCargo().getAmountType(id);

		if (type == 0){
			s = LocalizationCargo.TRANSFER_ALL_SHORT.translate();
		}else{
			int amount = getCargo().getAmount(id);
			s = String.valueOf(amount);

			if (type == 1){
				s += " " + LocalizationCargo.TRANSFER_ITEMS_SHORT.translate();
			}else{
				s += " " + LocalizationCargo.TRANSFER_STACKS_SHORT.translate();
			}
		}
		return s;
	}

    private static final ResourceLocation CARGO_TEXTURE = ResourceHelper.getResource("/gui/cargo.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TOP_HEIGHT = 10;
    private static final int MIDDLE_HEIGHT = 50;
    private static final int MIDDLE_UNITS = 2;
    private static final int BOT_HEIGHT = 24;
    private static final int TEXTURE_SPACING = 1;

    private static final int PLAYER_INVENTORY_X = 65;
    private static final int PLAYER_INVENTORY_WIDTH = 176;
    private static final int PLAYER_INVENTORY_HEIGHT = 88;

    private static final int TOP_Y = 0;
    private static final int MIDDLE_Y = TOP_Y + TOP_HEIGHT;
    private static final int BOT_Y = MIDDLE_Y + MIDDLE_HEIGHT * MIDDLE_UNITS;
    private static final int PLAYER_INVENTORY_Y = BOT_Y + BOT_HEIGHT;

    private static final int TOP_SRC_Y = 0;
    private static final int MIDDLE_SRC_Y = TOP_SRC_Y + TOP_HEIGHT + TEXTURE_SPACING;
    private static final int BOT_SRC_Y = MIDDLE_SRC_Y + MIDDLE_HEIGHT + TEXTURE_SPACING;
    private static final int PLAYER_INVENTORY_SRC_Y = BOT_SRC_Y + BOT_HEIGHT + TEXTURE_SPACING;


    private static final int EXTRA_WIDTH = 49;
    private static final int EXTRA_HEIGHT = TOP_HEIGHT + MIDDLE_HEIGHT * MIDDLE_UNITS + BOT_HEIGHT;
    private static final int EXTRA_SRC_Y = PLAYER_INVENTORY_SRC_Y;

    private static final int INVENTORY_SRC_X = 143;
    private static final int INVENTORY_SRC_Y = 177;
    private static final int INVENTORY_WIDTH = 90;
    private static final int INVENTORY_HEIGHT = 54;

    private static final int INVENTORY_BORDER_SRC_X = 50;
    private static final int INVENTORY_BORDER_SRC_Y = 176;
    private static final int INVENTORY_BORDER_WIDTH = 92;
    private static final int INVENTORY_BORDER_HEIGHT = 56;
    private static final int INVENTORY_BORDER_OFFSET_X = (INVENTORY_BORDER_WIDTH - INVENTORY_WIDTH) / 2;
    private static final int INVENTORY_BORDER_OFFSET_Y = (INVENTORY_BORDER_HEIGHT - INVENTORY_HEIGHT) / 2;

	@Override
	protected void drawBackground(int left, int top) {
        ResourceHelper.bindResource(CARGO_TEXTURE);

        drawTexturedModalRect(left, top + TOP_Y, 0, TOP_SRC_Y, TEXTURE_WIDTH, TOP_HEIGHT);
        for (int i = 0; i < MIDDLE_UNITS; i++) {
            drawTexturedModalRect(left, top + MIDDLE_Y + MIDDLE_HEIGHT * i, 0, MIDDLE_SRC_Y, TEXTURE_WIDTH, MIDDLE_HEIGHT);
        }
        drawTexturedModalRect(left, top + BOT_Y, 0, BOT_SRC_Y, TEXTURE_WIDTH, BOT_HEIGHT);
        drawTexturedModalRect(left + PLAYER_INVENTORY_X, top + PLAYER_INVENTORY_Y, PLAYER_INVENTORY_X, PLAYER_INVENTORY_SRC_Y, PLAYER_INVENTORY_WIDTH, PLAYER_INVENTORY_HEIGHT);
        drawTexturedModalRect(left + TEXTURE_WIDTH, top, 0, EXTRA_SRC_Y, EXTRA_WIDTH, EXTRA_HEIGHT);


        for (int i = 0; i < 4; i++) {
            int [] coordinate = getInventoryCoordinate(i);
            drawTexturedModalRect(left + coordinate[0], top + coordinate[1], INVENTORY_SRC_X, INVENTORY_SRC_Y, INVENTORY_WIDTH, INVENTORY_HEIGHT);
            if (hasSeparatedInventory()) {
                if (getManager().layoutType == 1) {
                    setColor(ColorEffect.BLACK);
                }else{
                    setColor(getManager().color[i] - 1);
                }
                drawTexturedModalRect(left + coordinate[0] - INVENTORY_BORDER_OFFSET_X, top + coordinate[1] - INVENTORY_BORDER_OFFSET_Y, INVENTORY_BORDER_SRC_X, INVENTORY_BORDER_SRC_Y, INVENTORY_BORDER_WIDTH, INVENTORY_BORDER_HEIGHT);
                setColor(ColorEffect.CLEAR);
            }
        }
    }


    @Override
	protected int getCenterTargetX() {
		return 98;
	}

	@Override
	protected void drawItems(int id, RenderItem renderitem, int left, int top) {
		ItemStack cartIcon;
	
		if (getCargo().target[id] < 0 || getCargo().target[id] >= TileEntityCargo.itemSelections.size() ||  TileEntityCargo.itemSelections.get(getCargo().target[id]).getIcon() == null) {
			cartIcon = new ItemStack(Items.minecart, 1);
		}else{
			cartIcon = TileEntityCargo.itemSelections.get(getCargo().target[id]).getIcon();
		}
	

		int[] coordinate = getBoxCoordinate(id);
		GL11.glDisable(GL11.GL_LIGHTING);
		renderitem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, cartIcon, left + coordinate[0], top + coordinate[1]);
		GL11.glEnable(GL11.GL_LIGHTING);	
	}
	
	@Override
	protected int offsetObjectY(int layout, int x, int y) {
        if (layout != 0) {
            return -5 + y * 10;
        }else{
			return super.offsetObjectY(layout, x, y);
		}
	}
	
	@Override
	protected boolean sendOnClick(int id, int button, int x, int y) {
		if (inRect(x, y, getBoxCoordinate(id))) {
            sendPacket(TileEntityManager.PacketId.VEHICLE_PART, id, button == 0);
			return true;
		}else{
			return false;
		}		
	} 
	
	@Override
	protected void drawExtraOverlay(int id, int x, int y) {
		if (getCargo().target[id] >= 0 && getCargo().target[id] < TileEntityCargo.itemSelections.size()) {
			CargoItemSelection item = TileEntityCargo.itemSelections.get(getCargo().target[id]);
			if (item.getName() != null) {
				drawMouseOver(LocalizationCargo.CHANGE_VEHICLE_PART.translate() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " + item.getName(),x,y, getBoxCoordinate(id));
			}else{
				drawMouseOver(LocalizationCargo.CHANGE_VEHICLE_PART.translate() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " + LocalizationCargo.SLOT_INVALID.translate(),x,y, getBoxCoordinate(id));
			}
		}else{
			drawMouseOver(LocalizationCargo.CHANGE_VEHICLE_PART.translate() + "\n" + LocalizationManager.CURRENT_SETTING.translate() + ": " + LocalizationCargo.SLOT_INVALID.translate(),x,y, getBoxCoordinate(id));
		}
	}
	
	@Override
	protected Block getBlock() {
		return ModBlocks.CARGO_MANAGER.getBlock();
	}

	@Override
	protected String getManagerName() {
		return LocalizationCargo.TITLE.translate();
	}
	
    private int[] getInventoryCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int offset = hasSeparatedInventory() ? 5 : 0;
        int targetX = 7 + x * 198;
        int targetY = 15 - offset + y * (54 + offset * 2) ;
        return new int[] {targetX, targetY};
    }	

    private boolean hasSeparatedInventory() {
        return getManager().layoutType != 0;
    }

	private TileEntityCargo getCargo() {
		return (TileEntityCargo)getManager();
	}
	
	@Override
	protected String getLayoutString() {
		return LocalizationCargo.CHANGE_LAYOUT.translate();
	}
	
	@Override
	protected String getLayoutOption(int id) {
		switch (id) {
            default:
			case 0:
				return LocalizationCargo.SHARED_LAYOUT.translate();
			case 1:
				return LocalizationCargo.SIDE_LAYOUT.translate();
			case 2:
				return LocalizationCargo.COLOR_LAYOUT.translate();
		}
	}
}
