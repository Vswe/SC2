package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.block.BlockRailBase;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleRocket extends ModuleBase {

	public ModuleRocket(MinecartModular cart) {
		super(cart);
	}

	
	private boolean flying;
	private int landDirX;
	private int landDirZ;
	private double flyX;
	private double flyZ;
	private float yaw;
	private boolean isLanding;
	private double landY;
	private double groundY;
	
	@Override
	public void update() {
		if (isPlaceholder()) {
			return;
		}
		
		if (getCart().worldObj.isRemote) {
			if (!flying && getDw(0) != 0) {
				takeOff();
			}else if (!isLanding && getDw(0)  > 1) {
				land();
			}else if(flying && isLanding && getDw(0) == 0) {
				done();
			}
		}
		
		if (flying) {
			getCart().motionX = isLanding ? landDirX * 0.05F : 0;
			getCart().motionY = isLanding ? 0 : 0.1D;
			getCart().motionZ = isLanding ? landDirZ * 0.05F : 0;
			if (!isLanding || landDirX == 0) {
				getCart().posX = flyX;
			}else{
				getCart().posX += getCart().motionX;
			}
			if (!isLanding || landDirZ == 0) {
				getCart().posZ = flyZ;
			}else{
				getCart().posZ += getCart().motionZ;
			}
			getCart().rotationYaw = yaw;
			getCart().rotationPitch = 0;
			
			if (isLanding) {
				getCart().posY = landY;
				
				int id = getCart().worldObj.getBlockId(getCart().x(), getCart().y(), getCart().z());
				
	            if (BlockRailBase.isRailBlock(id)) {
	            	done();
	            	updateDw(0, (byte)0);
	            }				
			}
			
			if (!isLanding && getCart().posY - groundY > 2) {
	            int id = getCart().worldObj.getBlockId(getCart().x() + landDirX, getCart().y(), getCart().z() + landDirZ);
	
	            if (BlockRailBase.isRailBlock(id)) {
	            	land();
	            	updateDw(0, (byte)2);
	            }
			}
			
		}
	}
	
	@Override
	public void activatedByRail(int x, int y, int z, boolean active) {
		if (active) {
			takeOff();
			updateDw(0, (byte)1);
		}
	}	
	
	private void takeOff() {
		flying = true;
		getCart().setCanUseRail(false);
		flyX = getCart().posX;
		flyZ = getCart().posZ;
		yaw = getCart().rotationYaw;
		groundY = getCart().posY;
		
		
		if (Math.abs(getCart().motionX) > Math.abs(getCart().motionZ)) {
			landDirX = getCart().motionX > 0 ? 1 : -1;
		}else{
			landDirZ = getCart().motionZ > 0 ? 1 : -1;
		}
	}
	
	@Override
	public int numberOfDataWatchers() {
		return 1;
	}
	
	@Override
	public void initDw() {
		addDw(0, (byte)0);
	}
	
	private void land() {
    	isLanding = true;
    	landY = getCart().posY;
    	getCart().setCanUseRail(true);		
	}
	
	private void done() {
		flying = false;
		isLanding = false;
		landDirX = 0;
		landDirZ = 0;
	}

	
}
