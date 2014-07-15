package vswe.stevesvehicles.tileentity.detector;


import vswe.stevesvehicles.client.gui.detector.DropDownMenuFlow;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class LogicObjectOperator extends LogicObject {
    private OperatorObject operator;
    public LogicObjectOperator(byte id, OperatorObject operator) {
        super(id, (short)operator.getId());
        this.operator = operator;
    }

    @Override
    public LogicObject copy(LogicObject parent) {
        LogicObject obj = new LogicObjectOperator(getId(), operator);
        obj.setParent(parent);
        return obj;
    }

    @Override
    public String getName() {
        String name = operator.getName();
        return name + "\nChild nodes: " + getChildren().size() + "/" + getMaxChildCount();
    }

    @Override
    public boolean canBeRemoved() {
        return operator.inTab();
    }

    @Override
    protected int getWidth() {
        return 20;
    }

    @Override
    protected int getHeight() {
        return 11;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void draw(GuiDetector gui, int mouseX, int mouseY) {
        ResourceHelper.bindResource(GuiDetector.TEXTURE);

        int[] src = DropDownMenuFlow.getSource(gui, data);
        gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, src[0], src[1], getWidth(), getHeight());

        if (gui.inRect(mouseX, mouseY, getRect())) {
            int overlayIndex;
            if (gui.currentObject == null) {
                overlayIndex = 2;
            }else if(hasRoomForChild() && isChildValid(gui.currentObject)) {
                overlayIndex = 0;
            }else{
                overlayIndex = 1;
            }

            gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y , 35 + overlayIndex * 21, 203, getWidth(), getHeight());
        }

        super.draw(gui, mouseX, mouseY);
    }

    @Override
    public boolean evaluateLogicTree(TileEntityDetector detector, VehicleBase vehicle, int depth) {
        if (!super.evaluateLogicTree(detector, vehicle, depth) || getChildren().size() != getMaxChildCount()) {
            return false;
        }

        LogicObject childA = operator.getChildCount() > 0 ? getChildren().get(0) : null;
        LogicObject childB = operator.getChildCount() > 1 ? getChildren().get(1) : null;

        return operator.evaluate(detector, vehicle, depth + 1, childA, childB);
    }

    @Override
    protected int getMaxChildCount() {
        return operator.getChildCount();
    }

    @Override
    public boolean isValidAsChild(LogicObjectOperator parent) {
        return parent.operator.isChildValid(operator);
    }

    @Override
    public boolean isChildValid(LogicObject child) {
        return !(level >= 4 && child.getMaxChildCount() > 0 || level >= 5) && child.isValidAsChild(this);
    }
}
