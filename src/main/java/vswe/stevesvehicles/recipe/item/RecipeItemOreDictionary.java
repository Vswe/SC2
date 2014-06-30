package vswe.stevesvehicles.recipe.item;

import net.minecraftforge.oredict.OreDictionary;

public class RecipeItemOreDictionary extends RecipeItemCluster {
    public RecipeItemOreDictionary(String entry) {
        super(OreDictionary.getOres(entry).toArray());
    }
}
