package vswe.stevesvehicles.tileentity;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevesvehicles.localization.PlainText;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.old.Helpers.DropDownMenuItem;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.block.BlockCartAssembler;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.old.Helpers.*;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.upgrade.effect.assembly.FreeModules;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.old.Containers.ContainerCartAssembler;
import vswe.stevesvehicles.old.Containers.ContainerUpgrade;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.client.gui.screen.GuiCartAssembler;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.container.slots.SlotAssembler;
import vswe.stevesvehicles.container.slots.SlotAssemblerFuel;
import vswe.stevesvehicles.container.slots.SlotHull;
import vswe.stevesvehicles.container.slots.SlotOutput;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;
import vswe.stevesvehicles.upgrade.effect.fuel.CombustionFuel;
import vswe.stevesvehicles.upgrade.effect.external.Deployer;
import vswe.stevesvehicles.upgrade.effect.assembly.Disassemble;
import vswe.stevesvehicles.upgrade.effect.fuel.FuelCapacity;
import vswe.stevesvehicles.upgrade.effect.fuel.FuelCost;
import vswe.stevesvehicles.upgrade.effect.external.Manager;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlat;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlatCart;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlatRemoved;
import vswe.stevesvehicles.upgrade.effect.assembly.WorkEfficiency;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

/**
 * The tile entity used by the Cart Assembler
 * @author Vswe
 *
 */
public class TileEntityCartAssembler extends TileEntityBase
    implements IInventory, ISidedInventory {
	
	/**
	 * ASSEMBLING VARIABLES
	 */
	
	/**
	 * When this time is reached the cart is finished and will be placed in the output slot
	 */
	private int maxAssemblingTime;
	
	/**
	 * The time the cart has been assembled
	 */
	private float currentAssemblingTime = -1;
	
	/**
	 * The item that will appear in the output slot when the assembling is done, i.e. the cart being assembled
	 */
	protected ItemStack outputItem;
	
	/**
	 * Modules that are being removed by the assembler, used when modifying cart
	 */
	protected ArrayList<ItemStack> spareModules;
	
	/**
	 * Defines if the Cart Assembler is busy
	 */
	private boolean isAssembling;
	
	
	
	
	
	/**
	 * GRAPHICAL VARIABLES
	 */	
	
	/**
	 * Whether the cached error list needs to be recalculated or not
	 */
	public boolean isErrorListOutdated;

    public boolean isFreeModulesOutdated;
	
	/**
	 * The graphical boxes drawn as module headers
	 */
	private ArrayList<TitleBox> titleBoxes;
	
	/**
	 * The graphical menus drawn in the dropdown menu at the simulated cart
	 */
	private ArrayList<DropDownMenuItem> dropDownItems;
	
	/**
	 * The object controlling the simulation of the cart
	 */
	private SimulationInfo info;
	
	/**
	 * If the simulated cart should spin or not, this is false if the player is holding the cart with the mouse.
	 */
	private boolean shouldSpin = true;
	
	/**
	 * The simulated cart, this cart will only exist on the client side
	 */
	private VehicleBase placeholder;
	
	/**
	 * The current yaw (rotation) of the simulated cart
	 */
	private float yaw = 0F;
	
	/**
	 * The current roll (rotation) of the simulated cart
	 */
	private float roll = 0F;
	
	/**
	 * Whether the simulated cart is current rolling up or down.
	 */
	private boolean rollDown = false;

    /**
     * Which tab is selected in the interface (this is here to prevent reset on reopening the interface)
     */
    public int selectedTab;

    public ModuleSortMode sortMode = ModuleSortMode.NORMAL;
	
	
	/**
	 * SLOT VARIABLES
	 */
	

	/**
	 * All the slots this tile entity is using
	 */
	private ArrayList<SlotAssembler> slots;
	
	/**
	 * All the engine slots this tile entity is using
	 */
	private ArrayList<SlotAssembler> engineSlots;
	
	/**
	 * All the addon slots this tile entity is using
	 */
	private ArrayList<SlotAssembler> addonSlots;
	
	/**
	 * All storage slots this tile entity is using
	 */
	private ArrayList<SlotAssembler> chestSlots;	
	
	/**
	 * All the attachment slots this tile entity is using
	 */
	private ArrayList<SlotAssembler> attachmentSlots;
	
	/**
	 * The hull slot of this tile entity, this is where the user puts the hull to start designing the cart.
	 */
	private SlotHull hullSlot;
	
	/**
	 * The tool slot of this tile entity
	 */
	private SlotAssembler toolSlot;
	
	/**
	 * The slot where the finished cart will be placed.
	 */
	private SlotOutput outputSlot;
	
	/**
	 * The slot where the user puts any fuel to power the cart assembler
	 */
	private SlotAssemblerFuel fuelSlot;
	
	/**
	 * All the slot indices that should be accessed from the top or the bottom of the Cart Assembler block
	 */
    private final int[] topBotSlots;
    
    /**
     * All the slot indices that should be accessed from the sides of the Cart Assembler block
     */
    private final int[] sideSlots;		
	
    
    
    
    
	/**
	 * OTHER VARIABLES
	*/
	     
    /**
     * The hull that was the active one the last time the hull was checked. This is used to handle the change or removal of hulls.
     */
	private ItemStack lastHull;
	
	/**
	 * The current loaded level of fuel. This is the level of the turquoise bar, not how much coal is in the slot.
	 */
	private float fuelLevel;	
	
	/**
	 * A list of all the upgrades currently attached to this Cart Assembler
	 */
	private ArrayList<TileEntityUpgrade> upgrades;
	
	/**
	 * Used to properly detach any upgrades when the Cart Assembler block is broken
	 */
	public boolean isDead;
	

	
	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiCartAssembler(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerCartAssembler(inv, this);		
	}
	
	
    public TileEntityCartAssembler() {
    	//create all the lists for everything
		upgrades = new ArrayList<TileEntityUpgrade>();
		spareModules = new ArrayList<ItemStack>(); 		
		dropDownItems = new ArrayList<DropDownMenuItem>();		
		slots = new ArrayList<SlotAssembler>();
		engineSlots = new ArrayList<SlotAssembler>();
		addonSlots = new ArrayList<SlotAssembler>();
		chestSlots = new ArrayList<SlotAssembler>();
		attachmentSlots = new ArrayList<SlotAssembler>();
		titleBoxes = new ArrayList<TitleBox>();
		
		int slotID = 0;
		
		//create the hull slot
		hullSlot = new SlotHull(this, slotID++, 18,25);
		slots.add(hullSlot);


        //TODO localize these
		//create the title boxes at certain positions with certain colors
		TitleBox engineBox = new TitleBox(new PlainText("Engines"), 65, 0xF7941D);
		TitleBox toolBox = new TitleBox(new PlainText("Tool"), 100, 0x662D91);
		TitleBox attachBox = new TitleBox(new PlainText("Attachments"), 135, 0x005B7F);
		TitleBox storageBox = new TitleBox(new PlainText("Storage"), 170, 0x9E0B0E);
		TitleBox addonBox = new TitleBox(new PlainText("Addons"), 205, 0x005826);
		TitleBox infoBox = new TitleBox(new PlainText("Information"), 375, 30, 0xCCBE00);
		titleBoxes.add(engineBox);
		titleBoxes.add(toolBox);
		titleBoxes.add(attachBox);
		titleBoxes.add(storageBox);
		titleBoxes.add(addonBox);
		titleBoxes.add(infoBox);
		
		///create the engine slots
		for (int i = 0; i < 5; i++) {
			SlotAssembler slot = new SlotAssembler(this, slotID++, engineBox.getX() + 2 + 18*i,engineBox.getY(), ModuleType.ENGINE, false,i);
			slot.invalidate();
			slots.add(slot);
			engineSlots.add(slot);
		}
		

		//create the tool slot
		toolSlot = new SlotAssembler(this, slotID++, toolBox.getX() + 2,toolBox.getY(), ModuleType.TOOL, false,0);
		slots.add(toolSlot);		
		toolSlot.invalidate();
		
		//create the attachment slots
		for (int i = 0; i < 6; i++) {
			SlotAssembler slot = new SlotAssembler(this, slotID++, attachBox.getX() + 2 + 18*i,attachBox.getY(), ModuleType.ATTACHMENT, false,i);
			slot.invalidate();
			slots.add(slot);
			attachmentSlots.add(slot);
		}		
		
		//create the storage slots
		for (int i = 0; i < 6; i++) {
			SlotAssembler slot = new SlotAssembler(this, slotID++, storageBox.getX() + 2 + 18*i,storageBox.getY(), ModuleType.STORAGE, false,i);
			slot.invalidate();
			slots.add(slot);
			chestSlots.add(slot);
		}
		
		//create the addon slots
		for (int i = 0; i < 12; i++) {
			SlotAssembler slot = new SlotAssembler(this, slotID++, addonBox.getX() + 2 + 18*(i%6), addonBox.getY() + 18 * (i/6), ModuleType.ADDON, false, i);
			slot.invalidate();
			slots.add(slot);
			addonSlots.add(slot);
		}	
		
		//create the fuel and output slots
		fuelSlot = new SlotAssemblerFuel(this, slotID++, 395,220);
		slots.add(fuelSlot);
		outputSlot = new SlotOutput(this, slotID, 450,220);
		slots.add(outputSlot);
		
		//create the simulation info
		info = new SimulationInfo();
		
		//create the place to store all the items for the slots
		inventoryStacks = new ItemStack[slots.size()];
		
		//create the arrays used by ISidedInventory
		topBotSlots = new int[] {getSizeInventory() - nonModularSlots()};
		sideSlots = new int[] {getSizeInventory() - nonModularSlots() + 1};
    }

	
    /**
     * Clears the list of upgrades, used before refilling it again to keep it up to date
     */
	public void clearUpgrades() {
		upgrades.clear();
	}

	/**
	 * Add an upgrade to the list of upgrades for this Cart Assembler
	 * @param upgrade The upgrade to be added
	 */
	public void addUpgrade(TileEntityUpgrade upgrade) {
		upgrades.add(upgrade);
    }
	
	/**
	 * Remove an upgrade from the list of upgrades for this Cart Assembler
	 * @param upgrade The upgrade to be removed
	 */
	public void removeUpgrade(TileEntityUpgrade upgrade) {
		upgrades.remove(upgrade);
	}
	
	/**
	 * Get a list of all the upgrades. This is not the upgrades themselves, but rather the tile entities holding the upgrades
	 * @return A list of the tile entities
	 */
	public ArrayList<TileEntityUpgrade> getUpgradeTiles() {
		return upgrades;
	}


	/**
	 * Get a list of all the upgrade effects of this Cart Assembler. This is every effect on the upgrade on every tile entity upgrade.
	 * @return A list of all the effects.
	 */
	public ArrayList<BaseEffect> getEffects() {
		ArrayList<BaseEffect> lst = new ArrayList<BaseEffect>();
		
		//go through all the upgrades attached to the cart assembler
		for (TileEntityUpgrade tile : upgrades) {
			List<BaseEffect> effects = tile.getEffects();
			if (effects != null) {
                lst.addAll(effects);
			}
		}
		return lst;
	}	
	

	/**
	 * Get the object that controls the simulation of the cart currently being designed
	 * @return The simulation object
	 */
	public SimulationInfo getSimulationInfo() {
		return info;
	}
	
	/**
	 * Get the menu items in the drop down menu used by the player to change the simulation of the cart being designed
	 * @return The drop down menus
	 */
	public ArrayList<DropDownMenuItem> getDropDown() {
		return dropDownItems;
	}	

	/**
	 * Get the title boxes used for rendering titles for the different module groups
	 * @return The list of title boxes
	 */
	public ArrayList<TitleBox> getTitleBoxes() {
		return titleBoxes;
	}
	
	/**
	 * Get the ItemStack size that is representing that a module is marked for removal
	 * @return The size value
	 */
	public static int getRemovedSize() {
		return -1;
	}
	
	/**
	 * Get the ItemStack size that is representing that a module is marked to be kept
	 * @return The size value
	 */
	public static int getKeepSize() {
		return 0;
	}

	/**
	 * Get all the slots used by this Cart Assembler
	 * @return All the slots this tile entity is using
	 */
	public ArrayList<SlotAssembler> getSlots() {
		return slots;
	}
	
	/**
	 * Get all the engine slots used by this Cart Assembler
	 * @return All the engine slots this tile entity is using
	 */
	public ArrayList<SlotAssembler> getEngines() {
		return engineSlots;
	}
	
	/**
	 * Get all the storage slots used by this Cart Assembler
	 * @return All the storage slots this tile entity is using
	 */
	public ArrayList<SlotAssembler> getChests() {
		return chestSlots;
	}	
	
	/**
	 * Get all the addon slots used by this Cart Assembler
	 * @return All the addon slots this tile entity is using
	 */	
	public ArrayList<SlotAssembler> getAddons() {
		return addonSlots;
	}
	
	/**
	 * Get all the attachment slots used by this Cart Assembler
	 * @return All the attachment slots this tile entity is using
	 */	
	public ArrayList<SlotAssembler> getAttachments() {
		return attachmentSlots;
	}			
	
	/**
	 * Get the tool slot that is used by this Cart Assembler
	 * @return The tool slot of this tile entity
	 */
	public SlotAssembler getToolSlot() {
		return toolSlot;
	}
	
	/**
	 * Get the full time it takes to complete assembling the cart. Doesn't take into account the time it has been worked on or how fast it is working.
	 * @return The total time in ticks
	 */
	public int getMaxAssemblingTime() {
		return maxAssemblingTime;
	}
	
	/**
	 * Get the time the assembler has been working on the cart. This time is increased faster if the assembler is working faster.
	 * @return The current time in ticks
	 */
	public int getAssemblingTime() {
		return (int)currentAssemblingTime;
	}
	
	/**
	 * Set the amount of ticks the assembler has been working on the cart.
	 * @param val The amount of ticks
	 */
	private void setAssemblingTime(int val) {
		currentAssemblingTime = val;
	}
	
	/**
	 * Get if the Cart Assembler is busy working or not
	 * @return If it's busy
	 */
	public boolean getIsAssembling() {
		return isAssembling;
	}
	
	/**
	 * Starts the assembling process of the cart in the designer if possible
	 */
	public void doAssemble() {
		//a cart is only allowed to be created if there's no errors
		if (!hasErrors()) {			
			//calculate the time it will take to create the cart and save that
			maxAssemblingTime = generateAssemblingTime();
			
			//create the cart, it will save the created cart, any spare modules as well as clearing the design area
			createCartFromModules();				
			
			//mark that the assembler is busy
			isAssembling = true;
			
			//remove the cart being edited, if any
            for (BaseEffect effect : getEffects()) {
                if (effect instanceof Disassemble) {
                    Disassemble disassemble = (Disassemble)effect;
                    disassemble.onVehicleCreation(outputItem);
                }
            }

		}	
	}
	
	@Override
	public void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			//if a player clicked the assemble button, try to assemble the cart
			doAssemble();
		}else if(id == 1) {

			//if a slot was clicked with a module of an already existing cart, mark it for removal or to keep it depending on what it already is. This is also used to remove modules in free mode.
			int slotId = data[0];
            if (slotId >= 0 && slotId < getSlots().size()) {
                SlotAssembler slot = getSlots().get(slotId);
                if (slot.getStack() != null) {
                    if (slot.getStack().stackSize > 0) {
                        boolean canRemove = freeMode;
                        if (canRemove && slotId == 0) {
                            for (int i = 1; i < getSlots().size() - nonModularSlots(); i++) {
                                if (getSlots().get(i).getHasStack()) {
                                    canRemove = false;
                                    break;
                                }
                            }
                        }

                        if (canRemove) {
                            slot.putStack(null);
                        }
                    }else if (slotId != 0) {
                        if (slot.getStack().stackSize == getKeepSize()) {
                            slot.getStack().stackSize = getRemovedSize();
                        }else{
                            slot.getStack().stackSize = getKeepSize();
                        }
                    }
                }
			}
        }else if(id == 2) {
            int b1 = data[1];
            if (b1 < 0) {
                b1 += 256;
            }
            int b2 = data[0];
            if (b2 < 0) {
                b2 += 256;
            }

            int val = (b2 << 8) | b1;
            ModuleData moduleData = ModuleRegistry.getModuleFromId(val);
            if (moduleData != null) {
                if (moduleData instanceof ModuleDataHull && getHullModule() != null) {
                    ItemStack item = moduleData.getItemStack();
                    hullSlot.putStack(item);

                    invalidateAll();
                    validateAll();

                    for (SlotAssembler slotAssembler : getSlots()) {
                        if (!slotAssembler.isValid()) {
                            slotAssembler.putStack(null);
                        }
                    }

                    lastHull = item;
                }else{
                    ItemStack item = moduleData.getItemStack();
                    for (SlotAssembler slot : slots) {
                        if (!slot.getHasStack() && slot.isItemValid(item)) {
                            slot.putStack(item);
                            break;
                        }
                    }
                }
            }
        }
	}



	/**
	 * Called when a upgrade is removed or added to this Cart Assembler
	 */
	public void onUpgradeUpdate() {

        //will also get modules that is just kept when modifying a cart, this is a huge problem (and the reason why this code isn't used)
		/*
		ArrayList<ModuleData> modules = ModuleData.getModulesFromItems(ModuleData.getModularItems(outputItem)); 
		ArrayList<ModuleData> removed = new ArrayList<ModuleData>(); 
		for (ItemStack item : spareModules) {
			ModuleData module = StevesCarts.instance.modules.getModuleData(item);
			if (module != null) {
				removed.add(module);
			}	
		}		
		maxAssemblingTime = generateAssemblingTime(modules, removed);
		*/

        freeMode = false;
        for (BaseEffect baseEffect : getEffects()) {
            if (baseEffect instanceof FreeModules) {
                freeMode = true;
                break;
            }
        }
	}
	
	/**
	 * Generate the time it takes to assemble the cart in the designer, note that this is not the full time of the cart
	 * currently being assembled. That can however be retrieved with getMaxAssemblingTime()
	 * @return The time it takes to make the cart
	 */
	public int generateAssemblingTime() {
		return generateAssemblingTime(
		getModules(true, new int[] {getKeepSize(), getRemovedSize()}),
		getModules(true, new int[] {getKeepSize(), 1})
		);
	}

	/**
	 * Generate the time it takes to assemble a bunch of modules
	 * @param modules A list of modules to assemble
	 * @param removed A list of module to disassemble
	 * @return The time it takes
	 */
	private int generateAssemblingTime(ArrayList<ModuleData> modules, ArrayList<ModuleData> removed) {
		int timeRequired = 100;
			
		for (ModuleData module : modules) {
			timeRequired += getAssemblingTime(module, false);
		}
		for (ModuleData module : removed) {
			timeRequired += getAssemblingTime(module, true);
		}		
		
		for (BaseEffect effect : getEffects()) {
			if (effect instanceof TimeFlatCart) {
				timeRequired += ((TimeFlatCart)effect).getTicks();
			}
		}		

		return Math.max(0, timeRequired);
	}
	
	/**
	 * Get assembling or disassembling time for a specific module
	 * @param module The module to assemble
	 * @param isRemoved If the module is being added(assembled) or removed(disassembled)
	 * @return The time it takes
	 */
	private int getAssemblingTime(ModuleData module, boolean isRemoved) {
		int time = (int)(5 * Math.pow(module.getCost(), 2.2));
		time += getTimeDecreased(isRemoved);
		
		return Math.max(0, time);
	}
	
	/**
	 * Get the cart that is the result of the modules in the design view. All the modules will however be left where they are.
	 * @param isSimulated If this is just a simulation
	 * @return An assembled cart
	 */
	public ItemStack getCartFromModules(boolean isSimulated) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
			ItemStack item = getStackInSlot(i);
			if (item != null) {
				if (item.stackSize != getRemovedSize()) {
					items.add(item);
				}else if (!isSimulated){
					ItemStack spare = item.copy();
					spare.stackSize = 1;
					spareModules.add(spare);
				}
			}
		}
		return ModuleDataItemHandler.createModularVehicle(items);
	}
	
	/**
	 * Create a cart and store it to be put in the output slot when the Cart Assembler is done. Will also clear all the modules from the design view.
	 */
	private void createCartFromModules() {
		spareModules.clear();
		
		//create the cart
		outputItem = getCartFromModules(false);
		
		
		if (outputItem != null) {
			//if a cart was properly made, remove all the modules
			for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
				setInventorySlotContents(i, null);
			}
		}else{
			//if something went wrong, clear the spare modules, no free modules here.
			spareModules.clear();
		}

	}
	
	public ArrayList<ModuleData> getNonHullModules() {
		return getModules(false);
	}
	
	public ArrayList<ModuleData> getModules(boolean includeHull) {
		return getModules(includeHull, new int[] {getRemovedSize()});
	}
	
	public ArrayList<ModuleData> getModules(boolean includeHull, int[] invalid) {
		ArrayList<ModuleData> modules = new ArrayList<ModuleData>();
		for (int i = includeHull ? 0 : 1; i < getSizeInventory() - nonModularSlots(); i++) {
			ItemStack item = getStackInSlot(i);

			if (item != null) {
			
				boolean validSize = true;
                for (int invalidItem : invalid) {
                    if (invalidItem == item.stackSize || (invalidItem > 0 && item.stackSize > 0)) {
                        validSize = false;
                        break;
                    }
                }
				
				if (validSize) {
					ModuleData module = ModItems.modules.getModuleData(item);
					if (module != null) {
						modules.add(module);
					}
				}
			}
		}
		return modules;
	}	
	
	public ModuleDataHull getHullModule() {
		if (getStackInSlot(0) != null) {
			ModuleData hullData = ModItems.modules.getModuleData(getStackInSlot(0));
			if(hullData instanceof ModuleDataHull) {
				return (ModuleDataHull)hullData;
			}
		}
		return null;
	}
	
	
	private boolean hasErrors() {
		return getErrors().size() > 0;
	}
	
	public ArrayList<String> getErrors() {	
		ArrayList<String> errors = new ArrayList<String>();
		
		if (hullSlot.getStack() == null) {
			errors.add(LocalizationAssembler.NO_HULL.translate());
		}else{
			ModuleData hullData = ModItems.modules.getModuleData(getStackInSlot(0));
			if (hullData == null || !(hullData instanceof ModuleDataHull)) {
				errors.add(LocalizationAssembler.INVALID_HULL_SHORT.translate());
			}else{
				if (isAssembling) {
					errors.add(LocalizationAssembler.BUSY_ASSEMBLER.translate());
				}else if (outputSlot != null && outputSlot.getStack() != null) {
					errors.add(LocalizationAssembler.OCCUPIED_DEPARTURE_BAY.translate());
				}
			
			
				
				
				ArrayList<ModuleData> modules = new ArrayList<ModuleData>();
				for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
					if (getStackInSlot(i) != null) {
						ModuleData data = ModItems.modules.getModuleData(getStackInSlot(i));
						if (data != null) {
							modules.add(data);
						}
					}				
				}
				
				String error = ModuleDataItemHandler.checkForErrors((ModuleDataHull)hullData, modules);
				if (error != null) {
					errors.add(error);
				}
			}
		}		
		

		return errors;
	}
		
	public int getTotalCost() {
		ArrayList<ModuleData> modules = new ArrayList<ModuleData>();
		for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
			if (getStackInSlot(i) != null) {
				ModuleData data = ModItems.modules.getModuleData(getStackInSlot(i));
				if (data != null) {
					modules.add(data);
				}
			}				
		}
		return ModuleDataItemHandler.getTotalCost(modules);
	}	
		

	
	@Override
	public void initGuiData(Container con, ICrafting crafting) {
		updateGuiData(con, crafting, 0, getShortFromInt(true,maxAssemblingTime));
		updateGuiData(con, crafting, 1, getShortFromInt(false,maxAssemblingTime));
		updateGuiData(con, crafting, 2, getShortFromInt(true,getAssemblingTime()));
		updateGuiData(con, crafting, 3, getShortFromInt(false,getAssemblingTime()));
		updateGuiData(con, crafting, 4, (short)(isAssembling ? 1 : 0));
		updateGuiData(con, crafting, 5, getShortFromInt(true, getFuelLevel()));
		updateGuiData(con, crafting, 6, getShortFromInt(false,getFuelLevel()));		
	}
	@Override
	public void checkGuiData(Container container, ICrafting crafting) {
		ContainerCartAssembler con = (ContainerCartAssembler)container;
		if (con.lastMaxAssemblingTime != maxAssemblingTime) {
			updateGuiData(con, crafting, 0, getShortFromInt(true,maxAssemblingTime));	
			updateGuiData(con, crafting, 1, getShortFromInt(false,maxAssemblingTime));	
			con.lastMaxAssemblingTime = maxAssemblingTime;	
		}		
		if (con.lastIsAssembling != isAssembling) {
			updateGuiData(con, crafting, 4, (short)(isAssembling ? 1 : 0));	
			con.lastIsAssembling = isAssembling;	
		}	
		if (con.lastFuelLevel != getFuelLevel()) {
			updateGuiData(con, crafting, 5, getShortFromInt(true,getFuelLevel()));	
			updateGuiData(con, crafting, 6, getShortFromInt(false,getFuelLevel()));	
			con.lastFuelLevel = getFuelLevel();	
		}		
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			maxAssemblingTime =getIntFromShort(true, maxAssemblingTime, data);
		}else if (id == 1) {
			maxAssemblingTime =getIntFromShort(false, maxAssemblingTime, data);
		}else if (id == 2) {
			setAssemblingTime(getIntFromShort(true, getAssemblingTime(), data));
		}else if (id == 3) {
			setAssemblingTime(getIntFromShort(false, getAssemblingTime(), data));		
		}else if(id == 4) {	
			isAssembling = data != 0;
			if (!isAssembling) {
				setAssemblingTime(0);
			}
		}else if(id == 5) {
			setFuelLevel(getIntFromShort(true, getFuelLevel(), data));
		}else if(id == 6) {
			setFuelLevel(getIntFromShort(false, getFuelLevel(), data));			
		}
	}


	private void invalidateAll() {
		for (int i = 0; i < getEngines().size(); i++) {
			getEngines().get(i).invalidate();
		}
		for (int i = 0; i < getAddons().size(); i++) {
			getAddons().get(i).invalidate();
		}
		for (int i = 0; i < getChests().size(); i++) {
			getChests().get(i).invalidate();
		}	
		for (int i = 0; i < getAttachments().size(); i++) {
			getAttachments().get(i).invalidate();
		}			
		getToolSlot().invalidate();		
	}
	
	private void validateAll() {
		if (hullSlot == null) {
			return;
		}
	
		ArrayList<SlotAssembler> slots = getValidSlotFromHullItem(hullSlot.getStack());
		if (slots != null) {
			for (SlotAssembler slot : slots) {
				slot.validate();
			}
		}
	}		
		
	public ArrayList<SlotAssembler> getValidSlotFromHullItem(ItemStack hullItem) {
		
		if (hullItem != null) {
			ModuleData data = ModItems.modules.getModuleData(hullItem);
			if (data != null && data instanceof ModuleDataHull) {
				ModuleDataHull hull = (ModuleDataHull)data;
				return getValidSlotFromHull(hull);
			}
		}
		
		return null;
	}	
		
	private ArrayList<SlotAssembler> getValidSlotFromHull(ModuleDataHull hull) {
		 ArrayList<SlotAssembler> slots = new ArrayList<SlotAssembler>();
		 
			for (int i = 0; i < hull.getEngineMaxCount(); i++) {
				slots.add(getEngines().get(i));
			}
			for (int i = 0; i < hull.getAddonMaxCount(); i++) {
				slots.add(getAddons().get(i));
			}	
			for (int i = 0; i < hull.getStorageMaxCount(); i++) {
				slots.add(getChests().get(i));
			}	
			for (int i = 0; i < getAttachments().size(); i++) {
				slots.add(getAttachments().get(i));
			}				
			slots.add(getToolSlot());

		return slots;
	}
	
	
	public int getMaxFuelLevel() {
		int capacity = 4000;
		for (BaseEffect effect : getEffects()) {
			if (effect instanceof FuelCapacity) {
				capacity += ((FuelCapacity)effect).getFuelCapacity();
			}
		}
		
		if (capacity > 200000) {
			capacity = 200000;
		}else if(capacity < 1) {
			capacity = 1;
		}
		return capacity;
	}
	
	public boolean isCombustionFuelValid() {
		for (BaseEffect effect : getEffects()) {
			if (effect instanceof CombustionFuel) {
				return true;
			}
		}	
		return false;
	}

	public int getFuelLevel() {
		return (int)fuelLevel;
	}	
	
	public void setFuelLevel(int val) {
		fuelLevel = val;
	}	
	
	private int getTimeDecreased(boolean isRemoved) {
		int timeDecrement = 0;
		for (BaseEffect effect : getEffects()) {
			if (effect instanceof TimeFlat && !(effect instanceof TimeFlatRemoved)) {
				timeDecrement += ((TimeFlat)effect).getTicks();
			}
		}
		
		if (isRemoved) {
			for (BaseEffect effect : getEffects()) {
				if (effect instanceof TimeFlatRemoved) {
					timeDecrement += ((TimeFlat)effect).getTicks();
				}
			}		
		}
		
		return timeDecrement;
	}

	private float getFuelCost() {
		float cost = 1.0F;
	
		for (BaseEffect effect : getEffects()) {
			if (effect instanceof FuelCost) {
				cost *= ((FuelCost)effect).getCost();
			}
		}

		return cost;
	}
	
	public float getEfficiency() {
		float efficiency = 1.0F;
	
		for (BaseEffect effect : getEffects()) {
			if (effect instanceof WorkEfficiency) {
				efficiency += ((WorkEfficiency)effect).getEfficiency();
			}
		}
	
		return efficiency;
	}
	
	private void deployCart() {
        for (BaseEffect effect : getEffects()) {
            if (effect instanceof Deployer) {
                TileEntityUpgrade tile = effect.getUpgrade();
                int x = 2 * tile.xCoord - xCoord;
                int y = 2 * tile.yCoord - yCoord;
                int z = 2 * tile.zCoord - zCoord;

                if (tile.yCoord > yCoord) {
                    y += 1;
                }

                if (BlockRailBase.func_150049_b_(worldObj, x, y, z)) {
                    try {
                        NBTTagCompound info = outputItem.getTagCompound();
                        if (info != null) {
                            EntityModularCart cart = new EntityModularCart(worldObj, x + 0.5F, y + 0.5F, z + 0.5F, info, outputItem.hasDisplayName() ? outputItem.getDisplayName() : null);


                            worldObj.spawnEntityInWorld(cart);
                            cart.temppushX = tile.xCoord - xCoord;
                            cart.temppushZ = tile.zCoord - zCoord;
                            managerInteract(cart, true);

                            return;
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

		
		outputSlot.putStack(outputItem);
	}
	
	public void managerInteract(EntityModularCart cart, boolean toCart) {
        for (BaseEffect effect : getEffects()) {
            if (effect instanceof Manager) {
                TileEntityUpgrade tile = effect.getUpgrade();
                int x2 = 2 * tile.xCoord - xCoord;
                int y2 = 2 * tile.yCoord - yCoord;
                int z2 = 2 * tile.zCoord - zCoord;

                if (tile.yCoord > yCoord) {
                    y2 += 1;
                }

                TileEntity managerTile = worldObj.getTileEntity(x2, y2, z2);
                if (managerTile != null && managerTile instanceof TileEntityManager) {
                    ManagerTransfer transfer = new ManagerTransfer();

                    transfer.setCart(cart);
                    if (tile.yCoord != yCoord) {
                        transfer.setSide(-1);
                    }else if(tile.xCoord < xCoord) {
                        //red
                        transfer.setSide(0);
                    }else if(tile.xCoord > xCoord) {
                        //green
                        transfer.setSide(3);
                    }else if(tile.zCoord < zCoord) {
                        //blue
                        transfer.setSide(1);
                    }else if(tile.zCoord > zCoord) {
                        //yellow
                        transfer.setSide(2);
                    }

                    if (toCart) {
                        transfer.setFromCartEnabled(false);
                    }else{
                        transfer.setToCartEnabled(false);
                    }


                    TileEntityManager manager = ((TileEntityManager)managerTile);

                    //noinspection StatementWithEmptyBody
                    while (manager.exchangeItems(transfer));
                }
            }

		}		
	}
	
	private void deploySpares() {
        for (BaseEffect effect : getEffects()) {
            if (effect instanceof Disassemble) {
                for (ItemStack item : spareModules)  {
                    TransferHandler.TransferItem(
                                item,
                                effect.getUpgrade(),
                                new ContainerUpgrade(null,effect.getUpgrade()),
                                1
                                );
                    if (item.stackSize > 0) {
                        puke(item);
                    }
                }
                return;
            }
        }
	}
	
	public void puke(ItemStack item) {
		EntityItem entityitem = new EntityItem(worldObj, xCoord, yCoord + 0.25, zCoord , item);
		entityitem.motionX = (0.5F - worldObj.rand.nextFloat()) / 10;
		entityitem.motionY = 0.15F;
		entityitem.motionZ = (0.5F - worldObj.rand.nextFloat()) / 10;
		worldObj.spawnEntityInWorld(entityitem);		
	}
	
	private boolean loaded;
	public void updateEntity() {
		if (!loaded) {
            ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(worldObj, xCoord, yCoord, zCoord);
			loaded = true;
		}

        //TODO rewrite this (this allows cart that were assembling when a assembler was broken to be put into the output slot and allow it to continue)
		/*if (!isAssembling && outputSlot != null && outputSlot.getStack() != null) {
			ItemStack itemInSlot = outputSlot.getStack();
			if (itemInSlot.getItem() == ModItems.cart) {
			
				NBTTagCompound info = itemInSlot.getTagCompound();
				if (info != null && info.hasKey("maxTime")) {
					ItemStack newItem = new ItemStack(ModItems.cart);
					
					NBTTagCompound save = new NBTTagCompound();
					save.setByteArray("Modules", info.getByteArray("Modules"));				
					newItem.setTagCompound(save);
					
					int moduleCount = info.getByteArray("Modules").length;
					
					maxAssemblingTime = info.getInteger("maxTime");
					setAssemblingTime(info.getInteger("currentTime"));	
					spareModules.clear();
					if (info.hasKey("Spares")) {
						byte[] moduleIDs = info.getByteArray("Spares");
						for (int i = 0; i < moduleIDs.length; i++) {
							byte id = moduleIDs[i];
							ItemStack module = new ItemStack(ModItems.modules, 1, id);
							ModItems.modules.addExtraDataToModule(module, info, i + moduleCount);
							spareModules.add(module);
						}
					}
					
					if (itemInSlot.hasDisplayName()) {
						newItem.setStackDisplayName(itemInSlot.getDisplayName());
					}
					
					isAssembling = true;					
					outputItem = newItem;
					outputSlot.putStack(null);
					
				}		
			}	
		}*/
	
		if (getFuelLevel() > getMaxFuelLevel()) {
			setFuelLevel(getMaxFuelLevel());
		}	
	
		if (isAssembling && outputSlot != null) {
			if (getFuelLevel() >= getFuelCost()) {
				currentAssemblingTime += getEfficiency();
				fuelLevel-= getFuelCost();
				if (getFuelLevel() <= 0) {
					setFuelLevel(0);
				}
				if (getAssemblingTime() >= maxAssemblingTime) {
					isAssembling = false;
					setAssemblingTime(0);	
					if (!worldObj.isRemote) {
						deployCart();						
						outputItem = null;
						deploySpares();
						spareModules.clear();
					}
				}
			}
			
		}
		
		if (!worldObj.isRemote && fuelSlot != null && fuelSlot.getStack() != null) {
			int fuel = fuelSlot.getFuelLevel(fuelSlot.getStack());

			if (fuel > 0 && getFuelLevel() + fuel <= getMaxFuelLevel()) {
				setFuelLevel(getFuelLevel() + fuel);			
				if (fuelSlot.getStack().getItem().hasContainerItem(fuelSlot.getStack())) {
					fuelSlot.putStack(new ItemStack(fuelSlot.getStack().getItem().getContainerItem()));
				} else{
					fuelSlot.getStack().stackSize--;
				}						
				if (fuelSlot.getStack().stackSize <= 0) {
					fuelSlot.putStack(null);
				}
			}
		}
		
		updateSlots();
		
		handlePlaceholder();

	}

	public void updateSlots() {
		if (hullSlot != null) {
			if (lastHull != null && hullSlot.getStack() == null) {
					invalidateAll();
				}else if (lastHull == null && hullSlot.getStack() != null) {
					validateAll();
				}else if (lastHull != hullSlot.getStack()) {
					invalidateAll();
					validateAll();
				}
			
			
			lastHull = hullSlot.getStack();
		}
		
		for (SlotAssembler slot : slots) {
			slot.update();
		}
			
	}
	
	public void resetPlaceholder() {
		placeholder = null;
	}
	
	public vswe.stevesvehicles.vehicle.VehicleBase getPlaceholder() {
		return placeholder;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public void setYaw(float val) {
		yaw = val;
	}
	
	public void setRoll(float val) {
		roll = val;
	}	
	
	public void setSpinning(boolean val) {
		shouldSpin = val;
	}
	
	public int nonModularSlots() {
		return 2;
	}

	private void handlePlaceholder() {
		if (worldObj.isRemote) {
			if (placeholder == null) {
				return;
			}
			
			if (!StevesVehicles.freezeCartSimulation) {
				int minRoll = -5;
				int maxRoll = 25;			
				if (shouldSpin) {
					yaw += 2F;	
					roll = roll % 360;
					if (!rollDown) {
						if (roll < minRoll - 3) {
							roll += 5;
						}else{
							roll += 0.2F;
						}
							
						if (roll > maxRoll) {
							rollDown = true;
						}
					}else{
						if (roll > maxRoll + 3) {
							roll -= 5;
						}else{
							roll -= 0.2F;
						}
						
						if (roll < minRoll) {
							rollDown = false;
						}
					}
				}
			}
			
			placeholder.onUpdate();
			if (placeholder == null) {
				return;
			}
			placeholder.updateFuel();
		}	
	}
	
	public boolean createPlaceholder() {
		if (placeholder == null) {
            ModuleDataHull hull = getHullModule();
            if (hull != null && hull.getValidVehicles() != null && !hull.getValidVehicles().isEmpty()) {
                try {
                    Constructor<? extends IVehicleEntity> constructor = hull.getValidVehicles().get(0).getClazz().getConstructor(World.class);
                    Object obj = constructor.newInstance(worldObj);
                    placeholder = ((IVehicleEntity)obj).getVehicle();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (placeholder != null) {
                    placeholder.setPlaceholder(this);
                    updatePlaceholder();
                }
            }
		}

        return placeholder != null;
	}
	
	public void updatePlaceholder() {
        if (placeholder != null) {
            ModuleDataHull hull = getHullModule();
            if (hull == null || hull.getValidVehicles() == null || hull.getValidVehicles().isEmpty()) {
                resetPlaceholder();
            }else{
                Class<? extends IVehicleEntity> placeHolderClass = placeholder.getVehicleEntity().getClass();
                Class<? extends IVehicleEntity> hullClass = hull.getValidVehicles().get(0).getClazz();

                if (!placeHolderClass.equals(hullClass)) {
                    resetPlaceholder();
                }
            }
        }

		if (placeholder != null) {
			placeholder.loadPlaceholderModules(getModularInfoIds());
			updateRenderMenu();
		}
        isErrorListOutdated = true;
	}
	
	
	private void updateRenderMenu() {
		ArrayList<DropDownMenuItem> list = info.getList();
		dropDownItems.clear();
		for (DropDownMenuItem item : list)  {
			if (item.getModuleClass() == null)  {
				dropDownItems.add(item);
			}else{
				for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
					if (getStackInSlot(i) != null) {
						if (ModuleDataItemHandler.isItemOfModularType(getStackInSlot(i),item.getModuleClass()) && (item.getExcludedClass() == null || !ModuleDataItemHandler.isItemOfModularType(getStackInSlot(i),item.getExcludedClass()))) {
							dropDownItems.add(item);	
							break;
						}
					}
				}	
			}
		} 
	}
	
	private int[] getModularInfoIds() {
		List<Integer> dataList = new ArrayList<Integer>();
		for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
			if (getStackInSlot(i) != null) {
				ModuleData data = ModItems.modules.getModuleData(getStackInSlot(i));
				if (data != null) {
					dataList.add(getStackInSlot(i).getItemDamage());
				}
			}
		}
		
		int[] ids = new int[dataList.size()];
		for (int i = 0; i < dataList.size(); i++) {
			ids[i] = dataList.get(i);
		}
		return ids;
	}

	public boolean getIsDisassembling() {
		for (int i = 0; i < getSizeInventory() - nonModularSlots(); i++) {
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize <= 0) {
				return true;
			}
		}	
		return false;
	}
	
	
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
    }
	
	ItemStack[] inventoryStacks;


	
	@Override
    public int getSizeInventory()
    {
		return inventoryStacks.length;
    }

	@Override
    public ItemStack getStackInSlot(int i) {
		return inventoryStacks[i];
    }	
	
	@Override
    public ItemStack decrStackSize(int i, int j) {
		if (inventoryStacks[i] != null) {
			if (inventoryStacks[i].stackSize <= j) {
				ItemStack itemstack = inventoryStacks[i];
				inventoryStacks[i] = null;
				markDirty();
				return itemstack;
			}

			ItemStack result = inventoryStacks[i].splitStack(j);

			if (inventoryStacks[i].stackSize == 0) {
				inventoryStacks[i] = null;
			}

			markDirty();
			return result;
		}else {
			return null;
		}
		
    }	
	
	@Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventoryStacks[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}

		markDirty();
    }

	@Override
    public String getInventoryName() {
        return "container.vehicle_assembler";
    }	

	@Override
    public boolean hasCustomInventoryName() {
        return false;
    }	
	
	@Override
    public int getInventoryStackLimit() {
        return 64;
    }	
	
	@Override
    public void closeInventory() {
    }
	@Override
    public void openInventory() {
    }

    @Override
	public ItemStack getStackInSlotOnClosing(int i){
		ItemStack item = getStackInSlot(i);
	
		if (item != null) {
			setInventorySlotContents(i, null);
			if (item.stackSize == 0) {
				return null;
			}
			return item;
		}else{
			return null;
		}
		
    }	
	
    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		NBTTagList items = tagCompound.getTagList("Items", NBTHelper.COMPOUND.getId());

		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getByte("Slot") & 255;

			ItemStack iStack = ItemStack.loadItemStackFromNBT(item);
			
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, iStack);
			}
		}
		
		NBTTagList spares = tagCompound.getTagList("Spares", NBTHelper.COMPOUND.getId());
		spareModules.clear();
		for (int i = 0; i < spares.tagCount(); ++i) {
			NBTTagCompound item = spares.getCompoundTagAt(i);
			ItemStack iStack = ItemStack.loadItemStackFromNBT(item);
			spareModules.add(iStack);
		}		
		
		NBTTagCompound outputTag = (NBTTagCompound)tagCompound.getTag("Output");
		if (outputTag != null) {
			outputItem = ItemStack.loadItemStackFromNBT(outputTag);
		}
		
		
		//Backwards comparability
		if (tagCompound.hasKey("Fuel")) {
			setFuelLevel(tagCompound.getShort("Fuel"));
		}else{
			setFuelLevel(tagCompound.getInteger("IntFuel"));
		}
		
		maxAssemblingTime = tagCompound.getInteger("maxTime");
		setAssemblingTime(tagCompound.getInteger("currentTime"));	
		isAssembling = tagCompound.getBoolean("isAssembling");


    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i) {
			ItemStack iStack = getStackInSlot(i);
		
			if (iStack != null) {
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte)i);
				iStack.writeToNBT(item);
				items.appendTag(item);
			}
		}
		
		tagCompound.setTag("Items", items);
		
		
		NBTTagList spares = new NBTTagList();
        for (ItemStack iStack : spareModules) {
            if (iStack != null) {
                NBTTagCompound item = new NBTTagCompound();
                //item.setByte("Slot", (byte)i);
                iStack.writeToNBT(item);
                spares.appendTag(item);
            }
        }
		
		tagCompound.setTag("Spares", spares);
		
		if (outputItem != null) {
			NBTTagCompound outputTag = new NBTTagCompound();
			outputItem.writeToNBT(outputTag);
			tagCompound.setTag("Output", outputTag);						
		}		

		tagCompound.setInteger("IntFuel", getFuelLevel());
		tagCompound.setInteger("maxTime", maxAssemblingTime);
		tagCompound.setInteger("currentTime", getAssemblingTime());
		tagCompound.setBoolean("isAssembling", isAssembling);
    }	

	public ItemStack getOutputOnInterrupt() {
        //TODO rewrite to output an interrupted vehicle
		/*if (outputItem == null) {
			return null;
		}else if (!outputItem.hasTagCompound()) {
			return null;
		}else {
			NBTTagCompound info = outputItem.getTagCompound();
			if (info == null) {
				return null;
			}else{
				info.setInteger("currentTime", getAssemblingTime());
				info.setInteger("maxTime", maxAssemblingTime);
				
				int moduleCount = info.getByteArray("Modules").length;
				
				NBTTagCompound spares = new NBTTagCompound();
				byte [] moduleIDs = new byte[spareModules.size()];
				for (int i = 0; i < spareModules.size(); i++) {
					ItemStack item = spareModules.get(i);
					ModuleData data = ModItems.modules.getModuleData(item);
					if (data != null) {		
						moduleIDs[i] = data.getID();
						ModItems.modules.addExtraDataToCart(info, item, i + moduleCount);
					}					
				}


				info.setByteArray("Spares", moduleIDs);				
								
				
				return outputItem;
			}
		}*/
		
		return null;
	}

	@Override
    public boolean isItemValidForSlot(int slotId, ItemStack item) {
        return slotId >= 0 && slotId < slots.size() && slots.get(slotId).isItemValid(item);
	}	
	

    //slots
	@Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return (side == 0 || side == 1) ? topBotSlots : sideSlots;
    }

    //in
	@Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return (side == 0 || side == 1) && this.isItemValidForSlot(slot, item);
    }

    //out
	@Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return true;
    }

	public void increaseFuel(int val) {
		fuelLevel += val;
		if (fuelLevel > getMaxFuelLevel()) {
			fuelLevel = getMaxFuelLevel();
		}
	}    

    private boolean freeMode;
    public boolean isInFreeMode() {
        return freeMode;
    }
}
