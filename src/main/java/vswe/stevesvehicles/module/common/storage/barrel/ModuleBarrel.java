package vswe.stevesvehicles.module.common.storage.barrel;



import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.container.slots.SlotBarrel;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.localization.entry.module.LocalizationBarrel;
import vswe.stevesvehicles.module.common.storage.ModuleStorage;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.old.Helpers.ColorHelper;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.vehicle.VehicleBase;

import java.util.List;

//TODO do something about advanced recipe modules so they know the amount of items in a barrel?
//TODO what should happen when a cart with a barrel is broken? Currently it will only drop the items in the input and output slots, but one can't just drop all the items (could be 65k items).
//TODO removing items by not shift clicking from the output slot can cause it not to update on the client properly, I think it has something to do with the fact that the slot content doesn't really change (it refills right away) and the server therefore thinks it doesn't have to send any info to the client. Only happens sometimes though, so might be something else.

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

    private static final int BOX_SRC_X = 1;
    private static final int BOX_SRC_Y = 84;
    private static final int BOX_WIDTH = 20;
    private static final int BOX_HEIGHT = 20;
    private static final int BOX_X = 17;
    private static final int BOX_Y = 15;

    private static final int SIGN_SRC_X = 1;
    private static final int SIGN_SRC_Y = 71;
    private static final int SIGN_WIDTH = 24;
    private static final int SIGN_HEIGHT = 12;
    private static final int SIGN_X = 15;
    private static final int SIGN_Y = 0;

    private static final int SMALL_SIGN_SRC_X = 1;
    private static final int SMALL_SIGN_SRC_Y = 62;
    private static final int SMALL_SIGN_WIDTH = 14;
    private static final int SMALL_SIGN_HEIGHT = 8;
    private static final int SMALL_SIGN_X = 20;
    private static final int SMALL_SIGN_Y = 40;

    private static final int LOCK_SRC_X = 1;
    private static final int LOCK_SRC_Y = 105;
    private static final int LOCK_WIDTH = 5;
    private static final int LOCK_HEIGHT = 6;
    private static final int LOCK_X = 36;
    private static final int LOCK_Y = 34;

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


        if (isLocked) {
            drawImage(gui, BARREL_X + LOCK_X, BARREL_Y + LOCK_Y, LOCK_SRC_X, LOCK_SRC_Y, LOCK_WIDTH, LOCK_HEIGHT);
        }

        drawHoverImage(gui, BARREL_X + BOX_X, BARREL_Y + BOX_Y, BOX_SRC_X, BOX_SRC_Y, BOX_WIDTH, BOX_HEIGHT, x, y);
        drawHoverImage(gui, BARREL_X + SMALL_SIGN_X, BARREL_Y + SMALL_SIGN_Y, SMALL_SIGN_SRC_X, SMALL_SIGN_SRC_Y, SMALL_SIGN_WIDTH, SMALL_SIGN_HEIGHT, x, y);
        drawHoverImage(gui, BARREL_X + SIGN_X, BARREL_Y + SIGN_Y, SIGN_SRC_X, SIGN_SRC_Y, SIGN_WIDTH, SIGN_HEIGHT, x, y);
    }

    private void drawHoverImage(GuiVehicle gui, int x, int y, int u, int v, int w, int h, int mX, int mY) {
        if (inRect(mX, mY, x, y, w, h )) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 0.8F, 0.8F);
        }

        drawImage(gui, x, y, u, v, w, h);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void drawMouseOver(GuiVehicle gui, int x, int y) {

        ItemStack item = getStoredItem();
        if (inRect(x, y, BARREL_X + BOX_X, BARREL_Y + BOX_Y, BOX_WIDTH, BOX_HEIGHT)) {
            if (item != null) {
                List<String> info = null;
                try {
                    //noinspection unchecked
                    info = item.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
                }catch (Exception ignored) {} //if some item fails to generate its tooltip we don't want to crash, just ignore displaying a tooltip

                if (info != null) {
                    if (isLocked) {
                        info.add("");
                        info.add(ColorHelper.ORANGE + LocalizationBarrel.LOCKED.translate());
                        info.add(ColorHelper.GRAY + LocalizationBarrel.UNLOCK.translate());
                    }else{
                        info.add(ColorHelper.GRAY + LocalizationBarrel.LOCK.translate());
                    }

                    drawStringOnMouseOver(gui, info, x, y);
                }
            }
        }else if(inRect(x, y, BARREL_X + SIGN_X, BARREL_Y + SIGN_Y, SIGN_WIDTH, SIGN_HEIGHT)) {
            String info = LocalizationBarrel.STACKS.translate(getColoredText(formatAmount(getStackCount()), ColorHelper.GREEN));
            if(item != null) {
                int stackSize = item.getMaxStackSize();
                int max = getMaxItems(false);
                if (stackSize > 0 && max > 0) {
                    info += "\n" +  LocalizationBarrel.STACK_SIZE.translate(getColoredText(formatAmount(stackSize), ColorHelper.YELLOW), getColoredText(formatAmount(max), ColorHelper.YELLOW));
                }
            }
            drawStringOnMouseOver(gui, info, x, y);
        }else if(inRect(x, y, BARREL_X + SMALL_SIGN_X, BARREL_Y + SMALL_SIGN_Y, SMALL_SIGN_WIDTH, SMALL_SIGN_HEIGHT)) {
            String info = null;
            if (item != null) {
                int max = getMaxItems(false);
                if (max > 0) {
                    int total = getTotalCount();

                    if (total == max) {
                        info = ColorHelper.RED + LocalizationBarrel.FULL.translate();
                    }else{
                        float percentage =(10000 * total / max) / 100F;
                        info = LocalizationBarrel.FILLED_PERCENTAGE.translate(getColoredText((String.format("%4.2f%%", percentage)), ColorHelper.GREEN));
                    }

                    info += "\n" + LocalizationBarrel.FILLED_AMOUNT.translate(getColoredText(formatAmount(total), ColorHelper.YELLOW) , getColoredText(formatAmount(max), ColorHelper.YELLOW));
                    int stackSize = item.getMaxStackSize();
                    if (stackSize > 0) {
                        info += "\n" + LocalizationBarrel.FILLED_AMOUNT_STACKS.translate(getColoredText(formatAmount(total / stackSize), ColorHelper.YELLOW) , getColoredText(formatAmount(total % stackSize), ColorHelper.YELLOW));
                    }
                }
            }else{
                info = LocalizationBarrel.EMPTY.translate();
            }

            if (info != null) {
                drawStringOnMouseOver(gui, info, x, y);
            }
        }


    }

    @Override
    protected void receivePacket(DataReader dr, EntityPlayer player) {
        isLocked = !isLocked;
    }

    @Override
    public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
        if (inRect(x, y, BARREL_X + BOX_X, BARREL_Y + BOX_Y, BOX_WIDTH, BOX_HEIGHT)) {
            sendPacketToServer(getDataWriter());
        }
    }

    private String getColoredText(String str, ColorHelper color) {
        return color + str + ColorHelper.WHITE;
    }

    private String formatAmount(int amount) {
        return String.format("%,d", amount).replace((char)160,(char)32);
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
                int total = getTotalCount();
                int percentage = 100 * total / max;
                String info = percentage + "%";
                if (max == total) {
                    info = ColorHelper.RED + info;
                }
                drawScaledCenteredString(gui, info, BARREL_X + SMALL_SIGN_X, BARREL_Y + SMALL_SIGN_Y + 3, SMALL_SIGN_WIDTH, 0.5F, 0x000000);
            }
        }
    }

    @Override
    public int numberOfGuiData() {
        return 2;
    }

    private static final int TRANSFER_DELAY = 5;
    private int moveDelay = 0;


    @Override
    public void update() {
        super.update();

        if (++moveDelay >= TRANSFER_DELAY) {
            moveDelay = 0;

            if (getVehicle().getWorld().isRemote) {
                ItemStack output = getStack(1);
                if (output != null && storedItem == null) {
                    storedItem = output.copy();
                    storedItem.stackSize = 1;
                }else if(output == null && storedItem != null && itemCount == 0 && !isLocked) {
                    storedItem = null;
                }
            }else{
                int items = getTotalCount();
                if (items == 0) {
                    if (storedItem != null) {
                        if (isLocked) {
                            ItemStack itemStack = storedItem.copy();
                            itemStack.stackSize = 0;
                            setStack(1, itemStack);
                        }else{
                            storedItem = null;
                        }
                    }
                }else{
                    ItemStack input = getStack(0);
                    if (canItemMergeWith(input, storedItem)) {

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

    }

    private boolean isLocked;
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

    @Override
    public void receiveGuiData(int id, short data) {
        if (id == 0) {
            itemCount = data;
            if (itemCount < 0) {
                itemCount += 65536;
            }
        }else if(id == 1) {
            isLocked = data != 0;
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
        updateGuiData(info, 1, (short)(isLocked ? 1 : 0));
    }

    public boolean isItemValid(ItemStack item) {
        return canItemMergeWith(item, getStoredItem());
    }


    private boolean canItemMergeWith(ItemStack item, ItemStack slotItem) {
        return item != null && (slotItem == null || item.isItemEqual(slotItem) && ItemStack.areItemStackTagsEqual(slotItem, item));
    }

    private static final String NBT_ITEM = "StoredItem";
    private static final String NBT_BARREL_COUNT = "BarrelCount";
    private static final String NBT_LOCKED = "Locked";

    @Override
    protected void save(NBTTagCompound tagCompound) {
        if (storedItem != null) {
            NBTTagCompound itemCompound = new NBTTagCompound();
            storedItem.writeToNBT(itemCompound);
            itemCompound.setShort(NBT_BARREL_COUNT, (short)itemCount);
            tagCompound.setTag(NBT_ITEM, itemCompound);
        }
        tagCompound.setBoolean(NBT_LOCKED, isLocked);
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
        isLocked = tagCompound.getBoolean(NBT_LOCKED);
    }
}
