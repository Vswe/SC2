package vswe.stevescarts.Blocks;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.TileEntities.TileEntityDetector;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDetector extends BlockContainerBase
{

    public BlockDetector()
    {
        super(Material.circuits);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
    }




    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta)
    {        
        return DetectorType.getTypeFromMeta(meta).getIcon(side);
    }
	
	
    @SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister register)
    {
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
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isBlockNormalCube() {
        return true;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
		if (entityplayer.isSneaking()) {
			return false;
		}


        if (world.isRemote)
        {
            return true;
        }


		FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 6, world, i, j, k);

        return true;
    }
	
    /**
     * Is this block powering the block on the specified side
     */
	 @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
		int meta = world.getBlockMetadata(x, y, z);
		return ((meta & 8) != 0 && DetectorType.getTypeFromMeta(meta).shouldEmitRedstone()) ? 15 : 0;
    }

    /**
     * Is this block indirectly powering the block on the specified side
     */
	 @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
	 @Override
    public boolean canProvidePower()
    {
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
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityDetector();
    }
	
	@Override
    public int damageDropped(int meta) {
        return meta;
    }   
	
}
