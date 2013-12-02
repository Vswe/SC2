package vswe.stevescarts.Modules.Workers.Tools;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ITreeModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.Plants.ModulePlantSize;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFuel;
import vswe.stevescarts.Slots.SlotSapling;
public abstract class ModuleWoodcutter extends ModuleTool implements ISuppliesModule, ITreeModule {
	public ModuleWoodcutter(MinecartModular cart) {
		super(cart);
	}

	//lower numbers are prioritized
	public byte getWorkPriority() {
		return 80;
	}

	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.TOOLS.CUTTER.translate(), 8, 6, 0x404040);
	}

	@Override
	protected int getInventoryWidth() {
		return super.getInventoryWidth() + 3;
	}
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		if (x == 0) {
			return super.getSlot(slotId, x, y);
		}else{
			x--;
			return new SlotSapling(getCart(), this, slotId,8+x*18,28+y*18);
		}
	}	
	
	

	public boolean useDurability() {
		return true;
	}
	
	private ArrayList<ITreeModule> treeModules;
	private ModulePlantSize plantSize;
	@Override
	public void init() {
		super.init();
		treeModules = new ArrayList<ITreeModule>();
		
		for (ModuleBase module : getCart().getModules()) {
			if (module instanceof ITreeModule) {
				treeModules.add((ITreeModule)module);
			}else if(module instanceof ModulePlantSize) {
				plantSize = (ModulePlantSize)module;
			}
		}
		
	}	
	
	
	/*public abstract int getApplePercentageDropChance();
	public abstract int getSaplingPercentageDropChance();
	public abstract int getWoodPercentageDropChance();
	public abstract int getLogPercentageDropChance();
	public abstract int getTwigPercentageDropChance();*/
	
	public abstract int getPercentageDropChance();
	
	public ArrayList<ItemStack> getTierDrop(ArrayList<ItemStack> baseItems) {
		ArrayList<ItemStack> nerfedItems = new ArrayList<ItemStack>();
		
		for (ItemStack item : baseItems) {
			if (item != null) {
				/*if (item.getItem() == Item.appleRed) {
					dropItemByMultiplierChance(nerfedItems, item, getApplePercentageDropChance());
				}else if(item.itemID == Block.sapling.blockID || item.itemID == Block.leaves.blockID) {					
					dropItemByMultiplierChance(nerfedItems, item, getSaplingPercentageDropChance());
				}else if(item.itemID == Block.wood.blockID) {
					int r = getCart().rand.nextInt(100);
					if (r < getWoodPercentageDropChance()) {
						nerfedItems.add(item);
					}else if((enchanter == null || !enchanter.useSilkTouch()) && r < getWoodPercentageDropChance() + getLogPercentageDropChance() + getTwigPercentageDropChance()) {
						nerfedItems.add(ItemCartComponent.getWood(item.getItemDamage(), r < getWoodPercentageDropChance() + getLogPercentageDropChance(), item.stackSize));
					}
				}else{
					nerfedItems.add(item);
				}*/
				dropItemByMultiplierChance(nerfedItems, item, getPercentageDropChance());
			}
		}
		
		
		return nerfedItems;
	}	
	
	private void dropItemByMultiplierChance(ArrayList<ItemStack> items, ItemStack item, int percentage) {
		int drop = 0;
		
		while(percentage > 0) {
			if (getCart().rand.nextInt(100) < percentage) {
				items.add(item.copy());
				drop++;
			}
			percentage -= 100;
		}
	}
	
	
	private boolean isPlanting;
	
	//return true when the work is done, false allow other modules to continue the work
	public boolean work() {
        //get the next block so the cart knows where to mine
        Vec3 next = getNextblock();
        //save the coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

        //loop through the blocks in the "hole" in front of the cart

        int size = getPlantSize();
        
        destroyLeaveBlockOnTrack(x, y, z);
        destroyLeaveBlockOnTrack(x, y + 1, z);
        
        for (int i = -size; i <= size; i++)
        {
        	if (i == 0) {
        		continue;
        	}
        	
        	//plant big trees in the correct order
        	int i2 = i;
        	if (i2 < 0) {
        		i2 = -size - i2 - 1;
        	}
        	
        	
            int plantX = x + (getCart().z() != z ? i2 : 0);
            int plantY = y - 1;
            int plantZ = z + (getCart().x() != x ? i2 : 0);               
            
			if (plant(size, plantX, plantY, plantZ, x, z))
            {
				setCutting(false);
                return true;
            }
                
        }
        
        if (!isPlanting) {
	        for (int i = -1; i <= 1; i++)
	        {
	            for (int j = -1; j <= 1; j++)
	            {
	                int farmX = x + i;
	                int farmY = y - 1;
	                int farmZ = z + j;
	                
	                if (farm(farmX, farmY, farmZ))
	                {
						setCutting(true);
	                    return true;
	                }	                
	            }	            
	        }
        }
        
		isPlanting = false;
		setCutting(false);
		return false;
    }


private boolean plant(int size, int x, int y, int z, int cx, int cz)
    {
	   //backwards compat
	   if (size == 1) {
		   if ((x+z) % 2 == 0) {
			   return false;
		   }
	   }else if ((x == cx && ((x / size) % 2 == 0)) || (z == cz && (z / size) % 2 == 0 )) {
		   return false;
	   }
	   

        int id = getCart().worldObj.getBlockId(x, y, z);
        int idOfBlockAbove = getCart().worldObj.getBlockId(x, y + 1, z);
        Block b = Block.blocksList[idOfBlockAbove];
        
        /*if ((id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.tilledField.blockID) && idOfBlockAbove == 0)
        {
            int hasSapling = -1;


            for (int i = 0; i <	getInventorySize(); i++)
            {
                SlotBase slot = getSlots().get(i);
                if (slot.containsValidItem()) {
                    hasSapling = i;                     
                    break;
                }             
            }

            if (hasSapling != -1)
            {
                if (doPreWork())
                {
					startWorking(25);
					isPlanting = true;
                    return true;
                }
                else
                {
                    stopWorking();
					isPlanting = false;
					
                    getCart().worldObj.setBlock(x, y + 1, z, Block.sapling.blockID, getStack(hasSapling).getItem().getMetadata(getStack(hasSapling).getItemDamage()), 3);

                    getStack(hasSapling).stackSize--;

                    if (getStack(hasSapling).stackSize == 0)
                    {
                        setStack(hasSapling,null);
                    }
                }
            }
        }*/
        
        int saplingSlotId = -1;
        ItemStack sapling = null;

        for (int i = 0; i <	getInventorySize(); i++)
        {
            SlotBase slot = getSlots().get(i);
            if (slot.containsValidItem()) {
            	saplingSlotId = i;  
            	sapling = getStack(i);
                break;
            }             
        }


        if (sapling != null /*&& ((ItemBlock)sapling.getItem()).canPlaceItemBlockOnSide(getCart().worldObj, x, y, z, 1, getFakePlayer(), sapling)*/)
        {
            if (doPreWork())
            {
            	
            	
				if (sapling.getItem().onItemUse(sapling, getFakePlayer(), getCart().worldObj, x, y, z, 1, 0, 0, 0)) {
	                if (sapling.stackSize == 0)
	                {
	                    setStack(saplingSlotId,null);
	                }
					startWorking(25);
					isPlanting = true;
	                return true;	                
				}

            }
            else
            {
                stopWorking();
				isPlanting = false;
				
				
				 
                //getCart().worldObj.setBlock(x, y + 1, z, Block.sapling.blockID, getStack(hasSapling).getItem().getMetadata(getStack(hasSapling).getItemDamage()), 3);

				

            }        	
        }
        

        return false;
    }

   private boolean farm(int x, int y, int z)
    {
	   	if (!isBroken()) {
	        int id = getCart().worldObj.getBlockId(x, y + 1, z);
	        Block b = Block.blocksList[id];
	        int m = getCart().worldObj.getBlockMetadata(x, y + 1, z);
	
	        if (b != null && isWoodHandler(b, x, y + 1, z))
	        {
				ArrayList<BlockCoord> checked = new ArrayList<BlockCoord>();
	
				if (removeAt(x,y+1,z,checked)) {
					return true;
				}else{
					stopWorking();
				}
	        }
	   	}

        return false;
    }



	private boolean removeAt(int i, int j, int k, ArrayList<BlockCoord> checked) {
		BlockCoord here = new BlockCoord(i,j,k);
		checked.add(here);

		int id = getCart().worldObj.getBlockId(i, j, k);
		Block b = Block.blocksList[id];
		int m = getCart().worldObj.getBlockMetadata(i, j, k);

		if (b == null) {
			return false;
		}
		

		if (checked.size() < 125 && here.getHorizontalDistToCartSquared(getCart()) < 175) {
			for (int type = 0; type < 2; type++) {
				boolean hitWood = false;;
				if (isLeavesHandler(b, i, j, k)) {
					type = 1;
				}else if(type == 1) {
					hitWood = true;
				}

				for (int x = -1; x <= 1; x++) {
					for (int y = 1; y >= 0; y--) {
						for (int z = -1; z <= 1; z++) {
							Block currentBlock = Block.blocksList[getCart().worldObj.getBlockId(i+x, j+y, k+z)];
							if (currentBlock != null && (hitWood ? isWoodHandler(currentBlock, i+x, j+y, k+z) : isLeavesHandler(currentBlock, i+x, j+y, k+z))) {
								if (!checked.contains(new BlockCoord(i+x,j+y,k+z))) {
									return removeAt(i+x,j+y,k+z,checked);
								}
							}
						}
					}
				}
			}
		}
		
		
		ArrayList<ItemStack> stuff;
		
		if (shouldSilkTouch(b, i, j, k, m)) {
			stuff = new ArrayList<ItemStack>();
			ItemStack stack = getSilkTouchedItem(b, m);
			if (stack != null) {
				stuff.add(stack);
			}
		}else{
			int fortune = enchanter != null ? enchanter.getFortuneLevel() : 0;
			stuff = Block.blocksList[id].getBlockDropped(getCart().worldObj, i, j, k, m, fortune);

	        int applerand = 200;
	
	        if (fortune > 0) {
	        	applerand -= 10 << fortune;
	
	            if (applerand < 40) {
	            	applerand = 40;
	            }
	        }		
			
			if ((m & 3) == 0 && id == Block.leaves.blockID && getCart().rand.nextInt(applerand) == 0) {
				stuff.add(new ItemStack(Item.appleRed, 1, 0));
			}
		}

		ArrayList<ItemStack> nerfedstuff = getTierDrop(stuff);
		
		
		boolean first = true;
		for (ItemStack iStack : nerfedstuff)
		{
			getCart().addItemToChest(iStack, Slot.class,SlotFuel.class);

			if (iStack.stackSize != 0)
			{
				if (first) {
					return false;
				}

				EntityItem entityitem = new EntityItem(getCart().worldObj, getCart().posX, getCart().posY, getCart().posZ , iStack);
				entityitem.motionX = (float)(i - getCart().x()) / 10;
				entityitem.motionY = 0.15F;
				entityitem.motionZ = (float)(k - getCart().z()) / 10;
				getCart().worldObj.spawnEntityInWorld(entityitem);
			}
			first = false;
		}

		try{
			//worldObj.playAuxSFX(2001, i, j, k, id + m * 256);
		}catch(Exception e) {
		}

		getCart().worldObj.setBlockToAir(i, j, k);

		int basetime;
		
		if (isLeavesHandler(b, i, j, k)) {
			basetime = 2;
			damageTool(1);
		}else{
			basetime = 25;
			damageTool(5);
		}

		
    	int efficiency = enchanter != null ? enchanter.getEfficiencyLevel() : 0;	
		startWorking((int)(basetime / Math.pow(1.3F, efficiency)));
		
		return true;
    }

	@Override
	public void initDw() {
		addDw(0,0);
	}
	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	private void setCutting(boolean val) {
		updateDw(0, (byte)(val  ? 1 : 0));
	}

	protected boolean isCutting() {
		if (isPlaceholder()) {
			return getSimInfo().getIsCutting();
		}else{
			return getDw(0) != 0;
		}
	}

	private float cutterAngle = (float)(Math.PI / 4);
	public float getCutterAngle() {
		return cutterAngle;
	}
    /**
     Called every tick, here the necessary actions should be taken
     **/
    public void update()
    {
        //call the method from the super class, this will do all ordinary things first
        super.update();

		boolean cuttingflag = isCutting();
		if (cuttingflag || cutterAngle != (float)(Math.PI / 4)) {
			boolean flag = false;
			if (!cuttingflag && cutterAngle < (float)(Math.PI / 4)) {
				flag = true;
			}

			cutterAngle = (float)((cutterAngle + 0.9F) % (Math.PI * 2));

			if (!cuttingflag && cutterAngle > (float)(Math.PI / 4) && flag) {
				cutterAngle = (float)(Math.PI / 4);
			}
		}
    }


	
	@Override
	public boolean haveSupplies() {
		for (int i = 0; i < getInventorySize(); i++) {
			if (getSlots().get(i).containsValidItem()) {
				return true;
			}
		}
		return false;
	}	
	
	public boolean isLeavesHandler(Block b, int x, int y, int z) {
		for (ITreeModule module : treeModules) {
			if (module.isLeaves(b, x, y, z)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isWoodHandler(Block b, int x, int y, int z) {
		for (ITreeModule module : treeModules) {
			if (module.isWood(b, x, y, z)) {
				return true;
			}
		}		
		
		return false;
	}	
	
	public boolean isSaplingHandler(ItemStack sapling) {
		for (ITreeModule module : treeModules) {
			if (module.isSapling(sapling)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isLeaves(Block b, int x, int y, int z) {
		return b.blockID == Block.leaves.blockID;
	}
	
	public boolean isWood(Block b, int x, int y, int z) {
		return b.blockID == Block.wood.blockID;
	}
	public boolean isSapling(ItemStack sapling) {
		return sapling != null && sapling.getItem().itemID == Block.sapling.blockID;	
	}
	

	private int getPlantSize() {
		if (plantSize != null) {
			return plantSize.getSize();
		}else{
			return 1;
		}
	}
	
	
	private void destroyLeaveBlockOnTrack(int x, int y, int z) {
        int id = getCart().worldObj.getBlockId(x, y, z);                
        Block b = Block.blocksList[id];
        
        if (b != null && isLeavesHandler(b, x, y, z)) {
        	getCart().worldObj.setBlockToAir(x, y, z);
        }		
	}
}