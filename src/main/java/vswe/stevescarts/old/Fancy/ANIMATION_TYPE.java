package vswe.stevescarts.old.Fancy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ANIMATION_TYPE {
    STILL("Still"),
    ANIMATION("Loop"),
    PAUSE("Pause"),
    RANDOM("Random");

    private String code;

    ANIMATION_TYPE(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
