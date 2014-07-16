package vswe.stevesvehicles.localization.entry.module.cart;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationCartRails {
    public static final ILocalizedText TITLE = createSimple("railer.title");
    public static final ILocalizedText TORCH = createSimple("torch_placer.light");
    public static final ILocalizedText MOVE_TARGET = createSimple("height_controller.move_target");
    public static final ILocalizedText MOVE_TARGET_TEN = createSimple("height_controller.move_target_ten");
    public static final ILocalizedText RESET_TARGET = createSimple("height_controller.reset_target");

    private static final String HEADER = "steves_vehicles:gui.cart.rails:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationCartRails() {}
}
