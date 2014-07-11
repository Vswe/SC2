package vswe.stevesvehicles.block;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tab.CreativeTabLoader;

public class BlockDetector extends BlockContainerBase {

    public BlockDetector() {
        super(Material.circuits);
        setCreativeTab(CreativeTabLoader.blocks);
    }




    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
        return DetectorType.getTypeFromMeta(meta).getIcon(side);
    }
	
	
    @SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister register) {
    	for (DetectorType type : DetectorType.values()) {
    		type.registerIcons(register);
    	}
    }

    
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
    	for (DetectorType type : DetectorType.values()) {
    		list.add(new ItemStack(item, 1, type.getMeta()));
    	}
    }



    @Override
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }


	@Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		return ((meta & 8) != 0 && DetectorType.getTypeFromMeta(meta).shouldEmitRedstone()) ? 15 : 0;
    }

	@Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return 0;
    }

	@Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        if (side == -1) {
            return false;
        }else{
            DetectorType type = DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y, z));

            return type.shouldEmitRedstone() || type == DetectorType.REDSTONE;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDetector();
    }
	
	@Override
    public int damageDropped(int meta) {
        return meta;
    }   
	
}
