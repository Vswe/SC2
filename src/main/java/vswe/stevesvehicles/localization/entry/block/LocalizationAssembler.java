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

    //TODO localize these
    public static final ILocalizedText INFO_BACKGROUND = new PlainText("Background");
    public static final ILocalizedText INFO_CHEST = new PlainText("Chest");
    public static final ILocalizedText INFO_INVISIBLE = new PlainText("Invisible");
    public static final ILocalizedText INFO_BRAKE = new PlainText("Brake");
    public static final ILocalizedText INFO_DRILL = new PlainText("Drill");
    public static final ILocalizedText INFO_LIGHT = new PlainText("Light");
    public static final ILocalizedText INFO_BRIDGE = new PlainText("Bridge");
    public static final ILocalizedText INFO_FARM = new PlainText("Farm");
    public static final ILocalizedText INFO_CUTTING = new PlainText("Cutting");
    public static final ILocalizedText INFO_LIQUID = new PlainText("Liquid");
    public static final ILocalizedText INFO_FUSE = new PlainText("Fuse");
    public static final ILocalizedText INFO_RAILS = new PlainText("Rails");
    public static final ILocalizedText INFO_EXPLOSIVES = new PlainText("Explosives");
    public static final ILocalizedText INFO_EXPLODE = new PlainText("Explode");
    public static final ILocalizedText INFO_SHIELD = new PlainText("Shield");
    public static final ILocalizedText INFO_TORCHES = new PlainText("Torches");
    public static final ILocalizedText INFO_BARRELS = new PlainText("Barrels");
    public static final ILocalizedText INFO_BARREL = new PlainText("Barrel");

    private static final String HEADER = "steves_vehicles:gui.assembler:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationAssembler() {}
}
