package vswe.stevesvehicles.old.Slots;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;

public class SlotHull extends SlotAssembler
{
	

    public SlotHull(TileEntityCartAssembler assembler, int i, int j, int k)
    {
        super(assembler, i, j, k, ModuleType.HULL, true,0);

    }

}
