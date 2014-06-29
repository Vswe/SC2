package vswe.stevesvehicles.vehicle;


import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class VehicleCart extends VehicleBase {
    public VehicleCart(EntityModularCart entity) {
        super(entity);
    }

    public VehicleCart(EntityModularCart entity, TileEntityCartAssembler assembler, int[] data) {
        super(entity, assembler, data);
    }

    public VehicleCart(EntityModularCart entity, NBTTagCompound info, String name) {
        super(entity, info, name);
    }

    private EntityModularCart getCart() {
        return (EntityModularCart)getEntity();
    }

    @Override
    public void updateFuel() {
        super.updateFuel();
        //if a cart is not moving but has fuel for it, start it
        if (hasFuel()) {
            if (!engineFlag) {
                getCart().pushX = getCart().temppushX;
                getCart().pushZ = getCart().temppushZ;
            }

            //if the cart doesn't have fuel but is moving, stop it
        }else if(engineFlag){
            getCart().temppushX = getCart().pushX;
            getCart().temppushZ = getCart().pushZ;
            getCart().pushX = getCart().pushZ = 0.0D;
        }
    }
}
