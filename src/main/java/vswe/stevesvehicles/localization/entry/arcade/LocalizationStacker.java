package vswe.stevesvehicles.localization.entry.arcade;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationStacker {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText REMOVED = createAdvanced("removed");
    public static final ILocalizedText REMOVED_COMBO = createAdvanced("removed_combo");
    public static final ILocalizedText ROTATE = createSimple("rotate");
    public static final ILocalizedText DROP = createSimple("drop");
    public static final ILocalizedText HIGH_SCORE = createAdvanced("high_score");
    public static final ILocalizedText SCORE = createAdvanced("score");
    public static final ILocalizedText RIGHT = createSimple("right");
    public static final ILocalizedText LEFT = createSimple("left");
    public static final ILocalizedText RESET = createSimple("reset");

    private static final String HEADER = "steves_vehicles:gui.arcade.stacker:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationStacker() {}
}
