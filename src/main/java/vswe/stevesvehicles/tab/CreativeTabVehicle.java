package vswe.stevesvehicles.tab;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.localization.LocalizedTextSimple;
import vswe.stevesvehicles.item.ModItems;
import vswe.stevesvehicles.vehicle.VehicleRegistry;
import vswe.stevesvehicles.vehicle.VehicleType;

public class CreativeTabVehicle extends CreativeTabCustom {
    private VehicleType vehicleType;

    public CreativeTabVehicle(VehicleType vehicleType) {
        super(new LocalizedTextSimple(vehicleType.getUnlocalizedName()));
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getIconItemStack() {
        int id = VehicleRegistry.getInstance().getIdFromType(vehicleType);
        if (id >= 0) {
            return new ItemStack(ModItems.vehicles, 1, id);
        }else{
            return null;
        }
    }
}
