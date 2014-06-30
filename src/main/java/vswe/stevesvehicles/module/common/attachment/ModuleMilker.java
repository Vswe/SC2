package vswe.stevesvehicles.module.common.attachment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.old.Slots.SlotBase;
import vswe.stevesvehicles.old.Slots.SlotMilker;

public class ModuleMilker extends ModuleAttachment {

	public ModuleMilker(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	
	
	int cooldown = 0;
	int milkBuffer = 0;

    private static final int MILKING_COOLDOWN = 20;
	
	@Override
	public void update() {
		super.update();
		
		if (cooldown <= 0) {
			if (!getVehicle().getWorld().isRemote && getVehicle().hasFuel()) {
				generateMilk();
				depositMilk();
			}
			
			cooldown = MILKING_COOLDOWN;
		}else{
			--cooldown;
		}
	}

	private void depositMilk() {
		if (milkBuffer > 0) {
			FluidStack ret = FluidContainerRegistry.getFluidForFilledItem(new ItemStack(Items.milk_bucket));
			if (ret != null) {
				ret.amount = milkBuffer;
				milkBuffer -= getVehicle().fill(ret, true);
			}
			
			if (milkBuffer == FluidContainerRegistry.BUCKET_VOLUME) {
				for (int i = 0; i < getInventorySize(); i++) {
					ItemStack bucket = getStack(i);
					if (bucket != null && bucket.getItem() == Items.bucket) {
						ItemStack milk = new ItemStack(Items.milk_bucket);

						getVehicle().addItemToChest(milk);
						
						if (milk.stackSize <= 0) {
							milkBuffer = 0;
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
		if (milkBuffer < FluidContainerRegistry.BUCKET_VOLUME) {
			Entity rider = getVehicle().getEntity().riddenByEntity;
			if (rider != null && rider instanceof EntityCow) {
				milkBuffer = Math.min(milkBuffer + 75, FluidContainerRegistry.BUCKET_VOLUME);
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
		return new SlotMilker(getVehicle().getVehicleEntity() , slotId, 8 + x * 18, 23 + y * 18);
	}	
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setShort("Milk", (short) milkBuffer);
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		milkBuffer = tagCompound.getShort("Milk");
	}		
}
