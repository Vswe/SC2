package vswe.stevescarts.Renders;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import vswe.stevescarts.Items.ModItems;

import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import vswe.stevescarts.ModuleData.ModuleData;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import vswe.stevescarts.Models.Cart.ModelCartbase;
public class RendererMinecartItem implements IItemRenderer {
	public RendererMinecartItem() {
		MinecraftForgeClient.registerItemRenderer(ModItems.carts, this);
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
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		if (type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0, -1, 1);
		}else if(type == ItemRenderType.INVENTORY) {
			GL11.glTranslatef(0, 0.1F, 0);
		}
		NBTTagCompound info = item.getTagCompound();
		if (info != null) {
			NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
			byte [] bytes = moduleIDTag.func_150292_c();
			
			HashMap<String, ModelCartbase> models = new HashMap<String, ModelCartbase>();
			
			float lowestMult = 1.0F;
			
			for (byte id : bytes) {
				ModuleData module = ModuleData.getList().get(id);
				if (module != null && module.haveModels(true)) {
					if (module.getModelMult() < lowestMult) {
						lowestMult = module.getModelMult();
					}
					models.putAll(module.getModels(true));
				}
			}
			
			for (byte id : bytes) {
				ModuleData module = ModuleData.getList().get(id);
				if (module != null && module.haveRemovedModels()) {
					for (String str : module.getRemovedModels()) {
						models.remove(str);
					}
				}
			}	

			if(type == ItemRenderType.INVENTORY) {
				GL11.glScalef(lowestMult, lowestMult, lowestMult);
			}			
			
			for (ModelCartbase model : models.values()) {
				model.render(null, null, 0, 0, 0, 0.0625F, 0);
			}
		}


		GL11.glPopMatrix();	
	}
	
}