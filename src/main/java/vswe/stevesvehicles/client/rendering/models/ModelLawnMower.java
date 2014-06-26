package vswe.stevesvehicles.client.rendering.models;
import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleFlowerRemover;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelLawnMower extends ModelVehicle
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/lawnmowerModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		

	protected int getTextureWidth() {
		return 64;
	}
	protected int getTextureHeight() {
		return 32;
	}

	private ArrayList<ModelRenderer> bladepins;
    public ModelLawnMower()
    {
    	bladepins = new ArrayList<ModelRenderer>();
    	createSide(false);
    	createSide(true);
    }

	private void createSide(boolean opposite) {
		ModelRenderer anchor = new ModelRenderer(this, 0, 0);
		AddRenderer(anchor);

		if (opposite) {
			anchor.rotateAngleY = (float)Math.PI;
		}

		ModelRenderer base = new ModelRenderer(this, 0, 0);
		anchor.addChild(base);
		fixSize(base);

		base.addBox(
			-11.5F, 	//X
			-3F, 	//Y
			-1,	 	//Z
			23,					//Size X
			6,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		base.setRotationPoint(
			0, 		//X
			-1.5F,			//Y
			-9F			//Z
		);
		
		
		for (int i = 0; i < 2; i++) {
			ModelRenderer arm = new ModelRenderer(this, 0, 8);
			base.addChild(arm);
			fixSize(arm);
			
			arm.addBox(
					-8F, 	//X
					-1.5F, 	//Y
					-1.5F,	 	//Z
					16,					//Size X
					3,					//Size Y
					3,			     	//Size Z
					0.0F			 	//Size Increasement
				);
			arm.setRotationPoint(
				-8.25F + i * 16.5F, 		//X
				0F,			//Y
				-8			//Z
			);	
			
			arm.rotateAngleY = (float)Math.PI / 2;
			
			ModelRenderer arm2 = new ModelRenderer(this, 0, 14);
			arm.addChild(arm2);
			fixSize(arm2);
			
			arm2.addBox(
					-1.5F, 	//X
					-1.5F, 	//Y
					-1.5F,	 	//Z
					3,					//Size X
					3,					//Size Y
					3,			     	//Size Z
					0.0F			 	//Size Increasement
				);
			arm2.setRotationPoint(
				6.5F, 		//X
				3F,			//Y
				0			//Z
			);	
			
			arm2.rotateAngleZ = (float)Math.PI / 2;		
			
			ModelRenderer bladepin = new ModelRenderer(this, 0, 20);
			arm2.addChild(bladepin);
			fixSize(bladepin);
			
			bladepin.addBox(
					-1F, 	//X
					-0.5F, 	//Y
					-0.5F,	 	//Z
					2,					//Size X
					1,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
			bladepin.setRotationPoint(
				2.5F, 		//X
				0,			//Y
				0			//Z
			);
			
			ModelRenderer bladeanchor = new ModelRenderer(this, 0, 0);
			bladepin.addChild(bladeanchor);		
			bladeanchor.rotateAngleY = (float)Math.PI / 2;
			
			for (int j = 0; j < 4; j++) {
				ModelRenderer blade = new ModelRenderer(this, 0, 22);
				bladeanchor.addChild(blade);
				fixSize(blade);
				
				blade.addBox(
						-1.5F, 	//X
						-1.5F, 	//Y
						-0.5F,	 	//Z
						8,					//Size X
						3,					//Size Y
						1,			     	//Size Z
						0.0F			 	//Size Increasement
					);
				blade.setRotationPoint(
					0, 		//X
					0,			//Y
					j * 0.01F			//Z
				);				
					
				blade.rotateAngleZ = ((float)Math.PI / 2) * (j + i * 0.5F);	
				
				
				ModelRenderer bladetip = new ModelRenderer(this, 0, 26);
				blade.addChild(bladetip);
				fixSize(bladetip);
				
				bladetip.addBox(
						6.5F, 	//X
						-1.0F, 	//Y
						-0.5F,	 	//Z
						6,					//Size X
						2,					//Size Y
						1,			     	//Size Z
						0.0F			 	//Size Increasement
					);
				bladetip.setRotationPoint(
					0, 		//X
					0,			//Y
					0.005F			//Z
				);								
			}
			bladepins.add(bladepin);
		}
	
		
	}

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll, float partialtime) {

		

		float angle = module == null ? 0F : (((ModuleFlowerRemover)module).getBladeAngle() + partialtime * ((ModuleFlowerRemover)module).getBladeSpindSpeed());
		
		
		for (int i = 0; i < bladepins.size(); i++) {
			ModelRenderer bladepin = bladepins.get(i);
			if (i % 2 == 0) {
				bladepin.rotateAngleX = angle;	
			}else{
				bladepin.rotateAngleX = -angle;	
			}
		}
		
		
	}
}
