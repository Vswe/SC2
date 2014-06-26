package vswe.stevesvehicles.models;
import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelGun extends ModelVehicle
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/gunModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}			

	protected int getTextureWidth() {
		return 32;
	}

	protected int getTextureHeight() {
		return 8;
	}

    public ModelGun()
	{
		super();
	}

    public ModelGun(ArrayList<Integer> pipes)
    {
		guns = new ModelRenderer[pipes.size()];
		for (int i = 0; i < pipes.size(); i++) {
			float angle = (new int[] {3,4,5,2,-1,6,1,0,7})[pipes.get(i)];
			angle *= (float)Math.PI / 4F;

			ModelRenderer gunAnchorAnchor = new ModelRenderer(this);
			AddRenderer(gunAnchorAnchor);

			gunAnchorAnchor.rotateAngleY = angle;

			guns[i] = createGun(gunAnchorAnchor);
		}
    }

	protected ModelRenderer createGun(ModelRenderer parent) {
		ModelRenderer gunAnchor = new ModelRenderer(this);
		parent.addChild(gunAnchor);

		gunAnchor.setRotationPoint(
			2.5F, 		//X
			0,			//Y
			0		//Z
		);

		ModelRenderer gun = new ModelRenderer(this, 0, 16);
		fixSize(gun);
		gunAnchor.addChild(gun);

		gun.addBox(
			-1.5F, 	//X
			-2.5F , 	//Y
			-1.5F,	 	//Z
			7,					//Size X
			3,					//Size Y
			3,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		gun.setRotationPoint(
			0, 		//X
			-9F,			//Y
			0		//Z
		);

		return gun;
	}

	ModelRenderer[] guns;

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		for (int i = 0; i < guns.length; i++) {
			guns[i].rotateAngleZ = module == null ? 0 : ((ModuleShooter)module).getPipeRotation(i);
		}	
	}
}
