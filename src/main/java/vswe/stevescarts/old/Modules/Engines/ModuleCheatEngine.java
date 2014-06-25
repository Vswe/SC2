package vswe.stevescarts.old.Modules.Engines;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Interfaces.GuiMinecart;

public class ModuleCheatEngine extends ModuleEngine {

	public ModuleCheatEngine(EntityModularCart cart) {
		super(cart);

	}

	@Override
    public void loadFuel() {
		//no reason to load fuel
	}

	@Override
	public int getFuelLevel() {
		return 9001;
	}

	@Override
	public void setFuelLevel(int val) {
		//in your face
	}
	
	@Override
	public void drawForeground(GuiMinecart gui) {
        String[] split = getModuleName().split(" ");
	    drawString(gui, split[0], 8, 6, 0x404040);
        if (split.length > 1) {
		    drawString(gui,split[1], 8, 16, 0x404040);
        }
        drawString(gui, Localization.MODULES.ENGINES.OVER_9000.translate(String.valueOf(getFuelLevel())), 8, 42, 0x404040);
	}	
	
	@Override
	public int getTotalFuel() {
		return 9001000;
	}
	
	@Override
	public float[] getGuiBarColor() {
		return new float[] {0.97F, 0.58F, 0.11F};
	}
	
	@Override
	public boolean hasSlots() {
		return false;
	}

}