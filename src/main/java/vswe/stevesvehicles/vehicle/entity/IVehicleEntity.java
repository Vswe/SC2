package vswe.stevesvehicles.vehicle.entity;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.IFluidHandler;
import vswe.stevesvehicles.vehicle.VehicleBase;

public interface IVehicleEntity extends IInventory, IEntityAdditionalSpawnData, IFluidHandler {
    VehicleBase getVehicle();
}
