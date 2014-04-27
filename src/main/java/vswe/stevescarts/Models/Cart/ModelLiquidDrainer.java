package vswe.stevescarts.Models.Cart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import vswe.stevescarts.Modules.ModuleBase;
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
