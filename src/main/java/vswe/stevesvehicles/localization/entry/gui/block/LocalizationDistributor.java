package vswe.stevesvehicles.localization.entry.gui.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationDistributor {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText NOT_CONNECTED = createSimple("not_connected");
    public static final ILocalizedText SIDE_NAME = createAdvanced("side_name");
    public static final ILocalizedText DROP_INSTRUCTION = createSimple("drop_instruction");
    public static final ILocalizedText REMOVE_INSTRUCTION = createSimple("remove_instruction");
    public static final ILocalizedText ALL = createSimple("all");
    public static final ILocalizedText RED = createSimple("red");
    public static final ILocalizedText BLUE = createSimple("blue");
    public static final ILocalizedText YELLOW = createSimple("yellow");
    public static final ILocalizedText GREEN = createSimple("green");
    public static final ILocalizedText TOP_LEFT = createSimple("top_left");
    public static final ILocalizedText TOP_RIGHT = createSimple("top_right");
    public static final ILocalizedText BOTTOM_LEFT = createSimple("bottom_left");
    public static final ILocalizedText BOTTOM_RIGHT = createSimple("bottom_right");
    public static final ILocalizedText TO_VEHICLE = createSimple("to_vehicle");
    public static final ILocalizedText FROM_VEHICLE = createSimple("from_vehicle");
    public static final ILocalizedText TOP_MANAGER = createSimple("top_manager");
    public static final ILocalizedText BOTTOM_MANAGER = createSimple("bottom_manager");
    public static final ILocalizedText ORANGE = createSimple("orange");
    public static final ILocalizedText PURPLE = createSimple("purple");
    public static final ILocalizedText SIDE_TOOLTIP = createAdvanced("side_tooltip");


    private static final String HEADER = "steves_vehicles:gui.distributor.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationDistributor() {}
}
