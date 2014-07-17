package vswe.stevesvehicles.client.rendering.models.common;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;

@SideOnly(Side.CLIENT)
public abstract class ModelEngineBase extends ModelVehicle {

	protected ModelRenderer anchor;
    public ModelEngineBase() {
		anchor = new ModelRenderer(this, 0, 0);
		addRenderer(anchor);

		anchor.setRotationPoint(
			10.5F, 		//X
			0.5F,		//Y
			-0F			//Z
		);		
		
		anchor.rotateAngleY = -(float)Math.PI / 2;
	}


}
