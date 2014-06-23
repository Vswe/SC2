package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCake;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleCakeServer extends ModuleBase implements ISuppliesModule {

	public ModuleCakeServer(MinecartModular cart) {
		super(cart);
	}

	
	
	private int cooldown = 0;
	private static final int MAX_CAKES = 10;
	private static final int SLICES_PER_CAKE = 6;
	private static final int MAX_TOTAL_SLICES = ((MAX_CAKES + 1) * SLICES_PER_CAKE);
	
	@Override
	public void update() {
		super.update();
		
		if (!getCart().worldObj.isRemote) {
			if (getCart().hasCreativeSupplies()) {
				if (cooldown >= 20) {
					if (getCakeBuffer() < MAX_TOTAL_SLICES) {
						setCakeBuffer(getCakeBuffer() + 1);
					}
					
					cooldown = 0;
				}else{
					++cooldown;
				}
			}
			
			ItemStack item = getStack(0);
			if (item != null && item.getItem().equals(Items.cake) && getCakeBuffer() + SLICES_PER_CAKE <= MAX_TOTAL_SLICES) {
				setCakeBuffer(getCakeBuffer() + SLICES_PER_CAKE);
				setStack(0, null);
			}
		}
	}


	private void setCakeBuffer(int i) {
		updateShortDw(0, i);
	}


	private int getCakeBuffer() {
		if (isPlaceholder()) {
			return 6;
		}
		return getShortDw(0);
	}


	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addShortDw(0,0);	
	}
	
	
	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	protected int getInventoryWidth() {
		return 1;
	}
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotCake(getCart(),slotId,8+x*18,38+y*18);
	}	
	
	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.ATTACHMENTS.CAKE_SERVER.translate(), 8, 6, 0x404040);
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort(generateNBTName("Cake",id), (short)getCakeBuffer());	
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setCakeBuffer(tagCompound.getShort(generateNBTName("Cake",id)));
	}	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAKES.translate(String.valueOf(getCakes()), String.valueOf(MAX_CAKES)) + "\n" + Localization.MODULES.ATTACHMENTS.SLICES.translate(String.valueOf(getSlices()), String.valueOf(SLICES_PER_CAKE)), x, y, rect);
	}
	
	private int getCakes() {
		if (getCakeBuffer() == MAX_TOTAL_SLICES) return MAX_CAKES;
		
		return getCakeBuffer() / SLICES_PER_CAKE;
	}

	private int getSlices() {
		if (getCakeBuffer() == MAX_TOTAL_SLICES) return SLICES_PER_CAKE;
		
		return getCakeBuffer() % SLICES_PER_CAKE;
	}

	private int[] rect = {40, 20, 13, 36};
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/cake.png");
				
		drawImage(gui, rect, 0, inRect(x, y, rect) ? rect[3] : 0);
		int maxHeight = rect[3] - 2;
		int height = (int)((getCakes() / (float)MAX_CAKES) * maxHeight);
		if (height > 0) {
			drawImage(gui, rect[0] + 1, rect[1] + 1 + maxHeight - height, rect[2], maxHeight - height, 7, height);
		}
		height = (int)((getSlices() / (float)SLICES_PER_CAKE) * maxHeight);
		if (height > 0) {
			drawImage(gui, rect[0] + 9, rect[1] + 1 + maxHeight - height, rect[2] + 7, maxHeight - height, 3, height);
		}		
	}
	
	@Override
	public int guiWidth() {
		return 75;
	}
	
	@Override
	public int guiHeight() {
		return 60;
	}
	
	@Override
	public boolean onInteractFirst(EntityPlayer entityplayer) {
		if (getCakeBuffer() > 0) {
			if (!getCart().worldObj.isRemote && entityplayer.canEat(false)) {
				setCakeBuffer(getCakeBuffer() - 1);
				entityplayer.getFoodStats().addStats(2, 0.1F);
			}
		
			return true;
		}else{
			return false;
		}
	}


	public int getRenderSliceCount() {
		int count = getSlices();
		if (count == 0 && getCakes() > 0) {
			count = 6;
		}
		return count;
	}

    @Override
    public boolean haveSupplies() {
        return getCakeBuffer() > 0;
    }
}

