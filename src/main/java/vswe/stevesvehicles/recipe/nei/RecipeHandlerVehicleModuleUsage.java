package vswe.stevesvehicles.recipe.nei;


import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.recipe.ModuleRecipe;
import vswe.stevesvehicles.recipe.item.RecipeItem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RecipeHandlerVehicleModuleUsage extends RecipeHandlerVehicleBase {

    private class CachedVehicleRecipeModuleWrapper extends CachedVehicleRecipeBase {
        private List<CachedVehicleRecipeModule> recipes = new ArrayList<CachedVehicleRecipeModule>();

        public CachedVehicleRecipeModuleWrapper(ItemStack ingredient) {
            CachedVehicleRecipeModule recipe = new CachedVehicleRecipeModule(this, ingredient);
            if (recipe.isValid()) {
                recipes.add(recipe);
            }
            currentRecipe = 0;
            lookForOthers(ingredient);
            hasMultipleRecipes = false;
        }

        public CachedVehicleRecipeModuleWrapper() {
            for (ModuleData moduleData : ModuleRegistry.getAllModules()) {
                if (moduleData.getIsValid()) {
                    CachedVehicleRecipeModule recipe = new CachedVehicleRecipeModule(this, moduleData.getItemStack());
                    if (recipe.isValid()) {
                        recipes.add(recipe);
                    }
                }
            }
            currentRecipe = 0;
            others = false;
            hasMultipleRecipes = true;
        }


        private void removeRecipe(CachedVehicleRecipeModule recipe) {
            recipes.remove(recipe);
            if (recipes.isEmpty()) {
                arecipes.remove(this);
            }
        }

        @Override
        public ModuleTypeRow[] getRows() {
            CachedVehicleRecipeModule recipe = getRecipe();
            return recipe != null ? recipe.getRows() : super.getRows();
        }

        @Override
        public int getAssemblyTime() {
            CachedVehicleRecipeModule recipe = getRecipe();
            return recipe != null ? recipe.getAssemblyTime() : super.getAssemblyTime();
        }

        @Override
        public int getCoalAmount() {
            CachedVehicleRecipeModule recipe = getRecipe();
            return recipe != null ? recipe.getCoalAmount() : super.getCoalAmount();
        }

        @Override
        public int getModularCost() {
            CachedVehicleRecipeModule recipe = getRecipe();
            return recipe != null ? recipe.getModularCost() : super.getModularCost();
        }

        @Override
        protected boolean isValid() {
            return !recipes.isEmpty();
        }

        @Override
        public PositionedStack getResult() {
            CachedVehicleRecipeModule recipe = getRecipe();
            return recipe != null ? recipe.getResult() : null;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            CachedVehicleRecipeModule recipe = getRecipe();
            return recipe != null ? recipe.getIngredients() : null;
        }

        private CachedVehicleRecipeModule getRecipe() {
            if (!isValid()) {
                return null;
            }else{
                if (currentRecipe < 0) {
                    currentRecipe += recipes.size();
                }else if (currentRecipe >= recipes.size()){
                    currentRecipe -= recipes.size();
                }
                return recipes.get(currentRecipe);
            }
        }
    }

    private class CachedVehicleRecipeModule extends CachedVehicleRecipeBase {
        private CachedVehicleRecipeModuleWrapper wrapper;
        private ModuleData ingredientData;
        private ItemStack ingredient;
        private boolean valid;
        private PositionedStack result;
        private int lastSeed;
        private List<ModuleDataHull> hulls = new ArrayList<ModuleDataHull>();

        public CachedVehicleRecipeModule(CachedVehicleRecipeModuleWrapper wrapper, ItemStack ingredient) {
            this.wrapper = wrapper;
            ModuleData data = ModItems.modules.getModuleData(ingredient);
            if (data != null && data.getIsValid() && !(data instanceof ModuleDataHull) && data.getValidVehicles() != null && !data.getValidVehicles().isEmpty()) {
                ingredientData = data;
                this.ingredient = ingredient;

                for (ModuleData moduleData : ModuleRegistry.getAllModules()) {
                    if (moduleData instanceof ModuleDataHull) {
                        ModuleDataHull hull = (ModuleDataHull)moduleData;
                        if (hull.getIsValid() && hull.isModuleValid(data)) {
                            hulls.add(hull);
                        }
                    }
                }
                valid = !hulls.isEmpty();
                if (valid) {
                    updateVehicleStats();
                }

            }
        }


        private void updateVehicleStats() {
            int seed = cycleticks / 20;

            if (lastSeed != seed) {
                lastSeed = seed;
                loadVehicleStats(seed);
            }
        }



        private void loadVehicleStats(int seed) {
            ingredients.clear();

            Random random = new Random(seed);
            ModuleDataHull hull = hulls.get(random.nextInt(hulls.size()));
            List<ModuleData> modules = new ArrayList<ModuleData>();
            addModuleItem(hull, hull.getItemStack());
            initHull(null);
            modules.add(hull);

            addModuleItem(ingredientData, ingredient);
            modules.add(ingredientData);

            if (ingredientData.getParent() != null) {
                if (!ingredientData.getParent().getIsValid() || !hull.isModuleValid(ingredientData.getParent())) {
                    removeHull(hull);
                    return;
                }
                addModuleItem(ingredientData.getParent(), ingredientData.getParent().getItemStack());
                modules.add(ingredientData.getParent());
            }

            if (!addRequirements(hull, ingredientData, modules, random)) {
                removeHull(hull);
                return;
            }

            List<ItemStack> items = new ArrayList<ItemStack>();
            for (PositionedStack ingredient : ingredients) {
                items.add(ingredient.items[0]);
            }
            loadVehicleStats(items);
            ItemStack vehicle = ModuleDataItemHandler.createModularVehicle(items);
            result = vehicle == null ? null : new PositionedStack(vehicle, RESULT_X + BIG_SLOT_OFFSET, RESULT_Y + BIG_SLOT_OFFSET);
        }

        private boolean addRequirements(ModuleDataHull hull, ModuleData module, List<ModuleData> addedModules, Random random) {
            if (module.getRequirement() != null) {
                for (ModuleDataGroup moduleDataGroup : module.getRequirement()) {
                    List<ModuleData> modules = new ArrayList<ModuleData>();
                    int requiredAmount = moduleDataGroup.getCount();
                    for (ModuleData moduleData : moduleDataGroup.getModules()) {
                        if (addedModules.contains(moduleData) ) {
                            requiredAmount--;
                        }

                        if (moduleData.getIsValid() && hull.isModuleValid(moduleData) && canModuleBeAdded(addedModules, moduleData)) {
                            modules.add(moduleData);
                        }
                    }


                    if (modules.isEmpty()) {
                        return requiredAmount == 0;
                    }else{
                        for (int i = 0; i < requiredAmount; i++) {
                            boolean added = false;
                            while (!added) {
                                if (modules.isEmpty()) {
                                    return false;
                                }

                                ModuleData moduleData = modules.get(random.nextInt(modules.size()));
                                if (!addRequirements(hull, moduleData, addedModules, random)) {
                                    return false;
                                }
                                addModuleItem(moduleData, moduleData.getItemStack());
                                addedModules.add(moduleData);
                                added = true;

                                for (Iterator<ModuleData> iterator = modules.iterator(); iterator.hasNext(); ) {
                                    ModuleData data = iterator.next();
                                    if (!canModuleBeAdded(addedModules, data)) {
                                        iterator.remove();
                                    }
                                }

                            }
                        }
                    }


                }
            }
            return true;
        }

        private boolean canModuleBeAdded(List<ModuleData> addedModules, ModuleData moduleData) {
            if (!moduleData.getAllowDuplicate() && addedModules.contains(moduleData)) {
                return false;
            }

            for (ModuleData addedModule : addedModules) {
                if (addedModule.getNemesis() != null && addedModule.getNemesis().contains(moduleData)) {
                    return false;
                }

                if (addedModule.getSides() != null && !addedModule.getSides().isEmpty() && moduleData.getSides() != null && !moduleData.getSides().isEmpty()  ) {
                    for (ModuleSide moduleSide : moduleData.getSides()) {
                        if (addedModule.getSides().contains(moduleSide)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        private void removeHull(ModuleDataHull hull) {
            hulls.remove(hull);
            if (hulls.size() == 0) {
                wrapper.removeRecipe(this);
            }else{
                loadVehicleStats(lastSeed);
            }
        }

        @Override
        protected boolean isValid() {
            return valid;
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            updateVehicleStats();
            return ingredients;
        }
    }

    private int currentRecipe;
    private boolean others;
    private boolean hasMultipleRecipes;

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("crafting")) { //TODO this shouldn't be "crafting", it should be its own type that can be accessed from the normal vehicle recipe
            CachedVehicleRecipeBase cache = new CachedVehicleRecipeModuleWrapper();
            if (cache.isValid()) {
                arecipes.add(cache);
            }
        }else{
            super.loadUsageRecipes(inputId, ingredients);
        }
    }


    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient != null && ingredient.getItem() == ModItems.modules) {
            CachedVehicleRecipeBase cache = new CachedVehicleRecipeModuleWrapper(ingredient);
            if (cache.isValid()) {
                arecipes.add(cache);
            }
        }
    }

    private void lookForOthers(ItemStack ingredient) {
        for (Object obj : CraftingManager.getInstance().getRecipeList()) {
            if (obj instanceof ModuleRecipe) {
                ModuleRecipe recipe = (ModuleRecipe)obj;

                for (RecipeItem item : recipe.getRecipeItems()) {
                    List<ItemStack> items = item.getVisualStacks();
                    if (items != null) {
                        for (ItemStack itemStack : items) {
                            if (itemStack != null && ingredient.getItem() == itemStack.getItem() && ingredient.getItemDamage() == itemStack.getItemDamage()) {
                                others = true;
                                return;
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    protected boolean hasButtons() {
        return others;
    }

    private static final int EXTRA_HEIGHT = 15;

    @Override
    protected int getBackgroundExtraHeight() {
        return super.getBackgroundExtraHeight() + (hasMultipleRecipes ? EXTRA_HEIGHT : 0);
    }


    private CachedVehicleRecipeModuleWrapper getWrapper() {
        return (CachedVehicleRecipeModuleWrapper)arecipes.get(0);
    }

    /*
        ============ METHODS TO GET RID OF THE PAGE TEXT ===========
    */

    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    @Override
    protected void repairRemovedTitle() {
        super.repairRemovedTitle();

        if (hasMultipleRecipes) {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            String str = NEIClientUtils.translate("recipe.page", currentRecipe + 1, getWrapper().recipes.size());
            fontRenderer.drawString(str, (DISPLAY_WIDTH - fontRenderer.getStringWidth(str)) / 2 - TRANSLATE_X, getHeight() - 16, 0x404040);
        }

    }

    private int getHeight() {
        return DISPLAY_HEIGHT + getBackgroundExtraHeight() - EXTRA_HEIGHT_OVERLAP - TRANSLATE_Y;
    }

    @Override
    protected void drawArrows(int mX, int mY) {
        super.drawArrows(mX, mY);

        if (hasMultipleRecipes) {
            drawArrowWithBackground("<", ARROW_LEFT_X, ARROW_BOT_Y + getHeight(), ARROW_WIDTH, ARROW_HEIGHT, mX, mY);
            drawArrowWithBackground(">", ARROW_RIGHT_X, ARROW_BOT_Y + getHeight(), ARROW_WIDTH, ARROW_HEIGHT, mX, mY);
        }
    }

    private static final int ARROW_BOT_Y = -18;

    private void drawArrowWithBackground(String str, int x, int y, int w, int h, int mX, int mY) {
        GL11.glColor4f(1, 1, 1, 1);
        ResourceHelper.bindResource(BUTTON_TEXTURE);

        int texture = inRect(x, y, w, h, mX, mY) ? 1 : 0;
        drawTexturedModalRect(x,            y,              0,              66 + texture * 20,                  w / 2,      h / 2);
        drawTexturedModalRect(x + w / 2,    y,              200 - w / 2,    66 + texture * 20,                  w / 2,      h / 2);
        drawTexturedModalRect(x,            y + h / 2,      0,              66 + texture * 20 + 20 - h / 2,     w / 2,      h / 2);
        drawTexturedModalRect(x + w / 2,    y + h / 2,      200 - w / 2,    66 + texture * 20 + 20 - h / 2,     w / 2,      h / 2);

        drawArrow(str, x, y, w, h, mX, mY);
    }



    private boolean clickArrow(int x, int y, int w, int h, int mX, int mY) {
        if (super.inRect(x, y, w, h, mX, mY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        if (!super.mouseClicked(gui, button, recipe)) {
            Point mouse = getMouse();
            int mX = mouse.x;
            int mY = mouse.y;
            if(clickArrow(ARROW_LEFT_X, ARROW_BOT_Y + getHeight(), ARROW_WIDTH, ARROW_HEIGHT, mX, mY)) {
                currentRecipe--;
                return true;
            }else if (clickArrow(ARROW_RIGHT_X, ARROW_BOT_Y + getHeight(), ARROW_WIDTH, ARROW_HEIGHT, mX, mY)){
                currentRecipe++;
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
}
