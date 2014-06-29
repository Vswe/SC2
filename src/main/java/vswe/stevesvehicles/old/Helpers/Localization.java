package vswe.stevesvehicles.old.Helpers;


import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.localization.ILocalizedText;

//TODO get rid of this class and use PlainText, LocalizedTextSimple and LocalizedTextAdvanced. Needs changes in the whole project.

public final class Localization {
    private Localization() {}

    public static class GUI {

        public static enum ASSEMBLER implements ILocalizedText {
            TITLE("cartAssembler"),
            ASSEMBLE_INSTRUCTION("basicAssembleInstruction"),
            INVALID_HULL("invalidHullError"),
            HULL_CAPACITY("hullCapacity"),
            COMPLEXITY_CAP("complexityCap"),
            TOTAL_COST("totalCost"),
            TOTAl_TIME("totalTime"),
            NO_ERROR("readyMessage"),
            ASSEMBLE_PROGRESS("assembleProgress"),
            TIME_LEFT("timeLeft"),
            IDLE_MESSAGE("idleAssemblerMessage"),
            MODIFY_CART("modifyCart"),
            ASSEMBLE_CART("assembleCart"),
            FUEL_LEVEL("fuelLevel"),
            HULL_ERROR("noHullError"),
            INVALID_HULL_SHORT("invalidHullErrorShort"),
            BUSY("busyAssemblerError"),
            DEPARTURE_BAY("departureBayError");

            private String name;
            ASSEMBLER(String name) {
                this.name = name;
            }



            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }

        public static enum MANAGER implements ILocalizedText {
            TITLE("manager"),
            CURRENT_SETTING("currentSetting"),
            CHANGE_TRANSFER_DIRECTION("changeTransferDirection"),
            DIRECTION_TO_CART("directionToCart"),
            DIRECTION_FROM_CART("directionFromCart"),
            CHANGE_TURN_BACK_SETTING("changeTurnBack"),
            TURN_BACK_NOT_SELECTED("turnBackDisabled"),
            TURN_BACK_DO("turnBack"),
            TURN_BACK_DO_NOT("continueForward"),
            CHANGE_TRANSFER_SIZE("changeTransferSize"),
            CHANGE_SIDE("changeSide"),
            CURRENT_SIDE("currentSide"),
            SIDE_RED("sideRed"),
            SIDE_BLUE("sideBlue"),
            SIDE_YELLOW("sideYellow"),
            SIDE_GREEN("sideGreen"),
            SIDE_DISABLED("sideDisabled");


            private String name;
            MANAGER(String name) {
                this.name = name;
            }


            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

        public static enum CARGO implements ILocalizedText {
            TITLE("cargoManager"),
            CHANGE_SLOT_LAYOUT("changeSlotLayout"),
            LAYOUT_SHARED("layoutShared"),
            LAYOUT_SIDE("layoutSide"),
            LAYOUT_COLOR("layoutColor"),
            TRANSFER_ALL("transferAll"),
            TRANSFER_ITEMS("transferItems"),
            TRANSFER_STACKS("transferStacks"),
            TRANSFER_ALL_SHORT("transferAllShort"),
            TRANSFER_ITEMS_SHORT("transferItemsShort"),
            TRANSFER_STACKS_SHORT("transferStacksShort"),
            CHANGE_STORAGE_AREA("changeTransferCartArea"),
            UNKNOWN_AREA("unknownAreaMessage"),
            AREA_ALL("cartAreaAll"),
            AREA_ENGINE("cartAreaEngine"),
            AREA_RAILER("cartAreaRailer"),
            AREA_STORAGE("cartAreaStorage"),
            AREA_TORCHES("cartAreaTorches"),
            AREA_EXPLOSIVES("cartAreaExplosives"),
            AREA_ARROWS("cartAreaArrows"),
            AREA_BRIDGE("cartAreaBridge"),
            AREA_SEEDS("cartAreaSeeds"),
            AREA_FERTILIZER("cartAreaFertilizer"),
            AREA_SAPLINGS("cartAreaSaplings"),
            AREA_FIREWORK("cartAreaFirework"),
            AREA_BUCKETS("cartAreaBuckets"),
            AREA_CAKES("cartAreaCakes");





            private String name;
            CARGO(String name) {
                this.name = name;
            }


            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

        public static enum LIQUID implements ILocalizedText {
            TITLE("liquidManager"),
            CHANGE_LAYOUT("changeTankLayout"),
            LAYOUT_ALL("layoutSharedTanks"),
            LAYOUT_SIDE("layoutSidedTanks"),
            LAYOUT_COLOR("layoutColorTanks"),
            TRANSFER_ALL("transferAllLiquid"),
            TRANSFER_BUCKETS("transferBuckets"),
            TRANSFER_ALL_SHORT("transferAllLiquidShort"),
            TRANSFER_BUCKET_SHORT("transferBucketShort");


            private String name;
            LIQUID(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }


        public static enum TOGGLER implements ILocalizedText {
            TITLE("moduleToggler"),
            OPTION_DRILL("optionDrill"),
            OPTION_SHIELD("optionShield"),
            OPTION_INVISIBILITY("optionInvisibility"),
            OPTION_CHUNK("optionChunk"),
            OPTION_CAGE_AUTO("optionCageAuto"),
            OPTION_CAGE("optionCage"),
            SETTING_DISABLED("settingDisabled"),
            SETTING_ORANGE("settingOrange"),
            SETTING_BLUE("settingBlue"),
            STATE_ACTIVATE("stateActivate"),
            STATE_DEACTIVATE("stateDeactivate"),
            STATE_TOGGLE("stateToggle");


            private String name;
            TOGGLER(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

        public static enum DISTRIBUTOR implements ILocalizedText {
            TITLE("externalDistributor"),
            NOT_CONNECTED("distributorNotConnected"),
            SIDE("sideName"),
            DROP_INSTRUCTION("dropInstruction"),
            REMOVE_INSTRUCTION("removeInstruction"),
            SETTING_ALL("distributorAll"),
            SETTING_RED("distributorRed"),
            SETTING_BLUE("distributorBlue"),
            SETTING_YELLOW("distributorYellow"),
            SETTING_GREEN("distributorGreen"),
            SETTING_TOP_LEFT("distributorTopLeft"),
            SETTING_TOP_RIGHT("distributorTopRight"),
            SETTING_BOTTOM_LEFT("distributorBottomLeft"),
            SETTING_BOTTOM_RIGHT("distributorBottomRight"),
            SETTING_TO_CART("distributorToCart"),
            SETTING_FROM_CART("distributorFromCart"),
            MANAGER_TOP("managerTop"),
            MANAGER_BOT("managerBot"),
            SIDE_ORANGE("distributorSideOrange"),
            SIDE_PURPLE("distributorSidePurple"),
            SIDE_YELLOW("distributorSideYellow"),
            SIDE_GREEN("distributorSideGreen"),
            SIDE_BLUE("distributorSideBlue"),
            SIDE_RED("distributorSideRed"),
            SIDE_TOOL_TIP("sideToolTip");



            private String name;
            DISTRIBUTOR(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

        public static enum DETECTOR implements ILocalizedText {
            OUTPUT("operatorOutput"),
            AND("operatorAnd"),
            OR("operatorOr"),
            NOT("operatorNot"),
            XOR("operatorXor"),
            TOP("operatorTopUnit"),
            BOT("operatorBotUnit"),
            NORTH("operatorNorthUnit"),
            WEST("operatorWestUnit"),
            SOUTH("operatorSouthUnit"),
            EAST("operatorEastUnit"),
            REDSTONE("operatorRedstone"),
            REDSTONE_TOP("operatorRedstoneTop"),
            REDSTONE_BOT("operatorRedstoneBot"),
            REDSTONE_NORTH("operatorRedstoneNorth"),
            REDSTONE_WEST("operatorRedstoneWest"),
            REDSTONE_SOUTH("operatorRedstoneSouth"),
            REDSTONE_EAST("operatorRedstoneEast"),
            RAIL("stateRails"),
            TORCH("stateTorches"),
            SAPLING("stateSaplings"),
            SEED("sateSeeds"),
            BRIDGE("stateBridge"),
            PROJECTILE("stateProjectiles"),
            FERTILIZING("stateFertilizing"),
            SHIELD("stateShield"),
            CHUNK("stateChunk"),
            INVISIBILITY("stateInvisibility"),
            DRILL("stateDrill"),
            CAGE("stateCage"),
            STORAGE_FULL("stateStorageFull"),
            STORAGE_EMPTY("stateStorageEmpty"),
            PASSENGER("statePassenger"),
            ANIMAL("stateAnimal"),
            TAMEABLE("stateTameable"),
            BREEDABLE("stateBreedable"),
            HOSTILE("stateHostile"),
            CREEPER("stateCreeper"),
            SKELETON("stateSkeleton"),
            SPIDER("stateSpider"),
            ZOMBIE("stateZombie"),
            PIG_MAN("stateZombiePigMan"),
            SILVERFISH("stateSilverFish"),
            BLAZE("stateBlaze"),
            BAT("stateBat"),
            WITCH("stateWitch"),
            PIG("statePig"),
            SHEEP("stateSheep"),
            COW("stateCow"),
            MOOSHROOM("stateMooshroom"),
            CHICKEN("stateChicken"),
            WOLF("stateWolf"),
            SNOW_GOLEM("stateSnowGolem"),
            OCELOT("stateOcelot"),
            VILLAGER("stateVillager"),
            PLAYER("statePlayer"),
            ZOMBIE_VILLAGER("stateZombieVillager"),
            CHILD("stateChild"),
            TAMED("stateTamed"),
            POWER_RED("statePowerRed"),
            POWER_BLUE("statePowerBlue"),
            POWER_GREEN("statePowerGreen"),
            POWER_YELLOW("statePowerYellow"),
            TANKS_FULL("stateTanksFull"),
            TANKS_EMPTY("stateTanksEmpty"),
            TANK_EMPTY("stateTankEmpty"),
            CAKE("stateCake");






            private String name;
            DETECTOR(String name) {
                this.name = name;
            }

            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

    }

    public static enum MODULE_INFO implements ILocalizedText {
        ENGINE_GROUP("moduleGroupEngine"),
        DRILL_GROUP("moduleGroupDrill"),
        FARMER_GROUP("moduleGroupFarmer"),
        CUTTER_GROUP("moduleGroupCutter"),
        TANK_GROUP("moduleGroupTank"),
        ENTITY_GROUP("moduleGroupEntity"),
        SHOOTER_GROUP("moduleGroupShooter"),
        TOOL_GROUP("moduleGroupTool"),
        TOOL_OR_SHOOTER_GROUP("moduleGroupToolShooter"),
        HULL_CATEGORY("moduleCategoryHull"),
        ENGINE_CATEGORY("moduleCategoryEngine"),
        TOOL_CATEGORY("moduleCategoryTool"),
        STORAGE_CATEGORY("moduleCategoryStorage"),
        ADDON_CATEGORY("moduleCategoryAddon"),
        ATTACHMENT_CATEGORY("moduleCategoryAttachment"),
        INVALID_CATEGORY("moduleCategoryInvalid"),
        PIG_MESSAGE("pigExtraMessage"),
        OCEAN_MESSAGE("oceanExtraMessage"),
        ALPHA_MESSAGE("alphaExtraMessage"),
        STORAGE_EMPTY("storageEmpty"),
        STORAGE_FULL("storageFull"),
        GIFT_STORAGE_FULL("giftStorageFull"),
        EGG_STORAGE_FULL("eggStorageFull"),
        MODULAR_COST("modularCost"),
        SIDE_NONE("cartSideNone"),
        SIDE_TOP("cartSideTop"),
        SIDE_CENTER("cartSideCenter"),
        SIDE_BOTTOM("cartSideBottom"),
        SIDE_BACK("cartSideBack"),
        SIDE_LEFT("cartSideLeft"),
        SIDE_RIGHT("cartSideRight"),
        SIDE_FRONT("cartSideFront"),
        OCCUPIED_SIDES("occupiedSides"),
        VEHICLE_TYPES("vehicleTypes"),
        AND("sidesAnd"),
        NO_SIDES("noSides"),
        CONFLICT_HOWEVER("moduleConflictHowever"),
        CONFLICT_ALSO("moduleConflictAlso"),
        REQUIREMENT("moduleRequirement"),
        MODULE_COUNT_1("moduleCount1"),
        MODULE_COUNT_2("moduleCount2"),
        MODULE_COUNT_3("moduleCount3"),
        DUPLICATES("allowDuplicates"),
        TYPE("moduleType"),
        CAPACITY_ERROR("capacityOverloadError"),
        COMBINATION_ERROR("impossibleCombinationError"),
        COMPLEXITY_ERROR("complexityOverloadError"),
        PARENT_ERROR("missingParentError"),
        NEMESIS_ERROR("presentNemesisError"),
        DUPLICATE_ERROR("presentDuplicateError"),
        CLASH_ERROR("sideClashError"),
        NO_VEHICLE_ERROR("noVehicleError"),
        TOOL_UNBREAKABLE("toolUnbreakable"),
        TOOL_DURABILITY("toolDurability"),
        MODULAR_CAPACITY("modularCapacity"),
        COMPLEXITY_CAP("complexityCap"),
        MAX_ENGINES("maxEngineCount"),
        MAX_ADDONS("maxAddonCount");


        private String name;
        MODULE_INFO(String name) {
            this.name = name;
        }

        public String translate(String ... vars) {
            return doTranslate("info.SC2:" + name, vars);
        }
    }

    public static enum UPGRADES implements ILocalizedText {
        BLUEPRINT("effectBlueprint"),
        COMBUSTION("effectCombustionFuel"),
        DEPLOYER("effectDeployer"),
        DISASSEMBLE("effectDisassemble"),
        FUEL_CAPACITY("effectFuelCapacity"),
        FUEL_COST("effectFuelCost"),
        INPUT_CHEST("effectInputChest"),
        BRIDGE("effectManagerBridge"),
        GENERATOR("effectGenerator"),
        REDSTONE("effectRedstone"),
        SOLAR("effectSolar"),
        THERMAL("effectThermal"),
        FLAT("effectTimeFlat"),
        CART_FLAT("effectTimeFlatCart"),
        FLAT_REMOVED("effectTimeFlatRemove"),
        TRANSPOSER("effectTransposer"),
        EFFICIENCY("effectEfficiency");



        private String name;
        UPGRADES(String name) {
            this.name = name;
        }

        public String translate(String ... vars) {
            return doTranslate("info.SC2:" + name, vars);
        }
    }

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
