package vswe.stevesvehicles.block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.tileentity.TileEntityDistributor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import vswe.stevesvehicles.tab.CreativeTabLoader;

public class BlockDistributor extends BlockContainerBase {


    public BlockDistributor() {
        super(Material.rock);
        setCreativeTab(CreativeTabLoader.blocks);
    }


	private IIcon purpleIcon;
	private IIcon orangeIcon;
	private IIcon redIcon;
	private IIcon blueIcon;
	private IIcon greenIcon;
	private IIcon yellowIcon;

    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
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
    public void registerBlockIcons(IIconRegister register) {
		purpleIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":distributor/purple");
        orangeIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":distributor/orange");
		redIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":distributor/red");
		blueIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":distributor/blue");
		greenIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":distributor/green");
		yellowIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":distributor/yellow");
    }	


	@Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDistributor();
    }
}
