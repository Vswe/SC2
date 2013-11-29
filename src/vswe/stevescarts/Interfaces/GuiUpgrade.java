package vswe.stevescarts.Interfaces;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.InterfaceEffect;
import vswe.stevescarts.Upgrades.InventoryEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiUpgrade extends GuiBase
{
    public GuiUpgrade(InventoryPlayer invPlayer, TileEntityUpgrade upgrade)
    {
        super(new ContainerUpgrade(invPlayer, upgrade));
        this.upgrade = upgrade;
        this.invPlayer = invPlayer;
        setXSize(256);
        setYSize(190);
    }

    public void drawGuiForeground(int x, int y)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
		
		if (upgrade.getUpgrade() != null) {
			fontRenderer.drawString(upgrade.getUpgrade().getName(), 8, 6, 0x404040);
			
			InterfaceEffect gui = upgrade.getUpgrade().getInterfaceEffect();
			if (gui != null) {
				gui.drawForeground(upgrade, this);
				gui.drawMouseOver(upgrade, this, x, y);
			}			
		}

		GL11.glEnable(GL11.GL_LIGHTING);
    }
	

    private static ResourceLocation texture = ResourceHelper.getResource("/gui/upgrade.png");
    public void drawGuiBackground(float f, int x, int y)
    {


        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = getGuiLeft();
        int k = getGuiTop();
		ResourceHelper.bindResource(texture);
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

		if (upgrade.getUpgrade() != null) {
			InventoryEffect inventory = upgrade.getUpgrade().getInventoryEffect();
			if (inventory != null) {
			
				for (int i = 0; i < inventory.getInventorySize(); i++)
				{
					drawTexturedModalRect(j + inventory.getSlotX(i) - 1, k + inventory.getSlotY(i) - 1, 0, ySize, 18, 18);
				}					
			}	
			
			
			InterfaceEffect gui = upgrade.getUpgrade().getInterfaceEffect();
			if (gui != null) {
				gui.drawBackground(upgrade, this, x, y);
			}
		}
		
		
 

		GL11.glColor4f(1F, 1F, 1F, 1F);
    }

 
    private TileEntityUpgrade upgrade;
    private InventoryPlayer invPlayer;
}
