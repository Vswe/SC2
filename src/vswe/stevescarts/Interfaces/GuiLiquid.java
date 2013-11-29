package vswe.stevescarts.Interfaces;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerLiquid;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityLiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLiquid extends GuiManager
{
    public GuiLiquid(InventoryPlayer invPlayer, TileEntityLiquid liquid)
    {
        super(invPlayer, liquid, new ContainerLiquid(invPlayer, liquid));
        setXSize(230);
        setYSize(222);
    }


	@Override
	protected String getMaxSizeOverlay(int id) {
		int amount = getLiquid().getMaxAmount(id);
	
		if (!getLiquid().hasMaxAmount(id)) {
			return "Transfer as much as possible";
		}else{
			return "Transfer a maximum of " + getMaxSizeText(id);
		}
	}
	
	@Override
	protected String getMaxSizeText(int id) {


		if (!getLiquid().hasMaxAmount(id)){
			return "MAX";
		}else{
			String B = String.valueOf(getLiquid().getMaxAmount(id) / 1000F);
	
			if (B.charAt(B.length() - 1) == '0') {
				B = B.substring(0, B.length() - 2);
			}else if(B.charAt(0) == '0') {
				B = B.substring(1);
			}
			
			return B + "B";
		}
	}	
	
	private static ResourceLocation texture = ResourceHelper.getResource("/gui/liquidmanager.png");
	private static ResourceLocation textureExtra = ResourceHelper.getResource("/gui/liquidmanagerExtra.png");
	@Override
	protected void drawBackground(int left, int top) {
        ResourceHelper.bindResource(texture);
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		
		if (getLiquid().getTanks() != null) {
			for (int i = 0; i < 4; i++) {
				int [] coords = getTankCoords(i);
				getLiquid().getTanks()[i].drawFluid(this, coords[0], coords[1]);
			}
		}
		
		ResourceHelper.bindResource(textureExtra);
		
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
			int [] coords = getTankCoords(i);
			int type = i % 2;
			drawTexturedModalRect(left + coords[0], top + coords[1], 0, 51 * type, 36, 51);	
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
			int [] coords = getTankCoords(id);
			drawTexturedModalRect(left + coords[0], top + coords[1], 36, 51 * color, 36, 51);
		}
		
	}	
	
	@Override
	protected int offsetObjectY(int layout, int x, int y) {
		return -5 + y * 10;
	}

	
	@Override
	protected void drawExtraOverlay(int id, int x, int y) {
		drawMouseOver(getLiquid().getTanks()[id].getMouseOver() ,x,y,getTankCoords(id));				
	}
	
	@Override
	protected Block getBlock() {
		return StevesCarts.instance.blockLiquidManager;
	}

	@Override
	protected String getManagerName() {
		return "Liquid";
	}
	
    private int[] getTankCoords(int id) {
        int x = id % 2;
        int y = id / 2;
        int xCoord = 25 + x * 144;
        int yCoord = 12 + y * 63;
        return new int[] {xCoord, yCoord, 36, 51};
    }	
	

	private TileEntityLiquid getLiquid() {
		return (TileEntityLiquid)getManager();
	}
	
	@Override
	protected String getLayoutString() {
		return "Change tank layout";
	}
	
	@Override
	protected String getLayoutOption(int id) {
		switch (id) {
			case 0:
				return "All tanks are shared by all sides";
			case 1:
				return "Each side has its own tank";
			case 2:
				return "Each color has its own tank(s)";
			default:
				return "Invalid layout";
		}
	}	
}
