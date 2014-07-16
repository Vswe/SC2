package vswe.stevesvehicles.item;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import util.Localization;
import vswe.stevesvehicles.localization.entry.info.LocalizationLabel;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleDataPair;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.util.Tuple;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.VehicleRegistry;
import vswe.stevesvehicles.vehicle.VehicleType;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.vehicle.version.VehicleVersion;
import vswe.stevesvehicles.client.gui.ColorHelper;
import vswe.stevesvehicles.GeneratedInfo;
import vswe.stevesvehicles.module.data.ModuleData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class ItemVehicles extends Item {

	
	
	
    public ItemVehicles() {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(null);	
    }

	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        for (VehicleType vehicleType : VehicleRegistry.getInstance().getAllVehicles()) {
            vehicleType.registerIcons(register);
        }
        fallbackFallbackIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":unknown");
    }

    @SideOnly(Side.CLIENT)
    private IIcon fallbackFallbackIcon; //if it fails to use a fallback icon :P

    //this will only be used if the 3d rendering fails for some reason
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg) {
        VehicleType type = VehicleRegistry.getInstance().getTypeFromId(dmg);
        if (type != null) {
            return type.getFallbackIcon();
        }else{
            return fallbackFallbackIcon;
        }
    }

    //TODO
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		VehicleVersion.updateItemStack(par1ItemStack);
		
	    if (BlockRailBase.func_150049_b_(par3World,par4, par5, par6))
        {
            if (!par3World.isRemote) {
               try {
                    NBTTagCompound info = par1ItemStack.getTagCompound();
                    if (info != null) {
                        if (!info.hasKey(VehicleBase.NBT_INTERRUPT_MAX_TIME)) {
                            EntityModularCart cart = new EntityModularCart(par3World, (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), info, par1ItemStack.hasDisplayName() ? par1ItemStack.getDisplayName() : null);
                            par3World.spawnEntityInWorld(cart);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
			
			--par1ItemStack.stackSize;
						
			return true;
		}	
        return false;
    }


    @SideOnly(Side.CLIENT)
	@Override
    /**
     * allows items to add custom lines of information to the mouse over description
     */
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean useExtraInfo) {
		VehicleVersion.updateItemStack(item);

        NBTTagCompound info = item.getTagCompound();

		if (info != null) {
            addInfo(ModuleDataItemHandler.getModulesAndCompoundsFromItem(item), list, null);
            addInfo(ModuleDataItemHandler.getSpareModulesAndCompoundsFromItem(item), list, ColorHelper.ORANGE);

			if (info.hasKey(VehicleBase.NBT_INTERRUPT_MAX_TIME)) {
				list.add(ColorHelper.RED + LocalizationLabel.INCOMPLETE.translate());
                list.add(ColorHelper.RED + LocalizationLabel.INTERRUPT_INSTRUCTION.translate());
				int maxTime = info.getInteger(VehicleBase.NBT_INTERRUPT_MAX_TIME);
				int currentTime = info.getInteger(VehicleBase.NBT_INTERRUPT_TIME);
				int timeLeft = maxTime - currentTime;
				list.add(ColorHelper.RED + LocalizationLabel.TIME_LEFT.translate() + ": " + formatTime(timeLeft));

			}

			if (GeneratedInfo.inDev) {
                //dev version only, no localization required
				list.add(ColorHelper.WHITE + "Version: " + (info.hasKey("CartVersion") ? info.getByte("CartVersion") : 0));
			}
		}else{
			list.add(LocalizationLabel.NO_MODULES.translate());
		}
	}

    private void addInfo(List<Tuple<ModuleData, NBTTagCompound>> modules, List list, ColorHelper color) {
        if (modules != null) {

            ArrayList<ModuleDataPair> counts = new ArrayList<ModuleDataPair>();

            for (Tuple<ModuleData, NBTTagCompound> moduleTuple : modules) {
                ModuleData module = moduleTuple.getFirstObject();
                NBTTagCompound moduleCompound = moduleTuple.getSecondObject();
                boolean found = false;
                if (module.hasExtraData()) {
                    for (ModuleDataPair count : counts) {
                        if (count.isContainingData(module)) {
                            count.increase();
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    ModuleDataPair count = new ModuleDataPair(module);
                    if (module.hasExtraData()) {
                        count.setExtraData(moduleCompound);
                    }
                    counts.add(count);
                }
            }

            for (ModuleDataPair count : counts) {
                if (color != null) {
                    list.add(color + count.toString());
                }else{
                    list.add(count.toString());
                }

            }
        }
    }
	
	private String formatTime(int ticks) {
		int seconds = ticks / 20;
		ticks -= seconds * 20;
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		int hours = minutes / 60;
		minutes -= hours * 60;
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}	
	

    @Override
    public String getUnlocalizedName(ItemStack item) {
        int dmg = item.getItemDamage();
        VehicleType type = VehicleRegistry.getInstance().getTypeFromId(dmg);
        if (type != null) {
            return type.getUnlocalizedNameForItem();
        }else{
            return "item.unknown";
        }
    }
}
