package vswe.stevesvehicles.localization.entry.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCake {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText CAKES_LABEL = createAdvanced("cakes_label");
    public static final ILocalizedText SLICES_LABEL = createAdvanced("slices_label");

    private static final String HEADER = "steves_vehicles:gui.common.cake:common.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationCake() {}
}
