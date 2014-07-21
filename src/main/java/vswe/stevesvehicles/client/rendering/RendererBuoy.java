package vswe.stevesvehicles.client.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.buoy.EntityBuoy;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelBuoy;


public class RendererBuoy extends Render {
    private ModelBase model = new ModelBuoy();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        bindEntityTexture(entity);

        GL11.glPushMatrix();
        EntityBuoy buoy = (EntityBuoy)entity;
        y += Math.sin((buoy.getRenderTick() + partialTickTime) * buoy.getRenderMultiplier() * 0.15) * 0.03;

        GL11.glTranslatef((float) x, (float) y, (float) z);

        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        model.render(entity, 0, 0, 0, 0, 0, 0.0625F);

        GL11.glPopMatrix();
    }

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/buoy/normal.png");

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}
