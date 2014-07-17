package vswe.stevesvehicles.client.rendering.models.boat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelHullTop extends ModelVehicle {

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}

    @Override
	protected int getTextureHeight() {
		return 16;
	}


	private final ResourceLocation resource;
	private boolean useColors;
	public ModelHullTop(ResourceLocation resource) {
		this(resource, true);
	}
	
    public ModelHullTop(ResourceLocation resource, boolean useColors) {
		this.resource = resource;
		this.useColors = useColors;

		ModelRenderer top = new ModelRenderer(this, 0, 0);
		addRenderer(top);

        top.addBox(
			-8, 	    //X
			-6, 	    //Y
			-1,	 	    //Z
			16,		    //Size X
			12,		    //Size Y
			2,			//Size Z
			0.0F
		);
        top.setRotationPoint(
			0.0F, 		//X
			-4,	        //Y
			0.0F		//Z
		);

		top.rotateAngleX = (float)-Math.PI / 2;
    }

    @Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		if (useColors && module != null) {
			float[] color = module.getVehicle().getColor();
			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		}
		super.render(module,yaw,pitch,roll, multiplier, partialTime);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }	
}
