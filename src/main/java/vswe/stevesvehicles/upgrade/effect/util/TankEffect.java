package vswe.stevesvehicles.upgrade.effect.util;

import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.old.Containers.ContainerUpgrade;
import vswe.stevesvehicles.old.Helpers.ITankHolder;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Helpers.Tank;
import vswe.stevesvehicles.old.Helpers.TransferHandler;
import vswe.stevesvehicles.client.gui.screen.GuiUpgrade;
import vswe.stevesvehicles.container.slots.SlotLiquidOutput;
import vswe.stevesvehicles.container.slots.SlotLiquidUpgradeInput;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TankEffect extends InventoryEffect implements ITankHolder {

	public TankEffect(TileEntityUpgrade upgrade) {
		super(upgrade);
	}

    public Tank getTank() {
        return tank;
    }

    public abstract int getTankSize();
	
	
	@Override
	public Class<? extends Slot> getSlot(int id) {
		return SlotLiquidOutput.class;		
	}

	@Override
	public Slot createSlot(int id) {
		if (id == 0) {
			return new SlotLiquidUpgradeInput(upgrade, tank, 16, id, getSlotX(id), getSlotY(id));
		}else{
			return super.createSlot(id);
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
	public void drawBackground(GuiUpgrade gui, int x, int y) {
		if (texture == null) {
			texture = ResourceHelper.getResource("/gui/tank.png");
		}
		
		tank.drawFluid(gui, tankInterfaceX, tankInterfaceY);
		ResourceHelper.bindResource(texture);	
		gui.drawTexturedModalRect(gui.getGuiLeft() + tankInterfaceX, gui.getGuiTop() + tankInterfaceY, 0, 0, 36, 51);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiUpgrade gui, int x, int y) {
		drawMouseOver(gui, tank.getMouseOver(), x,y, new int[] {tankInterfaceX, tankInterfaceY, 36, 51});
	}
	
	
	// TODO synchronize the tag somehow :S
	@Override
	public void checkGuiData(ContainerUpgrade con, ICrafting crafting, boolean isNew) {
		
		boolean changed = false;
		int id = 0;
		int amount1 = 1;
		int amount2 = 2;
		int meta = 3;

		FluidStack oldFluid = (FluidStack)con.olddata;
		
		if ((isNew || oldFluid != null) && tank.getFluid() == null) {
			upgrade.updateGuiData(con, crafting, id, (short)-1);
			changed = true;
		}else if(tank.getFluid() != null) {
			if (isNew || oldFluid == null) {
				upgrade.updateGuiData(con, crafting, id, (short)tank.getFluid().fluidID);
				upgrade.updateGuiData(con, crafting, amount1, upgrade.getShortFromInt(true, tank.getFluid().amount));
				upgrade.updateGuiData(con, crafting, amount2, upgrade.getShortFromInt(false, tank.getFluid().amount));
				changed = true;
			}else{
			
				if (oldFluid.fluidID != tank.getFluid().fluidID) {
					upgrade.updateGuiData(con, crafting, id, (short)tank.getFluid().fluidID);
					changed = true;
				}					
				if (oldFluid.amount != tank.getFluid().amount) {
					upgrade.updateGuiData(con, crafting, amount1, upgrade.getShortFromInt(true, tank.getFluid().amount));
					upgrade.updateGuiData(con, crafting, amount2, upgrade.getShortFromInt(false, tank.getFluid().amount));
					changed = true;
				}					
			
			}
		
		}
		
		if (changed) {
			if (tank.getFluid() == null) {
				con.olddata = null;
			}else{
				con.olddata = tank.getFluid().copy();
			}
		}		
	}
	
	
	// TODO Synchronize the tag somehow :S
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			if (data == -1) {
				tank.setFluid(null);
			}else if (tank.getFluid() == null){
				tank.setFluid(new FluidStack(data, 0));
			}
		}else if(tank.getFluid() != null) {
			tank.getFluid().amount = upgrade.getIntFromShort(id == 1, tank.getFluid().amount, data);
		}

	}		


    private int tick;
    private Tank tank;
	
	@Override
	public void init() {
		tank = new Tank(this, getTankSize(), 0);
	}
	

	@Override
	public void update() {
		super.update();

		if (--tick <= 0) {
			tick = 5;
		}else{
			return;
		}
		
		if (!upgrade.getWorldObj().isRemote && slots != null && slots.size() >= 2) {
			tank.containerTransfer();
		}
	}

	

	@Override
	public void load(NBTTagCompound compound) {
	    tank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
	}
	
	@Override
	public void save(NBTTagCompound compound) {
		if (tank.getFluid() != null) {
			tank.getFluid().writeToNBT(compound);
		}
	}


    @Override
    public ItemStack getInputContainer(int tankId) {
        return upgrade.getStackInSlot(0);
    }

    @Override
    public void clearInputContainer(int tankId) {
        upgrade.setInventorySlotContents(0, null);
    }

    @Override
    public void addToOutputContainer(int tankId, ItemStack item) {
        TransferHandler.TransferItem(item, upgrade, 1, 1, new ContainerUpgrade(null, upgrade), Slot.class, null, -1);
    }

    @Override
    public void onFluidUpdated(int tankId) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawImage(int tankId, GuiBase gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY) {
        gui.drawIcon(icon, gui.getGuiLeft() + targetX, gui.getGuiTop() + targetY, sizeX / 16F, sizeY / 16F, srcX / 16F, srcY / 16F);
    }
}
