package vswe.stevesvehicles.old.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.tileentity.TileEntityActivator;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tab.CreativeTabLoader;


public class BlockActivator extends BlockContainerBase
{


    public BlockActivator()
    {
        super(Material.rock);
        setCreativeTab(CreativeTabLoader.blocks);
    }


	private IIcon topIcon;
	private IIcon botIcon;
	private IIcon sideIcon;
	
    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta)
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
    public void registerBlockIcons(IIconRegister register)
    {
        topIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + "module_toggler" + "_top");
		botIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + "module_toggler" + "_bot");
		sideIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + "module_toggler" + "_side");
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


		FMLNetworkHandler.openGui(entityplayer, StevesVehicles.instance, 4, world, i, j, k);

        return true;
    }

	@Override
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityActivator();
    }
}
