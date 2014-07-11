package vswe.stevesvehicles.detector;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.localization.entry.block.LocalizationDetector;
import vswe.stevesvehicles.block.BlockRailAdvancedDetector;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.tileentity.TileEntityDetector;


public enum DetectorType {
	NORMAL(0, true, false, true,
			"detector_manager_bot",
			"detector_manager_top",
			"detector_manager_yellow",
			"detector_manager_blue",
			"detector_manager_green",
			"detector_manager_red"),
			
	UNIT(1, false, false, false,
		"detector_manager_bot",
		"detector_manager_bot",
		"detector_unit_yellow",
		"detector_unit_blue",
		"detector_unit_green",
		"detector_unit_red"),	
	
		
	STOP(2, true, true, false,
		"detector_manager_bot",
		"detector_station_top",
		"detector_station_yellow",
		"detector_station_blue",
		"detector_station_green",
		"detector_station_red") {

		@Override
		public void activate(TileEntityDetector detector, VehicleBase vehicle) {
            ((EntityModularCart)vehicle.getEntity()).releaseCart();		 //TODO
		}
		
	},
		
	JUNCTION(3, true, false, false,
			"detector_manager_bot",
			"detector_junction_top",
			"detector_junction_yellow",
			"detector_junction_blue",
			"detector_junction_green",
			"detector_junction_red") {
		
		
		@Override
		public void activate(TileEntityDetector detector, VehicleBase vehicle) {
			update(detector, true);	
		}					

		@Override
		public void deactivate(TileEntityDetector detector) {
			update(detector, false);		
		}			
		
		private void update(TileEntityDetector detector, boolean flag) {
			if (detector.getWorldObj().getBlock(detector.xCoord, detector.yCoord + 1, detector.zCoord) == ModBlocks.ADVANCED_DETECTOR.getBlock()) {
                ((BlockRailAdvancedDetector)ModBlocks.ADVANCED_DETECTOR.getBlock()).refreshState(detector.getWorldObj(), detector.xCoord, detector.yCoord + 1, detector.zCoord, flag);
			}
		}
	},

	REDSTONE(4, false, false, false,
			"detector_redstone_bot",
			"detector_redstone_bot",
			"detector_redstone_yellow",
			"detector_redstone_blue",
			"detector_redstone_green",
			"detector_redstone_red") {
			
			@Override
			public void initOperators(HashMap<Byte, OperatorObject> operators) {
				super.initOperators(operators);
				
				new OperatorObject.OperatorObjectRedstone(operators, 11, LocalizationDetector.REDSTONE, 0, 0, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 12, LocalizationDetector.TOP_REDSTONE, 0, 1, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 13, LocalizationDetector.BOTTOM_REDSTONE, 0, -1, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 14, LocalizationDetector.NORTH_REDSTONE, 0, 0, -1);
				new OperatorObject.OperatorObjectRedstone(operators, 15, LocalizationDetector.WEST_REDSTONE, -1, 0, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 16, LocalizationDetector.SOUTH_REDSTONE, 0, 0, 1);
				new OperatorObject.OperatorObjectRedstone(operators, 17, LocalizationDetector.EAST_REDSTONE, 1, 0, 0);
			}	
			
		};	


	private int meta;
	private String[] textures;
	private IIcon[] icons;
	private boolean acceptCart;
	private boolean stopCart;
	private boolean emitRedstone;
	private HashMap<Byte, OperatorObject> operators;
	
	DetectorType(int meta, boolean acceptCart, boolean stopCart, boolean emitRedstone, String ... textures) {

		this.meta = meta;
		this.textures = textures;
		this.acceptCart = acceptCart;
		this.stopCart = stopCart;
		this.emitRedstone = emitRedstone;
	}


	public int getMeta() {
		return meta;
	}

	public String getName() {
		return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
	}

    public String getUnlocalizedName() {
        return "steves_vehicles:tile.detector_unit:" + toString().toLowerCase();
    }


	public void registerIcons(IIconRegister register) {
		icons = new IIcon[textures.length];
		for (int i = 0; i < textures.length; i++) {
			icons[i] = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + textures[i]);
		}
	}
	

	public IIcon getIcon(int side) {
		return icons[side];
	}
	
	public boolean canInteractWithCart() {
		return acceptCart;
	}
	
	public boolean shouldStopCart() {
		return stopCart;
	}
	
	public boolean shouldEmitRedstone() {
		return emitRedstone;
	}
	
	public void activate(TileEntityDetector detector, VehicleBase vehicle) {
		
	}
	
	public void deactivate(TileEntityDetector detector) {

	}
	
	
	public static DetectorType getTypeFromMeta(int meta) {
		return values()[meta & 7];
	}


	public void initOperators(HashMap<Byte, OperatorObject> operators) {
		this.operators = operators;
	}
	
	public HashMap<Byte, OperatorObject> getOperators() {
		return operators;
	}
	
}
