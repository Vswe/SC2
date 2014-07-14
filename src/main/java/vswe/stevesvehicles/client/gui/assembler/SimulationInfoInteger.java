package vswe.stevesvehicles.client.gui.assembler;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.client.gui.screen.GuiCartAssembler;
import vswe.stevesvehicles.localization.ILocalizedText;

public class SimulationInfoInteger extends SimulationInfo {
    private int maxValue;
    private int minValue;
    private int value;
    public SimulationInfoInteger(ILocalizedText name, String texture, int maxValue) {
        this(name, texture, 0, maxValue, 0);
    }

    public SimulationInfoInteger(ILocalizedText name, String texture, int minValue, int maxValue, int value) {
        super(name, texture);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.value = value;
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiCartAssembler gui, int i, int x, int y) {
        super.draw(gui, i, x, y);

        int rect[] = getRect(gui.getDropDownX(), gui.getDropDownY(), i);
        int subRect[] = getSubRect(gui.getDropDownX(), gui.getDropDownY(), i);

        if (getIsSubMenuOpen()) {
            drawIncrementBox(gui, x, y, getOffSetXForSubMenuBox(0, 2) + subRect[0], 3 + subRect[1]);
            drawDecrementBox(gui, x, y, getOffSetXForSubMenuBox(1, 2) + subRect[0], 3 + subRect[1]);
        }
        int targetX = rect[0] + 16;
        int targetY = rect[1] + 7;
        gui.getFontRenderer().drawString(String.valueOf(value), gui.getGuiLeft() + targetX + (value >= 10 ? -4 : 0), gui.getGuiTop() + targetY, 0x000000);
    }

    private static final int STANDARD_BOX_INCREASE_SRC_X = 12;
    private static final int STANDARD_BOX_DECREASE_SRC_X = 23;

    @SideOnly(Side.CLIENT)
    private void drawIncrementBox(GuiCartAssembler gui, int mouseX, int mouseY, int x, int y) {
        drawStandardBox(gui, mouseX, mouseY, x, y, STANDARD_BOX_INCREASE_SRC_X);
    }
    @SideOnly(Side.CLIENT)
    private void drawDecrementBox(GuiCartAssembler gui, int mouseX, int mouseY, int x, int y) {
        drawStandardBox(gui, mouseX, mouseY, x, y, STANDARD_BOX_DECREASE_SRC_X);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClick(GuiCartAssembler gui, int i, int x, int y) {
        if (getIsSubMenuOpen()) {
            int[] subRect = getSubRect(gui.getDropDownX(), gui.getDropDownY(), i);

            if (clickBox(gui, x,y, getOffSetXForSubMenuBox(0,2) + subRect[0],3 + subRect[1])) {
                value = Math.min(maxValue, value + 1);
            }else if (clickBox(gui, x, y, getOffSetXForSubMenuBox(1,2) + subRect[0],3 + subRect[1])) {
                value = Math.max(minValue, value - 1);
            }
        }
    }

    public int getValue() {
        return value;
    }
}
