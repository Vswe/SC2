package vswe.stevesvehicles.old.Items;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.old.Upgrades.AssemblerUpgrade;
import vswe.stevesvehicles.old.Upgrades.BaseEffect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
public class ItemUpgrade extends ItemBlock
{


    public ItemUpgrade(Block block)
    {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(StevesVehicles.tabsSC2Blocks);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg)
    {
		AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(dmg);
		if (upgrade != null) {
			return upgrade.getIcon();
		}
		return null;
    }	
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
		for (AssemblerUpgrade upgrade : AssemblerUpgrade.getUpgradesList()) {
			upgrade.createIcon(register);
		}
      
		AssemblerUpgrade.initSides(register);
    }	

	public String getName(ItemStack item)
    {
		AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(item.getItemDamage());
		if (upgrade != null) {
			return upgrade.getName();
		}	
	
        return "Unknown";
    }

 	@Override
    public String getUnlocalizedName(ItemStack item)
    {
		AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(item.getItemDamage());
		if (upgrade != null) {
			return "item." + StevesVehicles.localStart + upgrade.getRawName();
		}	
	
        return "item.unknown";
    }	
	
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for (AssemblerUpgrade upgrade : AssemblerUpgrade.getUpgradesList()) {
			ItemStack iStack = new ItemStack(par1, 1, upgrade.getId());
			par3List.add(iStack);
        }
    }

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
		
	
		if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
			TileEntity tile = world.getTileEntity(x,y,z);
			if (tile != null && tile instanceof TileEntityUpgrade) {
				TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
				upgrade.setType(stack.getItemDamage());
			}
			
			return true;
		}
       return false;
    }	
	
   @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(par1ItemStack.getItemDamage());
		if (upgrade != null) {
			for (BaseEffect effect : upgrade.getEffects()) {
				par3List.add(effect.getName());
			}
		}
	}		

}
