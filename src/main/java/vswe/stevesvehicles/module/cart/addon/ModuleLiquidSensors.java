package vswe.stevesvehicles.module.cart.addon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleLiquidDrainer;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;

public class ModuleLiquidSensors extends ModuleAddon {
	public ModuleLiquidSensors(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	public void update() {
		super.update();

		if (isDrillSpinning()) {
			sensorRotation += 0.05F * multiplier;

			if ((multiplier == 1 && sensorRotation > Math.PI / 4) || (multiplier == -1 && sensorRotation < -Math.PI / 4)) {
				multiplier *=-1;
			}
		}else{
			if (sensorRotation != 0) {
				if (sensorRotation > 0) {
					sensorRotation -= 0.05F;
					if (sensorRotation < 0) {
						sensorRotation = 0;
					}
				}else{
					sensorRotation += 0.05F;
					if (sensorRotation > 0) {
						sensorRotation = 0;
					}
				}
			}

			if(activateTime >= 0) {
				activateTime++;
				if (activateTime >= 10) {
					setLight(1);
					activateTime = -1;
				}
			}
		}
	}

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0,1);
	}

	private void activateLight(int light) {
		if (getLight() == 3 && light == 2) {
			return;
		}
		setLight(light);
		activateTime = 0;
	}

	//called from any drill, byte data will contain which light number and if the drill is spinning or not
	public void getInfoFromDrill(byte data) {
		byte light = (byte)(data & 3);
		if (light != 1) {
			activateLight(light);
		}

		data &= ~3;
		data |= getLight();
		setSensorInfo(data);
	}

	private void setLight(int val) {
		if (isPlaceholder()) {
			return;
		}
		byte data = getDw(0);
		data &= ~3;
		data |= val;
		setSensorInfo(data);
	}

	private void setSensorInfo(int val) {
		if (isPlaceholder()) {
			return;
		}	
		updateDw(0,val);
	}

	private float sensorRotation;
	private int activateTime = -1;
	private int multiplier = 1;

	public int getLight() {
		if (isPlaceholder()) {
			return getSimInfo().getLiquidLight();
		}else{
			return getDw(0) & 3;
		}
	}

	protected boolean isDrillSpinning() {
		if (isPlaceholder()) {
			return getSimInfo().getDrillSpinning();
		}else{
			return (getDw(0) & 4) != 0;
		}
	}

	public float getSensorRotation() {
		return sensorRotation;
	}

	
	//check if it's dangerous to remove a certain block(only used if a addon allows the cart to use it)
	public boolean isDangerous(ModuleDrill drill, int x, int y, int z, int offsetX, int offsetY, int offsetZ) {
        int targetX = x + offsetX;
        int targetY = y + offsetY;
        int targetZ = z + offsetZ;

		Block block = getVehicle().getWorld().getBlock(targetX, targetY, targetZ);
		
        if (block == Blocks.lava) { //static lava
			handleLiquid(drill, targetX, targetY, targetZ);
			return true;
        }else if (block == Blocks.water) { //static water
			handleLiquid(drill, targetX, targetY, targetZ);
			return true;
        }else if (block != null && block instanceof IFluidBlock) { //static other //TODO is this really a static fluid
			handleLiquid(drill, targetX, targetY, targetZ);
			return true;		
		}

        //for moving there's different cases:
        //1. the liquid is above, -> it will fall down -> not good
        //2. the liquid is at the side -> might be alright(see below)
        //2.1. the liquid is at the side but has flown so far that it can't spread further -> nothing will happen -> it's alright
        //2.2. the liquid is already falling and will therefore not spread -> nothing will happen -> it's alright
        //2.2.E exception: if there's a block below it will spread anyways -> not good
        //Ignore the 2.3 ones, no liquid in the tunnel, it's easier to code and more convenient for the user
        //2.3. the liquid is at the side but has flown so far that it can only spread ONE block more -> might be alright(see below)
        //2.3.1. the liquid is at the bottom of the tunnel -> it will spread one block -> the cart is too far away -> it's alright
        //2.3.2. the liquid is not at the bottom -> it will spread one block and then fall -> it will flow to the bottom and start to spread -> the cart will be in the way -> not good
        //2.4.  none of the above -> the liquid will flow and destroy the cart -> not good
        //3. when the block is removed sand or gravel will fall down -> liquid on top of this will fall down -> the liquid will hit the cart -> not good (this is very difficult to detect(maybe not :P))
        boolean isWater = block == Blocks.water || block == Blocks.flowing_water || block == Blocks.ice /* ice */;
        boolean isLava = block == Blocks.lava || block == Blocks.flowing_lava;
		

		//boolean isOther = block != null && block instanceof IFluidBlock;

		
        boolean isLiquid = isWater || isLava; // || isOther;

		
        if (isLiquid && block != null) {
            //check for cases 1. and 2.
            if (offsetY == 1) {
				handleLiquid(drill, targetX, targetY, targetZ);
				return true; //case 1.
            }else {
                int m = getVehicle().getWorld().getBlockMetadata(targetX, targetY, targetZ);

                if ((m & 8) == 8) {
					
					
                    if (block.isBlockSolid(getVehicle().getWorld(),targetX, targetY - 1, targetZ,1)) {
						handleLiquid(drill, targetX, targetY, targetZ);
						return true; //case 2.2.E.
                    }else{
                        return false; //case 2.2.
                    }
                }else if (isWater && ((m & 7) == 7)){
                    return false; //case 2.1.
                }else if (isLava && ((m & 7) == 7) && getVehicle().getWorld().provider.isHellWorld){
                    return false; //case 2.1.
                }else if (isLava && ((m & 7) == 6)){
                    return false; //case 2.1.
				}	
                //TODO make a more advanced version of this, fluids are so more advanced than liquids
                /*else if (isOther &&	((m & 7) == ((IFluidBlock)block).getFlowDistance()))
                {
                    return false; //case 2.1.
                }*/               				
                else{
					handleLiquid(drill, targetX, targetY, targetZ);
                    return true; //case 2.4
                }
            }
        }else{
            //check for case 3
            if (offsetY == 1){
				//sand or gravel
                boolean isFalling = block instanceof BlockFalling;

                if (isFalling){
                    return  isDangerous(drill, targetX, targetY, targetZ,   0,  1,  0) ||
                            isDangerous(drill, targetX, targetY, targetZ,   1,  0,  0) ||
                            isDangerous(drill, targetX, targetY, targetZ,   -1, 0,  0) ||
                            isDangerous(drill, targetX, targetY, targetZ,   0,  0,  1) ||
                            isDangerous(drill, targetX, targetY, targetZ,   0,  0, -1);
                }
            }
        }

        return false;
    }	
	
	private void handleLiquid(ModuleDrill drill, int x, int y, int z) {
		ModuleLiquidDrainer liquidDrainer = null;
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleLiquidDrainer) {
				liquidDrainer = (ModuleLiquidDrainer)module;
				break;
			}
		}	
		
		if (liquidDrainer != null) {
			liquidDrainer.handleLiquid(drill, x, y, z);
		}
	}	
}