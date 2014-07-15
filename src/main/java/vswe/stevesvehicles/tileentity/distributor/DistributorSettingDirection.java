package vswe.stevesvehicles.tileentity.distributor;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.tileentity.TileEntityManager;

/**
* Created by Vswe on 15/07/14.
*/
class DistributorSettingDirection extends DistributorSetting {
    private boolean toCart;
    public DistributorSettingDirection(int id, boolean top, ILocalizedText name, boolean toCart) {
        super(id,top,name);
        this.toCart = toCart;
    }

    public boolean isValid(TileEntityManager manager, int chunkId, boolean top) {
        if (manager.layoutType == 0) {
            return super.isValid(manager, chunkId, top);
        }else{
            return super.isValid(manager, chunkId, top) && manager.toCart[chunkId] == toCart;
        }
    }
}
