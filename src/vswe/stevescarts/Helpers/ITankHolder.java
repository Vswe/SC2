package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Interfaces.GuiBase;

public interface ITankHolder {


	public ItemStack getInputContainer(int tankid);
	public void clearInputContainer(int tankid);
	public void addToOutputContainer(int tankid, ItemStack item);
	public void onFluidUpdated(int tankid);
	@SideOnly(Side.CLIENT)
	public void drawImage(int tankid, GuiBase gui, Icon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY);
}