package vswe.stevescarts.old.Interfaces;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.GeneratedInfo;
import vswe.stevescarts.old.Helpers.ModuleCountPair;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Slots.SlotBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMinecart extends GuiBase
{
    public GuiMinecart(InventoryPlayer invPlayer, EntityModularCart cart)
    {
        super(cart.getCon(invPlayer));
        setup(cart);
    }

    protected void setup(EntityModularCart cart)
    {
        this.cart = cart;

		setXSize(478);
		setYSize(256);
    }

    public void drawGuiForeground(int x, int y)
    {

		GL11.glDisable(GL11.GL_LIGHTING);

	
		if (cart.getModules() != null) {
			ModuleBase thief = cart.getInterfaceThief();
			
			if (thief != null) {
				drawModuleForeground(thief);
				drawModuleMouseOver(thief, x, y);
			}else{
				for (ModuleBase module : cart.getModules()) {
					drawModuleForeground(module);
				}
	
				renderModuleListText(x, y);
				
				for (ModuleBase module : cart.getModules()) {
					drawModuleMouseOver(module, x, y);
				}
				
				renderModuleListMouseOver(x, y);				
			}
		}
			
		GL11.glEnable(GL11.GL_LIGHTING);	
    }
    
 

    private static ResourceLocation textureLeft = ResourceHelper.getResource("/gui/guiBase1.png");
    private static ResourceLocation textureRight = ResourceHelper.getResource("/gui/guiBase2.png");
    public void drawGuiBackground(float f, int x, int y)
    {
		GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = getGuiLeft();
        int k = getGuiTop();

		ResourceHelper.bindResource(textureLeft);
        drawTexturedModalRect(j, k, 0, 0, 256, ySize);

        ResourceHelper.bindResource(textureRight);
        drawTexturedModalRect(j+256, k, 0, 0, xSize-256, ySize);
        
		ModuleBase thief = cart.getInterfaceThief();

		if (thief != null) {
			drawModuleSlots(thief);
			drawModuleBackground(thief, x, y);
			drawModuleBackgroundItems(thief, x, y);
			
			for (ModuleBase module : cart.getModules()) {
				if (module.hasGui()) {
					if (module.hasSlots()) {
						ArrayList<SlotBase> slotsList = module.getSlots();
						for (SlotBase slot : slotsList) {
							resetSlot(slot);
						}
					}
				}
			}
		
		}else if (cart.getModules() != null) {
			drawTexturedModalRect(j + scrollBox[0], k + scrollBox[1], 222, 24, scrollBox[2], scrollBox[3]);
			drawTexturedModalRect(j + scrollBox[0] + 2, k + scrollBox[1] + 2 + cart.getScrollY(), 222+18, 26 + (cart.canScrollModules ? 0 : 25), 14, 25);
			
								
			for (ModuleBase module : cart.getModules()) {
				drawModuleSlots(module);
			}

			//loop again since these function calls will probably bind other textures
			for (ModuleBase module : cart.getModules()) {
				drawModuleBackground(module, x, y);
			}
			
			renderModuleList(x, y);
			
			for (ModuleBase module : cart.getModules()) {
				drawModuleBackgroundItems(module, x, y);
			}
			
		}
		
		

		GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    public static ResourceLocation moduleTexture = ResourceHelper.getResourceFromPath("/atlas/items.png");
    
    private void renderModuleList(int x, int y) {
    	x -= getGuiLeft();
    	y -= getGuiTop();
    	
    	ArrayList<ModuleCountPair> moduleCounts = cart.getModuleCounts();
    	ResourceHelper.bindResource(moduleTexture);
    	
    	GL11.glEnable(GL11.GL_BLEND);
    	
    	for (int i = 0; i < moduleCounts.size(); i++) {
    		ModuleCountPair count = moduleCounts.get(i);
    		
    		float alpha = inRect(x, y, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16) ? 1 : 0.5F;
    		GL11.glColor4f(1F, 1F, 1F, alpha);
    		
    		this.drawIcon(count.getData().getIcon(), getGuiLeft() + getModuleDisplayX(i), getGuiTop() + getModuleDisplayY(i), 1, 1, 0, 0);
    	}
    	GL11.glDisable(GL11.GL_BLEND);
    }
    
    private void renderModuleListText(int x, int y) {    
    	x -= getGuiLeft();
    	y -= getGuiTop();    	
    	
    	ArrayList<ModuleCountPair> moduleCounts = cart.getModuleCounts();
    	GL11.glColor4f(1F, 1F, 1F, 1F);
    	
    	getFontRenderer().drawString(cart.getCartName(), 5, 172, 0x404040);
    	
    	GL11.glEnable(GL11.GL_BLEND);
    	for (int i = 0; i < moduleCounts.size(); i++) {
    		ModuleCountPair count = moduleCounts.get(i);
    		
    		if (count.getCount() != 1) {
        		int alpha = (int)((inRect(x, y, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16) ? 1 : 0.75F) * 256);
    			
    			String str = String.valueOf(count.getCount());
    			getFontRenderer().drawStringWithShadow(str, getModuleDisplayX(i) + 16 - getFontRenderer().getStringWidth(str), getModuleDisplayY(i) + 8, 0xFFFFFF | (alpha << 24));
    		}
    	}
    	GL11.glDisable(GL11.GL_BLEND);
    }
    
    private void renderModuleListMouseOver(int x, int y) {
    	x -= getGuiLeft();
    	y -= getGuiTop();    	
    	
    	ArrayList<ModuleCountPair> moduleCounts = cart.getModuleCounts();
    	GL11.glColor4f(1F, 1F, 1F, 1F);
    	
    	for (int i = 0; i < moduleCounts.size(); i++) {
    		ModuleCountPair count = moduleCounts.get(i);
    		
    		if (inRect(x, y, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16)) {
    			drawMouseOver(count.toString(), x, y);
    		}
    	}    	
    }
    
    private int getModuleDisplayX(int id) {
    	return (id % 8) * 18 + 7;
    }
    
    private int getModuleDisplayY(int id) {
    	return (id / 8) * 18 + 182;
    }
    
	private boolean isScrolling;
	private int[] scrollBox = new int[] {450,15,18,225};
	public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);


		ModuleBase thief = cart.getInterfaceThief();
		
		if (thief != null) {
			handleModuleMouseClicked(thief, x, y, button);				
		}else if (cart.getModules() != null) {		
			if (inRect(x-getGuiLeft(),y-getGuiTop(), scrollBox[0], scrollBox[1], scrollBox[2], scrollBox[3])) {
				isScrolling = true;
			}			
			
			for (ModuleBase module : cart.getModules()) {
				handleModuleMouseClicked(module, x, y, button);
			}
		}
		
    }

	protected boolean inRect(int x, int y, int x1, int y1, int sizeX, int sizeY) {
		return x >= x1 && x <= x1 + sizeX && y >= y1 && y <= y1 + sizeY;
	}	
	
	public void mouseMoved(int x, int y, int button)
    {
        super.mouseMoved(x, y, button);
		
		if (isScrolling) {
			int temp = y-getGuiTop() - 12 - (scrollBox[1] + 2);
			if (temp < 0) {
				temp = 0;
			}else if(temp > 198) {
				temp = 198;
			}
			cart.setScrollY(temp);
		}
		
		if (button != -1) {
			isScrolling = false;
		}	
		
		
		if (cart.getModules() != null) {
			ModuleBase thief = cart.getInterfaceThief();
			
			if (thief != null) {
				handleModuleMouseMoved(thief, x, y, button);
			}else{
				for (ModuleBase module : cart.getModules()) {
					handleModuleMouseMoved(module, x, y, button);
				}
			}
		}
    }

	public void keyPress(char character, int extraInformation) {
		super.keyPress(character,extraInformation);
		
		if (cart.getModules() != null) {

			ModuleBase thief = cart.getInterfaceThief();
			
			if (thief != null) {
				handleModuleKeyPress(thief, character, extraInformation);
			}else{
				for (ModuleBase module : cart.getModules()) {
					handleModuleKeyPress(module, character, extraInformation);
				}
			}
		}	
	}	
	
	
    @Override
    public boolean disableStandardKeyFunctionality() {
		if (cart.getModules() != null) {

			ModuleBase thief = cart.getInterfaceThief();
			
			if (thief != null) {
				return thief.disableStandardKeyFunctionality();
			}else{
				for (ModuleBase module : cart.getModules()) {
					if (module.disableStandardKeyFunctionality()) {
						return true;
					}
				}
			}
		}
		
		return false;
    }	
	
    private EntityModularCart cart;


    private void drawModuleForeground(ModuleBase module) {
    	if (module.hasGui()) {
	    	module.drawForeground(this);
			if (module.useButtons()) {
				module.drawButtonText(this);
			}
    	}
    }
    
    private void drawModuleMouseOver(ModuleBase module, int x, int y) {
		if (module.hasGui()) {
			module.drawMouseOver(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
			if (module.useButtons()) {
				module.drawButtonOverlays(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
			}					
		}    	
    }
    
    private void drawModuleSlots(ModuleBase module) {
		if (module.hasGui()) {
			if (module.hasSlots()) {
				ArrayList<SlotBase> slotsList = module.getSlots();

				for (SlotBase slot : slotsList) {
					int[] rect = new int[] {slot.getX() + 1, slot.getY() + 1, 16, 16};
					module.handleScroll(rect);
					boolean drawAll = rect[3] == 16;
					if (drawAll) {
						slot.xDisplayPosition = slot.getX() + module.getX() + 1;
						slot.yDisplayPosition = slot.getY() + module.getY() + 1 - cart.getRealScrollY();
					}else{
						resetSlot(slot);
					}
				
					module.drawImage(this, slot.getX(), slot.getY(), xSize-256, 0, 18, 18);
					if (!drawAll) {
						module.drawImage(this, slot.getX()+1, slot.getY()+1, xSize-256+18, 1, 16, 16);
					}
				}
			}

			if (GeneratedInfo.inDev) {
				drawTexturedModalRect(getGuiLeft() + module.getX(), getGuiTop() + module.getY(), xSize-256, 18, 3, 3);
				drawTexturedModalRect(getGuiLeft() + module.getX() + module.guiWidth() - 2, getGuiTop() + module.getY(), xSize-256 + 3, 18, 3, 3);
				drawTexturedModalRect(getGuiLeft() + module.getX() + module.guiWidth() - 2, getGuiTop() + module.getY() + module.guiHeight() - 2, xSize-256 + 3, 18+3, 3, 3);
				drawTexturedModalRect(getGuiLeft() + module.getX(), getGuiTop() + module.getY() + module.guiHeight() - 2, xSize-256, 18 + 3, 3, 3);
			}
		}
		
    } 

    private void resetSlot(SlotBase slot) {
		slot.xDisplayPosition = -9001;
		slot.yDisplayPosition = -9001;
    }
    
    private void drawModuleBackground(ModuleBase module, int x, int y) {
		if (module.hasGui()) {
			module.drawBackground(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
			if (module.useButtons()) {
				module.drawButtons(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
			}
		}  	
    }
    
    private void drawModuleBackgroundItems(ModuleBase module, int x, int y) {
		if (module.hasGui()) {
			module. drawBackgroundItems(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
		}  	
    }    
    
   
    
    private void handleModuleMouseClicked(ModuleBase module, int x, int y, int button) {
		module.mouseClicked(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY(),button);
		if (module.useButtons()) {
			module.mouseClickedButton(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY(),button);
		}  	
    }
    
    private void handleModuleMouseMoved(ModuleBase module, int x, int y, int button) {
    	module.mouseMovedOrUp(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY(),button);    	
    }
    
    private void handleModuleKeyPress(ModuleBase module, char character, int extraInformation) {
    	module.keyPress(this, character, extraInformation);    	
    }	
	
    

    
}
