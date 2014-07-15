package vswe.stevesvehicles.module.common.storage.tank;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.gui.ColorHelper;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationTank;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.tank.ITankHolder;
import vswe.stevesvehicles.tank.Tank;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.module.common.storage.ModuleStorage;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotLiquidInput;
import vswe.stevesvehicles.container.slots.SlotLiquidOutput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModuleTank extends ModuleStorage implements IFluidTank, ITankHolder {

	public ModuleTank(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		tank = new Tank(this, getTankSize(), 0);
	}


	protected abstract int getTankSize();
	
	protected Tank tank;

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		if (y == 0) {
			return new SlotLiquidInput(getVehicle().getVehicleEntity(), tank, -1,  slotId, 8 + x * 18, 24 + y * 24);
		}else{
			return new SlotLiquidOutput(getVehicle().getVehicleEntity(), slotId, 8 + x * 18, 24 + y * 24);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName() , 8, 6, 0x404040);
	}

	@Override
	public int getInventoryWidth() {
		return 1;
	}

	@Override
	public int getInventoryHeight() {
		return 2;
	}
	
	@Override
	public int guiWidth() {
		return 100;
	}

	@Override
	public int guiHeight() {
		return 80;
	}

	public boolean hasVisualTank() {
		return true;
	}

	private int tick;
	
	@Override
	public void update() {
		super.update();
		
		if (tick-- <= 0) {
			tick = 5;
		}else{
			return;
		}
		
		if (!getVehicle().getWorld().isRemote) {
			tank.containerTransfer();
		}else if(!isPlaceholder()) {
			if (getShortDw(0) == -1) {
				tank.setFluid(null);
			}else{
				tank.setFluid(new FluidStack(getShortDw(0), getIntDw(1)));		
			}	
		}
	}
	
	@Override
	public ItemStack getInputContainer(int tankId) {
		return getStack(0);
	}	
	
	@Override
	public void clearInputContainer(int tankId) {
		setStack(0, null);	
	}
	
	@Override
	public void addToOutputContainer(int tankId, ItemStack item) {
		addStack(1, item);
	}
	
	@Override
	public void onFluidUpdated(int tankId) {
		if (getVehicle().getWorld().isRemote) {
			return;
		}
	
		updateDw();
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawImage(int tankId, GuiBase gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY) {
		drawImage((GuiVehicle)gui, icon, targetX, targetY, srcX, srcY, sizeX, sizeY);
	}
	

	protected static final int[] TANK_BOUNDS = {35, 20, 36, 51};
    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/tank.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		tank.drawFluid(gui, TANK_BOUNDS[0], TANK_BOUNDS[1]);

		ResourceHelper.bindResource(TEXTURE);
		drawImage(gui, TANK_BOUNDS, 1, 1);
	
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getTankInfo(), x, y, TANK_BOUNDS);
	}	
	

	protected String getTankInfo() {
		String str = tank.getMouseOver();
		if (tank.isLocked()) {
			str += "\n\n" + ColorHelper.GREEN + LocalizationTank.LOCKED.translate() + "\n" + LocalizationTank.UNLOCK.translate();
		}else if (tank.getFluid() != null){
			str += "\n\n" + LocalizationTank.LOCK.translate();
		}
		return str;
	}



	@Override
	public FluidStack getFluid() {
		return tank.getFluid() == null ? null : tank.getFluid().copy();
	}


	@Override
	public int getCapacity() {
		return getTankSize();
	}


	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill, getVehicle().getWorld().isRemote);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain, getVehicle().getWorld().isRemote);
	}


	@Override
	protected void save(NBTTagCompound tagCompound) {
		if (tank.getFluid() != null) {
			NBTTagCompound compound = new NBTTagCompound();
			tank.getFluid().writeToNBT(compound);
			tagCompound.setTag("Fluid", compound);
		}
		tagCompound.setBoolean("Locked", tank.isLocked());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		tank.setFluid(FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag("Fluid")));
		tank.setLocked(tagCompound.getBoolean("Locked"));
		updateDw();	
	}		
	
	//TODO synchronize the tag somehow :S
	@Override
	public int numberOfDataWatchers() {
		return 2;
	}

	protected void updateDw() {
		updateShortDw(0, tank.getFluid() == null ? -1 : tank.getFluid().fluidID);
		updateIntDw(1, tank.getFluid() == null ? -1 : tank.getFluid().amount);	
	}

	@Override
	public void initDw() {
		addShortDw(0, tank.getFluid() == null ? -1 : tank.getFluid().fluidID);
		addIntDw(1, tank.getFluid() == null ? -1 : tank.getFluid().amount);
	}
		
	public float getFluidRenderHeight() {
		if (tank.getFluid() == null) {
			return 0;
		}else{
			return tank.getFluid().amount / (float)getTankSize();
		}
	}
	
	public boolean isCompletelyFilled() {
        return !(getFluid() == null || getFluid().amount < getTankSize());
    }
	
	public boolean isCompletelyEmpty() {
        return getFluid() == null || getFluid().amount == 0;
    }
	

	@Override
	public int getFluidAmount() {
		return getFluid() == null ? 0 : getFluid().amount;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(getFluid(), getCapacity());
	}	

	

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
		if ((getFluid() != null || tank.isLocked() /* just to allow the user to unlock it if something goes wrong*/)) {
			tank.setLocked(!tank.isLocked());
			if (!tank.isLocked() && tank.getFluid() != null && tank.getFluid().amount <= 0) {
				tank.setFluid(null);
				updateDw();
			}
		}
	}	
	
	@Override
	public int numberOfGuiData() {
		return 1;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)(tank.isLocked() ? 1 : 0));
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			tank.setLocked(data != 0);
		}
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (inRect(x, y, TANK_BOUNDS)) {
            DataWriter dw = getDataWriter();
            dw.writeBoolean(button == 0);
            dw.writeBoolean(GuiScreen.isShiftKeyDown());
            sendPacketToServer(dw);
		}
	}
		
}