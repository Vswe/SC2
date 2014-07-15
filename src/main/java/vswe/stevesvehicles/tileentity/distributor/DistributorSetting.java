package vswe.stevesvehicles.tileentity.distributor;
import java.util.ArrayList;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.entry.block.LocalizationDistributor;
import vswe.stevesvehicles.tileentity.TileEntityDistributor;
import vswe.stevesvehicles.tileentity.TileEntityManager;
	
public class DistributorSetting {

	public static ArrayList<DistributorSetting> settings;
	
	static {
		settings = new ArrayList<DistributorSetting>();
		settings.add(new DistributorSetting(0, true, LocalizationDistributor.ALL));
		settings.add(new DistributorSetting(1, false, LocalizationDistributor.ALL));
		
		settings.add(new DistributorSettingColor(2, true, LocalizationDistributor.RED, 1));
		settings.add(new DistributorSettingColor(3, false, LocalizationDistributor.RED, 1));
		settings.add(new DistributorSettingColor(4, true, LocalizationDistributor.BLUE, 2));
		settings.add(new DistributorSettingColor(5, false, LocalizationDistributor.BLUE, 2));
		settings.add(new DistributorSettingColor(6, true, LocalizationDistributor.YELLOW, 3));
		settings.add(new DistributorSettingColor(7, false, LocalizationDistributor.YELLOW, 3));
		settings.add(new DistributorSettingColor(8, true, LocalizationDistributor.GREEN, 4));
		settings.add(new DistributorSettingColor(9, false, LocalizationDistributor.GREEN, 4));
		
		settings.add(new DistributorSettingChunk(10, true, LocalizationDistributor.TOP_LEFT, 0));
		settings.add(new DistributorSettingChunk(11, false, LocalizationDistributor.TOP_LEFT, 0));
		settings.add(new DistributorSettingChunk(12, true, LocalizationDistributor.TOP_RIGHT, 1));
		settings.add(new DistributorSettingChunk(13, false, LocalizationDistributor.TOP_RIGHT, 1));
		settings.add(new DistributorSettingChunk(14, true, LocalizationDistributor.BOTTOM_LEFT, 2));
		settings.add(new DistributorSettingChunk(15, false, LocalizationDistributor.BOTTOM_LEFT, 2));
		settings.add(new DistributorSettingChunk(16, true, LocalizationDistributor.BOTTOM_RIGHT, 3));
		settings.add(new DistributorSettingChunk(17, false, LocalizationDistributor.BOTTOM_RIGHT, 3));

		settings.add(new DistributorSettingDirection(18, true, LocalizationDistributor.TO_VEHICLE, true));
		settings.add(new DistributorSettingDirection(19, false, LocalizationDistributor.TO_VEHICLE, true));
		settings.add(new DistributorSettingDirection(20, true, LocalizationDistributor.FROM_VEHICLE, false));
		settings.add(new DistributorSettingDirection(21, false, LocalizationDistributor.FROM_VEHICLE, false));
		
	}


	private int id;
	private int imageId;
	private boolean top;
	private ILocalizedText name;
	public DistributorSetting(int id, boolean top, ILocalizedText name) {
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
			return name.translate() + " (" + (getIsTop() ? LocalizationDistributor.TOP_MANAGER.translate() : LocalizationDistributor.BOTTOM_MANAGER.translate()) + ")";
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


}