package vswe.stevesvehicles.old;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevesvehicles.vehicle.VehicleBase;
import cpw.mods.fml.common.network.IGuiHandler;
import vswe.stevesvehicles.old.TileEntities.TileEntityBase;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

public class CommonProxy implements IGuiHandler {
	public void renderInit() {
	}

	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 0) {
			VehicleBase vehicle = getVehicle(x, world);
			if (vehicle != null) {
				return vehicle.getGui(player);
			}
		}else{	
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityBase) {
				return ((TileEntityBase)tileentity).getGui(player.inventory);
			}
		}
		
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 0) {
            VehicleBase vehicle = getVehicle(x, world);
            if (vehicle != null) {
				return vehicle.getCon(player.inventory);
			}
		}else{
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityBase) {
				return ((TileEntityBase)tileentity).getContainer(player.inventory);
			}
		}
		
		return null;
	}
	
	private VehicleBase getVehicle(int id, World world) {
        Entity entity = world.getEntityByID(id);
        if (entity instanceof IVehicleEntity) {
            return ((IVehicleEntity)entity).getVehicle();
        }
		return null;
	}

	public World getClientWorld() {
		return null;
	}


	public void soundInit() {

	}

}
