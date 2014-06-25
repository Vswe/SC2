package vswe.stevescarts.old;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import cpw.mods.fml.common.network.IGuiHandler;
import vswe.stevescarts.old.TileEntities.TileEntityBase;

public class CommonProxy implements IGuiHandler {
	public void renderInit()
	{
	}

	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {	
		if (ID == 0) {	
			EntityModularCart cart = getCart(x, world);
			if (cart != null) {
				return cart.getGui(player);
			}
		}else{	
			TileEntity tileentity = world.getTileEntity(x, y, z);
			
			if (tileentity != null && tileentity instanceof TileEntityBase) {
				
				return ((TileEntityBase)tileentity).getGui(player.inventory);
									
			}
		}
		
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {	
			EntityModularCart cart = getCart(x, world);
			if (cart != null) {
				return cart.getCon(player.inventory);
			}
		}else{
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity != null && tileentity instanceof TileEntityBase) {
				
				return ((TileEntityBase)tileentity).getContainer(player.inventory);
				
				
			}
		}
		
		
		
		return null;
	}
	
	private EntityModularCart getCart(int ID, World world) {
		for (Object e : world.loadedEntityList) {
			if (e instanceof Entity && ((Entity)e).getEntityId() == ID && e instanceof EntityModularCart) {
				return (EntityModularCart)e;
			}
		}
		return null;
	}

	public World getClientWorld() {
		return null;
	}


	public void soundInit() {

	}

}
