package vswe.stevesvehicles.block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.tab.CreativeTabLoader;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRailJunction extends BlockSpecialRailBase {

	private IIcon normalIcon;
	private IIcon cornerIcon;

    public BlockRailJunction() {
        super(false);
        setCreativeTab(CreativeTabLoader.blocks);
    }

	@Override
    public IIcon getIcon(int side, int meta) {
        return meta >= 6 ? cornerIcon : normalIcon;
    }
	

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        normalIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":junction_rail");
		cornerIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":junction_rail_corner");
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z) {
		if (cart instanceof EntityModularCart) {
			EntityModularCart modularCart = (EntityModularCart)cart;
			
			int meta = modularCart.getRailMeta(x, y, z);
			
			if (meta != -1) {
				return meta;
			}
		}
		

        return world.getBlockMetadata(x, y, z);
    }
}
