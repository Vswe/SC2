package vswe.stevesvehicles.client.rendering;

public class AnimationRigPart {
	private float val;
	private float min;
	private float max;
	private float speed;
	private AnimationRigPart down;
	private AnimationRigPart up;
	
			
	public AnimationRigPart(AnimationRig rig, float min, float max, float speed) {
		this.min = min;
		this.max = max;
		this.speed = speed;


		this.val = this.min;
		
		
		rig.addVal(this);
	}
	
	public void setUp(AnimationRigPart up) {
		this.up = up;
	}
	
	public void setDown(AnimationRigPart down) {
		this.down = down;
	}	
	
	public void setUpAndDown(AnimationRigPart up) {
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
				down.update(true);
			}
		}else if(up != null) {
			up.update(false);
		}
		return true;
	}

	public void setSpeedToSync(AnimationRigPart syncTo, boolean invert) {
		speed = (max - min) / ((syncTo.max - syncTo.min) / syncTo.speed);
		if (invert) {
			speed *= -1;
		}
	}		
}
