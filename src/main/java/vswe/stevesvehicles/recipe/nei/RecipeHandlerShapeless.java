package vswe.stevesvehicles.recipe.nei;


import vswe.stevesvehicles.recipe.ModuleRecipe;
import vswe.stevesvehicles.recipe.ModuleRecipeShapeless;

public class RecipeHandlerShapeless extends RecipeHandler {

    //same as the vanilla recipes use, for consistency
    private static final int[][] STACK_ORDER = {
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1},
            {0, 2},
            {1, 2},
            {2, 0},
            {2, 1},
            {2, 2}
    };

    protected class CachedModuleRecipeShapeless extends CachedModuleRecipe {


        public CachedModuleRecipeShapeless(ModuleRecipeShapeless recipe) {
            super(recipe);
        }

        @Override
        protected int getX(int id) {
            return 25 + STACK_ORDER[id][0] * 18  ;
        }

        @Override
        protected int getY(int id) {
            return 6 + STACK_ORDER[id][1] * 18;
        }
    }

    public RecipeHandlerShapeless() {

    }

    @Override
    public String getRecipeName() {
        return "SV Shapeless Crafting";
    }

    @Override
    protected CachedModuleRecipe createCachedRecipe(ModuleRecipe recipe) {
        if (recipe instanceof ModuleRecipeShapeless) {
            return new CachedModuleRecipeShapeless((ModuleRecipeShapeless)recipe);
        }else{
            return null;
        }
    }


}
