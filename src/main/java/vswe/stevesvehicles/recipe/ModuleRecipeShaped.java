package vswe.stevesvehicles.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.recipe.item.RecipeItem;


public class ModuleRecipeShaped extends ModuleRecipe {
    private int width;
    private int height;
    private int recipeStartX;
    private int recipeStartY;



    public ModuleRecipeShaped(IRecipeOutput result, int width, int height, Object[] recipe) {
        super(result, recipe);

        recipeStartX = width;
        recipeStartY = height;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                RecipeItem item = this.recipe[i + j * width];
                if (!item.isEmpty()) {
                    if (i < recipeStartX) {
                        recipeStartX = i;
                    }
                    if (j < recipeStartY) {
                        recipeStartY = j;
                    }
                }
            }
        }

        this.width = width - recipeStartX;
        this.height = height - recipeStartY;
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        if (width == 0 || height == 0) return false;

        int gridStartX = GRID_WIDTH;
        int gridStartY = GRID_HEIGHT;
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                ItemStack item = crafting.getStackInRowAndColumn(i, j);
                if (item != null) {
                    if (i < gridStartX) {
                        gridStartX = i;
                    }
                    if (j < gridStartY) {
                        gridStartY = j;
                    }
                }
            }
        }

        return gridStartX != GRID_WIDTH && matches(crafting, gridStartX, gridStartY, recipeStartX, recipeStartY);
    }

    private boolean matches(InventoryCrafting crafting, int gridStartX, int gridStartY, int recipeStartX, int recipeStartY) {
        int gridWidth = GRID_WIDTH - gridStartX;
        int gridHeight = GRID_HEIGHT - gridStartY;

        if (gridWidth < width || gridHeight < height) {
            return false;
        }

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                ItemStack item = crafting.getStackInRowAndColumn(i + gridStartX, j + gridStartY);
                if (i >= width || j >= height) {
                    if (item != null) {
                        return false;
                    }
                }else if(!recipe[i + recipeStartX + (j + recipeStartY) * this.width].matches(item)) {
                    return false;
                }
            }
        }

        return true;
    }
}
