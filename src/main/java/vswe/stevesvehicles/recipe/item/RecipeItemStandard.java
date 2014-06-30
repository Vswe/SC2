package vswe.stevesvehicles.recipe.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeItemStandard extends RecipeItemStackBase {
    private ItemStack itemStack;

    public RecipeItemStandard(ItemStack itemStack) {
        this.itemStack = itemStack.copy();
    }

    public RecipeItemStandard(Block block) {
        this.itemStack = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
    }

    public RecipeItemStandard(Item item) {
        this.itemStack = new ItemStack(item);
    }

    @Override
    protected ItemStack getItemStack() {
        return itemStack;
    }
}

