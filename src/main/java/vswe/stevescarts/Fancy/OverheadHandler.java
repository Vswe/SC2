package vswe.stevescarts.Fancy;


import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class OverheadHandler extends FancyPancyHandler {

    private ModelRenderer model;


    private Map<String, OverheadData> dataObjects;

    private class OverheadData {
        private ResourceLocation resourceLocation;
        private ThreadDownloadImageData image;

        private OverheadData(AbstractClientPlayer player) {
            resourceLocation = getDefaultResource(player);
            dataObjects.put(StringUtils.stripControlCodes(player.getDisplayName()), this);
        }
    }

    public OverheadHandler() {
        super("Overhead");
        MinecraftForge.EVENT_BUS.register(this);
        dataObjects = new HashMap<String, OverheadData>();

        ModelBase base = new ModelBase() {};
        model = new ModelRenderer(base, 0, 0);
        model.addBox(-16, 0, 0, 32, 26, 0);
    }

    private OverheadData getData(AbstractClientPlayer player) {
        OverheadData data = dataObjects.get(StringUtils.stripControlCodes(player.getDisplayName()));
        if (data == null) {
            data = new OverheadData(player);
        }

        return data;
    }

    @Override
    public String getDefaultUrl(AbstractClientPlayer player) {
        return null;
    }

    @Override
    public ResourceLocation getDefaultResource(AbstractClientPlayer player) {
        return null;
    }

    @Override
    public ThreadDownloadImageData getCurrentTexture(AbstractClientPlayer player) {
        return getData(player).image;
    }

    @Override
    public ResourceLocation getCurrentResource(AbstractClientPlayer player) {
        return getData(player).resourceLocation;
    }

    @Override
    public void setCurrentResource(AbstractClientPlayer player, ResourceLocation resource, String url) {
        OverheadData data = getData(player);

        data.resourceLocation = resource;
        data.image = tryToDownloadFancy(resource, url);
    }

    @Override
    public LOAD_TYPE getDefaultLoadType() {
        return LOAD_TYPE.OVERRIDE;
    }

    @Override
    public String getDefaultUrl() {
        return null;
    }

    @SubscribeEvent
    public void render(RenderLivingEvent.Specials.Post event) {

        if (event.entity instanceof AbstractClientPlayer && event.renderer instanceof RenderPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer)event.entity;
            RenderPlayer renderer = (RenderPlayer)event.renderer;
            EntityPlayer observer = Minecraft.getMinecraft().thePlayer;
            boolean isObserver = player == observer;

            double distanceSq = player.getDistanceSqToEntity(observer);
            double distanceLimit = player.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;

            if (distanceSq < distanceLimit * distanceLimit) {
                if (player.isPlayerSleeping()) {
                    renderOverHead(renderer, player, "Hello", event.x, event.y - 1.5D, event.z, isObserver);
                }else{
                    renderOverHead(renderer, player, "Hello", event.x, event.y, event.z, isObserver);
                }
            }


        }
    }

    private void renderOverHead(RenderPlayer renderer, AbstractClientPlayer player, String text, double x, double y, double z, boolean isObserver) {
        OverheadData data = getData(player);
        if (data.image != null && data.image.isTextureUploaded()) {
            RenderManager renderManager = ReflectionHelper.getPrivateValue(Render.class, renderer, 1);
            //check if it's in an inventory
            if (isObserver && player.openContainer != null && renderManager.playerViewY == 180 /* set to 180 when rendering, it might be 180 at other points but won't be the end of the world*/) {
                return;
            }
            renderManager.renderEngine.bindTexture(data.resourceLocation);

            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + player.height + (isObserver ? 0.8F : 1.1F), (float)z);
            GL11.glNormal3f(0, 1, 0);
            GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
            GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);

            GL11.glScalef(-1, -1, 1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
            model.render(0.015F);



            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            GL11.glPopMatrix();
        }
    }


}
