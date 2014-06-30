package vswe.stevesvehicles.module.common.engine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotFuel;

public abstract class ModuleCoalBase extends ModuleEngine {
	public ModuleCoalBase(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    /**
    Load new fuel, this is called all the time but has an if statement checking if it's necessary to re-fill fuel.
    **/
	@Override
    protected void loadFuel() {
		int consumption =  getVehicle().getConsumption(true) * 2; //compare the fuel to how much would be required to move 2 ticks
	
        //if there's no fuel left it's time to re-fill
        if (getFuelLevel() <= consumption) {
            //loop through the slots of fuel
            for (int i = 0; i < getInventorySize(); i++) {
                //get the burn time of the fuel
                setFuelLevel(getFuelLevel() + SlotFuel.getItemBurnTime(this, getStack(i)));

                //if the new fuel did a difference that fuel should be remove from its inventory
                if (getFuelLevel() > consumption) {
                    //just to make sure
                    if (getStack(i) != null) {
                        //code for emptying buckets and the like
                        if (getStack(i).getItem().hasContainerItem(getStack(i))) {
                            setStack(i,new ItemStack(getStack(i).getItem().getContainerItem()));
                        }else {
                            //if this isn't a bucket of lava or something similar decrease the number of items.
                            getStack(i).stackSize--;
                        }

                        //an empty stack is not worth anything, remove the stack if so.
                        if (getStack(i).stackSize == 0) {
                           setStack(i,null);
                        }
                    }

                    //after filling the cart with fuel we're done here
                    break;
                }
            }
        }
    }

	@Override
	public int getTotalFuel() {
		int totalFuel = getFuelLevel();
				
		for (int i = 0; i < getInventorySize(); i++) {
			if (getStack(i) != null) {
				totalFuel += SlotFuel.getItemBurnTime(this, getStack(i)) * getStack(i).stackSize;
			}			
		}
				
		return totalFuel;
	}

	@Override
	public float[] getGuiBarColor() {
		return new float[] {0F, 0F, 0F};
	}	

	
	@Override
	public void smoke() {
        double offsetX = 0.0;
        double offsetZ = 0.0;

		if (getVehicle().getEntity().motionX != 0) {
			offsetX =  (getVehicle().getEntity().motionX > 0 ? -1 : 1);
		}

		if (getVehicle().getEntity().motionZ != 0) {
			offsetZ =  (getVehicle().getEntity().motionZ > 0 ? -1 : 1);
		}

        if (getVehicle().getRandom().nextInt(2) == 0) {
			getVehicle().getWorld().spawnParticle("largesmoke", getVehicle().getEntity().posX + offsetX * 0.85, getVehicle().getEntity().posY + 0.12D, getVehicle().getEntity().posZ + offsetZ * 0.85, 0.0D, 0.0D, 0.0D);
		}

		if (getVehicle().getRandom().nextInt(30) == 0) {
            getVehicle().getWorld().spawnParticle("flame", getVehicle().getEntity().posX + offsetX * 0.75, getVehicle().getEntity().posY + 0.15D, getVehicle().getEntity().posZ + offsetZ * 0.75, getVehicle().getEntity().motionX, getVehicle().getEntity().motionY, getVehicle().getEntity().motionZ);
		}
    }

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotFuel(getVehicle().getVehicleEntity(), slotId, 8 + x * 18, 23 + 18 * y);
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.ENGINES.COAL.translate(), 8, 6, 0x404040);
        String str = Localization.MODULES.ENGINES.NO_FUEL.translate();

        if (getFuelLevel() > 0) {
            str = Localization.MODULES.ENGINES.FUEL.translate(String.valueOf(getFuelLevel()));
        }

        drawString(gui,str, 8, 42, 0x404040);
	}

	@Override
	public int numberOfGuiData() {
		return 1;
	}

	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)getFuelLevel());
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			setFuelLevel(data);
			if (getFuelLevel() < 0) {
				setFuelLevel(getFuelLevel() + 65536);
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if (fireCoolDown <= 0) {
			fireIndex = getVehicle().getRandom().nextInt(4) + 1;
			fireCoolDown = 2;
		}else{
			fireCoolDown--;
		}
	}	
	
	private int fireCoolDown;
	private int fireIndex;
	public int getFireIndex() {
		if (getVehicle().isEngineBurning()) {
			return fireIndex;
		}else{
			return 0;
		}
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		super.save(tagCompound);
		tagCompound.setShort("Fuel", (short)getFuelLevel());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		super.load(tagCompound);
		setFuelLevel(tagCompound.getShort("Fuel"));
		if (getFuelLevel() < 0) {
			setFuelLevel(getFuelLevel() + 65536);
		}
	}	
	
	public abstract double getFuelMultiplier();
	
}