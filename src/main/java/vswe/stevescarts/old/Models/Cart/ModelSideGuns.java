package vswe.stevescarts.old.Models.Cart;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Realtimers.ModuleShooterAdvSide;

public class ModelSideGuns extends ModelCartbase {

	private static ResourceLocation texture = ResourceHelper.getResource("/models/sidegunsModel.png");
	
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
	
	ModelRenderer[][] models;
	public ModelSideGuns(){	
		models = new ModelRenderer[2][];
		models[0] = createSide(false);
		models[1] = createSide(true);	
	}	

	private ModelRenderer[] createSide(boolean opposite) {
		ModelRenderer anchor = new ModelRenderer(this, 0, 0);
		AddRenderer(anchor);

		int mult = opposite ? 1 : -1;		



	
		ModelRenderer handle = new ModelRenderer(this, 0, 8);
		anchor.addChild(handle);
		fixSize(handle);		

		handle.addBox(
				-2F, 	//X
				-2F, 	//Y
				-2.5F,	 	//Z
				4,					//Size X
				4,					//Size Y
				5,			     	//Size Z
				0.0F			 	//Size Increasement
			);
		handle.setRotationPoint(
				6, 		//X
				0,			//Y
				0			//Z
		);		
		
		ModelRenderer base = new ModelRenderer(this, 0, 0);
		handle.addChild(base);
		fixSize(base);

		base.addBox(
			-2.5F, 	//X
			-2.5F, 	//Y
			-1.5F,	 	//Z
			12,					//Size X
			5,					//Size Y
			3,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		base.setRotationPoint(
			0, 		//X
			0,			//Y
			0			//Z
		);

		ModelRenderer gun = new ModelRenderer(this, 0, 0);
		base.addChild(gun);
		fixSize(gun);

		gun.addBox(
			-2.5F, 	//X
			-2.5F, 	//Y
			-1.5F,	 	//Z
			12,					//Size X
			5,					//Size Y
			3,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		gun.setRotationPoint(
			7.005F, 		//X
			0.005F,			//Y
			0.005F			//Z
		);
		
		ModelRenderer back = new ModelRenderer(this, 18, 8);
		gun.addChild(back);
		fixSize(back);

		back.addBox(
			-6.5F, 	//X
			-2F, 	//Y
			-0.5F,	 	//Z
			7,					//Size X
			4,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		back.setRotationPoint(
			0, 		//X
			0F,			//Y
			-0.5F * mult			//Z
		);	
		
		ModelRenderer backAttacher = new ModelRenderer(this, 18, 8);
		back.addChild(backAttacher);
		fixSize(backAttacher);

		backAttacher.addBox(
			0F, 	//X
			-2F, 	//Y
			-0.5F + 0.5F * mult,	 	//Z
			7,					//Size X
			4,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		backAttacher.setRotationPoint(
			-6.5F, 		//X
			0F,			//Y
			0.5F * mult + 0.005F			//Z
		);		
	
		ModelRenderer stabalizer = new ModelRenderer(this, 0, 8);
		back.addChild(stabalizer);
		fixSize(stabalizer);

		stabalizer.addBox(
			-0.5F, 	//X
			-1.5F, 	//Y
			-0.5F,	 	//Z
			1,					//Size X
			3,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		stabalizer.setRotationPoint(
			-5.75F, 		//X
			0F,			//Y
			0			//Z
		);	
		
		ModelRenderer[] missileStands = new ModelRenderer[6];
		for (int i = 0; i < missileStands.length; i++) {
			missileStands[i] = new ModelRenderer(this, 0, 17);
			float posX;
			if (i < 3) {
				back.addChild(missileStands[i]);
				posX = -5;
			}else{
				backAttacher.addChild(missileStands[i]);
				posX = 0;
			}
			fixSize(missileStands[i]);
	
			missileStands[i].addBox(
				1F, 	//X
				-1.5F, 	//Y
				-0.5F,	 	//Z
				2,					//Size X
				3,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			missileStands[i].setRotationPoint(
				posX, 		//X
				0F,			//Y
				0			//Z
			);	
			
			
		}
		
		ModelRenderer missleArmBase = new ModelRenderer(this, 7, 17);
		base.addChild(missleArmBase);
		fixSize(missleArmBase);

		missleArmBase.addBox(
			-2, 	//X
			-2F, 	//Y
			-0.5F,	 	//Z
			4,					//Size X
			4,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		missleArmBase.setRotationPoint(
			0, 		//X
			0,			//Y
			0.75F * mult			//Z
		);
		
		ModelRenderer missleArm = new ModelRenderer(this, 17, 17);
		missleArmBase.addChild(missleArm);
		fixSize(missleArm);

		missleArm.addBox(
			-0.5F, 	//X
			-2F, 	//Y
			-0.5F,	 	//Z
			11,					//Size X
			4,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		missleArm.setRotationPoint(
			0, 		//X
			0,			//Y
			0			//Z
		);	
		
		ModelRenderer missleArmBaseFake = new ModelRenderer(this);
		base.addChild(missleArmBaseFake);
		fixSize(missleArmBaseFake);
		missleArmBaseFake.setRotationPoint(
			0, 		//X
			0,			//Y
			0.75F * mult			//Z
		);
		
		ModelRenderer missleArmFake = new ModelRenderer(this);
		missleArmBaseFake.addChild(missleArmFake);
		fixSize(missleArmFake);
		missleArmFake.setRotationPoint(
			0, 		//X
			0,			//Y
			0			//Z
		);			

		ModelRenderer[] missiles = new ModelRenderer[2];
		for (int i = 0; i < 2; i++) {
			ModelRenderer missile = new ModelRenderer(this, 0, 22);
			missiles[i] = missile;
			missleArmFake.addChild(missile);
			fixSize(missile);
	
			missile.addBox(
				-2F, 	//X
				-1F, 	//Y
				-1F,	 	//Z
				4,					//Size X
				2,					//Size Y
				2,			     	//Size Z
				-0.2F			 	//Size Increasement
			);
			missile.setRotationPoint(
				i == 0 ? 9.5F : 4F, 		//X
				0,			//Y
				mult * -1F		//Z
			);
			missile.rotateAngleZ = (float)Math.PI / 2;
			
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (j != 0 && k != 0) {
						continue;
					}
					
					ModelRenderer missilePart = new ModelRenderer(this, 12, 22);
					missile.addChild(missilePart);
					fixSize(missilePart);				
					missilePart.addBox(
							-0.5F, 	//X
							-0.5F, 	//Y
							-0.5F,	 	//Z
							1,					//Size X
							1,					//Size Y
							1,			     	//Size Z
							0			 	//Size Increasement
						);
					missilePart.setRotationPoint(
							-1F, 		//X
							j * 0.6F,			//Y
							k * 0.6F		//Z
						);				
				}			
			}
			
			ModelRenderer missilePart = new ModelRenderer(this, 12, 24);
			missile.addChild(missilePart);
			fixSize(missilePart);				
			missilePart.addBox(
					-0.5F, 	//X
					-0.5F, 	//Y
					-0.5F,	 	//Z
					1,					//Size X
					1,					//Size Y
					1,			     	//Size Z
					0			 	//Size Increasement
				);
			missilePart.setRotationPoint(
					1.75F, 		//X
					0,			//Y
					0		//Z
				);
		}
		

		return new ModelRenderer[] {handle, base, gun, back, backAttacher, stabalizer, missileStands[0], missileStands[1], missileStands[2], missileStands[3], missileStands[4], missileStands[5], missleArmBase, missleArm, missleArmBaseFake, missleArmFake, missiles[0], missiles[1]};
	}
	

	@Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		if (module == null) {
			for (int i = 0; i < 2; i++) {
				ModelRenderer[] models = this.models[i];

				//TODO Add default values
			}
		}else{
	
			ModuleShooterAdvSide shooter = (ModuleShooterAdvSide)module;
		
			for (int i = 0; i < 2; i++) {
				ModelRenderer[] models = this.models[i];
				int mult = i != 0 ? 1 : -1;

				models[0].rotationPointZ = shooter.getHandlePos(mult);
				models[1].rotationPointZ = shooter.getBasePos(mult);
				models[0].rotateAngleZ = shooter.getHandleRot(mult);
				models[2].rotateAngleZ = shooter.getGunRot(mult);
				models[3].rotationPointX = shooter.getBackPos(mult);
				models[3].rotateAngleY = shooter.getBackRot(mult);
				models[4].rotateAngleY = shooter.getAttacherRot(mult);	
				models[5].rotationPointZ = shooter.getStabalizerOut(mult);
				models[5].rotationPointY = shooter.getStabalizerDown(mult);
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 3; k++) {					
						models[6 + j * 3 + k].rotationPointZ = shooter.getStandOut(mult, j, k - 1);
						models[6 + j * 3 + k].rotationPointY = shooter.getStandUp(mult, j, k - 1);
					}
				}
				
				models[12].rotationPointX = shooter.getArmBasePos(mult, false);
				models[13].rotateAngleY = shooter.getArmRot(mult, false);
				models[13].rotationPointX = shooter.getArmPos(mult, false);
				
				models[14].rotationPointX = shooter.getArmBasePos(mult, true);
				models[15].rotateAngleY = shooter.getArmRot(mult, true);
				models[15].rotationPointX = shooter.getArmPos(mult, true);
				
				for (int j = 0; j < 2; j++) {
					models[16 + j].rotationPointY = shooter.getMissilePos(mult);
					models[16 + j].rotateAngleY = shooter.getMissileRot(mult);
				}
			}
		}
	}	
	
	

	
}
