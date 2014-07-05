package vswe.stevesvehicles.localization.entry.gui.info;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCategory {
    public static final ILocalizedText HULL = createSimple("hull");
    public static final ILocalizedText ENGINE = createSimple("engine");
    public static final ILocalizedText TOOL = createSimple("tool");
    public static final ILocalizedText STORAGE = createSimple("storage");
    public static final ILocalizedText ADDON = createSimple("addon");
    public static final ILocalizedText ATTACHMENT = createSimple("attachment");
    public static final ILocalizedText INVALID = createSimple("invalid");


    private static final String HEADER = "steves_vehicles:gui.info.category.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationCategory() {}
}
