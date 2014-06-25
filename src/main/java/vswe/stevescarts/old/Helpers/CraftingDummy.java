package vswe.stevescarts.old.Helpers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import vswe.stevescarts.old.Modules.Addons.ModuleCrafter;

public class CraftingDummy extends InventoryCrafting
{

    private int inventoryWidth;
    
    private ModuleCrafter module;
    
    public CraftingDummy(ModuleCrafter module) {
    	super(null, 3, 3);
    	inventoryWidth = 3;

        this.module = module;
    }

    @Override
    public int getSizeInventory()
    {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : module.getStack(par1);
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
    public ItemStack getStackInSlotOnClosing(int par1) {
    	return null;
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
    	return null;
    }


    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        return;
    }

	public void update() {		
		module.setStack(9, getResult());
	}
	
	public ItemStack getResult() {
		return CraftingManager.getInstance().findMatchingRecipe(this, module.getCart().worldObj);
	}
	
	public IRecipe getRecipe() {
        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); ++i)
        {
            IRecipe irecipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);

            if (irecipe.matches(this, module.getCart().worldObj))
            {
                return irecipe;
            }
        }

        return null;		
	}

}
