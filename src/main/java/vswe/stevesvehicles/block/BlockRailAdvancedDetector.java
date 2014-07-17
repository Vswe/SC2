package vswe.stevesvehicles.block;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.tab.CreativeTabLoader;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.tileentity.detector.DetectorType;
import vswe.stevesvehicles.tileentity.TileEntityActivator;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import vswe.stevesvehicles.tileentity.TileEntityManager;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;
import vswe.stevesvehicles.upgrade.effect.assembly.Disassemble;
import vswe.stevesvehicles.upgrade.effect.external.Transposer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRailAdvancedDetector extends BlockSpecialRailBase {
	
	private IIcon normalIcon;
	private IIcon cornerIcon;

    public BlockRailAdvancedDetector() {
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
        normalIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":rails/detector");
		cornerIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":rails/detector_corner");
    }	


    @Override
    public boolean canMakeSlopes(IBlockAccess world, int x, int y, int z) {
        return false;
    }


	@Override
	public void onMinecartPass(World world, EntityMinecart Minecart, int x, int y, int z) {
		if (world.isRemote || !(Minecart instanceof EntityModularCart)) {
			return;
		}
	
	
		EntityModularCart cart = (EntityModularCart)Minecart;
		

	
		if (world.getBlock(x, y - 1, z) == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y-1, z)).canInteractWithCart()) {
			
			TileEntity tileentity = world.getTileEntity(x, y - 1, z);
			
			if (tileentity != null && tileentity instanceof TileEntityDetector) {
				TileEntityDetector detector = (TileEntityDetector)tileentity;

				detector.handleCart(cart.getVehicle());
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

							activator.handleCart(cart, isOrange);
							cart.releaseCart();							
						}
						
						
						return;
						
					}else if(block == ModBlocks.UPGRADE.getBlock()) {
						TileEntity tileentity = world.getTileEntity(x+i, y, z+j);
						
						TileEntityUpgrade upgrade = (TileEntityUpgrade)tileentity;
						if(upgrade != null && upgrade.getEffects() != null) {
							for (BaseEffect effect : upgrade.getEffects()) {
								if (effect instanceof Transposer) {
									if (upgrade.getMaster() != null) {
										for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
											if (tile.getEffects() != null) {
												for (BaseEffect effect2 : tile.getEffects()) {
													if (effect2 instanceof Disassemble) {
														if (tile.getStackInSlot(0) == null) {
															tile.setInventorySlotContents(0, ModuleDataItemHandler.createModularVehicle(cart.getVehicle()));
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
                        if(upgrade != null && upgrade.getEffects() != null) {
                            for (BaseEffect effect : upgrade.getEffects()) {
                                if (effect instanceof Transposer) {
                                    if (upgrade.getMaster() != null) {
                                        for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
                                            if (tile.getEffects() != null) {
                                                for (BaseEffect effect2 : tile.getEffects()) {
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
	
	private boolean isCartReadyForAction(EntityModularCart cart, int x, int y, int z) {
        return (cart.disabledX == x || cart.disabledY == y || cart.disabledZ == z) && cart.getVehicle().isDisabled();

    }


	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return world.getBlock(x, y - 1, z) == ModBlocks.DETECTOR_UNIT.getBlock() && ModBlocks.DETECTOR_UNIT.getBlock().onBlockActivated(world, x, y - 1, z, player, side, hitX, hitY, hitZ);
    }


    public void refreshState(World world, int x, int y, int z, boolean flag) {
        new Rail(world, x, y, z).func_150655_a(flag, false);
    }
}
