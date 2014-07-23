package vswe.stevesvehicles.buoy;


import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.client.ResourceHelper;

public enum BuoyType {
    NORMAL(0xE25B00),
    OTHER(0x06477C);


    BuoyType(int color) {
        this.color = color;
        this.texture = ResourceHelper.getResource("/models/buoy/" + this.toString().toLowerCase() + ".png");
    }

    private int color;
    private ResourceLocation texture;

    public String getUnlocalizedName() {
        return "steves_vehicles:tile.buoy:" + toString().toLowerCase();
    }

    public String getName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
    }

    public static BuoyType getType(int meta) {
        return values()[meta % values().length];
    }

    public int getMeta() {
        return ordinal();
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getColor() {
        return color;
    }
}
