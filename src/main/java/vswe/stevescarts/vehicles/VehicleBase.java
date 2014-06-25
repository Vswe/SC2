package vswe.stevescarts.vehicles;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import vswe.stevescarts.old.Containers.ContainerMinecart;
import vswe.stevescarts.old.Helpers.ActivatorOption;
import vswe.stevescarts.old.Helpers.CartVersion;
import vswe.stevescarts.old.Helpers.CompWorkModule;
import vswe.stevescarts.old.Helpers.GuiAllocationHelper;
import vswe.stevescarts.old.Helpers.ModuleCountPair;
import vswe.stevescarts.old.Helpers.TransferHandler;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Models.Cart.ModelCartbase;
import vswe.stevescarts.old.ModuleData.ModuleData;
import vswe.stevescarts.old.Modules.Addons.ModuleCreativeSupplies;
import vswe.stevescarts.old.Modules.Engines.ModuleEngine;
import vswe.stevescarts.old.Modules.IActivatorModule;
import vswe.stevescarts.old.Modules.ModuleBase;
import vswe.stevescarts.old.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.old.Modules.Workers.ModuleWorker;
import vswe.stevescarts.old.StevesCarts;
import vswe.stevescarts.old.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.vehicles.entities.IVehicleEntity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

public class VehicleBase {
    private byte [] moduleLoadingData;
    private ForgeChunkManager.Ticket cartTicket;
    private ModuleWorker workingComponent;
    public TileEntityCartAssembler placeholderAsssembler;
    public boolean isPlaceholder;
    public int keepAlive;
    protected int modularSpaceHeight;
    public boolean canScrollModules;
    private ArrayList<ModuleCountPair> moduleCounts;

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



    private IVehicleEntity vehicleEntity;
    private Entity entity;

    public World getWorld() {
        return entity.worldObj;
    }

    public VehicleBase(IVehicleEntity entity) {
        this.vehicleEntity = entity;
        this.entity = (Entity)entity;
    }

    public VehicleBase(IVehicleEntity entity, TileEntityCartAssembler assembler, byte[] data) {
        this(entity);
        setPlaceholder(assembler);

        loadPlaceholderModules(data);
    }

    public VehicleBase(IVehicleEntity entity,NBTTagCompound info, String name) {
        this(entity);
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

    /**
     * The name the cart has if renamed /by an anvil)
     */
    protected String name;

    /**
     * The version this cart has, for more info about cersion see {@link vswe.stevescarts.old.Helpers.CartVersion}
     */
    public byte cartVersion;

    public ArrayList<ModuleCountPair> getModuleCounts() {
        return moduleCounts;
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

                Constructor moduleConstructor = moduleClass.getConstructor(new Class[] {EntityModularCart.class});

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
    public void loadChunks(ForgeChunkManager.Ticket ticket) {
        loadChunks(ticket, x() >> 4, z() >> 4);
    }
    /**
     * Load chunks with the given ticket at the given position
     * @param ticket The ticket to load with
     * @param chunkX The chunk's X coordinate
     * @param chunkZ The chunk's Z coordinate
     */
    public void loadChunks(ForgeChunkManager.Ticket ticket, int chunkX, int chunkZ) {
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
     * Add an item to the cart's inventory
     * @param iStack The item to put in the cart
     */
    public void addItemToChest(ItemStack iStack)
    {
        TransferHandler.TransferItem(iStack, this, getCon(null), Slot.class, null, -1);
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
     Returns the container of this cart
     **/
    public Container getCon(InventoryPlayer player)
    {
        return new ContainerMinecart(player, this);
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


    public void preDeath() {
        //removes all the items on the client side, this is so the client won't drop ghost items
        if (worldObj.isRemote) {
            for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
            {
                setInventorySlotContents(var1, null);
            }
        }

    }

    public void postDeath() {
        //tell all the modules that the cart is being removed
        if (modules != null) {
            for (ModuleBase module : modules) {
                module.onDeath();
            }
        }

        //stop loading chunks
        dropChunkLoading();
    }
}
