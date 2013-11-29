package vswe.stevescarts.Helpers;
import java.util.ArrayList;

import vswe.stevescarts.TileEntities.TileEntityDistributor;
import vswe.stevescarts.TileEntities.TileEntityManager;
	
public class DistributorSetting {

	public static ArrayList<DistributorSetting> settings;
	
	static {
		settings = new ArrayList<DistributorSetting>();
		settings.add(new DistributorSetting(0, true, "All"));
		settings.add(new DistributorSetting(1, false, "All"));
		
		settings.add(new distributorSettingColor(2, true, "Red", 1));	
		settings.add(new distributorSettingColor(3, false, "Red", 1));
		settings.add(new distributorSettingColor(4, true, "Blue", 2));	
		settings.add(new distributorSettingColor(5, false, "Blue", 2));	
		settings.add(new distributorSettingColor(6, true, "Yellow", 3));	
		settings.add(new distributorSettingColor(7, false, "Yellow", 3));	
		settings.add(new distributorSettingColor(8, true, "Green", 4));	
		settings.add(new distributorSettingColor(9, false, "Green", 4));
		
		settings.add(new distributorSettingChunk(10, true, "Top Left", 0));	
		settings.add(new distributorSettingChunk(11, false, "Top Left", 0));
		settings.add(new distributorSettingChunk(12, true, "Top Right", 1));	
		settings.add(new distributorSettingChunk(13, false, "Top Right", 1));
		settings.add(new distributorSettingChunk(14, true, "Bottom Left", 2));	
		settings.add(new distributorSettingChunk(15, false, "Bottom Left", 2));
		settings.add(new distributorSettingChunk(16, true, "Bottom Right", 3));	
		settings.add(new distributorSettingChunk(17, false, "Bottom Right", 3));	

		settings.add(new distributorSettingDirection(18, true, "To Cart", true));	
		settings.add(new distributorSettingDirection(19, false, "To Cart", true));	
		settings.add(new distributorSettingDirection(20, true, "From Cart", false));	
		settings.add(new distributorSettingDirection(21, false, "From Cart", false));		
		
	}


	private int id;
	private int imageId;
	private boolean top;
	private String name;
	public DistributorSetting(int id, boolean top, String name) {
		this.id = id;
		this.top = top;
		this.name = name;
		imageId = id / 2;
	}
	
	//used to determine if a chunk of 15 slots is valid or not
	public boolean isValid(TileEntityManager manager, int chunkId, boolean top) {
		return top == this.top;
	}
	
	public int getId() {
		return id;
	}
	
	public int getImageId() {
		return imageId;
	}
	
	public String getName(TileEntityManager[] manager) {
		if (manager != null && manager.length > 1) {
			return name + " (" + (getIsTop() ? "Top" : "Bot") + " Manager)";
		}else{
			return name;
		}	
	}
	
	public boolean getIsTop() {
		return top;
	}
	
	public boolean isEnabled(TileEntityDistributor distributor) {
		if (distributor.getInventories().length == 0) {
			return false;
		}else if(top){
			return distributor.hasTop;
		}else{
			return distributor.hasBot;
		}
	}
	
	
	
	private static class distributorSettingColor extends DistributorSetting {
		private int color;
		public distributorSettingColor(int id, boolean top, String name, int color) {
			super(id,top,name);
			this.color = color;
		}	

		public boolean isValid(TileEntityManager manager, int chunkId, boolean top) {
			if (manager.layoutType == 0) {
				return super.isValid(manager, chunkId, top);
			}else{
				return super.isValid(manager, chunkId, top) && manager.color[chunkId] == color;
			}
		}		
	}
	
	
	private static class distributorSettingChunk extends DistributorSetting {
		private int chunk;
		public distributorSettingChunk(int id, boolean top, String name, int chunk) {
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
	
	private static class distributorSettingDirection extends DistributorSetting {
		private boolean toCart;
		public distributorSettingDirection(int id, boolean top, String name, boolean toCart) {
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
	
}