package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelToolPlate extends ModelCartbase
{


	private static ResourceLocation texture = ResourceHelper.getResource("/models/toolPlateModel.png");
	
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

    public ModelToolPlate()
    {
		super();

		ModelRenderer drillBase = new ModelRenderer(this, 0, 0);
		AddRenderer(drillBase);

        drillBase.addBox(
			-cartWidth / 2 + 3,		//X
			-cartHeight + 1, 		//Y
			-2.0F, 					//Z
			10, 					//Size X
			6,						//Size Y
			1,						//Size Z
			0.0F					//Size Increasement
		);
        drillBase.setRotationPoint(
			-cartLength / 2 + 1,	//X
			cartOnGround,			//Y
			0.0F					//Z
		);

		drillBase.rotateAngleY = ((float)Math.PI / 2F);
    }
}
