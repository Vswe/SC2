package vswe.stevesvehicles.recipe.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;


public abstract class RecipeItemStackBase extends RecipeItem {
    protected abstract ItemStack getItemStack();

    @Override
    public boolean matches(ItemStack other) {
        ItemStack item = getItemStack();
        return other != null && item.getItem() == other.getItem() && (item.getItemDamage() == OreDictionary.WILDCARD_VALUE || item.getItemDamage() == other.getItemDamage());
    }

    @Override
    public List<ItemStack> getVisualStacks() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(getItemStack());
        return items;
    }
}
