package vswe.stevesvehicles.localization.entry.gui.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationToggler {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText DRILL_OPTION = createSimple("drill_option");
    public static final ILocalizedText SHIELD_OPTION = createSimple("shield_option");
    public static final ILocalizedText INVISIBILITY_OPTION = createSimple("invisibility_option");
    public static final ILocalizedText CHUNK_OPTION = createSimple("chunk_option");
    public static final ILocalizedText AUTO_CAGE_OPTION = createSimple("auto_cage_option");
    public static final ILocalizedText CAGE_OPTION = createSimple("cage_option");
    public static final ILocalizedText DISABLED_SETTING = createSimple("disabled_setting");
    public static final ILocalizedText ORANGE_SETTING = createSimple("orange_setting");
    public static final ILocalizedText BLUE_SETTING = createSimple("blue_setting");
    public static final ILocalizedText ACTIVATE_STATE = createSimple("activate_state");
    public static final ILocalizedText DEACTIVATE_STATE = createSimple("deactivate_state");
    public static final ILocalizedText TOGGLE_STATE = createSimple("toggle_state");

    private static final String HEADER = "steves_vehicles:gui.toggler.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationToggler() {}
}
