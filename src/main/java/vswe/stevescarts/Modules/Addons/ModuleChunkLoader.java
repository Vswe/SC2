package vswe.stevescarts.Modules.Addons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleChunkLoader extends ModuleAddon implements IActivatorModule{
	public ModuleChunkLoader(MinecartModular cart) {
		super(cart);
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
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui,"Chunk Loader", 8, 6, 0x404040);
	}

   public void update()
    {
        super.update();

		if (!rdyToInit) {
			rdyToInit = true;
		}
		
		if (isLoadingChunk() &&  !getCart().hasFuelForModule() && !getCart().worldObj.isRemote) {
			setChunkLoading(false);
		}	
    }	
	
	private boolean rdyToInit;
	public void setChunkLoading(boolean val) {
	
		if (!isPlaceholder()) {
			updateDw(0, (byte)(val ? 1 : 0));
			
			//just to make sure
			if (!getCart().worldObj.isRemote && rdyToInit) {
				if (val) {
					getCart().initChunkLoading();
				}else{
					getCart().dropChunkLoading();
				}
			}
		}
	}
	
	
	private boolean isLoadingChunk() {
		if (isPlaceholder()) {
			return false;//getSimInfo().getShieldActive();
		}else{		
			return getDw(0) != 0;
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/chunk.png");

		int imageID = isLoadingChunk() ? 1 : 0;
		int borderID = 0;
		if (inRect(x,y, buttonRect)) {
			borderID = 1;			
		}

		drawImage(gui,buttonRect, 0, buttonRect[3] * borderID);

		int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
		drawImage(gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
	}

	private int[] buttonRect = new int[] {20,20, 24, 12};

	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y,buttonRect);
	}


	private String getStateName() {
		if (!isLoadingChunk()) {
			return "Activate chunk loading";
		}else{
			return "Deactivate chunk loading";
		}
	}

	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, buttonRect)) {
				sendPacket(0);
			}
		}
	}
	

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			setChunkLoading(!isLoadingChunk());
		}
	}

	@Override
	public int numberOfPackets() {
		return 1;
	}


	@Override
	public int numberOfDataWatchers() {
		return 1;
	}
	
	@Override
	public void initDw() {
		addDw(0,(byte)0);
	}	
	
	public int getConsumption(boolean isMoving) {
		return isLoadingChunk() ? 5 : super.getConsumption(isMoving);
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setBoolean(generateNBTName("ChunkLoading",id), isLoadingChunk());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setChunkLoading(tagCompound.getBoolean(generateNBTName("ChunkLoading",id)));		
	}	

	public void doActivate(int id) {
		setChunkLoading(true);
	}
	public void doDeActivate(int id) {
		setChunkLoading(false);
	}
	public boolean isActive(int id) {
		return isLoadingChunk();
	}
	
}