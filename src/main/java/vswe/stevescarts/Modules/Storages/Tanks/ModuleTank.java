package vswe.stevescarts.Modules.Storages.Tanks;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.*;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Storages.ModuleStorage;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotLiquidInput;
import vswe.stevescarts.Slots.SlotLiquidOutput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModuleTank extends ModuleStorage implements IFluidTank, ITankHolder {
	public ModuleTank(MinecartModular cart) {
		super(cart);
		
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
			return new SlotLiquidInput(getCart(), tank, -1,  slotId,8+x*18,24+y*24);
		}else{
			return new SlotLiquidOutput(getCart(), slotId,8+x*18,24+y*24);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiMinecart gui) {
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
		
		if (!getCart().worldObj.isRemote) {
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
	public ItemStack getInputContainer(int tankid) {
		return getStack(0);
	}	
	
	@Override
	public void clearInputContainer(int tankid) {
		setStack(0, null);	
	}
	
	@Override
	public void addToOutputContainer(int tankid, ItemStack item) {
		addStack(1, item);
	}
	
	@Override
	public void onFluidUpdated(int tankid) {
		if (getCart().worldObj.isRemote) {
			return;
		}
	
		updateDw();
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawImage(int tankid, GuiBase gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY) {
		drawImage((GuiMinecart)gui, icon, targetX, targetY, srcX, srcY, sizeX, sizeY);
	}
	

	protected int[] tankBounds = {35, 20, 36, 51};
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {

		tank.drawFluid(gui, tankBounds[0], tankBounds[1]);
	

		ResourceHelper.bindResource("/gui/tank.png");		
		drawImage(gui, tankBounds, 0, 0);
	
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, getTankInfo(), x,y, tankBounds);
	}	
	

	protected String getTankInfo() {
		String str = tank.getMouseOver();
		if (tank.isLocked()) {
			str += "\n\n" + ColorHelper.GREEN + Localization.MODULES.TANKS.LOCKED.translate() + "\n" + Localization.MODULES.TANKS.UNLOCK.translate();
		}else if (tank.getFluid() != null){
			str += "\n\n" + Localization.MODULES.TANKS.LOCK.translate();
		}
		return str;
	}


	/**
	 * @return FluidStack representing the fluid contained in the tank, null if empty.
	 */
	@Override
	public FluidStack getFluid() {
		return tank.getFluid() == null ? null : tank.getFluid().copy();
	}

	/**
	 * @return capacity of this tank
	 */
	@Override
	public int getCapacity() {
		return getTankSize();
	}

	/**
	 *
	 * @param resource
	 * @param doFill
	 * @return Amount of fluid used for filling.
	 */
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill, getCart().worldObj.isRemote);
	}
	/**
	 *
	 * @param maxDrain
	 * @param doDrain
	 * @return Null if nothing was drained, otherwise a FluidStack containing the drained.
	 */
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain, getCart().worldObj.isRemote);
	}




	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		if (tank.getFluid() != null) {
			NBTTagCompound compound = new NBTTagCompound();
			tank.getFluid().writeToNBT(compound);
			tagCompound.setTag(generateNBTName("Fluid", id), compound);
		}
		tagCompound.setBoolean(generateNBTName("Locked",id), tank.isLocked());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		tank.setFluid(FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag(generateNBTName("Fluid", id))));
		tank.setLocked(tagCompound.getBoolean(generateNBTName("Locked",id)));
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
		if (getFluid() == null || getFluid().amount < getTankSize()) {
			return false;
		}
		return true;
	}
	
	public boolean isCompletelyEmpty() {
		if (getFluid() == null || getFluid().amount == 0) {
			return true;
		}
		return false;
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
	protected int numberOfPackets() {
		return 1;
	}
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0 && (getFluid() != null || tank.isLocked() /* just to allow the user to unlock it if something goes wrong*/)) {
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
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (inRect(x, y, tankBounds)) {
			byte data = (byte)button;
			if (GuiScreen.isShiftKeyDown()) {
				data |= 2;
			}
			sendPacket(0, data);
		}
	}
		
}