package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPumpkinHullTop extends ModelHullTop
{

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return (module == null || isActive(module)) ? resourceactive : resourceidle;
	}
	
	private ResourceLocation resourceactive;
	private ResourceLocation resourceidle;	
    public ModelPumpkinHullTop(ResourceLocation resourceactive, ResourceLocation resourceidle)
    {
		super(resourceactive);
		this.resourceactive = resourceactive;
		this.resourceidle = resourceidle;

    }
	
	private boolean isActive(ModuleBase module) {
		long time = module.getVehicle().getWorld().getWorldInfo().getWorldTime() % 24000;
		return time >= 12000 && time <= 18000;
	}
	
}
