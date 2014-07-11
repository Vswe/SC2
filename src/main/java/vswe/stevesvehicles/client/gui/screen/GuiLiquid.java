package vswe.stevesvehicles.client.gui.screen;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.localization.entry.block.LocalizationLiquid;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.old.Containers.ContainerLiquid;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityLiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLiquid extends GuiManager {
    public GuiLiquid(InventoryPlayer invPlayer, TileEntityLiquid liquid) {
        super(liquid, new ContainerLiquid(invPlayer, liquid));
        setXSize(230);
        setYSize(222);
    }


	@Override
	protected String getMaxSizeOverlay(int id) {
		if (!getLiquid().hasMaxAmount(id)) {
			return LocalizationLiquid.TRANSFER_ALL.translate();
		}else{
			return LocalizationLiquid.TRANSFER_BUCKETS.translate(getMaxSizeText(id));
		}
	}
	
	@Override
	protected String getMaxSizeText(int id) {
		if (!getLiquid().hasMaxAmount(id)){
			return LocalizationLiquid.TRANSFER_ALL_SHORT.translate();
		}else{
			String b = String.valueOf(getLiquid().getMaxAmount(id) / 1000F);
	
			if (b.charAt(b.length() - 1) == '0') {
				b = b.substring(0, b.length() - 2);
			}else if(b.charAt(0) == '0') {
				b = b.substring(1);
			}
			
			return b + LocalizationLiquid.TRANSFER_BUCKETS_SHORT.translate();
		}
	}	
	
	private static final ResourceLocation LIQUID_TEXTURE = ResourceHelper.getResource("/gui/liquid.png");

    private static final int TEXTURE_WIDTH = 230;
    private static final int TOP_HEIGHT = 9;
    private static final int MIDDLE_HEIGHT = 50;
    private static final int MIDDLE_UNITS = 2;
    private static final int BOT_HEIGHT = 113;
    private static final int TEXTURE_SPACING = 1;

    private static final int TOP_Y = 0;
    private static final int MIDDLE_Y = TOP_Y + TOP_HEIGHT;
    private static final int BOT_Y = MIDDLE_Y + MIDDLE_HEIGHT * MIDDLE_UNITS;

    private static final int TOP_SRC_Y = 0;
    private static final int MIDDLE_SRC_Y = TOP_SRC_Y + TOP_HEIGHT + TEXTURE_SPACING;
    private static final int BOT_SRC_Y = MIDDLE_SRC_Y + MIDDLE_HEIGHT + TEXTURE_SPACING;



    private static final int TANK_SRC_X = 1;
    private static final int TANK_SRC_Y = 175;
    private static final int TANK_BORDER_SRC_X = 38;
    private static final int TANK_BORDER_SRC_Y = 175;
    private static final int TANK_WIDTH = 36;
    private static final int TANK_HEIGHT = 61;

    private static final int SLOT_SRC_X = 75;
    private static final int SLOT_SRC_Y = 175;
    private static final int SLOT_SIZE = 18;

    private static final int DOUBLE_SLOT_WIDTH = 18;
    private static final int DOUBLE_SLOT_HEIGHT = 43;
    private static final int DOUBLE_SLOT_SRC_X = 94;
    private static final int DOUBLE_SLOT_SRC_Y = 175;

    private static final int TUBES_SRC_X = 113;
    private static final int TUBES_SRC_Y = 175;

    @Override
	protected void drawBackground(int left, int top) {
        ResourceHelper.bindResource(LIQUID_TEXTURE);
        drawTexturedModalRect(left, top + TOP_Y, 0, TOP_SRC_Y, TEXTURE_WIDTH, TOP_HEIGHT);
        for (int i = 0; i < MIDDLE_UNITS; i++) {
            drawTexturedModalRect(left, top + MIDDLE_Y + MIDDLE_HEIGHT * i, 0, MIDDLE_SRC_Y, TEXTURE_WIDTH, MIDDLE_HEIGHT);
        }
        drawTexturedModalRect(left, top + BOT_Y, 0, BOT_SRC_Y, TEXTURE_WIDTH, BOT_HEIGHT);

		
		if (getLiquid().getTanks() != null) {
			for (int i = 0; i < 4; i++) {
				int [] coordinate = getTankCoordinate(i);
				getLiquid().getTanks()[i].drawFluid(this, coordinate[0], coordinate[1]);
			}
		}
		
		ResourceHelper.bindResource(LIQUID_TEXTURE);
		
		int texture = getManager().layoutType == 0 ? 0 : 1;

		
		for (int i = 0; i < 2; i++) {
			drawTexturedModalRect(left + (i == 0 ? 27 : 171), top + 63, TUBES_SRC_X, TUBES_SRC_Y + texture * 13, 32, 12);
		}
		
		for (int i = 0; i < 4; i++) {
			int [] coordinate = getTankCoordinate(i);
			RenderRotation rotation = i % 2 == 0 ? RenderRotation.NORMAL : RenderRotation.FLIP_HORIZONTAL;
			drawRect(left + coordinate[0], top + coordinate[1], TANK_SRC_X, TANK_SRC_Y, coordinate[2], coordinate[3], rotation);

            if (getManager().layoutType == 2) {
                setColor(getManager().color[i] - 1);
                drawTexturedModalRect(left + coordinate[0], top + coordinate[1], TANK_BORDER_SRC_X, TANK_BORDER_SRC_Y, coordinate[2], coordinate[3]);
                setColor(ColorEffect.CLEAR);
            }
            coordinate = getTankSlotsCoordinate(i);
            drawTexturedModalRect(left + coordinate[0], top + coordinate[1], DOUBLE_SLOT_SRC_X, DOUBLE_SLOT_SRC_Y, coordinate[2], coordinate[3]);
		}


	}


    @Override
	protected int getCenterTargetX() {
		return 62;
	}

	@Override
	protected void drawColors(int id, int color, int left, int top) {
		super.drawColors(id, color, left, top);


        ResourceHelper.bindResource(LIQUID_TEXTURE);
        int [] coordinate = getBoxCoordinate(id);
        drawTexturedModalRect(left + coordinate[0] - 1, top + coordinate[1] - 1, SLOT_SRC_X, SLOT_SRC_Y, SLOT_SIZE, SLOT_SIZE);
        ResourceHelper.bindResource(MANAGER_TEXTURE);
	}

	@Override
	protected int offsetObjectY(int layout, int x, int y) {
		return -5 + y * 10;
	}

	
	@Override
	protected void drawExtraOverlay(int id, int x, int y) {
		drawMouseOver(getLiquid().getTanks()[id].getMouseOver() ,x,y, getTankCoordinate(id));
	}
	
	@Override
	protected Block getBlock() {
		return ModBlocks.LIQUID_MANAGER.getBlock();
	}

	@Override
	protected String getManagerName() {
		return LocalizationLiquid.TITLE.translate();
	}
	
    private int[] getTankCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int targetX = 25 + x * 144;
        int targetY = 12 + y * 63;
        return new int[] {targetX, targetY, TANK_WIDTH, TANK_HEIGHT};
    }

    private int[] getTankSlotsCoordinate(int id) {
        int x = id % 2;
        int y = id / 2;
        int targetX = 5 + x * 202;
        int targetY = 16 + y * 63;
        return new int[] {targetX, targetY, DOUBLE_SLOT_WIDTH, DOUBLE_SLOT_HEIGHT};
    }


    private TileEntityLiquid getLiquid() {
		return (TileEntityLiquid)getManager();
	}
	
	@Override
	protected String getLayoutString() {
		return LocalizationLiquid.CHANGE_LAYOUT.translate();
	}
	
	@Override
	protected String getLayoutOption(int id) {
		switch (id) {
            default:
			case 0:
				return LocalizationLiquid.SHARED_LAYOUT.translate();
			case 1:
				return LocalizationLiquid.SIDE_LAYOUT.translate();
			case 2:
				return LocalizationLiquid.COLOR_LAYOUT.translate();
		}
	}	
}
