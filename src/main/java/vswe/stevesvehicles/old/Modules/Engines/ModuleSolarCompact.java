package vswe.stevesvehicles.old.Modules.Engines;

import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.AnimationRig;
import vswe.stevesvehicles.old.Helpers.AnimationRigVal;

public class ModuleSolarCompact extends ModuleSolarBase {
	public ModuleSolarCompact(EntityModularCart cart) {
		super(cart);
		
		
		rig = new AnimationRig();
		
		extraction = new AnimationRigVal(rig, 0.4F, 2F, 0.1F);
		topbot = new AnimationRigVal(rig, 0.1F, 4F, 0.25F);
		leftright = new AnimationRigVal(rig, 0.01F, 6F, 0.2F);
		corner = new AnimationRigVal(rig, 0.1F, 4F, 0.25F);
		extraction2 = new AnimationRigVal(rig, 0F, 1.8F, 0.1F);
		innerextraction = new AnimationRigVal(rig, 0.4F, 3, 0.2F);
		angle = new AnimationRigVal(rig, 0F, (float)Math.PI / 2, 0.1F);	

		innerextraction.setUpAndDown(angle);
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
	private AnimationRigVal topbot;
	private AnimationRigVal leftright;
	private AnimationRigVal corner;
	private AnimationRigVal angle;
	private AnimationRigVal extraction2;
	private AnimationRigVal innerextraction;
		
	@Override
	public boolean updatePanels() {
		return rig.update(isGoingDown());
	}


	
	public float getExtractionDist() {
		return extraction.getVal() + extraction2.getVal();
	}
	
	public float getTopBotExtractionDist() {
		return topbot.getVal();
	}
	
	public float getLeftRightExtractionDist() {
		return leftright.getVal();
	}
	
	public float getCornerExtractionDist() {
		return corner.getVal();
	}	
	
	public float getPanelAngle() {
		return angle.getVal();
	}
	

	public float getInnerExtraction() {
		return innerextraction.getVal();
	}	
	
	
	
}