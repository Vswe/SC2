package vswe.stevesvehicles.localization.entry.module.cart;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCartTravel {
    public static final ILocalizedText LEVER_TITLE = createSimple("brake_handle.title");
    public static final ILocalizedText LEVER_START = createSimple("brake_handle.start");
    public static final ILocalizedText LEVER_STOP = createSimple("brake_handle.stop");
    public static final ILocalizedText LEVER_TURN_BACK = createSimple("brake_handle.turn_back");
    public static final ILocalizedText CONTROL_TITLE = createSimple("advanced_control_system.title");
    public static final ILocalizedText CONTROL_UNITS = createAdvanced("advanced_control_system.units");
    public static final ILocalizedText CONTROL_ODO = createSimple("advanced_control_system.odo");
    public static final ILocalizedText CONTROL_TRIP = createSimple("advanced_control_system.trip");
    public static final ILocalizedText CONTROL_RESET_TRIP = createSimple("advanced_control_system.reset_trip");

    private static final String HEADER = "steves_vehicles:gui.cart.travel:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationCartTravel() {}
}
