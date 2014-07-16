package vswe.stevesvehicles.module.common.addon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfo;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfoBoolean;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.localization.entry.module.LocalizationIndependence;
import vswe.stevesvehicles.localization.entry.module.LocalizationVisual;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class ModuleInvisible extends ModuleAddon implements IActivatorModule {
	public ModuleInvisible(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    @Override
    public void loadSimulationInfo(List<SimulationInfo> simulationInfo) {
        simulationInfo.add(new SimulationInfoBoolean(LocalizationAssembler.INFO_INVISIBLE, "invisible"));
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
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}



    @Override
    public int guiWidth() {
        return 90;
    }

    @Override
    public int guiHeight() {
        return 40;
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiVehicle gui, int x, int y) {
        drawToggleBox(gui, "invisible", !isVisible(), x, y);
    }


    @Override
    public void drawMouseOver(GuiVehicle gui, int x, int y) {
        drawStringOnMouseOver(gui, getStateName(), x,y, TOGGLE_IMAGE_RECT);
    }


	@Override 
	public void update() {
		super.update();
		if (!isVisible() && !getVehicle().hasFuelForModule() && !getVehicle().getWorld().isRemote) {
			setIsVisible(true);
		}
	}
	
	private boolean isVisible() {
		if (isPlaceholder()) {
			return !getBooleanSimulationInfo();
		}else{
			return getDw(0) != 0;
		}
	}

	private String getStateName() {
        return LocalizationVisual.INVISIBILITY_TOGGLE.translate(isVisible() ? "0" : "1");
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, TOGGLE_BOX_RECT)) {
				sendPacketToServer(getDataWriter());
			}
		}
	}

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
	    setIsVisible(!isVisible());
	}

	public void setIsVisible(boolean val) {
		updateDw(0, val ? 1 : 0);
	}


    @Override
    public boolean shouldVehicleRender() {
        return isVisible();
    }

    @Override
	public int numberOfDataWatchers() {
		return 1;
	}
	
	@Override
	public void initDw() {
		addDw(0,(byte)1);
	}
	

	
	public int getConsumption(boolean isMoving) {
		return isVisible() ? super.getConsumption(isMoving) : 3;
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("Invisible", !isVisible());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setIsVisible(!tagCompound.getBoolean("Invisible"));
	}	

	public void doActivate(int id) {
		setIsVisible(false);
	}
	public void doDeActivate(int id) {
		setIsVisible(true);
	}
	public boolean isActive(int id) {
		return !isVisible();
	}
}