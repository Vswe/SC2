package vswe.stevesvehicles.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarCompact;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCompactSolarPanel extends ModelCartbase
{
	private static ResourceLocation texture = ResourceHelper.getResource("/models/panelModelSideActive.png");
	
	private static ResourceLocation texture2 = ResourceHelper.getResource("/models/panelModelSideIdle.png");
	
	public ResourceLocation getResource(ModuleBase module) {
		if (module != null && ((ModuleSolarCompact)module).getLight() == 15) {
			return texture;
		}else{
			return texture2;
		}
	}	
		
	


	protected int getTextureWidth() {
		return 64;
	}
	protected int getTextureHeight() {
		return 32;
	}

	ModelRenderer[][] models;
    public ModelCompactSolarPanel()
    {
		models = new ModelRenderer[2][];
		models[0] = createSide(false);
		models[1] = createSide(true);
    }

	private ModelRenderer[] createSide(boolean opposite) {
		ModelRenderer anchor = new ModelRenderer(this, 0, 0);
		AddRenderer(anchor);

		if (opposite) {
			anchor.rotateAngleY = (float)Math.PI;
		}

		ModelRenderer base = new ModelRenderer(this, 0, 0);
		anchor.addChild(base);
		fixSize(base);

		base.addBox(
			-7, 	//X
			-6, 	//Y
			-1.5F,	 	//Z
			14,					//Size X
			6,					//Size Y
			3,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		base.setRotationPoint(
			0, 		//X
			2F,			//Y
			-9F			//Z
		);

		ModelRenderer panelarminner = new ModelRenderer(this, 34, 0);
		anchor.addChild(panelarminner);
		fixSize(panelarminner);

		panelarminner.addBox(
			-1, 	//X
			-1, 	//Y
			-2,	 	//Z
			2,					//Size X
			2,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		panelarminner.setRotationPoint(
			0, 		//X
			-1F,			//Y
			0			//Z
		);
		
		ModelRenderer panelarmouter = new ModelRenderer(this, 34, 0);
		panelarminner.addChild(panelarmouter);
		fixSize(panelarmouter);

		panelarmouter.addBox(
			-1, 	//X
			-1, 	//Y
			-3F,	 	//Z
			2,					//Size X
			2,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		panelarmouter.setRotationPoint(
			0.001F, 		//X
			0.001F,			//Y
			0.001F			//Z
		);
	
		ModelRenderer panelBase = new ModelRenderer(this, 0, 9);
		panelarmouter.addChild(panelBase);
		fixSize(panelBase);

		panelBase.addBox(
			-5.5F, 	//X
			-2, 	//Y
			-1,	 	//Z
			11,					//Size X
			4,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		panelBase.setRotationPoint(
			0, 		//X
			0,			//Y
			-2.8F			//Z
		);

		ModelRenderer panelTop = createPanel(panelBase,10, 4, -0.497F, 0, 15); 
		ModelRenderer panelBot = createPanel(panelBase,10, 4, -0.494F, 22, 15); 
		
		ModelRenderer panelLeft = createPanel(panelBase,6, 4, -0.491F, 0, 20); 
		ModelRenderer panelRight = createPanel(panelBase,6, 4, -0.488F, 14, 20); 

		ModelRenderer panelTopLeft = createPanel(panelLeft,6, 4, 0.002F, 0, 25); 			
		ModelRenderer panelBotLeft = createPanel(panelLeft,6, 4, 0.001F, 28, 25); 
		
		ModelRenderer panelTopRight = createPanel(panelRight,6, 4, 0.002F, 14, 25); 			
		ModelRenderer panelBotRight = createPanel(panelRight,6, 4, 0.001F, 42, 25);		
		
		return new ModelRenderer[] {panelBase, panelTop, panelBot, panelLeft, panelRight, panelTopLeft, panelTopRight, panelBotLeft, panelBotRight, panelarmouter, panelarminner};
	}
	
	private ModelRenderer createPanel(ModelRenderer parent, int width, int height, float offset, int textureOffsetX, int textureOffsetY) {
		ModelRenderer panel = new ModelRenderer(this, textureOffsetX, textureOffsetY);
		parent.addChild(panel);
		fixSize(panel);

		panel.addBox(
			-width/2, 	//X
			-height/2, 	//Y
			-0.5F,	 	//Z
			width,				//Size X
			height,				//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		panel.setRotationPoint(
			0, 		//X
			0,			//Y
			offset			//Z
		);	
		return panel;
	}

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		if (module == null) {
			for (int i = 0; i < 2; i++) {
				ModelRenderer[] models = this.models[i];
				models[9].rotationPointZ = 0.6F;
				models[10].rotationPointZ = -8.1F;
				
				models[1].rotationPointY = -0.1F;
				models[2].rotationPointY = 0.1F;
				models[3].rotationPointX = -2.01F;
				models[4].rotationPointX = 2.01F;
				models[5].rotationPointY = models[6].rotationPointY = -0.1F;
				models[7].rotationPointY = models[8].rotationPointY = 0.1F;

				models[9].rotateAngleX = 0F;
			}
		}else{
	
			ModuleSolarCompact solar = (ModuleSolarCompact)module;
		
			for (int i = 0; i < 2; i++) {
				ModelRenderer[] models = this.models[i];
				models[9].rotationPointZ = 1-solar.getExtractionDist();
				models[10].rotationPointZ = -7.7F - solar.getInnerExtraction();
				
				models[1].rotationPointY = -solar.getTopBotExtractionDist();
				models[2].rotationPointY = solar.getTopBotExtractionDist();
				models[3].rotationPointX = -2-solar.getLeftRightExtractionDist();
				models[4].rotationPointX = 2+solar.getLeftRightExtractionDist();
				models[5].rotationPointY = models[6].rotationPointY = -solar.getCornerExtractionDist();
				models[7].rotationPointY = models[8].rotationPointY = solar.getCornerExtractionDist();

				models[9].rotateAngleX = -solar.getPanelAngle();
			}
		}
	}
}
