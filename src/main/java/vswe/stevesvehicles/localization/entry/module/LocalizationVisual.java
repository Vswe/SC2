package vswe.stevesvehicles.localization.entry.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationVisual {
    public static final ILocalizedText RED = createSimple("colorizer.red");
    public static final ILocalizedText GREEN = createSimple("colorizer.green");
    public static final ILocalizedText BLUE = createSimple("colorizer.blue");
    public static final ILocalizedText RANDOMIZE = createSimple("color_randomizer.randomizer");
    public static final ILocalizedText NAME_LABEL = createSimple("information_provider.name_label");
    public static final ILocalizedText DISTANCE_LABEL = createSimple("information_provider.distance_label");
    public static final ILocalizedText DISTANCE_MESSAGE = createAdvanced("information_provider.distance_message");
    public static final ILocalizedText POSITION_LABEL = createSimple("information_provider.position_label");
    public static final ILocalizedText POSITION_MESSAGE = createAdvanced("information_provider.position_message");
    public static final ILocalizedText FUEL_LABEL = createSimple("information_provider.fuel_label");
    public static final ILocalizedText FUEL_MESSAGE = createSimple("information_provider.fuel_message");
    public static final ILocalizedText FUEL_MESSAGE_NO_CONSUMPTION = createSimple("information_provider.fuel_message_no_consumption");
    public static final ILocalizedText STORAGE_LABEL = createSimple("information_provider.storage_label");
    public static final ILocalizedText LABELS = createSimple("information_provider.labels");
    public static final ILocalizedText INVISIBILITY_TOGGLE = createAdvanced("invisibility_core.state");
    public static final ILocalizedText PIANO = createSimple("note_sequencer.piano");
    public static final ILocalizedText BASS_DRUM = createSimple("note_sequencer.bass_drum");
    public static final ILocalizedText SNARE_DRUM = createSimple("note_sequencer.snare_drum");
    public static final ILocalizedText STICKS = createSimple("note_sequencer.sticks");
    public static final ILocalizedText BASS_GUITAR = createSimple("note_sequencer.bass_guitar");
    public static final ILocalizedText CREATE_TRACK = createSimple("note_sequencer.create_track");
    public static final ILocalizedText REMOVE_TRACK = createSimple("note_sequencer.remove_track");
    public static final ILocalizedText ACTIVATE_INSTRUMENT = createAdvanced("note_sequencer.activate_instrument");
    public static final ILocalizedText DEACTIVATE_INSTRUMENT = createSimple("note_sequencer.deactivate_instrument");
    public static final ILocalizedText DELAY = createAdvanced("note_sequencer.delay");
    public static final ILocalizedText ADD_NOTE = createAdvanced("note_sequencer.add_note");
    public static final ILocalizedText REMOVE_NOTE = createAdvanced("note_sequencer.remove_note");
    public static final ILocalizedText VOLUME = createAdvanced("note_sequencer.volume");


    private static final String HEADER = "steves_vehicles:gui.common.visual:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationVisual() {}
}
