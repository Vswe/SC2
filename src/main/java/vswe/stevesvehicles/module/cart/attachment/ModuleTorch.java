package vswe.stevesvehicles.module.cart.attachment;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.cart.ModuleWorker;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.old.Slots.SlotBase;
import vswe.stevesvehicles.old.Slots.SlotTorch;

public class ModuleTorch extends ModuleWorker implements ISuppliesModule  {
	public ModuleTorch(VehicleBase vehicleBase) {
		super(vehicleBase);
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
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotTorch(getVehicle().getVehicleEntity() ,slotId, 8 + x * 18 , 23 + y * 18);
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 95;
	}

	@Override
	public boolean work() {
        //get the next block
        Vec3 next = getLastBlock();
        //save the next block's coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

        //if it's too dark, try to place torches
        if (light <= lightLimit) {
            //try to place it at both sides
            for (int side = -1; side <= 1; side += 2) {

                //calculate the x and z coordinates, this depends on which direction the cart is going
                int xTorch = x + (getVehicle().z() != z ? side : 0);
                int zTorch = z + (getVehicle().x() != x ? side : 0);

                //now it's time to find a proper y value
                for (int level = 2; level >= -2; level--) {

                    //check if this coordinate is a valid place to place a torch
                    if (getVehicle().getWorld().isAirBlock(xTorch, y + level, zTorch)  && Blocks.torch.canPlaceBlockAt(getVehicle().getWorld(), xTorch, y + level, zTorch)) {

                        //check if there's any torches to place
                        for (int i = 0; i < getInventorySize(); i++) {

                            //check if the slot contains torches
                            if (getStack(i) != null) {

                                if (Block.getBlockFromItem(getStack(i).getItem()) == Blocks.torch) {
                                    if (doPreWork()) {
										startWorking(3);
										return true;
									}

									//if so place it and remove one torch from the cart's inventory
                                    getVehicle().getWorld().setBlock(xTorch, y + level, zTorch, Blocks.torch);
        							if (!getVehicle().hasCreativeSupplies()) {
	                                    getStack(i).stackSize--;
	
	                                    if (getStack(i).stackSize == 0) {
	                                        setStack(i,null);
	                                    }
	
										this.onInventoryChanged();
        							}
                                    break;
                                }
                            }
                        }

                        break;

                    //if it isn't valid but there's already a torch there this side is already done. This shouldn't really happen since then it wouldn't be dark enough in the first place.
                    } else if (getVehicle().getWorld().getBlock(xTorch, y + level, zTorch) == Blocks.torch) {
                        break;
                    }
                }
            }
        }
		stopWorking();
		return false;
	}

	private int light;
	private int lightLimit = 8;

	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/torch.png");

		int barLength = 3 * light;
		if (light == 15) {
			barLength--;
		}
		
		
		
		int srcX = 0;
		if (inRect(x,y, boxRect)) {
			srcX += boxRect[2];
		}		

		drawImage(gui, boxRect, srcX, 0);
		drawImage(gui, 12+1, guiHeight()-10 + 1, 0, 9, barLength, 7);
		drawImage(gui, 12 + 3 * lightLimit,guiHeight()-10,0, 16 , 1, 9);
	}
	
	private int[] boxRect = new int[] {12, guiHeight()-10, 46, 9};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, "Threshold: " + lightLimit + " Current: " + light, x,y, boxRect);
	}	
	
	@Override
	public int guiHeight() {
		return super.guiHeight() + 10;
	}
	@Override
	public int numberOfGuiData() {
		return 2;
	}

	@Override
	protected void checkGuiData(Object[] info) {
		short data = (short)(light & 15);
		data |= (short)((lightLimit & 15) << 4);
		
		
		updateGuiData(info, 0, data);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			light = data & 15;
			lightLimit = (data & 240) >> 4;
		}
	}
	
	@Override
	public int numberOfPackets() {
		return 1;
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			lightLimit = data[0];
			if (lightLimit < 0) {
				lightLimit = 0;
			}else if (lightLimit > 15) {
				lightLimit = 15;
			}			
		}
	}
	
	boolean markerMoving = false;
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, boxRect)) {
				generatePacket(x,y);
				markerMoving = true;
			}
		}
	}	
	
	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if(markerMoving){
			generatePacket(x,y);
		}

        if (button != -1)
        {
            markerMoving = false;
        }
	}	
	
	private void generatePacket(int x, int y) {
		int xInBox = x - boxRect[0];
		int val = xInBox / 3;
		if (val < 0) {
			val = 0;
		}else if (val > 15) {
			val = 15;
		}
		sendPacket(0, (byte)val);	
	}
	
	public void setThreshold(byte val) {
		lightLimit = val;
	}
	
	public int getThreshold() {
		return lightLimit;
	}	
	
	public int getLightLevel() {
		return light;
	}
	
	@Override
	public void update() {
		super.update();

		light = getVehicle().getWorld().getBlockLightValue(getVehicle().x(), getVehicle().y() + 1, getVehicle().z());
	}

	@Override
	public void initDw() {
		addDw(0,0);
	}
	@Override
	public int numberOfDataWatchers() {
		return 1;
	}
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();

		calculateTorches();
	}

	private void calculateTorches() {
		if (getVehicle().getWorld().isRemote) {
			return;
		}
	
		int val = 0;

		for (int i = 0; i < 3; i++) {
			val |= ((getStack(i) != null ? 1 : 0) << i);
		}

		updateDw(0,val);
	}

	public int getTorches() {
		if (isPlaceholder()) {
			return getSimInfo().getTorchInfo();
		}else{
			return getDw(0);
		}
	}

	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte("lightLimit", (byte)lightLimit);
	}	
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		lightLimit = tagCompound.getByte("lightLimit");
	
		calculateTorches();
	}	

	@Override
	public boolean haveSupplies() {
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			if (item != null && Block.getBlockFromItem(item.getItem()) == Blocks.torch) {
				return true;
			}
		}
		return false;
	}	

}