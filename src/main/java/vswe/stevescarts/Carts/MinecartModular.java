package vswe.stevescarts.Carts;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerMinecart;
import vswe.stevescarts.Helpers.ActivatorOption;
import vswe.stevescarts.Helpers.CartVersion;
import vswe.stevescarts.Helpers.CompWorkModule;
import vswe.stevescarts.Helpers.DataWatcherLockable;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.Helpers.GuiAllocationHelper;
import vswe.stevescarts.Helpers.ModuleCountPair;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleCreativeSupplies;
import vswe.stevescarts.Modules.Engines.ModuleEngine;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Modules.Workers.ModuleWorker;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;


import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The modular minecart class, this is the cart. This is what controls all modules whereas 
 * the modules({@link ModuleBase} handles specific tasks. The cart the player can see in-game 
 * is a combination of the cart and its modules.
 * @author Vswe
 */
public class MinecartModular extends EntityMinecart
    implements IInventory, IEntityAdditionalSpawnData, IFluidHandler
{

	
    public int disabledX;
    public int disabledY;
    public int disabledZ;
    protected boolean wasDisabled;
    public double pushX;
    public double pushZ;		
    public double temppushX;
    public double temppushZ;
    protected boolean engineFlag = false;
    private int motorRotation;
	public boolean cornerFlip;
	private int fixedMeta = -1;
	private int fixedMX;
	private int fixedMY;
	private int fixedMZ;
	private byte [] moduleLoadingData;
	private Ticket cartTicket;
	private int wrongRender;
	private boolean oldRender;
	private	float lastRenderYaw;
	private double lastMotionX;
	private double lastMotionZ;
	private int workingTime;
	private ModuleWorker workingComponent;	
	public TileEntityCartAssembler placeholderAsssembler;
	public boolean isPlaceholder;
	public int keepAlive;
	public static final int MODULAR_SPACE_WIDTH = 443;
	public static final int MODULAR_SPACE_HEIGHT = 168;
	protected int modularSpaceHeight;
	public boolean canScrollModules;	
	private ArrayList<ModuleCountPair> moduleCounts;
	
	/**
	 * Information about how trails turn
	 */
	public static final int[][][] railDirectionCoordinates = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

	/**
	 * All Modules that belong to this cart
	 */
	private ArrayList<ModuleBase> modules;
	
	/**
	 * All Worker Modules that belong to this cart
	 * These modules can stop the cart while they perform some work during a certain amount of time
	 */
	private ArrayList<ModuleWorker> workModules;
	

	/**
	 * All Engine Modules that belong to this cart
	 * These modules power the cart and some modules
	 */
	private ArrayList<ModuleEngine> engineModules;
	
	/**
	 * All Tank Modules that belong to this cart
	 * These module can carry fluid for the cart. The cart itself will always say that it
	 * "can" carry fluids but if no tanks are present it will just fail to drain/fill anything
	 */
	private ArrayList<ModuleTank> tankModules;

	private ModuleCreativeSupplies creativeSupplies;
	
	/**
	 * All Modules that belong to this cart
	 * @return All Modules that belong to this cart
	 */
	public ArrayList<ModuleBase> getModules() {
		return modules;
	}

	/**
	 * These modules can stop the cart while they perform some work during a certain amount of time
	 * @return All Worker Modules that belong to this cart
	 */
	public ArrayList<ModuleWorker> getWorkers() {
		return workModules;
	}		
	
	/**
	 * These modules power the cart and some modules
	 * @return All Engine Modules that belong to this cart
	 */
	public ArrayList<ModuleEngine> getEngines() {
		return engineModules;
	}
	
	/**
	 * These module can carry fluid for the cart. The cart itself will always say that it
	 * "can" carry fluids but if no tanks are present it will just fail to drain/fill anything
	 * @return All Tank Modules that belong to this cart
	 */
	public ArrayList<ModuleTank> getTanks() {
		return tankModules;
	}

	
	public Random rand = new Random();
	
	/**
	 * The name the cart has if renamed /by an anvil)
	 */
	protected String name;
	
	/**
	 * The version this cart has, for more info about cersion see {@link CartVersion}
	 */
	public byte cartVersion;
	
	public ArrayList<ModuleCountPair> getModuleCounts() {
		return moduleCounts;
	}
	
	
	/**
	 * Creates a cart in the world, this is used when the cart is spawned by a player(or something else)
	 * on the server side.
	 * @param world The world the cart is placed in
	 * @param x The X coordinate the cart is placed at
	 * @param y The Y coordinate the cart is placed at
	 * @param z The Z coordinate the cart is placed at
	 * @param info The tag compound with mostly the cart's modules.
	 * @param name The name of this cart
	 */
    public MinecartModular(World world, double x, double y, double z, NBTTagCompound info, String name)
    {
        super(world, x, y, z);
		overrideDatawatcher();
		
		cartVersion = info.getByte("CartVersion");
		
		
		loadModules(info);
		this.name = name;

		for (int i = 0; i < modules.size(); i++) {
			if (modules.get(i).hasExtraData() && info.hasKey("Data" + i)) {
				modules.get(i).setExtraData(info.getByte("Data" + i));
			}
		}
    }
	
	
    /**
     * Creates a cart in the world. This is used when a cart is loaded on the server, or when a cart is created on the client.
     * @param world The world the cart is created in
     */
    public MinecartModular(World world)
    {
        super(world);
		overrideDatawatcher();
    }
	
	/**
	 * Creates a PlaceHolder cart, 
	 * @param world The world this cart is created in
	 * @param assembler The CartAssembler this placeholder cart belongs to
	 * @param data The byte array containing the modules of this cart
	 */
    public MinecartModular(World world, TileEntityCartAssembler assembler, byte[] data)
    {
        this(world);
		
		setPlaceholder(assembler);
		
		loadPlaceholderModules(data);
    }	
	
    /**
     * The normal datawatcher is overridden by a special one on the client side. This is to be able
     * to wait to process data in the beginning. Fixing the syncing at start up.
     */
	private void overrideDatawatcher() {
		if (worldObj.isRemote) {
			this.dataWatcher = new DataWatcherLockable(this);

			this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
			this.dataWatcher.addObject(1, Short.valueOf((short)300));
			
			this.entityInit();
		}
	}


    public String getEntityIdDifference() {
        return "This: " + getEntityId() + " Super: " + super.getEntityId();
    }

    /**
	 * Load a placeholder's modules, this is a bit special since it can be done on existing carts.
	 * Therefore new modules should be loaded, old modules that still are there be ignored and
	 * old modules that are no longer present be removed.
	 * @param data The byte array representing the modules.
	 */
	private void loadPlaceholderModules(byte[] data) {
		if (modules == null) {
			modules = new ArrayList<ModuleBase>();
			doLoadModules(data);
		}else{	

			//Rule 1 -> IN OLD, NOT NEW -> remove module
			//Rule 2 -> IN NEW, NOT OLD -> add module
			//Rule 3 -> IN OLD, IN NEW -> keep the module, do nothing

			ArrayList<Byte> modulesToAdd = new ArrayList<Byte>();
			ArrayList<Byte> oldModules = new ArrayList<Byte>(); 
			for (int i = 0; i < moduleLoadingData.length; i++) {
				oldModules.add(moduleLoadingData[i]);
			}

			
			for (int i = 0; i < data.length; i++) {
				boolean found = false;
				for (int j = 0; j < oldModules.size(); j++) {
					if (data[i] == oldModules.get(j)) {
						//Rule 3
						found = true;
						oldModules.remove(j);
						break;
					}
				}
				if (!found) {
					//Rule 2
					modulesToAdd.add(data[i]);
				}
			}
			
			for (byte id : oldModules) {
				for (int i = 0; i < modules.size(); i++) {
					if (id == modules.get(i).getModuleId()) {
						//Rule 1
						modules.remove(i);
						break;
					}
				}
			}
			

			

			byte[] newModuleData = new byte[modulesToAdd.size()];
			for (int i = 0; i < modulesToAdd.size(); i++) {
				newModuleData[i] = modulesToAdd.get(i);
			}

			doLoadModules(newModuleData);
		}
		
		initModules();
		moduleLoadingData = data;
	}
	

	/**
	 * Load the cart modules from the tag compound from the Cart Item
	 * @param info The tag compound
	 */
	private void loadModules(NBTTagCompound info) {
		NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
		if (moduleIDTag == null) {
			return;
		}
		
		//on the server, make sure the version is correct
		if (worldObj.isRemote) {
			moduleLoadingData = moduleIDTag.func_150292_c();
		}else{
			moduleLoadingData = CartVersion.updateCart(this, moduleIDTag.func_150292_c());
		}
		
		loadModules(moduleLoadingData);	
	}
	
	/**
	 * Updates the PlaceHolder cart by giving it the current setup of modules.
	 * @param bytes The byte array representing the modules.
	 */
	public void updateSimulationModules(byte[] bytes) {
		if (!isPlaceholder) {
			System.out.println("You're stupid! This is not a placeholder cart.");
		}else {
			loadPlaceholderModules(bytes);
		}
	}
	
	/**
	 * Create and initiate the cart with the given modules.
	 * @param bytes The byte array representing the modules.
	 */
	protected void loadModules(byte[] bytes) {

		modules = new ArrayList<ModuleBase>();
	
		doLoadModules(bytes);

		initModules();
	}
	
	/**
	 * Create the given modules
	 * @param bytes The byte array representing the modules.
	 */
	private void doLoadModules(byte[] bytes) {
	
		for (byte id : bytes) {
			
		    try {
				Class<? extends ModuleBase> moduleClass = ModuleData.getList().get((byte)id).getModuleClass();
				
				Constructor moduleConstructor = moduleClass.getConstructor(new Class[] {MinecartModular.class});

				Object moduleObject = moduleConstructor.newInstance(new Object[] {this});

				ModuleBase module = (ModuleBase)moduleObject;
				module.setModuleId(id);
				modules.add(module);		   
			}catch(Exception e) {
				System.out.println("Failed to load module with ID " + id + "! More info below.");
			
				e.printStackTrace();
			}			
			
		}		
	}
	


	/**
	 * Initiate the modules on the cart. 
	 * This will allocate all required IDs, place them on the interface
	 * and initiate every module.
	 */
	private void initModules() {
		moduleCounts = new ArrayList<ModuleCountPair>();
		for (ModuleBase module : modules) {
			ModuleData data = ModuleData.getList().get(module.getModuleId()); 
			
			boolean found = false;
			for (ModuleCountPair count : moduleCounts) {
				if (count.isContainingData(data)) {
					count.increase();
					found = true;
					break;
				}
			}
			if (!found) {
				moduleCounts.add(new ModuleCountPair(data));
			}
		}		
		
		
		//pre-initialize the modules
		for (ModuleBase module : modules) {
			module.preInit();
		}		
		
		workModules = new ArrayList<ModuleWorker>();
		engineModules = new ArrayList<ModuleEngine>();
		tankModules = new ArrayList<ModuleTank>();
		
		int x = 0;
		int y = 0;
		int maxH = 0;
		int guidata = 0;
		int datawatcher = 0;
		int packets = 0;
		
		//generate all the models this cart should use
		if (worldObj.isRemote) {
			generateModels();
		}


		for (ModuleBase module : modules) {
			if (module instanceof ModuleWorker) {
				workModules.add((ModuleWorker)module);
			}else if (module instanceof ModuleEngine) {
				engineModules.add((ModuleEngine)module);
			}else if(module instanceof ModuleTank) {
				tankModules.add((ModuleTank)module);
			}else if(module instanceof ModuleCreativeSupplies) {
				creativeSupplies = (ModuleCreativeSupplies)module;
			}
		}
		
				
		CompWorkModule sorter = new CompWorkModule();
		Collections.sort(workModules, sorter);
		
		//gives all their modules a place to render their graphics on
		if (!isPlaceholder) {
			ArrayList<GuiAllocationHelper> lines = new ArrayList<GuiAllocationHelper>();
			int slots = 0;
			for (ModuleBase module : modules) {
				//only for modules that actually have an interface
				if (module.hasGui()) {
					boolean foundLine = false;
					//check if there's room in an already existing line, if so, place it there
					for (GuiAllocationHelper line : lines) {
						if (line.width + module.guiWidth() <= MODULAR_SPACE_WIDTH) {
							module.setX(line.width);
							line.width += module.guiWidth();
							line.maxHeight = Math.max(line.maxHeight, module.guiHeight());
							line.modules.add(module);
							foundLine = true;
							break;
						}
					}
					
					//if there wasn't any room for the module, create a new line for it
					if (!foundLine) {
						GuiAllocationHelper line = new GuiAllocationHelper();
						module.setX(0);
						line.width =  module.guiWidth();
						line.maxHeight = module.guiHeight();
						line.modules.add(module);
						lines.add(line);
					}
			
					//initiate the gui data IDs
					module.setGuiDataStart(guidata);
					guidata += module.numberOfGuiData();	
					
					//initiate the slots
					if (module.hasSlots()) {
						slots = module.generateSlots(slots);
					}					
					
				}
				
				//initiate the data watchers and give the modules the correct IDs
				module.setDataWatcherStart(datawatcher);
				datawatcher += module.numberOfDataWatchers();
				if (module.numberOfDataWatchers() > 0) {
					module.initDw();		
				}
				
				//allocate some packet IDs to the module if required
				module.setPacketStart(packets);
				packets += module.totalNumberOfPackets();					
			}
			
			
			
			//when the interface has been generated, calculate if scrolling is required and how that is done
			int currentY = 0;
			for (GuiAllocationHelper line : lines) {
				for (ModuleBase module : line.modules) {
					module.setY(currentY);
				}
				currentY += line.maxHeight;
			}			
			
			if (currentY > MODULAR_SPACE_HEIGHT) {
				canScrollModules = true;
			}
			modularSpaceHeight = currentY;
		}
		
		//initialize the modules
		for (ModuleBase module : modules) {
			module.init();
		}

	}


    /**
     * Is called when the Entity is killed
     */
    public void setDead()
    {
    	//removes all the items on the client side, this is so the client won't drop ghost items
		if (worldObj.isRemote) {
			for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
			{
				setInventorySlotContents(var1, null);
			}
		}
	
        super.setDead();

        //tell all the modules that the cart is being removed
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.onDeath();
			}
		}
		
		//stop loading chunks
		dropChunkLoading();
    }	
	
	/**
	 * Allows the modules to render overlays on the screen
	 * @param minecraft
	 */
	@SideOnly(Side.CLIENT)
	public void renderOverlay(Minecraft minecraft) {
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.renderOverlay(minecraft);
			}
		}		
	}
	
	/**
	 * Initiates the entity, used for initiating data watchers.
	 */
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }	
	
	/**
	 * Handles the fuel usage
	 */
    public void updateFuel()
    {

        //check how much power the cart needs the next tick
		int consumption = getConsumption();
		
		//if the cart needs power we need to consume it
        if (consumption > 0)
        {
        	
        	//get a engine to draib power from, if any
			ModuleEngine engine = getCurrentEngine();
			if (engine != null) {
				//consume
				engine.consumeFuel(consumption);		

			
				//let the engine emit smoke
				if (!isPlaceholder && worldObj.isRemote && hasFuel() && !isDisabled()) {
					engine.smoke();		
				}				
			}
        }
		
        //if a cart is not moving but has fuel for it, start it
		if (hasFuel()) {
			if (!engineFlag) {
				pushX = temppushX;
				pushZ = temppushZ;
			}
			
		//if the cart doesn't have fuel but is moving, stop it
		}else if(engineFlag){
			temppushX = pushX;
			temppushZ = pushZ;
			pushX = pushZ = 0.0D;
		}
		

		//set the current state of the engine
        setEngineBurning(hasFuel() && !isDisabled());		
    }
    
	/**
	 * Get the engine's state, if it's on or off.
	 * This should not be used to determine if a module
	 * that requires power should run or not.
	 * @return
	 */
	public boolean isEngineBurning() {
		return isCartFlag(0);
	}
	
	/**
	 * Set the engine's state, if it's on or off.
	 * @param on The state of the engine
	 */
    public void setEngineBurning(boolean on)
    {
		setCartFlag(0, on);
    }	
	
    /**
     * Returns one of up to 8 flags about the cart that is synchronized between the client and the server
     * @param id The Flag's id
     * @return If the flag is set or not
     */
	private boolean isCartFlag(int id) {
		return (dataWatcher.getWatchableObjectByte(16) & (1 << id)) != 0;
	}
	
	/**
	 * Sets one of up to 8 flags about the cart that is synchronized between the client and the server
	 * @param id The Flag's id
	 * @param val The new valie pf the flag
	 */
	private void setCartFlag(int id, boolean val) {
		if (worldObj.isRemote) {
			return;
		}
	
		byte data = (byte)((dataWatcher.getWatchableObjectByte(16) & ~(1 << id)) | ((val ? 1 : 0) << id));
	
		dataWatcher.updateObject(16, data);
	}
	
	
	/**
	 * Get the engine that should be used for this tick
	 * @return The engine, or null if no valid one were found
	 */
	private ModuleEngine getCurrentEngine() {
		if (modules == null) {
			return null;
		}
		
		//force stop it all?
		for (ModuleBase module : modules) {
			if (module.stopEngines()) {
				return null;
			}
		}
	
		//get the consumption when the cart is moving.
		int consumption = getConsumption(true);
	
		//get a list of all the working engines with the highest available priority
		ArrayList<ModuleEngine> priority = new ArrayList<ModuleEngine>();
		int mostImportant = -1; 
		for (ModuleEngine engine : engineModules) {
			if (engine.hasFuel(consumption) && (mostImportant == -1 || mostImportant >= engine.getPriority())) {
				if (engine.getPriority() < mostImportant) {
					priority.clear();
				}
				mostImportant = engine.getPriority();
				priority.add(engine);
			}			
		}
		
		//if there are valid engines, use one of them. If there's more than one, use different ones on different ticks.
		if (priority.size() > 0) {
			if (motorRotation >= priority.size()) {
				motorRotation = 0;
			}
			motorRotation = (motorRotation +1) % priority.size();
			return priority.get(motorRotation);
		}
		return null;
	}
	
	/**
	 * Get the current consumption value
	 * @return The consumption for this tick
	 */
	public int getConsumption() {
		return getConsumption(!isDisabled() && isEngineBurning());
	}	
	
	/**
	 * Get the "current" consumption value. The value is calculated depending on if the cart is assumed to be moving or not
	 * @param isMoving If the cart is powered to move
	 * @return  The consumption for this tick
	 */
	public int getConsumption(boolean isMoving) {
		//one is the base when moving
		int consumption = isMoving ? 1 : 0;
		
		//loop through all the modules and sum up their consumption
		if (modules != null && !isPlaceholder) {
			for (ModuleBase module : modules) {
				consumption += module.getConsumption(isMoving);
			}
		}
		
		return consumption;
	}

	
	/**
	 * Get the "Eye height" of the cart
	 */
	@Override
	public float getEyeHeight()
    {
        return 0.9F;
    }	
	
	/**
	 * Get the offset the riding entity should be rendered at
	 */
	@Override
    public double getMountedYOffset()
    {
		if (modules != null && riddenByEntity != null) {
			for (ModuleBase module : modules) {
				float offset = module.mountedOffset(riddenByEntity);
				if (offset != 0) {
					return offset;
				}
			}
		}	
		return super.getMountedYOffset();
    }	
	
	/**
	 * Get this cart as an item. Used when breaking the cart or "picking"(middle mouse button) it for instance.
	 */
	@Override
    public ItemStack getCartItem()
    {
		if (modules != null) {
			ItemStack cart = ModuleData.createModularCart(this);	
			if (name != null && !name.equals("") && !name.equals(ModItems.carts.getName())) {
				cart.setStackDisplayName(name);
			}

			return cart;
		}else{
			return new ItemStack(ModItems.carts);
		}
    }

	/**
	 * When the cart 
	 */
	@Override
	public void killMinecart(DamageSource dmg) {
		this.setDead();
		if (dropOnDeath()) {
			this.entityDropItem(getCartItem(), 0.0F);
			
		   for (int i = 0; i < this.getSizeInventory(); ++i)
			{
				ItemStack itemstack = this.getStackInSlot(i);

				if (itemstack != null)
				{
					float f = this.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0)
					{
						int j = this.rand.nextInt(21) + 10;

						if (j > itemstack.stackSize)
						{
							j = itemstack.stackSize;
						}

						itemstack.stackSize -= j;
						EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double)f, this.posY + (double)f1, this.posZ + (double)f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
						entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
						this.worldObj.spawnEntityInWorld(entityitem);
					}
				}
			}			
		}
	}
	
	/**
	 * Whether a cart should drop its items when killed
	 * @return
	 */
	public boolean dropOnDeath() {
		if (isPlaceholder) {
			return false;
		}
	
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (!module.dropOnDeath()) {
					return false;
				}
			}
		}	
			
		return true;
	}
	



	/**
	 * Get the max speed this cart can move at
	 */
	@Override
    public float getMaxCartSpeedOnRail()
    {
		//the calculated maximum speed
		float maxSpeed = super.getMaxCartSpeedOnRail(); 
		if (modules != null) {
			for (ModuleBase module : modules) {
				float tempMax = module.getMaxSpeed();
				if (tempMax < maxSpeed) {
					maxSpeed = tempMax;
				}
			}	
		}		
        return maxSpeed;
    }

    /**
     * Returns if this cart can be powered
     */
	@Override
    public boolean isPoweredCart()
    {
        return engineModules.size() > 0;
    }
	
	@Override
	public int getDefaultDisplayTileData() {
		return -1;
	}

	//TODO this method doesn't exist anymore, remove or find another name for it
	/*@Override
    public Block getDefaultDisplayTile()
    {
        return null;
    }*/

	@Override
	public int getMinecartType() {
		return -1;
	}
		
	
	/**
	 * Return the color filter that should be applied to this cart
	 * @return The color [R: 0-1, G: 0-1, B: 0-1]
	 */
	public float[] getColor() {
		if (modules != null) {
			for (ModuleBase module : getModules()) {
				float[] color = module.getColor();
				if (color[0] != 1F || color[1] != 1F || color[2] != 1F) {
					return color;
				}
			}
		}
		
		return new float[] {1F,1F,1F};
	}	
	
	
	/**
	 * Return the Y level in the world the cart is aiming for
	 * @return The target level
	 */
	public int getYTarget() {
		if (modules != null) {
			for (ModuleBase module : getModules()) {
				int yTarget = module.getYTarget();
				if (yTarget != -1) {
					return yTarget;
				}
			}
		}
		
		return (int)posY;
	}
	
	/**
	 * Returns a module that want to use the whole interface for itself, this prevents all other modules to be able to acces the interface.
	 * @return The module that wants to steal the interface, or null if no module wants to.
	 */
	public ModuleBase getInterfaceThief() {
		if (modules != null) {
			for (ModuleBase module : getModules()) {
				if (module.doStealInterface()) {
					return module;
				}
			}
		}
		
		return null;		
	}
	
	
	/**
	 * When the cart takes damage
	 */
	@Override
	public boolean attackEntityFrom(DamageSource dmg, float par2)
    {
		if (isPlaceholder) {
			return false;
		}
	
		if (modules != null) {
			for (ModuleBase module : getModules()) {
				if (!module.receiveDamage(dmg, par2)) {
					return false;
				}
			}
		}
	
		return super.attackEntityFrom(dmg,par2);		
	}
	
	/**
	 * When the cart passes a activator rail
	 * @param x The X coordinate of the rail
	 * @param y The Y coordinate of the rail
	 * @param z The Z coordinate of the rail
	 * @param active If the rail is active or not
	 */
	@Override
	public void onActivatorRailPass(int x, int y, int z, boolean active) {
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.activatedByRail(x,y,z, active);
			}
		}		
	}
	
	

	@Override
	/**
	 * Called when the cart is moving on top of a rail
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param z The Z coordinate
	 * @param acceleration seems like the acceleration
	 */
    public void moveMinecartOnRail(int x, int y, int z, double acceleration/*?*/)
    {
        super.moveMinecartOnRail(x, y, z, acceleration);
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.moveMinecartOnRail(x,y,z);
			}
		}
		
        Block block = worldObj.getBlock(x, y, z);
		Block blockBelow = worldObj.getBlock(x, y - 1, z);
		int metaBelow = worldObj.getBlockMetadata(x, y-1, z);
		int m = ((BlockRailBase)block).getBasicRailMetadata(worldObj, this, x, y, z);
		
	
		if (((m == 6 || m == 7) && motionX < 0) || ((m == 8 || m == 9) && motionX > 0)) {
			cornerFlip = true;
		}else{
			cornerFlip = false;
		}
		
       if (block != ModBlocks.ADVANCED_DETECTOR.getBlock() && isDisabled())
        {
            releaseCart();
        }

        boolean canBeDisabled = block == ModBlocks.ADVANCED_DETECTOR.getBlock() && (blockBelow != ModBlocks.DETECTOR_UNIT.getBlock() || !DetectorType.getTypeFromMeta(metaBelow).canInteractWithCart() || DetectorType.getTypeFromMeta(metaBelow).shouldStopCart());
        boolean forceUnDisable = (wasDisabled && disabledX == x && disabledY == y && disabledZ == z);

        if (!forceUnDisable && wasDisabled)
        {
            wasDisabled = false;
        }

        canBeDisabled = forceUnDisable ? false : canBeDisabled;

        if (canBeDisabled && !isDisabled())
        {
            setIsDisabled(true);

            if (pushX != 0 || pushZ != 0)
            {
                temppushX = pushX;
                temppushZ = pushZ;
                pushX = pushZ = 0;
            }

            disabledX = x;
            disabledY = y;
            disabledZ = z;
        }
		
		
		if (fixedMX != x || fixedMY != y || fixedMZ != z)
        {
            fixedMeta = -1;
			fixedMY = -1;
        }
    }

	/**
	 * Allows the cart to override the rail's meta data when traveling
	 * @param x The X coordinate of the rail
	 * @param y The Y coordinate of the rail
	 * @param z The Z coordinate of the rail
	 * @return The meta data of the rail
	 */
	public int getRailMeta(int x, int y, int z) {		
		ModuleBase.RAILDIRECTION dir = ModuleBase.RAILDIRECTION.DEFAULT;
		for (ModuleBase module : getModules()) {
			dir = module.getSpecialRailDirection(x,y,z);
			if (dir != ModuleBase.RAILDIRECTION.DEFAULT) {
				break;
			}
		}
	
		if (dir == ModuleBase.RAILDIRECTION.DEFAULT)  {
			return -1;
		}
	
		int Yaw = (int)(rotationYaw % 180);

        if (Yaw < 0)
        {
            Yaw += 180 ;
        }

        boolean flag = Yaw >= 45 && Yaw <= 135;
		
        if (fixedMeta == -1)
        {		
			switch (dir) {
				case FORWARD: 
					if (flag)
					{
						fixedMeta = 0;
					}
					else
					{
						fixedMeta = 1;
					}				
					break;
				case LEFT:
					if (flag)
					{
						if (motionZ > 0)
						{
							fixedMeta = 9;
						}
						else if (motionZ <= 0)
						{
							fixedMeta = 7;
						}
					}
					else
					{
						if (motionX > 0)
						{
							fixedMeta = 8;
						}
						else if (motionX < 0)
						{
							fixedMeta = 6;
						}
					}	
					break;
				case RIGHT:					
					if (flag)
					{
						if (motionZ > 0)
						{
							fixedMeta = 8;
						}
						else if (motionZ <= 0)
						{
							fixedMeta = 6;
						}
					}
					else
					{
						if (motionX > 0)
						{
							fixedMeta = 7;
						}
						else if (motionX < 0)
						{
							fixedMeta = 9;
						}
					}
					break;
					
					//doesn't work
				case NORTH: 
					if (flag)
					{
						if (motionZ > 0)
						{
							fixedMeta = 0;
						}
						
					}else {
						if (motionX > 0)
						{
							fixedMeta = 7;
						}else if (motionX < 0) {
							fixedMeta = 6;
						}
					}
					
					break;
				default:
					fixedMeta = -1;
			}

            if (fixedMeta == -1)
            {
                return -1;
            }

            fixedMX = x;
            fixedMY = y;
            fixedMZ = z;
        }

        return fixedMeta;			
	}
	
	/**
	 * Reset the modified meta data
	 */
	public void resetRailDirection() {
		fixedMeta = -1;
	}

    /**
    Turn the cart around
    **/
    public void turnback()
    {
        pushX *= -1;
        pushZ *= -1;
        temppushX *= -1;
        temppushZ *= -1;
        motionX *= -1;
        motionY *= -1;
        motionZ *= -1;
    }

    /**
     * Allows the cart to move again after being disabled
     */
    public void releaseCart()
    {
        wasDisabled = true;
        setIsDisabled(false);
        pushX = temppushX;
        pushZ = temppushZ;
    }

	

    /**
     * Lets the modules know when the inventory of the cart has been changed
     */
	@Override
	public void markDirty() { //TODO make sure this method name is correct
		if (modules != null) {
			for (ModuleBase module : modules) {
				module.onInventoryChanged();
			}
		}
	}




    /**
     Returns the number of slots in this cart
    **/
	@Override
    public int getSizeInventory()
    {
		int slotCount = 0;
		if (modules != null) {
			for (ModuleBase module : modules) {
				slotCount += module.getInventorySize();
			}
		}else{
			//slotCount = 100;
		}
	
        return slotCount;
    }

	
	


	@Override
    protected void func_145821_a(int par1, int par2, int par3, double par4, double par6, Block par8, int par9)
    {
        super.func_145821_a(par1, par2, par3, par4, par6, par8, par9);
        double d2 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d2 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D)
        {
            d2 = (double)MathHelper.sqrt_double(d2);
            this.pushX /= d2;
            this.pushZ /= d2;

            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D)
            {
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            }
            else
            {
                this.pushX = this.motionX;
                this.pushZ = this.motionZ;
            }
        }
    }

	@Override
    protected void applyDrag()
    {
	
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
		engineFlag = d0 > 1.0E-4D;
		if (isDisabled()) {
			motionX = 0;
			motionY = 0;
			motionZ = 0;
        }else if (engineFlag) {
            d0 = (double)MathHelper.sqrt_double(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = getPushFactor();
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            this.motionX += this.pushX * d1;
            this.motionZ += this.pushZ * d1;
        }else{
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

	/**
	 * Get the factor this cart will push itself with when powered
	 * @return
	 */
    protected double getPushFactor()
    {
		if (modules != null) {
			for (ModuleBase module : modules) {
				double factor = module.getPushFactor();
				if (factor >= 0) {
					return factor;
				}							
			}
		}
	
        return 0.05D;
    }
	
    /**
     * Saves data of the cart, also allows all modules to save their data
     */
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
    {
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("cartName", name);

        tagCompound.setDouble("pushX", pushX);
        tagCompound.setDouble("pushZ", pushZ);		
		tagCompound.setDouble("temppushX", temppushX);
        tagCompound.setDouble("temppushZ", temppushZ);
		
		tagCompound.setShort("workingTime", (short)workingTime);	
			
		tagCompound.setByteArray("Modules", moduleLoadingData);
		
		tagCompound.setByte("CartVersion", cartVersion);
		
		if (modules != null) {
			for (int i = 0; i < modules.size(); i++) {
				ModuleBase module = modules.get(i);
				module.writeToNBT(tagCompound,i);
			}
		}
	}
	
	
	/**
	 * Loads the data of the cart, also allows the modules to load their data
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
    {
		super.readFromNBT(tagCompound);

        name = tagCompound.getString("cartName");
		
        pushX = tagCompound.getDouble("pushX");
        pushZ = tagCompound.getDouble("pushZ");	
        temppushX = tagCompound.getDouble("temppushX");
        temppushZ = tagCompound.getDouble("temppushZ");	
		
		workingTime = tagCompound.getShort("workingTime");	

		cartVersion = tagCompound.getByte("CartVersion");
		
		int oldVersion = cartVersion;
		

		
		loadModules(tagCompound);
			
		if (modules != null) {
			for (int i = 0; i < modules.size(); i++) {
				ModuleBase module = modules.get(i);
				module.readFromNBT(tagCompound, i);
			
			}
		}	
		
		
		
		if (oldVersion < 2) {
			int newSlot = -1;
			int slotCount = 0;
			for (ModuleBase module : modules) {
				if (module instanceof ModuleTool) {
					newSlot = slotCount;
					break;
				}else{
					slotCount += module.getInventorySize();
				}
			}
			if (newSlot != -1) {
				ItemStack lastitem = null;
				for (int i = newSlot; i < getSizeInventory(); i++) {
					ItemStack thisitem = getStackInSlot(i);
					setInventorySlotContents(i, lastitem);
					lastitem = thisitem;
				}
			}
		}		
		
	}
	

	/**
	 * Gets if a cart has been disabled by an ADR
	 * @return If it's disabled
	 */
	public boolean isDisabled() {
		return isCartFlag(1);
	}
	
	/**
	 * Sets if a cart has been disabled by an ADR
	 * @param disabled If it's disabled
	 */
	public void setIsDisabled(boolean disabled) {
		setCartFlag(1, disabled);
	}
	
	


	@Override
    public void onUpdate()
    {
		super.onUpdate();

		
		onCartUpdate();
		
	}	

	/**
	 * Updates the cart logic
	 */
	public void onCartUpdate() {	
		if (modules != null) {
			updateFuel();
		
			for (ModuleBase module : modules) {
				module.update();
			}
			for (ModuleBase module : modules) {
				module.postUpdate();
			}
		
			work();	
			setCurrentCartSpeedCapOnRail(getMaxCartSpeedOnRail());
		}
		
		if (isPlaceholder) {
			if (keepAlive++ > 20) {
				kill();
				placeholderAsssembler.resetPlaceholder();
			}
		}
    }


	/**
	 * Return if this cart has enough fuel to work
	 * @return If it has enough fuel
	 */
    public boolean hasFuel()
    {
		if(isDisabled()) {
			return false;
		}
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (module.stopEngines()) {
					return false;
				}
			}
		}
	
		return hasFuelForModule();
    }
	
    
    /**
     * Return if this cart has enough fuel to work, doesn't care if the cart itself is not allowed to move
     * @return If it has enough fuel
     */
	public boolean hasFuelForModule(){
		if (isPlaceholder) {
			return true;
		}
	
		int consumption = getConsumption(true);
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (module.hasFuel(consumption)) {
					return true;
				}
			}
		}
		
		
        return false;	
	}

	/**
	 * Returns if this inventory(the cart) is usuable by the specific player
	 */
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
       return entityplayer.getDistanceSq(x(), y(), z()) <= 64D;
    }	
	
    /**
     A method to be called when this cart is activated by the player
    **/
	@Override
    public boolean interactFirst(EntityPlayer entityplayer)
    {
		if (isPlaceholder) {
			return false;
		}
		

		if (modules != null && !entityplayer.isSneaking()) {
			boolean interupt = false;
			for (ModuleBase module : modules) {
				if (module.onInteractFirst(entityplayer)) {
					interupt = true;
				}
			}
			if (interupt) {
				return true;
			}
		}
		
		if (!worldObj.isRemote) {
	        //Saves which direction the player activated the cart from
	        if (!isDisabled() && riddenByEntity != entityplayer)
	        {
	            temppushX = posX - entityplayer.posX;
	            temppushZ = posZ - entityplayer.posZ;
	        }
	
	        //if the cart can move, start it in the desired direction.
	        if (!isDisabled() && hasFuel() && pushX == 0 && pushZ == 0)
	        {
	            pushX = temppushX;
	            pushZ = temppushZ;
	        }
	
	        
	        FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 0, worldObj, getEntityId(), 0, 0);
			openInventory();
		}
		
        return true;
    }

	

	
	/**
	 * Load chunks with the current ticket at the current position
	 */
	public void loadChunks() {
		loadChunks(cartTicket, x() >> 4, z() >> 4);
	}	
	/**
	 * Loads chunks with the current ticket at the given position
	 * @param chunkX The chunk's X coordinate
	 * @param chunkZ The chunk's Z coordinate
	 */
	public void loadChunks(int chunkX, int chunkZ) {
		loadChunks(cartTicket, chunkX, chunkZ);
	}	
	/**
	 * Load chunks with the given ticket at the current position
	 * @param ticket The ticket to load with
	 */
	public void loadChunks(Ticket ticket) {
		loadChunks(ticket, x() >> 4, z() >> 4);
	}
	/**
	 * Load chunks with the given ticket at the given position
	 * @param ticket The ticket to load with
	 * @param chunkX The chunk's X coordinate
	 * @param chunkZ The chunk's Z coordinate
	 */
	public void loadChunks(Ticket ticket, int chunkX, int chunkZ) {
		if (worldObj.isRemote || ticket == null) {
			return;
		}else if (cartTicket == null) {
			cartTicket = ticket;
		}
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(chunkX+i, chunkZ+j));			
			}		
		}
		
	}
	
	/**
	 * Starts loading chunks
	 */
	public void initChunkLoading() {
		if (worldObj.isRemote || cartTicket != null) {
			return;
		}
	
		cartTicket = ForgeChunkManager.requestTicket(StevesCarts.instance, worldObj, ForgeChunkManager.Type.ENTITY);
		if (cartTicket != null) {
		
			cartTicket.bindEntity(this);
			cartTicket.setChunkListDepth(9);
			loadChunks();
		}
	}
	/**
	 * Stops loading chunks
	 */
	public void dropChunkLoading() {
		if (worldObj.isRemote) {
			return;
		}	
	
		if (cartTicket != null) {
			ForgeChunkManager.releaseTicket(cartTicket);
			cartTicket = null;
		}
	}

	
	/**
	 * Sets the current worker module to be working for the cart
	 * @param worker The new worker module or null
	 */
	public void setWorker(ModuleWorker worker) {
		if (workingComponent != null && worker != null) {
			workingComponent.stopWorking();
		}
		workingComponent = worker;
		if (worker == null) {
			setWorkingTime(0);
		}
	}
	/**
	 * Gets the current worker module that is working for the cart
	 * @return The Worker module or null
	 */
	public ModuleWorker getWorker() {
		return workingComponent;
	}
	/**
	 * Set the time left the current worker has before its done with its current task
	 * @param val The time in ticks
	 */
	public void setWorkingTime(int val) {
		workingTime = val;
	}
	
	/**
	 * Allows the current worker to work or allows the cart to assign a new worker(depending on priority)
	 */
	private void work() {
		if (isPlaceholder) {
			return;
		}
	
		//if this cart has fuel it is allowed to work
        if (!worldObj.isRemote && hasFuel())
        {
            //if the work cool down is at zero it's time to work
            if (workingTime <= 0)
            {
				ModuleWorker oldComponent = workingComponent;
				if (workingComponent != null) {
					
					boolean result = workingComponent.work();
					if (workingComponent != null && oldComponent == workingComponent && workingTime <= 0 && !workingComponent.preventAutoShutdown()) {
						workingComponent.stopWorking();
					}
					
					if (result) {
						work();
						return;
					}					
				}
				
				if (workModules != null) {
					for (ModuleWorker module : workModules) {
						if (module.work()) {
							return;
						}
					}
				}
            }
            else
            {
                //otherwise decrease the cool down
                workingTime--;
            }
        }			
	}
	

	/**
	 * Handles a activator setting from the Module Toggler
	 * @param option The option to handle
	 * @param isOrange Whether the cart is moving the orange direction or not
	 */
	public void handleActivator(ActivatorOption option, boolean isOrange) {
		for (ModuleBase module : modules) {
			if (module instanceof IActivatorModule && option.getModule().isAssignableFrom(module.getClass())) {
				IActivatorModule iactivator = (IActivatorModule)module;
				if (option.shouldActivate(isOrange)) {
					iactivator.doActivate(option.getId());
				}else if(option.shouldDeactivate(isOrange)) {
					iactivator.doDeActivate(option.getId());				
				}else if(option.shouldToggle()) {
					if (iactivator.isActive(option.getId())) {
						iactivator.doDeActivate(option.getId());				
					}else{
						iactivator.doActivate(option.getId());
					}
				}
				
			}		
		}
	}	
	
	public boolean getRenderFlippedYaw(float yaw) {		
		yaw = yaw % 360;
		if (yaw < 0) {
			yaw += 360;
		}
	
	
		if (!oldRender || Math.abs(yaw - lastRenderYaw) < 90 || Math.abs(yaw - lastRenderYaw) > 270 || (motionX > 0 && lastMotionX < 0) || (motionZ > 0 && lastMotionZ < 0) || (motionX < 0 && lastMotionX > 0) || (motionZ < 0 && lastMotionZ > 0) || wrongRender >= 50) {
			lastMotionX = motionX;
			lastMotionZ = motionZ;
			lastRenderYaw = yaw;
			oldRender = true;
			wrongRender = 0;
			return false;
		}else{
			wrongRender++;
			return true;		
		}

	}
	

	/**
	 * Get the lines to render on top of the cart
	 * @return The lines to render
	 */
	public ArrayList<String> getLabel() {
		ArrayList<String> label = new ArrayList<String>();
		
		if (getModules() != null) {
			for (ModuleBase module : getModules()) {
				module.addToLabel(label);
			}
			
		}
		
		return label;
	}
	
	/**
	 * The x coordinate of the cart
	 * @return The x coordinate
	 */
    public int x()
    {
        return (int)MathHelper.floor_double(posX);
    }
    /**
     * The y coordinate of the cart
     * @return The y coordinate
     */
    public int y()
    {
        return (int)MathHelper.floor_double(posY);
    }
    /**
     * The z coordinate of the cart
     * @return The y coordinate
     */
    public int z()
    {
        return (int)MathHelper.floor_double(posZ);
    }



    /**
     * Add an item to the cart's inventory
     * @param iStack The item to put in the cart
     */
    public void addItemToChest(ItemStack iStack)
    {
    	TransferHandler.TransferItem(iStack, this, getCon(null), Slot.class,null, -1);
    }		
    /**
     * Add an item to the cart's inventory
     * @param iStack The item to put in the cart
     * @param start The index of the first valid slot
     * @param end The index of the last valid slot
     */
    public void addItemToChest(ItemStack iStack, int start, int end)
    {
    	TransferHandler.TransferItem(iStack, this, start, end, getCon(null), Slot.class,null, -1);
    }
    /**
     * Add an item to the cart's inventory
     * @param iStack The item to put in the cart
     * @param validSlot The class of the valid slots
     * @param invalidSlot The class of the invalid slots
     */
	public void addItemToChest(ItemStack iStack, java.lang.Class validSlot, java.lang.Class invalidSlot)
    {
        TransferHandler.TransferItem(iStack, this, getCon(null), validSlot, invalidSlot, -1);
    }	



	
 
	
	/**
	 * Returns an item in a slot
	 */
	 @Override
    public ItemStack getStackInSlot(int i)
    {
		if (modules != null) {
			for(ModuleBase module : modules) {
				if (i < module.getInventorySize()) {
					return module.getStack(i);
				}else{
					i -= module.getInventorySize();
				}
			}
		}
	
        return null;
    }
	
	
    /**
     * Sets the given item stack to the specified slot in the inventory
     */
	 @Override
    public void setInventorySlotContents(int i, ItemStack item)
    {
		if (modules != null) {
			for(ModuleBase module : modules) {
				if (i < module.getInventorySize()) {
					module.setStack(i,item);
					break;
				}else{
					i -= module.getInventorySize();
				}
			}	
		}
    }	
	
	    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
	 @Override
    public ItemStack decrStackSize(int i, int n)
    {
		if (modules == null) {
			return null;
		}
	
        if (this.getStackInSlot(i) != null)
        {
            ItemStack item;

            if (this.getStackInSlot(i).stackSize <= n)
            {
                item = this.getStackInSlot(i);
                this.setInventorySlotContents(i,null);
                return item;
            }
            else
            {
                item = this.getStackInSlot(i).splitStack(n);

                if (this.getStackInSlot(i).stackSize == 0)
                {
                    this.setInventorySlotContents(i,null);
                }

                return item;
            }
        }
        else
        {
            return null;
        }
    }
	@Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.getStackInSlot(i) != null)
        {
            ItemStack var2 = this.getStackInSlot(i);
            this.setInventorySlotContents(i,null);
            return var2;
        }
        else
        {
            return null;
        }
    }	

    /**
    Returns the container of this cart
    **/
    public Container getCon(InventoryPlayer player)
    {
        return new ContainerMinecart(player, this);
    }



	
	/**
	 * Called when a player interacts with the cart to open any chests
	 */
    @Override
    public void openInventory()
    {
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (module instanceof ModuleChest) {
					((ModuleChest)module).openChest();
				}
			}
		}
    }

    /**
     * Called when a player stops interacting with the cart to close any chests
     */
    @Override
    public void closeInventory()
    {
		if (modules != null) {
			for (ModuleBase module : modules) {
				if (module instanceof ModuleChest) {
					((ModuleChest)module).closeChest();
				}
			}
		}
    }	



	
    /**
     * Mark this cart as a placeholder cart, a cart that is simulated in the Cart Assembler's interface
     * @param assembler The assembler the cart is simulated in
     */
	public void setPlaceholder(TileEntityCartAssembler assembler) {
		isPlaceholder = true;
		placeholderAsssembler = assembler;
	}


	/**
	 * Returns the bounding box of the cart.
	 */
	@Override
	public AxisAlignedBB getBoundingBox()
    {	
		if (isPlaceholder) {
			return null;
		}else{
			return super.getBoundingBox();
		}
    }
	
	/**
	 * Returns whether something can collide with the cart or not.
	 */
	@Override
	public boolean canBeCollidedWith()
    {
		if (isPlaceholder) {
			return false;
		}else{
			return super.canBeCollidedWith();
		}
    }
	
	/**
	 * Returns whether the cart can be pushed or not
	 */
	@Override
	public boolean canBePushed()
    {
		if (isPlaceholder) {
			return false;
		}else{
			return super.canBePushed();
		}
    }	
	
	/**
	 * Generate the models for this cart
	 */
	@SideOnly(Side.CLIENT)
	private void generateModels() {
		if (modules != null) {
			ArrayList<String> invalid = new ArrayList<String>();
			//loops through the modules to remove all models that should be prevented to render
			for (ModuleBase module : modules) {
				ModuleData data = module.getData();
			
				if (data.haveRemovedModels()) {
					for (String remove : data.getRemovedModels()) {
						invalid.add(remove);
					}
				}
			}
			
			//loop through all the modules backwards so later modules will "override" the early ones
			for (int i = modules.size() - 1; i >= 0; i--) {
				ModuleBase module = modules.get(i);
				
				ModuleData data = module.getData();
				if (data != null) {
					if (data.haveModels(isPlaceholder)) {
						ArrayList<ModelCartbase> models = new ArrayList<ModelCartbase>();
						
						//add all the models
						for (String str : data.getModels(isPlaceholder).keySet()) {
							if (!invalid.contains(str)) {
								models.add(data.getModels(isPlaceholder).get(str));
								
								//mark that this model has been added somewhere, don't register it again
								invalid.add(str);
							}
						}
						
						//if there's any models, register them at the module
						if (models.size() > 0) {
							module.setModels(models);
						}
					}

				}
				
			}
		}

		
	}	
	
	@SideOnly(Side.CLIENT)	
	/**
     Returns the gui of this cart
    **/
	public GuiScreen getGui(EntityPlayer player)
    {
        return new GuiMinecart(player.inventory, this);
    }
	
    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param data The packet data stream
     */
	 @Override
    public void writeSpawnData(ByteBuf data) {
		data.writeByte(moduleLoadingData.length);
		for (byte b : moduleLoadingData) {
			data.writeByte(b);
		}
		
		data.writeByte(name.getBytes().length);
        for (byte b : name.getBytes()) {
            data.writeByte(b);
        }
	}

    /**
     * Called by the client when it receives a Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param data The packet data stream
     */
	 @Override
    public void readSpawnData(ByteBuf data) {
		byte length = data.readByte();
		byte[] bytes  = new byte[length];
		data.readBytes(bytes);
		loadModules(bytes);
		
		int nameLength = data.readByte();
		byte[] nameBytes = new byte[nameLength];
		for (int i = 0; i < nameLength; i++) {
            nameBytes[i] = data.readByte();
		}
        name = new String(nameBytes);
		
		if (getDataWatcher() instanceof DataWatcherLockable) {
			((DataWatcherLockable)getDataWatcher()).release();
		}
		
	}


	
	private int scrollY;
	public void setScrollY(int val) {
		if (canScrollModules) {
			scrollY = val;
		}
	}
	public int getScrollY() {
		if (getInterfaceThief() != null) {
			return 0;
		}else{
			return scrollY;
		}		
	}
	public int getRealScrollY() {
		return (int)(((modularSpaceHeight - MODULAR_SPACE_HEIGHT) / 198F) * getScrollY());
	}
		

	public int fill(FluidStack resource, boolean doFill) {
		return fill(ForgeDirection.UNKNOWN, resource, doFill);
	}
	
	/**
	 * Fills fluid into internal tanks, distribution is left to the ITankContainer.
	 * @param from Orientation the fluid is pumped in from.
	 * @param resource FluidStack representing the maximum amount of fluid filled into the ITankContainer
	 * @param doFill If false filling will only be simulated.
	 * @return Amount of resource that was filled into internal tanks.
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int amount = 0;
		if (resource != null && resource.amount > 0) {
			FluidStack fluid = resource.copy();
			for (int i = 0; i < tankModules.size(); i++) {
				int tempAmount = tankModules.get(i).fill(fluid, doFill);
		
				amount += tempAmount;
				fluid.amount -= tempAmount;
				if (fluid.amount <= 0) {
					break;
				}
			}
		}
		return amount;			
	}


	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}
	
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, resource == null ? 0 : resource.amount, doDrain);
	}
	
	private FluidStack drain(ForgeDirection from, FluidStack resource, int maxDrain, boolean doDrain) {
		FluidStack ret = resource;
		if (ret != null) {
			ret = ret.copy();
			ret.amount = 0;
		}
		for (int i = 0; i < tankModules.size(); i++) {
			FluidStack temp = null;
			temp = tankModules.get(i).drain(maxDrain, doDrain);
			
			if (temp != null && (ret == null || ret.isFluidEqual(temp))) {
				if (ret == null) {
					ret = temp;
				}else{
					ret.amount += temp.amount;
				}
			
				maxDrain -= temp.amount;
				if (maxDrain <= 0) {
					break;
				}
			}
		}
		if (ret != null && ret.amount == 0) {
			return null;
		}
		return ret;			
	}
	
	public int drain(Fluid type, int maxDrain, boolean doDrain) {
		int amount = 0;
		if (type != null && maxDrain > 0) {
			for (ModuleTank tank : tankModules) {
				FluidStack drained = tank.drain(maxDrain, false);
				if (drained != null && type.equals(drained.getFluid())) {
					amount += drained.amount;
					maxDrain -= drained.amount;
					if (doDrain) {
						tank.drain(drained.amount,true);
					}
					
					if (maxDrain <= 0) {
						break;
					}
				}
			}
		}
		return amount;
	}		
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}	

	/**
	 * @param direction tank side: UNKNOWN for default tank set
	 * @return Array of {@link FluidTank}s contained in this ITankContainer for this direction
	 */
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection direction) {
		FluidTankInfo[] ret = new FluidTankInfo[tankModules.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new FluidTankInfo(tankModules.get(i).getFluid(), tankModules.get(i).getCapacity());
		}
		return ret;
	}

	
		@Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {	
		if (modules != null) {
			for(ModuleBase module : modules) {
				if (slot < module.getInventorySize()) {
					return module.getSlots().get(slot).isItemValid(item);
				}else{
					slot -= module.getInventorySize();
				}
			}
		}
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public String getInventoryName() {
		return "container.modularcart";
	}

	public String getCartName() {
		if (name == null || name.length() == 0) {
			return "Modular Cart";
		}else{
			return name;
		}
	}
	
	public boolean hasCreativeSupplies() {
		return creativeSupplies != null;
	}


	@Override
	public boolean canRiderInteract() {
		return true;
	}

}
