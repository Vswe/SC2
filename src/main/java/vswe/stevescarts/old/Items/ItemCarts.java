package vswe.stevescarts.old.Items;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vswe.stevescarts.old.StevesCarts;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.CartVersion;
import vswe.stevescarts.old.Helpers.ColorHelper;
import vswe.stevescarts.old.Helpers.GeneratedInfo;
import vswe.stevescarts.old.Helpers.ModuleCountPair;
import vswe.stevescarts.old.ModuleData.ModuleData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class ItemCarts extends ItemMinecart
{

	
	
	
    public ItemCarts()
    {
        super(0);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(null);	
    }
	

	public String getName()
    {
        return "Modular cart";
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "modular_cart" + "_icon");
    }	
	
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
		CartVersion.updateItemStack(par1ItemStack);
		
	    if (BlockRailBase.func_150049_b_(par3World,par4, par5, par6))
        {
            if (!par3World.isRemote) {
               try {
                    NBTTagCompound info = par1ItemStack.getTagCompound();
                    if (info != null) {
                        if (!info.hasKey("maxTime")) {
                            EntityModularCart cart = new EntityModularCart(par3World, (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), info, par1ItemStack.getDisplayName());
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
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean useExtraInfo) {
		CartVersion.updateItemStack(item);
		
		NBTTagCompound info = item.getTagCompound();
		if (info != null) {
			NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
			byte [] bytes = moduleIDTag.func_150292_c();
			
			ArrayList<ModuleCountPair> counts = new ArrayList<ModuleCountPair>();
			
			for (int i = 0; i < bytes.length; i++) { 
				byte id = bytes[i];
				ModuleData module = ModuleData.getList().get(id);
				if (module != null) {
					boolean found = false;
					if (!info.hasKey("Data" + i)) {
						for (ModuleCountPair count : counts) {
							if (count.isContainingData(module)) {
								count.increase();
								found = true;
								break;
							}
						}
					}
					
					if (!found) {
						ModuleCountPair count = new ModuleCountPair(module);
						if (info.hasKey("Data" + i)) {
							count.setExtraData(info.getByte("Data" + i));
						}						
						counts.add(count);
					}
				}
			}
			
			for (ModuleCountPair count : counts) {
				list.add(count.toString());				
			}

			
			if (info.hasKey("Spares")) {
				byte[] spares = info.getByteArray("Spares");
				for (int i = 0; i < spares.length; i++) {
					byte id = spares[i];
					ModuleData module = ModuleData.getList().get(id);
					if (module != null) {
						String name = module.getName();
						if (info.hasKey("Data" + (bytes.length + i))) {
							name = module.getCartInfoText(name, info.getByte("Data" + (bytes.length + i)));
						}
						
						list.add(ColorHelper.ORANGE + name);
					}
				}
			}			
			
			if (info.hasKey("maxTime")) {
				list.add(ColorHelper.RED + "Incomplete cart!");
				int maxTime = info.getInteger("maxTime");
				int currentTime = info.getInteger("currentTime");
				int timeLeft = maxTime - currentTime;
				list.add(ColorHelper.RED + "Time left: " + formatTime(timeLeft));
			}

			if (GeneratedInfo.inDev) {
				list.add(ColorHelper.WHITE + "Version: " + (info.hasKey("CartVersion") ? info.getByte("CartVersion") : 0));
			}
		}else{
			list.add("No modules loaded");
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
	public boolean getShareTag() {
		return true;
	}

}
