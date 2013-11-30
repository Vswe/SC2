package vswe.stevescarts.Helpers;


import net.minecraft.util.StatCollector;

public final class Localization {
    private Localization() {}

    public static class GUI {

        public static enum ASSEMBLER {
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
            FUEL_LEVEL("fuelLevel");

            private String name;
            ASSEMBLER(String name) {
                this.name = name;
            }


            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

        public static enum MANAGER {
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

        public static enum CARGO {
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
            UNKNOWN_AREA("unknownAreaMessage");





            private String name;
            CARGO(String name) {
                this.name = name;
            }


            public String translate(String ... vars) {
                return doTranslate("gui.SC2:" + name, vars);
            }
        }

        public static enum LIQUID {
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


        public static enum TOGGLER {
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

        public static enum DISTRIBUTOR {
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

                    int optionId = vars[i].equals("1") ? 0 : 1;
                    if (optionId >= 0 && optionId < options.length) {
                        String option = options[optionId];
                        result = result.substring(0, index) + option + result.substring(endIndex + 1);
                    }
                }
            }else{
                result = result.replace("[%" + (i + 1) + "]", vars[i]);
            }
        }
        return result;
    }
}
