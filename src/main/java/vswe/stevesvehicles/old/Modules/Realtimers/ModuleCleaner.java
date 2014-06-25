package vswe.stevesvehicles.old.Modules.Realtimers;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.modules.ModuleBase;

public class ModuleCleaner extends ModuleBase {
	public ModuleCleaner(EntityModularCart cart) {
		super(cart);
	}

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();

		if (getCart().worldObj.isRemote) {
			return;
		}

		if (getCart().hasFuel()) {
			suck();
		}
		clean();
	}

	private double  calculatemotion(double dif) {
		if (dif > -0.5D && dif < 0.5D) {
			return 0;
		}

		return 1 / (dif * 15);
	}

	private void suck() {
		List list = getCart().worldObj.getEntitiesWithinAABBExcludingEntity(getCart(), getCart().boundingBox.expand(3D, 1D, 3D));

		for (int e = 0; e < list.size(); e++)
        {
            if (list.get(e) instanceof EntityItem)
            {
				EntityItem eItem = (EntityItem)list.get(e);
				if (eItem.delayBeforeCanPickup <= 10)
				{
					double difX = getCart().posX - eItem.posX;
					double difY = getCart().posY - eItem.posY;
					double difZ = getCart().posZ - eItem.posZ;

					eItem.motionX += calculatemotion(difX);
					eItem.motionY += calculatemotion(difY);
					eItem.motionZ += calculatemotion(difZ);
				}
			}
		}
	}

	private void clean() {
		List list = getCart().worldObj.getEntitiesWithinAABBExcludingEntity(getCart(), getCart().boundingBox.expand(1D, 0.5D, 1D));

        for (int e = 0; e < list.size(); e++)
        {
            if (list.get(e) instanceof EntityItem)
            {
                EntityItem eItem = (EntityItem)list.get(e);

                if (eItem.delayBeforeCanPickup <= 10 && !eItem.isDead)
				{
                    int stackSize = eItem.getEntityItem().stackSize;
                    getCart().addItemToChest(eItem.getEntityItem());

                    if (stackSize != eItem.getEntityItem().stackSize)
                    {
                        getCart().worldObj.playSoundAtEntity(getCart(), "random.pop", 0.2F, ((getCart().rand.nextFloat() - getCart().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

                        if (eItem.getEntityItem().stackSize <= 0)
                        {
                            eItem.setDead();
                        }
                    }
                    else
                    {
					   if (failPickup(eItem.getEntityItem())) {
							eItem.setDead();
					   }
                    }
                }
            }
            else if (list.get(e) instanceof EntityArrow)
            {
                EntityArrow eItem = (EntityArrow)list.get(e);

                if (Math.pow(eItem.motionX,2) + Math.pow(eItem.motionY,2) + Math.pow(eItem.motionZ,2) < 0.2D && eItem.arrowShake <= 0 && !eItem.isDead)
                {
                    eItem.arrowShake = 3;
                    ItemStack iItem = new ItemStack(Items.arrow, 1);
                    getCart().addItemToChest(iItem);

                    if (iItem.stackSize <= 0)
                    {
                        getCart().worldObj.playSoundAtEntity(getCart(), "random.pop", 0.2F, ((getCart().rand.nextFloat() - getCart().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						eItem.setDead();
				   }else{
						if (failPickup(iItem)) {
							eItem.setDead();
						}
					}
                }
            }
        }
	}

	private boolean failPickup(ItemStack item) {
		int x = normalize(getCart().pushZ);
		int z = normalize(getCart().pushX);

		if (x == 0 && z == 0) {
			return false;
		}else if (getCart().worldObj.isRemote) {
			//return true;
		}

		EntityItem entityitem = new EntityItem(getCart().worldObj, getCart().posX, getCart().posY, getCart().posZ , item.copy());
		entityitem.delayBeforeCanPickup = 35;

		entityitem.motionX = x / 3F;
		entityitem.motionY = 0.15F;
		entityitem.motionZ = z / 3F;
		getCart().worldObj.spawnEntityInWorld(entityitem);

		return true;
	}

	private int normalize(double val) {
		if (val == 0) {
			return 0;
		}else if (val > 0) {
			return 1;
		}else{
			return -1;
		}
	}

}