package vswe.stevesvehicles.localization.entry.gui.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationIndependence {
    public static final ILocalizedText SHIELD_TOGGLE = createAdvanced("divine_shield.toggle");
    public static final ILocalizedText EXPLOSIVES_TITLE = createSimple("dynamite_carrier.title");

    private static final String HEADER = "steves_vehicles:gui.common.independence:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationIndependence() {}
}
