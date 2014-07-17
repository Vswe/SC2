package vswe.stevesvehicles.client.rendering.models.common;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.chest.ModuleEggBasket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelEggBasket extends ModelVehicle {
	
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/chestModelEaster.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}	
	
    @Override
	protected int getTextureHeight() {
		return 128;
	}
	
	private ModelRenderer chestTop;
    public ModelEggBasket()
    {

		for (int i = 0; i < 2; i++) {
			ModelRenderer chestSide = new ModelRenderer(this, 0, 13);
			addRenderer(chestSide);

			chestSide.addBox(
                -8,         //X
                -2.5F,      //Y
                -0.5F,      //Z
                16,         //Size X
                5,          //Size Y
                1,          //Size Z
                0.0F
            );
			chestSide.setRotationPoint(
                0F,                 //X
                -8.5F,              //Y
                -5.5F + i * 11      //Z
            );


		
			ModelRenderer chestFrontBack = new ModelRenderer(this, 0, 19);
			addRenderer(chestFrontBack);

			chestFrontBack.addBox(
                -5,         //X
                -2.5F,      //Y
                -0.5F,      //Z
                10,         //Size X
                5,          //Size Y
                1,          //Size Z
                0.0F
            );
			chestFrontBack.setRotationPoint(
                -7.5F + i * 15,         //X
                -8.5F,                  //Y
                0                       //Z
            );

			chestFrontBack.rotateAngleY = (float)Math.PI / 2;


			ModelRenderer chestHandle = new ModelRenderer(this, 0, 36);
			addRenderer(chestHandle);

			chestHandle.addBox(
                -1,         //X
                -1.5F,      //Y
                -0.5F,      //Z
                2,          //Size X
                3,          //Size Y
                1,          //Size Z
                0.0F
            );
			chestHandle.setRotationPoint(
                0F,                 //X
                -12.5F,             //Y
                -5.5F + i * 11      //Z
            );

			ModelRenderer chestHandleSmall = new ModelRenderer(this, 0, 40);
			addRenderer(chestHandleSmall);

			chestHandleSmall.addBox(
                -1,         //X
                -0.5F,      //Y
                -0.5F,      //Z
                2,          //Size X
                1,          //Size Y
                1,          //Size Z
                0.0F
            );
			chestHandleSmall.setRotationPoint(
                0F,                 //X
                -14.5F,             //Y
                -4.5F + i * 9       //Z
            );
		}

			
		chestTop = new ModelRenderer(this, 0, 0);
		addRenderer(chestTop);

		chestTop.addBox(
            -7,         //X
            -5,         //Y
            -0.5F,      //Z
            14,         //Size X
            10,         //Size Y
            1,          //Size Z
            0.0F
        );
		chestTop.setRotationPoint(
            0,          //X
            -11.5F,     //Y
            0           //Z
        );
		
		chestTop.rotateAngleX = (float)Math.PI / 2;
		chestTop.rotateAngleY = 0.1F;
		
		ModelRenderer chestBot = new ModelRenderer(this, 0, 25);
		addRenderer(chestBot);

		chestBot.addBox(
            -7,         //X
            -5,         //Y
            -0.5F,      //Z
            14,         //Size X
            10,         //Size Y
            1,          //Size Z
            0.0F
        );
		chestBot.setRotationPoint(
            0,              //X
            -5.5F,          //Y
            0               //Z
        );
		
		chestBot.rotateAngleX = (float)Math.PI / 2;
		
		
		ModelRenderer chestHandleTop = new ModelRenderer(this, 0, 42);
		addRenderer(chestHandleTop);

		chestHandleTop.addBox(
            -1,             //X
            -4F,            //Y
            -0.5F,          //Z
            2,              //Size X
            8,              //Size Y
            1,              //Size Z
            0.0F
        );
		chestHandleTop.setRotationPoint(
            0F,        //X
            -15.5F,    //Y
            0          //Z
        );
		
		chestHandleTop.rotateAngleX = (float)Math.PI / 2;

		for (int i = 0; i < 12; i++) {
			addEgg(i);
		}
    }

	
	private void addEgg(int id) {
		int x = id % 3; 
		int y = id / 3;
	
		float targetX = -3 + x * (10F / 3);
		float targetY = -5 + y * (14F / 4);
		
		int textureY = 19 + id * 5;
		
		ModelRenderer eggBot = new ModelRenderer(this, 30, textureY);
		addRenderer(eggBot);

		eggBot.addBox(
                -1F,        //X
                -0.5F,        //Y
                -1F,        //Z
                2,            //Size X
                1,            //Size Y
                2,            //Size Z
                0.0F
        );
		eggBot.setRotationPoint(
                targetY,        //X
                -6.5F,            //Y
                targetX            //Z
        );
	
		ModelRenderer eggBase = new ModelRenderer(this, 38, textureY);
		addRenderer(eggBase);

		eggBase.addBox(
            -1.5F,          //X
            -1F,            //Y
            -1.5F,          //Z
            3,              //Size X
            2,              //Size Y
            3,              //Size Z
            0.0F
        );
		eggBase.setRotationPoint(
            targetY,        //X
            -7.5F,          //Y
            targetX         //Z
        );

		ModelRenderer eggMiddle = new ModelRenderer(this, 50, textureY);
		addRenderer(eggMiddle);

		eggMiddle.addBox(
            -1F,        //X
            -0.5F,      //Y
            -1F,        //Z
            2,          //Size X
            1,          //Size Y
            2,          //Size Z
            0.0F
        );
		eggMiddle.setRotationPoint(
            targetY,            //X
            -8.75F,             //Y
            targetX             //Z
        );
		
		ModelRenderer eggTip = new ModelRenderer(this, 58, textureY);
		addRenderer(eggTip);

		eggTip.addBox(
            -0.5F,      //X
            -0.5F,      //Y
            -0.5F,      //Z
            1,          //Size X
            1,          //Size Y
            1,           //Size Z
            0.0F
        );
		eggTip.setRotationPoint(
                targetY,            //X
                -9.25F,             //Y
                targetX             //Z
        );
	}

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		if (module != null) {
			chestTop.rotateAngleY = 0.1F + ((ModuleEggBasket)module).getChestAngle();
		}else{
            chestTop.rotateAngleY = 0.1F;
        }
	}	

}
