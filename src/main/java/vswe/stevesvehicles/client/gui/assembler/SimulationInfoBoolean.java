package vswe.stevesvehicles.client.gui.assembler;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.client.gui.screen.GuiCartAssembler;
import vswe.stevesvehicles.localization.ILocalizedText;

public class SimulationInfoBoolean extends SimulationInfo{
    private boolean value;
    public SimulationInfoBoolean(ILocalizedText name, String texture) {
        this(name, texture, false);
    }

    public SimulationInfoBoolean(ILocalizedText name, String texture, boolean defaultvalue) {
        super(name, texture);
        this.value = defaultvalue;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiCartAssembler gui, int i, int x, int y) {
        super.draw(gui, i, x, y);

        int rect[] = getRect(gui.getDropDownX(), gui.getDropDownY(), i);
        drawBooleanBox(gui, x, y, 5 + rect[0], 5 + rect[1], value);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClick(GuiCartAssembler gui, int i, int x, int y) {

        int[] rect = getRect(gui.getDropDownX(), gui.getDropDownY(), i);
        if (clickBox(gui, x, y, 5 + rect[0],5 + rect[1])) {
            value = !value;
        }
    }

    public boolean getValue() {
        return value;
    }
}
