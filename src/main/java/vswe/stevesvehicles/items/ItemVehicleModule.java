package vswe.stevesvehicles.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.old.StevesVehicles;

import java.util.List;


public class ItemVehicleModule extends Item {
    public ItemVehicleModule() {
        setCreativeTab(StevesVehicles.tabsSC2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg) {
        ModuleData data = getModuleData(dmg);
        if (data != null) {
            return data.getIcon();
        }
        return unknownIcon;
    }


    @Override
    public String getUnlocalizedName() {
        return "item." + StevesVehicles.localStart + "unknown_module";
    }

    IIcon unknownIcon;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        for (ModuleData moduleData : ModuleRegistry.getAllModules()) {
            moduleData.createIcon(register);
        }

        unknownIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + "unknown_icon");
    }

    @Override
    public String getUnlocalizedName(ItemStack item){
        ModuleData data = getModuleData(item);
        if (data != null) {
            return data.getUnlocalizedName();
        }
        return getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List lst) {
        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (module.getIsValid()) {
                lst.add(module.getItemStack());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List lst, boolean useExtraInfo) {
        ModuleData module = getModuleData(item);
        if (module != null) {
            module.addInformation(lst, item.getTagCompound());
        }else if (item != null && item.getItem() == this){
            lst.add("Module id " + item.getItemDamage());
        }else{
            lst.add("Unknown module id");
        }

    }

    private ModuleData getModuleData(int dmg) {
        return ModuleRegistry.getModuleFromId(dmg);
    }

    public ModuleData getModuleData(ItemStack item) {
        if (item != null && item.getItem() == this) {
            return getModuleData(item.getItemDamage());
        }else{
            return null;
        }
    }
}
