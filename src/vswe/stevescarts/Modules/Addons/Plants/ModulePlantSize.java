package vswe.stevescarts.Modules.Addons.Plants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.ModuleAddon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModulePlantSize extends ModuleAddon {

	public ModulePlantSize(MinecartModular cart) {
		super(cart);
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
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.ADDONS.PLANTER_RANGE.translate(), 8, 6, 0x404040);
	}	
		
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/plantsize.png");

		int srcX = ((size - 1) % 5) * 44;
		int srcY = ((size - 1) / 5 + 1) * 44;
		drawImage(gui, boxrect, srcX, srcY);
		
		if (inRect(x,y, boxrect)) {
			drawImage(gui, boxrect, 0, 0);
		}
			
	}

	private int[] boxrect = new int[] {10,18, 44, 44};	
	
	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.SAPLING_AMOUNT.translate() + ": " + size + "x" + size, x,y,boxrect);
	}
	
	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0 || button == 1) {
			if (inRect(x,y, boxrect)) {
				sendPacket(0, (byte)button);
			}
		}
	}
	

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			if (data[0] == 1) {
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
	}

	@Override
	public int numberOfPackets() {
		return 1;
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
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte(generateNBTName("size",id), (byte)size);
	}	
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		size = tagCompound.getByte(generateNBTName("size",id));
	}		
	
}
