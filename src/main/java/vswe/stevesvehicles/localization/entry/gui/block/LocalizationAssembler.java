package vswe.stevesvehicles.localization.entry.gui.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

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


    private static final String HEADER = "steves_vehicles:gui.assembler:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationAssembler() {}
}
