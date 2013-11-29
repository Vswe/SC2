package vswe.stevescarts.Models.Cart;
import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModelCartbase extends ModelBase
{
	//Compared to the center of the cart(before the object has been rotated)
	//X > 0 Back
	//X < 0 Forward
	//Y > 0 Down
	//Y < 0 Up
	//Z > 0 Right
	//Z < 0 Left

	//{0,0,0} is in the middle of the floor of the cart
	//positive size means that the opposing corner of the object has a higher x/y/z value



	protected final byte cartLength = 20;
    protected final byte cartHeight = 8;
    protected final byte cartWidth = 16;
    protected final byte cartOnGround = 4; //lower values makes the cart float higher


    @SideOnly(Side.CLIENT)
	public abstract ResourceLocation getResource(ModuleBase module);

	
	private ArrayList<ModelRenderer> renderers;
	
    public ModelCartbase()
    {
		renderers = new ArrayList<ModelRenderer>();
	}




    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
		
		ResourceLocation resource = getResource(module);
		if (resource == null) {
			return;
		}
		
		//render.loadTexture(StevesCarts.instance.texturePath + "/" + modelTexture(engine));
		//net.minecraftforge.client.ForgeHooksClient.engine().func_98187_b(texture);
		//net.minecraft.client.Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		ResourceHelper.bindResource(resource);
		
		applyEffects( module, yaw,pitch,roll, partialtime);
		do_render(mult);
    }
	
	
    public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll, float partialtime) {
    	applyEffects(module, yaw, pitch, roll);
    }
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {}
	
	//has to be called before anything is added to the renderer
	protected void AddRenderer(ModelRenderer renderer) {
		renderers.add(fixSize(renderer));
	}

	public ModelRenderer fixSize(ModelRenderer renderer) {
		return renderer.setTextureSize(getTextureWidth(), getTextureHeight());
	}



	protected int getTextureWidth() {
		return 64;
	}
	protected int getTextureHeight() {
		return 64;
	}

	public float extraMult() {
		return 1F;
	}
	
	protected void do_render(float mult) {
		for (ModelRenderer renderer : renderers) {
			renderer.render(mult*extraMult());
		}
	}
}
