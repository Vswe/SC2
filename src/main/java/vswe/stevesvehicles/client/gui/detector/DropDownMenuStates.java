package vswe.stevesvehicles.client.gui.detector;


import vswe.stevesvehicles.detector.LogicObject;
import vswe.stevesvehicles.detector.modulestate.ModuleState;
import vswe.stevesvehicles.detector.modulestate.registry.ModuleStateRegistry;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;

public class DropDownMenuStates extends DropDownMenuPages {
    public DropDownMenuStates(int index) {
        super(index);
    }

    @Override
    public void drawMain(GuiDetector gui, int x, int y) {
        super.drawMain(gui, x, y);

        int statePosId = 0;
        for (ModuleState state : ModuleStateRegistry.getAllStates()) {
            ResourceHelper.bindResource(state.getTexture());
            drawContent(gui, statePosId);

            statePosId++;
        }
    }

    @Override
    public void drawMouseOver(GuiDetector gui, int x, int y) {
        int statesPosId = 0;
        for (ModuleState state : ModuleStateRegistry.getAllStates()) {
            int[] target = getContentRect(statesPosId);
            if (gui.drawMouseOver(state.getName(), x, y, target)) {
                break;
            }

            statesPosId++;
        }
    }

    @Override
    public void onClick(GuiDetector gui, int x, int y) {
        super.onClick(gui, x, y);

        if (getScroll() != 0) {
            int statePosId = 0;
            for (ModuleState state : ModuleStateRegistry.getAllStates()) {
                int[] target = getContentRect(statePosId);
                if (gui.inRect(x, y, target)) {
                    gui.currentObject = new LogicObject((byte)2, (byte)ModuleStateRegistry.getIdFromState(state));

                    return;
                }
                statePosId++;
            }
        }
    }

    private void drawContent(GuiDetector gui, int index) {
        drawContent(gui, index, 0, 0, 16);
    }

    @Override
    protected int getPageCount() {
        return (int)Math.ceil(ModuleStateRegistry.getAllStates().size() / (float)getObjectsPerPage());
    }
}
