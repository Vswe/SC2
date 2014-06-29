package vswe.stevesvehicles.module.cart.attachment;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleCleaner extends ModuleBase {
	public ModuleCleaner(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();

		if (getVehicle().getWorld().isRemote) {
			return;
		}

		if (getVehicle().hasFuel()) {
			suck();
		}
		clean();
	}

	private double calculateMotion(double dif) {
		if (dif > -0.5D && dif < 0.5D) {
			return 0;
		}

		return 1 / (dif * 15);
	}

	private void suck() {
		List list = getVehicle().getWorld().getEntitiesWithinAABBExcludingEntity(getVehicle().getEntity(), getVehicle().getEntity().boundingBox.expand(3D, 1D, 3D));

        for (Object obj : list) {
            if (obj instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) obj;
                if (entityItem.delayBeforeCanPickup <= 10) {
                    double difX = getVehicle().getEntity().posX - entityItem.posX;
                    double difY = getVehicle().getEntity().posY - entityItem.posY;
                    double difZ = getVehicle().getEntity().posZ - entityItem.posZ;

                    entityItem.motionX += calculateMotion(difX);
                    entityItem.motionY += calculateMotion(difY);
                    entityItem.motionZ += calculateMotion(difZ);
                }
            }
        }
	}

	private void clean() {
		List list = getVehicle().getWorld().getEntitiesWithinAABBExcludingEntity(getVehicle().getEntity(), getVehicle().getEntity().boundingBox.expand(1D, 0.5D, 1D));

        for (Object obj : list) {
            if (obj instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) obj;

                if (entityItem.delayBeforeCanPickup <= 10 && !entityItem.isDead) {
                    int stackSize = entityItem.getEntityItem().stackSize;
                    getVehicle().addItemToChest(entityItem.getEntityItem());

                    if (stackSize != entityItem.getEntityItem().stackSize) {
                        getVehicle().getWorld().playSoundAtEntity(getVehicle().getEntity(), "random.pop", 0.2F, ((getVehicle().getRandom().nextFloat() - getVehicle().getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

                        if (entityItem.getEntityItem().stackSize <= 0) {
                            entityItem.setDead();
                        }
                    } else {
                        if (failPickup(entityItem.getEntityItem())) {
                            entityItem.setDead();
                        }
                    }
                }
            } else if (obj instanceof EntityArrow) {
                EntityArrow entityArrow = (EntityArrow) obj;

                if (Math.pow(entityArrow.motionX, 2) + Math.pow(entityArrow.motionY, 2) + Math.pow(entityArrow.motionZ, 2) < 0.2D && entityArrow.arrowShake <= 0 && !entityArrow.isDead) {
                    entityArrow.arrowShake = 3;
                    ItemStack iItem = new ItemStack(Items.arrow, 1);
                    getVehicle().addItemToChest(iItem);

                    if (iItem.stackSize <= 0) {
                        getVehicle().getWorld().playSoundAtEntity(getVehicle().getEntity(), "random.pop", 0.2F, ((getVehicle().getRandom().nextFloat() - getVehicle().getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        entityArrow.setDead();
                    } else {
                        if (failPickup(iItem)) {
                            entityArrow.setDead();
                        }
                    }
                }
            }
        }
	}

	private boolean failPickup(ItemStack item) {
		int x = normalize(((EntityModularCart)getVehicle().getEntity()).pushZ);
		int z = normalize(((EntityModularCart)getVehicle().getEntity()).pushX);

		if (x == 0 && z == 0) {
			return false;
		}

		EntityItem entityitem = new EntityItem(getVehicle().getWorld(), getVehicle().getEntity().posX, getVehicle().getEntity().posY, getVehicle().getEntity().posZ , item.copy());
		entityitem.delayBeforeCanPickup = 35;

		entityitem.motionX = x / 3F;
		entityitem.motionY = 0.15F;
		entityitem.motionZ = z / 3F;
		getVehicle().getWorld().spawnEntityInWorld(entityitem);

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