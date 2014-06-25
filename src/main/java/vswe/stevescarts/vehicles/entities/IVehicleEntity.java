package vswe.stevescarts.vehicles.entities;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.IFluidHandler;
import vswe.stevescarts.vehicles.VehicleBase;

public interface IVehicleEntity extends IInventory, IEntityAdditionalSpawnData, IFluidHandler {
    VehicleBase getVehicle();
}
