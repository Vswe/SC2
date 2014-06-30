package vswe.stevesvehicles.recipe.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


public abstract class RecipeItemStackBase extends RecipeItem {
    protected abstract ItemStack getItemStack();

    @Override
    public boolean matches(ItemStack other) {
        ItemStack item = getItemStack();
        return other != null && item.getItem() == other.getItem() && (item.getItemDamage() == OreDictionary.WILDCARD_VALUE || item.getItemDamage() == other.getItemDamage());
    }
}
