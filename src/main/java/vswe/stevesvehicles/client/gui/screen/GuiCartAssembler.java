package vswe.stevesvehicles.client.gui.screen;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.PlainText;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.old.Containers.ContainerCartAssembler;
import vswe.stevesvehicles.old.Helpers.DropDownMenuItem;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Helpers.TitleBox;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.container.slots.SlotAssembler;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCartAssembler extends GuiBase {
    public GuiCartAssembler(InventoryPlayer invPlayer, TileEntityCartAssembler assembler) {
        super(new ContainerCartAssembler(invPlayer, assembler));
        this.assembler = assembler;
        setXSize(512);
        setYSize(256);
    }

	@Override
    public void drawGuiForeground(int x, int y) {
        getFontRenderer().drawString(LocalizationAssembler.TITLE.translate() , 8, 6, 0x404040);

		if (assembler.isErrorListOutdated) {
			updateErrorList();
			assembler.isErrorListOutdated = false;
		}
		
		ArrayList<TextWithColor> lines = statusLog;
		if (lines != null) {
			int lineCount = lines.size();
			boolean dotDotDot = false;
			if (lineCount > 11) {
				lineCount = 10;
				dotDotDot = true;
			}
			for (int i = 0; i < lineCount; i++) {
				TextWithColor info = lines.get(i);
				if (info != null) {
					getFontRenderer().drawString(info.getText(), 375, 40 + i * 10, info.getColor());
				}
			}
			if (dotDotDot) {
				getFontRenderer().drawString("...", 375, 40 + lineCount * 10, 0x404040);
			}
		}

    }

	private ArrayList<TextWithColor> statusLog;
	private boolean hasErrors;
	private void updateErrorList() {
		ArrayList<TextWithColor> lines = new ArrayList<TextWithColor>();
		if (assembler.getStackInSlot(0) == null) {
			addText(lines, LocalizationAssembler.BASIC_INSTRUCTION.translate());
			hasErrors = true;
		}else{
			ModuleData hullData = ModItems.modules.getModuleData(assembler.getStackInSlot(0));
			if (hullData == null || !(hullData instanceof ModuleDataHull)) {
				addText(lines, LocalizationAssembler.INVALID_HULL.translate() ,0x9E0B0E);
				hasErrors = true;
			}else{

				ModuleDataHull hull = (ModuleDataHull)hullData;

				addText(lines, LocalizationAssembler.HULL_CAPACITY.translate() + ": " + hull.getModularCapacity());
				addText(lines, LocalizationAssembler.COMPLEXITY_CAP.translate() + ": " + hull.getComplexityMax());
				addText(lines, LocalizationAssembler.TOTAL_COST.translate() + ": " + assembler.getTotalCost());
				addText(lines, LocalizationAssembler.TOTAL_TIME.translate() + ": " + formatTime((int)(assembler.generateAssemblingTime() / assembler.getEfficiency())));
				addNewLine(lines);
				
				ArrayList<String> errors = assembler.getErrors();
				hasErrors = errors.size() > 0;
				if (errors.size() == 0) {
					addText(lines, LocalizationAssembler.READY_MESSAGE.translate(), 0x005826);
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

	private static final ResourceLocation[] BACKGROUNDS;
	static {
		BACKGROUNDS = new ResourceLocation[4];
		for (int i = 0; i < BACKGROUNDS.length; i++) {
			BACKGROUNDS[i] = ResourceHelper.getResource("/gui/garageBackground" + i + ".png");
		}	
	}
	private static final ResourceLocation TEXTURE_LEFT = ResourceHelper.getResource("/gui/garagePart1.png");
	private static final ResourceLocation TEXTURE_RIGHT = ResourceHelper.getResource("/gui/garagePart2.png");
	private static final ResourceLocation TEXTURE_EXTRA = ResourceHelper.getResource("/gui/garageExtra.png");

    private static final int [] ASSEMBLING_PROGRESS_RECT = new int[] {375, 180, 115,11};
    private static final int [] FUEL_PROGRESS_RECT = new int[] {375, 200, 115,11};

    private static final int TEXTURE_SPACING = 1;

    private static final int BIG_SLOT_SRC_X = 1;
    private static final int BIG_SLOT_SRC_Y = 1;
    private static final int BIG_SLOT_SIZE = 24;
    private static final int MODIFIED_GREEN_SLOT_SRC_X = 1;
    private static final int MODIFIED_RED_SLOT_SRC_X = 20;
    private static final int MODIFIED_SLOT__SRC_Y = 45;
    private static final int SLOT_SRC_X = 1;
    private static final int SLOT_SRC_Y = 26;
    private static final int SLOT_SIZE = 18;

    private static final int SLOT_DOOR_WIDTH = 16;
    private static final int SLOT_DOOR_HEIGHT = 9;
    private static final int SLOT_DOOR_SRC_X = 1;
    private static final int SLOT_TOP_DOOR_SRC_Y = 63;
    private static final int SLOT_BOT_DOOR_SRC_Y = 73;

    private static final int ASSEMBLE_BUTTON_SRC_X = 26;
    private static final int ASSEMBLE_BUTTON_SRC_Y = 1;

    private static final int PROGRESS_BAR_BOX_SRC_X = 107;
    private static final int PROGRESS_BAR_BOX_SRC_Y = 1;
    private static final int PROGRESS_BAR_SRC_X = 107;
    private static final int PROGRESS_BAR_PROGRESS_SRC_Y = 25;
    private static final int PROGRESS_BAR_FUEL_SRC_Y = 35;

    private static final int TITLE_BOX_WIDTH = 115;
    private static final int TITLE_BOX_HEIGHT = 11;
    private static final int TITLE_BOX_SRC_X = 107;
    private static final int TITLE_BOX_SRC_Y = 45;

    private static final int STANDARD_BOX_SRC_X = 1;
    private static final int STANDARD_BOX_INCREASE_SRC_X = 12;
    private static final int STANDARD_BOX_DECREASE_SRC_X = 23;
    private static final int STANDARD_BOX_HOVER_SRC_X = 34;
    private static final int STANDARD_BOX_SRC_Y = 141;

    private static final int BOOLEAN_CHECK_SRC_X = 1;
    private static final int BOOLEAN_CHECK_SRC_Y = 152;

    @Override
    public void drawGuiBackground(float f, int x, int y) {
		if (firstLoad) {
			updateErrorList();
			firstLoad = false;
		}
	
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);      
	    int left = getGuiLeft();
        int top = getGuiTop();
		ResourceHelper.bindResource(BACKGROUNDS[assembler.getSimulationInfo().getBackground()]);
        drawTexturedModalRect(left + 143, top + 15, 0, 0, 220, 148);
		
        ResourceHelper.bindResource(TEXTURE_LEFT);
        drawTexturedModalRect(left, top, 0, 0, 256, ySize);
        ResourceHelper.bindResource(TEXTURE_RIGHT);
        drawTexturedModalRect(left + 256, top, 0, 0, xSize - 256, ySize);
		
		ResourceHelper.bindResource(TEXTURE_EXTRA);
		
		ArrayList<SlotAssembler> slots = assembler.getSlots();
		for (SlotAssembler slot : slots) {
			
			int targetX = slot.getX() - 1;
			int targetY = slot.getY()  - 1;
			int srcX, srcY, size;

			
			if (slot.useLargeInterface()) {
				targetX -= (BIG_SLOT_SIZE - SLOT_SIZE) / 2;
				targetY -= (BIG_SLOT_SIZE - SLOT_SIZE) / 2;
				size = BIG_SLOT_SIZE;
				srcX = BIG_SLOT_SRC_X;
				srcY = BIG_SLOT_SRC_Y;
			}else{
				size = SLOT_SIZE;
				if (slot.getStack() != null && slot.getStack().stackSize <= 0) {
					if (slot.getStack().stackSize == TileEntityCartAssembler.getRemovedSize()) {
						srcX = MODIFIED_RED_SLOT_SRC_X;
					}else{
						srcX = MODIFIED_GREEN_SLOT_SRC_X;
					}
					srcY = MODIFIED_SLOT__SRC_Y;
				}else{
					srcX = SLOT_SRC_X;
					srcY = SLOT_SRC_Y;
				}
			}



            drawTexturedModalRect(left+targetX, top+targetY, srcX, srcY, size, size);

            int animationTick = slot.getAnimationTick();
            if (animationTick < 0) {
                animationTick = 0;
            }

            if (animationTick < SLOT_DOOR_HEIGHT && !slot.useLargeInterface()) {
                int height = SLOT_DOOR_HEIGHT - animationTick;
                drawTexturedModalRect(left + targetX + 1, top + targetY, SLOT_DOOR_SRC_X, SLOT_TOP_DOOR_SRC_Y + animationTick, SLOT_DOOR_WIDTH, height);
                drawTexturedModalRect(left + targetX + 1, top + targetY + SLOT_DOOR_HEIGHT + animationTick, SLOT_DOOR_SRC_X, SLOT_BOT_DOOR_SRC_Y, SLOT_DOOR_WIDTH, height);
            }

		}


		for (TitleBox box : assembler.getTitleBoxes()) {
			int targetY = box.getY() - 12;
			int targetX = box.getX();
			
			
			drawTexturedModalRect(left + targetX, top + targetY, TITLE_BOX_SRC_X, TITLE_BOX_SRC_Y, TITLE_BOX_WIDTH, TITLE_BOX_HEIGHT);
            getFontRenderer().drawString(box.getName().translate().toUpperCase(), left + targetX + 8, top + targetY + 2, box.getColor());
            ResourceHelper.bindResource(TEXTURE_EXTRA);
			GL11.glColor4f(1F, 1F, 1F, 1F);
		}
		
		boolean isDisassembling = assembler.getIsDisassembling();
		
		int srcX = ASSEMBLE_BUTTON_SRC_X;
		int srcY = ASSEMBLE_BUTTON_SRC_Y;
		int color = 0x000000;

		if (hasErrors) {
			srcY += (ASSEMBLE_RECT[3] + TEXTURE_SPACING) * 2;
            color = 0xC9C9C9;
		}else if (inRect(x-left,y-top, ASSEMBLE_RECT)) {
			srcY += ASSEMBLE_RECT[3] + TEXTURE_SPACING;
            color = 0xF0F0F0;
		}
		
		drawTexturedModalRect(left + ASSEMBLE_RECT[0], top + ASSEMBLE_RECT[1], srcX, srcY, ASSEMBLE_RECT[2], ASSEMBLE_RECT[3]);
        ILocalizedText buttonText = isDisassembling ? new PlainText("Modify") :  new PlainText("Assemble"); //TODO localize
        String str = buttonText.translate().toUpperCase();
        int textWidth = getFontRenderer().getStringWidth(str);
        getFontRenderer().drawString(str, left + ASSEMBLE_RECT[0] + (ASSEMBLE_RECT[2] - textWidth) / 2, top + ASSEMBLE_RECT[1] + 2, color);
        GL11.glColor4f(1, 1, 1, 1);
        ResourceHelper.bindResource(TEXTURE_EXTRA);

		float assemblingProgress = 0F;
		String assemblingInfo;
		if (assembler.getIsAssembling()) {	
			assemblingProgress = assembler.getAssemblingTime() / (float)assembler.getMaxAssemblingTime();
			assemblingInfo = LocalizationAssembler.PROGRESS.translate() + ": " + formatProgress(assemblingProgress);
			assemblingInfo += "\n" + LocalizationAssembler.TIME_LEFT.translate() +": " + formatTime((int)((assembler.getMaxAssemblingTime() - assembler.getAssemblingTime()) / assembler.getEfficiency()));
			
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
			assemblingInfo = LocalizationAssembler.IDLE_MESSAGE.translate();
		}
		drawProgressBar(ASSEMBLING_PROGRESS_RECT, assemblingProgress, PROGRESS_BAR_PROGRESS_SRC_Y, x, y);
		drawProgressBar(FUEL_PROGRESS_RECT, assembler.getFuelLevel() / (float)assembler.getMaxFuelLevel(), PROGRESS_BAR_FUEL_SRC_Y, x, y);
		
		
		renderDropDownMenu(x,y);
		render3DVehicle();

		if (!hasErrors) {
			if (isDisassembling) {
				drawProgressBarInfo(ASSEMBLE_RECT, x, y, LocalizationAssembler.MODIFY_VEHICLE.translate());
			}else{
				drawProgressBarInfo(ASSEMBLE_RECT, x, y,  LocalizationAssembler.ASSEMBLE_VEHICLE.translate());
			}
		}
		drawProgressBarInfo(ASSEMBLING_PROGRESS_RECT, x, y, assemblingInfo);
		drawProgressBarInfo(FUEL_PROGRESS_RECT, x, y, LocalizationAssembler.FUEL_LEVEL.translate() + ": " + assembler.getFuelLevel() + "/" + assembler.getMaxFuelLevel());
    }

	private String formatProgress(float progress) {
		float percentage = ((int)(progress * 10000)) / 100F;
		
		return String.format("%05.2f%%", percentage); // Tahg's the man
	}
	
	private String formatTime(int ticks) {
		int seconds = ticks / 20;
		//ticks -= seconds * 20;
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		int hours = minutes / 60;
		minutes -= hours * 60;
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	private static final int [] ASSEMBLE_RECT = new int[] {390, 160, 80,11};

	private void drawProgressBarInfo(int[] rect, int x, int y, String str) {		
		if (inRect(x - getGuiLeft(),y - getGuiTop(), rect)) {
			drawMouseOver(str, x, y);
		}
	}
	
	private void drawProgressBar(int[] rect, float progress, int barSrcY, int x, int y) {
		int left = getGuiLeft();
        int top = getGuiTop();
	
		int boxSrcY = PROGRESS_BAR_BOX_SRC_Y;
		if (inRect(x - left, y - top, rect)) {
			boxSrcY += rect[3] + TEXTURE_SPACING;
		}
	
		drawTexturedModalRect(left + rect[0], top + rect[1], PROGRESS_BAR_BOX_SRC_X, boxSrcY, rect[2], rect[3]);
		if (progress != 0F) {
			if (progress > 1F) {
				progress = 1F;
			}
			drawTexturedModalRect(left + rect[0] + 1, top + rect[1] + 1, PROGRESS_BAR_SRC_X, barSrcY,(int)(rect[2] * progress),rect[3] - 2);
		}
	}
	
	private void render3DVehicle() {
		if (assembler.createPlaceholder()) {
            int left = this.guiLeft;
            int top = this.guiTop;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(left + 512 / 2), (float)(top + (StevesVehicles.renderSteve ? 50 : 100)), 100.0F);
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
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                ItemStack stack = player.getCurrentEquippedItem();
                player.setCurrentItemOrArmor(0, assembler.getCartFromModules(true));
                float temp = player.rotationPitch;
                player.rotationPitch = (float)Math.PI / 4;
                RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
                player.rotationPitch = temp;
                player.setCurrentItemOrArmor(0, stack);
            }else{
                RenderManager.instance.renderEntityWithPosYaw(assembler.getPlaceholder().getEntity(), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            }
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
	}	

    private static final int DROP_DOWN_SRC_X = 1;
    private static final int DROP_DOWN_SHORT_SRC_Y = 82;
    private static final int DROP_DOWN_LARGE_SRC_Y = 103;

    private static final int DROP_DOWN_EXTRA_OPEN_SRC_X = 1;
    private static final int DROP_DOWN_EXTRA_CLOSED_SRC_X = 44;
    private static final int DROP_DOWN_EXTRA_SRC_Y = 124;


	private void renderDropDownMenu(int x, int y) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 200.0F);
	
        int left = getGuiLeft();
        int top = getGuiTop();
	
		if (dropdownX != -1 && dropdownY != -1) {
			ArrayList<DropDownMenuItem> items = assembler.getDropDown();
			for (int i = 0; i < items.size(); i++) {
				DropDownMenuItem item = items.get(i);
			
				int rect[] = item.getRect(dropdownX, dropdownY,i );
				int [] subRect = new int[0];
				int srcX = DROP_DOWN_SRC_X;
				int srcY = item.getIsLarge() ? DROP_DOWN_LARGE_SRC_Y : DROP_DOWN_SHORT_SRC_Y;
								
				drawTexturedModalRect(left + rect[0], top + rect[1], srcX, srcY, rect[2], rect[3]);
				
				if (item.getIsLarge()) {
                    getFontRenderer().drawString(item.getName().toUpperCase(), left + rect[0] + 55, top + rect[1] + 7, 0x000000);
                    ResourceHelper.bindResource(TEXTURE_EXTRA);
                    GL11.glColor4f(1, 1, 1, 1);
				}
				
				drawTexturedModalRect(left + rect[0] + 34, top + rect[1] + 2, (item.getImageID() % 16) * 16, 179 + (item.getImageID() / 16) * 16, 16, 16);
				
				if (item.hasSubmenu()) {
					subRect = item.getSubRect(dropdownX, dropdownY,i );
					srcX = item.getIsSubMenuOpen() ? DROP_DOWN_EXTRA_OPEN_SRC_X : DROP_DOWN_EXTRA_CLOSED_SRC_X;
					srcY = DROP_DOWN_EXTRA_SRC_Y;
									
					drawTexturedModalRect(left + subRect[0], top + subRect[1], srcX, srcY, subRect[2], subRect[3]);
				}
				
				switch (item.getType()) {
					case BOOL:
						drawBooleanBox(x,y, 5 + rect[0],5 + rect[1], item.getBOOL());
						break;
					case INT:
						if (item.getIsSubMenuOpen()) {
							drawIncrementBox(x, y, getOffSetXForSubMenuBox(0, 2) + subRect[0], 3 + subRect[1]);
							drawDecrementBox(x, y, getOffSetXForSubMenuBox(1, 2) + subRect[0], 3 + subRect[1]);
						}
						int targetX = rect[0] + 16;
						int targetY = rect[1] + 7;
						int valueToWrite = item.getINT();
                        getFontRenderer().drawString(String.valueOf(valueToWrite), left + targetX + (valueToWrite >= 10 ? -4 : 0), top + targetY, 0x000000);
                        ResourceHelper.bindResource(TEXTURE_EXTRA);
                        GL11.glColor4f(1, 1, 1, 1);
						break;
					case MULTIBOOL:
						if (item.getIsSubMenuOpen()) {
							int count = item.getMULTIBOOLCount();
							for (int bool = 0; bool < count; bool++) {
								drawBooleanBox(x,y, subRect[0] + getOffSetXForSubMenuBox(bool,count),subRect[1] + 3,item.getMULTIBOOL(bool));
							}
						}
						
						break;
					default:				
				}
				
			}
		}
		
		GL11.glPopMatrix();
	}

	private int getOffSetXForSubMenuBox(int id, int count) {	
		return 2 +(int)(20 + (id - count / 2F) * 10);
	}

	private void drawIncrementBox(int mouseX, int mouseY, int x, int y) {
		drawStandardBox(mouseX,mouseY, x, y, STANDARD_BOX_INCREASE_SRC_X);
	}
	private void drawDecrementBox(int mouseX, int mouseY, int x, int y) {
		drawStandardBox(mouseX,mouseY, x, y, STANDARD_BOX_DECREASE_SRC_X);
	}


	private void drawBooleanBox(int mouseX, int mouseY, int x, int y, boolean itemValue) {
		drawStandardBox(mouseX, mouseY, x,y, STANDARD_BOX_SRC_X);
		if (itemValue) {
			drawTexturedModalRect(getGuiLeft() + x + 2 , getGuiTop() + y + 2, BOOLEAN_CHECK_SRC_X, BOOLEAN_CHECK_SRC_Y, 6, 6);
		}
	}

	private void drawStandardBox(int mouseX, int mouseY, int x, int y, int srcX) {
		int targetX = getGuiLeft()+x;
		int targetY = getGuiTop()+y;

		drawTexturedModalRect(targetX, targetY, srcX, STANDARD_BOX_SRC_Y, 10, 10);
		if (inRect(mouseX,mouseY, new int[] {targetX,targetY,10,10})) {
			drawTexturedModalRect(targetX, targetY, STANDARD_BOX_HOVER_SRC_X, STANDARD_BOX_SRC_Y, 10, 10);
		}
	}
	

	private boolean clickBox(int mouseX, int mouseY, int x, int y) {
		return inRect(mouseX, mouseY, new int[] {x,y, 10, 10});
	}
	

	@Override
	public void mouseMoved(int x0, int y0, int button) {
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
	private static final int[] BLACK_BACKGROUND = new int[] {145, 15, 222,148};
    @Override
    public void mouseClick(int x0, int y0, int button) {
        super.mouseClick(x0, y0, button);

		int x = x0 - getGuiLeft();
		int y = y0 - getGuiTop();
		
		if (inRect(x,y, ASSEMBLE_RECT)) {
            PacketHandler.sendPacket(0, new byte[0]);
		}else if (inRect(x,y, BLACK_BACKGROUND)) {
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
					int[] subRect = new int[0];
					
					if (item.hasSubmenu() && item.getIsSubMenuOpen()) {
						subRect = item.getSubRect(dropdownX, dropdownY,i);
					}
						
					switch (item.getType()) {			
						case BOOL:					
							if (clickBox(x,y, 5 + rect[0],5 + rect[1])) {
								item.setBOOL(!item.getBOOL());
							}
							break;
						case INT:
							if (item.getIsSubMenuOpen()) {
								if (clickBox(x,y, getOffSetXForSubMenuBox(0,2) + subRect[0],3 + subRect[1])) {
									item.setINT(item.getINT() + 1);
								}
								if (clickBox(x,y, getOffSetXForSubMenuBox(1,2) + subRect[0],3 + subRect[1])) {
									item.setINT(item.getINT() - 1);
								}
							}
							break;
						case MULTIBOOL:
							if (item.getIsSubMenuOpen()) {
								int count = item.getMULTIBOOLCount();
								for (int bool = 0; bool < count; bool++) {
									if (clickBox(x,y, subRect[0] + getOffSetXForSubMenuBox(bool, count),subRect[1] + 3)) {
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

}
