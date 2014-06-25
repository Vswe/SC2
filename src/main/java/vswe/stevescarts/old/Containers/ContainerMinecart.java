package vswe.stevescarts.old.Containers;
import java.util.ArrayList;
import java.util.HashMap;
import vswe.stevescarts.old.TileEntities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Slots.SlotBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerMinecart extends ContainerBase
{
    public ContainerMinecart(IInventory player, EntityModularCart cart)
    {
        cartInv(cart);
        playerInv(player);
    }

	public IInventory getMyInventory() {
		return cart;
	}
	
	public TileEntityBase getTileEntity() {
		return null;
	}	

    protected void cartInv(EntityModularCart cart)
    {
        this.cart = cart;

		if (cart.getModules() != null) {
			for (ModuleBase module : cart.getModules())
			{
				if (module.hasSlots()) {
					ArrayList<SlotBase> slotsList = module.getSlots();

					for (SlotBase slot : slotsList) {
						slot.xDisplayPosition = slot.getX() + module.getX() + 1;
						slot.yDisplayPosition = slot.getY() + module.getY() + 1;

						addSlotToContainer(slot);
					}
				}

			}
		}else{
			for (int i = 0; i < 100; i++) {
				addSlotToContainer(new Slot(cart,i,-1000,-1000));
			}
		}
    }

	private IInventory player;
    protected void playerInv(IInventory player)
    {
		this.player = player;

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(player, k + i * 9 + 9, offsetX() + k * 18, i * 18 + offsetY()));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(player, j, offsetX() + j * 18, 58 + offsetY()));
        }
    }

    protected int offsetX()
    {
        return 159;
    }

    protected int offsetY()
    {
		return 174;
    }
	
	@Override
   public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
   {	
		//System.out.println("Clicked" + (cart == null ? "" : cart.worldObj.isRemote ? "Client" : "Server"));
		return super.slotClick(par1,par2,par3,par4EntityPlayer);
   }
	
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return cart.isUseableByPlayer(entityplayer);
    }

	@Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        cart.closeInventory();
    }

	@Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
		if (cart.getModules() != null) {
			for (ModuleBase module : cart.getModules()) {
				//module.initGuiData(this,par1ICrafting);
			}
		}
    }

	@SideOnly(Side.CLIENT)
	@Override
    public void updateProgressBar(int par1, int par2)
    {
		par2 &= 65535;

		if (cart.getModules() != null) {
			for (ModuleBase module : cart.getModules()) {
				if (par1 >= module.getGuiDataStart() && par1 < module.getGuiDataStart() + module.numberOfGuiData()) {
					module.receiveGuiData(par1-module.getGuiDataStart(),(short)par2);
					break;
				}
			}
		}
    }

	public HashMap<Short,Short> cache;
	@Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
		if (cart.getModules() != null) {
			if (crafters.size() > 0)
			{		
				for (ModuleBase module : cart.getModules()) {
					module.checkGuiData(this,crafters, false);
				}
			}
		}

    }

    public EntityModularCart cart;
}
