package vswe.stevesvehicles.client.gui.detector;


import net.minecraft.util.IIcon;
import vswe.stevesvehicles.detector.LogicObject;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.client.gui.screen.GuiDetector;

public class DropDownMenuModules extends DropDownMenuPages {
    public DropDownMenuModules(int index) {
        super(index);
    }

    @Override
    public void drawMain(GuiDetector gui, int x, int y) {
        super.drawMain(gui, x, y);

        ResourceHelper.bindResource(GuiDetector.MODULE_TEXTURE);
        int modulePosId = 0;
        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (module.getIsValid()) {
                drawContent(gui, modulePosId, module.getIcon());

                modulePosId++;
            }
        }
    }

    @Override
    public void drawMouseOver(GuiDetector gui, int x, int y) {
        int modulePosId = 0;
        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (module.getIsValid()) {

                int[] target = getContentRect(modulePosId);
                if (gui.drawMouseOver(module.getName(), x, y, target)) {
                    break;
                }

                modulePosId++;
            }
        }
    }

    @Override
    public void onClick(GuiDetector gui, int x, int y) {
        super.onClick(gui, x, y);

        if (getScroll() != 0) {
            int modulePosId = 0;
            for (ModuleData module : ModuleRegistry.getAllModules()) {
                if (module.getIsValid()) {

                    int[] target = getContentRect(modulePosId);
                    if (gui.inRect(x,y, target)) {
                        gui.currentObject = new LogicObject((byte)0, (byte)ModuleRegistry.getIdFromModule(module));

                        return;
                    }

                    modulePosId++;
                }
            }
        }
    }

    private void drawContent(GuiDetector gui, int index, IIcon icon) {
        int[] rect = getContentRect(index);
        if (rect == null) {
            return;
        }

        int gap = rect[1] - getMainRect()[1] + rect[3];

        if (gap > 0) {
            int height = Math.min(rect[3], gap);
            int offset = rect[3] - height;
            gui.drawIcon(icon, gui.getGuiLeft() + rect[0],  gui.getGuiTop() + rect[1] + offset, rect[2] / 16F, height / 16F, 0F, offset / 16F);
        }
    }

    @Override
    protected int getPageCount() {
        int count = 0;
        for (ModuleData moduleData : ModuleRegistry.getAllModules()) {
            if (moduleData.getIsValid()) {
                count++;
            }
        }
        return (int)Math.ceil(count / (float)getObjectsPerPage());
    }
}
