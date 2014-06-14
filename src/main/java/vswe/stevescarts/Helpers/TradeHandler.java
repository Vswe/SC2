package vswe.stevescarts.Helpers;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import vswe.stevescarts.Items.ModItems;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class TradeHandler implements VillagerRegistry.IVillageTradeHandler
{
	public static int santaId = 523;
	
    public TradeHandler()
    {
    	VillagerRegistry.instance().registerVillagerId(santaId);

		VillagerRegistry.instance().registerVillageTradeHandler(santaId,this);		
    }


    public void registerSkin() {
        VillagerRegistry.instance().registerVillagerSkin(santaId, ResourceHelper.getResource("/models/santa.png"));
    }

	/**
	 * Called to allow changing the content of the {@link MerchantRecipeList} for the villager
	 * supplied during creation
	 *
	 * @param villager
	 * @param recipeList
	 */
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(new ItemStack(ModItems.component, 3, 50), new ItemStack(ModItems.component,1,51)));
	}
	

	
}