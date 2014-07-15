package vswe.stevesvehicles.module.common.storage.tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.localization.entry.module.LocalizationTank;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.gui.ColorHelper;


public class ModuleCheatTank extends ModuleTank{
	public ModuleCheatTank(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	private static final ColorHelper[] colors = {ColorHelper.YELLOW, ColorHelper.GREEN, ColorHelper.RED, ColorHelper.ORANGE};
	
	private int mode;
	
	@Override
	protected String getTankInfo() {
		String str = super.getTankInfo();
		str += "\n\n" + LocalizationTank.CREATIVE_MODE.translate(colors[mode].toString(), String.valueOf(mode)) + "\n" + LocalizationTank.CREATIVE_CHANGE_MODE.translate();
		if (mode != 0) {
			str += "\n" + LocalizationTank.CREATIVE_RESET_MODE.translate();
		}
		return str;
	}

	
	@Override
	protected int getTankSize() {
		return 2000000000;
	}

	@Override
	public boolean hasVisualTank() {
		return false;
	}
	

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
        int button = dr.readBoolean() ? 0 : 1;
        boolean isShift = dr.readBoolean();
		if (button != 0) {
			if (mode != 0 && isShift) {
				mode = 0;
			}else if (++mode == colors.length) {
				mode = 1;
			}
			
			updateAmount();
			updateDw();
		}else{
			super.receivePacket(dr, player);
		}
	}	

	@Override
	public int numberOfGuiData() {
		return super.numberOfGuiData() + 1;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {
		super.checkGuiData(info);
		
		updateGuiData(info, super.numberOfGuiData(), (short)mode);
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == super.numberOfGuiData()) {
			mode = data;
		}else{
			super.receiveGuiData(id, data);
		}
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		super.save(tagCompound);
		tagCompound.setByte("mode", (byte)mode);
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		super.load(tagCompound);
		mode = tagCompound.getByte("mode");
	}	
		
	private void updateAmount() {
		if (tank.getFluid() != null) {
			if (mode == 1) {
				tank.getFluid().amount = getTankSize();
			}else if(mode == 2) {
				tank.getFluid().amount = 0;
				if (!tank.isLocked()) {
					tank.setFluid(null);
				}
			}else if(mode == 3) {
				tank.getFluid().amount = getTankSize() / 2;
			}
		}
	}
	
	@Override
	public void onFluidUpdated(int tankId) {
		updateAmount();		
		super.onFluidUpdated(tankId);
	}
	
}