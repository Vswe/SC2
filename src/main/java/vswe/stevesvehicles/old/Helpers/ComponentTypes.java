package vswe.stevesvehicles.old.Helpers;


import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.recipe.IRecipeOutput;

public enum ComponentTypes implements IRecipeOutput {
    WOODEN_WHEELS,
    IRON_WHEELS,
    RED_PIGMENT,
    GREEN_PIGMENT,
    BLUE_PIGMENT,
    GLASS_O_MAGIC,
    DYNAMITE,
    REMOVED_1(true),
    REMOVED_2(true),
    SIMPLE_PCB,
    GRAPHICAL_INTERFACE,
    RAW_HANDLE,
    REFINED_HANDLE,
    SPEED_HANDLE,
    WHEEL,
    SAW_BLADE,
    ADVANCED_PCB,
    WOOD_CUTTING_CORE,
    RAW_HARDENER,
    REFINED_HARDENER,
    HARDENED_MESH,
    STABILIZED_METAL,
    REINFORCED_METAL,
    REINFORCED_WHEELS,
    PIPE,
    SHOOTING_STATION,
    ENTITY_SCANNER,
    ENTITY_ANALYZER,
    EMPTY_DISK,
    TRI_TORCH,
    CHEST_PANE,
    LARGE_CHEST_PANE,
    HUGE_CHEST_PANE,
    CHEST_LOCK,
    IRON_PANE,
    LARGE_IRON_PANE,
    HUGE_IRON_PANE,
    DYNAMIC_PANE,
    LARGE_DYNAMIC_PANE,
    HUGE_DYNAMIC_PANE,
    CLEANING_FAN,
    CLEANING_CORE,
    CLEANING_TUBE,
    FUSE,
    SOLAR_PANEL,
    EYE_OF_GALGADOR,
    LUMP_OF_GALGADOR,
    GALGADORIAN_METAL,
    LARGE_LUMP_OF_GALGADOR,
    ENHANCED_GALGADORIAN_METAL,
    STOLEN_PRESENT,
    GREEN_WRAPPING_PAPER,
    RED_WRAPPING_PAPER,
    WARM_HAT,
    RED_GIFT_RIBBON,
    YELLOW_GIFT_RIBBON,
    SOCK,
    STUFFED_SOCK,
    ADVANCED_SOLAR_PANEL,
    BLANK_UPGRADE,
    TANK_VALVE,
    TANK_PANE,
    LARGE_TANK_PANE,
    HUGE_TANK_PANE,
    LIQUID_CLEANING_CORE,
    LIQUID_CLEANING_TUBE,
    EXPLOSIVE_EASTER_EGG,
    BURNING_EASTER_EGG,
    GLISTERING_EASTER_EGG,
    CHOCOLATE_EASTER_EGG,
    PAINTED_EASTER_EGG,
    BASKET,
    OAK_LOG,
    OAK_TWIG,
    SPRUCE_LOG,
    SPRUCE_TWIG,
    BIRCH_LOG,
    BIRCH_TWIG,
    JUNGLE_LOG,
    JUNGLE_TWIG,
    HARDENED_SAW_BLADE,
    GALGADORIAN_SAW_BLADE,
    GALGADORIAN_WHEELS,
    IRON_BLADE,
    BLADE_ARM;

    private int id;
    private String unlocalizedName;
    ComponentTypes(boolean invalid) {
        if (invalid) {
            id = 0;
            unlocalizedName = null;
        }else{
            this.id = ordinal();
            this.unlocalizedName = toString().toLowerCase();
        }
    }

    ComponentTypes() {
        this(false);
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public ItemStack getItemStack(int count) {
        return new ItemStack(ModItems.component, count, id);
    }

    @Override
    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    public int getId() {
        return id;
    }

    public boolean isStackOfType(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == ModItems.component && itemstack.getItemDamage() == id;
    }

}
