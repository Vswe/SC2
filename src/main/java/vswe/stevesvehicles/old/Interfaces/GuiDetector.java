package vswe.stevesvehicles.old.Interfaces;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.detector.modulestate.registry.ModuleStateRegistry;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.old.Containers.ContainerDetector;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.old.Helpers.DropDownMenu;
import vswe.stevesvehicles.old.Helpers.DropDownMenuPages;
import vswe.stevesvehicles.old.Helpers.LogicObject;
import vswe.stevesvehicles.detector.modulestate.ModuleState;
import vswe.stevesvehicles.old.Helpers.OperatorObject;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.old.TileEntities.TileEntityDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDetector extends GuiBase
{
    public GuiDetector(InventoryPlayer invPlayer, TileEntityDetector detector)
    {
        super(new ContainerDetector(invPlayer, detector));
        this.invPlayer = invPlayer;
        setXSize(255);
        setYSize(202);
		this.detector = detector;
		
		for (LogicObject child : detector.mainObj.getChilds()) {
			child.setParent(null);
			break;
		}
		detector.recalculateTree();	
		menus = new ArrayList<DropDownMenu>();
		menus.add(modulesMenu = new DropDownMenuPages(0, 2));
		menus.add(statesMenu = new DropDownMenu(1));
		menus.add(flowMenu = new DropDownMenu(2));
		
	
    }

	private ArrayList<DropDownMenu> menus;
	private DropDownMenuPages modulesMenu;
	private DropDownMenu statesMenu;
	private DropDownMenu flowMenu;
	
    public void drawGuiForeground(int x, int y)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
	
        getFontRenderer().drawString(DetectorType.getTypeFromMeta(detector.getBlockMetadata()).getName(), 8, 6, 0x404040);
		
		if (modulesMenu.getScroll() != 0) {
			int modulePosId = 0;
			for (ModuleData module : ModuleRegistry.getAllModules()) {
				if (module.getIsValid()) {
			
					int[] target = modulesMenu.getContentRect(modulePosId);			
					if (drawMouseOver(module.getName(), x, y, target)) {					
						break;
					}
					
					modulePosId++;
				}
			}	
		}else if (statesMenu.getScroll() != 0) {
			int statesPosId = 0;
			for (ModuleState state : ModuleStateRegistry.getAllStates()) {
				int[] target = statesMenu.getContentRect(statesPosId);			
				if (drawMouseOver(state.getName(), x, y, target)) {					
					break;
				}
				
				statesPosId++;
			}				
		}else if (flowMenu.getScroll() != 0) {

			int flowPosId = 0;
			for (OperatorObject operator : OperatorObject.getOperatorList(detector.getBlockMetadata())) {
				if (operator.inTab()) {
					int[] target =  flowMenu.getContentRect(flowPosId);
							
					if (drawMouseOver(operator.getName(), x, y, target)) {
						break;
					}
					
					flowPosId++;
				}
			}	
			
	
		}else{
			drawMouseOverFromObject(detector.mainObj, x, y);
		}
		
		
 		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	private boolean drawMouseOverFromObject(LogicObject obj, int x, int y) {
		if (drawMouseOver(obj.getName(), x, y, obj.getRect())) {
			return true;
		}
		
		for (LogicObject child : obj.getChilds()) {
			if (drawMouseOverFromObject(child, x, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean drawMouseOver(String str, int x, int y, int[] rect) {
		if (rect != null && inRect(x-getGuiLeft(),y-getGuiTop(),rect)) {
			drawMouseOver(str, x-getGuiLeft(), y-getGuiTop());
			return true;
		}	
		return false;
	}


	

	public static ResourceLocation texture = ResourceHelper.getResource("/gui/detector.png");
	public static ResourceLocation moduleTexture = ResourceHelper.getResourceFromPath("/atlas/items.png");
	public static ResourceLocation dropdownTexture = ResourceHelper.getResource("/gui/detector2.png");
    public void drawGuiBackground(float f, int x, int y)
    {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	

        int j = getGuiLeft();        
        int k = getGuiTop();
        ResourceHelper.bindResource(texture);
        drawTexturedModalRect(j , k, 0, 0, xSize, ySize);

		x-= getGuiLeft();
		y-= getGuiTop();		
	
		
		detector.mainObj.draw(this, x, y);

		DropDownMenu.update(this, x, y, menus);
		
		flowMenu.drawMain(this, x, y);

        ResourceHelper.bindResource(texture);
		int flowPosId = 0;
		for (OperatorObject operator : OperatorObject.getOperatorList(detector.getBlockMetadata())) {
			if (operator.inTab()) {
				int[] src = getOperatorTexture(operator.getID());
						
				flowMenu.drawContent(this, flowPosId, src[0], src[1]);
				
				flowPosId++;
			}
		}			
		
		statesMenu.drawMain(this, x, y);
		

		int statePosId = 0;
		for (ModuleState state : ModuleStateRegistry.getAllStates()) {
            if (state.getTexture() != null) {
                ResourceHelper.bindResource(state.getTexture());

                statesMenu.drawContent(this, statePosId);
            }

            statePosId++;
		}		
		
		modulesMenu.drawMain(this, x, y);

        ResourceHelper.bindResource(moduleTexture);
		int modulePosId = 0;
		for (ModuleData module : ModuleRegistry.getAllModules()) {
			if (module.getIsValid()) {	
				modulesMenu.drawContent(this,modulePosId, module.getIcon());

				modulePosId++;
			}
		}
		

		
		flowMenu.drawHeader(this);		
		statesMenu.drawHeader(this);	
		modulesMenu.drawHeader(this);	

		
		if (currentObject != null) {
			currentObject.draw(this, -500, -500, x,y);
		}	

    }



	
	public int[] getOperatorTexture(byte operatorId) {
		int x = operatorId % 11;
		int y = operatorId / 11;
	
		return new int[] {1 + x * 21, ySize + 18 + y * 12};
	}	

	public int[] getModuleTexture(byte moduleId) {
		int srcX = (moduleId % 16) * 16;
		int srcY = (moduleId / 16) * 16;
		
		return new int[] {srcX, srcY};
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

				
				int modulePosId = 0;
				for (ModuleData module : ModuleRegistry.getAllModules()) {
					if (module.getIsValid()) {
				
						int[] target = modulesMenu.getContentRect(modulePosId);			
						if (inRect(x,y, target)) {
							currentObject = new LogicObject((byte)0, (byte)ModuleRegistry.getIdFromModule(module));
						
							return;
						}
						
						modulePosId++;
					}
				}
				
				int statePosId = 0;
				for (ModuleState state : ModuleStateRegistry.getAllStates()) {
					int[] target = statesMenu.getContentRect(statePosId);			
					if (inRect(x,y, target)) {
						currentObject = new LogicObject((byte)2, (byte)ModuleStateRegistry.getIdFromState(state));
					
						return;
					}
					statePosId++;				
				}
				
				int flowPosId = 0;
				for (OperatorObject operator : OperatorObject.getOperatorList(detector.getBlockMetadata())) {
					if (operator.inTab()) {
						int[] target =  flowMenu.getContentRect(flowPosId);
								
						if (inRect(x, y, target)) {
							currentObject = new LogicObject((byte)1, operator.getID());
							
							return;
						}
						
						
						flowPosId++;
					}
				}
				
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
	public void mouseMoved(int x, int y, int button)
    {
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
			object.setParent(detector, null);
		
			return true;
		}	
	
		for (LogicObject child : object.getChilds()) {
			if (removeObject(x, y, child)) {
				return true;
			}
		}
		
		return false;	
	}
	
	private boolean pickupObject(int x, int y, LogicObject object) {
		if (inRect(x,y, object.getRect()) && object.canBeRemoved()) {
			currentObject = object;
			object.setParent(detector, null);
		
			return true;
		}	
	
		for (LogicObject child : object.getChilds()) {
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
					object.setParent(detector, null);
					drop.setParent(detector, parent);
					object.setParent(detector, drop);
				
			}else{}*/
				if (object.hasRoomForChild() && object.isChildValid(drop)) {
					drop.setParent(detector, object);
				}
			//}

			return true;
		}
		
		
		for (LogicObject child : object.getChilds()) {
			if (dropOnObject(x, y, child, drop)) {
				return true;
			}
		}
		
		return false;
	}
	
	public LogicObject currentObject;


	
	TileEntityDetector detector;
	InventoryPlayer invPlayer;
}
