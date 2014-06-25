package vswe.stevescarts.old.Modules.Realtimers;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Modules.IActivatorModule;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleCage extends ModuleBase implements IActivatorModule {
	public ModuleCage(EntityModularCart cart) {
		super(cart);
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
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/cage.png");

		drawButton(gui, x, y, autoRect, disablePickup ? 2 : 3);
		drawButton(gui, x, y, manualRect, isCageEmpty() ? 0 : 1);
	}

	private void drawButton(GuiMinecart gui, int x, int y, int[] coords, int imageID) {
		if (inRect(x,y, coords)) {
			drawImage(gui,coords, 0, coords[3]);
		}else{
			drawImage(gui,coords, 0, 0);
		}

		int srcY = coords[3] * 2 + imageID * (coords[3] - 2);
		drawImage(gui, coords[0] + 1, coords[1] + 1, 0, srcY, coords[2] - 2, coords[3] - 2);
	}	
	
	private int[] autoRect = new int[] {15,20, 24, 12};
	private int[] manualRect = new int[] {autoRect[0] + autoRect[2] + 5,autoRect[1], autoRect[2], autoRect[3]};


	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAGE_AUTO.translate(disablePickup ? "0" : "1"), x,y, autoRect);
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAGE.translate(isCageEmpty() ? "0" : "1"), x,y, manualRect);
	}

	private boolean isCageEmpty() {		
		return getCart().riddenByEntity == null;
	}


	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, autoRect)) {
				sendPacket(0);
			}else if (inRect(x,y, manualRect)) {
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

	@Override
	public void update() {
		super.update();
		
		if (cooldown > 0) {
			cooldown--;
		}else if(!disablePickup) {
			pickUpCreature(2);
			cooldown = 20;
		}
	}

	private void manualDrop() {
		if (!isCageEmpty()) {
			getCart().riddenByEntity.mountEntity(null);		
			cooldown = 20;	
		}
	}
	
	private void manualPickUp() {
		pickUpCreature(5);
	}

	private EntityNearestTarget sorter = new EntityNearestTarget(getCart());
	private void pickUpCreature(int searchDistance) {
		if (getCart().worldObj.isRemote || !isCageEmpty()) {
			return;
		}
		
		List entities = getCart().worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getCart().boundingBox.expand((double)searchDistance, 4.0D, (double)searchDistance));
		Collections.sort(entities, sorter);
		Iterator itt = entities.iterator();

		while (itt.hasNext())
		{
			EntityLivingBase target = (EntityLivingBase)itt.next();

			
			
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
				(target instanceof EntitySkeleton && ((EntitySkeleton)target).getSkeletonType() == 1)) {
				
					continue;
			}
		
		
			if (target.ridingEntity == null) {
				target.mountEntity(getCart());
				return;
			}
		}

	}

	
	private int cooldown = 0;


	
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
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setBoolean(generateNBTName("disablePickup",id), disablePickup);
	}	
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		disablePickup = tagCompound.getBoolean(generateNBTName("disablePickup",id));
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