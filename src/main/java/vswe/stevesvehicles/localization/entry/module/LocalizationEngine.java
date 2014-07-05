package vswe.stevesvehicles.localization.entry.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationEngine {
    public static final ILocalizedText CREATIVE_POWER = createAdvanced("creative_engine.power");
    public static final ILocalizedText COAL_TITLE = createSimple("coal_engine.title");
    public static final ILocalizedText COAL_NO_POWER = createSimple("coal_engine.no_power");
    public static final ILocalizedText COAL_POWER = createAdvanced("coal_engine.power");
    public static final ILocalizedText SOLAR_TITLE = createSimple("solar_engine.title");
    public static final ILocalizedText SOLAR_NO_POWER = createSimple("solar_engine.no_power");
    public static final ILocalizedText SOLAR_POWER = createAdvanced("solar_engine.power");
    public static final ILocalizedText THERMAL_TITLE = createSimple("thermal_engine.title");
    public static final ILocalizedText THERMAL_POWERED = createSimple("thermal_engine.powered");
    public static final ILocalizedText THERMAL_NO_WATER = createSimple("thermal_engine.no_water");
    public static final ILocalizedText THERMAL_NO_LAVA = createSimple("thermal_engine.no_lava");
    public static final ILocalizedText DISABLED = createSimple("common.disabled");
    public static final ILocalizedText PRIORITY = createAdvanced("common.priority");

    private static final String HEADER = "steves_vehicles:gui.common.engines:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationEngine() {}
}
