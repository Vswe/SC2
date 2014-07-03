package vswe.stevesvehicles.recipe.item;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.util.Tuple;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public abstract class RecipeItem {
    private static Map<Class, Class<? extends RecipeItem>> registry = new HashMap<Class, Class<? extends RecipeItem>>();
    private static RecipeItem empty = new RecipeItemEmpty();

    static {
        register(RecipeItemStandard.class, ItemStack.class);
        register(RecipeItemStandard.class, Block.class);
        register(RecipeItemStandard.class, Item.class);
        register(RecipeItemComponent.class, ComponentTypes.class);
        register(RecipeItemModule.class, ModuleData.class);
        register(RecipeItemUpgrade.class, Upgrade.class);
        register(RecipeItemOreDictionary.class, String.class);
        register(RecipeItemCluster.class, Object[].class);
    }

    public static void register(Class<? extends RecipeItem> recipeItemClass, Class dataClass) {
        registry.put(dataClass, recipeItemClass);
    }

    public static RecipeItem createRecipeItem(Object data) {
        if (data != null) {
            Class dataClass = data.getClass();
            Tuple<Class<? extends RecipeItem>, Class>  recipeItemClasses = getRecipeClassFromClass(dataClass);
            if (recipeItemClasses != null) {
                try {
                    Constructor<? extends RecipeItem> constructor = recipeItemClasses.getFirstObject().getConstructor(recipeItemClasses.getSecondObject());
                    Object obj = constructor.newInstance(data);
                    return (RecipeItem)obj;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.err.println("Failed to create a RecipeItem for the given data. The given data was " + data.toString() + "with the class " + data.getClass().getName());
        }

        return empty;
    }

    private static Tuple<Class<? extends RecipeItem>, Class> getRecipeClassFromClass(Class dataClass) {
        Class<? extends RecipeItem> recipeItemClass = registry.get(dataClass);
        if (recipeItemClass != null) {
            return new Tuple<Class<? extends RecipeItem>, Class>(recipeItemClass, dataClass);
        }else{
            Class dataSuperClass = dataClass.getSuperclass();
            return dataSuperClass == null ? null : getRecipeClassFromClass(dataSuperClass);
        }
    }

    public boolean isEmpty() {
        return false;
    }

    public abstract boolean matches(ItemStack other);
}
