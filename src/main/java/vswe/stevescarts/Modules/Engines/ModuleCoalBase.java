package vswe.stevescarts.Modules.Engines;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelEngineFrame;
import vswe.stevescarts.Models.Cart.ModelEngineInside;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFuel;

public abstract class ModuleCoalBase extends ModuleEngine {
	public ModuleCoalBase(MinecartModular cart) {
		super(cart);
	}

    /**
    Load new fuel, this is called all the time but has an if statement checking if it's necessary to re-fill fuel.
    **/
	@Override
    protected void loadFuel()
    {
		int consumption =  getCart().getConsumption(true) * 2;
	
        //if there's no fuel left it's time to re-fill
        if (getFuelLevel() <= consumption)
        {
            //loop through the slots of fuel
            for (int i = 0; i < getInventorySize(); i++)
            {
                //get the burn time of the fuel
                setFuelLevel(getFuelLevel() + SlotFuel.getItemBurnTime(this, getStack(i)));

                //if the new fuel did a difference that fuel should be remove from its inventory
                if (getFuelLevel() > consumption)
                {
                    //just to make sure
                    if (getStack(i) != null)
                    {
                        //code for emptying buckets and the like
                        if (getStack(i).getItem().hasContainerItem())
                        {
                            setStack(i,new ItemStack(getStack(i).getItem().getContainerItem()));
                        }
                        else
                        {
                            //if this isn't a bucket of lava or something similar decrease the number of items.
                            getStack(i).stackSize--;
                        }

                        //an empty stack is not worth anything, remove the stack if so.
                        if (getStack(i).stackSize == 0)
                        {
                           setStack(i,null);
                        }
                    }

                    //start the cart if it isn't moving
                    /*if (getCart().pushX == 0.0D && getCart().pushZ == 0.0D && getCart().StartOnActivate())
                    {
                        getCart().pushX = getCart().temppushX;
                        getCart().pushZ = getCart().temppushZ;
                    }*/

                    //after filling the cart with fuel we're done here
                    break;
                }
            }
        }
    }

	@Override
	public int getTotalFuel() {
		int totalfuel = getFuelLevel();
				
		for (int i = 0; i < getInventorySize(); i++) {
			if (getStack(i) != null) {
				totalfuel += SlotFuel.getItemBurnTime(this, getStack(i)) * getStack(i).stackSize;
			}			
		}
				
		return totalfuel;
	}
	@Override
	public float[] getGuiBarColor() {
		return new float[] {0F,0F,0F};
	}	
	
	
	
	@Override
	public void smoke()
    {
        double oX = 0.0;
        double oZ = 0.0;

		if (getCart().motionX != 0)
		{
			oX =  (getCart().motionX > 0 ? -1 : 1);
		}

		if (getCart().motionZ != 0)
		{
			oZ =  (getCart().motionZ > 0 ? -1 : 1);
		}

        if (getCart().rand.nextInt(2) == 0) {
			getCart().worldObj.spawnParticle("largesmoke", getCart().posX + oX * 0.85, getCart().posY + 0.12D, getCart().posZ + oZ * 0.85, 0.0D, 0.0D, 0.0D);
		}

		if (getCart().rand.nextInt(30) == 0) {
			//if (getCart().getMaxSpeedRail() == 0) {
				//getCart().worldObj.spawnParticle("flame",getCart().posX + oX* 0.75, getCart().posY + 0.15D, getCart().posZ + oZ*0.75, 0, 0,0);
			//}else{
				getCart().worldObj.spawnParticle("flame",getCart().posX + oX* 0.75, getCart().posY + 0.15D, getCart().posZ + oZ*0.75, getCart().motionX, getCart().motionY, getCart().motionZ);
			//}
		}
    }

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotFuel(getCart(),slotId,8+x*18,23+18*y);
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.ENGINES.COAL.translate(), 8, 6, 0x404040);
        String strfuel = Localization.MODULES.ENGINES.NO_FUEL.translate();

        if (getFuelLevel() > 0)
        {
            strfuel = Localization.MODULES.ENGINES.FUEL.translate(String.valueOf(getFuelLevel()));
        }

        drawString(gui,strfuel, 8, 42, 0x404040);
		//drawString(gui,"Consumption: " + getCart().getConsumption(), 8, 50, 0x404040);
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
			fireIndex = getCart().rand.nextInt(4) + 1;
			fireCoolDown = 2;
		}else{
			fireCoolDown--;
		}
	}	
	
	private int fireCoolDown;
	private int fireIndex;
	public int getFireIndex() {
		if (getCart().isEngineBurning()) {
			return fireIndex;
		}else{
			return 0;
		}
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);
		tagCompound.setShort(generateNBTName("Fuel",id), (short)getFuelLevel());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		setFuelLevel(tagCompound.getShort(generateNBTName("Fuel",id)));
		if (getFuelLevel() < 0) {
			setFuelLevel(getFuelLevel() + 65536);
		}
	}	
	
	public abstract double getFuelMultiplier();
	
}