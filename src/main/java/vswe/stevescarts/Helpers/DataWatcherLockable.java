package vswe.stevescarts.Helpers;

import java.util.List;

import net.minecraft.entity.DataWatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DataWatcherLockable extends DataWatcher
{
	private boolean isLocked = true;
	private List lockedList;
	
	public void release() {
		isLocked = false;
		if (lockedList != null) {
			updateWatchedObjectsFromList(lockedList);
		}
	}
	
    @SideOnly(Side.CLIENT)
    public void updateWatchedObjectsFromList(List lst)
    {
		if (isLocked) {
			lockedList = lst;
		}else{
			super.updateWatchedObjectsFromList(lst);
		}
    }

}
