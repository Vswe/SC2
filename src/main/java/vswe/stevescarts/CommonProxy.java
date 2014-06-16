package vswe.stevescarts;
import java.lang.reflect.Constructor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import cpw.mods.fml.common.network.IGuiHandler;
import vswe.stevescarts.TileEntities.TileEntityBase;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Containers.ContainerBase;
import net.minecraft.entity.player.InventoryPlayer;

public class CommonProxy implements IGuiHandler {
	public void renderInit()
	{
	}

	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {	
		if (ID == 0) {	
			MinecartModular cart = getCart(x, world);
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
			MinecartModular cart = getCart(x, world);
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
	
	private MinecartModular getCart(int ID, World world) {
		for (Object e : world.loadedEntityList) {
			if (e instanceof Entity && ((Entity)e).entityId == ID && e instanceof MinecartModular) {
				return (MinecartModular)e;
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
