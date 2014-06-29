package vswe.stevesvehicles.old.Helpers;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class BlockCoordinate {
	private int x;
	private int y;
	private int z;
	public BlockCoordinate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}		
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockCoordinate) {
			BlockCoordinate coord = (BlockCoordinate)obj;
			return this.x == coord.x &&
					this.y == coord.y &&
					this.z == coord.z;
		}else{
			return false;
		}
	}
	
	public double getDistToVehicleSquared(VehicleBase vehicle) {
		int xDif = this.x - vehicle.x();
		int yDif = this.y - vehicle.y();
		int zDif = this.z - vehicle.z();
		
		return Math.pow(xDif, 2)
			 + Math.pow(yDif, 2)
			 + Math.pow(zDif, 2);
	}
	
	public double getHorizontalDistToVehicleSquared(VehicleBase vehicle) {
		int xDif = this.x - vehicle.x();
		int zDif = this.z - vehicle.z();
		
		return Math.pow(xDif, 2)
			 + Math.pow(zDif, 2);
	}		
}