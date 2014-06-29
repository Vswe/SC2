package vswe.stevesvehicles.module.cart.attachment;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ModuleBase;
import net.minecraftforge.common.IShearable;

public class ModuleFlowerRemover extends ModuleBase {

	public ModuleFlowerRemover(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    private static final float MAX_BLADE_SPEED = 1F;
    private static final float MIN_BLADE_SPEED = 0F;
    private static final float BLADE_ACCELERATION = 0.005F;

	//called to update the module's actions. Called by the cart's update code.
    @Override
    public void update() {
        super.update();

        if (getVehicle().getWorld().isRemote) {
            bladeAngle += getBladeSpindSpeed();
            if (getVehicle().hasFuel()) {
                bladeSpeed = Math.min(MAX_BLADE_SPEED, bladeSpeed + BLADE_ACCELERATION);
            }else{
                bladeSpeed = Math.max(MIN_BLADE_SPEED, bladeSpeed - BLADE_ACCELERATION);
            }
            return;
        }

        if (getVehicle().hasFuel()) {
            if (tick >= getInterval()) {
                tick = 0;
                mowTheLawn();
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


	private void mowTheLawn() {
		for (int offsetX = -getBlocksOnSide(); offsetX <= getBlocksOnSide(); offsetX++) {
			for (int offsetZ = -getBlocksOnSide(); offsetZ <= getBlocksOnSide(); offsetZ++) {
				for (int offsetY = -getBlocksFromLevel(); offsetY <= getBlocksFromLevel(); offsetY++) {
					int targetX = offsetX + getVehicle().x();
					int targetY = offsetY + getVehicle().y();
					int targetZ = offsetZ + getVehicle().z();
					
					if (isFlower(targetX, targetY, targetZ)) {
						Block block = getVehicle().getWorld().getBlock(targetX, targetY, targetZ);
						int m = getVehicle().getWorld().getBlockMetadata(targetX, targetY, targetZ);
						
						if (block != null) {
							addStuff(block.getDrops(getVehicle().getWorld(), targetX, targetY, targetZ, m, 0));

			                getVehicle().getWorld().setBlockToAir(targetX, targetY, targetZ);
						}
					}
					
				}
			}
		}		
	}
	
	private void shearEntities() {
		List entities = getVehicle().getWorld().getEntitiesWithinAABB(EntityLiving.class, getVehicle().getEntity().boundingBox.expand((double) getBlocksOnSide(), getBlocksFromLevel() + 2F, (double) getBlocksOnSide()));

        for (Object entity : entities) {
            EntityLiving target = (EntityLiving) entity;


            if (target instanceof IShearable) {
                IShearable shearable = (IShearable) target;

                if (shearable.isShearable(null, getVehicle().getWorld(), (int) target.posX, (int) target.posY, (int) target.posZ)) {
                    addStuff(shearable.onSheared(null, getVehicle().getWorld(), (int) target.posX, (int) target.posY, (int) target.posZ, 0));
                }
            }
        }
		
	}
		
	private boolean isFlower(int x, int y, int z) {
		Block block = getVehicle().getWorld().getBlock(x, y, z);

        return block != null && block instanceof BlockBush;
	}
	
	
	private void addStuff(ArrayList<ItemStack> stuff) {
        for (ItemStack item : stuff){
            getVehicle().addItemToChest(item);

            if (item.stackSize != 0) {
                EntityItem entityitem = new EntityItem(getVehicle().getWorld(), getVehicle().getEntity().posX, getVehicle().getEntity().posY, getVehicle().getEntity().posZ , item);
                entityitem.motionX = 0;
                entityitem.motionY = 0.15F;
                entityitem.motionZ = 0;
                getVehicle().getWorld().spawnEntityInWorld(entityitem);
            }
        }		
	}


	private float bladeAngle;
	private float bladeSpeed = 0;
	public float getBladeAngle() {
		return bladeAngle;
	}

	public float getBladeSpindSpeed() {
		return bladeSpeed;
	}
}
