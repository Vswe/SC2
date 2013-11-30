package vswe.stevescarts.Helpers;


import net.minecraft.util.StatCollector;

public final class Localization {
    private Localization() {}

    public static enum GUI {
        CART_ASSEMBLER("cartAssembler"),
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
        GUI(String name) {
            this.name = name;
        }


        public String translate() {
            return StatCollector.translateToLocal("gui.SC2:" + name);
        }
    }
}
