package vswe.stevesvehicles.old.Interfaces;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vswe.stevesvehicles.client.interfaces.GuiBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.old.Containers.ContainerCartAssembler;
import vswe.stevesvehicles.old.Helpers.DropDownMenuItem;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Helpers.TitleBox;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataHull;
import vswe.stevesvehicles.old.Slots.SlotAssembler;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCartAssembler extends GuiBase
{
    public GuiCartAssembler(InventoryPlayer invPlayer, TileEntityCartAssembler assembler)
    {
        super(new ContainerCartAssembler(invPlayer, assembler));
        this.assembler = assembler;
        this.invPlayer = invPlayer;
        setXSize(512);
        setYSize(256);
    }

	@Override
    public void drawGuiForeground(int x, int y)
    {
        getFontRenderer().drawString(Localization.GUI.ASSEMBLER.TITLE.translate() , 8, 6, 0x404040);

		if (assembler.isErrorListOutdated) {
			updateErrorList();
			assembler.isErrorListOutdated = false;
		}
		
		ArrayList<TextWithColor> lines = statusLog;
		if (lines != null) {
			int lineCount = lines.size();
			boolean dotdotdot = false;
			if (lineCount > 11) {
				lineCount = 10;
				dotdotdot = true;
			}
			for (int i = 0; i < lineCount; i++) {
				TextWithColor info = lines.get(i);
				if (info != null) {
					getFontRenderer().drawString(info.getText(), 375, 40 + i * 10, info.getColor());
				}
			}
			if (dotdotdot) {
				getFontRenderer().drawString("...", 375, 40 + lineCount * 10, 0x404040);
			}
		}

    }

	private ArrayList<TextWithColor> statusLog;
	private boolean hasErrors;
	private void updateErrorList() {
		ArrayList<TextWithColor> lines = new ArrayList<TextWithColor>();
		if (assembler.getStackInSlot(0) == null) {
			addText(lines, Localization.GUI.ASSEMBLER.ASSEMBLE_INSTRUCTION.translate());
			hasErrors = true;
		}else{
			ModuleData hulldata = ModItems.modules.getModuleData(assembler.getStackInSlot(0));
			if (hulldata == null || !(hulldata instanceof ModuleDataHull)) {
				addText(lines, Localization.GUI.ASSEMBLER.INVALID_HULL.translate() ,0x9E0B0E);
				hasErrors = true;
			}else{

				ModuleDataHull hull = (ModuleDataHull)hulldata;

				addText(lines, Localization.GUI.ASSEMBLER.HULL_CAPACITY.translate() + ": " + hull.getModularCapacity());
				addText(lines, Localization.GUI.ASSEMBLER.COMPLEXITY_CAP.translate() + ": " + hull.getComplexityMax());
				addText(lines, Localization.GUI.ASSEMBLER.TOTAL_COST.translate() + ": " + assembler.getTotalCost());
				addText(lines, Localization.GUI.ASSEMBLER.TOTAl_TIME.translate() + ": " + formatTime((int)(assembler.generateAssemblingTime() / assembler.getEfficiency())));
				addNewLine(lines);
				
				ArrayList<String> errors = assembler.getErrors();
				hasErrors = errors.size() > 0;
				if (errors.size() == 0) {
					addText(lines, Localization.GUI.ASSEMBLER.NO_ERROR.translate(), 0x005826);
				}else {
					for (String error : errors) {
						addText(lines,error, 0x9E0B0E);
					}
				}
				

			}	
				
		}
		
		statusLog = lines;	
	}

	
	
	private void addText(ArrayList<TextWithColor> lines, String text) {
		addText(lines, text, 0x404040);
	}
	
	private void addText(ArrayList<TextWithColor> lines, String text, int color) {
		List newlines = getFontRenderer().listFormattedStringToWidth(text,130);
		for (Object line : newlines) {
			
			lines.add(new TextWithColor(line.toString(), color));
		}
	}
	
	private void addNewLine(ArrayList<TextWithColor> lines) {
		lines.add(null);
	}		
	
	
	private class TextWithColor {
		private String text;
		private int color;
		
		public TextWithColor(String text, int color) {
			this.text = text;
			this.color = color;
		}
		
		public String getText() {
			return text;
		}
		
		public int getColor() {
			return color;
		}	
	}	
	
	private boolean firstLoad = true;

	private static ResourceLocation[] backgrounds;
	static {
		backgrounds = new ResourceLocation[4];
		for (int i = 0; i < backgrounds.length; i++) {
			backgrounds[i] = ResourceHelper.getResource("/gui/garageBackground" + i + ".png");
		}	
	}
	private static final ResourceLocation textureLeft = ResourceHelper.getResource("/gui/garagePart1.png");
	private static final ResourceLocation textureRight = ResourceHelper.getResource("/gui/garagePart2.png");
	private static final ResourceLocation textureExtra = ResourceHelper.getResource("/gui/garageExtra.png");
    public void drawGuiBackground(float f, int x, int y)
    {
		if (firstLoad) {
			updateErrorList();
			firstLoad = false;
		}
	
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);      
	    int j = getGuiLeft();
        int k = getGuiTop();
		ResourceHelper.bindResource(backgrounds[assembler.getSimulationInfo().getBackground()]);
       drawTexturedModalRect(j+143, k+15, 0, 0, 220, 148);
		
        ResourceHelper.bindResource(textureLeft);
        drawTexturedModalRect(j, k, 0, 0, 256, ySize);
        ResourceHelper.bindResource(textureRight);
        drawTexturedModalRect(j+256, k, 0, 0, xSize - 256, ySize);
		
		 drawTexturedModalRect(j+256, k, 0, 0, xSize - 256, ySize);
		
		 ResourceHelper.bindResource(textureExtra);
		
		ArrayList<SlotAssembler> slots = assembler.getSlots();
		for (SlotAssembler slot : slots) {
			
			int targetX = slot.getX() - 1;
			int targetY = slot.getY()  - 1;
			int srcX, srcY, size;

			
			if (slot.useLargeInterface()) {
				targetX -= 3;
				targetY -= 3;	
				size = 24;
				srcX = 0;
				srcY = 0;
			}else{
				size = 18;	
				if (slot.getStack() != null && slot.getStack().stackSize <= 0) {
					if (slot.getStack().stackSize == TileEntityCartAssembler.getRemovedSize()) {
						srcX = 140;
					}else{
						srcX = 122;
					}
					srcY = 40;				
				}else{
					srcX = 24;
					srcY = 0;				
				}
			}
			
			
			
			 drawTexturedModalRect(j+targetX, k+targetY, srcX, srcY, size, size);
			 
			 int animationTick = slot.getAnimationTick();
			 if (animationTick < 0) {
				animationTick = 0;
			 }
			if (animationTick < 8 && !slot.useLargeInterface()) {	
				drawTexturedModalRect(j+targetX+1, k+targetY+1, 0, 24 + animationTick, 16, 8 - animationTick);
				drawTexturedModalRect(j+targetX+1, k+targetY+1 + 8 + animationTick, 0, 24 + 8, 16, 8 - animationTick);
			}
			
			/*if (slot.isPartlyInvalid()) {
				this.zLevel = 400;
				drawTexturedModalRect(j+targetX, k+targetY, srcX, size, size, size);
				this.zLevel = 0;
			}*/
			
		}


		for (TitleBox box : assembler.getTitleBoxes()) {
			int targetY = box.getY() - 12;
			int targetX = box.getX();
			
			
			drawTexturedModalRect(j+targetX, k+targetY, 0, 40, 115, 11);	

			GL11.glColor4f((float)(box.getColor() >> 16) / 255.0F, (float)(box.getColor() >> 8 & 255) / 255.0F, (float)(box.getColor() & 255) / 255.0F, 1F);
			drawTexturedModalRect(j+targetX + 8, k+targetY + 2, 0, 51+box.getID()*7, 115, 7);	
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		
		boolean isDisassembling = assembler.getIsDisassembling();
		
		int srcX = 42;
		int srcY = 0;
		if (isDisassembling) {
			srcX = 158;
			srcY = 40;
		}
		
		if (hasErrors) {
			srcY += 22;
		}else if (inRect(x-j,y-k, assembleRect)) {
			srcY += 11;
		}
		
		drawTexturedModalRect(j + assembleRect[0], k + assembleRect[1], srcX, srcY, assembleRect[2], assembleRect[3]);

		
		int [] assemblingProgRect = new int[] {375, 180, 115,11};
		int [] fuelProgRect = new int[] {375, 200, 115,11};
		float assemblingProgress = 0F;
		String assemblingInfo;
		if (assembler.getIsAssembling()) {	
			assemblingProgress = assembler.getAssemblingTime() / (float)assembler.getMaxAssemblingTime();
			assemblingInfo = Localization.GUI.ASSEMBLER.ASSEMBLE_PROGRESS.translate() + ": " + formatProgress(assemblingProgress);
			assemblingInfo += "\n" + Localization.GUI.ASSEMBLER.TIME_LEFT.translate() +": " + formatTime((int)((assembler.getMaxAssemblingTime() - assembler.getAssemblingTime()) / assembler.getEfficiency()));
			
			//won't work, the client won't know about this
			/*assemblingInfo += "\n\nModules:\n";
			
			NBTTagCompound info = assembler.outputItem.getTagCompound();
			if (info != null) {
				NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
				byte [] bytes = moduleIDTag.byteArray;
				
				for (byte id : bytes) {
					ModuleData module = ModuleData.getList().get(id);
					if (module != null) {
						assemblingInfo += module.getName() + "\n";
					}
				}
						
			}*/
		}else{
			assemblingInfo = Localization.GUI.ASSEMBLER.IDLE_MESSAGE.translate();
		}
		drawProgressBar(assemblingProgRect, assemblingProgress,22,x,y);
		
		drawProgressBar(fuelProgRect, assembler.getFuelLevel() / (float)assembler.getMaxFuelLevel(),31,x,y);
		
		
		renderDropDownMenu(x,y);
		render3DCart();

		if (!hasErrors) {
			if (isDisassembling) {
				drawProgressBarInfo(assembleRect, x, y, Localization.GUI.ASSEMBLER.MODIFY_CART.translate());
			}else{
				drawProgressBarInfo(assembleRect, x, y,  Localization.GUI.ASSEMBLER.ASSEMBLE_CART.translate());
			}
		}
		drawProgressBarInfo(assemblingProgRect, x, y, assemblingInfo);
		drawProgressBarInfo(fuelProgRect, x, y, Localization.GUI.ASSEMBLER.FUEL_LEVEL.translate() + ": " + assembler.getFuelLevel() + "/" + assembler.getMaxFuelLevel());
    }

	private String formatProgress(float progress) {
		float percentage = ((int)(progress * 10000)) / 100F;
		
		return String.format("%05.2f%%", percentage); // Tahg's the man
	}
	
	private String formatTime(int ticks) {
		int seconds = ticks / 20;
		ticks -= seconds * 20;
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		int hours = minutes / 60;
		minutes -= hours * 60;
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	private int [] assembleRect = new int[] {390, 160, 80,11};

	private void drawProgressBarInfo(int[] rect, int x, int y, String str) {		
		if (inRect(x-getGuiLeft(),y-getGuiTop(),rect)) {
			drawMouseOver(str, x, y);
		}
	}
	
	private void drawProgressBar(int[] rect, float progress, int barSrcY, int x, int y) {
		int j = getGuiLeft();
        int k = getGuiTop();
	
		int boxSrcY = 0;
		if (inRect(x-j,y-k,rect)) {
			boxSrcY = 11;
		}
	
		drawTexturedModalRect(j + rect[0], k + rect[1], 122, boxSrcY,rect[2],rect[3]);
		if (progress != 0F) {
			if (progress > 1F) {
				progress = 1F;
			}
			drawTexturedModalRect(j + rect[0] + 1, k + rect[1] + 1, 122, barSrcY,(int)(rect[2] * progress),rect[3] - 2);
		}
	}
	
	private void render3DCart() {
		assembler.createPlaceholder();
	
		int left = this.guiLeft;
		int top = this.guiTop;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(left + 512 / 2), (float)(top + (StevesVehicles.instance.renderSteve ? 50 : 100)), 100.0F);
		float scale = 50.0F;
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
		


		GL11.glRotatef(assembler.getRoll(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(assembler.getYaw(), 0.0F, 1.0F, 0.0F);

					
		RenderManager.instance.playerViewY = 180.0F;
		
		if (StevesVehicles.renderSteve) {
			EntityPlayer player = (EntityPlayer)net.minecraft.client.Minecraft.getMinecraft().thePlayer;
			ItemStack stack = player.getCurrentEquippedItem();
			player.setCurrentItemOrArmor(0, assembler.getCartFromModules(true));	
			float temp = player.rotationPitch;
			player.rotationPitch = (float)Math.PI / 4;	
			RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
			player.rotationPitch = temp;
			player.setCurrentItemOrArmor(0, stack);			
		}else{
			RenderManager.instance.renderEntityWithPosYaw(assembler.getPlaceholder(), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);		
		}
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		
		assembler.getPlaceholder().getVehicle().keepAlive = 0;
	}	
	
	private void renderDropDownMenu(int x, int y) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 200.0F);
	
        int j = getGuiLeft();
        int k = getGuiTop();	
	
		if (dropdownX != -1 && dropdownY != -1) {
			ArrayList<DropDownMenuItem> items = assembler.getDropDown();
			for (int i = 0; i < items.size(); i++) {
				DropDownMenuItem item = items.get(i);
			
				int rect[] = item.getRect(dropdownX, dropdownY,i );
				int [] subrect = new int[0];
				int srcX = 0;
				int srcY = item.getIsLarge() ? 113 : 93;
								
				drawTexturedModalRect(j + rect[0], k + rect[1], srcX, srcY, rect[2], rect[3]);
				
				if (item.getIsLarge()) {
					drawString(item.getName(), j + rect[0] + 55, k + rect[1] + 7);
				}
				
				drawTexturedModalRect(j + rect[0] + 34, k + rect[1] + 2, (item.getImageID() % 16) * 16, 179 + (item.getImageID() / 16) * 16, 16, 16);
				
				if (item.hasSubmenu()) {
					subrect = item.getSubRect(dropdownX, dropdownY,i );
					srcX = item.getIsSubMenuOpen() ? 0 : 43;
					srcY = 133;
									
					drawTexturedModalRect(j + subrect[0], k + subrect[1], srcX, srcY, subrect[2], subrect[3]);				
				}
				
				switch (item.getType()) {
					case BOOL:
						drawBooleanBox(x,y, 5 + rect[0],5 + rect[1],item.getBOOL());
						break;
					case INT:
						if (item.getIsSubMenuOpen()) {
							drawIncreamentBox(x,y, getOffSetXForSubMenuBox(0,2) + subrect[0],3 + subrect[1]);
							drawDecreamentBox(x,y, getOffSetXForSubMenuBox(1,2) + subrect[0],3 + subrect[1]);
						}
						int targetX = rect[0] + 16;
						int targetY = rect[1] + 7;
						int valueToWrite = item.getINT();
						if (valueToWrite >= 10) {
							drawDigit(valueToWrite / 10, -1,targetX, targetY);
							drawDigit(valueToWrite % 10, 1,targetX, targetY);
						}else{
							drawDigit(valueToWrite,0,targetX, targetY);
						}
						break;
					case MULTIBOOL:
						if (item.getIsSubMenuOpen()) {
							int count = item.getMULTIBOOLCount();
							for (int bool = 0; bool < count; bool++) {
								drawBooleanBox(x,y, subrect[0] + getOffSetXForSubMenuBox(bool,count),subrect[1] + 3,item.getMULTIBOOL(bool));
							}
						}
						
						break;
					default:				
				}
				
			}
		}
		
		GL11.glPopMatrix();
	}
	
	String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private void drawString(String str, int x, int y) {
		str = str.toUpperCase();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int index = validChars.indexOf(c);
			if (index != -1) {
				drawTexturedModalRect(x+7*i, y, 8*index, 165, 6, 7);
			}		
		}
	}
	
	private int getOffSetXForSubMenuBox(int id, int count) {	
		return 2 +(int)(20 + (id - count / 2F) * 10);
	}
	
	private void drawDigit(int digit, int offset, int targetX, int targetY) {		
		int srcX = digit * 8;
		int srcY = 172;
		
		targetX += offset * 4;
		drawTexturedModalRect(getGuiLeft()+targetX, getGuiTop()+targetY, srcX, srcY, 6, 7);
	}
	
	private void drawIncreamentBox(int mouseX, int mouseY, int x, int y) {
		drawStandardBox(mouseX,mouseY,x,y,10);
	}
	private void drawDecreamentBox(int mouseX, int mouseY, int x, int y) {
		drawStandardBox(mouseX,mouseY,x,y,20);
	}
	
	private void drawBooleanBox(int mouseX, int mouseY, int x, int y, boolean itemvalue) {
		drawStandardBox(mouseX,mouseY, x,y, 0);
		if (itemvalue) {
			drawTexturedModalRect(getGuiLeft() + x+2 , getGuiTop() + y+2 , 0, 159, 6, 6);
		}
		
	}
	private void drawStandardBox(int mouseX, int mouseY, int x, int y, int srcX) {
		int targetX = getGuiLeft()+x;
		int targetY = getGuiTop()+y;
	
		int srcY = 149;
		
		drawTexturedModalRect(targetX, targetY, srcX, srcY, 10, 10);
		if (inRect(mouseX,mouseY, new int[] {targetX,targetY,10,10})) {
			drawTexturedModalRect(targetX, targetY, 30, srcY, 10, 10);
		}
	}
	

	private boolean clickBox(int mouseX, int mouseY, int x, int y) {
		return inRect(mouseX,mouseY, new int[] {x,y,10,10}); 
	}
	

	@Override
	public void mouseMoved(int x0, int y0, int button)
    {
        super.mouseMoved(x0, y0, button);		
		int x = x0 - getGuiLeft();
		int y = y0 - getGuiTop();		
		
		
		if (dropdownX != -1 && dropdownY != -1) {
			ArrayList<DropDownMenuItem> items = assembler.getDropDown();
			for (int i = 0; i < items.size(); i++) {
				DropDownMenuItem item = items.get(i);
			
				boolean insideSubRect = false;
				if (item.hasSubmenu()) {	
					insideSubRect = inRect(x,y, item.getSubRect(dropdownX, dropdownY,i ));
					if (!insideSubRect && item.getIsSubMenuOpen()) {
						item.setIsSubMenuOpen(false);
					}else if (insideSubRect && !item.getIsSubMenuOpen()) {
						item.setIsSubMenuOpen(true);
					}	
				}
			
				boolean insideRect = insideSubRect || inRect(x,y, item.getRect(dropdownX, dropdownY,i ));
				if (!insideRect && item.getIsLarge()) {
					item.setIsLarge(false);
				}else if (insideRect && !item.getIsLarge()) {
					item.setIsLarge(true);
				}
	

	
			}
		}
		
		if (isScrolling) {

			
			if (button != -1) {
				isScrolling = false;
				assembler.setSpinning(true);
			}else{
				assembler.setYaw(assembler.getYaw() + x - scrollingX);
				assembler.setRoll(assembler.getRoll() + y - scrollingY);
				scrollingX = x;
				scrollingY = y;
			}
		}		
	}
	
	
	private int dropdownX = -1;
	private int dropdownY = -1;
 	private int scrollingX;
	private int scrollingY;
	private boolean isScrolling;
	private int[] blackBackground = new int[] {145, 15, 222,148};
    public void mouseClick(int x0, int y0, int button)
    {
        super.mouseClick(x0, y0, button);

		int x = x0 - getGuiLeft();
		int y = y0 - getGuiTop();
		
		if (inRect(x,y, assembleRect)) {
			PacketHandler.sendPacket(0, new byte[0]);
		}else if (inRect(x,y, blackBackground)) {
			if (button == 0) {
				if (!isScrolling) {
					scrollingX = x;
					scrollingY = y;
					isScrolling = true;
					assembler.setSpinning(false);
				}
			}else if (button == 1) {
				dropdownX = x;
				dropdownY = y;
				if (dropdownY + assembler.getDropDown().size() * 20 > 164) {
					dropdownY = 164 - assembler.getDropDown().size() * 20;
				}
			}
		}else{
			ArrayList<SlotAssembler> slots = assembler.getSlots();
			for (int i = 1; i < slots.size(); i++) {
				SlotAssembler slot = slots.get(i);
				int targetX = slot.getX() - 1;
				int targetY = slot.getY()  - 1;
				int size = 18;	
					
				if (inRect(x,y, new int[] {targetX, targetY, size, size})) {
					if (slot.getStack() != null && slot.getStack().stackSize <= 0) {
						PacketHandler.sendPacket(1, new byte[] {(byte)i});
					}
				}
				
			}
		}
		
		if (button == 0) {
			if (dropdownX != -1 && dropdownY != -1) {
				boolean anyLargeItem = false;
				ArrayList<DropDownMenuItem> items = assembler.getDropDown();
				for (int i = 0; i < items.size(); i++) {
					DropDownMenuItem item = items.get(i);	
					
					if (item.getIsLarge()) {
						anyLargeItem = true;
					}else{
						continue;
					}
					
					int[] rect = item.getRect(dropdownX, dropdownY,i);
					int[] subrect = new int[0];
					
					if (item.hasSubmenu() && item.getIsSubMenuOpen()) {
						subrect = item.getSubRect(dropdownX, dropdownY,i);
					}
						
					switch (item.getType()) {			
						case BOOL:					
							if (clickBox(x,y, 5 + rect[0],5 + rect[1])) {
								item.setBOOL(!item.getBOOL());
							}
							break;
						case INT:
							if (item.getIsSubMenuOpen()) {
								if (clickBox(x,y, getOffSetXForSubMenuBox(0,2) + subrect[0],3 + subrect[1])) {
									item.setINT(item.getINT() + 1);
								}
								if (clickBox(x,y, getOffSetXForSubMenuBox(1,2) + subrect[0],3 + subrect[1])) {
									item.setINT(item.getINT() - 1);
								}
							}
							break;
						case MULTIBOOL:
							if (item.getIsSubMenuOpen()) {
								int count = item.getMULTIBOOLCount();
								for (int bool = 0; bool < count; bool++) {
									if (clickBox(x,y, subrect[0] + getOffSetXForSubMenuBox(bool, count),subrect[1] + 3)) {
										item.setMULTIBOOL(bool,!item.getMULTIBOOL(bool));
										break;
									}						
								}
							}
						
							break;
						default:				
					}	
				}
				
				if (!anyLargeItem) {
					dropdownX = dropdownY = -1;					
				}
			}	
		}
    }

    private TileEntityCartAssembler assembler;
    private InventoryPlayer invPlayer;
}
