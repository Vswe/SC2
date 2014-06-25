package vswe.stevescarts.old.Slots;
import vswe.stevescarts.old.TileEntities.TileEntityCargo;

public class SlotCargo extends SlotBase implements ISpecialSlotValidator
{
	

	private TileEntityCargo cargo;
	private int id;
    public SlotCargo(TileEntityCargo cargo, int id)
    {
        super(cargo, id, -3000, -3000);
		this.id = id;
		this.cargo = cargo;
    }
	
	
	public boolean isSlotValid() {
		if (cargo.layoutType == 0) {
			return true;
		}else {
			int type;
			if (cargo.layoutType == 1) {
				type = cargo.getCurrentTransferForSlots().getSetting();
			}else{
				type = cargo.getCurrentTransferForSlots().getSide();
			}
			
			int slotType = id / 15;
			if (cargo.layoutType == 2) {
				 slotType = cargo.color[slotType] - 1;
			}
			           
			
			return slotType == type;
			
		}
	}
	

	
	
	public void updatePosition() {
		int offset;
		if (cargo.layoutType == 0)
        {
			offset = 0;
		}else{
			offset = 5;
		}
		
		if (id < 15) {
			int x = (id) % 5;
			int y = (id) / 5;			
			
			xDisplayPosition = 8+x*18;
			yDisplayPosition = 16+y*18 - offset;	
		}else if(id < 30) {
			int x = (id-15) % 5 + 11;
			int y = (id-15) / 5;			
			
			xDisplayPosition = 8+x*18;
			yDisplayPosition = 16+y*18 - offset;						
		}else if (id < 45) {
			int x = (id-30) % 5;
			int y = (id-30) / 5 + 3;			
			
			xDisplayPosition = 8+x*18;
			yDisplayPosition = 16+y*18 + offset;						
		}else{
			int x = (id-45) % 5 + 11;
			int y = (id-45) / 5 + 3;			
			
			xDisplayPosition = 8+x*18;
			yDisplayPosition = 16+y*18 + offset;				
		}
		
		

	}
	



	
}
