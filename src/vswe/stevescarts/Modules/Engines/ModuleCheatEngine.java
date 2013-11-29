package vswe.stevescarts.Modules.Engines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleCheatEngine extends ModuleEngine {

	public ModuleCheatEngine(MinecartModular cart) {
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
	    drawString(gui,"Creative", 8, 6, 0x404040);
		drawString(gui,"Engine", 8, 16, 0x404040);
        drawString(gui,"Power Level: " + getFuelLevel(), 8, 42, 0x404040);
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