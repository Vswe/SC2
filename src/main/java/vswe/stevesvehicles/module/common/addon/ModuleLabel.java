package vswe.stevesvehicles.module.common.addon;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationVisual;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.engine.ModuleEngine;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleLabel extends ModuleAddon {

	private ArrayList<LabelInformation> labels;
    private int delay = 0;
    private ArrayList<SlotBase> storageSlots;

	public ModuleLabel(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		labels = new ArrayList<LabelInformation>();
		
		labels.add(new LabelInformation(LocalizationVisual.NAME_LABEL) {
			@Override
			public String getLabel() {
				return getVehicle().getVehicleName();
			}			
		});
			
		labels.add(new LabelInformation(LocalizationVisual.DISTANCE_LABEL) {
			@Override
			public String getLabel() {
				return LocalizationVisual.DISTANCE_MESSAGE.translate(String.valueOf((int)getVehicle().getEntity().getDistanceToEntity(getClientPlayer())));
			}			
		});		
		
		labels.add(new LabelInformation(LocalizationVisual.POSITION_LABEL) {
			@Override
			public String getLabel() {
				return LocalizationVisual.POSITION_MESSAGE.translate(String.valueOf(getVehicle().x()), String.valueOf(getVehicle().y()), String.valueOf(getVehicle().z()));
			}			
		});	
		
		labels.add(new LabelInformation(LocalizationVisual.FUEL_LABEL) {
			@Override
			public String getLabel() {
				int seconds =  getIntDw(1);
				
				if (seconds == -1) {
					return LocalizationVisual.FUEL_MESSAGE_NO_CONSUMPTION.translate();
				}else{
					int minutes = seconds / 60;
					seconds -= minutes * 60;
					int hours = minutes / 60;
					minutes -= hours * 60;

					return String.format(LocalizationVisual.FUEL_MESSAGE.translate() + ": %02d:%02d:%02d", hours, minutes, seconds);
				}
			}			
		});		
		
		labels.add(new LabelInformation(LocalizationVisual.STORAGE_LABEL) {
			@Override
			public String getLabel() {
				int used = getDw(2);
				if (used < 0) {
					used += 256;
				}
				
				return storageSlots == null ? "" : LocalizationVisual.STORAGE_LABEL.translate() + ": " + used + "/" + storageSlots.size() + (storageSlots.size() == 0 ? "" : "[" + (int)(100F * used / storageSlots.size()) + "%]");
			}			
		});			
	}

    @Override
    public void init() {
        storageSlots = new ArrayList<SlotBase>();

        for(ModuleBase module : getVehicle().getModules()) {
            if (module.getSlots() != null) {

                for (SlotBase slot : module.getSlots()) {
                    if (slot instanceof SlotChest) {
                        storageSlots.add(slot);
                    }
                }
            }
        }
    }



    @Override
	public void addToLabel(ArrayList<String> label) {
		for (int i = 0; i < labels.size(); i++) {
			if (isActive(i)) {
				label.add(labels.get(i).getLabel());
			}
		}
	}

	
	private int[] getBoxArea(int i) {
		return new int[] {10, 17 + i * 12, 8, 8};
	}

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/label.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);
		
		for (int i = 0; i < labels.size(); i++) {
			int[] rect = getBoxArea(i);
			drawImage(gui, rect, isActive(i) ? 10 : 1, 1);
			drawImage(gui, rect, inRect(x, y, rect) ? 10 : 1, 10);
		}
	}
	
	private boolean isActive(int i) {
        return !isPlaceholder() && (getDw(0) & (1 << i)) != 0;
	}
	
	private void toggleActive(int i) {
		updateDw(0, getDw(0) ^ (1 << i));
	}
	
	
	@Override
	public int numberOfDataWatchers() {

		return 3;
	}

	
	@Override
	public void initDw() {
		addDw(0, (byte)0);
		addIntDw(1, 0);
		addDw(2, 0);
	}
	

	
	@Override
	public void update() {
		if (!isPlaceholder() && !getVehicle().getWorld().isRemote ) {
			if ( delay <= 0) {
					
				if (isActive(3)) {
					int data = 0;
					
					for (ModuleEngine engine : getVehicle().getEngines()) {
						if (engine.getPriority() != 3) {
							data += engine.getTotalFuel();
						}
					}
					
					if (data != 0) {
						int consumption = getVehicle().getConsumption();
						
						if (consumption == 0) {
							data = -1;
						}else{
							data /= consumption * 20;
						}
					}
					
					updateIntDw(1, data);
				}
				if(isActive(4)) {
					int data = 0;
					for (SlotBase slot : storageSlots) {
						if (slot.getHasStack()) {
							++data;
						}
					}
					updateDw(2, (byte)data);
				}

				delay = 20;
			}else if(delay > 0){
				--delay;
			}
		}
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		for (int i = 0; i < labels.size(); i++) {
			int[] rect = getBoxArea(i);
			
			if (inRect(x, y, rect)) {
                DataWriter dw = getDataWriter();
                dw.writeByte(i);
                sendPacketToServer(dw);
				break;
			}
		}
	}
	
	

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
	    toggleActive(dr.readByte());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
		drawString(gui, LocalizationVisual.LABELS.translate(), 8, 6, 0x404040);
		
		for (int i = 0; i < labels.size(); i++) {
			int[] rect = getBoxArea(i);
			
			drawString(gui, labels.get(i).getName(), rect[0] + 12, rect[1] + 1, 0x404040);
		}		
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	public boolean hasSlots() {
		return false;
	}
	
	@Override
	public int guiWidth() {
		return 92;
	}
	
	@Override
	public int guiHeight() {
		return 77;
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		updateDw(0, tagCompound.getByte("Active"));
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setByte("Active", getDw(0));
	}
}
