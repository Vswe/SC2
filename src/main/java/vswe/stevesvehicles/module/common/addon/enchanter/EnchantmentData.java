package vswe.stevesvehicles.module.common.addon.enchanter;

import vswe.stevesvehicles.client.gui.ColorHelper;
import vswe.stevesvehicles.localization.entry.module.LocalizationUtility;

public class EnchantmentData {

	private EnchantmentInfo type;
	private int value;
	public EnchantmentData(EnchantmentInfo type) {
		this.type = type;
		value = 0;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int val) {
		this.value = val;
	}
	
	public EnchantmentInfo getEnchantment() {
		return type;
	}

	public void setEnchantment(EnchantmentInfo info) {
		type = info;
	}	
	
	public void damageEnchant(int dmg) {
		damageEnchantLevel(dmg, getValue(), 1);	
	}
	
	private boolean damageEnchantLevel(int dmg, int value,  int level) {
		if (level > type.getEnchantment().getMaxLevel() || value <= 0) {
			return false;
		}
		
		int levelValue = getEnchantment().getValue(level);
		
		if (!damageEnchantLevel(dmg, value - levelValue, level + 1)) {
			int dmgDealt = dmg * (int)Math.pow(2, level - 1);
			if (dmgDealt > value) {
				dmgDealt = value;
			}
			
			setValue(getValue() - dmgDealt);
		}
		
		return true;
	}

	public int getLevel() {
		int value = getValue();
		for (int i = 0; i < type.getEnchantment().getMaxLevel(); i++) {
			if (value > 0) {
				value -= getEnchantment().getValue(i+1);
			}else{
				return i;
			}		
		}
		return type.getEnchantment().getMaxLevel();
	}

	public String getInfoText() {
		int value = getValue();
		int level;
		int percentage = 0;
		for (level = 1; level <= type.getEnchantment().getMaxLevel(); level++) {
			
			if (value > 0) {
				int levelValue = getEnchantment().getValue(level);
				percentage = (100 * value) / levelValue;
				value -= levelValue;
				if (value < 0) {
					break;
				}
			}	
		}
		
		

		return ColorHelper.YELLOW + getEnchantment().getEnchantment().getTranslatedName(getLevel()) + "\n" + LocalizationUtility.ENCHANTER_LEFT.translate(String.valueOf(percentage));
	}


	
	
}

