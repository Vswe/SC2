package vswe.stevesvehicles.client.rendering.models;
import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.engine.ModuleSolarTop;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelSolarPanelHeads extends ModelSolarPanel {
	private static final ResourceLocation TEXTURE_ACTIVE = ResourceHelper.getResource("/models/panelModelActive.png");
	private static final ResourceLocation TEXTURE_IDLE = ResourceHelper.getResource("/models/panelModelIdle.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		if (module != null && ((ModuleSolarTop)module).getLight() == 15) {
			return TEXTURE_ACTIVE;
		}else{
			return TEXTURE_IDLE;
		}
	}		

    @Override
	protected int getTextureWidth() {
		return 32;
	}

    @Override
	protected int getTextureHeight() {
		return 16;
	}

    public ModelSolarPanelHeads(int panelCount) {
		panels = new ArrayList<ModelRenderer>();

		ModelRenderer moving = createMovingHolder(0,0);

		for (int i = 0; i < panelCount; i++) {
			createPanel(moving,i);
		}
    }

	private void createPanel(ModelRenderer base, int index) {
		float rotation;
		float f;
		
		switch (index) {
			case 0:
				rotation = 0F;
				f = -1.5F;
				break;
			case 1:
				rotation = (float)Math.PI;
				f = -1.5F;
				break;
			case 2:
				rotation = (float)Math.PI * 3F / 2F;
				f = -6F;
				break;
			case 3:
				rotation = (float)Math.PI / 2F;
				f = -6F;
				break;
			default:
				return;
		}
		createPanel(base, rotation, f);
	}
	
	private void createPanel(ModelRenderer base,float rotation,float f) {
		ModelRenderer panel = new ModelRenderer(this, 0, 0);
		fixSize(panel);
		base.addChild(panel);

		panel.addBox(
			-6F, 	        //X
			0, 	            //Y
			-2,	 	        //Z
			12,				//Size X
			13,				//Size Y
			2,			    //Size Z
			0.0F
		);

		panel.setRotationPoint(
			(float)Math.sin(rotation) * f, 		//X
			-5F,			                    //Y
			(float)Math.cos(rotation) * f		//Z
		);

		panel.rotateAngleY = rotation;

		panels.add(panel);
	}

	private ArrayList<ModelRenderer> panels;
    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		super.applyEffects(module,yaw,pitch,roll);


		for (ModelRenderer panel : panels) {
			panel.rotateAngleX = module == null ? 0F : -((ModuleSolarTop)module).getInnerRotation();
		}
	}
}
