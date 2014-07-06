package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleAdvancedControl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWheel extends ModelVehicle {
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/wheelModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		
	@Override
	protected int getTextureWidth() {
		return 32;
	}
    @Override
	protected int getTextureHeight() {
		return 32;
	}

    @Override
	public float extraMultiplier() {
		return 0.65F;
	}
	
	private final ModelRenderer anchor;
    public ModelWheel() {
		anchor = new ModelRenderer(this);
		addRenderer(anchor);
		
		anchor.setRotationPoint(
			-10, 		//X	
			-5,			//Y
			0			//Z
		);	


		ModelRenderer top = new ModelRenderer(this, 0, 0);
		anchor.addChild(top);
		fixSize(top);

		
		top.addBox(
			-4.5F, 	    //X
			-1F, 	    //Y
			-1F,	 	//Z
			9,			//Size X
			2,			//Size Y
			2,			//Size Z
			0.0F
		);
		top.setRotationPoint(
			0, 		    //X
			-6,			//Y
			0			//Z
		);	
		top.rotateAngleY = (float)-Math.PI / 2;
		
		
		ModelRenderer topLeft = new ModelRenderer(this, 0, 4);
		anchor.addChild(topLeft);
		fixSize(topLeft);

		
		topLeft.addBox(
            -1F,        //X
            -1F,        //Y
            -1F,        //Z
            2,          //Size X
            2,          //Size Y
            2,          //Size Z
            0.0F
        );
		topLeft.setRotationPoint(
            0,          //X
            -4F,        //Y
            -5.5F       //Z
        );
		topLeft.rotateAngleY = (float)-Math.PI / 2;


		
		ModelRenderer topRight = new ModelRenderer(this, 0, 4);
		anchor.addChild(topRight);
		fixSize(topRight);

		
		topRight.addBox(
            -1F,        //X
            -1F,        //Y
            -1F,        //Z
            2,          //Size X
            2,          //Size Y
            2,          //Size Z
            0.0F
        );
		topRight.setRotationPoint(
            0,              //X
            -4F,            //Y
            5.5F            //Z
        );
		topRight.rotateAngleY = (float)-Math.PI / 2;
		

		ModelRenderer left = new ModelRenderer(this, 0, 12);
		anchor.addChild(left);
		fixSize(left);

		
		left.addBox(
			-1F, 	    //X
			-2.5F, 	    //Y
			-1F,	 	//Z
			2,			//Size X
			5,			//Size Y
			2,			//Size Z
			0.0F
		);
		left.setRotationPoint(
			0, 		    //X
			-0.5F,		//Y
			-7.5F		//Z
		);		
		left.rotateAngleY = (float)-Math.PI / 2;

		
		ModelRenderer right = new ModelRenderer(this, 0, 12);
		anchor.addChild(right);
		fixSize(right);

		
		right.addBox(
			-1F, 	    //X
			-2.5F, 	    //Y
			-1F,	 	//Z
			2,			//Size X
			5,			//Size Y
			2,			//Size Z
			0.0F
		);
		right.setRotationPoint(
			0, 		    //X
			-0.5F,		//Y
			7.5F		//Z
		);
		right.rotateAngleY = (float)-Math.PI / 2;

		
		ModelRenderer bottomLeft = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottomLeft);
		fixSize(bottomLeft);

		
		bottomLeft.addBox(
                -1F,        //X
                -1F,        //Y
                -1F,        //Z
                2,          //Size X
                2,          //Size Y
                2,          //Size Z
                0.0F
        );
		bottomLeft.setRotationPoint(
                0,          //X
                3,          //Y
                -5.5F       //Z
        );
		bottomLeft.rotateAngleY = (float)-Math.PI / 2;

		
		ModelRenderer bottomRight = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottomRight);
		fixSize(bottomRight);

		
		bottomRight.addBox(
            -1F,        //X
            -1F,        //Y
            -1F,        //Z
            2,          //Size X
            2,          //Size Y
            2,          //Size Z
            0.0F
        );
		bottomRight.setRotationPoint(
            0,      //X
            3,      //Y
            5.5F    //Z
        );
		bottomRight.rotateAngleY = (float)-Math.PI / 2;
		
		
		ModelRenderer bottomInnerLeft = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottomInnerLeft);
		fixSize(bottomInnerLeft);

		
		bottomInnerLeft.addBox(
            -1F,        //X
            -1F,        //Y
            -1F,        //Z
            2,          //Size X
            2,          //Size Y
            2,          //Size Z
            0.0F
        );
		bottomInnerLeft.setRotationPoint(
            0,              //X
            5,              //Y
            -3.5F           //Z
        );
		bottomInnerLeft.rotateAngleY = (float)-Math.PI / 2;
		
		ModelRenderer bottomInnerRight = new ModelRenderer(this, 0, 4);
		anchor.addChild(bottomInnerRight);
		fixSize(bottomInnerRight);

		
		bottomInnerRight.addBox(
            -1F,        //X
            -1F,        //Y
            -1F,        //Z
            2,          //Size X
            2,          //Size Y
            2,          //Size Z
            0.0F
        );
		bottomInnerRight.setRotationPoint(
            0,        //X
            5,        //Y
            3.5F      //Z
        );
		bottomInnerRight.rotateAngleY = (float)-Math.PI / 2;
		
		ModelRenderer bottom = new ModelRenderer(this, 0, 8);
		anchor.addChild(bottom);
		fixSize(bottom);

		
		bottom.addBox(
			-2.5F, 	        //X
			-1F, 	        //Y
			-1F,	 	    //Z
			5,				//Size X
			2,				//Size Y
			2,			    //Size Z
			0.0F
		);
		bottom.setRotationPoint(
			0, 		    //X
			7,			//Y
			0			//Z
		);	
		bottom.rotateAngleY = (float)-Math.PI / 2;
		


		ModelRenderer middleBottom = new ModelRenderer(this, 0, 19);
		anchor.addChild(middleBottom);
		fixSize(middleBottom);

		
		middleBottom.addBox(
            -0.5F,      //X
            -2.5F,      //Y
            -0.5F,      //Z
            1,          //Size X
            5,          //Size Y
            1,          //Size Z
            0.0F
        );
		middleBottom.setRotationPoint(
            0.5F,        //X
            3.5F,        //Y
            0F           //Z
        );
		middleBottom.rotateAngleY = (float)-Math.PI / 2;

		ModelRenderer middle = new ModelRenderer(this, 0, 25);
		anchor.addChild(middle);
		fixSize(middle);

		
		middle.addBox(
			-1.5F, 	    //X
			-1F, 	    //Y
			-0.5F,	 	//Z
			3,			//Size X
			2,			//Size Y
			1,			//Size Z
			0.0F
		);
		middle.setRotationPoint(
			0.5F, 		//X	
			0F,			//Y
			0F			//Z
		);	
		middle.rotateAngleY = (float)-Math.PI / 2;		
		
		ModelRenderer middleLeft = new ModelRenderer(this, 0, 25);
		anchor.addChild(middleLeft);
		fixSize(middleLeft);

		
		middleLeft.addBox(
            -1.5F,          //X
            -1F,            //Y
            -0.5F,          //Z
            3,              //Size X
            2,              //Size Y
            1,              //Size Z
            0.0F
        );
		middleLeft.setRotationPoint(
            0.5F,        //X
            -1F,         //Y
            -3F          //Z
        );
		middleLeft.rotateAngleY = (float)-Math.PI / 2;
		
		ModelRenderer middleRight = new ModelRenderer(this, 0, 25);
		anchor.addChild(middleRight);
		fixSize(middleRight);

		
		middleRight.addBox(
            -1.5F,      //X
            -1F,        //Y
            -0.5F,      //Z
            3,          //Size X
            2,          //Size Y
            1,          //Size Z
            0.0F
        );
		middleRight.setRotationPoint(
            0.5F,           //X
            -1F,            //Y
            3F              //Z
        );
		middleRight.rotateAngleY = (float)-Math.PI / 2;
		
		
		ModelRenderer innerLeft = new ModelRenderer(this, 0, 28);
		anchor.addChild(innerLeft);
		fixSize(innerLeft);

		
		innerLeft.addBox(
            -1.5F,          //X
            -0.5F,          //Y
            -0.5F,          //Z
            2,              //Size X
            1,              //Size Y
            1,              //Size Z
            0.0F
        );
		innerLeft.setRotationPoint(
            0.5F,           //X
            -1.5F,          //Y
            -5F            //Z
        );
		innerLeft.rotateAngleY = (float)-Math.PI / 2;

		ModelRenderer innerRight = new ModelRenderer(this, 0, 28);
		anchor.addChild(innerRight);
		fixSize(innerRight);

		
		innerRight.addBox(
            -1.5F,      //X
            -0.5F,      //Y
            -0.5F,      //Z
            2,          //Size X
            1,          //Size Y
            1,          //Size Z
            0.0F
        );
		innerRight.setRotationPoint(
            0.5F,        //X
            -1.5F,       //Y
            6F           //Z
        );
		innerRight.rotateAngleY = (float)-Math.PI / 2;
    }

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		anchor.rotateAngleX = module == null ? 0 : ((ModuleAdvancedControl)module).getWheelAngle();
	}

	
}
				
