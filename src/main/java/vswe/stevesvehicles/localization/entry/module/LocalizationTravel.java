package vswe.stevesvehicles.localization.entry.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationTravel {
    public static final ILocalizedText SEAT_MESSAGE = createAdvanced("seat.message");
    public static final ILocalizedText CAGE_AUTO_MESSAGE = createAdvanced("cage.auto_message");
    public static final ILocalizedText CAGE_MESSAGE = createAdvanced("cage.message");
    public static final ILocalizedText ARCADE_PAGES = createAdvanced("steves_arcade.pages");
    public static final ILocalizedText ARCADE_EXIT = createSimple("steves_arcade.exit");

    private static final String HEADER = "steves_vehicles:gui.common.travel:";
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationTravel() {}
}
