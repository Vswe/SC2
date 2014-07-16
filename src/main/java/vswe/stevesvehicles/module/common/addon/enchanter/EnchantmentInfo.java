package vswe.stevesvehicles.module.common.addon.enchanter;

import java.util.ArrayList;

import net.minecraft.init.Items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class EnchantmentInfo {
	
	private Enchantment enchantment;
	private int rank1Value;
	private Enchantment_Type type;
	
	public EnchantmentInfo(Enchantment enchantment, Enchantment_Type type,  int rank1Value) {
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
	
	public static EnchantmentInfo fortune = new EnchantmentInfo(Enchantment.fortune, Enchantment_Type.TOOL, 500000);
	public static EnchantmentInfo efficiency = new EnchantmentInfo(Enchantment.efficiency, Enchantment_Type.TOOL, 500000);
	//public static EnchantmentInfo unbreaking = new EnchantmentInfo(Enchantment.unbreaking, Enchantment_Type.TOOL, 64000);
	
	
	public static EnchantmentInfo power = new EnchantmentInfo(Enchantment.power, Enchantment_Type.SHOOTER, 3750);
	public static EnchantmentInfo punch = new EnchantmentInfo(Enchantment.punch, Enchantment_Type.SHOOTER, 5000);
	public static EnchantmentInfo flame = new EnchantmentInfo(Enchantment.flame, Enchantment_Type.SHOOTER, 5000);
	public static EnchantmentInfo infinity = new EnchantmentInfo(Enchantment.infinity, Enchantment_Type.SHOOTER, 2500);

	public static boolean isItemValid(ArrayList<Enchantment_Type> enabledTypes, ItemStack itemstack) {
	
		
		if (itemstack != null && itemstack.getItem() == Items.enchanted_book) {

			for (EnchantmentInfo info : enchants) {
				
				boolean isValid = false;
				for (Enchantment_Type type : enabledTypes) {
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
	
	public static EnchantmentData addBook(ArrayList<Enchantment_Type> enabledTypes, EnchantmentData data, ItemStack itemstack) {
		if (itemstack != null && itemstack.getItem() == Items.enchanted_book) {
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
	
	
	private static EnchantmentData addEnchantment(ArrayList<Enchantment_Type> enabledTypes, EnchantmentData data, ItemStack itemstack, EnchantmentInfo info) {
		boolean isValid = false;
		for (Enchantment_Type type : enabledTypes) {
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
	
    private static int getEnchantmentLevel(int id, ItemStack item) {
        if (item == null) {
            return 0;
        }else {
            NBTTagList nbttaglist =  Items.enchanted_book.func_92110_g(item);
           
            if (nbttaglist == null) {
                return 0;
            }else{
                for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                    short enchantmentId = nbttaglist.getCompoundTagAt(j).getShort("id");
                    short enchantmentLevel = nbttaglist.getCompoundTagAt(j).getShort("lvl");

                    if (enchantmentId == id){
                        return enchantmentLevel;
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
	
	
	public static enum Enchantment_Type {
		TOOL,
		SHOOTER
	}


	public Enchantment_Type getType() {
		return type;
	}
	
}
