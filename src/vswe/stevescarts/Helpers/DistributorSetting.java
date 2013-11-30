package vswe.stevescarts.Helpers;
import java.util.ArrayList;

import vswe.stevescarts.TileEntities.TileEntityDistributor;
import vswe.stevescarts.TileEntities.TileEntityManager;
	
public class DistributorSetting {

	public static ArrayList<DistributorSetting> settings;
	
	static {
		settings = new ArrayList<DistributorSetting>();
		settings.add(new DistributorSetting(0, true, Localization.GUI.DISTRIBUTOR.SETTING_ALL));
		settings.add(new DistributorSetting(1, false, Localization.GUI.DISTRIBUTOR.SETTING_ALL));
		
		settings.add(new distributorSettingColor(2, true, Localization.GUI.DISTRIBUTOR.SETTING_RED, 1));
		settings.add(new distributorSettingColor(3, false, Localization.GUI.DISTRIBUTOR.SETTING_RED, 1));
		settings.add(new distributorSettingColor(4, true, Localization.GUI.DISTRIBUTOR.SETTING_BLUE, 2));
		settings.add(new distributorSettingColor(5, false, Localization.GUI.DISTRIBUTOR.SETTING_BLUE, 2));
		settings.add(new distributorSettingColor(6, true, Localization.GUI.DISTRIBUTOR.SETTING_YELLOW, 3));
		settings.add(new distributorSettingColor(7, false, Localization.GUI.DISTRIBUTOR.SETTING_YELLOW, 3));
		settings.add(new distributorSettingColor(8, true, Localization.GUI.DISTRIBUTOR.SETTING_GREEN, 4));
		settings.add(new distributorSettingColor(9, false, Localization.GUI.DISTRIBUTOR.SETTING_GREEN, 4));
		
		settings.add(new distributorSettingChunk(10, true, Localization.GUI.DISTRIBUTOR.SETTING_TOP_LEFT, 0));
		settings.add(new distributorSettingChunk(11, false, Localization.GUI.DISTRIBUTOR.SETTING_TOP_LEFT, 0));
		settings.add(new distributorSettingChunk(12, true, Localization.GUI.DISTRIBUTOR.SETTING_TOP_RIGHT, 1));
		settings.add(new distributorSettingChunk(13, false, Localization.GUI.DISTRIBUTOR.SETTING_TOP_RIGHT, 1));
		settings.add(new distributorSettingChunk(14, true, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_LEFT, 2));
		settings.add(new distributorSettingChunk(15, false, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_LEFT, 2));
		settings.add(new distributorSettingChunk(16, true, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_RIGHT, 3));
		settings.add(new distributorSettingChunk(17, false, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_RIGHT, 3));

		settings.add(new distributorSettingDirection(18, true, Localization.GUI.DISTRIBUTOR.SETTING_TO_CART, true));
		settings.add(new distributorSettingDirection(19, false, Localization.GUI.DISTRIBUTOR.SETTING_TO_CART, true));
		settings.add(new distributorSettingDirection(20, true, Localization.GUI.DISTRIBUTOR.SETTING_FROM_CART, false));
		settings.add(new distributorSettingDirection(21, false, Localization.GUI.DISTRIBUTOR.SETTING_FROM_CART, false));
		
	}


	private int id;
	private int imageId;
	private boolean top;
	private Localization.GUI.DISTRIBUTOR name;
	public DistributorSetting(int id, boolean top, Localization.GUI.DISTRIBUTOR name) {
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
			return name.translate() + " (" + (getIsTop() ? Localization.GUI.DISTRIBUTOR.MANAGER_TOP.translate() : Localization.GUI.DISTRIBUTOR.MANAGER_BOT.translate()) + ")";
		}else{
			return name.translate();
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
		public distributorSettingColor(int id, boolean top, Localization.GUI.DISTRIBUTOR name, int color) {
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
		public distributorSettingChunk(int id, boolean top, Localization.GUI.DISTRIBUTOR name, int chunk) {
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
		public distributorSettingDirection(int id, boolean top, Localization.GUI.DISTRIBUTOR name, boolean toCart) {
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