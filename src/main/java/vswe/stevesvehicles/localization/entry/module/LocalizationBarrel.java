package vswe.stevesvehicles.localization.entry.module;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;


//TODO localize
public final class LocalizationBarrel {
    public static final ILocalizedText EMPTY = createSimple("This barrel is empty");
    public static final ILocalizedText FULL = createSimple("This barrel is completely filled");
    public static final ILocalizedText FILLED_PERCENTAGE = createAdvanced("This barrel is [%1] filled");
    public static final ILocalizedText FILLED_AMOUNT = createAdvanced("That's [%1] / [%2] items");
    public static final ILocalizedText FILLED_AMOUNT_STACKS = createAdvanced("or [%1] stacks and [%2] items");
    public static final ILocalizedText STACKS = createAdvanced("This barrel can hold [%1] stacks");
    public static final ILocalizedText STACK_SIZE = createAdvanced("With a stack size of [%1] that's [%2] items");
    public static final ILocalizedText LOCKED = createSimple("This barrel is locked");
    public static final ILocalizedText UNLOCK = createSimple("Click to unlock");
    public static final ILocalizedText LOCK = createSimple("Click to lock to this item");

    private static final String HEADER = "";//"steves_vehicles:gui.common.barrel:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationBarrel() {}
}
