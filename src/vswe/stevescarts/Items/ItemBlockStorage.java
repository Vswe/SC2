package vswe.stevescarts.Items;
import java.util.List;

import vswe.stevescarts.Helpers.StorageBlock;
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
public class ItemBlockStorage extends ItemBlock
{

	public static StorageBlock[] blocks;
	
	public static void init() {
		blocks = new StorageBlock[] {
			new StorageBlock("Reinforced Metal Block", new ItemStack(Items.component, 1, 22)),
			new StorageBlock("Galgadorian Block", new ItemStack(Items.component, 1, 47)),
			new StorageBlock("Enhanced Galgadorian Block", new ItemStack(Items.component, 1, 49)),
		};		
	}
	
	public static void loadRecipes() {
		for (int i = 0; i < blocks.length; i++) {
			blocks[i].loadRecipe(i);
		}
	}
	
    public Icon[] icons;
    
    public ItemBlockStorage(int i)
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
    	dmg %= icons.length;
    	
    	return icons[dmg];
    }	
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
    	icons = new Icon[blocks.length];
    	for (int i = 0; i < icons.length; i++) {
    		icons[i] = register.registerIcon(StevesCarts.instance.textureHeader + ":" + blocks[i].getName().replace(":","").replace(" ","_").toLowerCase());	
    	}
    }	

	public String getName(ItemStack item)
    {
		if (item == null) {
			return "Unknown";
		}else{
			int dmg = item.getItemDamage();
			dmg %= blocks.length;
			return blocks[dmg].getName();
		}
    }

 	@Override
    public String getUnlocalizedName(ItemStack item)
    {

		if (item != null) {
			return "item." + StevesCarts.instance.localStart + "BlockStorage" + item.getItemDamage();
		}	
	
        return "item.unknown";
    }	
	
 	 @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int id, CreativeTabs tab, List items)
    {
        for (int i = 0; i < blocks.length; i++)
        {
            items.add(new ItemStack(id, 1, i));
        }
    }
    
    @Override
    public int getMetadata(int dmg)
    {
        return dmg;
    }   
	

}
