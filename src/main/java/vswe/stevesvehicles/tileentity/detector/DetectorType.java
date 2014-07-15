package vswe.stevesvehicles.tileentity.detector;

import java.util.HashMap;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.localization.entry.block.LocalizationDetector;
import vswe.stevesvehicles.block.BlockRailAdvancedDetector;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.recipe.IRecipeOutput;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;
import vswe.stevesvehicles.recipe.ModuleRecipeShapeless;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.tileentity.TileEntityDetector;


public enum DetectorType implements IRecipeOutput {
	NORMAL(0, true, false, true,
			"detectors/manager/bot",
			"detectors/manager/top",
			"detectors/manager/yellow",
			"detectors/manager/blue",
			"detectors/manager/green",
			"detectors/manager/red"),
			
	UNIT(1, false, false, false,
		"detectors/unit/bot",
		"detectors/unit/bot",
		"detectors/unit/yellow",
		"detectors/unit/blue",
		"detectors/unit/green",
		"detectors/unit/red"),	
	
		
	STOP(2, true, true, false,
		"detectors/station/bot",
		"detectors/station/top",
		"detectors/station/yellow",
		"detectors/station/blue",
		"detectors/station/green",
		"detectors/station/red") {

		@Override
		public void activate(TileEntityDetector detector, VehicleBase vehicle) {
            ((EntityModularCart)vehicle.getEntity()).releaseCart();		 //TODO
		}
		
	},
		
	JUNCTION(3, true, false, false,
			"detectors/junction/bot",
			"detectors/junction/top",
			"detectors/junction/yellow",
			"detectors/junction/blue",
			"detectors/junction/green",
			"detectors/junction/red") {
		
		
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
			"detectors/redstone/bot",
			"detectors/redstone/top",
			"detectors/redstone/yellow",
			"detectors/redstone/blue",
			"detectors/redstone/green",
			"detectors/redstone/red") {
			
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


    @Override
    public ItemStack getItemStack() {
        return new ItemStack(ModBlocks.DETECTOR_UNIT.getBlock(), 1, meta);
    }

    public void addShapedRecipe(Object ... recipe) {
        GameRegistry.addRecipe(new ModuleRecipeShaped(this, 3, 3, recipe));
    }

    public void addShapelessRecipe(Object ... recipe) {
        GameRegistry.addRecipe(new ModuleRecipeShapeless(this, recipe));
    }
}
