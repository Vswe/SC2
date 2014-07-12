package vswe.stevesvehicles.recipe.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.client.gui.screen.GuiCartAssembler;
import vswe.stevesvehicles.container.slots.SlotAssemblerFuel;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Helpers.TitleBox;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public abstract class RecipeHandlerVehicleBase extends TemplateRecipeHandler {
    protected abstract class CachedVehicleRecipeBase extends CachedRecipe {
        protected List<PositionedStack> ingredients;
        private ModuleTypeRow[] rows;
        private int assemblyTime;
        private int coalAmount;
        private int modularCost;

        public ModuleTypeRow[] getRows() {
            return rows;
        }

        public int getAssemblyTime() {
            return assemblyTime;
        }

        public int getCoalAmount() {
            return coalAmount;
        }

        public int getModularCost() {
            return modularCost;
        }

        public CachedVehicleRecipeBase() {
            ingredients = new ArrayList<PositionedStack>();

        }

        protected void loadVehicleStats(List<ItemStack> items) {
            if (items != null) {
                modularCost = 0;
                assemblyTime = TileEntityCartAssembler.FLAT_VEHICLE_BASE_TIME;
                for (ItemStack item : items) {
                    ModuleData module = ModItems.modules.getModuleData(item);
                    if (module != null) {
                        modularCost += module.getCost();
                        assemblyTime += TileEntityCartAssembler.getAssemblingTime(module);
                    }
                }
                coalAmount = (int)Math.ceil(assemblyTime / (TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal)) * SlotAssemblerFuel.FUEL_MULTIPLIER));
            }else {
                assemblyTime = 0;
                modularCost = 0;
                coalAmount = 0;
            }
        }

        protected void initHull(ModuleDataHull hull) {
            rows = new ModuleTypeRow[] {
                    new ModuleTypeRow(ModuleType.ENGINE, TileEntityCartAssembler.ENGINE_BOX, hull != null ? hull.getEngineMaxCount() : TileEntityCartAssembler.MAX_ENGINE_SLOTS, TileEntityCartAssembler.MAX_ENGINE_SLOTS),
                    new ModuleTypeRow(ModuleType.TOOL, TileEntityCartAssembler.TOOL_BOX, TileEntityCartAssembler.MAX_TOOL_SLOTS, TileEntityCartAssembler.MAX_TOOL_SLOTS),
                    new ModuleTypeRow(ModuleType.ATTACHMENT, TileEntityCartAssembler.ATTACH_BOX, TileEntityCartAssembler.MAX_ATTACHMENT_SLOTS, TileEntityCartAssembler.MAX_ATTACHMENT_SLOTS),
                    new ModuleTypeRow(ModuleType.STORAGE, TileEntityCartAssembler.STORAGE_BOX, hull != null ? hull.getStorageMaxCount() : TileEntityCartAssembler.MAX_STORAGE_SLOTS, TileEntityCartAssembler.MAX_STORAGE_SLOTS),
                    new ModuleTypeRow(ModuleType.ADDON, TileEntityCartAssembler.ADDON_BOX, hull != null ? hull.getAddonMaxCount() : TileEntityCartAssembler.MAX_ADDON_SLOTS, TileEntityCartAssembler.MAX_ADDON_SLOTS)
            };
        }

        protected void addModuleItem(ModuleData module, ItemStack item) {
            if (module instanceof ModuleDataHull) {
                ingredients.add(new PositionedStack(item, HULL_X + BIG_SLOT_OFFSET, HULL_Y + BIG_SLOT_OFFSET));
            }else{
                for (int i = 0; i < rows.length; i++) {
                    ModuleTypeRow row = rows[i];
                    if (row.type.getClazz().isAssignableFrom(module.getModuleClass())) {
                        if (row.length < row.availableLength) {
                            int id = row.length;
                            int x = id % 6;
                            int y = id / 6;
                            ingredients.add(new PositionedStack(item, ROW_X + x * GuiCartAssembler.SLOT_SIZE + 1, ROW_Y + i * ROW_OFFSET_Y + y * GuiCartAssembler.SLOT_SIZE + 1));
                            row.length++;
                        }
                        break;
                    }
                }
            }

        }

        protected abstract boolean isValid();

    }

    protected class ModuleTypeRow {
        private int length;
        private int availableLength;
        private int maxLength;
        private ModuleType type;
        private TitleBox box;

        private ModuleTypeRow(ModuleType type, TitleBox box, int availableLength, int maxLength) {
            this.box = box;
            this.type = type;
            this.availableLength = availableLength;
            this.maxLength = maxLength;
        }
    }

    protected RecipeHandlerVehicleBase() {

    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    protected static final int DISPLAY_WIDTH = 176;
    protected static final int DISPLAY_HEIGHT = 166;

    private static final int ITEM_SIZE = 16;
    protected static final int BIG_SLOT_OFFSET = (GuiCartAssembler.BIG_SLOT_SIZE - ITEM_SIZE) / 2;
    private static final int HULL_X = 140;
    private static final int HULL_Y = 4;
    protected static final int RESULT_X = 140;
    protected static final int RESULT_Y = 140;

    private static final int TITLE_X = -1;
    private static final int TITLE_Y = -10;
    private static final int ROW_X = 1;
    private static final int ROW_Y = 12;
    private static final int ROW_OFFSET_Y = 29;

    private static final int COST_X = 120;
    private static final int COST_Y = 39;

    private static final int ASSEMBLY_X = 120;
    private static final int ASSEMBLY_Y = 69;

    private static final int FUEL_X = 120;
    private static final int FUEL_Y = 99;
    private static final int COAL_X = 146;
    private static final int COAL_Y = 101;

    @Override
    public String getRecipeName() {
        return "SV Vehicle Recipe";
    }

    private RenderItem renderItem = new RenderItem();
    private void drawItem(ItemStack item, int x, int y) {
        renderItem.zLevel = 100;

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        renderItem.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, item, x, y);

        renderItem.zLevel = 0;
    }

    @Override
    public void drawBackground(int id) {
        CachedVehicleRecipeBase recipe = (CachedVehicleRecipeBase)arecipes.get(id);
        GL11.glColor4f(1, 1, 1, 1);

        drawExtraBackground();

        ResourceHelper.bindResource(GuiCartAssembler.TEXTURE_EXTRA);
        drawTexturedModalRect(HULL_X, HULL_Y, GuiCartAssembler.BIG_SLOT_SRC_X, GuiCartAssembler.BIG_SLOT_SRC_Y, GuiCartAssembler.BIG_SLOT_SIZE, GuiCartAssembler.BIG_SLOT_SIZE);
        drawTexturedModalRect(RESULT_X, RESULT_Y, GuiCartAssembler.BIG_SLOT_SRC_X, GuiCartAssembler.BIG_SLOT_SRC_Y, GuiCartAssembler.BIG_SLOT_SIZE, GuiCartAssembler.BIG_SLOT_SIZE);

        ModuleTypeRow[] rows = recipe.getRows();
        for (int i = 0; i < rows.length; i++) {
            ModuleTypeRow row = rows[i];

            int baseY = ROW_Y + i * ROW_OFFSET_Y;

            for (int j = 0; j < row.maxLength; j++) {
                int x = j % 6;
                int y = j / 6;
                int targetX = ROW_X + x * GuiCartAssembler.SLOT_SIZE;
                int targetY = baseY + y * GuiCartAssembler.SLOT_SIZE;
                drawTexturedModalRect(targetX, targetY, GuiCartAssembler.SLOT_SRC_X, GuiCartAssembler.SLOT_SRC_Y, GuiCartAssembler.SLOT_SIZE, GuiCartAssembler.SLOT_SIZE);
                if (j >= row.availableLength) {
                    drawTexturedModalRect(targetX + 1, targetY, GuiCartAssembler.SLOT_DOOR_SRC_X, GuiCartAssembler.SLOT_TOP_DOOR_SRC_Y, GuiCartAssembler.SLOT_DOOR_WIDTH, GuiCartAssembler.SLOT_DOOR_HEIGHT);
                    drawTexturedModalRect(targetX + 1, targetY + GuiCartAssembler.SLOT_DOOR_HEIGHT, GuiCartAssembler.SLOT_DOOR_SRC_X, GuiCartAssembler.SLOT_BOT_DOOR_SRC_Y, GuiCartAssembler.SLOT_DOOR_WIDTH, GuiCartAssembler.SLOT_DOOR_HEIGHT);
                }
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 500);
            drawTexturedModalRect(ROW_X + TITLE_X, baseY + TITLE_Y, GuiCartAssembler.TITLE_BOX_SRC_X, GuiCartAssembler.TITLE_BOX_SRC_Y, GuiCartAssembler.TITLE_BOX_WIDTH, GuiCartAssembler.TITLE_BOX_HEIGHT);
            GL11.glPopMatrix();
        }


        drawItem(new ItemStack(Items.coal), COAL_X, COAL_Y);

        disableTextRendering();
    }

    @Override
    public void drawForeground(int id) {
        enableTextRendering();
        repairRemovedTitle();

        CachedVehicleRecipeBase recipe = (CachedVehicleRecipeBase)arecipes.get(id);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        ResourceHelper.bindResource(GuiCartAssembler.TEXTURE_EXTRA);


        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        ModuleTypeRow[] rows = recipe.getRows();
        for (int i = 0; i < rows.length; i++) {
            ModuleTypeRow row = rows[i];

            int targetX = ROW_X + TITLE_X + GuiCartAssembler.TITLE_BOX_TEXT_OFFSET_X;
            int targetY = ROW_Y + i * ROW_OFFSET_Y + TITLE_Y + GuiCartAssembler.TITLE_BOX_TEXT_OFFSET_Y;
            fontRenderer.drawString(row.box.getName().translate().toUpperCase(), targetX, targetY, row.box.getColor());
        }

        fontRenderer.drawString("Capacity", COST_X, COST_Y, 0x404040); //TODO localize
        fontRenderer.drawString(String.valueOf(recipe.getModularCost()), COST_X, COST_Y + fontRenderer.FONT_HEIGHT, 0x404040);

        fontRenderer.drawString("Time", ASSEMBLY_X, ASSEMBLY_Y, 0x404040); //TODO localize
        fontRenderer.drawString(GuiCartAssembler.formatTime(recipe.getAssemblyTime()), ASSEMBLY_X, ASSEMBLY_Y + fontRenderer.FONT_HEIGHT, 0x404040);

        fontRenderer.drawString("Fuel", FUEL_X, FUEL_Y, 0x404040); //TODO localize
        String str = String.valueOf(recipe.getCoalAmount());
        fontRenderer.drawString(str, COAL_X - fontRenderer.getStringWidth(str) - 1, FUEL_Y + fontRenderer.FONT_HEIGHT, 0x404040);
    }


    private static final int TEXTURE_SIZE = 256;
    @SideOnly(Side.CLIENT)
    public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h) {
        float multiplierX = 1F / TEXTURE_SIZE;
        float multiplierY = 1F / TEXTURE_SIZE;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(    x,      y + h,  0,  u * multiplierX,        (v + h) * multiplierY   );
        tessellator.addVertexWithUV(    x + w,  y + h,  0,  (u + w) * multiplierX,  (v + h) * multiplierY   );
        tessellator.addVertexWithUV(    x + w,  y,      0,  (u + w) * multiplierX,  v * multiplierY         );
        tessellator.addVertexWithUV(    x,      y,      0,  u * multiplierX,        v * multiplierY         );
        tessellator.draw();
    }





    @Override
    public String getGuiTexture() {
        return null;
    }



    /*
        ============ METHODS TO GET RID OF THE PAGE TEXT ===========
     */

    private static final ResourceLocation EMPTY_RESOURCE = ResourceHelper.getResource("/gui/blank.png");
    private boolean oldUnicodeFlag;
    private void disableTextRendering() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        oldUnicodeFlag = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        ResourceLocation[] resources = getUnicodeResourceList(fontRenderer);
        for (int i = 0; i < resources.length; i++) {
            resources[i] = EMPTY_RESOURCE;
        }
    }

    private void enableTextRendering() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.setUnicodeFlag(oldUnicodeFlag);
        ResourceLocation[] resources = getUnicodeResourceList(fontRenderer);
        for (int i = 0; i < resources.length; i++) {
            resources[i] = null;
        }
    }

    private ResourceLocation[] getUnicodeResourceList(FontRenderer fontRenderer) {
        return ReflectionHelper.getPrivateValue(FontRenderer.class, fontRenderer, 0);
    }

    //all text was removed to remove the page text, manually redraw the missing title (with arrows)
    protected void repairRemovedTitle() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        String str = getRecipeName();
        fontRenderer.drawString(str, (DISPLAY_WIDTH - fontRenderer.getStringWidth(str)) / 2 - TRANSLATE_X, - 11, 0x404040);



        Point mouse = getMouse();
        drawArrows(mouse.x, mouse.y);

    }

    protected Point getMouse() {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int w = scaledresolution.getScaledWidth();
        int h = scaledresolution.getScaledHeight();
        int mX = Mouse.getX() * w / Minecraft.getMinecraft().displayWidth;
        int mY = h - Mouse.getY() * h / Minecraft.getMinecraft().displayHeight - 1;

        mX -= (w - DISPLAY_WIDTH) / 2;
        mY -= (h - DISPLAY_HEIGHT) / 2;

        mX -= TRANSLATE_X;
        mY -= TRANSLATE_Y;

        return new Point(mX, mY);
    }

    protected void drawArrows(int mX, int mY) {
        if (hasButtons()) {
            drawArrow("<", ARROW_LEFT_X, ARROW_TOP_Y, ARROW_WIDTH, ARROW_HEIGHT, mX, mY);
            drawArrow(">", ARROW_RIGHT_X, ARROW_TOP_Y, ARROW_WIDTH, ARROW_HEIGHT, mX, mY);
        }
    }

    protected abstract boolean hasButtons();

    protected static final int TRANSLATE_X = 5;
    protected static final int TRANSLATE_Y = 16;

    protected static final int ARROW_LEFT_X = 13;
    protected static final int ARROW_RIGHT_X = 140;
    protected static final int ARROW_TOP_Y = 3 - TRANSLATE_Y;
    protected static final int ARROW_WIDTH = 13;
    protected static final int ARROW_HEIGHT = 12;



    protected void drawArrow(String str, int x, int y, int w, int h, int mX, int mY) {
        int color = inRect(x, y, w, h, mX, mY) ? 0xFFFFA0 : 0xE0E0E0;
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawStringWithShadow(str, x + (w - fontRenderer.getStringWidth(str)) / 2, y + (h - 8) / 2, color);
    }

    protected boolean inRect(int x, int y, int w, int h, int mX, int mY) {
        return x <= mX && mX < x + w && y <= mY && mY < y + h;
    }

    protected static final int EXTRA_HEIGHT_OVERLAP = 5;
    private static final int EXTRA_HEIGHT = 25;

    protected int getBackgroundExtraHeight() {
        return EXTRA_HEIGHT;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final ResourceLocation TEXTURE_BACKGROUND = new ResourceLocation("nei:textures/gui/recipebg.png");
    private void drawExtraBackground() {
        ResourceHelper.bindResource(TEXTURE_BACKGROUND);
        int extraHeight = getBackgroundExtraHeight();
        drawTexturedModalRect(-TRANSLATE_X, -TRANSLATE_Y + DISPLAY_HEIGHT - EXTRA_HEIGHT_OVERLAP, 0, DISPLAY_HEIGHT - extraHeight, DISPLAY_WIDTH, extraHeight);
    }

}
