package vswe.stevescarts.old.Modules.Addons;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.LabelInformation;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Engines.ModuleEngine;
import vswe.stevescarts.old.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.old.Slots.SlotBase;
import vswe.stevescarts.old.Slots.SlotChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleLabel extends ModuleAddon {

	private ArrayList<LabelInformation> labels;
    private int delay = 0;
    private ArrayList<SlotBase> storageSlots;
    private ModuleTool tool;

	public ModuleLabel(EntityModularCart cart) {
		super(cart);
		
		labels = new ArrayList<LabelInformation>();
		
		labels.add(new LabelInformation(Localization.MODULES.ADDONS.NAME) {
			@Override
			public String getLabel() {
				return getCart().getCartName();
			}			
		});
			
		labels.add(new LabelInformation(Localization.MODULES.ADDONS.DISTANCE) {
			@Override
			public String getLabel() {
				return Localization.MODULES.ADDONS.DISTANCE_LONG.translate(String.valueOf((int)getCart().getDistanceToEntity(getClientPlayer())));
			}			
		});		
		
		labels.add(new LabelInformation(Localization.MODULES.ADDONS.POSITION) {
			@Override
			public String getLabel() {
				return Localization.MODULES.ADDONS.POSITION_LONG.translate(String.valueOf(getCart().x()), String.valueOf(getCart().y()), String.valueOf(getCart().z()));
			}			
		});	
		
		labels.add(new LabelInformation(Localization.MODULES.ADDONS.FUEL) {
			@Override
			public String getLabel() {
				int seconds =  getIntDw(1);
				
				if (seconds == -1) {
					return Localization.MODULES.ADDONS.FUEL_NO_CONSUMPTION.translate();
				}else{
					int minutes = seconds / 60;
					seconds -= minutes * 60;
					int hours = minutes / 60;
					minutes -= hours * 60;

					return String.format(Localization.MODULES.ADDONS.FUEL_LONG.translate() + ": %02d:%02d:%02d", hours, minutes, seconds);
				}
			}			
		});		
		
		labels.add(new LabelInformation(Localization.MODULES.ADDONS.STORAGE) {
			@Override
			public String getLabel() {
				int used = getDw(2);
				if (used < 0) {
					used += 256;
				}
				
				return storageSlots == null ? "" : Localization.MODULES.ADDONS.STORAGE.translate() + ": " + used + "/" + storageSlots.size() + (storageSlots.size() == 0 ? "" : "[" + (int)(100F * used / storageSlots.size()) + "%]");
			}			
		});			
	}

    @Override
    public void preInit() {
        if (getCart().getModules() != null) {
            for (ModuleBase moduleBase : getCart().getModules()) {
                if (moduleBase instanceof ModuleTool) {
                    tool = (ModuleTool)moduleBase;

                    labels.add(new LabelInformation(Localization.MODULES.ADDONS.DURABILITY) {
                        @Override
                        public String getLabel() {
                            if (tool.useDurability()) {
                                int data = getIntDw(3);
                                if (data == 0) {
                                    return Localization.MODULES.ADDONS.BROKEN.translate();
                                }else if(data > 0) {
                                    return Localization.MODULES.ADDONS.DURABILITY.translate() + ": " + data + " / " + tool.getMaxDurability() + " [" + ((100 * data) / tool.getMaxDurability()) + "%]";
                                }else if(data == -1) {
                                    return "";
                                }else if (data == -2) {
                                    return Localization.MODULES.ADDONS.NOT_BROKEN.translate();
                                }else{
                                    return Localization.MODULES.ADDONS.REPAIR.translate() + " [" + -(data + 3) + "%]";
                                }
                            }else{
                                return Localization.MODULES.ADDONS.UNBREAKABLE.translate();
                            }
                        }
                    });

                    break;
                }
            }
        }
    }

    @Override
    public void init() {
        storageSlots = new ArrayList<SlotBase>();

        for(ModuleBase module : getCart().getModules()) {
            if (module.getSlots() != null) {

                for (SlotBase slot : module.getSlots()) {
                    if (slot instanceof SlotChest) {
                        storageSlots.add(slot);
                    }
                }
            }
        }
    }

    private boolean hasTool() {
        return  tool != null;
    }

    private boolean hasToolWithDurability() {
        return  hasTool() && tool.useDurability();
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

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/label.png");
		
		for (int i = 0; i < labels.size(); i++) {
			int[] rect = getBoxArea(i);
			drawImage(gui, rect, isActive(i) ? 8 : 0, 0);
			drawImage(gui, rect, inRect(x, y, rect) ? 8 : 0, 8);
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
        int count = 3;

        if (hasToolWithDurability()) {
            count++;
        }

		return count;
	}

	
	@Override
	public void initDw() {
		addDw(0, (byte)0);
		addIntDw(1, 0);
		addDw(2, 0);

       if (hasToolWithDurability()) {
           // >= 0 - Current durability
           // = -1 - Invalid
           // = -2 - Can't repair
           // < -2 - Repairing, percentage = -(value + 3)
           addIntDw(3, -1);
       }
	}
	

	
	@Override
	public void update() {
		if (!isPlaceholder() && !getCart().worldObj.isRemote ) {
			if ( delay <= 0) {
					
				if (isActive(3)) {
					int data = 0;
					
					for (ModuleEngine engine : getCart().getEngines()) {
						if (engine.getPriority() != 3) {
							data += engine.getTotalFuel();
						}
					}
					
					if (data != 0) {
						int consumption = getCart().getConsumption();
						
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
                if (hasToolWithDurability()) {
                    if (isActive(5)) {
                        if (tool.isRepairing()) {
                            if (tool.isActuallyRepairing()) {
                                updateIntDw(3, -3 - tool.getRepairPercentage());
                            }else{
                                updateIntDw(3, -2);
                            }
                        }else{
                            updateIntDw(3, tool.getCurrentDurability());
                        }
                    }else if (getIntDw(3) != -1){
                        updateIntDw(3, -1);
                    }
                }

				delay = 20;
			}else if(delay > 0){
				--delay;
			}
		}
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		for (int i = 0; i < labels.size(); i++) {
			int[] rect = getBoxArea(i);
			
			if (inRect(x, y, rect)) {
				sendPacket(0, (byte)i);
				break;
			}
		}
	}
	
	
	@Override
	protected int numberOfPackets() {
		return 1;
	}
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			toggleActive(data[0]);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiMinecart gui) {
		drawString(gui, Localization.MODULES.ADDONS.LABELS.translate(), 8, 6, 0x404040);
		
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
	protected void Load(NBTTagCompound tagCompound, int id) {
		updateDw(0,tagCompound.getByte(generateNBTName("Active",id)));
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte(generateNBTName("Active",id), getDw(0));	
	}
}
