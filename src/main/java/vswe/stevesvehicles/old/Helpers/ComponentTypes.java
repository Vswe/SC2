package vswe.stevesvehicles.old.Helpers;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.item.ModItems;
import vswe.stevesvehicles.recipe.IRecipeOutput;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;
import vswe.stevesvehicles.recipe.ModuleRecipeShapeless;

public enum ComponentTypes implements IRecipeOutput {
    WOODEN_WHEELS,
    IRON_WHEELS,
    RED_PIGMENT,
    GREEN_PIGMENT,
    BLUE_PIGMENT,
    GLASS_O_MAGIC,
    DYNAMITE,
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
    CHEST_LOCK,
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
    STOLEN_PRESENT {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    GREEN_WRAPPING_PAPER {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    RED_WRAPPING_PAPER {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    WARM_HAT {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    RED_GIFT_RIBBON {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    YELLOW_GIFT_RIBBON {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    SOCK {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    STUFFED_SOCK {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.CHRISTMAS;
        }
    },
    ADVANCED_SOLAR_PANEL,
    BLANK_UPGRADE,
    TANK_VALVE,
    LIQUID_CLEANING_CORE,
    LIQUID_CLEANING_TUBE,
    EXPLOSIVE_EASTER_EGG {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.EASTER;
        }
    },
    BURNING_EASTER_EGG {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.EASTER;
        }
    },
    GLISTERING_EASTER_EGG {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.EASTER;
        }
    },
    CHOCOLATE_EASTER_EGG {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.EASTER;
        }
    },
    PAINTED_EASTER_EGG {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.EASTER;
        }
    },
    BASKET {
        @Override
        public HolidayType getRequiredHoliday() {
            return HolidayType.EASTER;
        }
    },
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

    public HolidayType getRequiredHoliday() {
        return null;
    }

    public void addShapedRecipeWithSizeAndCount(int width, int height, int count, Object ... recipe) {
        if (ModItems.component.isValid(getItemStack())) {
            GameRegistry.addRecipe(new ModuleRecipeShaped(this, count, width, height, recipe));
        }
    }

    public void addShapedRecipeWithSize(int width, int height, Object ... recipe) {
        addShapedRecipeWithSizeAndCount(width, height, 1, recipe);
    }

    public void addShapedRecipe(Object ... recipe) {
        addShapedRecipeWithSizeAndCount(3, 3, 1, recipe);
    }

    public void addShapelessRecipeWithCount(int count, Object ... recipe) {
        if (ModItems.component.isValid(getItemStack())) {
            GameRegistry.addRecipe(new ModuleRecipeShapeless(this, count, recipe));
        }
    }

    public void addShapelessRecipe(Object ... recipe) {
        addShapelessRecipeWithCount(1, recipe);
    }
}
