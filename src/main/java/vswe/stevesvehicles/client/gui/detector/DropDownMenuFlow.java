package vswe.stevesvehicles.client.gui.detector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.detector.LogicObjectOperator;
import vswe.stevesvehicles.detector.OperatorObject;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;

@SideOnly(Side.CLIENT)
public class DropDownMenuFlow extends DropDownMenu {
    public DropDownMenuFlow(int index) {
        super(index);
    }


    @Override
    public void drawMain(GuiDetector gui, int x, int y) {
        super.drawMain(gui, x, y);

        ResourceHelper.bindResource(GuiDetector.TEXTURE);
        int flowPosId = 0;
        for (OperatorObject operator : OperatorObject.getOperatorList(gui.getDetector().getBlockMetadata())) {
            if (operator.inTab()) {
                drawContent(gui, flowPosId, operator.getId());
                flowPosId++;
            }
        }
    }

    @Override
    public void drawMouseOver(GuiDetector gui, int x, int y) {
        int flowPosId = 0;
        for (OperatorObject operator : OperatorObject.getOperatorList(gui.getDetector().getBlockMetadata())) {
            if (operator.inTab()) {
                int[] target = getContentRect(flowPosId);

                if (gui.drawMouseOver(operator.getName(), x, y, target)) {
                    break;
                }

                flowPosId++;
            }
        }
    }

    @Override
    public void onClick(GuiDetector gui, int x, int y) {
        super.onClick(gui, x, y);

        if (getScroll() != 0) {
            int flowPosId = 0;
            for (OperatorObject operator : OperatorObject.getOperatorList(gui.getDetector().getBlockMetadata())) {
                if (operator.inTab()) {
                    int[] target =  getContentRect(flowPosId);

                    if (gui.inRect(x, y, target)) {
                        gui.currentObject = new LogicObjectOperator((byte)0, operator);

                        return;
                    }

                    flowPosId++;
                }
            }
        }
    }

    private void drawContent(GuiDetector gui, int targetId, int sourceId) {
        int[] src = getSource(gui, sourceId);

        drawContent(gui, targetId, src[0], src[1], 256);
    }

    public static int[] getSource(GuiDetector gui, int id) {
        int x = id % 11;
        int y = id / 11;

        return new int[] {1 + x * 21, gui.getYSize() + 18 + y * 12};
    }

    @Override
    protected int getObjectsPerRow() {
        return 9;
    }

    @Override
    protected int getObjectRows() {
        return 10;
    }

    @Override
    protected int getObjectWidth() {
        return 20;
    }

    @Override
    protected int getObjectHeight() {
        return 11;
    }

    @Override
    protected int getObjectStartY() {
        return 34;
    }
}
