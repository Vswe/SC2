package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.tanks.Tank;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
public class SlotLiquidUpgradeInput extends SlotLiquidInput {

	private TileEntityUpgrade upgrade;
    public SlotLiquidUpgradeInput(TileEntityUpgrade upgrade, Tank tank, int maxsize, int id, int x, int y) {
        super(upgrade, tank, maxsize, id, x, y);
        this.upgrade = upgrade;
    }

}
