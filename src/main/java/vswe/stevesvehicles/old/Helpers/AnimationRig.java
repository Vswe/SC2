package vswe.stevesvehicles.old.Helpers;

import java.util.ArrayList;

public class AnimationRig {

	private ArrayList<AnimationRigVal> rigs;
	
	public AnimationRig() {
		rigs = new ArrayList<AnimationRigVal>();
	}
	
	public boolean update(boolean goDown) {
		if (goDown) {
			for (int i = rigs.size() - 1; i >= 0; i--) {
				if (rigs.get(i).update(goDown)) {
					return false;
				}				
			}	
			return false;
		}else{		
			for (int i = 0; i < rigs.size(); i++) {
				if (rigs.get(i).update(goDown)) {
					return false;
				}	
			}	
			return true;
		}			
	}
	
	public void addVal(AnimationRigVal val) {
		rigs.add(val);
	}
}
