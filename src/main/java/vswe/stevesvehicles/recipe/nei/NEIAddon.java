package vswe.stevesvehicles.recipe.nei;

import codechicken.nei.api.API;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.GeneratedInfo;


//TODO make sure nothing goes wrong just because NEI doesn't exist

@Mod(modid = "StevesVehiclesNEIAddon", name = "Steve's Vehicles - NEI Addon", version = GeneratedInfo.version)
public class NEIAddon {
    @Mod.Instance("StevesVehiclesNEIAddon")
    public static NEIAddon instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent evt) {
        if (evt.getSide() == Side.CLIENT) {
            register();
        }
    }

    @SideOnly(Side.CLIENT)
    private void register() {
        register(new RecipeHandlerModuleShaped());
        register(new RecipeHandlerModuleShapeless());
        API.registerRecipeHandler(new RecipeHandlerVehicleNormal());
        register(new RecipeHandlerVehicleModuleUsage());
    }

    @SideOnly(Side.CLIENT)
    private void register(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }

}
