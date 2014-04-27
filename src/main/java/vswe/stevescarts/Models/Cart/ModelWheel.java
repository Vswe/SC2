package vswe.stevescarts.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleAdvControl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelWheel extends ModelCartbase
{


	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/wheelModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		
	
	protected int getTextureWidth() {
		return 32;
	}
	protected int getTextureHeight() {
		return 32;
	}

	public float extraMult() {
		return 0.65F;
	}
	
	private ModelRenderer anchor;
    public ModelWheel()
    {


		anchor = new ModelRenderer(this);
		AddRenderer(anchor);
		
		anchor.setRotationPoint(
			-10, 		//X	
			-5,			//Y
			0			//Z
		);	


		
		
		ModelRenderer top = new ModelRenderer(this, 0, 0);
		anchor.addChild(top);
		fixSize(top);

		
		top.addBox(
			-4.5F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			9,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		top.setRotationPoint(
			0, 		//X	
			-6,			//Y
			0			//Z
		);	
		top.rotateAngleY = (float)-Math.PI / 2;
		
		
		ModelRenderer topleft = new ModelRenderer(this, 0, 4);
		anchor.addChild(topleft);
		fixSize(topleft);

		
		topleft.addBox(
			-1F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		topleft.setRotationPoint(
			0, 		//X	
			-4F,			//Y
			-5.5F			//Z
		);	
		topleft.rotateAngleY = (float)-Math.PI / 2;


		
		ModelRenderer topright = new ModelRenderer(this, 0, 4);
		anchor.addChild(topright);
		fixSize(topright);

		
		topright.addBox(
			-1F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		topright.setRotationPoint(
			0, 		//X	
			-4F,			//Y
			5.5F			//Z
		);	
		topright.rotateAngleY = (float)-Math.PI / 2;
		

		ModelRenderer left = new ModelRenderer(this, 0, 12);
		anchor.addChild(left);
		fixSize(left);

		
		left.addBox(
			-1F, 	//X
			-2.5F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			5,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		left.setRotationPoint(
			0, 		//X	
			-0.5F,			//Y
			-7.5F			//Z
		);		
		left.rotateAngleY = (float)-Math.PI / 2;

		
		ModelRenderer right = new ModelRenderer(this, 0, 12);
		anchor.addChild(right);
		fixSize(right);

		
		right.addBox(
			-1F, 	//X
			-2.5F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			5,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		right.setRotationPoint(
			0, 		//X	
			-0.5F,			//Y
			7.5F			//Z
		);
		right.rotateAngleY = (float)-Math.PI / 2;

		
		ModelRenderer bottomleft = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottomleft);
		fixSize(bottomleft);

		
		bottomleft.addBox(
			-1F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		bottomleft.setRotationPoint(
			0, 		//X	
			3,			//Y
			-5.5F			//Z
		);
		bottomleft.rotateAngleY = (float)-Math.PI / 2;

		
		ModelRenderer bottomright = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottomright);
		fixSize(bottomright);

		
		bottomright.addBox(
			-1F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		bottomright.setRotationPoint(
			0, 		//X	
			3,			//Y
			5.5F			//Z
		);
		bottomright.rotateAngleY = (float)-Math.PI / 2;
		
		
		ModelRenderer bottominnerleft = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottominnerleft);
		fixSize(bottominnerleft);

		
		bottominnerleft.addBox(
			-1F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		bottominnerleft.setRotationPoint(
			0, 		//X	
			5,			//Y
			-3.5F			//Z
		);		
		bottominnerleft.rotateAngleY = (float)-Math.PI / 2;
		
		ModelRenderer bottominnerright = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottominnerright);
		fixSize(bottominnerright);

		
		bottominnerright.addBox(
			-1F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		bottominnerright.setRotationPoint(
			0, 		//X	
			5,			//Y
			3.5F			//Z
		);	
		bottominnerright.rotateAngleY = (float)-Math.PI / 2;
		
		ModelRenderer bottom = new ModelRenderer(this, 0, 8);
		anchor.addChild(bottom);
		fixSize(bottom);

		
		bottom.addBox(
			-2.5F, 	//X
			-1F, 	//Y
			-1F,	 	//Z
			5,					//Size X
			2,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		bottom.setRotationPoint(
			0, 		//X	
			7,			//Y
			0			//Z
		);	
		bottom.rotateAngleY = (float)-Math.PI / 2;
		


		ModelRenderer middlebottom = new ModelRenderer(this, 0, 19);
		anchor.addChild(middlebottom);
		fixSize(middlebottom);

		
		middlebottom.addBox(
			-0.5F, 	//X
			-2.5F, 	//Y
			-0.5F,	 	//Z
			1,					//Size X
			5,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		middlebottom.setRotationPoint(
			0.5F, 		//X	
			3.5F,			//Y
			0F			//Z
		);	
		middlebottom.rotateAngleY = (float)-Math.PI / 2;	

		ModelRenderer middle = new ModelRenderer(this, 0, 25);
		anchor.addChild(middle);
		fixSize(middle);

		
		middle.addBox(
			-1.5F, 	//X
			-1F, 	//Y
			-0.5F,	 	//Z
			3,					//Size X
			2,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		middle.setRotationPoint(
			0.5F, 		//X	
			0F,			//Y
			0F			//Z
		);	
		middle.rotateAngleY = (float)-Math.PI / 2;		
		
		ModelRenderer middleleft = new ModelRenderer(this, 0, 25);
		anchor.addChild(middleleft);
		fixSize(middleleft);

		
		middleleft.addBox(
			-1.5F, 	//X
			-1F, 	//Y
			-0.5F,	 	//Z
			3,					//Size X
			2,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		middleleft.setRotationPoint(
			0.5F, 		//X	
			-1F,			//Y
			-3F			//Z
		);	
		middleleft.rotateAngleY = (float)-Math.PI / 2;	
		
		ModelRenderer middleright = new ModelRenderer(this, 0, 25);
		anchor.addChild(middleright);
		fixSize(middleright);

		
		middleright.addBox(
			-1.5F, 	//X
			-1F, 	//Y
			-0.5F,	 	//Z
			3,					//Size X
			2,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		middleright.setRotationPoint(
			0.5F, 		//X	
			-1F,			//Y
			3F			//Z
		);	
		middleright.rotateAngleY = (float)-Math.PI / 2;		
		
		
		ModelRenderer innerleft = new ModelRenderer(this, 0, 28);
		anchor.addChild(innerleft);
		fixSize(innerleft);

		
		innerleft.addBox(
			-1.5F, 	//X
			-0.5F, 	//Y
			-0.5F,	 	//Z
			2,					//Size X
			1,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		innerleft.setRotationPoint(
			0.5F, 		//X	
			-1.5F,			//Y
			-5F			//Z
		);	
		innerleft.rotateAngleY = (float)-Math.PI / 2;	

		ModelRenderer innerright = new ModelRenderer(this, 0, 28);
		anchor.addChild(innerright);
		fixSize(innerright);

		
		innerright.addBox(
			-1.5F, 	//X
			-0.5F, 	//Y
			-0.5F,	 	//Z
			2,					//Size X
			1,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		innerright.setRotationPoint(
			0.5F, 		//X	
			-1.5F,			//Y
			6F			//Z
		);	
		innerright.rotateAngleY = (float)-Math.PI / 2;			
    }
	
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		anchor.rotateAngleX = module == null ? 0 : ((ModuleAdvControl)module).getWheelAngle();	
	}
	
	
}
				
