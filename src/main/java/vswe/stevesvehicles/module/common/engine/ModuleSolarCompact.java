package vswe.stevesvehicles.module.common.engine;

import vswe.stevesvehicles.client.rendering.AnimationRigPart;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.rendering.AnimationRig;

public class ModuleSolarCompact extends ModuleSolarBase {
	public ModuleSolarCompact(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		
		rig = new AnimationRig();
		
		extraction = new AnimationRigPart(rig, 0.4F, 2F, 0.1F);
		topBot = new AnimationRigPart(rig, 0.1F, 4F, 0.25F);
		leftRight = new AnimationRigPart(rig, 0.01F, 6F, 0.2F);
		corner = new AnimationRigPart(rig, 0.1F, 4F, 0.25F);
		extraction2 = new AnimationRigPart(rig, 0F, 1.8F, 0.1F);
		innerExtraction = new AnimationRigPart(rig, 0.4F, 3, 0.2F);
		angle = new AnimationRigPart(rig, 0F, (float)Math.PI / 2, 0.1F);

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
	private AnimationRigPart extraction;
	private AnimationRigPart topBot;
	private AnimationRigPart leftRight;
	private AnimationRigPart corner;
	private AnimationRigPart angle;
	private AnimationRigPart extraction2;
	private AnimationRigPart innerExtraction;
		
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