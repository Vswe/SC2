package vswe.stevesvehicles.localization.entry.arcade;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationSweeper {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText MAP_TINY = createSimple("map_tiny");
    public static final ILocalizedText MAP_MEDIUM = createSimple("map_medium");
    public static final ILocalizedText MAP_LARGE = createSimple("map_large");
    public static final ILocalizedText REMAINING = createAdvanced("remaining");
    public static final ILocalizedText TIME = createAdvanced("time");
    public static final ILocalizedText CHANGE_MAP = createSimple("change_map");
    public static final ILocalizedText CURRENT_MAP = createAdvanced("current_map");
    public static final ILocalizedText HIGH_SCORE_TITLE = createSimple("high_score_title");
    public static final ILocalizedText HIGH_SCORE_ENTRY = createAdvanced("high_score_entry");
    public static final ILocalizedText RESTART = createSimple("restart"); //TODO add to lang file

    private static final String HEADER = "steves_vehicles:gui.arcade.sweeper:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationSweeper() {}
}
