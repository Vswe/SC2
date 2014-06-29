package vswe.stevesvehicles.module.common.engine;

import vswe.stevesvehicles.vehicle.VehicleBase;

public abstract class ModuleSolarTop extends ModuleSolarBase {
	public ModuleSolarTop(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public float getInnerRotation() {
		return innerRotation;
	}
	public float getMovingLevel() {
		return movingLevel;
	}
	
	private static final  float MIN_VAL = -4F;
	private static final float MAX_VAL = -13F;
	private static final float MIN_ANGLE = 0F;
	private static final float MAX_ANGLE = (float)(Math.PI / 2);
	private float innerRotation = 0F;
	private float movingLevel = MIN_VAL;
	
	@Override
	public boolean updatePanels() {




		if (movingLevel > MIN_VAL) {
			movingLevel = MIN_VAL;
		}
		if (innerRotation < MIN_ANGLE) {
			innerRotation = MIN_ANGLE;
		}else if (innerRotation > MAX_ANGLE) {
			innerRotation = MAX_ANGLE;
		}

		float targetAngle = isGoingDown() ? MIN_ANGLE : MAX_ANGLE;

		if (movingLevel > MAX_VAL && innerRotation != targetAngle) {
			movingLevel -= 0.2F;
			if (movingLevel <= MAX_VAL) {
				movingLevel = MAX_VAL;
			}
		}else if (innerRotation != targetAngle){
			innerRotation += isGoingDown() ? -0.05F : 0.05F;
			if ((!isGoingDown() && innerRotation >= targetAngle) || (isGoingDown() && innerRotation <= targetAngle)) {
				innerRotation = targetAngle;
			}
		}else if (movingLevel < MIN_VAL){
			movingLevel += 0.2F;
			if (movingLevel >= MIN_VAL) {
				movingLevel = MIN_VAL;
			}
		}
		return innerRotation == MAX_ANGLE;
	}

}