package vswe.stevesvehicles.old.Helpers;


import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.localization.ILocalizedText;

//TODO get rid of this class and use PlainText, LocalizedTextSimple and LocalizedTextAdvanced. Needs changes in the whole project.

public final class Localization {
    private Localization() {}



    public static class MODULES {

        public static enum ADDONS implements ILocalizedText {
            BUTTON_RANDOMIZE("buttonRandomize"),
            DETECTOR_ANIMALS("detectorAnimals"),
            DETECTOR_BATS("detectorBats"),
            DETECTOR_MONSTERS("detectorMonsters"),
            DETECTOR_PLAYERS("detectorPlayers"),
            DETECTOR_VILLAGERS("detectorVillagers"),
            PLANTER_RANGE("planterRangeExtenderTitle"),
            SAPLING_AMOUNT("saplingPlantAmount") ,
            CONTROL_LEVER("controlLeverTitle") ,
            LEVER_START("leverStartCart"),
            LEVER_STOP("leverStopCart"),
            LEVER_TURN("leverTurnAroundCart"),
            COLOR_RED("colorizerRgbRed"),
            COLOR_GREEN("colorizerRgbGreen"),
            COLOR_BLUE("colorizerRgbBlue"),
            LOCKED("intelligenceLockedBlock"),
            CHANGE_INTELLIGENCE("intelligenceChange"),
            CURRENT_INTELLIGENCE("intelligenceCurrent"),
            ENCHANT_INSTRUCTION("enchanterInstruction"),
            INVISIBILITY("invisibilityToggle"),
            NAME("informationProviderLabelName"),
            DISTANCE("informationProviderLabelDistance"),
            DISTANCE_LONG("informationProviderMessageDistance"),
            POSITION("informationProviderLabelPosition"),
            POSITION_LONG("informationProviderMessagePosition"),
            FUEL("informationProviderLabelFuel"),
            FUEL_LONG("informationProviderMessageFuel"),
            FUEL_NO_CONSUMPTION("informationProviderMessageNoConsumption"),
            STORAGE("informationProviderLabelStorage"),
            LABELS("informationProviderLabels"),
            DURABILITY("informationProviderLabelDurability"),
            BROKEN("informationProviderMessageToolBroken"),
            NOT_BROKEN("informationProviderMessageToolNotBroken"),
            REPAIR("informationProviderMessageRepair"),
            UNBREAKABLE("informationProviderMessageUnbreakable"),
            K("powerThousandSuffix"),
            OBSERVER_INSTRUCTION("powerObserverInstruction"),
            OBSERVER_REMOVE("powerObserverRemoveInstruction"),
            OBSERVER_DROP("powerObserverDropInstruction"),
            OBSERVER_CHANGE("powerObserverChangeInstruction"),
            OBSERVER_CHANGE_10("powerObserverChangeInstruction10"),
            RECIPE_OUTPUT("recipeOutput"),
            CURRENT("recipeCurrentSelection"),
            INVALID_OUTPUT("recipeInvalidOutput"),
            RECIPE_MODE("recipeChangeMode"),
            RECIPE_NO_LIMIT("recipeNoLimit"),
            RECIPE_LIMIT("recipeLimit"),
            RECIPE_DISABLED("recipeDisabled"),
            RECIPE_CHANGE_AMOUNT("recipeChangeLimit"),
            RECIPE_CHANGE_AMOUNT_10("recipeChangeLimit10"),
            RECIPE_CHANGE_AMOUNT_64("recipeChangeLimit64"),
            SHIELD("shieldToggle");

            private String name;
            ADDONS(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("modules.addons.SC2:" + name, vars);
            }
        }

        public static enum ENGINES implements ILocalizedText {
            OVER_9000("creativePowerLevel"),
            COAL("coalEngineTitle"),
            NO_FUEL("outOfFuel"),
            FUEL("fuelLevel"),
            SOLAR("solarEngineTitle"),
            NO_POWER("outOfPower"),
            POWER("powerLevel"),
            THERMAL("thermalEngineTitle"),
            POWERED("thermalPowered"),
            NO_WATER("outOfWater"),
            NO_LAVA("outOfLava"),
            ENGINE_DISABLED("engineDisabledMessage"),
            ENGINE_PRIORITY("enginePriorityMessage");




            private String name;
            ENGINES(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("modules.engines.SC2:" + name, vars);
            }
        }

        public static enum TANKS implements ILocalizedText {
            CREATIVE_MODE("creativeTankMode"),
            CHANGE_MODE("creativeTankChangeMode"),
            RESET_MODE("creativeTankResetMode"),
            LOCKED("tankLocked"),
            LOCK("tankLock"),
            UNLOCK("tankUnlock"),
            EMPTY("tankEmpty"),
            INVALID("tankInvalidFluid");

            private String name;
            TANKS(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("modules.tanks.SC2:" + name, vars);
            }
        }

        public static enum TOOLS implements ILocalizedText {
            DURABILITY("toolDurability"),
            BROKEN("toolBroken"),
            REPAIRING("toolRepairing"),
            DECENT("toolDecent"),
            INSTRUCTION("toolRepairInstruction"),
            UNBREAKABLE("toolUnbreakable"),
            UNBREAKABLE_REPAIR("toolUnbreakableRepairError"),
            DRILL("drillTitle"),
            TOGGLE("drillToggle"),
            DIAMONDS("repairDiamonds"),
            IRON("repairIron"),
            FARMER("farmerTitle"),
            CUTTER("cutterTitle");


            private String name;
            TOOLS(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("modules.tools.SC2:" + name, vars);
            }
        }

        public static enum ATTACHMENTS implements ILocalizedText {
            FERTILIZERS("fertilizers"),
            RAILER("railerTitle"),
            CONTROL_SYSTEM("controlSystemTitle"),
            DISTANCES("controlSystemDistanceUnits"),
            ODO("controlSystemOdoMeter"),
            TRIP("controlSystemTripMeter"),
            CAGE_AUTO("cageAutoPickUp"),
            CAGE("cagePickUp"),
            CAKE_SERVER("cakeServerTitle"),
            CAKES("cakesLabel"),
            SLICES("slicesLabel"),
            EXPLOSIVES("explosivesTitle"),
            EXPERIENCE("experienceTitle"),
            EXPERIENCE_LEVEL("experienceLevel"),
            EXPERIENCE_EXTRACT("experienceExtract"),
            EXPERIENCE_PLAYER_LEVEL("experiencePlayerLevel"),
            SHOOTER("shooterTitle"),
            FREQUENCY("shooterFrequency"),
            SECONDS("shooterSeconds"),
            DELAY("shooterDelay"),
            PIANO("notePiano"),
            BASS_DRUM("noteBassDrum"),
            SNARE_DRUM("noteSnareDrum"),
            STICKS("noteSticks"),
            BASS_GUITAR("noteBassGuitar"),
            CREATE_TRACK("noteCreateTrack"),
            REMOVE_TRACK("noteRemoveTrack"),
            ACTIVATE_INSTRUMENT("noteActivateInstrument"),
            DEACTIVATE_INSTRUMENT("noteDeactivateInstrument"),
            NOTE_DELAY("noteDelay"),
            ADD_NOTE("noteAdd"),
            REMOVE_NOTE("noteRemove"),
            VOLUME("noteVolume"),
            SEAT_MESSAGE("seatStateMessage"),
            CONTROL_RESET("controlSystemReset");







            private String name;
            ATTACHMENTS(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("modules.attachments.SC2:" + name, vars);
            }
        }

    }


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
