package vswe.stevesvehicles.old.Upgrades;

public abstract class SimpleInventoryEffect extends InventoryEffect {

	private int inventoryWidth;
	private int inventoryHeight;
	public SimpleInventoryEffect(int inventoryWidth, int inventoryHeight) {
		super();
		this.inventoryWidth = inventoryWidth;
		this.inventoryHeight = inventoryHeight;
	}
	
	@Override
	public int getInventorySize() {
		return inventoryWidth * inventoryHeight;
	}
	
	@Override
	public int getSlotX(int id) {
		return (256 - 18 * inventoryWidth) / 2 + (id % inventoryWidth)* 18;
	}
	
	@Override	
	public int getSlotY(int id) {
		return (107 - 18 * inventoryHeight) / 2 + (id / inventoryWidth) * 18;
	}
	
}