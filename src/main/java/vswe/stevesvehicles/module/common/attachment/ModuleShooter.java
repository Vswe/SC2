package vswe.stevesvehicles.module.common.attachment;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfo;
import vswe.stevesvehicles.client.gui.assembler.SimulationInfoMultiBoolean;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.block.LocalizationAssembler;
import vswe.stevesvehicles.localization.entry.module.LocalizationShooter;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.module.common.addon.enchanter.EnchantmentInfo;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.addon.enchanter.ModuleEnchants;
import vswe.stevesvehicles.module.common.addon.projectile.ModuleProjectile;
import vswe.stevesvehicles.container.slots.SlotArrow;
import vswe.stevesvehicles.container.slots.SlotBase;


//TODO this module has so many numbers that should be replaced with constants

public class ModuleShooter extends ModuleAttachment implements ISuppliesModule {
	public ModuleShooter(VehicleBase vehicleBase) {
		super(vehicleBase);
		pipes = new ArrayList<Integer>();
		generatePipes(pipes);
		pipeRotations = new float[pipes.size()];
		generateInterfaceRegions();
	}

    @Override
    public void loadSimulationInfo(List<SimulationInfo> simulationInfo) {
        simulationInfo.add(new SimulationInfoMultiBoolean(LocalizationAssembler.INFO_BARRELS, "barrels", 4));
        simulationInfo.add(new SimulationInfoMultiBoolean(LocalizationAssembler.INFO_BARRELS, "barrels", 4));
    }

    private ArrayList<ModuleProjectile> projectiles;
	private ModuleEnchants enchanter;
	@Override
	public void init() {
		super.init();
		projectiles = new ArrayList<ModuleProjectile>();
		
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleProjectile) {
				projectiles.add((ModuleProjectile)module);
			}else if(module instanceof ModuleEnchants) {
				enchanter = (ModuleEnchants)module;
				enchanter.addType(EnchantmentInfo.Enchantment_Type.SHOOTER);
			}
		}
	}		
	

	
	@Override
	protected int getInventoryHeight() {
		return 2;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotArrow(getVehicle().getVehicleEntity(), this, slotId, 8 + x * 18, 23 + y * 18);
	}

	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationShooter.SHOOTER_TITLE.translate(), 8, 6, 0x404040);

		int delay = AInterval[arrowInterval];
        double freq = 20D / (delay+1);

        String s = String.valueOf((((int)(freq * 1000)) / 1000D));
        drawString(gui, LocalizationShooter.SHOOTER_FREQUENCY.translate() + ":", intervalDragArea[0] + intervalDragArea[2] + 5, 15, 0x404040);
		drawString(gui,s, intervalDragArea[0] + intervalDragArea[2] + 5, 23, 0x404040);
        s =  String.valueOf((delay / 20D) +LocalizationShooter.SHOOTER_SECONDS.translate());
        drawString(gui, LocalizationShooter.SHOOTER_DELAY.translate() + ":", intervalDragArea[0] + intervalDragArea[2] + 5, 35, 0x404040);
		drawString(gui,s, intervalDragArea[0] + intervalDragArea[2] + 5, 43, 0x404040);
	}

	@Override
	public int guiWidth() {
		return super.guiWidth() + guiExtraWidth();
	}

	protected int guiExtraWidth() {
		return 112;
	}

	@Override
	public int guiHeight() {
		return Math.max(super.guiHeight(),guiRequiredHeight());
	}

	protected int guiRequiredHeight() {
		return 67;
	}

	protected void generateInterfaceRegions() {
		pipeSelectionX = guiWidth() - 110;
		pipeSelectionY = ((guiHeight() - 12)/*the area used for drawing*/ - 26 /*its own height*/) / 2 + 12/*the area used for drawing starts here*/;

		intervalSelectionX = pipeSelectionX + 26 + 8;
		intervalSelectionY = 10;

		intervalSelection = new int[] {intervalSelectionX,intervalSelectionY,14,53};
		intervalDragArea = new int[] {intervalSelectionX-4,intervalSelectionY,40,53};	
	}
	
	private int pipeSelectionX;
	private int pipeSelectionY;

	private int intervalSelectionX;
	private int intervalSelectionY;

	private int[] intervalSelection;
	private int[] intervalDragArea;


    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/shooter.png");
	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);

		drawImage(gui, pipeSelectionX + (26-8) / 2, pipeSelectionY + (26-8) / 2 - 1, 1, 111, 8, 9);

        for (int i = 0; i < pipes.size(); i++) {
			int pipe = pipes.get(i);
			int pipeX = pipe % 3;
			int pipeY = pipe / 3;

			boolean active = isPipeActive(i);
			boolean selected = inRect(x, y, getRectForPipe(pipe)) || (arrowTick == 0 && active);

			int srcX = 1 + pipeX * 9;
			if (!active) {
				srcX += 27;
			}
			int srcY = 1 + pipeY * 9;
			if (selected) {
				srcY += 27;
			}

			drawImage(gui,getRectForPipe(pipe), srcX,srcY);
        }

		drawImage(gui, intervalSelection, 45, 55);

		int size = (int)((arrowInterval / (float)AInterval.length) * 4);

	    int targetX = intervalSelectionX + 7;
        int targetY = intervalSelectionY +  arrowInterval * 2;
        int srcX = 1;
        int srcY = 55 + size * 14;

        drawImage(gui,targetX,targetY, srcX, srcY, 25, 13);

		srcX += 26;
		targetX += 7;

        int scaledTick = 41 * arrowTick / AInterval[arrowInterval];
        drawImage(gui,targetX, targetY + 1, srcX, srcY + 1, 1, 11);
        drawImage(gui,targetX + 1, targetY + 2, srcX + 1, srcY + 2, 1, 9);
        drawImage(gui,targetX + 1, targetY + 1, srcX + 1, srcY + 1, Math.min(scaledTick, 15), 2);
        drawImage(gui,targetX + 15, targetY + 1, srcX + 15, srcY + 1, 2, Math.max(Math.min(scaledTick, 25) - 15, 0));
        int len = Math.max(Math.min(scaledTick, 41) - 25, 0);
        drawImage(gui,targetX + 1 + (16 - len), targetY + 10, srcX + 1 + (16 - len), srcY + 10, len, 2);
	}

	private int[] getRectForPipe(int pipe) {
		return new int[] {pipeSelectionX + (pipe % 3) * 9, pipeSelectionY + (pipe / 3) * 9,8,8};
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y,intervalDragArea)) {
				dragState = y - (intervalSelectionY + arrowInterval * 2);
			}else{
				for (int i = 0; i < pipes.size(); i++) {
					if (inRect(x,y, getRectForPipe(pipes.get(i)))) {
                        DataWriter dw = getDataWriter();
                        dw.writeBoolean(true);
                        dw.writeByte(i);
                        sendPacketToServer(dw);
						break;
					}
				}
			}
		}
	}

	private int dragState = -1;
	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
	    if (button != -1) {
            dragState = -1;
        }else if (dragState != -1) {
            int interval = ((y + getVehicle().getRealScrollY()) - intervalSelectionY - dragState) / 2;

            if (interval != arrowInterval)
            {
				if (interval >= 0 && interval < AInterval.length) {
                    DataWriter dw = getDataWriter();
                    dw.writeBoolean(false);
                    dw.writeByte(interval);
                    sendPacketToServer(dw);
				}
            }
        }
	}

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
		if (dr.readBoolean()) {
			byte info = getActivePipes();
			info ^= 1 << dr.readByte();
			setActivePipes(info);
		}else {
			int info = dr.readByte();
			if (info < 0){
				info = 0; }
			else if (info >= AInterval.length) {
				info = AInterval.length - 1;
			}

			arrowInterval = info;
			arrowTick = AInterval[info];
		}
	}


	@Override
	public int numberOfGuiData() {
		return 2;
	}


	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)arrowTick);
		updateGuiData(info, 1, (short)arrowInterval);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
            arrowTick = data;
		}else if (id == 1) {
			arrowInterval = data;
		}
	}

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();

		if (!getVehicle().getWorld().isRemote) {
			if (arrowTick > 0){
				arrowTick--;
			}else{
				shoot();
			}
		}else{
			rotatePipes(false);
		}
	}

	//pipes that this module have
	//0 (Forward Left)		, 1 (Forward)		, 2 (Forward Right)
	//3 (Left),				, 4 (Invalid)		, 5 (Right)
	//6 (Back Left)			, 7 (Back)			, 8 (Back Right)
	private final ArrayList<Integer> pipes;
	private final float[] pipeRotations;
	protected void generatePipes(ArrayList<Integer> list) {
		for (int i = 0; i < 9; i++) {
			if (i == 4) continue;
			list.add(i);
		}
	}

	
	protected boolean hasProjectileItem() {
		return getProjectileItem(false) != null;
	}
	//if flag is true, consume one projectile if found
	protected ItemStack getProjectileItem(boolean flag) {
		
		if (flag && enchanter != null && enchanter.useInfinity()) {
			flag = false;
		}
		
		for (int i = 0; i < getInventorySize(); i++) {
			if (getStack(i) != null) {
				if (isValidProjectileItem(getStack(i))) {
					ItemStack projectile =getStack(i).copy();
					projectile.stackSize = 1;
				
					if (flag && !getVehicle().hasCreativeSupplies()) {
                        getStack(i).stackSize--;

                        if (getStack(i).stackSize == 0) {
                            setStack(i,null);
                        }
					}

					return projectile;
				}
			}
		}

		return null;	
	}

	@SuppressWarnings("SuspiciousNameCombination")
    protected void shoot() {
		setTimeToNext(AInterval[arrowInterval]);


        double speedX = getVehicle().getEntity().motionX;
        double speedZ = getVehicle().getEntity().motionZ;

        boolean movingX = Math.abs(speedX) < 0.005;
        boolean movingZ = Math.abs(speedZ) < 0.005;

		if ((movingX && movingZ) || (!movingX && !movingZ) || !getVehicle().hasFuel()) {
			return;
		}

        boolean hasShot = false;

		for (int i = 0; i < pipes.size(); i++) {
			if (!isPipeActive(i)){
				continue;
			}
			int pipe = pipes.get(i);

			if (!hasProjectileItem()){
				break;
			}

			int x = pipe % 3 - 1;
			int y = pipe / 3 - 1;

			if (speedZ > 0){
				y *= -1;
				x *= -1;
			}else if (speedX < 0){
				int temp = -x;
				x = y;
				y = temp;
			}else if (speedX > 0){
				int temp = x;
				x = -y;
				y = temp;
			}


			Entity projectile = getProjectile(null, getProjectileItem(true));

			projectile.setPosition(getVehicle().getEntity().posX + x * 1.5, getVehicle().getEntity().posY + 0.75F, getVehicle().getEntity().posZ + y * 1.5);
			setHeading(projectile, x, 0.10000000149011612D, y, 1.6F, 12F);
			setProjectileDamage(projectile);
			setProjectileOnFire(projectile);
			setProjectileKnockBack(projectile);
			getVehicle().getWorld().spawnEntityInWorld(projectile);
			hasShot = true;
			damageEnchant();
		}

		if (hasShot){
			getVehicle().getWorld().playAuxSFX(1002, getVehicle().x(), getVehicle().y(), getVehicle().z(), 0);
		}
    }
	
	protected void damageEnchant() {
		if (enchanter != null) {
			enchanter.damageEnchant(EnchantmentInfo.Enchantment_Type.SHOOTER, 1);
		}
	}
	
	protected void setProjectileOnFire(Entity projectile) {
		if (enchanter != null && enchanter.useFlame()) {
			projectile.setFire(100);
		}
	}
	
	protected void setProjectileDamage(Entity projectile) {
		if (enchanter != null && projectile instanceof EntityArrow) {
			int power = enchanter.getPowerLevel();
			if (power > 0) {
				EntityArrow arrow = (EntityArrow)projectile;
				arrow.setDamage((arrow).getDamage() + (double)power * 0.5D + 0.5D);	
			}
		}	
	}
	          
	protected void setProjectileKnockBack(Entity projectile) {
		if (enchanter != null && projectile instanceof EntityArrow) {
			int punch = enchanter.getPunchLevel();
			if (punch > 0) {
				EntityArrow arrow = (EntityArrow)projectile;
				arrow.setKnockbackStrength(punch);
			}			
		}
	}
	
	                                                    
	
	protected void setHeading(Entity projectile, double motionX, double motionY, double motionZ, float motionMultiplier, float motionNoise) {
		if (projectile instanceof IProjectile) {
			((IProjectile)projectile).setThrowableHeading(motionX,motionY,motionZ,motionMultiplier,motionNoise);
		}else if(projectile instanceof EntityFireball) {
			//Not a projective :S
			EntityFireball fireball = (EntityFireball)projectile;
			
			double totalMotion = (double)MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
			fireball.accelerationX = motionX / totalMotion * 0.1D;
			fireball.accelerationY = motionY / totalMotion * 0.1D;
			fireball.accelerationZ = motionZ / totalMotion * 0.1D;			
		}
	}

	protected Entity getProjectile(Entity target, ItemStack item) {
		for (ModuleProjectile module : projectiles) {
			if (module.isValidProjectile(item)) {
				return module.createProjectile(target, item);
			}
		}
		return new EntityArrow(getVehicle().getWorld());
	}
	
	public boolean isValidProjectileItem(ItemStack item) {
		for (ModuleProjectile module : projectiles) {
			if (module.isValidProjectile(item)) {
				return true;
			}
		}	
		return item.getItem() == Items.arrow;
	}
	
	private final int[] AInterval = {1, 3, 5, 7, 10, 13, 17, 21, 27, 35, 44, 55, 70, 95, 130, 175, 220, 275, 340, 420, 520, 650};
    private int arrowTick;
    private int arrowInterval = 5;

	protected void setTimeToNext(int val) {
		arrowTick = val;
	}

	private void rotatePipes(boolean isNew) {
		float minRotation = 0F;
		float maxRotation = (float)Math.PI / 4;
		float speed = 0.15F;

		for (int i = 0; i < pipes.size(); i++) {
			boolean isActive = isPipeActive(i);
			if (isNew && isActive) {
				pipeRotations[i] = minRotation;
			}else if(isNew) {
				pipeRotations[i] = maxRotation;
			}else if (isActive && pipeRotations[i] > minRotation) {
				pipeRotations[i] -= speed;
				if (pipeRotations[i] < minRotation) {
					pipeRotations[i] = minRotation;
				}
			}else if (!isActive && pipeRotations[i] < maxRotation) {
				pipeRotations[i] += speed;
				if (pipeRotations[i] > maxRotation) {
					pipeRotations[i] = maxRotation;
				}
			}
		}
	}

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0,0);
	}

	public void setActivePipes(byte val) {
		updateDw(0,val);
	}

	public byte getActivePipes() {
		if (isPlaceholder()) {
			return (byte)(((SimulationInfoMultiBoolean)getSimulationInfo(0)).getIntegerValue() | (((SimulationInfoMultiBoolean)getSimulationInfo(1)).getIntegerValue() << 4));
		}else{
			return getDw(0);
		}
	}

	protected boolean isPipeActive(int id) {
		return (getActivePipes() & (1 << id)) != 0;
	}

	public float getPipeRotation(int id) {
		return pipeRotations[id];
	}

	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setByte("Pipes", getActivePipes());
		tagCompound.setByte("Interval", (byte)arrowInterval);
		saveTick(tagCompound);
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setActivePipes(tagCompound.getByte("Pipes"));
		arrowInterval = tagCompound.getByte("Interval");
        loadTick(tagCompound);
	}	
	
	protected void saveTick(NBTTagCompound tagCompound) {
		tagCompound.setByte("Tick", (byte)arrowTick);
	}
	
	protected void loadTick(NBTTagCompound tagCompound) {
		arrowTick = tagCompound.getByte("Tick");
	}
	
	@Override
	public boolean haveSupplies() {
		return hasProjectileItem();
	}
	
}