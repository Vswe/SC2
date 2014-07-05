package vswe.stevesvehicles.localization.entry.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationShooter {
    public static final ILocalizedText ANIMAL_TITLE = createSimple("entity_detector_animal.title");
    public static final ILocalizedText PLAYER_TITLE = createSimple("entity_detector_player.title");
    public static final ILocalizedText VILLAGER_TITLE = createSimple("entity_detector_villager.title");
    public static final ILocalizedText MONSTER_TITLE = createSimple("entity_detector_monster.title");
    public static final ILocalizedText BAT_TITLE = createSimple("entity_detector_bat.title");
    public static final ILocalizedText SHOOTER_TITLE = createSimple("shooter.title");
    public static final ILocalizedText SHOOTER_FREQUENCY = createSimple("shooter.frequency");
    public static final ILocalizedText SHOOTER_SECONDS = createSimple("shooter.seconds");
    public static final ILocalizedText SHOOTER_DELAY = createSimple("shooter.delay");

    private static final String HEADER = "steves_vehicles:gui.common.shooters:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationShooter() {}
}
