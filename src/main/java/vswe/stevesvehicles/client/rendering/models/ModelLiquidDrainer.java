package vswe.stevesvehicles.client.rendering.models;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.modules.ModuleBase;
@SideOnly(Side.CLIENT)
public class ModelLiquidDrainer extends ModelCleaner
{
	public String modelTexture(ModuleBase module) {
		return "/models/cleanerModelLiquid.png";
	}

    public ModelLiquidDrainer()
    {
		super();
    }
	
}
