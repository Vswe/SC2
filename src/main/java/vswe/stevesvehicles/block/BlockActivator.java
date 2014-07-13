package vswe.stevesvehicles.block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.tileentity.TileEntityActivator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tab.CreativeTabLoader;


public class BlockActivator extends BlockContainerBase {

    public BlockActivator() {
        super(Material.rock);
        setCreativeTab(CreativeTabLoader.blocks);
    }

	private IIcon topIcon;
	private IIcon botIcon;
	private IIcon sideIcon;
	
    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
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
    public void registerBlockIcons(IIconRegister register) {
        topIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":toggler/top");
		botIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":toggler/bot");
		sideIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":toggler/side");
    }	
	

	@Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityActivator();
    }
}
