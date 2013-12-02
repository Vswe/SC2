package vswe.stevescarts.Modules.Realtimers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTargetSorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleMobdetector;

public class ModuleShooterAdv extends ModuleShooter {
	public ModuleShooterAdv(MinecartModular cart) {
		super(cart);
	}

	
	private ArrayList<ModuleMobdetector> detectors;
	@Override
	public void preInit() {
		super.preInit();
		detectors = new ArrayList<ModuleMobdetector>();
		
		for (ModuleBase module : getCart().getModules()) {
			if (module instanceof ModuleMobdetector) {
				detectors.add((ModuleMobdetector)module);
			}
		}
	}		
	
	
	@Override
	protected void generatePipes(ArrayList<Integer> list) {
		list.add(1);
	}

	@Override
	protected int guiExtraWidth() {
		return 100;
	}

	@Override
	protected int guiRequiredHeight() {
		return 10 + 10 * detectors.size();
	}

	private int[] getSelectionBox(int id) {
		return new int[] {90, id * 10 +  (guiHeight() - 10 * detectors.size()) / 2,8,8};
	}

	@Override
	protected void generateInterfaceRegions() {}
	
	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.ATTACHMENTS.SHOOTER.translate(), 8, 6, 0x404040);

		for (int i = 0; i < detectors.size(); i++) {
			int[] box = getSelectionBox(i);
			drawString(gui,detectors.get(i).getName(), box[0] + 12, box[1], 0x404040);
		}
	}


	@Override
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/mobdetector.png");
		
		for (int i = 0; i < detectors.size(); i++) {
			int srcX = isOptionActive(i) ? 0 : 8;
			int srcY = inRect(x,y,getSelectionBox(i)) ? 8 : 0;
			drawImage(gui, getSelectionBox(i), srcX, srcY);
		}
	}

	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0) {
			for (int i = 0; i < detectors.size(); i++) {
				if (inRect(x,y,getSelectionBox(i))) {
					sendPacket(0, (byte)i);
					break;
				}
			}
		}
	}

	@Override
	public void mouseMovedOrUp(GuiMinecart gui,int x, int y, int button) {
	}

	
	@Override
	public int numberOfGuiData() {
		return 0;
	}
	@Override
	protected void checkGuiData(Object[] info) {

	}	

	@Override
	protected void Shoot()
    {
		setTimeToNext(15);
		if (!getCart().hasFuel())
		{
			return;
		}
		Entity target = getTarget();

		if (target != null) {
			if (hasProjectileItem()) {
				shootAtTarget(target);
			}else{
				getCart().worldObj.playAuxSFX(1001, (int)getCart().posX, (int)getCart().posY, (int)getCart().posZ, 0);
			}
		}
    }

	private void shootAtTarget(Entity target) {
		if (target == null) {
			return;
		}


		Entity projectile = getProjectile(target, getProjectileItem(true));
		

		//re-add if the EntityMob file behaves again
		//arrow.shootingEntity = getCart();
		projectile.posY = getCart().posY + (double)getCart().getEyeHeight() - 0.10000000149011612D;

		double disX = target.posX - getCart().posX;
		double disY = target.posY + (double)target.getEyeHeight() - 0.699999988079071D - projectile.posY;
		double disZ = target.posZ - getCart().posZ;

		double dis = (double)MathHelper.sqrt_double(disX * disX + disZ * disZ);

		if (dis >= 1.0E-7D)
        {
			float theta = (float)(Math.atan2(disZ, disX) * 180.0D / Math.PI) - 90.0F;
			float phi = (float)(-(Math.atan2(disY, dis) * 180.0D / Math.PI));

			setRifleDirection((float)Math.atan2(disZ, disX));

			double disPX = disX / dis;
            double disPZ = disZ / dis;

			projectile.setLocationAndAngles(getCart().posX + disPX * 1.5F, projectile.posY, getCart().posZ + disPZ * 1.5, theta, phi);

			projectile.yOffset = 0.0F;
            float disD5 = (float)dis * 0.2F;
			setHeading(projectile, disX, disY + (double)disD5, disZ, 1.6F, 0/*12.0F*/);
		}

		getCart().worldObj.playSoundAtEntity(getCart(), "random.bow", 1.0F, 1.0F / (getCart().rand.nextFloat() * 0.4F + 0.8F));
		
		setProjectileDamage(projectile);
		setProjectileOnFire(projectile);	
		setProjectileKnockback(projectile);
        getCart().worldObj.spawnEntityInWorld(projectile);
		damageEnchant();
	}

	protected int getTargetDistance() {
		return 16;
	}

	private EntityAINearestAttackableTargetSorter sorter = new EntityAINearestAttackableTargetSorter(getCart());
	private Entity getTarget() {
		List entities = getCart().worldObj.getEntitiesWithinAABB(Entity.class, getCart().boundingBox.expand((double)getTargetDistance(), 4.0D, (double)getTargetDistance()));
		Collections.sort(entities, sorter);
		Iterator itt = entities.iterator();

		while (itt.hasNext())
		{
			Entity target = (Entity)itt.next();

			if (target != getCart() && canSee(target))
			{
				for (int i = 0; i < detectors.size(); i++) {
					if (isOptionActive(i)) {
						ModuleMobdetector detector = detectors.get(i);
						if (detector.isValidTarget(target)) {
							return target;
						}
					}
				}
			}
		}

		return null;
	}

	private boolean canSee(Entity target)
    {
		if (target == null) {
			return false;
		}


        return getCart().worldObj.clip(Vec3.createVectorHelper(getCart().posX, getCart().posY + (double)getCart().getEyeHeight(), getCart().posZ), Vec3.createVectorHelper(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ)) == null;
    }

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			switchOption(data[0]);
		}
	}

	@Override
	public int numberOfPackets() {
		return 1;
	}

	@Override
	public int numberOfDataWatchers() {
		return 2;
	}

	@Override
	public void initDw() {
		addDw(0,0);
		addDw(1,0);
	}

	private void switchOption(int id) {
		byte val = getDw(0);
		val ^= 1 << id;
		updateDw(0,val);
	}

	public void setOptions(byte val) {
		updateDw(0,val);
	}	
	
	public byte selectedOptions() {
		return getDw(0);
	}

	private boolean isOptionActive(int id) {
		return (selectedOptions() & (1 << id)) != 0;
	}

	@Override
	protected boolean isPipeActive(int id) {
		if (isPlaceholder()) {
			return getSimInfo().getIsPipeActive();
		}else{
			return selectedOptions() != 0;
		}
	}

	private float detectorAngle;
	public float getDetectorAngle() {
		return detectorAngle;
	}

	public void update() {
		super.update();

		if (isPipeActive(0)) {
			detectorAngle = (float)((detectorAngle + 0.1F) % (Math.PI * 2));
		}
	}

	private	void setRifleDirection(float val) {
		val /= 2 * (float)Math.PI;
		val *= 256;
		val %= 256;
		if (val < 0) {
			val += 256;
		}
		updateDw(1,(byte)val);
	}
	public float getRifleDirection() {
		float val;
		if (isPlaceholder()) {
			val = 0F;
		}else{
			val = getDw(1);
		}
		val /= 256F;
		val *= (float)Math.PI * 2;
		return val;
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte(generateNBTName("Options",id), selectedOptions());	
		saveTick(tagCompound,id);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
        setOptions(tagCompound.getByte(generateNBTName("Options",id)));	
        loadTick(tagCompound, id);		
	}		
	
}