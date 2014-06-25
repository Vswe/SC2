package vswe.stevesvehicles.old.Helpers;


import net.minecraft.init.Blocks;
import org.lwjgl.opengl.GL11;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevesvehicles.client.interfaces.GuiBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Tank implements IFluidTank {

	private FluidStack fluid;
	private int tankSize;
	private ITankHolder owner;
	private int tankid;
	private boolean isLocked;
	
	public Tank(ITankHolder owner,int tankSize, int tankid) {
		this.owner = owner;
		this.tankSize = tankSize;
		this.tankid = tankid;
	}

	public Tank copy() {
		Tank tank = new Tank(owner, tankSize, tankid);
		if (getFluid() != null) {
			tank.setFluid(getFluid().copy());
		}		
		return tank;
	}
	
	@Override
	public FluidStack getFluid() {
		return fluid;
	}

	public void setFluid(FluidStack fluid) {
		this.fluid = fluid;
	}
	
	@Override
	public int getCapacity() {
		return tankSize;
	}
	
    public int getTankPressure() {
		return 0;
	}	
	
	public void containerTransfer() {
		ItemStack item = owner.getInputContainer(tankid);
		
		if (item != null) {

			if (FluidContainerRegistry.isFilledContainer(item)) {
				FluidStack fluidContent = FluidContainerRegistry.getFluidForFilledItem(item);
				if (fluidContent != null) {
				
					int fill = fill(fluidContent, false, false);
					if (fill == fluidContent.amount) {
	
						Item container = item.getItem().getContainerItem();
						
						ItemStack containerStack = null;
						if (container != null) {
							containerStack = new ItemStack(container, 1);
						
							owner.addToOutputContainer(tankid, containerStack);
						}
						
						if (containerStack == null || containerStack.stackSize == 0) {
						
							item.stackSize--;
							if (item.stackSize <= 0) {
								owner.clearInputContainer(tankid);
							}
							
							fill(fluidContent, true, false);	
						}	
					}
				}
			}else if(FluidContainerRegistry.isEmptyContainer(item)) {
				ItemStack full = FluidContainerRegistry.fillFluidContainer(fluid, item);
				if (full != null) {
					FluidStack fluidContent = FluidContainerRegistry.getFluidForFilledItem(full);
					
					if (fluidContent != null) {
						owner.addToOutputContainer(tankid, full);
						
						if (full.stackSize == 0) {
						
							item.stackSize--;
							if (item.stackSize <= 0) {
								owner.clearInputContainer(tankid);
							}
							
							
							
							drain(fluidContent.amount, true, false);	
						}	

					}
				}
			}

		}	
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return fill(resource, doFill, false);
	}
	
	public int fill(FluidStack resource, boolean doFill, boolean isRemote) {
		if (resource == null || (fluid != null && !resource.isFluidEqual(fluid))) {
			return 0;
		}else{
			int free = tankSize - (fluid == null ? 0 : fluid.amount);
			int fill = Math.min(free, resource.amount);
			
			if (doFill && !isRemote) {
				if (fluid == null) {
					fluid = resource.copy();
					fluid.amount = 0;
				}
			
				fluid.amount += fill;
				owner.onFluidUpdated(tankid);
			}
			
			return fill;
		}
	}	
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain, false);
	}	
	
	public FluidStack drain(int maxDrain, boolean doDrain, boolean isRemote) {
		if (fluid == null) {
			return null;
		}
	
		int amount = fluid.amount;
		int drain = Math.min(amount, maxDrain);
		
		FluidStack ret = fluid.copy();
		ret.amount = drain;		
		
		if (doDrain && !isRemote) {
			fluid.amount -= drain;
			
			if (fluid.amount <= 0 && !isLocked) {
				fluid = null;
			}
			
			owner.onFluidUpdated(tankid);
		}
		

		return ret;
	}
	
	public void setLocked(boolean val) {
		isLocked = val;
	}
	
	public boolean isLocked() {
		return isLocked;
	}

	
	public String getMouseOver() {
		String name = Localization.MODULES.TANKS.EMPTY.translate();
		int amount = 0;
		if (fluid != null) {
			//different mods store the name in different ways apparently
			name = fluid.getFluid().getLocalizedName();
			if (name.indexOf(".") != -1) {
				name = FluidRegistry.getFluidName(fluid);
			}
			
			if (name != null && !name.equals("")) {
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			}else{
				name = Localization.MODULES.TANKS.INVALID.translate();
			}
			amount = fluid.amount;
		}
		
		
	
		return "\u00a7F" + name + "\n\u00a77" + formatNumber(amount) + " / " + formatNumber(tankSize); 
	}	
	
	private String formatNumber(int number) {
		return String.format("%,d", number).replace((char)160,(char)32); 	
	}	
	
	public static IconData getIconAndTexture(FluidStack stack) {
		IIcon icon = null;
		String texture = null;
		
		if (stack != null) {

			
			Fluid fluid = stack.getFluid();
			if (fluid != null) {

				icon = fluid.getIcon();
				
				if (icon == null) {
					if (FluidRegistry.WATER.equals(fluid)) {
						icon = Blocks.water.getIcon(0, 0);
					}else if(FluidRegistry.LAVA.equals(fluid)) {
						icon = Blocks.lava.getIcon(0, 0);
					}
				}
				
				if (icon != null) {
					texture = "/atlas/blocks.png";
				}
			}
			
					
		}
		


		return new IconData (icon, texture);
	}
	
	private static float getColorComponent(int color, int id) {
		return ((color & (255 << (id * 8))) >> (id * 8)) / 255F;	
	}
	
	public static void applyColorFilter(FluidStack fluid) {
		int color = fluid.getFluid().getColor(fluid);
		GL11.glColor4f(getColorComponent(color, 2), getColorComponent(color, 1), getColorComponent(color, 0), 1F);
	}
	
	@SideOnly(Side.CLIENT)
	public void drawFluid(GuiBase gui,int startX, int startY) {
		if (fluid != null) {

 
			int fluidLevel = (int)(48 * (fluid.amount / (float)tankSize));
			
			IconData data = getIconAndTexture(fluid);
			
			if (data.getIcon() == null) {
				return;
			}
			
			ResourceHelper.bindResource(data.getResource());

			applyColorFilter(fluid);
				
			
			for (int y = 0; y < 3; y++) {	
				int pixels = fluidLevel - (2-y) * 16;
				
				if (pixels <= 0) {
					continue;
				}else if(pixels > 16) {
					pixels = 16;
				}
				
				for (int x = 0;x < 2; x++) {					
					owner.drawImage(tankid, gui, data.getIcon(), startX + 2 + 16*x, startY + 1 + 16*y + (16-pixels), 0, (16-pixels), 16, pixels);			
				}
			}		
			GL11.glColor4f(1F, 1F, 1F, 1F);
			
		}			
	}

	@Override
	public int getFluidAmount() {
		return fluid == null ? 0 : fluid.amount;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(fluid, getCapacity());
	}
	
}