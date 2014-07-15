package vswe.stevesvehicles.module.cart.addon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfo;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfoBoolean;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.localization.entry.module.cart.LocalizationCartTravel;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.cart.ILeverModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class ModuleBrake extends ModuleAddon implements ILeverModule {
	public ModuleBrake(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    @Override
    public void loadSimulationInfo(List<SimulationInfo> simulationInfo) {
        simulationInfo.add(new SimulationInfoBoolean(LocalizationAssembler.INFO_BRAKE, "brake"));
    }

    @Override
	public boolean hasSlots() {
		return false;
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return 80;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationCartTravel.LEVER_TITLE.translate(), 8, 6, 0x404040);
	}


	private static final int TEXTURE_SPACING = 1;
    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/lever.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);

		drawButton(gui, x, y, START_STOP_RECT, isForceStopping() ? 2 : 1);
		drawButton(gui, x, y, TURN_BACK_RECT, 0);
	}

	private void drawButton(GuiVehicle gui, int x, int y, int[] coordinates, int imageId) {
		if (inRect(x,y, coordinates)) {
			drawImage(gui ,coordinates, TEXTURE_SPACING, TEXTURE_SPACING * 2 + coordinates[3]);
		}else{
			drawImage(gui, coordinates, TEXTURE_SPACING, TEXTURE_SPACING);
		}

		int srcY = TEXTURE_SPACING + (TEXTURE_SPACING + coordinates[3]) * 2 + imageId * (TEXTURE_SPACING + coordinates[3] - 2);
		drawImage(gui, coordinates[0] + 1, coordinates[1] + 1, 0, srcY, coordinates[2] - 2, coordinates[3] - 2);
	}

	private static final int[] START_STOP_RECT = new int[] {15,20, 24, 12};
	private static final int[] TURN_BACK_RECT = new int[] {START_STOP_RECT[0] + START_STOP_RECT[2] + 5, START_STOP_RECT[1], START_STOP_RECT[2], START_STOP_RECT[3]};

	@Override
	public boolean stopEngines() {
		return isForceStopping();
	}

	private boolean isForceStopping() {
		if (isPlaceholder())  {
			return getBooleanSimulationInfo();
		}else{
			return getDw(0) != 0;
		}
	}

	private void setForceStopping(boolean val) {
		updateDw(0, (byte)(val ? 1 : 0));
	}

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, isForceStopping() ? LocalizationCartTravel.LEVER_START.translate() : LocalizationCartTravel.LEVER_STOP.translate(), x,y, START_STOP_RECT);
		drawStringOnMouseOver(gui, LocalizationCartTravel.LEVER_TURN_BACK.translate(), x,y, TURN_BACK_RECT);
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, START_STOP_RECT)) {
                DataWriter dw = getDataWriter();
                dw.writeBoolean(true);
				sendPacketToServer(dw);
			}else if (inRect(x,y, TURN_BACK_RECT)) {
                DataWriter dw = getDataWriter();
                dw.writeBoolean(false);
                sendPacketToServer(dw);
			}
		}
	}

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
		if (dr.readBoolean()) {
			setForceStopping(!isForceStopping());
		}else{
			turnback();
		}
	}


	@Override
	public float getLeverState() {
		if (isForceStopping()) {
			return 0;
		}else{
			return 1;
		}
	}
	

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0,0);
	}

	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("ForceStop", isForceStopping());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setForceStopping(tagCompound.getBoolean("ForceStop"));
	}		
}