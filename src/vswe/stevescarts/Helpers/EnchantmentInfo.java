package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import java.util.Map;

import vswe.stevescarts.Helpers.EnchantmentInfo.ENCHANTMENT_TYPE;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EnchantmentInfo {
	
	private Enchantment enchantment;
	private int rank1Value;
	private ENCHANTMENT_TYPE type;
	
	public EnchantmentInfo(Enchantment enchantment, ENCHANTMENT_TYPE type,  int rank1Value) {
		this.enchantment = enchantment;
		this.rank1Value = rank1Value;
		this.type = type;
		
		enchants.add(this);
	}

	
	public Enchantment getEnchantment() {
		return enchantment;
	}
	
	public int getMaxValue() {
		int max = 0;
		
		for (int i = 0; i < getEnchantment().getMaxLevel(); i++) {
			max += getValue(i + 1);
		}
		
		return max;	
	}
	
	public int getValue(int level) {	
		return (int)Math.pow(2, level-1) * rank1Value;		
	}
	
	
	public static ArrayList<EnchantmentInfo> enchants = new ArrayList<EnchantmentInfo>();
	
	public static EnchantmentInfo fortune = new EnchantmentInfo(Enchantment.fortune, ENCHANTMENT_TYPE.TOOL, 50000);
	public static EnchantmentInfo efficiency = new EnchantmentInfo(Enchantment.efficiency, ENCHANTMENT_TYPE.TOOL, 50000);
	public static EnchantmentInfo unbreaking = new EnchantmentInfo(Enchantment.unbreaking, ENCHANTMENT_TYPE.TOOL, 64000);
	
	
	public static EnchantmentInfo power = new EnchantmentInfo(Enchantment.power, ENCHANTMENT_TYPE.SHOOTER, 750);
	public static EnchantmentInfo punch = new EnchantmentInfo(Enchantment.punch, ENCHANTMENT_TYPE.SHOOTER, 1000);
	public static EnchantmentInfo flame = new EnchantmentInfo(Enchantment.flame, ENCHANTMENT_TYPE.SHOOTER, 1000);
	public static EnchantmentInfo infinity = new EnchantmentInfo(Enchantment.infinity, ENCHANTMENT_TYPE.SHOOTER, 500);

	public static boolean isItemValid(ArrayList<ENCHANTMENT_TYPE> enabledTypes, ItemStack itemstack) {
	
		
		if (itemstack != null && itemstack.getItem() == Item.enchantedBook) {

			for (EnchantmentInfo info : enchants) {
				
				boolean isValid = false;
				for (ENCHANTMENT_TYPE type : enabledTypes) {
					if (info.type == type) {
						isValid = true;
					}
				}
				if (isValid) {
					int level = getEnchantmentLevel(info.getEnchantment().effectId, itemstack);
					if (level > 0)  {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static EnchantmentData addBook(ArrayList<ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, ItemStack itemstack) {
		if (itemstack != null && itemstack.getItem() == Item.enchantedBook) {
			if (data == null) {
				for (EnchantmentInfo info : enchants) {
					data = addEnchantment(enabledTypes, data, itemstack, info);
				}
			}else{
				addEnchantment(enabledTypes, data, itemstack, data.getEnchantment());
			}	
		}
		
		return data;
	}
	
	
	private static EnchantmentData addEnchantment(ArrayList<ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, ItemStack itemstack, EnchantmentInfo info) {
		boolean isValid = false;
		for (ENCHANTMENT_TYPE type : enabledTypes) {
			if (info.type == type) {
				isValid = true;
			}
		}
		
		if (isValid) {	
			int level = getEnchantmentLevel(info.getEnchantment().effectId, itemstack);
			if (level > 0) {
				if (data == null) {
					data = new EnchantmentData(info);
				}
				
				int newValue = data.getEnchantment().getValue(level) + data.getValue();
				if (newValue <= data.getEnchantment().getMaxValue()) {
					data.setValue(newValue);
					itemstack.stackSize--;
				}
			}	
		}
		
		return data;
	}
	
    private static int getEnchantmentLevel(int par0, ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return 0;
        }
        else
        {
            NBTTagList nbttaglist =  Item.enchantedBook.func_92110_g(par1ItemStack);
           
            if (nbttaglist == null)
            {
                return 0;
            }
            else
            {
                for (int j = 0; j < nbttaglist.tagCount(); ++j)
                {
                    short short1 = ((NBTTagCompound)nbttaglist.tagAt(j)).getShort("id");
                    short short2 = ((NBTTagCompound)nbttaglist.tagAt(j)).getShort("lvl");

                    if (short1 == par0)
                    {
                        return short2;
                    }
                }

                return 0;
            }
        }
    }


	public static EnchantmentData createDataFromEffectId(EnchantmentData data, short id) {
		for (EnchantmentInfo info : enchants) {
			if (info.getEnchantment().effectId == id) {
				if (data == null) {
					data = new EnchantmentData(info);
				}else{
					data.setEnchantment(info);
				}
				break;
			}
		}
		
		return data;
	}
	
	
	public static enum ENCHANTMENT_TYPE {
		TOOL,
		SHOOTER
	}


	public ENCHANTMENT_TYPE getType() {
		return type;
	}
	
}
