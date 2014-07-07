package vswe.stevesvehicles.old.TileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.detector.LogicObjectOperator;
import vswe.stevesvehicles.detector.OperatorObject;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.old.Containers.ContainerDetector;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.detector.LogicObject;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityDetector extends TileEntityBase
{

	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiDetector(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerDetector(inv, this);		
	}
	
	public LogicObject mainObj;

    public TileEntityDetector() {
		mainObj = new LogicObjectOperator((byte)0, OperatorObject.MAIN);
    }


	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
		byte count = nbttagcompound.getByte("LogicObjectCount");
		for (int i = 0; i < count; i++) {
			loadLogicObjectFromInteger(nbttagcompound.getInteger("LogicObject" + i));
		}
    }

	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
		int count = saveLogicObject(nbttagcompound, mainObj, 0, false);
		nbttagcompound.setByte("LogicObjectCount", (byte)count);
    }

	private int saveLogicObject(NBTTagCompound nbttagcompound, LogicObject obj, int id, boolean saveMe) {
		if (saveMe) {
			nbttagcompound.setInteger("LogicObject" + id++, saveLogicObjectToInteger(obj));
		}
		
		for (LogicObject child : obj.getChildren()) {
			id = saveLogicObject(nbttagcompound, child, id, true);
		}
		
		
		return id;
	}
	
	private int saveLogicObjectToInteger(LogicObject obj) {
        return (obj.getInfoShort() << 16) | obj.getData();
	}
	
	private void loadLogicObjectFromInteger(int val) {
        System.out.println(val + ": " + Integer.toBinaryString(val));
        short info = (short)((val >> 16) & 65535);
        short data = (short)(val & 65535);

		LogicObject.createObject(this, info, data);
	}


	private int activeTimer = 20;
	
	@Override
    public void updateEntity()
    {	
		if (activeTimer > 0) {
			if (--activeTimer == 0) {
				DetectorType.getTypeFromMeta(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).deactivate(this);
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & ~8, 3);
			}	
		}	
    }



	@Override
	public void receivePacket(int id, byte[] data, EntityPlayer player) {
		//add object
		if (id == 0) {
			byte lowestId = (byte)-1;
			for (int i = 0; i < 128; i++) {
				if (!isIdOccupied(mainObj, i)) {
					lowestId = (byte)i;
					break;
				}
			}
			
			if (lowestId == -1) {
				return;
			}

            LogicObject.createObject(this, lowestId, data);
		//remove object
		}else if(id == 1) {
			removeObject(mainObj, data[0]);
		}
	}

	
	public LogicObject getObjectFromId(LogicObject object, int id) {
		if(object.getId() == id) {	
			return object;
		}
		
		for (LogicObject child : object.getChildren()) {
			LogicObject result = getObjectFromId(child, id);
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}	
	
	private boolean removeObject(LogicObject object, int idToRemove) {
		if(object.getId() == idToRemove) {
			object.setParent(null);		
			return true;
		}
		
		for (LogicObject child : object.getChildren()) {
			if (removeObject(child, idToRemove)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isIdOccupied(LogicObject object, int id) {
		if(object.getId() == id) {
			return true;
		}
		
		for (LogicObject child : object.getChildren()) {
			if (isIdOccupied(child, id)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void initGuiData(Container con, ICrafting crafting) {
		//sendAllLogicObjects(con, crafting, mainObj);
	}

	@Override
	public void checkGuiData(Container con, ICrafting crafting) {
		sendUpdatedLogicObjects(con, crafting, mainObj, ((ContainerDetector)con).mainObj);	
	}
	
	private void sendUpdatedLogicObjects(Container con, ICrafting crafting, LogicObject real, LogicObject cache) {
		if (!real.equals(cache)) {
			LogicObject parent = cache.getParent();
			cache.setParent(null);
			LogicObject clone = real.copy(parent);
			removeLogicObject(con, crafting, cache);
			sendLogicObject(con, crafting, clone);	
			cache = clone;			
		}

			
		while (real.getChildren().size() > cache.getChildren().size()) {
			int i = cache.getChildren().size();
			LogicObject clone = real.getChildren().get(i).copy(cache);
			sendLogicObject(con, crafting, clone);		
		}
		
		while (real.getChildren().size() < cache.getChildren().size()) {
			int i = real.getChildren().size();
			LogicObject toBeRemoved = cache.getChildren().get(i);
			toBeRemoved.setParent(null);
			removeLogicObject(con, crafting, toBeRemoved);				
		}		
		
		for (int i = 0; i < real.getChildren().size(); i++) {
			sendUpdatedLogicObjects(con, crafting, real.getChildren().get(i), cache.getChildren().get(i));
		}
	}

	private void sendLogicObject(Container con, ICrafting crafting, LogicObject obj) {
		if (obj.getParent() == null) {
			return;
		}

		updateGuiData(con, crafting, 0, obj.getInfoShort());
		updateGuiData(con, crafting, 1, obj.getData());
	}
	
	private void removeLogicObject(Container con, ICrafting crafting, LogicObject obj) {
		updateGuiData(con, crafting, 2, obj.getId());
	}
	
	private short oldData;
	private boolean hasOldData;
	@Override
	public void receiveGuiData(int id, short data) {	
		if (id == 0) {
			oldData = data;
			hasOldData = true;
		}else if(id == 1) {
			if (!hasOldData) {
				System.out.println("Doesn't have the other part of the data");
				return;
			}

			LogicObject.createObject(this, oldData, data);
			recalculateTree();	
			hasOldData = false;
		}else if(id == 2) {
			removeObject(mainObj, data);
			recalculateTree();	
		}
	}
	
	public void recalculateTree() {
		mainObj.generatePosition(5, 60, 245, 0);
	}
	
	public boolean evaluate(VehicleBase vehicle , int depth) {
		return mainObj.evaluateLogicTree(this, vehicle, depth);
	}
	
	public void handleCart(VehicleBase vehicle) {
		boolean truthValue = evaluate(vehicle, 0);

		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		boolean isOn = (meta & 8) != 0;
		
		if (truthValue != isOn) {
			if (truthValue) {
				DetectorType.getTypeFromMeta(meta).activate(this, vehicle);
				meta |= 8;				
			}else{
				DetectorType.getTypeFromMeta(meta).deactivate(this);
				meta &= ~8;
			}
			
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 3);
		}
		
		if (truthValue) {
			activeTimer = 20;
		}
	}

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
    }

 
	
	
}
