package vswe.stevesvehicles.module.common.attachment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotCake;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleCakeServer extends ModuleAttachment implements ISuppliesModule {

	public ModuleCakeServer(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	
	
	private int cooldown = 0;
	private static final int MAX_CAKES = 10;
	private static final int SLICES_PER_CAKE = 6;
	private static final int MAX_TOTAL_SLICES = ((MAX_CAKES + 1) * SLICES_PER_CAKE);
    private static final int REFILL_CHEAT_COOLDOWN = 20;
	
	@Override
	public void update() {
		super.update();
		
		if (!getVehicle().getWorld().isRemote) {
			if (getVehicle().hasCreativeSupplies()) {
				if (cooldown >= REFILL_CHEAT_COOLDOWN) {
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
		return new SlotCake(getVehicle().getVehicleEntity(), slotId, 8 + x * 18, 38 + y * 18);
	}	
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.ATTACHMENTS.CAKE_SERVER.translate(), 8, 6, 0x404040);
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setShort("Cake", (short)getCakeBuffer());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setCakeBuffer(tagCompound.getShort("Cake"));
	}	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAKES.translate(String.valueOf(getCakes()), String.valueOf(MAX_CAKES)) + "\n" + Localization.MODULES.ATTACHMENTS.SLICES.translate(String.valueOf(getSlices()), String.valueOf(SLICES_PER_CAKE)), x, y, RECT);
	}
	
	private int getCakes() {
		if (getCakeBuffer() == MAX_TOTAL_SLICES) return MAX_CAKES;
		
		return getCakeBuffer() / SLICES_PER_CAKE;
	}

	private int getSlices() {
		if (getCakeBuffer() == MAX_TOTAL_SLICES) return SLICES_PER_CAKE;
		
		return getCakeBuffer() % SLICES_PER_CAKE;
	}

	private static final int[] RECT = {40, 20, 13, 36};
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/cake.png");
				
		drawImage(gui, RECT, 0, inRect(x, y, RECT) ? RECT[3] : 0);
		int maxHeight = RECT[3] - 2;
		int height = (int)((getCakes() / (float)MAX_CAKES) * maxHeight);
		if (height > 0) {
			drawImage(gui, RECT[0] + 1, RECT[1] + 1 + maxHeight - height, RECT[2], maxHeight - height, 7, height);
		}
		height = (int)((getSlices() / (float)SLICES_PER_CAKE) * maxHeight);
		if (height > 0) {
			drawImage(gui, RECT[0] + 9, RECT[1] + 1 + maxHeight - height, RECT[2] + 7, maxHeight - height, 3, height);
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
			if (!getVehicle().getWorld().isRemote && entityplayer.canEat(false)) {
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

