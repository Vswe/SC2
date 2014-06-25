package vswe.stevescarts.old.Modules.Realtimers;
import java.util.ArrayList;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Modules.ModuleBase;
import vswe.stevescarts.old.Slots.SlotBase;
import vswe.stevescarts.old.Slots.SlotFirework;

public class ModuleFirework extends ModuleBase {
	public ModuleFirework(EntityModularCart cart) {
		super(cart);
	}

	private int fireCooldown;
	
	@Override
	public void update() {
		if (fireCooldown > 0) {
			fireCooldown--;
		}
	}
	
	
	@Override
	public void activatedByRail(int x, int y, int z, boolean active) {
		if (active && fireCooldown == 0 && getCart().hasFuel()) {
			fire();
			fireCooldown = 20;
		}
	}	
	
	
	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotFirework(getCart(),slotId,8+x*18,16+y*18);
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}

	@Override
	public int guiWidth() {
		return 15 + getInventoryWidth() * 18;
	}

	@Override
	public int guiHeight() {
		return 20 + getInventoryHeight() * 18 ;
	}

	@Override
	protected int getInventoryWidth()
	{
		return 8;
	}
	@Override
	protected int getInventoryHeight() {
		return 3;
	}	
	
	public void fire() {
		if (getCart().worldObj.isRemote) {
			return;
		}
	
		ItemStack firework = getFirework();
		if (firework != null) {
			launchFirework(firework);
		}
	}
	
	private ItemStack getFirework() {
		boolean hasGunpowder = false;
		boolean hasPaper = false;	
	
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			
			if (item != null) {
			
				if (item.getItem() == Items.fireworks) {
					ItemStack firework = item.copy();
					firework.stackSize = 1;
                    removeItemStack(item, 1, i);
					
					return firework;
				}else if(item.getItem() == Items.paper) {
					hasPaper = true;
				}else if(item.getItem() == Items.gunpowder) {
					hasGunpowder = true;
				}
			}			
		}
		

		
		if (hasPaper && hasGunpowder) {

		
			ItemStack firework = new ItemStack(Items.fireworks);
			
			int maxGunpowder = getCart().rand.nextInt(3) + 1;
			int countGunpowder = 0;
			boolean removedPaper = false;
			for (int i = 0; i < getInventorySize(); i++) {
				ItemStack item = getStack(i);
				
				if (item != null) {
					if(item.getItem() == Items.paper && !removedPaper) {
                        removeItemStack(item, 1, i);
						removedPaper = true;
					}else if(item.getItem() == Items.gunpowder && countGunpowder < maxGunpowder) {
						while (item.stackSize > 0 && countGunpowder < maxGunpowder) {
							countGunpowder++;
                            removeItemStack(item, 1, i);
						}
					}
				}
			}
			
			int chargeCount = 1;
			
			while (chargeCount < 7 && getCart().rand.nextInt(3 + chargeCount / 3) == 0) {
				chargeCount++;
			}
			
			NBTTagCompound itemstackNBT = new NBTTagCompound();
			NBTTagCompound fireworksNBT = new NBTTagCompound();
			NBTTagList explosionsNBT = new NBTTagList();
			
			for (int i = 0; i < chargeCount; i++) {
				ItemStack charge = getCharge();
				if (charge == null) {
					break;
				}else if (charge.hasTagCompound() && charge.getTagCompound().hasKey("Explosion")) {					
					explosionsNBT.appendTag(charge.getTagCompound().getCompoundTag("Explosion"));					
				}				
			}
			
			fireworksNBT.setTag("Explosions", explosionsNBT);
			fireworksNBT.setByte("Flight", (byte)countGunpowder);
			itemstackNBT.setTag("Fireworks", fireworksNBT);
			firework.setTagCompound(itemstackNBT);			
			
			return firework;
		}


		return null;
	}
	
	private ItemStack getCharge() {
	
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			
			if (item != null) {
			
				if (item.getItem() == Items.firework_charge) {
					ItemStack charge = item.copy();
					charge.stackSize = 1;
                    removeItemStack(item, 1, i);
					
					return charge;
				}
			}			
		}	
	
		ItemStack charge = new ItemStack(Items.firework_charge);
		NBTTagCompound itemNBT = new NBTTagCompound();
		NBTTagCompound explosionNBT = new NBTTagCompound();
		byte type = 0;

		

		boolean removedGunpowder = false;
		boolean canHasTrail = getCart().rand.nextInt(16) == 0;
		boolean canHasFlicker = getCart().rand.nextInt(8) == 0;
		boolean canHasModifier = getCart().rand.nextInt(4) == 0;
		byte modifierType = (byte)(getCart().rand.nextInt(4) + 1);
		boolean removedModifier = false;
		boolean removedDiamond = false;
		boolean removedGlow = false;
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			
			if (item != null) {
				if(item.getItem() == Items.gunpowder && !removedGunpowder) {
                    removeItemStack(item, 1, i);
					removedGunpowder = true;
				}else if(item.getItem() == Items.glowstone_dust && canHasFlicker && !removedGlow) {
                    removeItemStack(item, 1, i);
					removedGlow = true;
					explosionNBT.setBoolean("Flicker", true);
				}else if(item.getItem() == Items.diamond && canHasTrail && !removedDiamond) {
                    removeItemStack(item, 1, i);
					removedDiamond = true;
					explosionNBT.setBoolean("Trail", true);
				}else if(canHasModifier && !removedModifier && (
					(item.getItem() == Items.fire_charge && modifierType == 1) ||
					(item.getItem() == Items.gold_nugget && modifierType == 2) ||
					(item.getItem() == Items.skull && modifierType == 3) ||
					(item.getItem() == Items.feather && modifierType == 4)
					)
				
				
				) {
                    removeItemStack(item, 1, i);
					removedModifier = true;		
					type = modifierType;
				}
			}
		}


		
	
	

		int[] colors = generateColors(type != 0 ? 7 : 8);
		if (colors == null) {
			return null;
		}
		explosionNBT.setIntArray("Colors", colors);	
		if (getCart().rand.nextInt(4) == 0) {
			int[] fade = generateColors(8);
			if (fade != null) {
				explosionNBT.setIntArray("FadeColors", fade);
			}
		}
		explosionNBT.setByte("Type", type);
		itemNBT.setTag("Explosion", explosionNBT);
		charge.setTagCompound(itemNBT);	
		

		return charge;
	}
	
	
	private int[] generateColors(int maxColorCount) {
		int[] maxColors = new int[16];
		int[] currentColors = new int[16];
		
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			
			if (item != null) {		
				if(item.getItem() == Items.dye) {
					maxColors[item.getItemDamage()] += item.stackSize;	
				}
			}
		}
		
		int colorCount = getCart().rand.nextInt(2) + 1;
		while (colorCount <= maxColorCount - 2 && getCart().rand.nextInt(2) == 0) {
			colorCount+=2;
		}	

		ArrayList<Integer> colorPointers = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++) {
			if (maxColors[i] > 0) {
				colorPointers.add(i);
			}
		}
		
		if (colorPointers.size() == 0) {
			return null;
		}
		
		ArrayList<Integer> usedColors = new ArrayList<Integer>();
		while (colorCount > 0 && colorPointers.size() > 0) {
			int pointerId = getCart().rand.nextInt(colorPointers.size());
			int colorId = colorPointers.get(pointerId);
			currentColors[colorId]++;
			if(--maxColors[colorId] <= 0) {
				colorPointers.remove(pointerId);
			}
			usedColors.add(colorId);
			colorCount--;
		}
		
		int[] colors = new int[usedColors.size()];

		for (int i = 0; i < colors.length; ++i)
		{
			colors[i] = ItemDye.field_150922_c[usedColors.get(i)];
		}

		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			
			if (item != null) {
				if(item.getItem() == Items.dye) {
					if (currentColors[item.getItemDamage()] > 0) {
						int count = Math.min(currentColors[item.getItemDamage()], item.stackSize);
						currentColors[item.getItemDamage()] -= count;
					}
				}
			}
		}			
		
		return colors;
	}
	

	private void removeItemStack(ItemStack item, int count,  int id) {
        if (!getCart().hasCreativeSupplies()) {
            item.stackSize -= count;
            if (item.stackSize <= 0) {
                setStack(id, null);
            }
        }
    }
	
	private void launchFirework(ItemStack firework) {
		EntityFireworkRocket rocket = new EntityFireworkRocket(getCart().worldObj, getCart().posX, getCart().posY + 1, getCart().posZ, firework);
		getCart().worldObj.spawnEntityInWorld(rocket);	
	}
	
	

	
}