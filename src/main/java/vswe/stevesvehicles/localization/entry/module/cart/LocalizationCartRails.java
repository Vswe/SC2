package vswe.stevesvehicles.localization.entry.module.cart;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCartRails {
    public static final ILocalizedText TITLE = createSimple("title");

    private static final String HEADER = "steves_vehicles:gui.cart.rails:railer.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationCartRails() {}
}
