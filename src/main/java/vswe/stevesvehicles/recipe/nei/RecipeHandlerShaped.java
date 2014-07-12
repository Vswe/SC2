package vswe.stevesvehicles.recipe.nei;


import vswe.stevesvehicles.recipe.ModuleRecipe;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;

public class RecipeHandlerShaped extends RecipeHandler {

    protected class CachedModuleRecipeShaped extends CachedModuleRecipe {

        private int width;
        private boolean is2x2;

        public CachedModuleRecipeShaped(ModuleRecipeShaped recipe) {
            super(recipe);
        }

        @Override
        protected void init(ModuleRecipe recipe) {
            width = ((ModuleRecipeShaped)recipe).getFullWidth();
            int height = ((ModuleRecipeShaped)recipe).getFulLHeight();
            is2x2 = width <= 2 && height <= 2;
        }

        @Override
        protected int getX(int id) {
            return 25 + (id % width)  * 18  ;
        }

        @Override
        protected int getY(int id) {
            return 6 + (id / width) * 18;
        }

        @Override
        public boolean is2x2() {
            return is2x2;
        }
    }


    public RecipeHandlerShaped() {

    }

    @Override
    public String getRecipeName() {
        return "SV Shaped Crafting";
    }

    @Override
    protected CachedModuleRecipe createCachedRecipe(ModuleRecipe recipe) {
        if (recipe instanceof ModuleRecipeShaped) {
            return new CachedModuleRecipeShaped((ModuleRecipeShaped)recipe);
        }else{
            return null;
        }
    }
}
