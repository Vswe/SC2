package vswe.stevesvehicles.module.common.engine;

import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.AnimationRig;
import vswe.stevesvehicles.old.Helpers.AnimationRigVal;

public class ModuleSolarCompact extends ModuleSolarBase {
	public ModuleSolarCompact(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		
		rig = new AnimationRig();
		
		extraction = new AnimationRigVal(rig, 0.4F, 2F, 0.1F);
		topBot = new AnimationRigVal(rig, 0.1F, 4F, 0.25F);
		leftRight = new AnimationRigVal(rig, 0.01F, 6F, 0.2F);
		corner = new AnimationRigVal(rig, 0.1F, 4F, 0.25F);
		extraction2 = new AnimationRigVal(rig, 0F, 1.8F, 0.1F);
		innerExtraction = new AnimationRigVal(rig, 0.4F, 3, 0.2F);
		angle = new AnimationRigVal(rig, 0F, (float)Math.PI / 2, 0.1F);	

		innerExtraction.setUpAndDown(angle);
	}

	
	@Override
	protected int getMaxCapacity(){
		return 25000;
	}
	
	@Override
	protected int getGenSpeed() {
		return 5;
	}
	
	private AnimationRig rig;
	private AnimationRigVal extraction;
	private AnimationRigVal topBot;
	private AnimationRigVal leftRight;
	private AnimationRigVal corner;
	private AnimationRigVal angle;
	private AnimationRigVal extraction2;
	private AnimationRigVal innerExtraction;
		
	@Override
	public boolean updatePanels() {
		return rig.update(isGoingDown());
	}


	
	public float getExtractionDist() {
		return extraction.getVal() + extraction2.getVal();
	}
	
	public float getTopBotExtractionDist() {
		return topBot.getVal();
	}
	
	public float getLeftRightExtractionDist() {
		return leftRight.getVal();
	}
	
	public float getCornerExtractionDist() {
		return corner.getVal();
	}	
	
	public float getPanelAngle() {
		return angle.getVal();
	}
	

	public float getInnerExtraction() {
		return innerExtraction.getVal();
	}	
	
	
	
}