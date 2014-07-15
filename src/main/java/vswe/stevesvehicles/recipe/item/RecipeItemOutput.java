package vswe.stevesvehicles.recipe.item;


import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.recipe.IRecipeOutput;

public class RecipeItemOutput extends RecipeItemStackBase {

    private IRecipeOutput type;

    public RecipeItemOutput(IRecipeOutput type) {
        this.type = type;
    }

    @Override
    protected ItemStack getItemStack() {
        return type.getItemStack();
    }
}
