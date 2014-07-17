package vswe.stevesvehicles.client.rendering.models.cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelHull extends ModelVehicle {
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}

    @Override
	protected int getTextureHeight() {
		return 32;
	}

	private final ResourceLocation resource;
	
	
    public ModelHull(ResourceLocation resource) {
		this.resource = resource;

        ModelRenderer bot = new ModelRenderer(this, 0, 0);
        ModelRenderer front = new ModelRenderer(this, 0, 18);
        ModelRenderer left = new ModelRenderer(this, 0, 18);
        ModelRenderer right = new ModelRenderer(this, 0, 18);		
        ModelRenderer back = new ModelRenderer(this, 0, 18);

		addRenderer(bot);
		addRenderer(front);
		addRenderer(left);
		addRenderer(right);
		addRenderer(back);
		
        bot.addBox(
			-cartLength / 2,    //X
			-cartWidth / 2,	    //Y
			-1.0F, 			    //Z
			cartLength,		    //Size X
			cartWidth,		    //Size Y
			2,			        //Size Z
			0.0F
		);
        bot.setRotationPoint(
			0.0F, 			    //X
			cartOnGround,	    //Y
			0.0F			    //Z
		);

        front.addBox(
			-cartWidth / 2,			//X
			-cartHeight - 1, 		//Y
			-1.0F, 					//Z
			cartWidth, 				//Size X
			cartHeight,				//Size Y
			2,						//Size Z
			0.0F
		);
        front.setRotationPoint(
			-cartLength / 2 + 1,	//X
			cartOnGround,			//Y
			0.0F					//Z
		);

        left.addBox(
			-cartLength / 2 + 2,	//X
			-cartHeight - 1,		//Y
			-1.0F,					//Z
			cartLength - 4,			//Size X
			cartHeight,				//Size Y
			2,						//Size Z
			0.0F
		);
        left.setRotationPoint(
			0.0F,					//X
			cartOnGround,			//Y
			-cartWidth / 2 + 1		//Z
		);

        right.addBox(
			-cartLength / 2 + 2,	//X
			-cartHeight - 1,		//Y
			-1.0F,					//Z
			cartLength - 4,			//Size X
			cartHeight,				//Size Y
			2,						//Size Z
			0.0F
		);
        right.setRotationPoint(
			0.0F, 					//X
			cartOnGround,			//Y
			cartWidth / 2 - 1		//Z
		);

        back.addBox(
			-cartWidth / 2,			//X
			-cartHeight - 1,		//Y
			-1.0F,					//Z
			cartWidth , 			//Size X
			cartHeight,				//Size Y
			2,						//Size Z
			0.0F
		);
        back.setRotationPoint(
			cartLength / 2 - 1, 	//X
			cartOnGround,			//Y
			0.0F					//Z
		);		
		
		bot.rotateAngleX = ((float)Math.PI / 2F);
        front.rotateAngleY = ((float)Math.PI * 3F / 2F);
        left.rotateAngleY = (float)Math.PI;
		back.rotateAngleY = ((float)Math.PI / 2F);	

    }

    @Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		if (module != null) {
			float[] color = module.getVehicle().getColor();
			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		}
		super.render(module,yaw,pitch,roll, multiplier, partialTime);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }	
}
