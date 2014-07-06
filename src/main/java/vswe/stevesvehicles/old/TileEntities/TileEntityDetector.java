package vswe.stevesvehicles.old.TileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.old.Containers.ContainerDetector;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.old.Helpers.LogicObject;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.old.Interfaces.GuiDetector;
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

    public TileEntityDetector()
    {
		mainObj = new LogicObject((byte)1, (byte)0);
    }


	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
		byte count = nbttagcompound.getByte("LogicObjectCount");
		for (int i = 0; i < count; i++) {
			loadLogicObjectFromInteger(nbttagcompound.getInteger("LogicObject" + i));
		}
    }

	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
		int count = saveLogicObject(nbttagcompound, mainObj, 0, false);
		nbttagcompound.setByte("LogicObjectCount", (byte)count);
    }

	private int saveLogicObject(NBTTagCompound nbttagcompound, LogicObject obj, int id, boolean saveMe) {
		if (saveMe) {
			nbttagcompound.setInteger("LogicObject" + id++, saveLogicObjectToInteger(obj));
		}
		
		for (LogicObject child : obj.getChilds()) {
			id = saveLogicObject(nbttagcompound, child, id, true);
		}
		
		
		return id;
	}
	
	private int saveLogicObjectToInteger(LogicObject obj) {
		int returnVal = 0;
		returnVal |= obj.getId() << (8 * 3);
		returnVal |= obj.getParent().getId() << (8 * 2);
		returnVal |= obj.getExtra() << (8 * 1);	
		returnVal |= obj.getData() << (8 * 0);
		return returnVal;
	}
	
	private void loadLogicObjectFromInteger(int val) {	
		byte id = (byte)((val >> (8*3)) & 255);
		byte parent = (byte)((val >> (8*2)) & 255);
		byte extra = (byte)((val >> (8*1)) & 255);
		byte data = (byte)((val >> (8*0)) & 255);
		
		createObject(id, parent, extra, data);		
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
			

			createObject(lowestId, data[0], data[1], data[2]);
		//remove object
		}else if(id == 1) {
			removeObject(mainObj, data[0]);
		}
	}
	
	private void createObject(byte id, byte parentId, byte extra, byte data) {
		LogicObject newObject = new LogicObject(id, extra, data);
		
		LogicObject parent = getObjectFromId(mainObj,parentId);
		if (parent != null) {
			newObject.setParent(parent);
		}	
	}
	
	private LogicObject getObjectFromId(LogicObject object, int id) {
		if(object.getId() == id) {	
			return object;
		}
		
		for (LogicObject child : object.getChilds()) {
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
		
		for (LogicObject child : object.getChilds()) {
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
		
		for (LogicObject child : object.getChilds()) {
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

			
		while (real.getChilds().size() > cache.getChilds().size()) {
			int i = cache.getChilds().size();
			LogicObject clone = real.getChilds().get(i).copy(cache);
			sendLogicObject(con, crafting, clone);		
		}
		
		while (real.getChilds().size() < cache.getChilds().size()) {
			int i = real.getChilds().size();
			LogicObject toBeRemoved = cache.getChilds().get(i);
			toBeRemoved.setParent(null);
			removeLogicObject(con, crafting, toBeRemoved);				
		}		
		
		for (int i = 0; i < real.getChilds().size(); i++) {
			sendUpdatedLogicObjects(con, crafting, real.getChilds().get(i), cache.getChilds().get(i));
		}
	}
	
	private void sendAllLogicObjects(Container con, ICrafting crafting, LogicObject obj) {
		sendLogicObject(con, crafting, obj);
		
		for (LogicObject child : obj.getChilds()) {
			sendAllLogicObjects(con, crafting, child);
		}
	}
	
	private void sendLogicObject(Container con, ICrafting crafting, LogicObject obj) {
		if (obj.getParent() == null) {
			return;
		}
		short data = (short)((obj.getId() << 8) | obj.getParent().getId());
		short data2 = (short)((obj.getExtra() << 8) | obj.getData());
	
	
		updateGuiData(con, crafting, 0, data);
		updateGuiData(con, crafting, 1, data2);				
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
			byte logicid = (byte)((oldData & (255 << 8)) >> 8);
			byte parent = (byte)(oldData & 255);
			byte extra = (byte)((data & (255 << 8)) >> 8);
			byte logicdata = (byte)(data & 255);
				
			createObject(logicid, parent, extra, logicdata);	
			recalculateTree();	
			hasOldData = false;
		}else if(id == 2) {
			removeObject(mainObj, data);
			recalculateTree();	
		}
	}
	
	public void recalculateTree() {
		mainObj.generatePosition(5,60,245,0);
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
			//worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, StevesCarts.instance.detectorUnit.blockID);
			

		}
		
		if (truthValue) {
			activeTimer = 20;
		}		

	}


    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

 
	
	
}
