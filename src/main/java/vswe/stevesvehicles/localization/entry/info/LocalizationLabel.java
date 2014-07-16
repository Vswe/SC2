package vswe.stevesvehicles.localization.entry.info;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationLabel {
    public static final ILocalizedText MODULAR_COST = createSimple("modular_cost");
    public static final ILocalizedText TOP = createSimple("top");
    public static final ILocalizedText CENTER = createSimple("center");
    public static final ILocalizedText BOTTOM = createSimple("bottom");
    public static final ILocalizedText BACK = createSimple("back");
    public static final ILocalizedText LEFT = createSimple("left");
    public static final ILocalizedText RIGHT = createSimple("right");
    public static final ILocalizedText FRONT = createSimple("front");
    public static final ILocalizedText SIDES = createAdvanced("sides");
    public static final ILocalizedText VEHICLE_TYPES = createAdvanced("vehicle_types");
    public static final ILocalizedText AND = createSimple("and");
    public static final ILocalizedText NO_SIDES = createSimple("no_sides");
    public static final ILocalizedText MODULE_CONFLICT_HOWEVER = createSimple("module_conflict_however");
    public static final ILocalizedText MODULE_CONFLICT_ALSO = createSimple("module_conflict_also");
    public static final ILocalizedText REQUIRES = createSimple("requires");
    public static final ILocalizedText COUNT_ONE = createSimple("count_one");
    public static final ILocalizedText COUNT_TWO = createSimple("count_two");
    public static final ILocalizedText COUNT_THREE = createSimple("count_three");
    public static final ILocalizedText DUPLICATES = createSimple("duplicates");
    public static final ILocalizedText TYPE = createSimple("type");
    public static final ILocalizedText CAPACITY_ERROR = createSimple("capacity_error");
    public static final ILocalizedText COMBINATION_ERROR = createSimple("combination_error");
    public static final ILocalizedText COMPLEXITY_ERROR = createAdvanced("complexity_error");
    public static final ILocalizedText PARENT_ERROR = createAdvanced("parent_error");
    public static final ILocalizedText NEMESIS_ERROR = createAdvanced("nemesis_error");
    public static final ILocalizedText DUPLICATE_ERROR = createAdvanced("duplicate_error");
    public static final ILocalizedText CLASH_ERROR = createAdvanced("clash_error");
    public static final ILocalizedText MISSING_VEHICLE_ERROR = createSimple("missing_vehicle_error");
    public static final ILocalizedText CAPACITY = createAdvanced("capacity");
    public static final ILocalizedText COMPLEXITY_CAP = createAdvanced("complexity_cap");
    public static final ILocalizedText MAX_ENGINES = createAdvanced("max_engines");
    public static final ILocalizedText MAX_STORAGE = createAdvanced("max_storage");
    public static final ILocalizedText MAX_ADDONS = createAdvanced("max_addons");
    public static final ILocalizedText NO_MODULES = createSimple("no_modules");
    public static final ILocalizedText INCOMPLETE = createSimple("incomplete");
    public static final ILocalizedText TIME_LEFT = createSimple("time_left");
    public static final ILocalizedText INTERRUPT_INSTRUCTION = createSimple("interrupt_instruction");


    public static final ILocalizedText NO_VEHICLE_TYPE = createSimple("no_vehicle_type");
    public static final ILocalizedText INVALID_VEHICLE_TYPE = createAdvanced("invalid_vehicle_type");

    private static final String HEADER = "steves_vehicles:gui.info.label:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationLabel() {}
}
