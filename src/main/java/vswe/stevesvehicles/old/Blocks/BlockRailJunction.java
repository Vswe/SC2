package vswe.stevesvehicles.old.Blocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRailJunction extends BlockSpecialRailBase
{

	private IIcon normalIcon;
	private IIcon cornerIcon;

    public BlockRailJunction()
    {
        super(false);
        setCreativeTab(StevesVehicles.tabsSC2Blocks);
    }

	@Override
    public IIcon getIcon(int side, int meta)
    {
        return meta >= 6 ? cornerIcon : normalIcon;
    }
	

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        normalIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + "junction_rail");
		cornerIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + "junction_rail" + "_corner");
    }

    /*  Return true if the rail can go up and down slopes
     */
    @Override
    public boolean canMakeSlopes(IBlockAccess world, int i, int j, int k)
    {
        return false;
    }

    /*  Return the rails metadata
     */
    @Override
    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int i, int j, int k)
    {
		if (cart instanceof EntityModularCart) {
			EntityModularCart modularCart = (EntityModularCart)cart;
			
			int meta = modularCart.getRailMeta(i,j,k);
			
			if (meta != -1) {
				return meta;
			}
		}
		

        return world.getBlockMetadata(i, j, k);
    }
}
