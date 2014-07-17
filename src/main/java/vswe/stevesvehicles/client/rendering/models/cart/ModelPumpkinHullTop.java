package vswe.stevesvehicles.client.rendering.models.cart;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.rendering.models.common.ModelHullTop;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPumpkinHullTop extends ModelHullTop {

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return (module == null || isActive(module)) ? resourceActive : resourceIdle;
	}
	
	private final ResourceLocation resourceActive;
	private final ResourceLocation resourceIdle;
    public ModelPumpkinHullTop(ResourceLocation resourceActive, ResourceLocation resourceIdle) {
		super(resourceActive);
		this.resourceActive = resourceActive;
		this.resourceIdle = resourceIdle;
    }
	
	private boolean isActive(ModuleBase module) {
		long time = module.getVehicle().getWorld().getWorldInfo().getWorldTime() % 24000;
		return time >= 12000 && time <= 18000;
	}
}
