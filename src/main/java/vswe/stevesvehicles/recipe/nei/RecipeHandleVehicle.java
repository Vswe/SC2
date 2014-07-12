package vswe.stevesvehicles.recipe.nei;


import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.old.Items.ModItems;

import java.util.List;

public class RecipeHandleVehicle extends RecipeHandlerVehicleBase {
    private class CachedVehicleRecipe extends CachedVehicleRecipeBase {
        private PositionedStack result;
        private boolean valid;

        public CachedVehicleRecipe(ItemStack result) {
            this.result = new PositionedStack(result.copy(), RESULT_X + BIG_SLOT_OFFSET, RESULT_Y + BIG_SLOT_OFFSET);

            List<ModuleData> modules = ModuleDataItemHandler.getModulesFromItem(result);
            if (modules != null) {
                for (ModuleData module : modules) {
                    if (module instanceof ModuleDataHull) {
                        initHull((ModuleDataHull)module);

                        List<ItemStack> items = ModuleDataItemHandler.getModularItems(result);
                        loadVehicleStats(items);
                        if (items != null) {
                            for (ItemStack item : items) {
                                ModuleData moduleData = ModItems.modules.getModuleData(item);
                                if (moduleData != null) {
                                    addModuleItem(moduleData, item);
                                }
                            }
                            valid = true;
                        }
                    }
                }
            }

        }


        @Override
        public PositionedStack getResult() {
            return result;
        }

        @Override
        protected boolean isValid() {
            return valid;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return ingredients;
        }
    }

    public RecipeHandleVehicle() {

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result != null && result.getItem() == ModItems.carts) {
            CachedVehicleRecipeBase cache = new CachedVehicleRecipe(result);
            if (cache.isValid()) {
                arecipes.add(cache);
            }
        }
    }

    @Override
    protected boolean hasButtons() {
        return false;
    }
}
