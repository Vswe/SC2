package vswe.stevesvehicles.recipe.item;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleData;


public class RecipeItemModule extends RecipeItemStackBase {
    private ModuleData module;

    public RecipeItemModule(ModuleData module) {
        this.module = module;
    }

    @Override
    protected ItemStack getItemStack() {
        return module.getItemStack();
    }
}
