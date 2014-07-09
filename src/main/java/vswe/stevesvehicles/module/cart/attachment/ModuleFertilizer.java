package vswe.stevesvehicles.module.cart.attachment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.cart.LocalizationCartCultivationUtil;
import vswe.stevesvehicles.module.cart.ModuleWorker;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.tool.ModuleFarmer;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotFertilizer;

public class ModuleFertilizer extends ModuleWorker implements ISuppliesModule {
	public ModuleFertilizer(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//lower numbers are prioritized
	public byte getWorkPriority() {
		return 127;
	}

	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	protected int getInventoryWidth() {
		return 1;
	}

	private int tankPosX = guiWidth() - 21;
	private int tankPosY = 20;
	
	private int range = 1;
	
	public void init() {
		super.init();
	
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleFarmer) {
				range = ((ModuleFarmer)module).getExternalRange();
				break;
			}
		}
	}	


	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/fertilize.png");

		float percentage = fertilizerStorage / (float) getMaxFertilizerStorage();
		int size = (int)(percentage * 23);
		drawImage(gui, tankPosX + 2, tankPosY + 2 + (23-size), 20, 1 + (23-size), 14, size);

        drawImage(gui, tankPosX, tankPosY, 1, 1, 18 , 27);
	}

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, LocalizationCartCultivationUtil.FERTILIZER_TITLE.translate() + ": " + fertilizerStorage + " / " + getMaxFertilizerStorage(), x,y, tankPosX, tankPosY, 18, 27);
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}

	@Override
	public int guiWidth() {
		return super.guiWidth() + 25;
	}

	@Override
	public int guiHeight() {
		return Math.max(super.guiHeight(), 50);
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotFertilizer(getVehicle().getVehicleEntity(), slotId, 8 + x * 18, 23 + y * 18);
	}

	//return true when the work is done, false allow other modules to continue the work
    @Override
	public boolean work() {
       //get the next block so the cart knows where to mine
        Vec3 next = getNextBlock();
        //save thee coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

        //loop through the blocks in the "hole" in front of the cart

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                //calculate the coordinates of this "hole"
                int targetX = x + i;
                int targetY = y - 1;
                int targetZ = z + j;

				fertilize(targetX, targetY, targetZ);
            }
        }

		return false;
    }

    private void fertilize(int x, int y, int z) {
		Block block = getVehicle().getWorld().getBlock(x, y + 1, z);
        int metadataOfBlockAbove = getVehicle().getWorld().getBlockMetadata(x, y + 1, z);
        int metadata = getVehicle().getWorld().getBlockMetadata(x, y, z);

		if (fertilizerStorage > 0) {
			if (block instanceof BlockCrops && metadataOfBlockAbove != 7) {
				if ((metadata > 0 && getVehicle().getRandom().nextInt(250) == 0) || (metadata == 0 && getVehicle().getRandom().nextInt(1000) == 0)) {
					getVehicle().getWorld().setBlockMetadataWithNotify(x, y + 1, z, metadataOfBlockAbove + 1, 3);

					fertilizerStorage--;
				}
			}else if (block instanceof BlockSapling && getVehicle().getWorld().getBlockLightValue(x, y + 2, z) >= 9) {
				if (getVehicle().getRandom().nextInt(100) == 0) {
					if (getVehicle().getRandom().nextInt(6) == 0) {
						getVehicle().getWorld().setBlockMetadataWithNotify(x, y + 1, z, metadataOfBlockAbove | 8, 3);
						((BlockSapling) Blocks.sapling).func_149878_d(getVehicle().getWorld(), x, y + 1, z, getVehicle().getRandom());
					}
					fertilizerStorage--;
				}
			}
		}
    }

 	@Override
	public int numberOfGuiData() {
		return 1;
	}

	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short) fertilizerStorage);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			fertilizerStorage = data;
		}
	}

	public void update() {
		super.update();

		loadSupplies();
	}

	private void loadSupplies() {
		if (getVehicle().getWorld().isRemote) {
			return;
		}

        if (getStack(0) != null) {
            boolean isBone = getStack(0).getItem() == Items.bone;
            boolean isBoneMeal = getStack(0).getItem() == Items.dye && getStack(0).getItemDamage() == 15;

            if (isBone || isBoneMeal){
                int amount;

                if (isBoneMeal) {
                    amount = 1;
                }else {
                    amount = 3;
                }

                if (fertilizerStorage <= FERTILIZERS_PER_BONE_MEAL * (MAX_STACKS_OF_BONES * BONE_MEALS_PER_BONE * STACK_SIZE - amount) && getStack(0).stackSize > 0) {
                    getStack(0).stackSize--;
                    fertilizerStorage += amount * FERTILIZERS_PER_BONE_MEAL;
                }

                if (getStack(0).stackSize == 0) {
                    setStack(0,null);
                }
            }
        }
	}

	private int getMaxFertilizerStorage() {
		return FERTILIZERS_PER_BONE_MEAL * MAX_STACKS_OF_BONES * BONE_MEALS_PER_BONE * STACK_SIZE;
	}

    private int fertilizerStorage = 0;
    private static final int STACK_SIZE = 64;
    private static final int BONE_MEALS_PER_BONE = 3;
    private static final int FERTILIZERS_PER_BONE_MEAL = 4;
	private static final int MAX_STACKS_OF_BONES = 1;
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setShort("Fertilizers", (short) fertilizerStorage);
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		fertilizerStorage = tagCompound.getShort("Fertilizers");
	}	
	
	
	@Override
	public boolean haveSupplies() {
		return fertilizerStorage > 0;
	}	
	
}