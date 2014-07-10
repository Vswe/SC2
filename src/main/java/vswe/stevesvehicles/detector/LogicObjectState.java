package vswe.stevesvehicles.detector;


import vswe.stevesvehicles.client.gui.screen.GuiDetector;
import vswe.stevesvehicles.detector.modulestate.ModuleState;
import vswe.stevesvehicles.detector.modulestate.registry.ModuleStateRegistry;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class LogicObjectState extends LogicObject {
    private ModuleState state;
    public LogicObjectState(byte id, ModuleState state) {
        super(id, (short)ModuleStateRegistry.getIdFromState(state));
        this.state = state;
    }

    @Override
    public LogicObject copy(LogicObject parent) {
        LogicObject obj = new LogicObjectState(getId(), state);
        obj.setParent(parent);
        return obj;
    }

    @Override
    public String getName() {
        return state.getName();
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void draw(GuiDetector gui, int mouseX, int mouseY) {
        ResourceHelper.bindResource(GuiDetector.TEXTURE);
        int backgroundIndex = gui.inRect(mouseX, mouseY, getRect()) ? 1 : 0;
        gui.drawTexturedModalRect(gui.getGuiLeft()+ x, gui.getGuiTop() + y , 1 + backgroundIndex * 17, 203, getWidth(), getHeight());

        ModuleState state = ModuleStateRegistry.getStateFromId(data);
        if (state != null) {
            ResourceHelper.bindResource(state.getTexture());
            gui.drawRectWithTextureSize(gui.getGuiLeft() + x, gui.getGuiTop() + y, 0, 0, getWidth(), getHeight(), getWidth());
        }

        super.draw(gui, mouseX, mouseY);
    }

    @Override
    public boolean evaluateLogicTree(TileEntityDetector detector, VehicleBase vehicle, int depth) {
        return super.evaluateLogicTree(detector, vehicle, depth) && state.isValid(vehicle);
    }
}
