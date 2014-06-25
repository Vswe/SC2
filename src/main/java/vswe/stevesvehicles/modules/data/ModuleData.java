package vswe.stevesvehicles.modules.data;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Models.Cart.ModelCartbase;
import vswe.stevesvehicles.old.ModuleData.ModuleDataGroup;
import vswe.stevesvehicles.old.StevesCarts;

import java.util.ArrayList;
import java.util.HashMap;

public class ModuleData {
    private Class<? extends ModuleBase> moduleClass;
    private String unlocalizedName;
    private int modularCost;
    private ModuleType moduleType;
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
        this.unlocalizedName = unlocalizedName;
        this.modularCost = modularCost;

        for (ModuleType moduleType : ModuleType.values()) {
            if (moduleType.getClazz().isAssignableFrom(moduleClass)) {
                this.moduleType = moduleType;
                break;
            }
        }
    }

    public Class<? extends ModuleBase> getModuleClass() {
        return moduleClass;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    protected ModuleData lock() {
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

    protected boolean getAllowDuplicate() {
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

    protected ModuleData addSides(ModuleSide ... sides) {
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

    protected ModuleData addParent(ModuleData parent) {
        this.parent = parent;

        return this;
    }

    protected ModuleData addMessage(Localization.MODULE_INFO s) {
        if (message == null) {
            message = new ArrayList<Localization.MODULE_INFO>();
        }
        message.add(s);

        return this;
    }

    protected void addNemesis(ModuleData nemesis) {
        if (this.nemesis == null) {
            this.nemesis = new ArrayList<ModuleData>();
        }
        this.nemesis.add(nemesis);
    }

    protected ModuleData addRequirement(ModuleDataGroup requirement) {
        if (this.requirement == null) {
            this.requirement = new ArrayList<ModuleDataGroup>();
        }
        this.requirement.add(requirement);

        return this;
    }

    protected static void addNemesis(ModuleData m1, ModuleData m2) {
        m2.addNemesis(m1);
        m1.addNemesis(m2);
    }

    public float getModelMultiplier() {
        return modelMultiplier;
    }

    protected ModuleData setModelMultiplier(float val) {
        modelMultiplier = val;

        return this;
    }

    protected ModuleData addModel(String tag, ModelCartbase model) {
        addModel(tag, model, false);
        addModel(tag, model, true);
        return this;
    }
    protected ModuleData addModel(String tag, ModelCartbase model, boolean placeholder) {
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

    protected ModuleData removeModel(String tag) {
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
        return "item." + StevesCarts.localStart + unlocalizedName + ".name";
    }

    public int getCost() {
        return modularCost;
    }

    protected ModuleData getParent() {
        return parent;
    }

    protected ArrayList<ModuleData> getNemesis() {
        return nemesis;
    }

    protected ArrayList<ModuleDataGroup> getRequirement() {
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

}
