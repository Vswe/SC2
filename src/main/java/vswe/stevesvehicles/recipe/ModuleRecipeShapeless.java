package vswe.stevesvehicles.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.recipe.item.RecipeItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ModuleRecipeShapeless extends ModuleRecipe {
    public ModuleRecipeShapeless(ModuleData result, Object[] recipe) {
        super(result, recipe);
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        List<RecipeItem> remainingRecipe = new ArrayList<RecipeItem>();
        Collections.addAll(remainingRecipe, recipe);

        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                ItemStack item = crafting.getStackInRowAndColumn(i, j);
                if (item != null) {
                    boolean foundMatch = false;
                    for (RecipeItem recipeItem : remainingRecipe) {
                        if (recipeItem.matches(item)) {
                            remainingRecipe.remove(recipeItem);
                            foundMatch = true;
                            break;
                        }
                    }

                    if (!foundMatch) {
                        return false;
                    }
                }
            }
        }

        return remainingRecipe.isEmpty();
    }
}
