package vswe.stevesvehicles.detector;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.entry.block.LocalizationDetector;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class OperatorObject {

	private static HashMap<Byte, OperatorObject> allOperators;
	public static final OperatorObject MAIN;

	static {
		allOperators = new HashMap<Byte, OperatorObject>();
		HashMap<Byte, OperatorObject> operators = new HashMap<Byte, OperatorObject>();

        MAIN = new OperatorObject(operators, 0, LocalizationDetector.OUTPUT, 1) {
			public boolean inTab() {
				return false;
			}
			
			public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, vehicle, depth);
			}			
		};
		
		new OperatorObject(operators, 1, LocalizationDetector.AND, 2) {
			public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, vehicle, depth) && B.evaluateLogicTree(detector, vehicle, depth);
			}			
		};
		
		new OperatorObject(operators, 2, LocalizationDetector.OR, 2) {
			public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, vehicle, depth) || B.evaluateLogicTree(detector, vehicle, depth);
			}	
		};
		new OperatorObject(operators, 3, LocalizationDetector.NOT, 1) {
            @Override
			public boolean isChildValid(OperatorObject child) {
				return getId() != child.id;
			}
			
			public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth,  LogicObject A, LogicObject B) {
				return !A.evaluateLogicTree(detector, vehicle, depth);
			}		
		};
		
		new OperatorObject(operators, 4, LocalizationDetector.XOR, 2) {
			public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, vehicle, depth) != B.evaluateLogicTree(detector, vehicle, depth);
			}		
		};
		new OperatorObjectRedirection(operators, 5, LocalizationDetector.TOP_UNIT, 0, 1, 0);
		new OperatorObjectRedirection(operators, 6, LocalizationDetector.BOTTOM_UNIT, 0, -1, 0);
		new OperatorObjectRedirection(operators, 7, LocalizationDetector.NORTH_UNIT, 0, 0, -1);
		new OperatorObjectRedirection(operators, 8, LocalizationDetector.WEST_UNIT, -1, 0, 0);
		new OperatorObjectRedirection(operators, 9, LocalizationDetector.SOUTH_UNIT, 0, 0, 1);
		new OperatorObjectRedirection(operators, 10, LocalizationDetector.EAST_UNIT, 1, 0, 0);

		//Note that IDs are also used by the specific types, the next ID here shouldn't be 11, that one is already in use.
		
		for (DetectorType type : DetectorType.values()) {
			type.initOperators(new  HashMap<Byte, OperatorObject>(operators));
		}
	}
	
	
	public static Collection<OperatorObject> getOperatorList(int meta) {
		return DetectorType.getTypeFromMeta(meta).getOperators().values();
	}
	
	public static HashMap<Byte, OperatorObject> getAllOperators() {
		return allOperators;
	}	
	
	public static class OperatorObjectRedirection extends OperatorObject {
		private int x;
		private int y;
		private int z;
		public OperatorObjectRedirection(HashMap<Byte, OperatorObject> operators, int ID, ILocalizedText name, int x, int y, int z) {
			super(operators, ID, name, 0);
			
			this.x = x;
			this.y = y;
			this.z = z;			
		}
		
		@Override
		public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth, LogicObject A, LogicObject B) {
			int x = this.x + detector.xCoord;
			int y = this.y + detector.yCoord;
			int z = this.z + detector.zCoord;
			
			TileEntity tileentity = detector.getWorldObj().getTileEntity(x, y, z);
            return tileentity != null && tileentity instanceof TileEntityDetector && ((TileEntityDetector) tileentity).evaluate(vehicle, depth);
		}
	}
	
	public static class OperatorObjectRedstone extends OperatorObject {
		private int x;
		private int y;
		private int z;
		public OperatorObjectRedstone(HashMap<Byte, OperatorObject> operators, int ID, ILocalizedText name, int x, int y, int z) {
			super(operators, ID, name, 0);
			
			this.x = x;
			this.y = y;
			this.z = z;			
		}
		
		@Override
		public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth, LogicObject A, LogicObject B) {
			int x = this.x + detector.xCoord;
			int y = this.y + detector.yCoord;
			int z = this.z + detector.zCoord;
			
			
			if (this.x == 0 && this.y == 0 && this.z == 0) {
				return detector.getWorldObj().isBlockIndirectlyGettingPowered(x, y, z);
			}else{
				int direction;
				if (this.y > 0) {
					direction = 0;
				}else if(this.y < 0) {
					direction = 1;
				}else if(this.x > 0) {
					direction = 4;
				}else if(this.x < 0) {
					direction = 5;
				}else if(this.z > 0) {
					direction = 2;
				}else{
					direction = 3;
				}
				
		        return detector.getWorldObj().getIndirectPowerLevelTo(x, y, z, direction) > 0;
			}
		}
	}	
	
	private byte id;
	private ILocalizedText name;
	private int children;
	
	
	public OperatorObject(HashMap<Byte, OperatorObject> operators, int id, ILocalizedText name, int children) {
		this.id = (byte)id;
		this.name = name;
		this.children = children;
		
		operators.put(this.id, this);
		allOperators.put(this.id, this);
	}
	
	public byte getId() {
		return id;
	}
	
	public String getName() {
		return name.translate();
	}
	
	public int getChildCount() {
		return children;
	}
	
	public boolean inTab() {
		return true;
	}

    public boolean isChildValid(OperatorObject child) {
        return true;
    }

	public boolean evaluate(TileEntityDetector detector, VehicleBase vehicle, int depth, LogicObject A, LogicObject B) {
		return false;
	}


	
}
