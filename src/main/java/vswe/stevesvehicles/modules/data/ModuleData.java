package vswe.stevesvehicles.modules.data;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Helpers.ColorHelper;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Models.Cart.ModelCartbase;
import vswe.stevesvehicles.old.ModuleData.ModuleDataGroup;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleData {
    private final Class<? extends ModuleBase> moduleClass;
    private final String unlocalizedName;
    private final int modularCost;
    private final ModuleType moduleType;
    private ArrayList<ModuleSide> sides;
    private boolean allowDuplicate;
    private ArrayList<ModuleData> nemesis;
    private ArrayList<ModuleDataGroup> requirement;
    private ModuleData parent;
    private boolean isValid;
    private boolean isLocked;
    private boolean defaultLock;
    private boolean hasRecipe;
    private ArrayList<Localization.MODULE_INFO> message;
    private boolean useExtraData;
    private byte extraDataDefaultValue;
    private ArrayList<IRecipe> recipes;
    private ArrayList<VehicleType> validVehicles;

    @SideOnly(Side.CLIENT)
    private HashMap<String,ModelCartbase> models;
    @SideOnly(Side.CLIENT)
    private HashMap<String,ModelCartbase> modelsPlaceholder;
    @SideOnly(Side.CLIENT)
    private ArrayList<String> removedModels;
    @SideOnly(Side.CLIENT)
    private float modelMultiplier = 0.75F;

    public ModuleData(String unlocalizedName, Class<? extends ModuleBase> moduleClass, int modularCost) {
        this.moduleClass = moduleClass;
        if (unlocalizedName.contains(":")) {
            System.err.println("The raw unlocalized name can't contain colons. Any colons have been replaced with underscores.");
        }
        this.unlocalizedName = unlocalizedName.replace(":", "_");
        this.modularCost = modularCost;

        ModuleType moduleType = ModuleType.INVALID;
        for (ModuleType type : ModuleType.values()) {
            if (moduleType.getClazz().isAssignableFrom(moduleClass)) {
                moduleType = type;
                break;
            }
        }
        this.moduleType = moduleType;
    }

    public final Class<? extends ModuleBase> getModuleClass() {
        return moduleClass;
    }

    public final String getRawUnlocalizedName() {
        return unlocalizedName;
    }

    public final int getCost() {
        return modularCost;
    }

    public final ModuleType getModuleType() {
        return moduleType;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public ModuleData lock() {
        isLocked = true;

        return this;
    }

    public boolean getEnabledByDefault() {
        return !defaultLock;
    }

    protected ModuleData lockByDefault() {
        defaultLock = true;

        return this;
    }

    protected ModuleData setAllowDuplicate() {
        allowDuplicate = true;

        return this;
    }

    public boolean getAllowDuplicate() {
        return allowDuplicate;
    }

    public ModuleData useExtraData(byte defaultValue) {
        this.extraDataDefaultValue = defaultValue;
        this.useExtraData = true;

        return this;
    }

    public boolean isUsingExtraData() {
        return useExtraData;
    }

    public byte getDefaultExtraData() {
        return extraDataDefaultValue;
    }


    public ArrayList<ModuleSide> getSides() {
        return sides;
    }

    public ModuleData addSides(ModuleSide ... sides) {
        if (this.sides == null) {
            this.sides = new ArrayList<ModuleSide>();
        }
        for (ModuleSide side : sides) {
            this.sides.add(side);

            if (side == ModuleSide.TOP) { //TODO do this in a nicer way
                removeModel("Rails");
            }
        }

        return this;
    }

    public ModuleData addParent(ModuleData parent) {
        this.parent = parent;

        return this;
    }

    public ModuleData addMessage(Localization.MODULE_INFO s) {
        if (message == null) {
            message = new ArrayList<Localization.MODULE_INFO>();
        }
        message.add(s);

        return this;
    }

    public void addNemesis(ModuleData nemesis) {
        if (this.nemesis == null) {
            this.nemesis = new ArrayList<ModuleData>();
        }
        this.nemesis.add(nemesis);
    }

    public ModuleData addRequirement(ModuleDataGroup requirement) {
        if (this.requirement == null) {
            this.requirement = new ArrayList<ModuleDataGroup>();
        }
        this.requirement.add(requirement);

        return this;
    }

    public static void addNemesis(ModuleData m1, ModuleData m2) {
        m2.addNemesis(m1);
        m1.addNemesis(m2);
    }

    public float getModelMultiplier() {
        return modelMultiplier;
    }

    public ModuleData setModelMultiplier(float val) {
        modelMultiplier = val;

        return this;
    }

    public ModuleData addModel(String tag, ModelCartbase model) {
        addModel(tag, model, false);
        addModel(tag, model, true);
        return this;
    }
    public ModuleData addModel(String tag, ModelCartbase model, boolean placeholder) {
        if (placeholder) {
            if (modelsPlaceholder == null) {
                modelsPlaceholder = new HashMap<String,ModelCartbase>();
            }

            modelsPlaceholder.put(tag, model);
        }else{
            if (models == null) {
                models = new HashMap<String,ModelCartbase>();
            }

            models.put(tag, model);
        }

        return this;
    }

    public HashMap<String,ModelCartbase> getModels(boolean placeholder) {
        if (placeholder) {
            return modelsPlaceholder;
        }else{
            return models;
        }
    }

    public boolean haveModels(boolean placeholder) {
        if (placeholder) {
            return modelsPlaceholder != null;
        }else{
            return models != null;
        }
    }

    public ModuleData removeModel(String tag) {
        if (removedModels == null) {
            removedModels = new ArrayList<String>();
        }

        if(!removedModels.contains(tag)) {
            removedModels.add(tag);
        }
        return this;
    }

    public ArrayList<String> getRemovedModels() {
        return removedModels;
    }

    public boolean haveRemovedModels() {
        return removedModels != null;
    }

    public String getName() {
        return StatCollector.translateToLocal(getUnlocalizedName());
    }

    public String getUnlocalizedName() {
        return "item." + StevesVehicles.localStart + unlocalizedName + ".name";
    }


    public ModuleData getParent() {
        return parent;
    }

    public ArrayList<ModuleData> getNemesis() {
        return nemesis;
    }

    public ArrayList<ModuleDataGroup> getRequirement() {
        return requirement;
    }

    public boolean getHasRecipe() {
        return hasRecipe;
    }

    public String getModuleInfoText(byte data) {
        return null;
    }

    public String getCartInfoText(String name, byte data) {
        return name;
    }

    public void addSpecificInformation(List<String> list) {
        list.add(ColorHelper.LIGHTGRAY + Localization.MODULE_INFO.MODULAR_COST.translate() + ": " + modularCost);
    }


    public ModuleData addRecipe(IRecipe recipe) {
        if(this.recipes == null) {
            this.recipes = new ArrayList<IRecipe>();
        }

        this.recipes.add(recipe);

        return this;
    }


    public ModuleData addShapedRecipe(Object ... recipe) {
        addRecipe(null); //TODO create a shaped recipe

        return this;
    }

    public ModuleData addShapelessRecipe(Object ... recipe) {
        addRecipe(null); //TODO create a shapeless recipe

        return this;
    }

    public ModuleData addVehicles(VehicleType ... types) {
        if (validVehicles == null) {
            validVehicles = new ArrayList<VehicleType>();
        }

        for (VehicleType type : types) {
            validVehicles.add(type);
        }

        return this;
    }

    @SideOnly(Side.CLIENT)
    public void loadModels() {}
}
