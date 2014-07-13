package vswe.stevesvehicles.fancy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum LoadType {
    KEEP("Keep"),
    OVERRIDE("Override"),
    REQUIRE("Require");

    private String code;

    LoadType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
