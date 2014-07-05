package vswe.stevesvehicles.localization.entry.arcade;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationInvaders {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText LIVES = createSimple("lives");
    public static final ILocalizedText HIGH_SCORE = createAdvanced("high_score");
    public static final ILocalizedText SCORE = createAdvanced("score");
    public static final ILocalizedText SHOOT = createSimple("shoot");
    public static final ILocalizedText LEFT = createSimple("left");
    public static final ILocalizedText RIGHT = createSimple("right");
    public static final ILocalizedText RESTART = createSimple("restart");


    private static final String HEADER = "steves_vehicles:gui.arcade.invaders:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationInvaders() {}
}
