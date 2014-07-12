package vswe.stevesvehicles.recipe.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import vswe.stevesvehicles.recipe.ModuleRecipe;
import vswe.stevesvehicles.recipe.item.RecipeItem;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;


public abstract class RecipeHandler extends TemplateRecipeHandler {
    protected abstract class CachedModuleRecipe extends CachedRecipe {
        public List<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedModuleRecipe(ModuleRecipe recipe) {
            init(recipe);
            result = new PositionedStack(recipe.getRecipeOutput(), 119, 24);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients(recipe.getRecipeItems());
        }

        protected void init(ModuleRecipe recipe) {}

        protected void setIngredients(RecipeItem[] items) {
            for (int i = 0; i < items.length; i++) {
                RecipeItem item = items[i];
                List<ItemStack> stacks = item.getVisualStacks();
                if (stacks != null) {
                    PositionedStack ingredient = createIngredient(i, stacks);
                    if (ingredient != null) {
                        ingredients.add(ingredient);
                    }
                }
            }
        }

        protected PositionedStack createIngredient(int id, List<ItemStack> stacks) {
            PositionedStack stack = new PositionedStack(stacks, getX(id), getY(id), false);
            stack.setMaxSize(1);
            return stack;
        }
        protected abstract int getX(int id);
        protected abstract int getY(int id);

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        public PositionedStack getResult() {
            return result;
        }

        public void loadPermutations() {
            for (PositionedStack ingredient : ingredients) {
                ingredient.generatePermutations();
            }
        }

        public abstract boolean is2x2();
    }

    protected RecipeHandler() {

    }

    @Override
    public String getGuiTexture() {
        return "textures/gui/container/crafting_table.png"; //TODO own texture?
    }

    @Override
    public String getOverlayIdentifier() {
        return "crafting";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting")) {
            loadCraftingRecipes(null);
        }else{
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Object obj : CraftingManager.getInstance().getRecipeList()) {
            if (obj instanceof ModuleRecipe) {
                ModuleRecipe recipe = (ModuleRecipe)obj;

                ItemStack recipeResult = recipe.getRecipeOutput();
                if (result == null || (recipeResult != null && result.getItem() == recipeResult.getItem() && result.getItemDamage() == recipeResult.getItemDamage())) {
                    CachedModuleRecipe cache = createCachedRecipe(recipe);
                    if (cache != null) {
                        cache.loadPermutations();
                        arecipes.add(cache);
                    }
                }
            }
        }
    }


    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient != null) {
            for (Object obj : CraftingManager.getInstance().getRecipeList()) {
                if (obj instanceof ModuleRecipe) {
                    ModuleRecipe recipe = (ModuleRecipe)obj;

                    CachedModuleRecipe cache = createCachedRecipe(recipe);
                    if (cache != null && cache.contains(cache.ingredients, ingredient.getItem())) {
                        cache.loadPermutations();

                        if (cache.contains(cache.ingredients, ingredient)) {
                            cache.setIngredientPermutation(cache.ingredients,  ingredient);
                            arecipes.add(cache);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrafting.class;
    }

    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return super.hasOverlay(gui, container, recipe) || is2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
        if (renderer != null) {
            return renderer;
        }else{
            IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
            if (positioner == null) {
                return null;
            }else{
                return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
            }
        }
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
        if (handler != null) {
            return handler;
        }else{
            return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
        }
    }

    protected boolean is2x2(int recipe) {
        return ((CachedModuleRecipe)arecipes.get(recipe)).is2x2();
    }

    protected abstract CachedModuleRecipe createCachedRecipe(ModuleRecipe recipe);
}
