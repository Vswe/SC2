package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.EnchantmentInfo.ENCHANTMENT_TYPE;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleEnchants;
import vswe.stevescarts.Modules.Workers.ModuleWorker;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotRepair;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModuleTool extends ModuleWorker {
	public ModuleTool(MinecartModular cart) {
		super(cart);
		
		currentDurability = getMaxDurability();
	}
	
	
	
	private int currentDurability; 
	private int remainingRepairUnits;
	private int maximumRepairUnits = 1;

	
	public abstract int getMaxDurability();
	public abstract String getRepairItemName();	
	public abstract int getRepairItemUnits(ItemStack item);
	public abstract int getRepairSpeed();
	public abstract boolean useDurability();

	
	protected ModuleEnchants enchanter;
	@Override
	public void init() {
		super.init();
		for (ModuleBase module : getCart().getModules()) {	
			if (module instanceof ModuleEnchants) {
				enchanter = (ModuleEnchants)module;
				enchanter.addType(ENCHANTMENT_TYPE.TOOL);
				break;
			}			
		}		
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/tool.png");
		
		drawBox(gui, 0, 0, 1F);

		drawBox(gui, 0, 8, useDurability() ? (float)currentDurability / getMaxDurability() : 1F);
		drawBox(gui, 0, 16, (float)remainingRepairUnits / maximumRepairUnits);
		
		if (inRect(x, y, durabilityRect)) {
			drawBox(gui, 0, 24, 1F);
		}		
	}
	
	private int[] durabilityRect = new int[] {10, 15, 52, 8};
	
	private void drawBox(GuiMinecart gui, int u, int v, float mult) {
		int w = (int)(durabilityRect[2] * mult);
		if (w > 0) {
			drawImage(gui, durabilityRect[0], durabilityRect[1], u, v, w, durabilityRect[3]);
		}
	}
	
	public boolean isValidRepairMaterial(ItemStack item) {
		return getRepairItemUnits(item) > 0;
	}	
	
	@Override
	public boolean hasGui() {
		return true;
	}
	
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotRepair(this, getCart(),slotId, 76, 8);
	}
	
	
	@Override
	protected int getInventoryWidth() {
		return 1;
	}
	
	@Override
	public int guiWidth() {
		return 100;
	}	
	
	@Override
	public int guiHeight() {
		return 50;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		String str;
		
		if (useDurability()) {
			str = Localization.MODULES.TOOLS.DURABILITY.translate() + ": " + currentDurability + "/" + getMaxDurability();
			if (isBroken()) {
				str += " [" + Localization.MODULES.TOOLS.BROKEN.translate() + "]";
			}else{
				str += " [" + (100 * currentDurability) / getMaxDurability() + "%]";
			}
			
			str += "\n";
			
			if (isRepairing()) {
				if (isActuallyRepairing()) {
					str += " [" + getRepairPercentage() + "%]";
				}else{
					str += Localization.MODULES.TOOLS.DECENT.translate();
				}
			}else{
				str += Localization.MODULES.TOOLS.INSTRUCTION.translate(getRepairItemName());
			}
		}else{
			str = Localization.MODULES.TOOLS.UNBREAKABLE.translate();
			if (isRepairing() && !isActuallyRepairing()) {
				str += " " + Localization.MODULES.TOOLS.UNBREAKABLE_REPAIR.translate();
			}
		}
		
		drawStringOnMouseOver(gui, str, x, y, durabilityRect);
	}
	
	
	@Override
	public void update() {
		super.update();
		
		
		if (!getCart().worldObj.isRemote && useDurability()) {
			if (isActuallyRepairing()) {
				int dif = 1;
				
				remainingRepairUnits -= dif;
				currentDurability += dif * getRepairSpeed();
				if (currentDurability > getMaxDurability()) {
					currentDurability = getMaxDurability();
				}
			}
			
			//don't use an else instead of this, the value might have changed so both the condition below and above might be true even though they are each other's opposite
			if (!isActuallyRepairing()) {
				int units = getRepairItemUnits(getStack(0));
				if (units > 0 && units <= getMaxDurability() - currentDurability) {
					
					maximumRepairUnits = remainingRepairUnits = units / getRepairSpeed();
					
					getStack(0).stackSize--;
					if (getStack(0).stackSize <= 0) {
						setStack(0, null);
					}
				}
			}
		}
		
	}
	
	@Override
	public boolean stopEngines() {
		return isRepairing();
	}
	
	public boolean isRepairing() {
		return getStack(0) != null || isActuallyRepairing();
	}
	
	public boolean isActuallyRepairing() {
		return remainingRepairUnits > 0;
	}
	
	public boolean isBroken() {
		return currentDurability == 0 && useDurability();
	}
	
	public void damageTool(int val) {
		int unbreaking = enchanter != null ? enchanter.getUnbreakingLevel() : 0;
		
		if (getCart().rand.nextInt(100) < 100/(unbreaking + 1)) {		
			currentDurability -= val;
			if (currentDurability < 0) {
				currentDurability = 0;
			}
		}
		
		if (enchanter != null) {
			enchanter.damageEnchant(ENCHANTMENT_TYPE.TOOL,val);
		}
	}
		
	@Override
	public int numberOfGuiData() {
		return 4;
	}	
	
	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)(currentDurability & 65535));
		updateGuiData(info, 1, (short)((currentDurability >> 16) & 65535));
		updateGuiData(info, 2, (short)(remainingRepairUnits));
		updateGuiData(info, 3, (short)(maximumRepairUnits));
	}
	
	
	
	@Override
	public void receiveGuiData(int id, short data) {
		int dataint = data;
		if (dataint < 0) {
			dataint += 65536;
		}
		
		if (id == 0) {
			currentDurability = ((currentDurability & -65536) | dataint);
		}else if (id == 1) {
			currentDurability = ((currentDurability & 65535) | (dataint << 16));
		}else if(id == 2) {
			remainingRepairUnits = data;
		}else if(id == 3) {
			maximumRepairUnits = data;
		}
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setInteger(generateNBTName("Durability",id), currentDurability);
		tagCompound.setShort(generateNBTName("Repair",id), (short)remainingRepairUnits);
		tagCompound.setShort(generateNBTName("MaxRepair",id), (short)maximumRepairUnits);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		currentDurability = (tagCompound.getInteger(generateNBTName("Durability",id)));
		remainingRepairUnits = (tagCompound.getShort(generateNBTName("Repair",id)));
		maximumRepairUnits = (tagCompound.getShort(generateNBTName("MaxRepair",id)));
	}		
	
	@Override
	public boolean hasExtraData() {
		return true;
	}
	
	@Override
	public byte getExtraData() {
		return (byte)((100 * currentDurability) / getMaxDurability());
	}
	
	@Override
	public void setExtraData(byte b) {
		currentDurability = (b * getMaxDurability()) / 100;
	}	
	
	
	public boolean shouldSilkTouch(Block b, int x, int y, int z, int m) {
	    boolean doSilkTouch = false;
	    
	    //try-catch here just because I need to give it a null player, and other mods might assume that they actually get a player, I don't know.
	    try {
	    	if (enchanter != null && enchanter.useSilkTouch() && b.canSilkHarvest(getCart().worldObj, null, x, y, z, m)) {
	    		return true;
	    	}
	    }catch (Exception e) {
	
		}	
	    return false;
	}
	
	public ItemStack getSilkTouchedItem(Block b, int m) {		
	    int droppedMeta = 0;

        ItemStack stack = new ItemStack(b, 1, droppedMeta);
	    if (stack.getItem() != null && stack.getItem().getHasSubtypes()){
            return new ItemStack(b, 1, m);
	    }else{
            return stack;
        }
	}

    public int getCurrentDurability() {
        return currentDurability;
    }

    public int getRepairPercentage() {
        return (100 - (100 * remainingRepairUnits) / maximumRepairUnits);
    }
}
