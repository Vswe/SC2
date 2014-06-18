package vswe.stevescarts.Blocks;
import java.util.ArrayList;

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
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import vswe.stevescarts.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class BlockCartAssembler extends BlockContainerBase
{


    public BlockCartAssembler()
    {
        super(Material.rock);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
    }



	private IIcon topIcon;
	private IIcon botIcon;
	private IIcon sideIcons [];
	
    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta)
    {
        if (side == 0) {
			return botIcon;
		}else if(side == 1) {
			return topIcon;
		}else {
			return sideIcons[side - 2];
		}
    }
	
    @SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister register)
    {
        topIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cart_assembler" + "_top");
		botIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cart_assembler" + "_bot");
		sideIcons = new IIcon[4];
		for (int i = 1; i <= 4; i++) {
			sideIcons[i-1] = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "cart_assembler" + "_side_" + i);
		}
    }
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
		if (entityplayer.isSneaking()) {
			return false;
		}
	
		TileEntityCartAssembler assembler = (TileEntityCartAssembler)world.getTileEntity(x, y, z);
		if (assembler != null) {
		    if (!world.isRemote)
			{
				FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 3, world, x, y, z);								 
			}
			
			
		}else{
			return false;
		}
        return true;
    }
	

	public void updateMultiBlock(World world, int x, int y, int z) {

		TileEntityCartAssembler master = (TileEntityCartAssembler)world.getTileEntity(x, y, z);
		if (master != null) {
			master.clearUpgrades();
		}
		checkForUpgrades(world, x, y, z);
		if (!world.isRemote) {
			PacketHandler.sendBlockInfoToClients(world, new byte[] {}, x, y, z);
		}
		if (master != null) {
			master.onUpgradeUpdate();
		}
	}

	
	private void checkForUpgrades(World world, int x, int y, int z) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (Math.abs(i) + Math.abs(j) + Math.abs(k) == 1) {
						checkForUpgrade(world, x+i, y+j, z+k);
					}					
				}		
			}		
		}
	}
	
	private TileEntityCartAssembler checkForUpgrade(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
			ArrayList<TileEntityCartAssembler> masters = getMasters(world, x, y, z);
			if (masters.size() == 1) {
				TileEntityCartAssembler master = masters.get(0);
				master.addUpgrade(upgrade);
				upgrade.setMaster(master);
				return master;
			}else{
				for (TileEntityCartAssembler master : masters) {
					master.removeUpgrade(upgrade);
				}
				upgrade.setMaster(null);
			}						
		}
		return null;
	}
	
	private ArrayList<TileEntityCartAssembler> getMasters(World world, int x, int y, int z) {
		ArrayList<TileEntityCartAssembler> masters = new ArrayList<TileEntityCartAssembler>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (Math.abs(i) + Math.abs(j) + Math.abs(k) == 1) {
						TileEntityCartAssembler temp = getMaster(world, x+i, y+j, z+k);
						if (temp != null) {
							masters.add(temp);
						}
					}					
				}		
			}		
		}
		return masters;
	}
	
	private TileEntityCartAssembler getValidMaster(World world, int x, int y, int z) {
		TileEntityCartAssembler master = null;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (Math.abs(i) + Math.abs(j) + Math.abs(k) == 1) {
						TileEntityCartAssembler temp = getMaster(world, x+i, y+j, z+k);
						if (temp != null) {
							if (master != null) {
								return null;
							}else{
								master = temp;
							}
						}
					}					
				}		
			}		
		}
		return master;
	}	
	
	private TileEntityCartAssembler getMaster(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityCartAssembler) {
			TileEntityCartAssembler master = (TileEntityCartAssembler)tile;
			
			if (!master.isDead) {
				return master;
			}
		}
		return null;
	}
	
	public void addUpgrade(World world, int x, int y, int z) {

		TileEntityCartAssembler master = getValidMaster(world, x, y, z);
		if (master != null) {
			updateMultiBlock(world, master.xCoord, master.yCoord, master.zCoord);
		}
	}
	
	public void removeUpgrade(World world, int x, int y, int z) {
		
		TileEntityCartAssembler master = getValidMaster(world, x, y, z);
		if (master != null) {
			updateMultiBlock(world, master.xCoord, master.yCoord, master.zCoord);
		}
	}	
	

	@Override
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityCartAssembler();
    }
	

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		updateMultiBlock(world, x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block unknown1, int unknown2)
    {
		
	
        TileEntityCartAssembler var7 = (TileEntityCartAssembler)world.getTileEntity(x, y, z);
		var7.isDead = true;
		updateMultiBlock(world, x, y, z);
        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlotOnClosing(var8);

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
       

			ItemStack outputItem = var7.getOutputOnInterupt();
			if (outputItem != null) {
				EntityItem eItem = new EntityItem(world, (double)x + 0.2F, (double)y + 0.2F, (float)z + 0.2F, outputItem);
				eItem.motionX = (double)((float)world.rand.nextGaussian() *  0.05F);
				eItem.motionY = (double)((float)world.rand.nextGaussian() *  0.25F);
				eItem.motionZ = (double)((float)world.rand.nextGaussian() *  0.05F);

				if (outputItem.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)outputItem.getTagCompound().copy());
				}		
				world.spawnEntityInWorld(eItem);
			}
			
		}
        super.breakBlock(world, x, y, z, unknown1, unknown2);
    }	
	

		
	
}
