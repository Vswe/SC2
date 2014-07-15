package vswe.stevesvehicles.localization.entry.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;
import vswe.stevesvehicles.localization.PlainText;

public final class LocalizationAssembler {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText BASIC_INSTRUCTION = createSimple("basic_instruction");
    public static final ILocalizedText INVALID_HULL = createSimple("invalid_hull");
    public static final ILocalizedText HULL_CAPACITY = createSimple("hull_capacity");
    public static final ILocalizedText COMPLEXITY_CAP = createSimple("complexity_cap");
    public static final ILocalizedText TOTAL_COST = createSimple("total_cost");
    public static final ILocalizedText TOTAL_TIME = createSimple("total_time");
    public static final ILocalizedText READY_MESSAGE = createSimple("ready_message");
    public static final ILocalizedText PROGRESS = createSimple("progress");
    public static final ILocalizedText TIME_LEFT = createSimple("time_left");
    public static final ILocalizedText IDLE_MESSAGE = createSimple("idle_message");
    public static final ILocalizedText MODIFY_VEHICLE = createSimple("modify_vehicle");
    public static final ILocalizedText ASSEMBLE_VEHICLE = createSimple("assemble_vehicle");
    public static final ILocalizedText FUEL_LEVEL = createSimple("fuel_level");
    public static final ILocalizedText NO_HULL = createSimple("no_hull");
    public static final ILocalizedText INVALID_HULL_SHORT = createSimple("invalid_hull_short");
    public static final ILocalizedText BUSY_ASSEMBLER = createSimple("busy_assembler");
    public static final ILocalizedText OCCUPIED_DEPARTURE_BAY = createSimple("occupied_departure_bay");
    public static final ILocalizedText ASSEMBLE = createSimple("assemble");
    public static final ILocalizedText MODIFY = createSimple("modify");

    public static final ILocalizedText TITLE_ENGINES = createSimple("engines");
    public static final ILocalizedText TITLE_TOOL = createSimple("tool");
    public static final ILocalizedText TITLE_ATTACHMENTS = createSimple("attachments");
    public static final ILocalizedText TITLE_STORAGE = createSimple("storage");
    public static final ILocalizedText TITLE_ADDONS = createSimple("addons");
    public static final ILocalizedText TITLE_INFORMATION = createSimple("information");

    public static final ILocalizedText VEHICLE_RECIPE_TITLE = createSimple("recipe.vehicle_title");
    public static final ILocalizedText SHAPED_RECIPE_TITLE = createSimple("recipe.shaped_title");
    public static final ILocalizedText SHAPELESS_RECIPE_TITLE = createSimple("recipe.shapeless_title");
    public static final ILocalizedText FUEL_RECIPE = createSimple("recipe.fuel");
    public static final ILocalizedText CAPACITY_RECIPE = createSimple("recipe.capacity");
    public static final ILocalizedText TIME_RECIPE = createSimple("recipe.time");

    public static final ILocalizedText INFO_BACKGROUND = createSimple("info.background");
    public static final ILocalizedText INFO_CHEST = createSimple("info.chest");
    public static final ILocalizedText INFO_INVISIBLE = createSimple("info.invisible");
    public static final ILocalizedText INFO_BRAKE = createSimple("info.brake");
    public static final ILocalizedText INFO_DRILL = createSimple("info.drill");
    public static final ILocalizedText INFO_LIGHT = createSimple("info.light");
    public static final ILocalizedText INFO_BRIDGE = createSimple("info.bridge");
    public static final ILocalizedText INFO_FARM = createSimple("info.farm");
    public static final ILocalizedText INFO_CUTTING = createSimple("info.cutting");
    public static final ILocalizedText INFO_LIQUID = createSimple("info.liquid");
    public static final ILocalizedText INFO_FUSE = createSimple("info.fuse");
    public static final ILocalizedText INFO_RAILS = createSimple("info.rails");
    public static final ILocalizedText INFO_EXPLOSIVES = createSimple("info.explosives");
    public static final ILocalizedText INFO_EXPLODE = createSimple("info.explode");
    public static final ILocalizedText INFO_SHIELD = createSimple("info.shield");
    public static final ILocalizedText INFO_TORCHES = createSimple("info.torches");
    public static final ILocalizedText INFO_BARRELS = createSimple("info.barrels");
    public static final ILocalizedText INFO_BARREL = createSimple("info.barrel");

    public static final ILocalizedText SEARCH_RELAXED = createSimple("search_relaxed");
    public static final ILocalizedText SEARCH_NORMAL = createSimple("search_normal");
    public static final ILocalizedText SEARCH_STRICT = createSimple("search_strict");
    public static final ILocalizedText SEARCH_PAGE = createSimple("search_page");

    private static final String HEADER = "steves_vehicles:gui.assembler:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationAssembler() {}
}
