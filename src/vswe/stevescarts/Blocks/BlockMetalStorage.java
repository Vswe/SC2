package vswe.stevescarts.Blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import vswe.stevescarts.StevesCarts;

public class BlockMetalStorage extends Block {

    public BlockMetalStorage(int id)
    {
        super(id, Material.iron);
        this.setCreativeTab(StevesCarts.tabsSC2Blocks);
    }

    
    @SideOnly(Side.CLIENT)
	@Override
    public Icon getIcon(int side, int meta) {
    	meta %= StevesCarts.storages.icons.length;
    	
    	return StevesCarts.storages.icons[meta];
    }
    
    public int damageDropped(int meta) {
        return meta;
    }    

    @SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister register)
    {
    	//do nothing here
    }
    
	

}

