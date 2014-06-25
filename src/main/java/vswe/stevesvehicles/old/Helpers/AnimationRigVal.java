package vswe.stevesvehicles.old.Helpers;

public class AnimationRigVal {
	private float val;
	private float min;
	private float max;
	private float speed;
	private AnimationRigVal down;
	private AnimationRigVal up;
	
			
	public AnimationRigVal(AnimationRig rig, float min, float max, float speed) {
		this.min = min;
		this.max = max;
		this.speed = speed;


		this.val = this.min;
		
		
		rig.addVal(this);
	}
	
	public void setUp(AnimationRigVal up) {
		this.up = up;
	}
	
	public void setDown(AnimationRigVal down) {
		this.down = down;
	}	
	
	public void setUpAndDown(AnimationRigVal up) {
		this.setUp(up);
		up.setDown(this);
	}
	
	public float getVal() {
		return this.val;
	}
	
	public boolean update(boolean goDown) {
		float target = goDown ? min : max;
		
		if (target == this.val) {
			return false;
		}
	
		if (val < target) {
			val += speed;
			if (val > target) {
				val = target;
			}
		}else if(val > target){
			val -= speed;
			if (val < target) {
				val = target;
			}			
		}
		
		if (goDown) {
			if (down != null) {
				down.update(goDown);
			}
		}else if(up != null) {
			up.update(goDown);
		}
		return true;
	}

	public void setSpeedToSync(AnimationRigVal syncTo, boolean invert) {
		speed = (max - min) / ((syncTo.max - syncTo.min) / syncTo.speed);
		if (invert) {
			speed *= -1;
		}
	}		
}
