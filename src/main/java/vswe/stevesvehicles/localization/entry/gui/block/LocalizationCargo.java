package vswe.stevesvehicles.localization.entry.gui.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCargo {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText CHANGE_LAYOUT = createSimple("change_layout");
    public static final ILocalizedText SHARED_LAYOUT = createSimple("shared_layout");
    public static final ILocalizedText SIDE_LAYOUT = createSimple("side_layout");
    public static final ILocalizedText COLOR_LAYOUT = createSimple("color_layout");
    public static final ILocalizedText TRANSFER_ALL = createSimple("transfer_all");
    public static final ILocalizedText TRANSFER_ITEMS = createAdvanced("transfer_items");
    public static final ILocalizedText TRANSFER_STACKS = createAdvanced("transfer_stacks");
    public static final ILocalizedText TRANSFER_ALL_SHORT = createSimple("transfer_all_short");
    public static final ILocalizedText TRANSFER_ITEMS_SHORT = createSimple("transfer_items_short");
    public static final ILocalizedText TRANSFER_STACKS_SHORT = createSimple("transfer_stacks_short");
    public static final ILocalizedText CHANGE_VEHICLE_PART = createSimple("change_vehicle_part");
    public static final ILocalizedText SLOT_INVALID = createSimple("slot_invalid");
    public static final ILocalizedText SLOT_ALL = createSimple("slot_all");
    public static final ILocalizedText SLOT_ENGINE = createSimple("slot_engine");
    public static final ILocalizedText SLOT_RAILER = createSimple("slot_railer");
    public static final ILocalizedText SLOT_STORAGE = createSimple("slot_storage");
    public static final ILocalizedText SLOT_TORCH = createSimple("slot_torch");
    public static final ILocalizedText SLOT_EXPLOSIVE = createSimple("slot_explosive");
    public static final ILocalizedText SLOT_ARROW = createSimple("slot_arrow");
    public static final ILocalizedText SLOT_BRIDGE = createSimple("slot_bridge");
    public static final ILocalizedText SLOT_SEED = createSimple("slot_seed");
    public static final ILocalizedText SLOT_FERTILIZER = createSimple("slot_fertilizer");
    public static final ILocalizedText SLOT_SAPLING = createSimple("slot_sapling");
    public static final ILocalizedText SLOT_FIREWORK = createSimple("slot_firework");
    public static final ILocalizedText SLOT_BUCKET = createSimple("slot_bucket");
    public static final ILocalizedText SLOT_CAKE = createSimple("slot_cake");



    private static final String HEADER = "steves_vehicles:gui.cargo:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationCargo() {}
}
