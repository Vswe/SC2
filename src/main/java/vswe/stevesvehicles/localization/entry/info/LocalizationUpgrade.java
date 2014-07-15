package vswe.stevesvehicles.localization.entry.info;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationUpgrade {
    public static final ILocalizedText BLUEPRINT = createSimple("blueprint");
    public static final ILocalizedText COMBUSTION = createSimple("combustion");
    public static final ILocalizedText DEPLOYER = createSimple("deployer");
    public static final ILocalizedText DISASSEMBLE = createSimple("disassemble");
    public static final ILocalizedText FUEL_CAPACITY = createAdvanced("fuel_capacity");
    public static final ILocalizedText FUEL_COST = createAdvanced("fuel_cost");
    public static final ILocalizedText FUEL_COST_FREE = createSimple("fuel_cost_free");
    public static final ILocalizedText INPUT_CHEST = createAdvanced("input_chest");
    public static final ILocalizedText MANAGER = createSimple("manager");
    public static final ILocalizedText GENERATOR = createAdvanced("generator");
    public static final ILocalizedText REDSTONE = createSimple("redstone");
    public static final ILocalizedText SOLAR = createSimple("solar");
    public static final ILocalizedText THERMAL = createSimple("thermal");
    public static final ILocalizedText TIME_FLAT = createAdvanced("time_flat");
    public static final ILocalizedText TIME_FLAT_CART = createAdvanced("time_flat_cart");
    public static final ILocalizedText TIME_FLAT_REMOVE = createAdvanced("time_flat_remove");
    public static final ILocalizedText TRANSPOSER = createSimple("transposer");
    public static final ILocalizedText EFFICIENCY = createAdvanced("efficiency");
    public static final ILocalizedText FREE_MODULES = createSimple("free_modules");

    private static final String HEADER = "steves_vehicles:gui.info.upgrade:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationUpgrade() {}
}
