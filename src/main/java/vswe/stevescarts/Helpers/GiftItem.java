package vswe.stevescarts.Helpers;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.ModuleData.ModuleData;
public class GiftItem {
	private int chanceWeight;
	private int costPerItem;
	private ItemStack item;
	private boolean fixedSize;
	
	public GiftItem(ItemStack item, int costPerItem, int chanceWeight) {
		this.item = item;
		this.chanceWeight = chanceWeight;
		this.costPerItem = costPerItem;
	}
	
	public GiftItem(Block block, int costPerItem, int chanceWeight) {
		this(new ItemStack(block,1), costPerItem, chanceWeight);
	}

	public GiftItem(Item item, int costPerItem, int chanceWeight) {
		this(new ItemStack(item,1), costPerItem, chanceWeight);
	}

	
	public static ArrayList<GiftItem> ChristmasList;
	public static ArrayList<GiftItem> EasterList;
	
	public static void init() {
		ChristmasList = new ArrayList<GiftItem>();
		ChristmasList.add(new GiftItem(new ItemStack(Block.dirt, 32), 25, 200000));
		ChristmasList.add(new GiftItem(new ItemStack(Block.stone, 16), 50, 100000));
		ChristmasList.add(new GiftItem(new ItemStack(Item.coal, 8), 50, 50000));
		ChristmasList.add(new GiftItem(new ItemStack(Item.redstone, 2), 75, 22000));
		ChristmasList.add(new GiftItem(new ItemStack(Item.ingotIron, 4), 75, 25000));
		ChristmasList.add(new GiftItem(Item.ingotGold, 80, 17000));
		ChristmasList.add(new GiftItem(Item.diamond, 250, 5000));
		GiftItem.addModuleGifts(ChristmasList);
		
		EasterList = new ArrayList<GiftItem>();
		GiftItem.addModuleGifts(EasterList);		
	}
	
	public static void addModuleGifts(ArrayList<GiftItem> gifts) {
		for (ModuleData module : ModuleData.getList().values()) {
			if (module.getIsValid() && !module.getIsLocked() && module.getHasRecipe()) {
				if (module.getCost() > 0) {		
					GiftItem item = new GiftItem(new ItemStack(Items.modules, 1, module.getID()), module.getCost() * 20, (int)Math.pow(151 - module.getCost(), 2));
					item.fixedSize = true;
					gifts.add(item);
				}
			}
		}	
	}
	
	public static ArrayList<ItemStack> generateItems(java.util.Random rand, ArrayList<GiftItem> gifts, int value, int maxTries) {
		int totalChanceWeight = 0;
		for (GiftItem gift : gifts) {
			totalChanceWeight += gift.chanceWeight;
		}
				
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		if (totalChanceWeight == 0) {
			return items;
		}
		
		int tries = 0;
		while (value > 0 && tries < maxTries) {
			int chance = rand.nextInt(totalChanceWeight);
			for (GiftItem gift : gifts) {
				if (chance < gift.chanceWeight) {
					int maxSetSize = (value / gift.costPerItem);
					if (maxSetSize * gift.item.stackSize > gift.item.getItem().getItemStackLimit()) {
						maxSetSize = gift.item.getItem().getItemStackLimit() / gift.item.stackSize;
					}
					if (maxSetSize > 0) {
						int setSize = 1;
						if (!gift.fixedSize) {
							for (int i = 1; i < maxSetSize; i++) {
								if (rand.nextBoolean()) {
									setSize++;
								}
							}
						}
						
						ItemStack item = gift.item.copy();
						item.stackSize *= setSize;
						items.add(item);
						
						value -= setSize * gift.costPerItem;
					}
					
					break;
				}else{
					chance -= gift.chanceWeight;
				}
			}
			tries++;
		}
		return items;
	}
}