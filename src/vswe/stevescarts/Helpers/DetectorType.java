package vswe.stevescarts.Helpers;

import java.util.HashMap;

import net.minecraft.block.BlockBaseRailLogic;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import vswe.stevescarts.Blocks.Blocks;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.OperatorObject.OperatorObjectRedirector;
import vswe.stevescarts.TileEntities.TileEntityDetector;


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
		public void activate(TileEntityDetector detector, MinecartModular cart) {
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
		public void activate(TileEntityDetector detector, MinecartModular cart) {
			update(detector, true);	
		}					

		@Override
		public void deactivate(TileEntityDetector detector) {
			update(detector, false);		
		}			
		
		private void update(TileEntityDetector detector, boolean flag) {
			if (detector.worldObj.getBlockId(detector.xCoord, detector.yCoord + 1, detector.zCoord) == Blocks.ADVANCED_DETECTOR.getId()) {
				(new BlockBaseRailLogic((BlockRailBase)Blocks.ADVANCED_DETECTOR.getBlock(), detector.worldObj, detector.xCoord, detector.yCoord + 1, detector.zCoord)).func_94511_a(flag, false);
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
	private Icon[] icons;
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



	public void registerIcons(IconRegister register) {
		icons = new Icon[textures.length];
		for (int i = 0; i < textures.length; i++) {
			icons[i] = register.registerIcon(StevesCarts.instance.textureHeader + ":" + textures[i]);
		}
	}
	

	public Icon getIcon(int side) {
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
	
	public void activate(TileEntityDetector detector, MinecartModular cart) {
		
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
