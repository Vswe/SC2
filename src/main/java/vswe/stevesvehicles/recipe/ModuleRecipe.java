package vswe.stevesvehicles.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.recipe.item.RecipeItem;


//TODO need a NEI addon to show up in NEI

public abstract class ModuleRecipe implements IRecipe {

    private ModuleData result;
    protected RecipeItem[] recipe;

    protected static final int GRID_WIDTH = 3;
    protected static final int GRID_HEIGHT = 3;

    public ModuleRecipe(ModuleData result, Object[] recipe) {
        this.result = result;
        this.recipe = new RecipeItem[recipe.length];
        for (int i = 0; i < recipe.length; i++) {
            this.recipe[i] = RecipeItem.createRecipeItem(recipe[i]);
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return result.getItemStack();
    }

    @Override
    public int getRecipeSize() {
        return recipe.length;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.getItemStack();
    }
}
