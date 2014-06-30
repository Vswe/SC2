package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;

public class SlotHull extends SlotAssembler {

    public SlotHull(TileEntityCartAssembler assembler, int id, int x, int y) {
        super(assembler, id, x, y, ModuleType.HULL, true, 0);
    }
}
