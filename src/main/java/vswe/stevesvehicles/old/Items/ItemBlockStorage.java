package vswe.stevesvehicles.old.Items;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.old.Helpers.StorageBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.StevesCarts;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class ItemBlockStorage extends ItemBlock
{

	public static StorageBlock[] blocks;
	
	public static void init() {
		blocks = new StorageBlock[] {
			new StorageBlock("Reinforced Metal Block", ComponentTypes.REINFORCED_METAL.getItemStack()),
			new StorageBlock("Galgadorian Block", ComponentTypes.GALGADORIAN_METAL.getItemStack()),
			new StorageBlock("Enhanced Galgadorian Block", ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack()),
		};		
	}
	
	public static void loadRecipes() {
		for (int i = 0; i < blocks.length; i++) {
			blocks[i].loadRecipe(i);
		}
	}
	
    public IIcon[] icons;
    
    public ItemBlockStorage(Block block)
    {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(StevesCarts.tabsSC2Blocks);		
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg)
    {
    	dmg %= icons.length;
    	
    	return icons[dmg];
    }	
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
    	icons = new IIcon[blocks.length];
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
    public void getSubItems(Item item, CreativeTabs tab, List items)
    {
        for (int i = 0; i < blocks.length; i++)
        {
            items.add(new ItemStack(item, 1, i));
        }
    }
    
    @Override
    public int getMetadata(int dmg)
    {
        return dmg;
    }   
	

}
