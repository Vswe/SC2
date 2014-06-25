package vswe.stevesvehicles.old.Items;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import vswe.stevesvehicles.old.StevesCarts;
import vswe.stevesvehicles.old.ModuleData.ModuleData;
import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
public class ItemCartModule extends Item
{

    public ItemCartModule()
    {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(StevesCarts.tabsSC2);	
    }


	public String getName(ItemStack par1ItemStack)
    {
		ModuleData data = getModuleData(par1ItemStack, true);
		if (data == null) {
			return "Unknown SC2 module";
		}else{
			return data.getName();
		}
		
    }
	

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg)
    {
		ModuleData data = ModuleData.getList().get((byte)dmg);
		if (data != null) {
			return data.getIcon();
		}
		return unknownIcon;
    }	
		
	IIcon unknownIcon;
		
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
		for (ModuleData module : ModuleData.getList().values()) {
			module.createIcon(register);
		}
		unknownIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "unknown_icon");	
    }	

	@Override
    public String getUnlocalizedName() {
	
		return "item." + StevesCarts.localStart + "unknownmodule";
	}
	
	@Override
    public String getUnlocalizedName(ItemStack item)
    {
		ModuleData data = getModuleData(item, true);
		if (data != null) {
			 return "item." + StevesCarts.localStart  + data.getRawName();
		}
		return getUnlocalizedName();
    }
	
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
		for (ModuleData module : ModuleData.getList().values()) {
			if (module.getIsValid()) {
				par3List.add(module.getItemStack());			
			}
        }
    }
	
    @SideOnly(Side.CLIENT)
    @Override

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		ModuleData module = getModuleData(par1ItemStack, true);
		if (module != null) {
			module.addInformation(par3List, par1ItemStack.getTagCompound());
		}else if (par1ItemStack != null && par1ItemStack.getItem() instanceof ItemCartModule){
			par3List.add("Module id " + par1ItemStack.getItemDamage());
		}else{
			par3List.add("Unknown module id");
		}
		
		
	}	
    
    
	
	public ModuleData getModuleData(ItemStack itemstack) {
		return getModuleData(itemstack, false);
	}
	
	public ModuleData getModuleData(ItemStack itemstack, boolean ignoreSize) {
		if (itemstack != null && itemstack.getItem() instanceof ItemCartModule && (ignoreSize || itemstack.stackSize != TileEntityCartAssembler.getRemovedSize())) {
			return ModuleData.getList().get((byte)itemstack.getItemDamage());
		}else{
			return null;
		}
	}


	public void addExtraDataToCart(NBTTagCompound save, ItemStack module, int i) {
		if (module.getTagCompound() != null && module.getTagCompound().hasKey("Data")) {
			save.setByte("Data" + i, module.getTagCompound().getByte("Data"));
		}else{
			ModuleData data = getModuleData(module, true);
			if (data.isUsingExtraData()) {
				save.setByte("Data" + i, data.getDefaultExtraData());
			}
		}
	}




	public void addExtraDataToModule(NBTTagCompound save, ModuleBase module, int i) {
		if (module.hasExtraData()) {
			save.setByte("Data" + i, module.getExtraData());
		}
	}


	public void addExtraDataToModule(ItemStack module, NBTTagCompound info, int i) {
		NBTTagCompound save = module.getTagCompound();
		if (save == null) {
			module.setTagCompound(save = new NBTTagCompound());
		}
		

		if (info != null && info.hasKey("Data" + i)) {
			save.setByte("Data" , info.getByte("Data" + i));
		}else{
			ModuleData data = getModuleData(module, true);
			if (data.isUsingExtraData()) {
				save.setByte("Data", data.getDefaultExtraData());
			}
		}
		
				
	}
	
}
