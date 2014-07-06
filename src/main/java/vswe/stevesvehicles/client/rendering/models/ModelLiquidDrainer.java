package vswe.stevesvehicles.client.rendering.models;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;

@SideOnly(Side.CLIENT)
public class ModelLiquidDrainer extends ModelCleaner {
    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/cleanerModelLiquid.png");

    @Override
    public ResourceLocation getResource(ModuleBase module) {
        return TEXTURE;
    }
}
