package vswe.stevesvehicles.localization.entry.gui.block;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationDetector {
    public static final ILocalizedText OUTPUT = createSimple("output");
    public static final ILocalizedText AND = createSimple("and");
    public static final ILocalizedText OR = createSimple("or");
    public static final ILocalizedText NOT = createSimple("not");
    public static final ILocalizedText XOR = createSimple("xor");
    public static final ILocalizedText TOP_UNIT = createSimple("top_unit");
    public static final ILocalizedText BOTTOM_UNIT = createSimple("bottom_unit");
    public static final ILocalizedText NORTH_UNIT = createSimple("north_unit");
    public static final ILocalizedText WEST_UNIT = createSimple("west_unit");
    public static final ILocalizedText SOUTH_UNIT = createSimple("south_unit");
    public static final ILocalizedText EAST_UNIT = createSimple("east_unit");
    public static final ILocalizedText REDSTONE = createSimple("redstone");
    public static final ILocalizedText TOP_REDSTONE = createSimple("top_redstone");
    public static final ILocalizedText BOTTOM_REDSTONE = createSimple("bottom_redstone");
    public static final ILocalizedText NORTH_REDSTONE = createSimple("north_redstone");
    public static final ILocalizedText WEST_REDSTONE = createSimple("west_redstone");
    public static final ILocalizedText SOUTH_REDSTONE = createSimple("south_redstone");
    public static final ILocalizedText EAST_REDSTONE = createSimple("east_redstone");
    public static final ILocalizedText RAIL_SUPPLIES = createSimple("rail_supplies");
    public static final ILocalizedText TORCH_SUPPLIES = createSimple("torch_supplies");
    public static final ILocalizedText SAPLING_SUPPLIES = createSimple("sapling_supplies");
    public static final ILocalizedText SEED_SUPPLIES = createSimple("seed_supplies");
    public static final ILocalizedText BRIDGE_SUPPLIES = createSimple("bridge_supplies");
    public static final ILocalizedText PROJECTILE_SUPPLIES = createSimple("projectile_supplies");
    public static final ILocalizedText FERTILIZER_SUPPLIES = createSimple("fertilizer_supplies");
    public static final ILocalizedText CAKE_SUPPLIES = createSimple("cake_supplies");
    public static final ILocalizedText SHIELD_ACTIVE = createSimple("shield_active");
    public static final ILocalizedText CHUNK_ACTIVE = createSimple("chunk_active");
    public static final ILocalizedText INVISIBILITY_ACTIVE = createSimple("invisibility_active");
    public static final ILocalizedText DRILL_ACTIVE = createSimple("drill_active");
    public static final ILocalizedText CAGE_ACTIVE = createSimple("cage_active");
    public static final ILocalizedText STORAGE_FULL = createSimple("storage_full");
    public static final ILocalizedText STORAGE_EMPTY = createSimple("storage_empty");
    public static final ILocalizedText PASSENGER = createSimple("passenger");
    public static final ILocalizedText ANIMAL_PASSENGER = createSimple("animal_passenger");
    public static final ILocalizedText TAMEABLE_PASSENGER = createSimple("tameable_passenger");
    public static final ILocalizedText BREEDABLE_PASSENGER = createSimple("breedable_passenger");
    public static final ILocalizedText HOSTILE_PASSENGER = createSimple("hostile_passenger");
    public static final ILocalizedText CREEPER_PASSENGER = createSimple("creeper_passenger");
    public static final ILocalizedText SKELETON_PASSENGER = createSimple("skeleton_passenger");
    public static final ILocalizedText SPIDER_PASSENGER = createSimple("spider_passenger");
    public static final ILocalizedText ZOMBIE_PASSENGER = createSimple("zombie_passenger");
    public static final ILocalizedText ZOMBIE_PIG_MAN_PASSENGER = createSimple("zombie_pig_man_passenger");
    public static final ILocalizedText SILVERFISH_PASSENGER = createSimple("silverfish_passenger");
    public static final ILocalizedText BLAZE_PASSENGER = createSimple("blaze_passenger");
    public static final ILocalizedText BAT_PASSENGER = createSimple("bat_passenger");
    public static final ILocalizedText WITCH_PASSENGER = createSimple("witch_passenger");
    public static final ILocalizedText PIG_PASSENGER = createSimple("pig_passenger");
    public static final ILocalizedText SHEEP_PASSENGER = createSimple("sheep_passenger");
    public static final ILocalizedText COW_PASSENGER = createSimple("cow_passenger");
    public static final ILocalizedText MOOSHROOM_PASSENGER = createSimple("mooshroom_passenger");
    public static final ILocalizedText CHICKEN_PASSENGER = createSimple("chicken_passenger");
    public static final ILocalizedText WOLF_PASSENGER = createSimple("wolf_passenger");
    public static final ILocalizedText SNOW_GOLEM_PASSENGER = createSimple("snow_golem_passenger");
    public static final ILocalizedText OCELOT_PASSENGER = createSimple("ocelot_passenger");
    public static final ILocalizedText VILLAGER_PASSENGER = createSimple("villager_passenger");
    public static final ILocalizedText PLAYER_PASSENGER = createSimple("player_passenger");
    public static final ILocalizedText ZOMBIE_VILLAGE_PASSENGER = createSimple("zombie_village_passenger");
    public static final ILocalizedText CHILD_PASSENGER = createSimple("child_passenger");
    public static final ILocalizedText TAMED_PASSENGER = createSimple("tamed_passenger");
    public static final ILocalizedText RED_OBSERVER = createSimple("red_observer");
    public static final ILocalizedText BLUE_OBSERVER = createSimple("blue_observer");
    public static final ILocalizedText GREEN_OBSERVER = createSimple("green_observer");
    public static final ILocalizedText YELLOW_OBSERVER = createSimple("yellow_observer");
    public static final ILocalizedText TANKS_FULL = createSimple("tanks_full");
    public static final ILocalizedText TANKS_EMPTY = createSimple("tanks_empty");
    public static final ILocalizedText TANKS_SPARE = createSimple("tanks_spare");

    private static final String HEADER = "steves_vehicles:gui.detector.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationDetector() {}
}
