package vswe.stevesvehicles.old.Helpers;


import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.localization.ILocalizedText;

//TODO get rid of this class and use PlainText, LocalizedTextSimple and LocalizedTextAdvanced. Needs changes in the whole project.

public final class Localization {
    private Localization() {}


    public static enum ARCADE implements ILocalizedText {
        GHAST("ghastInvaders"),
        EXTRA_LIVES("ghastLives"),
        HIGH_SCORE("highScore"),
        SCORE("score"),
        INSTRUCTION_SHOOT("instructionShoot"),
        INSTRUCTION_LEFT("instructionLeft"),
        INSTRUCTION_RIGHT("instructionRight"),
        INSTRUCTION_RESTART("instructionRestart"),
        CREEPER("creeperSweeper"),
        MAP_1("creeperMapName1"),
        MAP_2("creeperMapName2"),
        MAP_3("creeperMapName3"),
        LEFT("creepersLeft"),
        TIME("creeperTime"),
        INSTRUCTION_CHANGE_MAP("instructionChangeMap"),
        MAP("creeperCurrentMap"),
        HIGH_SCORES("creeperHighScores"),
        HIGH_SCORE_ENTRY("creeperHighScore"),
        STACKER("mobStacker"),
        REMOVED_LINES("stackerRemovedLines"),
        REMOVED_LINES_COMBO("stackerRemovedLinesCombo"),
        INSTRUCTION_ROTATE("instructionRotate"),
        INSTRUCTION_DROP("instructionDrop"),
        OPERATOR("trackOperator"),
        SAVE_ERROR("operatorSaveError"),
        SAVE("operatorSave"),
        USER_MAPS("operatorUserCreatedMaps"),
        STORIES("operatorStories"),
        HELP("operatorHelp"),
        INSTRUCTION_SHAPE("instructionTrackShape"),
        INSTRUCTION_ROTATE_TRACK("instructionRotateTrack"),
        INSTRUCTION_FLIP_TRACK("instructionFlipTrack"),
        INSTRUCTION_DEFAULT_DIRECTION("instructionDefaultDirection"),
        INSTRUCTION_TRACK_TYPE("instructionTrackType"),
        INSTRUCTION_DELETE_TRACK("instructionDeleteTrack"),
        INSTRUCTION_COPY_TRACK("instructionCopyTrack"),
        INSTRUCTION_STEVE("instructionMoveSteve"),
        INSTRUCTION_MAP("instructionMoveMap"),
        INSTRUCTION_PLACE_TRACK("instructionPlaceTrack"),
        INSTRUCTION_DESELECT_TRACK("instructionDeselectTrack"),
        LEFT_MOUSE("leftMouseButton"),
        RIGHT_MOUSE("rightMouseButton"),
        BUTTON_START("buttonStart"),
        BUTTON_MENU("buttonMenu"),
        BUTTON_STOP("buttonStop"),
        BUTTON_NEXT("buttonNextLevel"),
        BUTTON_START_LEVEL("buttonStartLevel"),
        BUTTON_SELECT_STORY("buttonSelectStory"),
        BUTTON_SELECT_OTHER_STORY("buttonSelectStoryOther"),
        BUTTON_CREATE_LEVEL("buttonCreateLevel"),
        BUTTON_EDIT_LEVEL("buttonEditLevel"),
        BUTTON_REFRESH("buttonRefreshList"),
        BUTTON_SAVE("buttonSave"),
        BUTTON_SAVE_AS("buttonSaveAs"),
        BUTTON_CANCEL("buttonCancel"),


        MADNESS("forgecraftMadness");


        private String name;
        ARCADE(String name) {
            this.name = name;
        }

        public String translate(String ... vars) {
            return doTranslate("arcade.SC2:" + name, vars);
        }


    }

    public static class STORIES {
        public static enum THE_BEGINNING implements ILocalizedText {
            MAP_EDITOR("mapEditor"),
            TITLE("title"),
            MISSION("mission"),
            START("start"),
            STOP("stop"),
            MAP("map"),
            TRACK_OPERATOR("trackOperator"),
            GOOD_JOB("goodJob"),
            CHANGE_JUNCTIONS("changeJunctions"),
            BLAST("blast"),
            STEEL("steel"),
            DETECTOR("detector"),
            OUT_OF_REACH("outOfReach"),
            OUT_OF_REACH_2("outOfReach2"),
            LONG_JOURNEY("longJourney"),
            END("end"),
            THANKS("thanks"),
            LEVEL_1("level1"),
            LEVEL_2("level2"),
            LEVEL_3("level3"),
            LEVEL_4("level4"),
            LEVEL_5("level5"),
            LEVEL_6("level6"),
            LEVEL_7("level7"),
            LEVEL_8("level8"),
            LEVEL_9("level9");





            private String name;
            THE_BEGINNING(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("stories.beginning.SC2:" + name, vars);
            }
        }
    }

    private static String doTranslate(String name, String...vars) {
        String result = StatCollector.translateToLocal(name);
        for (int i = 0; i < vars.length; i++) {
            String pluralCheck = "[%" + (i + 1) + ":";
            int index = result.indexOf(pluralCheck);
            if (index != -1) {
                int endIndex = result.indexOf("]", index);
                if (endIndex != -1) {
                    String optionsStr = result.substring(index + pluralCheck.length(), endIndex);
                    String options[] = optionsStr.split("\\|");

                    int optionId = (vars[i].equals("1") || vars[i].equals("-1")) ? 0 : 1;
                    if (optionId >= 0 && optionId < options.length) {
                        String option = options[optionId];
                        result = result.substring(0, index) + option + result.substring(endIndex + 1);

                        //restart
                        i--;
                    }
                }
            }else{
                String listCheck = "[%" + (i + 1) + "->";
                int index2 = result.indexOf(listCheck);
                if (index2 != -1) {
                    int endIndex = result.indexOf("]", index2);
                    if (endIndex != -1) {
                        String optionsStr = result.substring(index2 + listCheck.length(), endIndex);
                        String options[] = optionsStr.split("\\|");

                        int optionId = Integer.parseInt(vars[i]);
                        if (optionId >= 0 && optionId < options.length) {
                            String option = options[optionId];
                            result = result.substring(0, index2) + option + result.substring(endIndex + 1);

                            //restart
                            i--;
                        }
                    }
                }else{
                    result = result.replace("[%" + (i + 1) + "]", vars[i]);
                }
            }
        }
        return result;
    }
}
