package vswe.stevescarts.Upgrades;

import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Interfaces.GuiUpgrade;
import vswe.stevescarts.Slots.SlotLiquidOutput;
import vswe.stevescarts.Slots.SlotLiquidUpgradeInput;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TankEffect extends InventoryEffect {

	public TankEffect() {
		super();
		
		
	}
	
	
	public abstract int getTankSize();
	
	
	@Override
	public Class<? extends Slot> getSlot(int id) {
		return SlotLiquidOutput.class;		
	}

	@Override
	public Slot createSlot(TileEntityUpgrade upgrade, int id) {
		if (id == 0) {
			return new SlotLiquidUpgradeInput(upgrade,upgrade.tank, 16, id, getSlotX(id), getSlotY(id));
		}else{
			return super.createSlot(upgrade, id);
		}
	}
	
	@Override
	public int getInventorySize() {
		return 2;
	}

	@Override
	public int getSlotX(int id) {
		return 8;
	}

	@Override
	public int getSlotY(int id) {
		return 24 * (id + 1);
	}	
	
	


	
	private static final int tankInterfaceX = 35;
	private static final int tankInterfaceY = 20;	
	
	@SideOnly(Side.CLIENT) 
	private static ResourceLocation texture;
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(TileEntityUpgrade upgrade, GuiUpgrade gui, int x, int y) {
		if (texture == null) {
			texture = ResourceHelper.getResource("/gui/tank.png");
		}
		
		upgrade.tank.drawFluid(gui, tankInterfaceX, tankInterfaceY);
		ResourceHelper.bindResource(texture);	
		gui.drawTexturedModalRect(gui.getGuiLeft() + tankInterfaceX, gui.getGuiTop() + tankInterfaceY, 0, 0, 36, 51);
		
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(TileEntityUpgrade upgrade, GuiUpgrade gui, int x, int y) {
		drawMouseOver(gui, upgrade.tank.getMouseOver(), x,y, new int[] {tankInterfaceX, tankInterfaceY, 36, 51});
	}
	
	
	// TODO synchronize the tag somehow :S
	@Override
	public void checkGuiData(TileEntityUpgrade upgrade, ContainerUpgrade con, ICrafting crafting, boolean isNew) {
		
		boolean changed = false;
		int id = 0;
		int amount1 = 1;
		int amount2 = 2;
		int meta = 3;
		
		FluidStack oldfluid = (FluidStack)con.olddata;
		
		if ((isNew || oldfluid != null) && upgrade.tank.getFluid() == null) {
			upgrade.updateGuiData(con, crafting, id, (short)-1);
			changed = true;
		}else if(upgrade.tank.getFluid() != null) {
			if (isNew || oldfluid == null) {
				upgrade.updateGuiData(con, crafting, id, (short)upgrade.tank.getFluid().fluidID);	
				upgrade.updateGuiData(con, crafting, amount1, upgrade.getShortFromInt(true, upgrade.tank.getFluid().amount));	
				upgrade.updateGuiData(con, crafting, amount2, upgrade.getShortFromInt(false, upgrade.tank.getFluid().amount));	
				changed = true;
			}else{
			
				if (oldfluid.fluidID != upgrade.tank.getFluid().fluidID) {
					upgrade.updateGuiData(con, crafting, id, (short)upgrade.tank.getFluid().fluidID);
					changed = true;
				}					
				if (oldfluid.amount != upgrade.tank.getFluid().amount) {
					upgrade.updateGuiData(con, crafting, amount1, upgrade.getShortFromInt(true, upgrade.tank.getFluid().amount));	
					upgrade.updateGuiData(con, crafting, amount2, upgrade.getShortFromInt(false, upgrade.tank.getFluid().amount));		
					changed = true;
				}					
			
			}
		
		}
		
		if (changed) {
			if (upgrade.tank.getFluid() == null) {
				con.olddata = null;
			}else{
				con.olddata = upgrade.tank.getFluid().copy();
			}
		}		
	}
	
	
	// TODO Synchronize the tag somehow :S
	@Override
	public void receiveGuiData(TileEntityUpgrade upgrade, int id, short data) {	
		
		if (id == 0) {
			if (data == -1) {
				upgrade.tank.setFluid(null);
			}else if (upgrade.tank.getFluid() == null){
				upgrade.tank.setFluid(new FluidStack(data, 0));
			}
		}else if(upgrade.tank.getFluid() != null) {
			upgrade.tank.getFluid().amount = upgrade.getIntFromShort(id == 1,upgrade.tank.getFluid().amount, data);			
		}
		

	}		
	
	
	@Override
	public void init(TileEntityUpgrade upgrade) {
		upgrade.tank = new Tank(upgrade, getTankSize(), 0);
		upgrade.getCompound().setByte("Tick", (byte)0);	
	}
	

	@Override
	public void update(TileEntityUpgrade upgrade) {
		super.update(upgrade);
		

		
		upgrade.getCompound().setByte("Tick", (byte)(upgrade.getCompound().getByte("Tick") - 1));
		if (upgrade.getCompound().getByte("Tick") <= 0) {
			upgrade.getCompound().setByte("Tick", (byte)5);
		}else{
			return;
		}
		
		if (!upgrade.getWorldObj().isRemote && slots != null && slots.size() >= 2) {
			upgrade.tank.containerTransfer();
		}
		
		
	}

	
	
	
	@Override
	public void load(TileEntityUpgrade upgrade, NBTTagCompound compound) {
		if (compound.getByte("Exists") != 0) {
			upgrade.tank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
		}else{
			upgrade.tank.setFluid(null);
		}	
	}
	
	@Override
	public void save(TileEntityUpgrade upgrade, NBTTagCompound compound) {
		if (upgrade.tank.getFluid() == null) {
			compound.setByte("Exists", (byte)0);
		}else{
			compound.setByte("Exists", (byte)1);
			upgrade.tank.getFluid().writeToNBT(compound);
		}
	}
		

	
}
