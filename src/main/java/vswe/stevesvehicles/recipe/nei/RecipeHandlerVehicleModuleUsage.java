package vswe.stevesvehicles.recipe.nei;


import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.recipe.ModuleRecipe;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;
import vswe.stevesvehicles.recipe.item.RecipeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeHandlerVehicleModuleUsage extends RecipeHandlerVehicleBase {
    private class CachedVehicleRecipeModule extends CachedVehicleRecipeBase {
        private ModuleData ingredientData;
        private ItemStack ingredient;
        private boolean valid;
        private PositionedStack result;
        private int lastSeed;
        private List<ModuleDataHull> hulls = new ArrayList<ModuleDataHull>();

        public CachedVehicleRecipeModule(ItemStack ingredient) {
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
                                if (!canModuleBeAdded(addedModules, moduleData)) {
                                    modules.remove(moduleData);
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
                arecipes.remove(this);
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

    private boolean others = false;

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient != null && ingredient.getItem() == ModItems.modules) {
            CachedVehicleRecipeBase cache = new CachedVehicleRecipeModule(ingredient);
            if (cache.isValid()) {
                arecipes.add(cache);

                lookForOthers(ingredient);
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
}
