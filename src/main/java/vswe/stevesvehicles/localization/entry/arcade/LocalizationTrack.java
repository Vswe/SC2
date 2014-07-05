package vswe.stevesvehicles.localization.entry.arcade;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationTrack {
    public static final ILocalizedText TITLE = createSimple("title");
    public static final ILocalizedText SAVE_ERROR = createSimple("save_error");
    public static final ILocalizedText SAVE_MESSAGE = createSimple("save_message");
    public static final ILocalizedText USER_MAPS = createSimple("user_maps");
    public static final ILocalizedText STORIES = createSimple("stories");
    public static final ILocalizedText HELP = createSimple("help");
    public static final ILocalizedText TRACK_SHAPE = createSimple("track_shape");
    public static final ILocalizedText TRACK_ROTATE = createSimple("track_rotate");
    public static final ILocalizedText TRACK_FLIP = createSimple("track_flip");
    public static final ILocalizedText TRACK_DIRECTION = createSimple("track_direction");
    public static final ILocalizedText TRACK_TYPE = createSimple("track_type");
    public static final ILocalizedText TRACK_DELETE = createSimple("track_delete");
    public static final ILocalizedText TRACK_COPY = createSimple("track_copy");
    public static final ILocalizedText MOVE_STEVE = createSimple("move_steve");
    public static final ILocalizedText MOVE_MAP = createSimple("move_map");
    public static final ILocalizedText PLACE_TRACK = createSimple("place_track");
    public static final ILocalizedText DESELECT_TRACK = createSimple("deselect_track");
    public static final ILocalizedText LEFT_MOUSE = createSimple("left_mouse");
    public static final ILocalizedText RIGHT_MOUSE = createSimple("right_mouse");
    public static final ILocalizedText START = createSimple("start");
    public static final ILocalizedText MENU = createSimple("menu");
    public static final ILocalizedText STOP = createSimple("stop");
    public static final ILocalizedText NEXT_LEVEL = createSimple("next_level");
    public static final ILocalizedText START_LEVEL = createSimple("start_level");
    public static final ILocalizedText SELECT_STORY = createSimple("select_story");
    public static final ILocalizedText SELECT_OTHER_STORY = createSimple("select_other_story");
    public static final ILocalizedText CREATE_LEVEL = createSimple("create_level");
    public static final ILocalizedText EDIT_LEVEL = createSimple("edit_level");
    public static final ILocalizedText REFRESH_LIST = createSimple("refresh_list");
    public static final ILocalizedText SAVE = createSimple("save");
    public static final ILocalizedText SAVE_AS = createSimple("save_as");
    public static final ILocalizedText CANCEL = createSimple("cancel");
    public static final ILocalizedText EDITOR_TITLE = createSimple("editor_title");

    private static final String HEADER = "steves_vehicles:gui.arcade.track:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }

    private LocalizationTrack() {}
}
