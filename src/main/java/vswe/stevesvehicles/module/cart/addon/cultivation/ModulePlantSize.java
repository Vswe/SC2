package vswe.stevesvehicles.module.cart.addon.cultivation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.cart.LocalizationCartCultivationUtil;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModulePlantSize extends ModuleAddon {

	public ModulePlantSize(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	
	private int size = 1;
	public int getSize() {
		return size;
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
		return 70;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationCartCultivationUtil.PLANTER_RANGE_TITLE.translate(), 8, 6, 0x404040);
	}	
		
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/plant_size.png");

		int srcX = 1 + ((size - 1) % 5) * 45;
		int srcY = 1 + ((size - 1) / 5 + 1) * 45;
		drawImage(gui, box, srcX, srcY);
		
		if (inRect(x,y, box)) {
			drawImage(gui, box, 1, 1);
		}
			
	}

	private int[] box = new int[] {10,18, 44, 44};
	
	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, LocalizationCartCultivationUtil.PLANTER_RANGE_SIZE.translate() + ": " + size + "x" + size, x,y, box);
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0 || button == 1) {
			if (inRect(x,y, box)) {
                DataWriter dw = getDataWriter();
                dw.writeBoolean(button == 0);
				sendPacketToServer(dw);
			}
		}
	}
	

	protected void receivePacket(DataReader dr, EntityPlayer player) {
        if (dr.readBoolean()) {
            size--;
            if (size < 1) {
                size = 7;
            }
        }else{
            size++;
            if (size > 7) {
                size = 1;
            }
        }
	}

	
	@Override
	public int numberOfGuiData() {
		return 1;
	}

	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)size);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			size = data;
		}
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setByte("size", (byte)size);
	}	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		size = tagCompound.getByte("size");
	}		
	
}
