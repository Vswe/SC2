package vswe.stevesvehicles.recipe.item;


import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;

public class RecipeItemComponent extends RecipeItemStackBase {

    private ComponentTypes type;

    public RecipeItemComponent(ComponentTypes type) {
        this.type = type;
    }

    @Override
    protected ItemStack getItemStack() {
        return type.getItemStack();
    }
}
