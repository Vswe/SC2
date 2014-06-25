package vswe.stevescarts.old.Modules.Realtimers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.modules.ModuleBase;
import net.minecraftforge.common.IShearable;
public class ModuleFlowerRemover extends ModuleBase {

	public ModuleFlowerRemover(EntityModularCart cart) {
		super(cart);
	}


	//called to update the module's actions. Called by the cart's update code.
		@Override
		public void update() {
			super.update();

			
			
			if (getCart().worldObj.isRemote) {
				bladeangle += getBladeSpindSpeed();
				if (getCart().hasFuel()) {
					bladespeed = Math.min(1F, bladespeed + 0.005F);
				}else{
					bladespeed = Math.max(0F, bladespeed - 0.005F);
				}
				return;
			}

			if (getCart().hasFuel()) {
				if (tick >= getInterval()) {
					tick = 0;
					mownTheLawn();
					shearEntities();
				}else{
					tick++;
				}
			}
		}

		private int tick;
		protected int getInterval() {
			return 70;
		}

		protected int getBlocksOnSide() {
			return 7;
		}

		protected int getBlocksFromLevel() {
			return 1;
		}


	private void mownTheLawn() {
		for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); x++) {
			for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); z++) {
				for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); y++) {
					int x1 = x + getCart().x();
					int y1 = y + getCart().y();
					int z1 = z + getCart().z();
					
					if (isFlower(x1, y1, z1)) {
						Block block = getCart().worldObj.getBlock(x1, y1, z1);
						int m = getCart().worldObj.getBlockMetadata(x1, y1, z1);
						
						if (block != null) {
							addStuff(block.getDrops(getCart().worldObj, x1, y1, z1, m, 0));

			                getCart().worldObj.setBlockToAir(x1, y1, z1);	
						}
					}
					
				}
			}
		}		
	}
	
	private void shearEntities() {
		List entities = getCart().worldObj.getEntitiesWithinAABB(EntityLiving.class, getCart().boundingBox.expand((double)getBlocksOnSide(),getBlocksFromLevel() + 2F, (double)getBlocksOnSide()));
		Iterator itt = entities.iterator();

		while (itt.hasNext())
		{
			EntityLiving target = (EntityLiving)itt.next();

			
			
			if (target instanceof IShearable)  {
				IShearable shearable = (IShearable)target;
			
				if (shearable.isShearable(null, getCart().worldObj, (int)target.posX, (int)target.posY, (int)target.posZ)) {
					addStuff(shearable.onSheared(null, getCart().worldObj, (int)target.posX, (int)target.posY, (int)target.posZ, 0));
				}
			}
		}
		
	}
		
	private boolean isFlower(int x, int y, int z) {
		Block block = getCart().worldObj.getBlock(x, y, z);

        return block != null && block instanceof BlockFlower;
	}
	
	
	private void addStuff(ArrayList<ItemStack> stuff) {
        for (ItemStack iStack : stuff)
        {
            getCart().addItemToChest(iStack);

            if (iStack.stackSize != 0)
            {
                EntityItem entityitem = new EntityItem(getCart().worldObj, getCart().posX, getCart().posY, getCart().posZ , iStack);
                entityitem.motionX = 0;
                entityitem.motionY = 0.15F;
                entityitem.motionZ = 0;
                getCart().worldObj.spawnEntityInWorld(entityitem);
            }
        }		
	}


	private float bladeangle;
	private float bladespeed = 0;
	public float getBladeAngle() {
		return bladeangle;
	}

	public float getBladeSpindSpeed() {
		return bladespeed;
	}
}
