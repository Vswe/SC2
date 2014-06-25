package vswe.stevesvehicles.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public abstract class ModelWire extends ModelCartbase
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/wireModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}

	protected int getTextureWidth() {
		return 32;
	}
	protected int getTextureHeight() {
		return 2;
	}

    public ModelWire()
    {
		super();
    }

	protected int baseZ() {
		return 0;
	}

	protected void CreateEnd(int x, int y) {
		CreateEnd(x,y,baseZ());
	}

	protected void CreateEnd(int x, int y, int z) {
		ModelRenderer end = new ModelRenderer(this, 28, 0);
		AddRenderer(end);

		end.addBox(
			0.5F, 					//X
			0.5F, 					//Y
			0.5F,	 				//Z
			1,					//Size X
			1,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);

		end.setRotationPoint(
			-7.5F + y, 		//X
			-5.5F - z,			//Y
			-5.5F + x			//Z
		);
	}

	protected void CreateWire(int x1, int y1, int x2, int y2) {
		CreateWire(x1,y1,baseZ(),x2,y2,baseZ());
	}

	protected void CreateWire(int x1, int y1, int z1, int x2, int y2, int z2) {
		if (x1 != x2 && y1 != y2 && z1 != z2) {
			return;
		}

		int length;
		boolean rotate;
		boolean rotateZ;
		if (y1 != y2) {
			rotate = false;
			rotateZ = false;
			length = y2 - y1 + 1;
			if (length < 0) {
				length*=-1;
				int y = y1;
				y1 = y2;
				y2 = y;
			}
		}else if(z1 != z2){
			rotate = false;
			rotateZ = true;
			length = z2 - z1 + 1;
			if (length < 0) {
				length*=-1;
				int z = z1;
				z1 = z2;
				z2 = z;
			}
		}else{
			rotate = true;
			rotateZ = false;
			length = x2 - x1 + 1;
			if (length < 0) {
				length*=-1;
				int x = x1;
				x1 = x2;
				x2 = x;
			}
		}

		if (length > 13) {
			return;
		}

		ModelRenderer wire = new ModelRenderer(this, 0, 0);
		AddRenderer(wire);

		wire.addBox(
			length / 2F, 					//X
			0.5F, 					//Y
			0.5F,	 				//Z
			length,					//Size X
			1,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);

		if (rotateZ) {
			wire.setRotationPoint(
				-7.5F+y1, 		//X
				-4.0F + length / 2F-z1,			//Y
				-5.5F + x1			//Z
			);
			wire.rotateAngleZ = ((float)Math.PI * 3F / 2F);
		}else if (rotate) {
			wire.setRotationPoint(
				-5.5F+y1, 		//X
				-5.5F-z1,			//Y
				-5.0F - length / 2F + x1			//Z
			);
			wire.rotateAngleY = ((float)Math.PI * 3F / 2F);
		}else{
			wire.setRotationPoint(
				-7.0F - length / 2F + y1, 		//X
				-5.5F-z1,			//Y
				-5.5F+x1			//Z
			);
		}
	}
}
