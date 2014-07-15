package vswe.stevesvehicles.client.rendering;

import java.util.ArrayList;

public class AnimationRig {

	private ArrayList<AnimationRigPart> rigs;
	
	public AnimationRig() {
		rigs = new ArrayList<AnimationRigPart>();
	}
	
	public boolean update(boolean goDown) {
		if (goDown) {
			for (int i = rigs.size() - 1; i >= 0; i--) {
				if (rigs.get(i).update(true)) {
					return false;
				}				
			}	
			return false;
		}else{
            for (AnimationRigPart rig : rigs) {
                if (rig.update(false)) {
                    return false;
                }
            }
			return true;
		}			
	}
	
	public void addVal(AnimationRigPart val) {
		rigs.add(val);
	}
}
