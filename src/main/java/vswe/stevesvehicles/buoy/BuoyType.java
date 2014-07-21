package vswe.stevesvehicles.buoy;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.StevesVehicles;

public enum BuoyType {
    NORMAL("buoys/normal"),
    OTHER("buoys/other");


    BuoyType(String texture) {
        this.texture = texture;
    }


    @SideOnly(Side.CLIENT)
    private IIcon icon;
    @SideOnly(Side.CLIENT)
    private String texture;

    public String getUnlocalizedName() {
        return "steves_vehicles:tile.buoy:" + toString().toLowerCase();
    }

    public String getName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
            icon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + texture);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side) {
        return icon;
    }

    public static BuoyType getType(int meta) {
        return values()[meta % values().length];
    }

    public int getMeta() {
        return ordinal();
    }
}
