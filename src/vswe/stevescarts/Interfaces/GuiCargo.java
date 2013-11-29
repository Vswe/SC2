package vswe.stevescarts.Interfaces;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerCargo;
import vswe.stevescarts.Helpers.CargoItemSelection;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityCargo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCargo extends GuiManager
{
    public GuiCargo(InventoryPlayer invPlayer, TileEntityCargo cargo)
    {
        super(invPlayer, cargo, new ContainerCargo(invPlayer, cargo));
        setXSize(305);
        setYSize(222);
    }

	@Override
	protected String getMaxSizeOverlay(int id) {
		int amount = getCargo().getAmount(id);
		int type = getCargo().getAmountType(id);
	
		if (type == 0) {
			return "Transfer as much as possible";
		}else if (type == 1) {
			return "Transfer a maximum of " + amount + " item" + (amount == 1 ? "" : "s");
		}else{
			return "Transfer a maximum of " + amount + " stack" + (amount == 1 ? "" : "s");
		}
	}
	
	@Override
	protected String getMaxSizeText(int id) {
		String s;
		int type = getCargo().getAmountType(id);

		if (type == 0){
			s = "MAX";
		}else{
			int amount = getCargo().getAmount(id);
			s = String.valueOf(amount);

			if (type == 1){
				s += " I";
			}else{
				s += " S";
			}
		}
		return s;
	}

	private static ResourceLocation[] texturesLeft = {ResourceHelper.getResource("/gui/cargoVersion0Part1.png"), ResourceHelper.getResource("/gui/cargoVersion1Part1.png")};
	private static ResourceLocation[] texturesRight = {ResourceHelper.getResource("/gui/cargoVersion0Part2.png"), ResourceHelper.getResource("/gui/cargoVersion1Part2.png")};
	
	@Override
	protected void drawBackground(int left, int top) {
		int version;

        if (getManager().layoutType == 0) {
            version = 0;
        }else{
            version = 1;
        }	
	
        ResourceHelper.bindResource(texturesLeft[version]);
        drawTexturedModalRect(left, top, 0, 0, 256, ySize);
        ResourceHelper.bindResource(texturesRight[version]);
        drawTexturedModalRect(left + 256, top, 0, 0, xSize - 256, ySize);
	}
	
	
	@Override
	protected int getArrowSourceX() {
		return 49;
	}

	@Override
	protected int getColorSourceX() {
		return 105;
	}
	
	@Override
	protected int getCenterTargetX() {
		return 98;
	}

	@Override
	protected void drawColors(int id, int color, int left, int top) {
		super.drawColors(id, color, left, top);

		if (getManager().layoutType == 2) {
			int [] coords = getInvCoords(id);
			drawTexturedModalRect(left + coords[0] - 2, top + coords[1] - 2, 125, 56 * color, 92, 56);
		}
		
	}	
	
	@Override
	protected void drawItems(int id, RenderItem renderitem, int left, int top) {
		ItemStack cartIcon;
	
		if (getCargo().target[id] < 0 || getCargo().target[id] >= getCargo().itemSelections.size() ||  getCargo().itemSelections.get(getCargo().target[id]).getIcon() == null) {
			cartIcon = new ItemStack(Item.minecartEmpty, 1);
		}else{
			cartIcon = getCargo().itemSelections.get(getCargo().target[id]).getIcon();
		}
	

		int[] coords = getBoxCoords(id);
		GL11.glDisable(GL11.GL_LIGHTING);
		renderitem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, cartIcon, left + coords[0], top + coords[1]);
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
	protected boolean sendOnClick(int id, int x, int y, byte data) {
		if (inRect(x, y, getBoxCoords(id))) {
			getManager().sendPacket(1, data);
			return true;
		}else{
			return false;
		}		
	} 
	
	@Override
	protected void drawExtraOverlay(int id, int x, int y) {
		if (getCargo().target[id] >= 0 && getCargo().target[id] < getCargo().itemSelections.size()) {
			CargoItemSelection item = getCargo().itemSelections.get(getCargo().target[id]);
			if (item.getName() != null) {
				drawMouseOver("Change part of the cart\nCurrently: " + item.getName(),x,y,getBoxCoords(id));	
			}else{
				drawMouseOver("Change part of the cart\nCurrently: Unknown option",x,y,getBoxCoords(id));	
			}
		}else{
			drawMouseOver("Change part of the cart\nCurrently: Unknown option",x,y,getBoxCoords(id));	
		}
	}
	
	@Override
	protected Block getBlock() {
		return StevesCarts.instance.blockCargoManager;
	}

	@Override
	protected String getManagerName() {
		return "Cargo";
	}
	
    private int[] getInvCoords(int id) {
        int x = id % 2;
        int y = id / 2;
        int xCoord = 8 + x * 198;
        int yCoord = 11 + y * 64;
        return new int[] {xCoord, yCoord};
    }	
	
	private TileEntityCargo getCargo() {
		return (TileEntityCargo)getManager();
	}
	
	@Override
	protected String getLayoutString() {
		return "Change slot layout";
	}
	
	@Override
	protected String getLayoutOption(int id) {
		switch (id) {
			case 0:
				return "All slots are shared by all sides";
			case 1:
				return "Each side has its own slots";
			case 2:
				return "Each color has its own slots";
			default:
				return "Invalid layout";
		}
	}
}
