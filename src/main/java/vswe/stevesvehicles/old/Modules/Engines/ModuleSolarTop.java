package vswe.stevesvehicles.old.Modules.Engines;

import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public abstract class ModuleSolarTop extends ModuleSolarBase {
	public ModuleSolarTop(EntityModularCart cart) {
		super(cart);
	}

	public float getInnerRotation() {
		return innerRotation;
	}
	public float getMovingLevel() {
		return movingLevel;
	}
	
	private float minVal = -4F;
	private float maxVal = -13F;
	private float minAngle = 0F;
	private float maxAngle = (float)(Math.PI / 2);
	private float innerRotation = 0F;
	private float movingLevel = minVal;
	
	@Override
	public boolean updatePanels() {




		if (movingLevel > minVal) {
			movingLevel = minVal;
		}
		if (innerRotation < minAngle) {
			innerRotation = minAngle;
		}else if (innerRotation > maxAngle) {
			innerRotation = maxAngle;
		}

		float targetAngle = isGoingDown() ? minAngle : maxAngle;

		if (movingLevel > maxVal && innerRotation != targetAngle) {
			movingLevel -= 0.2F;
			if (movingLevel <= maxVal) {
				movingLevel = maxVal;;
			}
		}else if (innerRotation != targetAngle){
			innerRotation += isGoingDown() ? -0.05F : 0.05F;
			if ((!isGoingDown() && innerRotation >= targetAngle) || (isGoingDown() && innerRotation <= targetAngle)) {
				innerRotation = targetAngle;
			}
		}else if (movingLevel < minVal){
			movingLevel += 0.2F;
			if (movingLevel >= minVal) {
				movingLevel = minVal;;
			}
		}
		return innerRotation == maxAngle;	
	}
	


	protected abstract int getPanelCount();
	
}