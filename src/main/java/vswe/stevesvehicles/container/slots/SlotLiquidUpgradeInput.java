package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.old.Helpers.Tank;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
public class SlotLiquidUpgradeInput extends SlotLiquidInput {

	private TileEntityUpgrade upgrade;
    public SlotLiquidUpgradeInput(TileEntityUpgrade upgrade, Tank tank, int maxsize, int id, int x, int y) {
        super(upgrade, tank, maxsize, id, x, y);
        this.upgrade = upgrade;
    }

}
