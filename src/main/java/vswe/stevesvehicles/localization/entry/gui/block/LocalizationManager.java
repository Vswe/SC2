package vswe.stevesvehicles.localization.entry.gui.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationManager {
    public static final ILocalizedText TITLE = createSimple("tile");
    public static final ILocalizedText CURRENT_SETTING = createSimple("current_setting");
    public static final ILocalizedText CHANGE_TRANSFER_DIRECTION = createSimple("change_transfer_direction");
    public static final ILocalizedText TO_VEHICLE = createSimple("to_vehicle");
    public static final ILocalizedText FROM_VEHICLE = createSimple("from_heicle");
    public static final ILocalizedText CHANGE_TURN_BACK = createSimple("change_turn_back");
    public static final ILocalizedText TURN_BACK_DISABLED = createSimple("turn_back_disabled");
    public static final ILocalizedText TURN_BACK = createSimple("turn_back");
    public static final ILocalizedText CONTINUE_FORWARD = createSimple("continue_forward");
    public static final ILocalizedText CHANGE_TRANSFER_SIZE = createSimple("change_transfer_size");
    public static final ILocalizedText CHANGE_SIDE = createSimple("change_side");
    public static final ILocalizedText CURRENT_SIDE = createSimple("current_side");
    public static final ILocalizedText RED_SIDE = createSimple("red_side");
    public static final ILocalizedText BLUE_SIDE = createSimple("blue_side");
    public static final ILocalizedText YELLOW_SIDE = createSimple("yellow_side");
    public static final ILocalizedText GREEN_SIDE = createSimple("green_side");
    public static final ILocalizedText DISABLED_SIDE = createSimple("disabled_side");



    private static final String HEADER = "steves_vehicles:gui.manager.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationManager() {}
}
