package vswe.stevesvehicles.client.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.buoy.BuoyType;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelBuoy;
import vswe.stevesvehicles.item.ModItems;

public class RenderBuoyItem implements IItemRenderer {
    private ModelBase model = new ModelBuoy();
	public RenderBuoyItem() {
		MinecraftForgeClient.registerItemRenderer(ModItems.buoys, this);
	}

  /** 
     * Checks if this renderer should handle a specific item's render type
     * @param item The item we are trying to render
     * @param type A render type to check if this renderer handles
     * @return true if this renderer should handle the given render type,
     * otherwise false
     */
	@Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
	}
    
    /**
     * Checks if certain helper functionality should be executed for this renderer.
     * See ItemRendererHelper for more info
     * 
     * @param type The render type
     * @param item The ItemStack being rendered
     * @param helper The type of helper functionality to be ran
     * @return True to run the helper functionality, false to not.
     */
	@Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}
    
    /**
     * Called to do the actual rendering, see ItemRenderType for details on when specific 
     * types are run, and what extra data is passed into the data parameter.
     * 
     * @param type The render type
     * @param item The ItemStack being rendered
     * @param data Extra Type specific data
     */
	@Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		if (type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0, -1, 1);
		}else if(type == ItemRenderType.INVENTORY) {
			GL11.glTranslatef(0, 0.5F, 0);
            GL11.glScalef(0.65F, 0.65F, 0.65F);
		}

        ResourceHelper.bindResource(BuoyType.getType(item.getItemDamage()).getTexture());
		model.render(null, 0, 0, 0, 0, 0, 0.0625F);

		GL11.glPopMatrix();	
	}
	
}