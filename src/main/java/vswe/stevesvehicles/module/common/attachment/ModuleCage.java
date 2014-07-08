package vswe.stevesvehicles.module.common.attachment;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationTravel;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleCage extends ModuleAttachment implements IActivatorModule {
	public ModuleCage(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public boolean hasSlots() {
		return false;
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return 80;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

    private static final int TEXTURE_SPACING = 1;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/cage.png");

		drawButton(gui, x, y, AUTO_RECT, disablePickup ? 2 : 3);
		drawButton(gui, x, y, MANUAL_RECT, isCageEmpty() ? 0 : 1);
	}

	private void drawButton(GuiVehicle gui, int x, int y, int[] coordinates, int imageId) {
		if (inRect(x,y, coordinates)) {
			drawImage(gui,coordinates, TEXTURE_SPACING, TEXTURE_SPACING * 2 + coordinates[3]);
		}else{
			drawImage(gui,coordinates, TEXTURE_SPACING, TEXTURE_SPACING);
		}

		int srcY = (TEXTURE_SPACING + coordinates[3]) * 2 + imageId * (TEXTURE_SPACING + coordinates[3] - 2);
		drawImage(gui, coordinates[0] + 1, coordinates[1] + 1, 0, srcY, coordinates[2] - 2, coordinates[3] - 2);
	}	
	
	private static final int[] AUTO_RECT = new int[] {15, 20, 24, 12};
	private static final int[] MANUAL_RECT = new int[] {AUTO_RECT[0] + AUTO_RECT[2] + 5, AUTO_RECT[1], AUTO_RECT[2], AUTO_RECT[3]};


	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, LocalizationTravel.CAGE_AUTO_MESSAGE.translate(disablePickup ? "0" : "1"), x, y, AUTO_RECT);
		drawStringOnMouseOver(gui, LocalizationTravel.CAGE_MESSAGE.translate(isCageEmpty() ? "0" : "1"), x, y, MANUAL_RECT);
	}

	private boolean isCageEmpty() {		
		return getVehicle().getEntity().riddenByEntity == null;
	}


	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, AUTO_RECT)) {
				sendPacket(0);
			}else if (inRect(x,y, MANUAL_RECT)) {
				sendPacket(1);
			}
		}
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if  (id == 0) {
			disablePickup = !disablePickup;
		}else if (id == 1) {
			if (!isCageEmpty()) {
				manualDrop();
			}else{
				manualPickUp();
			}		
		}
	}

	@Override
	public int numberOfPackets() {
		return 2;
	}

    private int cooldown = 0;
    private static int PICK_UP_COOLDOWN = 20;

	@Override
	public void update() {
		super.update();
		
		if (cooldown > 0) {
			cooldown--;
		}else if(!disablePickup) {
			pickUpCreature(2);
			cooldown = PICK_UP_COOLDOWN;
		}
	}

	private void manualDrop() {
		if (!isCageEmpty()) {
			getVehicle().getEntity().riddenByEntity.mountEntity(null);
			cooldown = PICK_UP_COOLDOWN;
		}
	}
	
	private void manualPickUp() {
		pickUpCreature(5);
	}

	private EntityNearestTarget sorter = new EntityNearestTarget(getVehicle().getEntity());
	private void pickUpCreature(int searchDistance) {
		if (getVehicle().getWorld().isRemote || !isCageEmpty()) {
			return;
		}
		
		List entities = getVehicle().getWorld().getEntitiesWithinAABB(EntityLivingBase.class, getVehicle().getEntity().boundingBox.expand((double) searchDistance, 4.0D, (double) searchDistance));
		Collections.sort(entities, sorter);

        for (Object entity : entities) {
            EntityLivingBase target = (EntityLivingBase) entity;


            if (target instanceof EntityPlayer ||
                    target instanceof EntityIronGolem ||
                    target instanceof EntityDragon ||
                    target instanceof EntitySlime ||
                    target instanceof EntityWaterMob ||
                    target instanceof EntityWither ||
                    target instanceof EntityEnderman ||
                    (target instanceof EntitySpider && !(target instanceof EntityCaveSpider)) ||
                    target instanceof EntityGiantZombie ||
                    target instanceof EntityFlying ||
                    (target instanceof EntitySkeleton && ((EntitySkeleton) target).getSkeletonType() == 1)) {

                continue;
            }


            if (target.ridingEntity == null) {
                target.mountEntity(getVehicle().getEntity());
                return;
            }
        }

	}

	



	
	@Override
	public float mountedOffset(Entity rider) {
		if (rider instanceof EntityBat) {
			return 0.5F;
		}else if(rider instanceof EntityZombie || rider instanceof EntitySkeleton) {
			return -0.75F;
		}
		
		return super.mountedOffset(rider);
	}
	
	@Override
	public int numberOfGuiData() {
		return 1;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {		
		updateGuiData(info, 0, (byte)(disablePickup ? 1 : 0));
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			disablePickup = data != 0;
		}
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("disablePickup", disablePickup);
	}	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		disablePickup = tagCompound.getBoolean("disablePickup");
	}		
	
	private boolean disablePickup;
	
	@Override
	public boolean isActive(int id) {
		if (id == 0) {
			return !disablePickup;
		}else{
			return !isCageEmpty();
		}
	}
	@Override
	public void doActivate(int id) {
		if (id == 0) {
			disablePickup = false;
		}else{
			manualPickUp();
		}
	}
	@Override
	public void doDeActivate(int id) {
		if (id == 0) {
			disablePickup = true;
		}else{
			manualDrop();
		}
	}

    private static class EntityNearestTarget implements Comparator {
        private Entity entity;

        public EntityNearestTarget(Entity entity) {
            this.entity = entity;
        }

        public int compareDistanceSq(Entity entity1, Entity entity2) {
            double distance1 = this.entity.getDistanceSqToEntity(entity1);
            double distance2 = this.entity.getDistanceSqToEntity(entity2);
            return distance1 < distance2 ? -1 : distance1 > distance2 ? 1 : 0;
        }

        public int compare(Object obj1, Object obj2) {
            return this.compareDistanceSq((Entity)obj1, (Entity)obj2);
        }
    }
}