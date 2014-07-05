package vswe.stevesvehicles.localization.entry.gui.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationUtility {
    public static final ILocalizedText THOUSAND_SUFFIX = createSimple("power_observer.thousand_suffix");
    public static final ILocalizedText OBSERVER_INSTRUCTION = createSimple("power_observer.instruction");
    public static final ILocalizedText OBSERVER_REMOVE_INSTRUCTION = createSimple("power_observer.remove_instruction");
    public static final ILocalizedText OBSERVER_DROP_INSTRUCTION = createSimple("power_observer.drop_instruction");
    public static final ILocalizedText OBSERVER_CHANGE_INSTRUCTION = createSimple("power_observer.change_instruction");
    public static final ILocalizedText OBSERVER_CHANGE_INSTRUCTION_TEN = createSimple("power_observer.change_instruction_ten");
    public static final ILocalizedText ENCHANTER_INSTRUCTION = createSimple("enchanter.instruction");

    private static final String HEADER = "steves_vehicles:gui.common.util:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationUtility() {}
}
