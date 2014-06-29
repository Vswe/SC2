package vswe.stevesvehicles.module.common.attachment;

import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.AnimationRig;
import vswe.stevesvehicles.old.Helpers.AnimationRigVal;

public class ModuleShooterAdvancedSide extends ModuleShooterAdvanced {

	private AnimationRig rig;
	private AnimationRigVal handlePos;
	private AnimationRigVal basePos;
	private AnimationRigVal handleRot;
	private AnimationRigVal gunRot;
	private AnimationRigVal backPos;
	private AnimationRigVal backRot;
	private AnimationRigVal attacherRot;
	private AnimationRigVal stabalizerOut;
	private AnimationRigVal stabalizerDown;
	private AnimationRigVal standOut;
	private AnimationRigVal standUp;
	private AnimationRigVal standSlide;
	private AnimationRigVal armBasePos;
	private AnimationRigVal armPos;
	private AnimationRigVal armRot;
	private AnimationRigVal missilePos;
	private AnimationRigVal missileRot;
	private AnimationRigVal armBasePos2;
	private AnimationRigVal armPos2;
	private AnimationRigVal armRot2;
	
	public ModuleShooterAdvancedSide(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		rig = new AnimationRig();
		
		handlePos = new AnimationRigVal(rig, 8.55F, 9.4F, 0);
		basePos = new AnimationRigVal(rig, 1.05F, 4F, 0.05F);
		handleRot = new AnimationRigVal(rig, (float)Math.PI, (float)Math.PI * 3 / 2, 0.075F);
		gunRot = new AnimationRigVal(rig, 0, -(float)Math.PI / 2, 0);
		backPos = new AnimationRigVal(rig, 4.5F, -3F, 0.3F);
		backRot = new AnimationRigVal(rig, 0, -(float)Math.PI / 2, 0.2F);
		attacherRot = new AnimationRigVal(rig, 0, -(float)Math.PI, 0.2F);
		stabalizerOut = new AnimationRigVal(rig, 0.001F, 0.8F, 0.1F);
		stabalizerDown = new AnimationRigVal(rig, 0, -2, 0.1F);
		standOut = new AnimationRigVal(rig, 0.001F, 0.8F, 0.1F);
		standUp = new AnimationRigVal(rig, 0, 2, 0.1F);
		standSlide = new AnimationRigVal(rig, 0, 0.25F, 0.01F);
		armBasePos  = new AnimationRigVal(rig, 0.5F, 10F, 0.3F);
		armPos = new AnimationRigVal(rig, -2.25F, 2.5F, 0);
		armRot  = new AnimationRigVal(rig, 0, (float)Math.PI / 2, 0.2F);
		missilePos = new AnimationRigVal(rig, 0, 3, 0.1F);
		missileRot = new AnimationRigVal(rig, 0, -0.2F, 0F);
		armRot2  = new AnimationRigVal(rig, 0, (float)Math.PI / 2, 0.2F);		
		armBasePos2  = new AnimationRigVal(rig, 0, 9.5F, 0.3F);
		armPos2 = new AnimationRigVal(rig, 0, 5F, 0);

		
		handlePos.setUpAndDown(basePos);
		handlePos.setSpeedToSync(basePos, false);
		handleRot.setUpAndDown(gunRot);
		gunRot.setSpeedToSync(handleRot, true);
		armPos.setSpeedToSync(armBasePos, false);
		armBasePos.setUpAndDown(armPos);
		missilePos.setUpAndDown(missileRot);
		missileRot.setSpeedToSync(missilePos, true);
		armPos2.setSpeedToSync(armBasePos2, false);
		armBasePos2.setUpAndDown(armPos2);		
	}
	
	@Override
	public void update() {
		super.update();
		
		rig.update(!isPipeActive(0));
	}
	
	public float getHandlePos(int multiplier) {
		return handlePos.getVal() * multiplier;
	}
	
	public float getBasePos(int multiplier) {
		return basePos.getVal() * multiplier;
	}
	
	public float getHandleRot(int multiplier) {
		return handleRot.getVal();
	}

	public float getGunRot(int multiplier) {
		return gunRot.getVal();
	}

	public float getBackPos(int multiplier) {
		return backPos.getVal();
	}
	
	public float getBackRot(int multiplier) {
		return backRot.getVal() * multiplier;
	}

	public float getAttacherRot(int multiplier) {
		return attacherRot.getVal() * multiplier;
	}

	public float getStabalizerOut(int multiplier) {
		return stabalizerOut.getVal() * multiplier;
	}
	
	public float getStabalizerDown(int multiplier) {
		return stabalizerDown.getVal();
	}

	public float getStandOut(int multiplier, int i, int j) {
		return standOut.getVal() * j + multiplier * i * 0.5F + 0.003F;
	}
	
	public float getStandUp(int multiplier, int i, int j) {
		return standUp.getVal() - standSlide.getVal() * (i * 2 - 1) * j * multiplier;
	}

	public float getArmBasePos(int multiplier, boolean fake) {
		return armBasePos.getVal() - (!fake ? armBasePos2.getVal() : 0);
	}	
	
	public float getArmRot(int multiplier, boolean fake) {
		return (armRot.getVal() - (!fake ? armRot2.getVal() : 0)) * multiplier;
	}	
	
	public float getArmPos(int multiplier, boolean fake) {
		return armPos.getVal() - (!fake ? armPos2.getVal() : 0);
	}

	public float getMissilePos(int multiplier) {
		return missilePos.getVal();
	}

	public float getMissileRot(int multiplier) {
		return missileRot.getVal() * multiplier;
	}
}
