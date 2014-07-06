package vswe.stevesvehicles.old.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tab.CreativeTabLoader;
import vswe.stevesvehicles.upgrade.Upgrade;

public class BlockUpgrade extends BlockContainerBase {

    public BlockUpgrade()
    {
        super(Material.rock);
        setCreativeTab(CreativeTabLoader.blocks);
    }


    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta)
    {
		return Upgrade.getStandardIcon();
    }	
	
	@Override
    public void registerBlockIcons(IIconRegister register)
    {
		//Load nothing here
    }
	
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
	@Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
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
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
			upgrade.removed();

		
		
			if (meta != 1) {
                Upgrade assemblerUpgrade = getUpgrade(world, x, y, z);
                if (assemblerUpgrade != null) {
                    dropBlockAsItem(world, x, y, z, assemblerUpgrade.getItemStack());
                }
			}
			
			if (upgrade.hasInventory()) {
				for (int var8 = 0; var8 < upgrade.getSizeInventory(); ++var8)
				{
					ItemStack var9 = upgrade.getStackInSlotOnClosing(var8);

					if (var9 != null)
					{
						float var10 = world.rand.nextFloat() * 0.8F + 0.1F;
						float var11 = world.rand.nextFloat() * 0.8F + 0.1F;
						EntityItem var14;

						for (float var12 = world.rand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; world.spawnEntityInWorld(var14))
						{
							int var13 = world.rand.nextInt(21) + 10;

							if (var13 > var9.stackSize)
							{
								var13 = var9.stackSize;
							}

							var9.stackSize -= var13;
							var14 = new EntityItem(world, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
							float var15 = 0.05F;
							var14.motionX = (double)((float)world.rand.nextGaussian() * var15);
							var14.motionY = (double)((float)world.rand.nextGaussian() * var15 + 0.2F);
							var14.motionZ = (double)((float)world.rand.nextGaussian() * var15);

							if (var9.hasTagCompound())
							{
								var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
							}
						}
					}
				}
			}
						
		}
		super.breakBlock(world, x, y, z, id, meta);
        ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).removeUpgrade(world, x, y, z);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
	@Override
    public int getRenderType()
    {
        return renderAsNormalBlock() || StevesVehicles.instance.blockRenderer == null ? 0 : StevesVehicles.instance.blockRenderer.getRenderId();
    }	
	

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }	
	
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
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
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		
		
		if (entityplayer.isSneaking()) {
			return false;
		}


 
		
		TileEntity tile = world.getTileEntity(i,j,k);
		if (tile != null && tile instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;

			if (upgrade.getMaster() == null) {
				return false;
			}
			
			if (world.isRemote)
			{
				return true;
			}
			

			if (upgrade.useStandardInterface()) {
				FMLNetworkHandler.openGui(entityplayer, StevesVehicles.instance, 3, world, upgrade.getMaster().xCoord, upgrade.getMaster().yCoord, upgrade.getMaster().zCoord);
				return true;
			}
		

			FMLNetworkHandler.openGui(entityplayer, StevesVehicles.instance, 7, world, i, j, k);
		}

		
        return true;
    }

	@Override
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityUpgrade();
    }
}
