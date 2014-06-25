package vswe.stevescarts.old.Helpers;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevescarts.old.Blocks.BlockRailAdvDetector;
import vswe.stevescarts.old.Blocks.ModBlocks;
import vswe.stevescarts.old.StevesCarts;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.TileEntities.TileEntityDetector;


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
		public void activate(TileEntityDetector detector, EntityModularCart cart) {
			cart.releaseCart();			
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
		public void activate(TileEntityDetector detector, EntityModularCart cart) {
			update(detector, true);	
		}					

		@Override
		public void deactivate(TileEntityDetector detector) {
			update(detector, false);		
		}			
		
		private void update(TileEntityDetector detector, boolean flag) {
			if (detector.getWorldObj().getBlock(detector.xCoord, detector.yCoord + 1, detector.zCoord) == ModBlocks.ADVANCED_DETECTOR.getBlock()) {
                ((BlockRailAdvDetector)ModBlocks.ADVANCED_DETECTOR.getBlock()).refreshState(detector.getWorldObj(), detector.xCoord, detector.yCoord + 1, detector.zCoord, flag);
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
				
				new OperatorObject.OperatorObjectRedstone(operators, 11, Localization.GUI.DETECTOR.REDSTONE, 0, 0, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 12, Localization.GUI.DETECTOR.REDSTONE_TOP, 0, 1, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 13, Localization.GUI.DETECTOR.REDSTONE_BOT, 0, -1, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 14, Localization.GUI.DETECTOR.REDSTONE_NORTH, 0, 0, -1);
				new OperatorObject.OperatorObjectRedstone(operators, 15, Localization.GUI.DETECTOR.REDSTONE_WEST, -1, 0, 0);
				new OperatorObject.OperatorObjectRedstone(operators, 16, Localization.GUI.DETECTOR.REDSTONE_SOUTH, 0, 0, 1);
				new OperatorObject.OperatorObjectRedstone(operators, 17, Localization.GUI.DETECTOR.REDSTONE_EAST, 1, 0, 0);
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
		return StatCollector.translateToLocal("item." + StevesCarts.instance.localStart + "BlockDetector" + meta + ".name");
	}



	public void registerIcons(IIconRegister register) {
		icons = new IIcon[textures.length];
		for (int i = 0; i < textures.length; i++) {
			icons[i] = register.registerIcon(StevesCarts.instance.textureHeader + ":" + textures[i]);
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
	
	public void activate(TileEntityDetector detector, EntityModularCart cart) {
		
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
