package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.chest.ModuleEggBasket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelEggBasket extends ModelVehicle
{
	
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/chestModelEaster.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}	
	

	protected int getTextureHeight() {
		return 128;
	}
	
	ModelRenderer chesttop;
    public ModelEggBasket()
    {

		for (int i = 0; i < 2; i++) {
			ModelRenderer chestside = new ModelRenderer(this, 0, 13);
			AddRenderer(chestside);

			chestside.addBox(
				-8, 	//X
				-2.5F, 	//Y
				-0.5F,	 	//Z
				16,					//Size X
				5,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			chestside.setRotationPoint(
				0F, 		//X
				-8.5F,			//Y
				-5.5F + i*11			//Z
			);


		
			ModelRenderer chestfrontback = new ModelRenderer(this, 0, 19);
			AddRenderer(chestfrontback);

			chestfrontback.addBox(
				-5, 	//X
				-2.5F, 	//Y
				-0.5F,	 	//Z
				10,					//Size X
				5,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			chestfrontback.setRotationPoint(
				-7.5F + i*15, 		//X
				-8.5F,		//Y
				0 			//Z
			);

			chestfrontback.rotateAngleY = (float)Math.PI / 2;


			ModelRenderer chesthandle = new ModelRenderer(this, 0, 36);
			AddRenderer(chesthandle);

			chesthandle.addBox(
				-1, 	//X
				-1.5F, 	//Y
				-0.5F,	 	//Z
				2,					//Size X
				3,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			chesthandle.setRotationPoint(
				0F, 		//X
				-12.5F,			//Y
				-5.5F + i*11			//Z
			);		

			ModelRenderer chesthandlesmall = new ModelRenderer(this, 0, 40);
			AddRenderer(chesthandlesmall);

			chesthandlesmall.addBox(
				-1, 	//X
				-0.5F, 	//Y
				-0.5F,	 	//Z
				2,					//Size X
				1,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			chesthandlesmall.setRotationPoint(
				0F, 		//X
				-14.5F,			//Y
				-4.5F + i*9			//Z
			);				
		}

			
		chesttop = new ModelRenderer(this, 0, 0);
		AddRenderer(chesttop);

		chesttop.addBox(
			-7, 	//X
			-5, 	//Y
			-0.5F,	 	//Z
			14,					//Size X
			10,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		chesttop.setRotationPoint(
			0, 		//X
			- 11.5F,		//Y
			0			//Z
		);
		
		chesttop.rotateAngleX = (float)Math.PI / 2;
		chesttop.rotateAngleY = 0.1F;
		
		ModelRenderer chestbot = new ModelRenderer(this, 0, 25);
		AddRenderer(chestbot);

		chestbot.addBox(
			-7, 	//X
			-5, 	//Y
			-0.5F,	 	//Z
			14,					//Size X
			10,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		chestbot.setRotationPoint(
			0, 		//X
			- 5.5F,		//Y
			0			//Z
		);
		
		chestbot.rotateAngleX = (float)Math.PI / 2;		
		
		
		ModelRenderer chesthandletop = new ModelRenderer(this, 0, 42);
		AddRenderer(chesthandletop);

		chesthandletop.addBox(
			-1, 	//X
			-4F, 	//Y
			-0.5F,	 	//Z
			2,					//Size X
			8,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		chesthandletop.setRotationPoint(
			0F, 		//X
			-15.5F,			//Y
			0			//Z
		);		
		
		chesthandletop.rotateAngleX = (float)Math.PI / 2;	

		for (int i = 0; i < 12; i++) {
			addEgg(i);
		}
    }

	
	private void addEgg(int id) {
		int x = id % 3; 
		int y = id / 3;
	
		float xCoord = -3 + x * (10F / 3);
		float yCoord = -5 + y * (14F / 4);
		
		int textureY = 19 + id * 5;
		
		ModelRenderer eggbot = new ModelRenderer(this, 30, textureY);
		AddRenderer(eggbot);

		eggbot.addBox(
			-1F, 	//X
			-0.5F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			1,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		eggbot.setRotationPoint(
			yCoord, 		//X
			-6.5F,			//Y
			xCoord			//Z
		);		
	
		ModelRenderer eggbase = new ModelRenderer(this, 38, textureY);
		AddRenderer(eggbase);

		eggbase.addBox(
			-1.5F, 	//X
			-1F, 	//Y
			-1.5F,	 	//Z
			3,					//Size X
			2,					//Size Y
			3,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		eggbase.setRotationPoint(
			yCoord, 		//X
			-7.5F,			//Y
			xCoord			//Z
		);			

		ModelRenderer eggmiddle = new ModelRenderer(this, 50, textureY);
		AddRenderer(eggmiddle);

		eggmiddle.addBox(
			-1F, 	//X
			-0.5F, 	//Y
			-1F,	 	//Z
			2,					//Size X
			1,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		eggmiddle.setRotationPoint(
			yCoord, 		//X
			-8.75F,			//Y
			xCoord			//Z
		);		
		
		ModelRenderer eggtip = new ModelRenderer(this, 58, textureY);
		AddRenderer(eggtip);

		eggtip.addBox(
			-0.5F, 	//X
			-0.5F, 	//Y
			-0.5F,	 	//Z
			1,					//Size X
			1,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		eggtip.setRotationPoint(
			yCoord, 		//X
			-9.25F,			//Y
			xCoord			//Z
		);			
	}

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		if (module != null) {
			chesttop.rotateAngleY =  0.1F + ((ModuleEggBasket)module).getChestAngle();
		}
	}	

}
