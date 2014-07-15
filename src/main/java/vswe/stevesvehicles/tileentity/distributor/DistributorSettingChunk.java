package vswe.stevesvehicles.tileentity.distributor;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.tileentity.TileEntityManager;

/**
* Created by Vswe on 15/07/14.
*/
class DistributorSettingChunk extends DistributorSetting {
    private int chunk;
    public DistributorSettingChunk(int id, boolean top, ILocalizedText name, int chunk) {
        super(id,top,name);
        this.chunk = chunk;
    }

    public boolean isValid(TileEntityManager manager, int chunkId, boolean top) {
        if (manager.layoutType == 0) {
            return super.isValid(manager, chunkId, top);
        }else{
            return super.isValid(manager, chunkId, top) && chunk == chunkId;
        }
    }
}
