package vswe.stevescarts.Modules.Storages.Chests;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelGiftStorage;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Helpers.GiftItem;
public class ModuleGiftStorage extends ModuleChest {
	public ModuleGiftStorage(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getInventoryWidth()
	{
		return 9;
	}
	@Override
	protected int getInventoryHeight() {
		return 4;
	}

	@Override
	public byte getExtraData() {
		return (byte)0; //empty, sorry :)
	}
	
	@Override
	public boolean hasExtraData() {
		return true;
	}
	
	@Override
	public void setExtraData(byte b) {
		if (b == 0) {
			return; //empty, sorry :)
		}
		
		ArrayList<ItemStack> items = GiftItem.generateItems(getCart().rand, GiftItem.ChristmasList, 50 + getCart().rand.nextInt(700), 1 + getCart().rand.nextInt(5));
		for (int i = 0; i < items.size(); i++) {
			setStack(i, items.get(i));
		}
	}

	

}