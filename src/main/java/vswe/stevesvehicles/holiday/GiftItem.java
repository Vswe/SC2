package vswe.stevesvehicles.holiday;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.module.data.ModuleData;
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


	@SuppressWarnings("UnusedDeclaration")
    public GiftItem(Block block, int costPerItem, int chanceWeight) {
		this(new ItemStack(block,1), costPerItem, chanceWeight);
	}

	public GiftItem(Item item, int costPerItem, int chanceWeight) {
		this(new ItemStack(item,1), costPerItem, chanceWeight);
	}

    public ItemStack getItem() {
        return item;
    }

    private static class GiftItemModule extends GiftItem {
        private ModuleData module;
        private GiftItemModule(ModuleData module, int costPerItem, int chanceWeight) {
            super((ItemStack)null, costPerItem, chanceWeight);
            this.module = module;
        }

        @Override
        public ItemStack getItem() {
            return module.getItemStack();
        }
    }
	
	public static ArrayList<GiftItem> ChristmasList = new ArrayList<GiftItem>();
	public static ArrayList<GiftItem> EasterList  = new ArrayList<GiftItem>();
	
	public static void init() {
		ChristmasList.add(new GiftItem(new ItemStack(Blocks.dirt, 32), 25, 200000));
		ChristmasList.add(new GiftItem(new ItemStack(Blocks.stone, 16), 50, 100000));
		ChristmasList.add(new GiftItem(new ItemStack(Items.coal, 8), 50, 50000));
		ChristmasList.add(new GiftItem(new ItemStack(Items.redstone, 2), 75, 22000));
		ChristmasList.add(new GiftItem(new ItemStack(Items.iron_ingot, 4), 75, 25000));
		ChristmasList.add(new GiftItem(Items.gold_ingot, 80, 17000));
		ChristmasList.add(new GiftItem(Items.diamond, 250, 5000));
		GiftItem.addModuleGifts(ChristmasList);

		GiftItem.addModuleGifts(EasterList);		
	}
	
	public static void addModuleGifts(ArrayList<GiftItem> gifts) {
		for (ModuleData module : ModuleRegistry.getAllModules()) {
			if (module.getIsValid() && !module.getIsLocked() && module.getHasRecipe()) {
				if (module.getCost() > 0) {		
					GiftItem item = new GiftItemModule(module, module.getCost() * 20, (int)Math.pow(151 - module.getCost(), 2));
					item.fixedSize = true;
					gifts.add(item);
				}
			}
		}	
	}
	
	public static ArrayList<ItemStack> generateItems(Random rand, ArrayList<GiftItem> gifts, int value, int maxTries) {
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
					if (maxSetSize * gift.getItem().stackSize > gift.getItem().getItem().getItemStackLimit(gift.getItem())) {
						maxSetSize = gift.getItem().getItem().getItemStackLimit(gift.getItem()) / gift.getItem().stackSize;
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
						
						ItemStack item = gift.getItem().copy();
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