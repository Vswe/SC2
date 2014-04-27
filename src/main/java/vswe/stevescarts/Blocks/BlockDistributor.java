package vswe.stevescarts.Blocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityDistributor;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;
import net.minecraft.client.renderer.texture.IconRegister;
public class BlockDistributor extends BlockContainer
{


    public BlockDistributor(int i)
    {
        super(i, Material.rock);
        setCreativeTab(StevesCarts.tabsSC2Blocks);			
    }


	private Icon purpleIcon;
	private Icon orangeIcon;
	private Icon redIcon;
	private Icon blueIcon;
	private Icon greenIcon;
	private Icon yellowIcon;
	
    @SideOnly(Side.CLIENT)
	@Override
    public Icon getIcon(int side, int meta)
    {
        if (side == 0) {
			return purpleIcon;
		}else if(side == 1) {
			return orangeIcon;
		}else if(side == 2){
			return yellowIcon;
		}else if(side == 3){
			return blueIcon;
		}else if(side == 4){
			return greenIcon;
		}else{
			return redIcon;
		}
    }
	
    @SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister register)
    {
		purpleIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cargo_distributor" + "_purple");	
        orangeIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cargo_distributor" + "_orange");
		redIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cargo_distributor" + "_red");
		blueIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cargo_distributor" + "_blue");
		greenIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cargo_distributor" + "_green");
		yellowIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cargo_distributor" + "_yellow");
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


		FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 5, world, i, j, k);

        return true;
    }

	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityDistributor();
    }
}
