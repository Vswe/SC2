package vswe.stevesvehicles.tileentity;
import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.localization.entry.block.LocalizationToggler;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.tileentity.toggler.TogglerOption;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.container.ContainerActivator;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.client.gui.screen.GuiActivator;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.module.common.addon.chunk.ModuleChunkLoader;
import vswe.stevesvehicles.module.common.addon.ModuleInvisible;
import vswe.stevesvehicles.module.common.addon.ModuleShield;
import vswe.stevesvehicles.module.common.attachment.ModuleCage;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;

/**
 * The tile entity used by the Module Toggler
 * @author Vswe
 *
 */
public class TileEntityActivator extends TileEntityBase {

	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiActivator(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerActivator(this);
	}
	
	/**
	 * The different settings the toggler can toggle
	 */
	private ArrayList<TogglerOption> options;

    public TileEntityActivator() {
		loadOptions();
    }

    //TODO let mods register these?
    /**
     * Load the different settings the player can toggle and change. For example the drill.
     */
	private void loadOptions() {
		options = new ArrayList<TogglerOption>();
		options.add(new TogglerOption(LocalizationToggler.DRILL_OPTION, ModuleDrill.class));
		options.add(new TogglerOption(LocalizationToggler.SHIELD_OPTION, ModuleShield.class));
		options.add(new TogglerOption(LocalizationToggler.INVISIBILITY_OPTION, ModuleInvisible.class));
		options.add(new TogglerOption(LocalizationToggler.CHUNK_OPTION, ModuleChunkLoader.class));
		options.add(new TogglerOption(LocalizationToggler.AUTO_CAGE_OPTION, ModuleCage.class, 0));
		options.add(new TogglerOption(LocalizationToggler.CAGE_OPTION, ModuleCage.class, 1));
	}
	
	/**
	 * Get the different settings the toggler can toggle
	 * @return A list of the settings
	 */
	public ArrayList<TogglerOption> getOptions() {
		return options;
	}

	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        
        //load all the options
 		for (TogglerOption option : options) {
			option.setOption(nbttagcompound.getByte(option.getName()));
		}
    }

	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        
        //save all the options
		for (TogglerOption option : options) {
			nbttagcompound.setByte(option.getName(), (byte)option.getOption());
		}
    }




	@Override
	public void receivePacket(DataReader dr, EntityPlayer player) {
        boolean leftClick = dr.readBoolean();
        int optionId = dr.readByte();
        if (optionId >= 0 && optionId < options.size()) {
            options.get(optionId).changeOption(leftClick);
        }

	}
	

	

	@Override
	public void initGuiData(Container con, ICrafting crafting) {
		for (int i = 0; i < options.size(); i++) {
			updateGuiData(con, crafting, i, (short)options.get(i).getOption());
		}
	}


	@Override
	public void checkGuiData(Container con, ICrafting crafting) {
		for (int i = 0; i < options.size(); i++) {
			int option = options.get(i).getOption();
			int lastOption = ((ContainerActivator)con).lastOptions.get(i);
			
			//if an update has been made, send the new data
			if (option != lastOption) {
				updateGuiData(con, crafting, i, (short)option);
				((ContainerActivator)con).lastOptions.set(i, option);
			}
		}
	}
	

	@Override
	public void receiveGuiData(int id, short data) {
		//if it's a valid id, update the option associated with that id
		if (id >= 0 && id < options.size()) {
			options.get(id).setOption(data);
		}	
	}
	
	/**
	 * Handles a cart that is passing an advanced detector rail "in front" of this toggler
	 * @param cart The cart that is passing
	 * @param isOrange Whether the cart is passing in the orange direction or not
	 */
	public void handleCart(EntityModularCart cart, boolean isOrange) {
		
		//tell the cart to update with any option that is not disabled
		for (TogglerOption option : options) {
			if (!option.isDisabled()) {
				cart.getVehicle().handleActivator(option, isOrange);
			}
		}
	}



}
