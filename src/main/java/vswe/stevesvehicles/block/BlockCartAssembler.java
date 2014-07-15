package vswe.stevesvehicles.block;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.network.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.tab.CreativeTabLoader;


public class BlockCartAssembler extends BlockContainerBase {

    public BlockCartAssembler() {
        super(Material.rock);
        setCreativeTab(CreativeTabLoader.blocks);
    }



	private IIcon topIcon;
	private IIcon botIcon;
	private IIcon sideIcons [];
	
    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
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
    public void registerBlockIcons(IIconRegister register) {
        topIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":assembler/top");
		botIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":assembler/bot");
		sideIcons = new IIcon[4];
		for (int i = 1; i <= 4; i++) {
			sideIcons[i-1] = register.registerIcon(StevesVehicles.instance.textureHeader + ":assembler/side_" + i);
		}
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
                    master.onUpgradeUpdate();
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
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileEntityCartAssembler();
    }
	

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		updateMultiBlock(world, x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityCartAssembler) {
            TileEntityCartAssembler assembler = (TileEntityCartAssembler)te;
            assembler.isDead = true;
            updateMultiBlock(world, x, y, z);

            ItemStack outputItem = assembler.getOutputOnInterrupt();
            if (outputItem != null) {
                EntityItem entityItem = new EntityItem(world, (double)x + 0.2F, (double)y + 0.2F, (float)z + 0.2F, outputItem.copy());
                entityItem.motionX = world.rand.nextGaussian() *  0.05F;
                entityItem.motionY = world.rand.nextGaussian() *  0.25F;
                entityItem.motionZ = world.rand.nextGaussian() *  0.05F;


                world.spawnEntityInWorld(entityItem);
            }
        }


        super.breakBlock(world, x, y, z, block, meta);
    }	

	
}
