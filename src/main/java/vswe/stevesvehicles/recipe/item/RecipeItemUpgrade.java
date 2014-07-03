package vswe.stevesvehicles.recipe.item;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.upgrade.Upgrade;


public class RecipeItemUpgrade extends RecipeItemStackBase {
    private Upgrade upgrade;

    public RecipeItemUpgrade(Upgrade upgrade) {
        this.upgrade = upgrade;
    }

    @Override
    protected ItemStack getItemStack() {
        return upgrade.getItemStack();
    }
}
