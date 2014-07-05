package vswe.stevesvehicles.localization.entry.gui.module.cart;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCartTool {
    public static final ILocalizedText DRILL = createSimple("drill.title");
    public static final ILocalizedText DRILL_TOGGLE = createAdvanced("drill.toggle");
    public static final ILocalizedText FARMER = createSimple("farmer.title");
    public static final ILocalizedText CUTTER = createSimple("wood_cutter.title");

    private static final String HEADER = "steves_vehicles:gui.cart.tools:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationCartTool() {}
}
