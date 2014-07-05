package vswe.stevesvehicles.old.Interfaces;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.gui.GuiBase;
import vswe.stevesvehicles.localization.entry.block.LocalizationDistributor;
import vswe.stevesvehicles.old.Containers.ContainerDistributor;
import vswe.stevesvehicles.old.Helpers.DistributorSetting;
import vswe.stevesvehicles.old.Helpers.DistributorSide;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.TileEntities.TileEntityDistributor;
import vswe.stevesvehicles.old.TileEntities.TileEntityManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDistributor extends GuiBase
{
    public GuiDistributor(InventoryPlayer invPlayer, TileEntityDistributor distributor)
    {
        super(new ContainerDistributor(invPlayer, distributor));
        this.invPlayer = invPlayer;
        setXSize(255);
        setYSize(186);
		this.distributor = distributor;
    }

	
	
    public void drawGuiForeground(int x, int y)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
	
        getFontRenderer().drawString(LocalizationDistributor.TITLE.translate(), 8, 6, 0x404040);
		
		TileEntityManager[] invs = distributor.getInventories();
		 
		if (invs.length == 0) {
			getFontRenderer().drawString(LocalizationDistributor.NOT_CONNECTED.translate(), 30, 40, 0xFF4040);
		}
			

		
		if (mouseOverText != null && !mouseOverText.equals("")) {
			drawMouseOver(mouseOverText, x- getGuiLeft(), y-getGuiTop());
		}
		mouseOverText = null;
		
 		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	private String mouseOverText;
	private void drawMouseMover(String str, int x, int y, int[] rect) {
		if (inRect(x,y,rect)) {
			mouseOverText = str;
		}	
	}

	private static ResourceLocation texture = ResourceHelper.getResource("/gui/distributor.png");
    public void drawGuiBackground(float f, int x, int y)
    {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


        int j = getGuiLeft();
        int k = getGuiTop();
        ResourceHelper.bindResource(texture);
        drawTexturedModalRect(j , k, 0, 0, xSize, ySize);

		x-= getGuiLeft();
		y-= getGuiTop();

		TileEntityManager[] invs = distributor.getInventories();
		ArrayList<DistributorSide> sides = distributor.getSides();
		
		int id = 0;
		for (DistributorSide side : sides) {
			if (side.isEnabled(distributor)) {
				int[] box = getSideBoxRect(id);
			
				int srcX = 0;
				
				if (inRect(x,y, box)) {
					srcX = box[2];
				}
			
				drawTexturedModalRect(j + box[0], k + box[1], srcX, ySize, box[2], box[3]);
				drawTexturedModalRect(j + box[0]+2, k + box[1]+2, box[2]*2 + (box[2]-4)*side.getId(), ySize, box[2]-4, box[3]-4);
				
				drawMouseMover(LocalizationDistributor.SIDE_NAME.translate(side.getName()) + (activeId != -1 ? "\n["+ LocalizationDistributor.DROP_INSTRUCTION.translate() + "]" : ""), x, y, box);
				
				int settingCount = 0;
				for (DistributorSetting setting : DistributorSetting.settings) { 
					if (setting.isEnabled(distributor)) {
						if (side.isSet(setting.getId())) {
							int[] settingbox = getActiveSettingBoxRect(id, settingCount++);
							
							drawSetting(setting, settingbox, inRect(x,y, settingbox));
								
							drawMouseMover(setting.getName(invs) + "\n[" + LocalizationDistributor.REMOVE_INSTRUCTION.translate() + "]", x, y, settingbox);
						}
					}
				}	
				
				id++;
			}
		}
		
		for (DistributorSetting setting : DistributorSetting.settings) {
			if (setting.isEnabled(distributor)) {
			
				int[] box = getSettingBoxRect(setting.getImageId(), setting.getIsTop());
				
				drawSetting(setting, box, inRect(x,y, box));
					
				drawMouseMover(setting.getName(invs), x, y, box);
			}
		}
		
		if (activeId != -1) {
			DistributorSetting setting = DistributorSetting.settings.get(activeId);	
			drawSetting(setting, new int[] {x-8,y-8, 16,16}, true);
		}
		

    }
	
	private void drawSetting(DistributorSetting setting, int[] box, boolean hover) {
        int j = getGuiLeft();
        int k = getGuiTop();	
	
		int srcX = 0;
		if (!setting.getIsTop()) {
			srcX += box[2]*2;
		}
		
		if (hover) {
			srcX += box[2];
		}			
		
		drawTexturedModalRect(j + box[0], k + box[1], srcX, ySize+getSideBoxRect(0)[3], box[2], box[3]);
		drawTexturedModalRect(j + box[0]+1, k + box[1]+1, box[2]*4 + (box[2]-2)*setting.getImageId(), ySize+getSideBoxRect(0)[3], box[2]-2, box[3]-2);
	}
	
	private int[] getSideBoxRect(int i) {
		return new int[] {20, 18 + i * 24, 22,22};
	}
	private int[] getSettingBoxRect(int i, boolean topRow) {
		return new int[] {20 + i * 18, 143 + (topRow ? 0 : 18), 16,16};
	}
	private int[] getActiveSettingBoxRect(int side, int setting) {
		int[] sideCoords = getSideBoxRect(side);
	
		return new int[] {sideCoords[0] + sideCoords[2] + 5 + setting * 18, sideCoords[1] + (sideCoords[3] - 16) / 2, 16,16};
	}	
 
	private int activeId = -1;
    public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);
		x-= getGuiLeft();
		y-= getGuiTop();

		if (button == 0) {
			for (DistributorSetting setting : DistributorSetting.settings) {
				if (setting.isEnabled(distributor)) {
					int[] box = getSettingBoxRect(setting.getImageId(), setting.getIsTop());

					if (inRect(x,y, box)) {
						activeId = setting.getId();
					}
				}
			}	
		}
    }
	
	public void mouseMoved(int x, int y, int button)
    {
        super.mouseMoved(x, y, button);		
		x -= getGuiLeft();
		y -= getGuiTop();	

		if (button == 0 && activeId != -1) {
			int id = 0;
			for (DistributorSide side : distributor.getSides()) {
				if (side.isEnabled(distributor)) {
					int[] box = getSideBoxRect(id++);
					
					if (inRect(x,y, box)) {
						distributor.sendPacket(0, new byte[] {(byte)activeId,(byte)side.getId()} );
						break;
					}
				}
			}
								
			activeId = -1;
		}else if(button == 1) {
			int id = 0;
			for (DistributorSide side : distributor.getSides()) {
				if (side.isEnabled(distributor)) {
					int settingCount = 0;
					for (DistributorSetting setting : DistributorSetting.settings) { 
						if (setting.isEnabled(distributor)) {
							if (side.isSet(setting.getId())) {
								int[] settingbox = getActiveSettingBoxRect(id, settingCount++);
								
								if (inRect(x,y, settingbox)) {
									distributor.sendPacket(1, new byte[] {(byte)setting.getId(),(byte)side.getId()} );
								}
									
							}
						}
					}
					id++;
				}
			}
		}
	}
	
	TileEntityDistributor distributor;
	InventoryPlayer invPlayer;
}
