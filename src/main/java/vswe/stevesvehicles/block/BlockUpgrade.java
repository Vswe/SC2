package vswe.stevesvehicles.block;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tab.CreativeTabLoader;
import vswe.stevesvehicles.upgrade.Upgrade;

public class BlockUpgrade extends BlockContainerBase {

    public BlockUpgrade() {
        super(Material.rock);
        setCreativeTab(CreativeTabLoader.blocks);
    }


    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
		return Upgrade.getStandardIcon();
    }	
	
	@Override
    public void registerBlockIcons(IIconRegister register) {
		//Load nothing here
    }
	

	@Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }	
	
	private Upgrade getUpgrade(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
			return upgrade.getUpgrade();
		}	
		return null;
	}
	
	@Override
    public int getDamageValue(World world, int x, int y, int z) {
        Upgrade upgrade = getUpgrade(world, x, y, z);
        return upgrade != null ? upgrade.getItemStack().getItemDamage() : 0;
    }
	
	@Override
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {}

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        if (side == -1) {
            return false;
        }

        Upgrade upgrade = getUpgrade(world, x, y, z);

        return upgrade != null && upgrade.connectToRedstone();
    }
	
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
        ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).addUpgrade(world, x, y, z);
	}
	@Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        if (player.capabilities.isCreativeMode)
        {
            world.setBlockMetadataWithNotify(x, y, z, 1, 0);
        }

        super.onBlockHarvested(world, x, y, z, meta, player);
    }
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block id, int meta) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)te;
			upgrade.removed();

			if (meta != 1) {
                Upgrade assemblerUpgrade = getUpgrade(world, x, y, z);
                if (assemblerUpgrade != null) {
                    dropBlockAsItem(world, x, y, z, assemblerUpgrade.getItemStack());
                }
			}
        }

		super.breakBlock(world, x, y, z, id, meta);
        ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).removeUpgrade(world, x, y, z);
    }


    @Override
	public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
	public boolean isOpaqueCube() {
        return false;
    }

	@Override
    public int getRenderType() {
        return renderAsNormalBlock() || StevesVehicles.instance.blockRenderer == null ? 0 : StevesVehicles.instance.blockRenderer.getRenderId();
    }	
	

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)  {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }	
	

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        setUpgradeBounds(par1IBlockAccess, par2, par3, par4);
    }	
	
	public final int setUpgradeBounds(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if(tile instanceof TileEntityUpgrade){
			TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
			TileEntityCartAssembler master = upgrade.getMaster();
			
			float margin = 0.1875F; //3 pixels
			float width = 0.125F; //2 pixels
			
			if (master == null) {
				setIdleBlockBounds();
				return 0;
			}else if (master.yCoord < y) {
				setBlockBounds(margin, 0, margin, 1.0F - margin, width, 1.0F - margin);
				return 0;
			}else if(master.yCoord > y) {
				setBlockBounds(margin, 1.0F-width, margin, 1.0F - margin, 1.0F, 1.0F - margin);
				return 1;
			}else if (master.xCoord < x) {
				setBlockBounds(0, margin, margin, width, 1.0F - margin, 1.0F - margin);
				return 3;
			}else if(master.xCoord > x) {
				setBlockBounds(1.0F-width, margin, margin, 1.0F, 1.0F - margin, 1.0F - margin);
				return 5;
			}else if (master.zCoord < z) {
				setBlockBounds(margin, margin, 0, 1.0F - margin, 1.0F - margin, width);
				return 4;
			}else if(master.zCoord > z) {
				setBlockBounds(margin, margin, 1.0F-width, 1.0F - margin, 1.0F - margin, 1.0F);
				return 2;
			}
		}
		return -1;
	}
	
	public void setIdleBlockBounds() {
		float margin = 0.1875F; //3 pixels
		float width = 0.125F; //2 pixels

		setBlockBounds(margin, width, margin, 1-margin, 1-width, 1-margin);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;

			if (upgrade.useStandardInterface()) {
                if (upgrade.getMaster() != null) {
                    return ModBlocks.CART_ASSEMBLER.getBlock().onBlockActivated(world, upgrade.getMaster().xCoord, upgrade.getMaster().yCoord, upgrade.getMaster().zCoord, player, side, hitX, hitY, hitZ);
                }
            }else{
			    return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
            }
		}

		
        return true;
    }

	@Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityUpgrade();
    }
}
