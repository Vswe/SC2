package vswe.stevesvehicles.localization.entry.module;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;



public final class LocalizationBarrel {
    public static final ILocalizedText EMPTY = createSimple("empty");
    public static final ILocalizedText FULL = createSimple("full");
    public static final ILocalizedText FILLED_PERCENTAGE = createAdvanced("filled_percentage");
    public static final ILocalizedText FILLED_AMOUNT = createAdvanced("filled_amount");
    public static final ILocalizedText FILLED_AMOUNT_STACKS = createAdvanced("filled_amount_stacks");
    public static final ILocalizedText STACKS = createAdvanced("stacks");
    public static final ILocalizedText STACK_SIZE = createAdvanced("stack_size");
    public static final ILocalizedText LOCKED = createSimple("locked");
    public static final ILocalizedText UNLOCK = createSimple("unlock");
    public static final ILocalizedText LOCK = createSimple("lock");

    private static final String HEADER = "steves_vehicles:gui.common.barrel:common.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationBarrel() {}
}
