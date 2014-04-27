package vswe.stevescarts.Items;
import java.util.List;
import vswe.stevescarts.TileEntities.TileEntityBase;

import vswe.stevescarts.Upgrades.AssemblerUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.client.renderer.texture.IconRegister;
public class ItemUpgrade extends ItemBlock
{


    public ItemUpgrade(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int dmg)
    {
		AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(dmg);
		if (upgrade != null) {
			return upgrade.getIcon();
		}
		return null;
    }	
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
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
			return "item." + StevesCarts.instance.localStart + upgrade.getRawName();
		}	
	
        return "item.unknown";
    }	
	
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
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
			TileEntity tile = world.getBlockTileEntity(x,y,z);
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
