package vswe.stevesvehicles.client.gui.screen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.localization.entry.block.LocalizationToggler;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.container.ContainerActivator;
import vswe.stevesvehicles.network.PacketType;
import vswe.stevesvehicles.old.Helpers.ActivatorOption;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityActivator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiActivator extends GuiBase {
    public GuiActivator(InventoryPlayer invPlayer, TileEntityActivator activator) {
        super(new ContainerActivator(activator));
        setXSize(255);
        setYSize(222);
		this.activator = activator;
    }

    @Override
    public void drawGuiForeground(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);
	
        getFontRenderer().drawString(LocalizationToggler.TITLE.translate(), 8, 6, 0x404040);
		
		for (int i = 0; i < activator.getOptions().size(); i++) {
			ActivatorOption option = activator.getOptions().get(i);

			int[] box = getBoxRect(i);
			
			getFontRenderer().drawString(option.getName(), box[0] + box[2] + 6, box[1] + 4, 0x404040);
		}			
		
		for (int i = 0; i < activator.getOptions().size(); i++) {
			ActivatorOption option = activator.getOptions().get(i);

			int[] box = getBoxRect(i);

			drawMouseMover(option.getInfo(),x,y,box);
		}	
		
 		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	private void drawMouseMover(String str, int x, int y, int[] rect) {
		if (inRect(x - getGuiLeft(),y - getGuiTop(), rect)) {
			drawMouseOver(str, x - getGuiLeft(), y - getGuiTop());
		}	
	}

    private static final int TEXTURE_SPACING = 1;
    private static final int BORDER_SRC_X = 1;
    private static final int BORDER_SRC_Y = 223;
    private static final int MODE_SRC_X = 1;
    private static final int MODE_SRC_Y = 240;
    private static final int MODE_SIZE = 14;

	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/activator.png");
    @Override
    public void drawGuiBackground(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


        int left = getGuiLeft();
        int top = getGuiTop();
        ResourceHelper.bindResource(TEXTURE);
        drawTexturedModalRect(left , top, 0, 0, xSize, ySize);

		x -= left;
		y -= top;
		
		for (int i = 0; i < activator.getOptions().size(); i++) {
			ActivatorOption option = activator.getOptions().get(i);
			
			int[] box = getBoxRect(i);
			int srcX = 0;
			
			if (inRect(x,y, box)) {
				srcX = box[2] + TEXTURE_SPACING;
			}
			
			drawTexturedModalRect(left + box[0], top + box[1], BORDER_SRC_X + srcX, BORDER_SRC_Y, box[2], box[3]);
			drawTexturedModalRect(left + box[0] + 1, top + box[1] + 1, MODE_SRC_X + (MODE_SIZE + TEXTURE_SPACING) * option.getOption(), MODE_SRC_Y, MODE_SIZE, MODE_SIZE);
		}
	

    }
	
	private int[] getBoxRect(int i) {
		return new int[] {20, 22 + i * 20, 16, 16};
	}


    @Override
    public void mouseClick(int x, int y, int button) {
        super.mouseClick(x, y, button);
		x-= getGuiLeft();
		y-= getGuiTop();

 		for (int i = 0; i < activator.getOptions().size(); i++) {
			int[] box = getBoxRect(i);

			if (inRect(x,y, box)) {
                DataWriter dw = PacketHandler.getDataWriter(PacketType.CONTAINER);
                dw.writeBoolean(button == 0);
                dw.writeByte(i);
				PacketHandler.sendPacketToServer(dw);
			}
		}
    }
	
	TileEntityActivator activator;
}
