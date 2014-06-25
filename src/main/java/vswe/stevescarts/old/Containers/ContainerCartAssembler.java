package vswe.stevescarts.old.Containers;
import java.util.ArrayList;
import java.util.Iterator;
import vswe.stevescarts.old.TileEntities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.old.Slots.SlotAssembler;
import vswe.stevescarts.old.Slots.SlotHull;
import vswe.stevescarts.old.TileEntities.TileEntityCartAssembler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCartAssembler extends ContainerBase
{

	public IInventory getMyInventory() {
		return assembler;
	}
	
	public TileEntityBase getTileEntity() {
		return assembler;
	}

    private TileEntityCartAssembler assembler;
    public ContainerCartAssembler(IInventory invPlayer, TileEntityCartAssembler assembler)
    {
        this.assembler = assembler;
		
		ArrayList<SlotAssembler> slots = assembler.getSlots();
		for (SlotAssembler slot : slots) {
			addSlotToContainer(slot);
		}
		
		
		
		for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, offsetX() + k * 18, i * 18 + offsetY()));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(invPlayer, j, offsetX() + j * 18, 58 + offsetY()));
        }
    }

  
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return assembler.isUseableByPlayer(entityplayer);
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);

		assembler.initGuiData(this,par1ICrafting);
    }

	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
		par2 &= 65535;

		assembler.receiveGuiData(par1, (short)par2);
    }

	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
		Iterator var1 = this.crafters.iterator();

		while (var1.hasNext())
		{
			ICrafting var2 = (ICrafting)var1.next();

			assembler.checkGuiData(this,var2);
		}
    }
	
    protected int offsetX()
    {
        return 176;
    }

    protected int offsetY()
    {
        return 174;
    }

	public int lastMaxAssemblingTime;
	public int lastCurrentAssemblingTime;
	public boolean lastIsAssembling;
	public int lastFuelLevel;
	
	@Override
	public ItemStack slotClick(int slotID, int button, int keyflag, EntityPlayer player)
    {
		if (slotID >= 0 && slotID < inventorySlots.size()) {
			Slot hullSlot = (Slot)inventorySlots.get(slotID);
			
			if (hullSlot != null && hullSlot instanceof SlotHull) {
				InventoryPlayer playerInventory = player.inventory;
				ItemStack playerItem = playerInventory.getItemStack();
				ItemStack slotItem = hullSlot.getStack();
							
				ArrayList<SlotAssembler> newSlots = assembler.getValidSlotFromHullItem(playerItem);
				ArrayList<SlotAssembler> oldSlots = assembler.getValidSlotFromHullItem(slotItem);
				
				if (oldSlots != null) {
					if (newSlots != null) {
						for(SlotAssembler slot : newSlots) {
							int index = oldSlots.indexOf(slot);
							if (index != -1) {
								oldSlots.remove(index);
							}
						}
					}
					
					for (SlotAssembler slot : oldSlots) {
						if (slot.getHasStack()) {
							return null;
						}
					}
					
				}
			}
		}
		
		return super.slotClick(slotID,button, keyflag, player);
	}
	
}
