package vswe.stevesvehicles.tileentity.distributor;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.tileentity.TileEntityManager;

/**
* Created by Vswe on 15/07/14.
*/
class DistributorSettingColor extends DistributorSetting {
    private int color;
    public DistributorSettingColor(int id, boolean top, ILocalizedText name, int color) {
        super(id,top,name);
        this.color = color;
    }

    public boolean isValid(TileEntityManager manager, int chunkId, boolean top) {
        if (manager.layoutType == 0) {
            return super.isValid(manager, chunkId, top);
        }else{
            return super.isValid(manager, chunkId, top) && manager.color[chunkId] == color;
        }
    }
}
