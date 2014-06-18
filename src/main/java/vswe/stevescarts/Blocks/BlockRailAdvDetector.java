package vswe.stevescarts.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.TileEntities.TileEntityActivator;
import vswe.stevescarts.TileEntities.TileEntityDetector;
import vswe.stevescarts.TileEntities.TileEntityManager;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;
import vswe.stevescarts.Upgrades.Disassemble;
import vswe.stevescarts.Upgrades.Transposer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class BlockRailAdvDetector extends BlockSpecialRailBase
{
	
	private IIcon normalIcon;
	private IIcon cornerIcon;

    public BlockRailAdvDetector()
    {
        super(false);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
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
        normalIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "advanced_detector_rail");
		cornerIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "advanced_detector_rail" + "_corner");
    }	

    /*  Return true if the rail can go up and down slopes
     */
    @Override
    public boolean canMakeSlopes(IBlockAccess world, int i, int j, int k)
    {
        return false;
    }


	@Override
	public void onMinecartPass(World world, EntityMinecart Minecart, int x, int y, int z)
    {
		if (world.isRemote || !(Minecart instanceof MinecartModular)) {
			return;
		}
	
	
		MinecartModular cart = (MinecartModular)Minecart;
		

	
		if (world.getBlock(x, y - 1, z) == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y-1, z)).canInteractWithCart()) {
			
			TileEntity tileentity = world.getTileEntity(x, y - 1, z);
			
			if (tileentity != null && tileentity instanceof TileEntityDetector) {
				TileEntityDetector detector = (TileEntityDetector)tileentity;

				detector.handleCart(cart);
			}
			return;				
		}
		
		if (!isCartReadyForAction(cart,x,y,z)) {
			return;
		}
		
	
		int side = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (Math.abs(i) != Math.abs(j)) {
					
					Block block = world.getBlock(x + i, y, z + j);
					if (block == ModBlocks.CARGO_MANAGER.getBlock() || block == ModBlocks.LIQUID_MANAGER.getBlock())
					{
						TileEntity tileentity = world.getTileEntity(x+i, y, z+j);
						
						if (tileentity != null && tileentity instanceof TileEntityManager) {
							TileEntityManager manager = (TileEntityManager)tileentity;
							if (manager.getCart() == null) {
								manager.setCart(cart);
								manager.setSide(side);
							}	
						}
						
						return;						
					}else if(block == ModBlocks.MODULE_TOGGLER.getBlock()) {
						TileEntity tileentity = world.getTileEntity(x+i, y, z+j);
						
						if (tileentity != null && tileentity instanceof TileEntityActivator) {
							TileEntityActivator activator = (TileEntityActivator)tileentity;

						
						
							boolean isOrange = false;
						
							if ((cart.temppushX == 0) == (cart.temppushZ == 0)) {
								continue;
							}
							
							if (i == 0) {
								if (j == -1) {
									isOrange = cart.temppushX < 0;
								}else{
									isOrange = cart.temppushX > 0;
								}
							
							}else if(j == 0) {
								if (i == -1) {
									isOrange = cart.temppushZ > 0;
								}else{
									isOrange = cart.temppushZ < 0;
								}						
							}
							boolean isBlueBerry = false;
							activator.handleCart(cart, isOrange);
							cart.releaseCart();							
						}
						
						
						return;
						
					}else if(block == ModBlocks.UPGRADE.getBlock()) {
						TileEntity tileentity = world.getTileEntity(x+i, y, z+j);
						
						TileEntityUpgrade upgrade = (TileEntityUpgrade)tileentity;
						if(upgrade != null && upgrade.getUpgrade() != null) {
							for (BaseEffect effect : upgrade.getUpgrade().getEffects()) {
								if (effect instanceof Transposer) {
									Transposer transposer = (Transposer)effect;
									if (upgrade.getMaster() != null) {
										for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
											if (tile.getUpgrade() != null) {
												for (BaseEffect effect2 : tile.getUpgrade().getEffects()) {
													if (effect2 instanceof Disassemble) {
														Disassemble disassembler = (Disassemble)effect2;
														if (tile.getStackInSlot(0) == null) {
															tile.setInventorySlotContents(0, ModuleData.createModularCart(cart));
															upgrade.getMaster().managerInteract(cart, false);
															for (int p = 0; p < cart.getSizeInventory(); p++) {
																ItemStack item = cart.getStackInSlotOnClosing(p);
																if (item != null) {
																	upgrade.getMaster().puke(item);
																}
															}															
															cart.setDead();
															return;
														}
													}
												}
											}
										}
									}									
								}
							}							
						}
					}

					side++;
				}
			}			
		}
		
		

		boolean receivesPower = world.isBlockIndirectlyGettingPowered(x, y, z);			
		if(receivesPower){
			cart.releaseCart();
		}			

    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        //check if any block is using this detector for something else

        if (world.getBlock(x, y-1, z) == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y-1, z)).canInteractWithCart()) {
            return false;
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) != Math.abs(j)) {

                    Block block = world.getBlock(x+i, y, z+j);
                    if (block == ModBlocks.CARGO_MANAGER.getBlock() || block == ModBlocks.LIQUID_MANAGER.getBlock() || block == ModBlocks.MODULE_TOGGLER.getBlock()) {
                        return false;
                    }else if(block == ModBlocks.UPGRADE.getBlock()) {
                        TileEntity tileentity = world.getTileEntity(x+i, y, z+j);

                        TileEntityUpgrade upgrade = (TileEntityUpgrade)tileentity;
                        if(upgrade != null && upgrade.getUpgrade() != null) {
                            for (BaseEffect effect : upgrade.getUpgrade().getEffects()) {
                                if (effect instanceof Transposer) {
                                    if (upgrade.getMaster() != null) {
                                        for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
                                            if (tile.getUpgrade() != null) {
                                                for (BaseEffect effect2 : tile.getUpgrade().getEffects()) {
                                                    if (effect2 instanceof Disassemble) {
                                                        return false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //if nothing else used this activator it can be controlled by redstone
        return true;
    }
	
	private boolean isCartReadyForAction(MinecartModular cart, int x, int y, int z) {
		if ((int)cart.disabledX == x || (int)cart.disabledY == y || (int)cart.disabledZ == z)
		{
			return cart.isDisabled();		
		}
		
		return false;
	}


	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
		if (world.getBlock(i,j-1,k) == ModBlocks.DETECTOR_UNIT.getBlock()) {
			return ModBlocks.DETECTOR_UNIT.getBlock().onBlockActivated(world, i, j-1, k, entityplayer, par6, par7, par8, par9);
		}
		
        return false;
    }	
	
}
