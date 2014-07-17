package vswe.stevesvehicles.client.rendering.models.boat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelHull extends ModelVehicle {
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}

    @Override
	protected int getTextureHeight() {
		return 32;
	}

	private final ResourceLocation resource;


    public ModelHull(ResourceLocation resource) {
		this.resource = resource;

        byte boatLength = 24;
        byte boatHeight = 6;
        byte boatWidth = 20;
        byte boatOffSurface = 4;

        ModelRenderer bot = new ModelRenderer(this, 0, 8);
        ModelRenderer front = new ModelRenderer(this, 0, 0);
        ModelRenderer left = new ModelRenderer(this, 0, 0);
        ModelRenderer right = new ModelRenderer(this, 0, 0);
        ModelRenderer back = new ModelRenderer(this, 0, 0);

		addRenderer(bot);
		addRenderer(front);
		addRenderer(left);
		addRenderer(right);
		addRenderer(back);

        bot.addBox(
                -boatLength / 2,
                -boatWidth / 2 + 2,
                -3.0F,
                boatLength,
                boatWidth - 4,
                4,
                0.0F
        );
        bot.setRotationPoint(
                0.0F,
                boatOffSurface,
                0.0F
        );

        front.addBox(
                -boatLength / 2 + 2,
                -boatHeight - 1,
                -1.0F,
                boatLength - 4,
                boatHeight,
                2,
                0.0F);
        front.setRotationPoint(
                -boatLength / 2 + 1,
                boatOffSurface,
                0.0F);

        back.addBox(
                -boatLength / 2 + 2,
                -boatHeight - 1,
                -1.0F,
                boatLength - 4,
                boatHeight,
                2,
                0.0F
        );
        back.setRotationPoint(
                boatLength / 2 - 1,
                boatOffSurface,
                0.0F);

        left.addBox(
                -boatLength / 2 + 2,
                -boatHeight - 1,
                -1.0F,
                boatLength - 4,
                boatHeight, 2,
                0.0F
        );
        left.setRotationPoint(
                0.0F,
                boatOffSurface,
                -boatWidth / 2 + 1
        );

        right.addBox(
                -boatLength / 2 + 2,
                -boatHeight - 1,
                -1.0F,
                boatLength - 4,
                boatHeight,
                2,
                0.0F
        );
        right.setRotationPoint(
                0.0F,
                boatOffSurface,
                boatWidth / 2 - 1
        );


        bot.rotateAngleX = ((float)Math.PI / 2F);
        front.rotateAngleY = ((float)Math.PI * 3F / 2F);
        back.rotateAngleY = ((float)Math.PI / 2F);
        left.rotateAngleY = (float)Math.PI;
    }

    @Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		if (module != null) {
			float[] color = module.getVehicle().getColor();
			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		}
		super.render(module,yaw,pitch,roll, multiplier, partialTime);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }	
}
