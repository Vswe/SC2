package vswe.stevesvehicles.module.common.attachment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfo;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfoBoolean;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.localization.entry.module.LocalizationShooter;
import vswe.stevesvehicles.module.common.addon.mobdetector.ModuleEntityDetector;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;

public class ModuleShooterAdvanced extends ModuleShooter {
	public ModuleShooterAdvanced(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    @Override
    public void loadSimulationInfo(List<SimulationInfo> simulationInfo) {
        simulationInfo.add(new SimulationInfoBoolean(LocalizationAssembler.INFO_BARREL, "barrel"));
    }

    private ArrayList<ModuleEntityDetector> detectors;
	@Override
	public void preInit() {
		super.preInit();
		detectors = new ArrayList<ModuleEntityDetector>();
		
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleEntityDetector) {
				detectors.add((ModuleEntityDetector)module);
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
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationShooter.SHOOTER_TITLE.translate(), 8, 6, 0x404040);

		for (int i = 0; i < detectors.size(); i++) {
			int[] box = getSelectionBox(i);
			drawString(gui,detectors.get(i).getName(), box[0] + 12, box[1], 0x404040);
		}
	}

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/mob_detector.png");

	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);
		
		for (int i = 0; i < detectors.size(); i++) {
			int srcX = isOptionActive(i) ? 1 : 10;
			int srcY = inRect(x,y,getSelectionBox(i)) ? 10 : 1;
			drawImage(gui, getSelectionBox(i), srcX, srcY);
		}
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			for (int i = 0; i < detectors.size(); i++) {
				if (inRect(x,y,getSelectionBox(i))) {
                    DataWriter dw = getDataWriter();
                    dw.writeByte(i);
                    sendPacketToServer(dw);
					break;
				}
			}
		}
	}

	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
	}

	
	@Override
	public int numberOfGuiData() {
		return 0;
	}
	@Override
	protected void checkGuiData(Object[] info) {

	}	

	@Override
	protected void shoot() {
		setTimeToNext(15);
		if (!getVehicle().hasFuel())
		{
			return;
		}
		Entity target = getTarget();

		if (target != null) {
			if (hasProjectileItem()) {
				shootAtTarget(target);
			}else{
				getVehicle().getWorld().playAuxSFX(1001, getVehicle().x(), getVehicle().y(), getVehicle().z(), 0);
			}
		}
    }

	private void shootAtTarget(Entity target) {
		if (target == null) {
			return;
		}


		Entity projectile = getProjectile(target, getProjectileItem(true));
		

		projectile.posY = getVehicle().getEntity().posY + (double)getVehicle().getEntity().getEyeHeight() - 0.10000000149011612D;

		double disX = target.posX - getVehicle().getEntity().posX;
		double disY = target.posY + (double)target.getEyeHeight() - 0.699999988079071D - projectile.posY;
		double disZ = target.posZ - getVehicle().getEntity().posZ;

		double dis = (double)MathHelper.sqrt_double(disX * disX + disZ * disZ);

		if (dis >= 1.0E-7D){
			float theta = (float)(Math.atan2(disZ, disX) * 180.0D / Math.PI) - 90.0F;
			float phi = (float)(-(Math.atan2(disY, dis) * 180.0D / Math.PI));

			setRifleDirection((float)Math.atan2(disZ, disX));

			double disPX = disX / dis;
            double disPZ = disZ / dis;

			projectile.setLocationAndAngles(getVehicle().getEntity().posX + disPX * 1.5F, projectile.posY, getVehicle().getEntity().posZ + disPZ * 1.5, theta, phi);

			projectile.yOffset = 0.0F;
            float disD5 = (float)dis * 0.2F;
			setHeading(projectile, disX, disY + (double)disD5, disZ, 1.6F, 0/*12.0F*/);
		}

		getVehicle().getWorld().playSoundAtEntity(getVehicle().getEntity(), "random.bow", 1.0F, 1.0F / (getVehicle().getRandom().nextFloat() * 0.4F + 0.8F));
		
		setProjectileDamage(projectile);
		setProjectileOnFire(projectile);	
		setProjectileKnockBack(projectile);
        getVehicle().getWorld().spawnEntityInWorld(projectile);
		damageEnchant();
	}

	protected int getTargetDistance() {
		return 16;
	}

	private EntityNearestTarget sorter = new EntityNearestTarget(getVehicle().getEntity());
	private Entity getTarget() {
		List entities = getVehicle().getWorld().getEntitiesWithinAABB(Entity.class, getVehicle().getEntity().boundingBox.expand((double) getTargetDistance(), 4.0D, (double) getTargetDistance()));
		Collections.sort(entities, sorter);

        for (Object entity : entities) {
            Entity target = (Entity) entity;

            if (target != getVehicle().getEntity() && canSee(target)) {
                for (int i = 0; i < detectors.size(); i++) {
                    if (isOptionActive(i)) {
                        ModuleEntityDetector detector = detectors.get(i);
                        if (detector.isValidTarget(target)) {
                            return target;
                        }
                    }
                }
            }
        }

		return null;
	}

	private boolean canSee(Entity target) {
        return target != null && getVehicle().getWorld().rayTraceBlocks(Vec3.createVectorHelper(getVehicle().getEntity().posX, getVehicle().getEntity().posY + (double) getVehicle().getEntity().getEyeHeight(), getVehicle().getEntity().posZ), Vec3.createVectorHelper(target.posX, target.posY + (double) target.getEyeHeight(), target.posZ)) == null;
    }

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
	    switchOption(dr.readByte());
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
		updateDw(0, val);
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
			return getBooleanSimulationInfo();
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
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setByte("Options", selectedOptions());
		saveTick(tagCompound);
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
        setOptions(tagCompound.getByte("Options"));
        loadTick(tagCompound);
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