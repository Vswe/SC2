package vswe.stevesvehicles.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.tab.CreativeTabLoader;

public class BlockMetalStorage extends Block implements IBlockBase {

    public BlockMetalStorage() {
        super(Material.iron);
        this.setCreativeTab(CreativeTabLoader.blocks);
        setHardness(5.0F);
        setResistance(10.0F);
    }

    
    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
    	meta %= ModItems.storages.icons.length;
    	
    	return ModItems.storages.icons[meta];
    }
    
    public int damageDropped(int meta) {
        return meta;
    }    

    @SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister register) {
    	//do nothing here
    }


    private String unlocalizedName;

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public void setUnlocalizedName(String name) {
        this.unlocalizedName = name;
    }
}

