package vswe.stevesvehicles.module.common.storage.barrel;



import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.container.slots.SlotBarrel;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.module.common.storage.ModuleStorage;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.vehicle.VehicleBase;

public abstract class ModuleBarrel extends ModuleStorage {
    public ModuleBarrel(VehicleBase vehicleBase) {
        super(vehicleBase);
    }

    @Override
    public boolean hasGui(){
        return true;
    }

    @Override
    protected SlotBase getSlot(int slotId, int x, int y) {
        return new SlotBarrel(getVehicle().getVehicleEntity(), this, slotId, 8 + x * 18, 19 + y * 30, y == 0);
    }

    @Override
    protected int getInventoryWidth() {
        return 1;
    }

    @Override
    protected int getInventoryHeight() {
        return 2;
    }




    @Override
    public int guiWidth() {
        return 95;
    }

    @Override
    public int guiHeight() {
        return 75;
    }

    private static final int BARREL_SRC_X = 1;
    private static final int BARREL_SRC_Y = 1;
    private static final int BARREL_X = 30;
    private static final int BARREL_Y = 15;
    private static final int BARREL_SIZE = 54;

    private static final int STACK_COUNT_X = 16;
    private static final int STACK_COUNT_Y = 3;
    private static final int STACK_COUNT_WIDTH = 24;

    private static final int STACK_X = 19;
    private static final int STACK_Y = 17;

    private static final int BAR_X = 3;
    private static final int BAR_Y = 46;
    private static final int BAR_SRC_X = 1;
    private static final int BAR_SRC_Y = 56;
    private static final int BAR_WIDTH = 47;
    private static final int BAR_HEIGHT = 5;

    private static final int SIGN_SRC_X = 1;
    private static final int SIGN_SRC_Y = 62;
    private static final int SIGN_WIDTH = 14;
    private static final int SIGN_HEIGHT = 8;
    private static final int SIGN_X = 20;
    private static final int SIGN_Y = 40;

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/barrel.png");

    @Override
    public void drawBackground(GuiVehicle gui, int x, int y) {
        ResourceHelper.bindResource(TEXTURE);
        drawImage(gui, BARREL_X, BARREL_Y, BARREL_SRC_X, BARREL_SRC_Y, BARREL_SIZE, BARREL_SIZE);

        ItemStack item = getStoredItem();
        if (item != null) {
            int max = getMaxItems(false);
            if (max > 0) {
                int barWidth = BAR_WIDTH * getTotalCount() / max;
                if (barWidth > 0) {
                    int srcX = BAR_SRC_X + BAR_WIDTH - barWidth;
                    if (barWidth < BAR_WIDTH) {
                        srcX++;
                    }
                    drawImage(gui, BARREL_X + BAR_X, BARREL_Y + BAR_Y, srcX, BAR_SRC_Y, barWidth, BAR_HEIGHT);
                }
            }
        }



        drawImage(gui, BARREL_X + SIGN_X, BARREL_Y + SIGN_Y, SIGN_SRC_X, SIGN_SRC_Y, SIGN_WIDTH, SIGN_HEIGHT);
    }

    @Override
    public void drawBackgroundItems(GuiVehicle gui, int x, int y) {
        ItemStack item = getStoredItem();
        if (item != null) {
            drawItemInInterface(gui, item, BARREL_X + STACK_X, BARREL_Y + STACK_Y);
        }
    }

    @Override
    public void drawForeground(GuiVehicle gui) {
        drawString(gui, getModuleName(), 8, 6, 0x404040);

        int y = BARREL_Y + STACK_COUNT_Y;
        String str = String.valueOf(getStackCount());
        float size;
        switch (str.length()) {
            case 1:
            case 2:
                y += 1;
                size = 1F;
                break;
            case 3:
                y += 2;
                size = 0.9F;
                break;
            case 4:
                y += 2;
                size = 0.7F;
                break;
            default:
                size = 0.5F;
        }

        drawScaledCenteredString(gui, str, BARREL_X + STACK_COUNT_X, y, STACK_COUNT_WIDTH, size, 0x000000);


        ItemStack item = getStoredItem();
        if (item != null) {
            int max = getMaxItems(false);
            if (max > 0) {
                int percentage = 100 * getTotalCount() / max;
                drawScaledCenteredString(gui, percentage + "%", BARREL_X + SIGN_X, BARREL_Y + SIGN_Y + 3, SIGN_WIDTH, 0.5F, 0x000000);
            }
        }
    }

    @Override
    public int numberOfGuiData() {
        return 1;
    }

    private static final int TRANSFER_DELAY = 5;
    private int moveDelay = 0;

    @Override
    public void update() {
        super.update();

        if (++moveDelay >= TRANSFER_DELAY) {
            moveDelay = 0;

            int items = getTotalCount();
            if (items == 0) { //TODO only do this if it's not locked
                storedItem = null;
            }else{
                ItemStack input = getStack(0);
                if (canItemMergeWith(input, storedItem)) {
                    System.out.println(itemCount);
                    if (storedItem == null) {
                        storedItem = input.copy();
                    }

                    int max = getMaxItems(true);
                    int canMove = Math.min(max - itemCount, input.stackSize);
                    itemCount += canMove;
                    input.stackSize -= canMove;
                    if (input.stackSize == 0) {
                        setStack(0, null);
                    }
                }

                ItemStack output = getStack(1);
                if (itemCount > 0 && canItemMergeWith(storedItem, output)) {
                    if (output == null) {
                        output = storedItem.copy();
                        setStack(1, output);
                        output.stackSize = 0;
                    }

                    int canMove = Math.min(output.getMaxStackSize() - output.stackSize, itemCount);
                    output.stackSize += canMove;
                    itemCount -= canMove;
                }
            }
        }

    }

    private ItemStack storedItem;
    private int itemCount;

    private ItemStack getStoredItem() {
        ItemStack output = getStack(1);
        if (output != null) {
            return output;
        }else if (storedItem != null){
            return storedItem;
        }else{
            return getStack(0);
        }
    }

    //TODO remove
    @Override
    public String getModuleName() {
        if (super.getModuleName().contains("basic")) {
            return "Basic Barrel";
        }else if (super.getModuleName().contains("normal")) {
            return "Standard Barrel";
        }else if (super.getModuleName().contains("large")) {
            return "Large Barrel";
        }else{
            return "Barrel";
        }
    }

    @Override
    public void receiveGuiData(int id, short data) {
        if (id == 0) {
            itemCount = data;
            if (itemCount < 0) {
                itemCount += 65536;
            }
        }
    }

    private int getTotalCount() {
        ItemStack input = getStack(0);
        ItemStack output = getStack(1);

        return itemCount + (input != null ? input.stackSize : 0) + (output != null ? output.stackSize : 0);
    }

    private int getMaxItems(boolean onlyInternal) {
        ItemStack storedItem = getStoredItem();
        if (storedItem == null) {
            return 0;
        }else{
            int stackSize = storedItem.getMaxStackSize();
            int stackCount = getStackCount();
            if (onlyInternal) {
                stackCount -= 2;
            }
            return stackCount * stackSize;
        }
    }

    protected abstract int getStackCount();

    @Override
    protected void checkGuiData(Object[] info) {
        updateGuiData(info, 0, (short)itemCount);
    }

    public boolean isItemValid(ItemStack item) {
        return canItemMergeWith(item, getStoredItem());
    }


    private boolean canItemMergeWith(ItemStack item, ItemStack slotItem) {
        return item != null && (slotItem == null || item.isItemEqual(slotItem) && ItemStack.areItemStackTagsEqual(slotItem, item));
    }

    private static final String NBT_ITEM = "StoredItem";
    private static final String NBT_BARREL_COUNT = "BarrelCount";

    @Override
    protected void save(NBTTagCompound tagCompound) {
        if (storedItem != null) {
            NBTTagCompound itemCompound = new NBTTagCompound();
            storedItem.writeToNBT(itemCompound);
            itemCompound.setShort(NBT_BARREL_COUNT, (short)itemCount);
            tagCompound.setTag(NBT_ITEM, itemCompound);
        }
    }

    @Override
    protected void load(NBTTagCompound tagCompound) {
       if (tagCompound.hasKey(NBT_ITEM)) {
           NBTTagCompound itemCompound = tagCompound.getCompoundTag(NBT_ITEM);
           storedItem = ItemStack.loadItemStackFromNBT(itemCompound);
           itemCount = itemCompound.getShort(NBT_BARREL_COUNT);
           if (itemCount < 0) {
               itemCount += 65536;
           }
       }else{
           storedItem = null;
           itemCount = 0;
       }
    }
}
