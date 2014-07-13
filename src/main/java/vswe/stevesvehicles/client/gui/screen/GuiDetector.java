package vswe.stevesvehicles.client.gui.screen;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.gui.detector.DropDownMenuFlow;
import vswe.stevesvehicles.client.gui.detector.DropDownMenuModules;
import vswe.stevesvehicles.client.gui.detector.DropDownMenuStates;
import vswe.stevesvehicles.container.ContainerDetector;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.client.gui.detector.DropDownMenu;
import vswe.stevesvehicles.detector.LogicObject;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDetector extends GuiBase {
    public GuiDetector(InventoryPlayer invPlayer, TileEntityDetector detector) {
        super(new ContainerDetector(detector));
        setXSize(255);
        setYSize(202);
		this.detector = detector;


        if (!detector.mainObj.getChildren().isEmpty()) {
            detector.mainObj.getChildren().get(0).setParent(null);
        }
		detector.recalculateTree();

		menus = new ArrayList<DropDownMenu>();
		menus.add(new DropDownMenuModules(0));
		menus.add( new DropDownMenuStates(1));
		menus.add(new DropDownMenuFlow(2));
    }

	private ArrayList<DropDownMenu> menus;

    @Override
    public void drawGuiForeground(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);
	
        getFontRenderer().drawString(DetectorType.getTypeFromMeta(detector.getBlockMetadata()).getName(), 8, 6, 0x404040);

        boolean isAnyMenuDown = false;
        for (DropDownMenu menu : menus) {
            if (menu.getScroll() != 0) {
                menu.drawMouseOver(this, x, y);
                isAnyMenuDown = true;
                break;
            }
        }

        if (!isAnyMenuDown) {
			drawMouseOverFromObject(detector.mainObj, x, y);
		}

 		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	private boolean drawMouseOverFromObject(LogicObject obj, int x, int y) {
		if (drawMouseOver(obj.getName(), x, y, obj.getRect())) {
			return true;
		}
		
		for (LogicObject child : obj.getChildren()) {
			if (drawMouseOverFromObject(child, x, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean drawMouseOver(String str, int x, int y, int[] rect) {
		if (rect != null && inRect(x-getGuiLeft(),y-getGuiTop(),rect)) {
			drawMouseOver(str, x - getGuiLeft(), y - getGuiTop());
			return true;
		}	
		return false;
	}


	public static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/detector.png");
	public static final ResourceLocation MODULE_TEXTURE = ResourceHelper.getResourceFromPath("/atlas/items.png");
	public static final ResourceLocation DROP_DOWN_TEXTURE = ResourceHelper.getResource("/gui/detector2.png");

    @Override
    public void drawGuiBackground(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        ResourceHelper.bindResource(TEXTURE);
        drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, xSize, ySize);

		x -= getGuiLeft();
		y -= getGuiTop();
	
		
		detector.mainObj.draw(this, x, y);

        updateMenus(x, y);

        for (DropDownMenu menu : menus) {
            menu.drawMain(this, x, y);
        }
        for (DropDownMenu menu : menus) {
            menu.drawHeader(this);
        }

		if (currentObject != null) {
			currentObject.draw(this, -500, -500, x,y);
		}
    }



    public void updateMenus(int x, int y) {
        if (currentObject == null) {
            for (DropDownMenu menu : menus){
                if (inRect(x,y, menu.getHeaderRect())) {
                    menu.release();
                    menu.update(true);
                    for (DropDownMenu menu2 : menus) {
                        if (!menu.equals(menu2)) {
                            menu2.goUp();
                            menu2.update(false);
                        }
                    }
                    return;
                }
            }

            for (DropDownMenu menu : menus){
                menu.update(inRect(x, y, menu.getMainRect()));
            }
        }else{
            for (DropDownMenu menu : menus){
                menu.update(false);
            }
        }
    }

    @Override
    public void mouseClick(int x, int y, int button) {
        super.mouseClick(x, y, button);
		x-= getGuiLeft();
		y-= getGuiTop();

		if (button == 0) {
			if (isShiftKeyDown()) {
				if (currentObject == null) {
					pickupObject(x, y, detector.mainObj);
				}				
			}else{
				for (DropDownMenu menu : menus) {
					menu.onClick(this, x, y);
				}

			}
		}else if(button == 1) {
			if (currentObject == null) {
				removeObject(x, y, detector.mainObj);
			}		
		}
    }

    @Override
	public void mouseMoved(int x, int y, int button) {
        super.mouseMoved(x, y, button);		
		x -= getGuiLeft();
		y -= getGuiTop();	

		if (button != -1 && currentObject != null) {
			dropOnObject(x, y, detector.mainObj, currentObject);
		
			currentObject = null;		
		}
	}		
	
	private boolean removeObject(int x, int y, LogicObject object) {
		if (inRect(x,y, object.getRect()) && object.canBeRemoved()) {
			object.setParentAndUpdate(null);
		
			return true;
		}	
	
		for (LogicObject child : object.getChildren()) {
			if (removeObject(x, y, child)) {
				return true;
			}
		}
		
		return false;	
	}
	
	private boolean pickupObject(int x, int y, LogicObject object) {
		if (inRect(x,y, object.getRect()) && object.canBeRemoved()) {
			currentObject = object;
			object.setParentAndUpdate(null);
		
			return true;
		}	
	
		for (LogicObject child : object.getChildren()) {
			if (pickupObject(x, y, child)) {
				return true;
			}
		}
		
		return false;	
	}	
	
	private boolean dropOnObject(int x, int y, LogicObject object, LogicObject drop) {
		if (inRect(x,y, object.getRect())) {
			/*if (isShiftKeyDown() && drop.hasRoomForChild() && drop.isChildValid(object)) {
				if (object.getParent() != null && object.getParent().isChildValid(drop)) {
					LogicObject parent = object.getParent();
					object.setParentAndUpdate(detector, null);
					drop.setParentAndUpdate(detector, parent);
					object.setParentAndUpdate(detector, drop);
				
			}else{}*/
				if (object.hasRoomForChild() && object.isChildValid(drop)) {
					drop.setParentAndUpdate(object);
				}
			//}

			return true;
		}
		
		
		for (LogicObject child : object.getChildren()) {
			if (dropOnObject(x, y, child, drop)) {
				return true;
			}
		}
		
		return false;
	}
	
	public LogicObject currentObject;


	
	private TileEntityDetector detector;

    public TileEntityDetector getDetector() {
        return detector;
    }
}
