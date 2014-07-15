package vswe.stevesvehicles.client.rendering.models;
import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleFlowerRemover;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelLawnMower extends ModelVehicle {
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/lawnmowerModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		

    @Override
	protected int getTextureWidth() {
		return 64;
	}
    @Override
	protected int getTextureHeight() {
		return 32;
	}

	private final ArrayList<ModelRenderer> bladePins;
    public ModelLawnMower() {
    	bladePins = new ArrayList<ModelRenderer>();
    	createSide(false);
    	createSide(true);
    }

	private void createSide(boolean opposite) {
		ModelRenderer anchor = new ModelRenderer(this, 0, 0);
		addRenderer(anchor);

		if (opposite) {
			anchor.rotateAngleY = (float)Math.PI;
		}

		ModelRenderer base = new ModelRenderer(this, 0, 0);
		anchor.addChild(base);
		fixSize(base);

		base.addBox(
			-11.5F, 	    //X
			-3F, 	        //Y
			-1,	 	        //Z
			23,				//Size X
			6,				//Size Y
			2,			    //Size Z
			0.0F
		);
		base.setRotationPoint(
			0, 		    //X
			-1.5F,		//Y
			-9F			//Z
		);
		
		
		for (int i = 0; i < 2; i++) {
			ModelRenderer arm = new ModelRenderer(this, 0, 8);
			base.addChild(arm);
			fixSize(arm);
			
			arm.addBox(
                -8F, 	    //X
                -1.5F, 	    //Y
                -1.5F,	 	//Z
                16,			//Size X
                3,			//Size Y
                3,			//Size Z
                0.0F
			);
			arm.setRotationPoint(
				-8.25F + i * 16.5F, 	//X
				0F,			            //Y
				-8			            //Z
			);	
			
			arm.rotateAngleY = (float)Math.PI / 2;
			
			ModelRenderer arm2 = new ModelRenderer(this, 0, 14);
			arm.addChild(arm2);
			fixSize(arm2);
			
			arm2.addBox(
					-1.5F, 	        //X
					-1.5F, 	        //Y
					-1.5F,	 	    //Z
					3,				//Size X
					3,				//Size Y
					3,			    //Size Z
					0.0F
				);
			arm2.setRotationPoint(
				6.5F, 		//X
				3F,			//Y
				0			//Z
			);	
			
			arm2.rotateAngleZ = (float)Math.PI / 2;		
			
			ModelRenderer bladePin = new ModelRenderer(this, 0, 20);
			arm2.addChild(bladePin);
			fixSize(bladePin);
			
			bladePin.addBox(
                -1F,        //X
                -0.5F,      //Y
                -0.5F,      //Z
                2,          //Size X
                1,          //Size Y
                1,          //Size Z
                0.0F
            );
			bladePin.setRotationPoint(
                2.5F,           //X
                0,              //Y
                0               //Z
            );
			
			ModelRenderer bladeAnchor = new ModelRenderer(this, 0, 0);
			bladePin.addChild(bladeAnchor);
			bladeAnchor.rotateAngleY = (float)Math.PI / 2;
			
			for (int j = 0; j < 4; j++) {
				ModelRenderer blade = new ModelRenderer(this, 0, 22);
				bladeAnchor.addChild(blade);
				fixSize(blade);
				
				blade.addBox(
                    -1.5F, 	        //X
                    -1.5F, 	        //Y
                    -0.5F,	 	    //Z
                    8,				//Size X
                    3,				//Size Y
                    1,			    //Size Z
                    0.0F
                );
				blade.setRotationPoint(
					0, 		    //X
					0,			//Y
					j * 0.01F	//Z
				);				
					
				blade.rotateAngleZ = ((float)Math.PI / 2) * (j + i * 0.5F);	
				
				
				ModelRenderer bladeTip = new ModelRenderer(this, 0, 26);
				blade.addChild(bladeTip);
				fixSize(bladeTip);
				
				bladeTip.addBox(
                    6.5F,           //X
                    -1.0F,          //Y
                    -0.5F,          //Z
                    6,              //Size X
                    2,              //Size Y
                    1,              //Size Z
                    0.0F
                );
				bladeTip.setRotationPoint(
                    0,          //X
                    0,          //Y
                    0.005F      //Z
                );
			}
			bladePins.add(bladePin);
		}
	
		
	}

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll, float partialTime) {
		float angle = module == null ? 0F : (((ModuleFlowerRemover)module).getBladeAngle() + partialTime * ((ModuleFlowerRemover)module).getBladeSpindSpeed());

		for (int i = 0; i < bladePins.size(); i++) {
			ModelRenderer bladePin = bladePins.get(i);
			if (i % 2 == 0) {
				bladePin.rotateAngleX = angle;
			}else{
				bladePin.rotateAngleX = -angle;
			}
		}

	}
}
