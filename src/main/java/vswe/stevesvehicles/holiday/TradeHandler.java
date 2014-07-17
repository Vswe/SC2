package vswe.stevesvehicles.holiday;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.item.ComponentTypes;
import cpw.mods.fml.common.registry.VillagerRegistry;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;

public class TradeHandler implements VillagerRegistry.IVillageTradeHandler {
	public static int SANTA_ID = 523;
	
    public TradeHandler() {
    	VillagerRegistry.instance().registerVillagerId(SANTA_ID);

		VillagerRegistry.instance().registerVillageTradeHandler(SANTA_ID,this);
    }


    public void registerSkin() {
        VillagerRegistry.instance().registerVillagerSkin(SANTA_ID, ResourceHelper.getResource("/models/santa.png"));
    }


	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(ComponentTypes.STOLEN_PRESENT.getItemStack(3), ComponentTypes.GREEN_WRAPPING_PAPER.getItemStack()));
	}
	

	
}