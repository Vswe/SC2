package vswe.stevesvehicles.client.gui.screen;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.container.slots.ISpecialSlotRender;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.GeneratedInfo;
import vswe.stevesvehicles.old.Helpers.ModuleCountPair;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.container.slots.SlotBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiVehicle extends GuiBase {

    private VehicleBase vehicle;

    public GuiVehicle(InventoryPlayer invPlayer, VehicleBase vehicle) {
        super(vehicle.getCon(invPlayer));
        setup(vehicle);
    }

    protected void setup(VehicleBase vehicle){
        this.vehicle = vehicle;

		setXSize(478);
		setYSize(256);
    }

    @Override
    public void drawGuiForeground(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);

	
		if (vehicle.getModules() != null) {
			ModuleBase thief = vehicle.getInterfaceThief();
			
			if (thief != null) {
				drawModuleForeground(thief);
				drawModuleMouseOver(thief, x, y);
			}else{
				for (ModuleBase module : vehicle.getModules()) {
					drawModuleForeground(module);
				}
	
				renderModuleListText(x, y);
				
				for (ModuleBase module : vehicle.getModules()) {
					drawModuleMouseOver(module, x, y);
				}
				
				renderModuleListMouseOver(x, y);				
			}
		}
			
		GL11.glEnable(GL11.GL_LIGHTING);	
    }
    




    private static final ResourceLocation TEXTURE_LEFT = ResourceHelper.getResource("/gui/guiBase1.png");
    private static final ResourceLocation TEXTURE_RIGHT = ResourceHelper.getResource("/gui/guiBase2.png");
    @Override
    public void drawGuiBackground(float f, int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = getGuiLeft();
        int k = getGuiTop();

		ResourceHelper.bindResource(TEXTURE_LEFT);
        drawTexturedModalRect(j, k, 0, 0, 256, ySize);

        ResourceHelper.bindResource(TEXTURE_RIGHT);
        drawTexturedModalRect(j+256, k, 0, 0, xSize-256, ySize);
        
		ModuleBase thief = vehicle.getInterfaceThief();

		if (thief != null) {
			drawModuleSlots(thief);
			drawModuleBackground(thief, x, y);

			for (ModuleBase module : vehicle.getModules()) {
				if (module.hasGui()) {
					if (module.hasSlots()) {
						ArrayList<SlotBase> slotsList = module.getSlots();
						for (SlotBase slot : slotsList) {
							resetSlot(slot);
						}
					}
				}
			}
		
		}else if (vehicle.getModules() != null) {
            int targetY = 0;

            drawTexturedModalRect(j + scrollBox[0], k + scrollBox[1], 223, 37, scrollBox[2], 13);
            targetY += 13;
            for (int i = 0; i < 4; i++) {
                drawTexturedModalRect(j + scrollBox[0], k + scrollBox[1] + targetY, 223, 51, scrollBox[2], 50);
                targetY += 50;
            }
            drawTexturedModalRect(j + scrollBox[0], k + scrollBox[1] + targetY, 223, 102, scrollBox[2], 13);


			drawTexturedModalRect(j + scrollBox[0] + 2, k + scrollBox[1] + 2 + vehicle.getScrollY(), 223, 116 + (vehicle.canScrollModules ? 0 : 26), 14, 25);
			
								
			for (ModuleBase module : vehicle.getModules()) {
				drawModuleSlots(module);
			}

			//loop again since these function calls will probably bind other textures
			for (ModuleBase module : vehicle.getModules()) {
				drawModuleBackground(module, x, y);
			}
			
			renderModuleList(x, y);
			
		}
		
		

		GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    public static ResourceLocation moduleTexture = ResourceHelper.getResourceFromPath("/atlas/items.png");
    
    private void renderModuleList(int x, int y) {
    	x -= getGuiLeft();
    	y -= getGuiTop();
    	
    	ArrayList<ModuleCountPair> moduleCounts = vehicle.getModuleCounts();
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
    	
    	ArrayList<ModuleCountPair> moduleCounts = vehicle.getModuleCounts();
    	GL11.glColor4f(1F, 1F, 1F, 1F);
    	
    	getFontRenderer().drawString(vehicle.getVehicleName(), 5, 172, 0x404040);
    	
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
    	
    	ArrayList<ModuleCountPair> moduleCounts = vehicle.getModuleCounts();
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
	public void mouseClick(int x, int y, int button) {
        super.mouseClick(x, y, button);


		ModuleBase thief = vehicle.getInterfaceThief();
		
		if (thief != null) {
			handleModuleMouseClicked(thief, x, y, button);				
		}else if (vehicle.getModules() != null) {
			if (inRect(x-getGuiLeft(),y-getGuiTop(), scrollBox[0], scrollBox[1], scrollBox[2], scrollBox[3])) {
				isScrolling = true;
			}			
			
			for (ModuleBase module : vehicle.getModules()) {
				handleModuleMouseClicked(module, x, y, button);
			}
		}
		
    }

	protected boolean inRect(int x, int y, int x1, int y1, int sizeX, int sizeY) {
		return x >= x1 && x <= x1 + sizeX && y >= y1 && y <= y1 + sizeY;
	}	

    @Override
	public void mouseMoved(int x, int y, int button) {
        super.mouseMoved(x, y, button);
		
		if (isScrolling) {
			int temp = y-getGuiTop() - 12 - (scrollBox[1] + 2);
			if (temp < 0) {
				temp = 0;
			}else if(temp > 198) {
				temp = 198;
			}
			vehicle.setScrollY(temp);
		}
		
		if (button != -1) {
			isScrolling = false;
		}	
		
		
		if (vehicle.getModules() != null) {
			ModuleBase thief = vehicle.getInterfaceThief();
			
			if (thief != null) {
				handleModuleMouseMoved(thief, x, y, button);
			}else{
				for (ModuleBase module : vehicle.getModules()) {
					handleModuleMouseMoved(module, x, y, button);
				}
			}
		}
    }

    @Override
	public void keyPress(char character, int extraInformation) {
		super.keyPress(character,extraInformation);
		
		if (vehicle.getModules() != null) {

			ModuleBase thief = vehicle.getInterfaceThief();
			
			if (thief != null) {
				handleModuleKeyPress(thief, character, extraInformation);
			}else{
				for (ModuleBase module : vehicle.getModules()) {
					handleModuleKeyPress(module, character, extraInformation);
				}
			}
		}	
	}	
	
	
    @Override
    public boolean disableStandardKeyFunctionality() {
		if (vehicle.getModules() != null) {

			ModuleBase thief = vehicle.getInterfaceThief();
			
			if (thief != null) {
				return thief.disableStandardKeyFunctionality();
			}else{
				for (ModuleBase module : vehicle.getModules()) {
					if (module.disableStandardKeyFunctionality()) {
						return true;
					}
				}
			}
		}
		
		return false;
    }	
	


    private void drawModuleForeground(ModuleBase module) {
    	if (module.hasGui()) {
	    	module.drawForeground(this);
    	}
    }
    
    private void drawModuleMouseOver(ModuleBase module, int x, int y) {
		if (module.hasGui()) {
			module.drawMouseOver(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
		}    	
    }
    
    private void drawModuleSlots(ModuleBase module) {
		if (module.hasGui()) {
			if (module.hasSlots()) {
				ArrayList<SlotBase> slotsList = module.getSlots();

				for (SlotBase slot : slotsList) {
					int[] rect = new int[] {slot.getX() + 1, slot.getY() + 1, 16, 16};
					module.handleScroll(rect);
					boolean visible = rect[3] > 0;
					if (visible) {
						slot.xDisplayPosition = slot.getX() + module.getX() + 1;
						slot.yDisplayPosition = slot.getY() + module.getY() + 1 - vehicle.getRealScrollY();
					}else{
						resetSlot(slot);
					}
				
					module.drawImage(this, slot.getX(), slot.getY(), 223, 1, 18, 18);
					if (!visible) {
						module.drawImage(this, slot.getX()+1, slot.getY()+1,223, 20, 16, 16);
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
		}  	
    }
    
    private void drawModuleBackgroundItems(ModuleBase module, int x, int y) {
		if (module.hasGui()) {
			module. drawBackgroundItems(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY());
		}  	
    }    
    
   
    
    private void handleModuleMouseClicked(ModuleBase module, int x, int y, int button) {
		module.mouseClicked(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY(),button);
    }
    
    private void handleModuleMouseMoved(ModuleBase module, int x, int y, int button) {
    	module.mouseMovedOrUp(this,x-getGuiLeft()-module.getX(),y-getGuiTop()-module.getY(),button);    	
    }
    
    private void handleModuleKeyPress(ModuleBase module, char character, int extraInformation) {
    	module.keyPress(this, character, extraInformation);    	
    }

    private static final int[] SCROLLABLE_AREA = {5, 4, 438, 164};

    @Override
    protected void renderSlots(int x, int y) {
        ModuleBase thief = vehicle.getInterfaceThief();

        if (thief != null) {
            drawModuleBackgroundItems(thief, x, y);
        }else if(vehicle.getModules() != null) {
            for (ModuleBase moduleBase : vehicle.getModules()) {
                drawModuleBackgroundItems(moduleBase, x, y);
            }
        }

        setupScissor(SCROLLABLE_AREA);
        super.renderSlots(x, y);
    }

    private boolean shouldScissorSlot(Slot slot) {
        return slot instanceof SlotBase;
    }

    @Override
    protected void renderSlot(Slot slot, ItemStack slotItem, boolean shouldSlotBeRendered, boolean shouldSlotItemBeRendered, boolean shouldSlotUnderlayBeRendered, boolean shouldSlotOverlayBeRendered, String info) {
        if (shouldScissorSlot(slot)) {
            startScissor();
        }
        boolean render = true;
        if (slot instanceof ISpecialSlotRender) {
            ISpecialSlotRender special = (ISpecialSlotRender)slot;
            slotItem = special.getStackToRender(slotItem);
            if (!special.renderSlot(slotItem, shouldSlotBeRendered, shouldSlotItemBeRendered, shouldSlotUnderlayBeRendered, shouldSlotOverlayBeRendered, info)) {
                render = false;
            }
        }
        if (render) {
            super.renderSlot(slot, slotItem, shouldSlotBeRendered, shouldSlotItemBeRendered, shouldSlotUnderlayBeRendered, shouldSlotOverlayBeRendered, info);
        }
        if (shouldScissorSlot(slot)) {
            stopScissor();
        }
    }

    @Override
    protected boolean isMouseOverSlot(Slot slot, int mX, int mY) {
        return (!shouldScissorSlot(slot) || inRect(mX - guiLeft, mY - guiTop, SCROLLABLE_AREA)) && super.isMouseOverSlot(slot, mX, mY);
    }

    public void setupAndStartScissor() {
        setupScissor(SCROLLABLE_AREA);
        startScissor();
    }
    @Override
    public void stopScissor() {
        super.stopScissor();
    }
}
