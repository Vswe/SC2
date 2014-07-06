package vswe.stevesvehicles.localization.entry.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationDetector {
    public static final ILocalizedText OUTPUT = createSimple("output");
    public static final ILocalizedText AND = createSimple("and");
    public static final ILocalizedText OR = createSimple("or");
    public static final ILocalizedText NOT = createSimple("not");
    public static final ILocalizedText XOR = createSimple("xor");
    public static final ILocalizedText TOP_UNIT = createSimple("top_unit");
    public static final ILocalizedText BOTTOM_UNIT = createSimple("bottom_unit");
    public static final ILocalizedText NORTH_UNIT = createSimple("north_unit");
    public static final ILocalizedText WEST_UNIT = createSimple("west_unit");
    public static final ILocalizedText SOUTH_UNIT = createSimple("south_unit");
    public static final ILocalizedText EAST_UNIT = createSimple("east_unit");
    public static final ILocalizedText REDSTONE = createSimple("redstone");
    public static final ILocalizedText TOP_REDSTONE = createSimple("top_redstone");
    public static final ILocalizedText BOTTOM_REDSTONE = createSimple("bottom_redstone");
    public static final ILocalizedText NORTH_REDSTONE = createSimple("north_redstone");
    public static final ILocalizedText WEST_REDSTONE = createSimple("west_redstone");
    public static final ILocalizedText SOUTH_REDSTONE = createSimple("south_redstone");
    public static final ILocalizedText EAST_REDSTONE = createSimple("east_redstone");

    private static final String HEADER = "steves_vehicles:gui.detector:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationDetector() {}
}
