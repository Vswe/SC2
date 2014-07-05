package vswe.stevesvehicles.localization.entry.gui.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;

public final class LocalizationTravel {
    public static final ILocalizedText SEAT_MESSAGE = createAdvanced("seat.message");
    public static final ILocalizedText CAGE_AUTO_MESSAGE = createAdvanced("cage.auto_message");
    public static final ILocalizedText CAGE_MESSAGE = createAdvanced("cage.message");

    private static final String HEADER = "steves_vehicles:gui.common.travel:";
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationTravel() {}
}
