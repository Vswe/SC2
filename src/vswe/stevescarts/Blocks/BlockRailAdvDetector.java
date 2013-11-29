package vswe.stevescarts.Blocks;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
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
public class BlockRailAdvDetector extends BlockRailBase
{
	
	private Icon normalIcon;
	private Icon cornerIcon;

    public BlockRailAdvDetector(int i)
    {
        super(i, false);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
    }
	
	@Override
    public Icon getIcon(int side, int meta)
    {
        return meta >= 6 ? cornerIcon : normalIcon;
    }
	

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
        normalIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "advanced_detector_rail");
		cornerIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "advanced_detector_rail" + "_corner");
    }	

    /*  Return true if the rail can go up and down slopes
     */
    @Override
    public boolean canMakeSlopes(World world, int i, int j, int k)
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
		

	
		if (world.getBlockId(x, y-1, z) == StevesCarts.instance.blockDetector.blockID && DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y-1, z)).canInteractWithCart()) {
			
			TileEntity tileentity = world.getBlockTileEntity(x, y-1, z);
			
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
					
					int blockId = world.getBlockId(x+i, y, z+j);
					if (blockId == StevesCarts.instance.blockCargoManager.blockID || blockId == StevesCarts.instance.blockLiquidManager.blockID)
					{
						TileEntity tileentity = world.getBlockTileEntity(x+i, y, z+j);
						
						if (tileentity != null && tileentity instanceof TileEntityManager) {
							TileEntityManager manager = (TileEntityManager)tileentity;
							if (manager.getCart() == null) {
								manager.setCart(cart);
								manager.setSide(side);
							}	
						}
						
						return;						
					}else if(blockId == StevesCarts.instance.blockActivator.blockID) {
						TileEntity tileentity = world.getBlockTileEntity(x+i, y, z+j);
						
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
						
					}else if(blockId == StevesCarts.instance.blockUpgrade.blockID) {
						TileEntity tileentity = world.getBlockTileEntity(x+i, y, z+j);
						
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
		if (world.getBlockId(i,j-1,k) == StevesCarts.instance.blockDetector.blockID) {
			return StevesCarts.instance.blockDetector.onBlockActivated(world, i, j-1, k, entityplayer, par6, par7, par8, par9);
		}
		
        return false;
    }	
	
}
