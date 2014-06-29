package vswe.stevesvehicles.old.Modules.Realtimers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.old.Slots.SlotBase;
import vswe.stevesvehicles.old.Slots.SlotMilker;

public class ModuleMilker extends ModuleBase {

	public ModuleMilker(EntityModularCart cart) {
		super(cart);
	}

	
	
	int cooldown = 0;
	int milkbuffer = 0;
	
	@Override
	public void update() {
		super.update();
		
		if (cooldown <= 0) {
			if (!getCart().worldObj.isRemote && getCart().hasFuel()) {
				generateMilk();
				depositeMilk();
			}
			
			cooldown = 20;
		}else{
			--cooldown;
		}
	}

	private void depositeMilk() {
		if (milkbuffer > 0) {
			FluidStack ret = FluidContainerRegistry.getFluidForFilledItem(new ItemStack(Items.milk_bucket));
			if (ret != null) {
				ret.amount = milkbuffer;
				milkbuffer -= getCart().fill(ret, true);
			}
			
			if (milkbuffer == FluidContainerRegistry.BUCKET_VOLUME) {
				for (int i = 0; i < getInventorySize(); i++) {
					ItemStack bucket = getStack(i);
					if (bucket != null && bucket.getItem() == Items.bucket) {
						ItemStack milk = new ItemStack(Items.milk_bucket);
						
						

						getCart().addItemToChest(milk);
						
						if (milk.stackSize <= 0) {
							milkbuffer = 0;
							if (--bucket.stackSize <= 0) {
								setStack(i, null);
							}
						}
						
					}
				}
			}
		}
	}

	private void generateMilk() {
		if (milkbuffer < FluidContainerRegistry.BUCKET_VOLUME) {
			Entity rider = getCart().riddenByEntity;
			if (rider != null && rider instanceof EntityCow) {
				milkbuffer = Math.min(milkbuffer + 75, FluidContainerRegistry.BUCKET_VOLUME);
			}
		}
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	protected int getInventoryWidth() {
		return 2;
	}
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotMilker(getCart(),slotId,8+x*18,23+y*18);
	}	
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort(generateNBTName("Milk",id), (short)milkbuffer);	
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		milkbuffer = tagCompound.getShort(generateNBTName("Milk",id));
	}		
}
