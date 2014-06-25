package vswe.stevesvehicles.old.Items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import vswe.stevesvehicles.old.StevesVehicles;

public class ItemBlockDetector extends ItemBlock {

   
    public ItemBlockDetector(Block b)
    {
        super(b);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(StevesVehicles.tabsSC2Blocks);
    }


 	@Override
    public String getUnlocalizedName(ItemStack item)
    {

		if (item != null) {
			return "item." + StevesVehicles.localStart + "BlockDetector" + item.getItemDamage();
		}	
	
        return "item.unknown";
    }	

 	
    @Override
    public int getMetadata(int dmg) {
        return dmg;
    } 	

}
