package vswe.stevesvehicles.module.common.addon.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import vswe.stevesvehicles.module.common.addon.recipe.ModuleCrafter;

public class CraftingDummy extends InventoryCrafting {

    private int inventoryWidth;
    
    private ModuleCrafter module;
    
    public CraftingDummy(ModuleCrafter module) {
    	super(null, 3, 3);
    	inventoryWidth = 3;

        this.module = module;
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int id) {
        return id >= this.getSizeInventory() ? null : module.getStack(id);
    }

    
    @Override
    public ItemStack getStackInRowAndColumn(int par1, int par2)
    {
        if (par1 >= 0 && par1 < this.inventoryWidth){
            int k = par1 + par2 * this.inventoryWidth;
            return this.getStackInSlot(k);
        }else{
            return null;
        }
    }


    @Override
    public ItemStack getStackInSlotOnClosing(int id) {
    	return null;
    }

    @Override
    public ItemStack decrStackSize(int id, int count){
    	return null;
    }


    @Override
    public void setInventorySlotContents(int id, ItemStack item) {

    }

	public void update() {		
		module.setStack(9, getResult());
	}
	
	public ItemStack getResult() {
		return CraftingManager.getInstance().findMatchingRecipe(this, module.getVehicle().getWorld());
	}
	
	public IRecipe getRecipe() {
        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); ++i) {
            IRecipe irecipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);

            if (irecipe.matches(this, module.getVehicle().getWorld())) {
                return irecipe;
            }
        }

        return null;		
	}

}
