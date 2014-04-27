package vswe.stevescarts.Blocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityActivator;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;
import net.minecraft.client.renderer.texture.IconRegister;


public class BlockActivator extends BlockContainer
{


    public BlockActivator(int i)
    {
        super(i, Material.rock);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
    }


	private Icon topIcon;
	private Icon botIcon;
	private Icon sideIcon;
	
    @SideOnly(Side.CLIENT)
	@Override
    public Icon getIcon(int side, int meta)
    {
        if (side == 0) {
			return botIcon;
		}else if(side == 1) {
			return topIcon;
		}else {
			return sideIcon;
		}
    }
	
    @SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister register)
    {
        topIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "module_toggler" + "_top");
		botIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "module_toggler" + "_bot");
		sideIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "module_toggler" + "_side");
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


		FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 4, world, i, j, k);

        return true;
    }

	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityActivator();
    }
}
