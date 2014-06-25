package vswe.stevesvehicles.old.Modules.Storages.Tanks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.ColorHelper;
import vswe.stevesvehicles.old.Helpers.Localization;


public class ModuleCheatTank extends ModuleTank{
	public ModuleCheatTank(EntityModularCart cart) {
		super(cart);
	}

	private static final ColorHelper[] colors = {ColorHelper.YELLOW, ColorHelper.GREEN, ColorHelper.RED, ColorHelper.ORANGE};
	
	private int mode;
	
	@Override
	protected String getTankInfo() {
		String str = super.getTankInfo();
		str += "\n\n" + Localization.MODULES.TANKS.CREATIVE_MODE.translate(colors[mode].toString(), String.valueOf(mode)) + "\n" + Localization.MODULES.TANKS.CHANGE_MODE.translate();
		if (mode != 0) {
			str += "\n" + Localization.MODULES.TANKS.RESET_MODE.translate();
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
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0 && (data[0] & 1) != 0) {
			if (mode != 0 && (data[0] & 2) != 0) {
				mode = 0;
			}else if (++mode == colors.length) {
				mode = 1;
			}
			
			updateAmount();
			updateDw();
		}else{
			super.receivePacket(id, data, player);
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
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);
		tagCompound.setByte(generateNBTName("mode",id), (byte)mode);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		mode = tagCompound.getByte(generateNBTName("mode",id));	
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
	public void onFluidUpdated(int tankid) {		
		updateAmount();		
		super.onFluidUpdated(tankid);
	}
	
}