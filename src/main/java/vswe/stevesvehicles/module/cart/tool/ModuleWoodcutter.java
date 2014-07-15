package vswe.stevesvehicles.module.cart.tool;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfo;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfoBoolean;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.localization.entry.module.cart.LocalizationCartTool;
import vswe.stevesvehicles.block.BlockCoordinate;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.module.cart.ITreeModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.addon.cultivation.ModulePlantSize;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotFuel;
import vswe.stevesvehicles.container.slots.SlotSapling;
public abstract class ModuleWoodcutter extends ModuleTool implements ISuppliesModule, ITreeModule {
	public ModuleWoodcutter(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    @Override
    public void loadSimulationInfo(List<SimulationInfo> simulationInfo) {
        simulationInfo.add(new SimulationInfoBoolean(LocalizationAssembler.INFO_CUTTING, "wood"));
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
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationCartTool.CUTTER.translate(), 8, 6, 0x404040);
	}


	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
        return new SlotSapling(getVehicle().getVehicleEntity(), this, slotId, 8 + x * 18, 28 + y * 18);
	}

    @Override
    protected int getInventoryWidth() {
        return 3;
    }
	
	private ArrayList<ITreeModule> treeModules;
	private ModulePlantSize plantSize;
	@Override
	public void init() {
		super.init();
		treeModules = new ArrayList<ITreeModule>();
		
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ITreeModule) {
				treeModules.add((ITreeModule)module);
			}else if(module instanceof ModulePlantSize) {
				plantSize = (ModulePlantSize)module;
			}
		}
		
	}	
	

	public abstract int getPercentageDropChance();
	
	public ArrayList<ItemStack> getTierDrop(ArrayList<ItemStack> baseItems) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		for (ItemStack item : baseItems) {
			if (item != null) {
				dropItemByMultiplierChance(items, item, getPercentageDropChance());
			}
		}
		
		
		return items;
	}	
	
	private void dropItemByMultiplierChance(ArrayList<ItemStack> items, ItemStack item, int percentage) {
		while(percentage > 0) {
			if (getVehicle().getRandom().nextInt(100) < percentage) {
				items.add(item.copy());
			}
			percentage -= 100;
		}
	}
	
	
	private boolean isPlanting;
	
	//return true when the work is done, false allow other modules to continue the work
    @Override
	public boolean work() {
        //get the next block so the cart knows where to mine
        Vec3 next = getNextBlock();
        //save the coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

        //loop through the blocks in the "hole" in front of the cart

        int size = getPlantSize();
        
        destroyLeaveBlockOnTrack(x, y, z);
        destroyLeaveBlockOnTrack(x, y + 1, z);
        
        for (int i = -size; i <= size; i++) {
        	if (i == 0) {
        		continue;
        	}
        	
        	//plant big trees in the correct order
        	int j = i;
        	if (j < 0) {
        		j = -size - j - 1;
        	}
        	
        	
            int plantX = x + (getVehicle().z() != z ? j : 0);
            int plantY = y - 1;
            int plantZ = z + (getVehicle().x() != x ? j : 0);
            
			if (plant(size, plantX, plantY, plantZ, x, z)) {
				setCutting(false);
                return true;
            }
                
        }
        
        if (!isPlanting) {
	        for (int i = -1; i <= 1; i++) {
	            for (int j = -1; j <= 1; j++) {
	                int farmX = x + i;
	                int farmY = y - 1;
	                int farmZ = z + j;
	                
	                if (farm(farmX, farmY, farmZ)) {
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


    private boolean plant(int size, int x, int y, int z, int cx, int cz) {
        if ((x == cx && ((x / size) % 2 == 0)) || (z == cz && (z / size) % 2 == 0 )) {
		   return false;
	    }

        int saplingSlotId = -1;
        ItemStack sapling = null;

        for (int i = 0; i <	getInventorySize(); i++) {
            SlotBase slot = getSlots().get(i);
            if (slot.containsValidItem()) {
            	saplingSlotId = i;  
            	sapling = getStack(i);
                break;
            }             
        }


        if (sapling != null) {
            if (doPreWork()) {

				if (sapling.getItem().onItemUse(sapling, getFakePlayer(), getVehicle().getWorld(), x, y, z, 1, 0, 0, 0)) {
	                if (sapling.stackSize == 0) {
	                    setStack(saplingSlotId, null);
	                }
					startWorking(25);
					isPlanting = true;
	                return true;	                
				}

            }else{
                stopWorking();
				isPlanting = false;
            }        	
        }
        

        return false;
    }

   private boolean farm(int x, int y, int z) {
        Block b = getVehicle().getWorld().getBlock(x, y + 1, z);

        if (b != null && isWoodHandler(b, x, y + 1, z)) {
            ArrayList<BlockCoordinate> checked = new ArrayList<BlockCoordinate>();

            if (removeAt(x,y+1,z,checked)) {
                return true;
            }else{
                stopWorking();
            }
        }

        return false;
    }



	private boolean removeAt(int x, int y, int z, ArrayList<BlockCoordinate> checked) {
		BlockCoordinate here = new BlockCoordinate(x,y,z);
		checked.add(here);

		Block b = getVehicle().getWorld().getBlock(x, y, z);
		int m = getVehicle().getWorld().getBlockMetadata(x, y, z);

		if (b == null) {
			return false;
		}
		

		if (checked.size() < 125 && here.getHorizontalDistToVehicleSquared(getVehicle()) < 175) {
			for (int type = 0; type < 2; type++) {
				boolean hitWood = false;
				if (isLeavesHandler(b, x, y, z)) {
					type = 1;
				}else if(type == 1) {
					hitWood = true;
				}

				for (int offsetX = -1; offsetX <= 1; offsetX++) {
					for (int offsetY = 1; offsetY >= 0; offsetY--) {
						for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                            int targetX = x + offsetX;
                            int targetY = y + offsetY;
                            int targetZ = z + offsetZ;
							Block currentBlock = getVehicle().getWorld().getBlock(targetX, targetY, targetZ);
							if (currentBlock != null && (hitWood ? isWoodHandler(currentBlock, targetX, targetY, targetZ) : isLeavesHandler(currentBlock, targetX, targetY, targetZ))) {
								if (!checked.contains(new BlockCoordinate(targetX, targetY, targetZ))) {
									return removeAt(targetX, targetY, targetZ, checked);
								}
							}
						}
					}
				}
			}
		}
		
		
		ArrayList<ItemStack> stuff;
		
		if (shouldSilkTouch(b, x, y, z, m)) {
			stuff = new ArrayList<ItemStack>();
			ItemStack stack = getSilkTouchedItem(b, m);
			if (stack != null) {
				stuff.add(stack);
			}
		}else{
			int fortune = enchanter != null ? enchanter.getFortuneLevel() : 0;
			stuff = b.getDrops(getVehicle().getWorld(), x, y, z, m, fortune);
		}

		stuff = getTierDrop(stuff);
		
		
		boolean first = true;
		for (ItemStack item : stuff) {
			getVehicle().addItemToChest(item, Slot.class,SlotFuel.class);

			if (item.stackSize != 0) {
				if (first) {
					return false;
				}

				EntityItem entityitem = new EntityItem(getVehicle().getWorld(), getVehicle().getEntity().posX, getVehicle().getEntity().posY, getVehicle().getEntity().posZ , item);
				entityitem.motionX = (float)(x - getVehicle().x()) / 10;
				entityitem.motionY = 0.15F;
				entityitem.motionZ = (float)(z - getVehicle().z()) / 10;
				getVehicle().getWorld().spawnEntityInWorld(entityitem);
			}
			first = false;
		}


		getVehicle().getWorld().setBlockToAir(x, y, z);

		int baseTime;
		
		if (isLeavesHandler(b, x, y, z)) {
			baseTime = 2;
		}else{
			baseTime = 25;
		}

		
    	int efficiency = enchanter != null ? enchanter.getEfficiencyLevel() : 0;	
		startWorking((int)(baseTime / Math.pow(1.3F, efficiency)));
		
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
			return getBooleanSimulationInfo();
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
    public void update() {
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

    @Override
	public boolean isLeaves(Block b, int x, int y, int z) {
		return b == Blocks.leaves;
	}
    @Override
	public boolean isWood(Block b, int x, int y, int z) {
		return b == Blocks.log || b == Blocks.log2;
	}
    @Override
	public boolean isSapling(ItemStack sapling) {
		return sapling != null && Block.getBlockFromItem(sapling.getItem()) == Blocks.sapling;
	}
	

	private int getPlantSize() {
		if (plantSize != null) {
			return plantSize.getSize();
		}else{
			return 1;
		}
	}
	
	
	private void destroyLeaveBlockOnTrack(int x, int y, int z) {
        Block b = getVehicle().getWorld().getBlock(x, y, z);

        if (b != null && isLeavesHandler(b, x, y, z)) {
        	getVehicle().getWorld().setBlockToAir(x, y, z);
        }		
	}
}