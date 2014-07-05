package vswe.stevesvehicles.localization.entry.arcade.stories;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationTheBeginning {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText MISSION = createSimple("mission");
    public static final ILocalizedText START = createSimple("start");
    public static final ILocalizedText STOP = createSimple("stop");
    public static final ILocalizedText MAP = createSimple("map");
    public static final ILocalizedText TRACK_OPERATOR = createSimple("track_operator");
    public static final ILocalizedText GOOD_JOB = createSimple("good_job");
    public static final ILocalizedText CHANGE_JUNCTION = createSimple("change_junction");
    public static final ILocalizedText BLAST = createSimple("blast");
    public static final ILocalizedText STEEL = createSimple("steel");
    public static final ILocalizedText DETECTOR = createSimple("detector");
    public static final ILocalizedText OUT_OF_REACH = createSimple("out_of_reach");
    public static final ILocalizedText OUT_OF_REACH_2 = createSimple("out_of_reach_2");
    public static final ILocalizedText LONG_JOURNEY = createSimple("long_journey");
    public static final ILocalizedText END = createSimple("end");
    public static final ILocalizedText THANKS = createSimple("thanks");
    public static final ILocalizedText LEVEL_1 = createSimple("level_1");
    public static final ILocalizedText LEVEL_2 = createSimple("level_2");
    public static final ILocalizedText LEVEL_3 = createSimple("level_3");
    public static final ILocalizedText LEVEL_4 = createSimple("level_4");
    public static final ILocalizedText LEVEL_5 = createSimple("level_5");
    public static final ILocalizedText LEVEL_6 = createSimple("level_6");
    public static final ILocalizedText LEVEL_7 = createSimple("level_7");
    public static final ILocalizedText LEVEL_8 = createSimple("level_8");
    public static final ILocalizedText LEVEL_9 = createSimple("level_9");


    private static final String HEADER = "steves_vehicles:gui.arcade.track.stories:beginning.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationTheBeginning() {}
}
