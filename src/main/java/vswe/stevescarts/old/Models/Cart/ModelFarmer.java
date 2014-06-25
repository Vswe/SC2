package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Workers.Tools.ModuleFarmer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelFarmer extends ModelCartbase
{
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}

	protected int getTextureWidth() {
		return 128;
	}

	public float extraMult() {
		return 0.5F;
	}

	private ModelRenderer mainAnchor;
	private ModelRenderer anchor;
	private ModelRenderer[] outers;
	private ResourceLocation resource;
    public ModelFarmer(ResourceLocation resource)
    {
    	this.resource = resource;
    	
		mainAnchor = new ModelRenderer(this);
		AddRenderer(mainAnchor);

		mainAnchor.setRotationPoint(
			-18, 		//X
			4,			//Y
			0			//Z
		);

		for (int i=-1; i<=1; i+=2){
			ModelRenderer smallarm = new ModelRenderer(this, 26, 23);
			mainAnchor.addChild(smallarm);
			fixSize(smallarm);

			smallarm.addBox(
				-1F, 	//X
				-1F, 	//Y
				-1F,	 	//Z
				8,					//Size X
				2,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			smallarm.setRotationPoint(
				0, 		//X
				0,			//Y
				i*17			//Z
			);
		}

		ModelRenderer mainarm = new ModelRenderer(this, 0, 37);
		mainAnchor.addChild(mainarm);
		fixSize(mainarm);

		mainarm.addBox(
			-30F, 	//X
			-2F, 	//Y
			-2F,	 	//Z
			60,					//Size X
			4,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		mainarm.setRotationPoint(
			8, 		//X
			0,			//Y
			0			//Z
		);
		mainarm.rotateAngleY = (float)Math.PI / 2;

		for (int i=-1; i<=1; i+=2){
			ModelRenderer extra = new ModelRenderer(this, 26, 27);
			mainAnchor.addChild(extra);
			fixSize(extra);

			extra.addBox(
				-2.5F, 	//X
				-2.5F, 	//Y
				-1F,	 	//Z
				5,					//Size X
				5,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			extra.setRotationPoint(
				8, 		//X
				0,			//Y
				i*30			//Z
			);

			ModelRenderer bigarm = new ModelRenderer(this, 26, 17);
			mainAnchor.addChild(bigarm);
			fixSize(bigarm);

			bigarm.addBox(
				-1F, 	//X
				-2F, 	//Y
				-1F,	 	//Z
				16,					//Size X
				4,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			bigarm.setRotationPoint(
				8, 		//X
				0,			//Y
				i*32			//Z
			);
		}

		anchor = new ModelRenderer(this);
		mainAnchor.addChild(anchor);

		anchor.setRotationPoint(
			22, 		//X
			0,			//Y
			0			//Z
		);

		float start = -1.5F;
		float end = 1.5F;
		for (float i = -1.5F; i <= 1.5F; i++) {
			for (int j = 0; j < 6; j++) {
				ModelRenderer side = new ModelRenderer(this, 0, 0);
				anchor.addChild(side);
				fixSize(side);

				side.addBox(
					-5F, 	//X
					-2F - 6.8F, 	//Y
					-1F,	 	//Z
					10,					//Size X
					4,					//Size Y
					2,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				side.setRotationPoint(
					0, 		//X
					0,			//Y
					i*20+(j%2) * 0.005F			//Z
				);
				side.rotateAngleZ = j * (float)(Math.PI * 2) / 6;
			}

			if (i == start || i == end) {
				ModelRenderer sidecenter = new ModelRenderer(this, 0, 12);
				anchor.addChild(sidecenter);
				fixSize(sidecenter);

				sidecenter.addBox(
					-6F, 	//X
					-6F, 	//Y
					-0.5F,	 	//Z
					12,					//Size X
					12,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				sidecenter.setRotationPoint(
					0, 		//X
					0,			//Y
					i*20			//Z
				);
			}else{
				for (int j=0; j < 3; j++) {
					ModelRenderer sidecenter = new ModelRenderer(this, 26, 12);
					anchor.addChild(sidecenter);
					fixSize(sidecenter);

					sidecenter.addBox(
						-4F + 3, 	//X
						-2F  ,	//Y
						-0.5F,	 	//Z
						8,					//Size X
						4,					//Size Y
						1,			     	//Size Z
						0.0F			 	//Size Increasement
					);
					sidecenter.setRotationPoint(
						0, 		//X
						0,			//Y
						i*20			//Z
					);
					sidecenter.rotateAngleZ = (j+0.25F) * (float)(Math.PI * 2) / 3;
				}
			}
		}

		for (int i = 0; i < 6; i++) {
			ModelRenderer middle = new ModelRenderer(this, 0, 6);
			anchor.addChild(middle);
			fixSize(middle);

			middle.addBox(
				-30F, 	//X
				-1F - 0.7F, 	//Y
				-1F,	 	//Z
				60,					//Size X
				2,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			middle.setRotationPoint(
				0, 		//X
				0,			//Y
				(i%2) * 0.005F			//Z
			);
			middle.rotateAngleX = i * (float)(Math.PI * 2) / 6;
			middle.rotateAngleY = (float)Math.PI / 2;
		}

		outers = new ModelRenderer[6];
		for (int i = 0; i < 6; i++) {
			ModelRenderer nailAnchor = new ModelRenderer(this);
			anchor.addChild(nailAnchor);
			nailAnchor.rotateAngleX = nailRot(i);
			nailAnchor.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer outer = new ModelRenderer(this, 0, 10);
			nailAnchor.addChild(outer);
			fixSize(outer);

			outer.addBox(
				-30F, 	//X
				-0.5F, 	//Y
				-0.5F,	 	//Z
				60,					//Size X
				1,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			outer.setRotationPoint(
				0, 		//X
				-8.8F,			//Y
				0			//Z
			);
			outer.rotateAngleX = (float)Math.PI;
			outers[i] = outer;

			for (int j = -13; j <= 13; j++) {
				if (Math.abs(j) <= 6 && Math.abs(j) >= 4) {
					continue;
				}

				ModelRenderer nail = new ModelRenderer(this, 44, 13);
				outer.addChild(nail);
				fixSize(nail);

				nail.addBox(
					-0.5F, 	//X
					-1.5F, 	//Y
					-0.5F,	 	//Z
					1,					//Size X
					3,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				nail.setRotationPoint(
					j*2, 		//X
					-2F,			//Y
					0			//Z
				);
				//nail.rotateAngleZ = -angle;
				//nail.rotateAngleY = (float)Math.PI / 2;
			}
		}
    }

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		mainAnchor.rotateAngleZ = module == null ? (float)Math.PI * 5 / 4 : -((ModuleFarmer)module).getRigAngle();
		float farmAngle = module == null ? 0 : ((ModuleFarmer)module).getFarmAngle();
		anchor.rotateAngleZ = -farmAngle;
		for (int i = 0; i < 6; i++) {
			outers[i].rotateAngleX = farmAngle + nailRot(i) /*- (float)Math.PI / 4*/;
		}
		
	}

	private float nailRot(int i) {
		return (i+0.5F) * (float)(Math.PI * 2) / 6;
	}
}
