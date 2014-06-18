package vswe.stevescarts.Modules.Workers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFertilizer;

public class ModuleFertilizer extends ModuleWorker implements ISuppliesModule {
	public ModuleFertilizer(MinecartModular cart) {
		super(cart);
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
	
		for (ModuleBase module : getCart().getModules()) {
			if (module instanceof ModuleFarmer) {
				range = ((ModuleFarmer)module).getExternalRange();
				break;
			}
		}
	}	


	@Override
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/fertilize.png");

		drawImage(gui, tankPosX, tankPosY, 0, 0, 18 , 27);
		float percentage = fert / (float)getMaxFert();
		int size = (int)(percentage * 23);
		drawImage(gui, tankPosX + 2, tankPosY + 2 + (23-size), 18, (23-size), 14, size);
	}

	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.FERTILIZERS.translate() + ": " + fert + " / " + getMaxFert(), x,y, tankPosX, tankPosY, 18, 27);
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
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
		return new SlotFertilizer(getCart(),slotId,8+x*18,23+y*18);
	}

	//return true when the work is done, false allow other modules to continue the work
	public boolean work() {
       //get the next block so the cart knows where to mine
        Vec3 next = getNextblock();
        //save thee coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

        //loop through the blocks in the "hole" in front of the cart

        for (int i = -range; i <= range; i++)
        {
            for (int j = -range; j <= range; j++)
            {
                //calculate the coordinates of this "hole"
                int coordX = x + i;
                int coordY = y - 1;
                int coordZ = z + j;

				fertilize(coordX, coordY, coordZ);
            }
        }

		return false;
    }

    private void fertilize(int x, int y, int z)
    {
		Block block = getCart().worldObj.getBlock(x, y + 1, z);
        int metadataOfBlockAbove = getCart().worldObj.getBlockMetadata(x, y + 1, z);
        int metadata = getCart().worldObj.getBlockMetadata(x, y, z);

		if (fert > 0) {
			if (block instanceof BlockCrops && metadataOfBlockAbove != 7)
			{
				if ((metadata > 0 && getCart().rand.nextInt(250) == 0) || (metadata == 0 && getCart().rand.nextInt(1000) == 0))
				{
					getCart().worldObj.setBlockMetadataWithNotify(x, y + 1, z, metadataOfBlockAbove + 1, 3);

					fert--;
				}
			}else  if (block instanceof BlockSapling && getCart().worldObj.getBlockLightValue(x, y + 2, z) >= 9) {
				if (getCart().rand.nextInt(100) == 0) {
					if (getCart().rand.nextInt(6) == 0) {
						getCart().worldObj.setBlockMetadataWithNotify(x, y+1, z, metadataOfBlockAbove | 8, 3);
						((BlockSapling) Blocks.sapling).func_149878_d(getCart().worldObj,x,y+1,z,getCart().rand);
					}
					fert--;
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
		updateGuiData(info, 0, (short)fert);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			fert = data;
		}
	}

	public void update() {
		super.update();

		loadSupplies();
	}

	private void loadSupplies() {
		if (getCart().worldObj.isRemote) {
			return;
		}

        if (getStack(0) != null)
        {
            boolean isBone = getStack(0).getItem() == Items.bone;
            boolean isBoneMeal = getStack(0).getItem() == Items.dye && getStack(0).getItemDamage() == 15;

            if (isBone || isBoneMeal)
            {
                int amount;

                if (isBoneMeal)
                {
                    amount = 1;
                }
                else
                {
                    amount = 3;
                }

                if (fert <= fertPerBonemeal * (maxStacksOfBones * 3 * 64 - amount) && getStack(0).stackSize > 0)
                {
                    getStack(0).stackSize--;
                    fert += amount * fertPerBonemeal;
                }

                if (getStack(0).stackSize == 0)
                {
                    setStack(0,null);
                }
            }
        }
	}

	private int getMaxFert() {
		return fertPerBonemeal * maxStacksOfBones * 3 * 64;
	}

    private int fert = 0;
    private final int fertPerBonemeal = 4;
	private final int maxStacksOfBones = 1;
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort(generateNBTName("Fert",id), (short)fert);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		fert = tagCompound.getShort(generateNBTName("Fert",id));
	}	
	
	
	@Override
	public boolean haveSupplies() {
		return fert > 0;
	}	
	
}