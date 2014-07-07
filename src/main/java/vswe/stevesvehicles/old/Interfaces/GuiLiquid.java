package vswe.stevesvehicles.old.Interfaces;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.localization.entry.block.LocalizationLiquid;
import vswe.stevesvehicles.old.Blocks.ModBlocks;
import vswe.stevesvehicles.old.Containers.ContainerLiquid;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.TileEntities.TileEntityLiquid;
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
			String B = String.valueOf(getLiquid().getMaxAmount(id) / 1000F);
	
			if (B.charAt(B.length() - 1) == '0') {
				B = B.substring(0, B.length() - 2);
			}else if(B.charAt(0) == '0') {
				B = B.substring(1);
			}
			
			return B + LocalizationLiquid.TRANSFER_BUCKETS_SHORT.translate();
		}
	}	
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/liquidmanager.png");
	private static final ResourceLocation TEXTURE_EXTRA = ResourceHelper.getResource("/gui/liquidmanagerExtra.png");
	@Override
	protected void drawBackground(int left, int top) {
        ResourceHelper.bindResource(TEXTURE);
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		
		if (getLiquid().getTanks() != null) {
			for (int i = 0; i < 4; i++) {
				int [] coordinate = getTankCoordinate(i);
				getLiquid().getTanks()[i].drawFluid(this, coordinate[0], coordinate[1]);
			}
		}
		
		ResourceHelper.bindResource(TEXTURE_EXTRA);
		
		int version;
        if (getManager().layoutType == 0) {
            version = 0;
        }else{
            version = 1;
        }	
		
		for (int i = 0; i < 2; i++) {
			drawTexturedModalRect(left + (i == 0 ? 27 : 171), top + 63, 0, 102 + version * 12, 32, 12);
		}
		
		for (int i = 0; i < 4; i++) {
			int [] coordinate = getTankCoordinate(i);
			int type = i % 2;
			drawTexturedModalRect(left + coordinate[0], top + coordinate[1], 0, 51 * type, 36, 51);
		}
	}
	
	
	@Override
	protected int getArrowSourceX() {
		return 72;
	}

	@Override
	protected int getColorSourceX() {
		return 128;
	}
	
	@Override
	protected int getCenterTargetX() {
		return 62;
	}

	@Override
	protected void drawColors(int id, int color, int left, int top) {
		super.drawColors(id, color, left, top);

		if (getManager().layoutType == 2) {
			int [] coordinate = getTankCoordinate(id);
			drawTexturedModalRect(left + coordinate[0], top + coordinate[1], 36, 51 * color, 36, 51);
		}
		
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
        return new int[] {targetX, targetY, 36, 51};
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
