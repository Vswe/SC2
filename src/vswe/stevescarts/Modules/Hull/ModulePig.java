package vswe.stevescarts.Modules.Hull;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Carts.MinecartModular;

public class ModulePig extends ModuleHull {
	public ModulePig(MinecartModular cart) {
		super(cart);
		oinkTimer = getRandomTimer();
	}


	private void oink() {
		getCart().worldObj.playSoundAtEntity(getCart(), "mob.pig.say", 1.0F, (getCart().rand.nextFloat() - getCart().rand.nextFloat()) * 0.2F + 1.0F);	
	}
	
	private int oinkTimer;
	
	private int getRandomTimer() {
		return oinkTimer = getCart().rand.nextInt(900) + 300;
	}
	
	@Override
	public void update() {
		if (oinkTimer <= 0) {
			oink();
			oinkTimer = getRandomTimer();
		}else{
			oinkTimer--;
		}
	}
	
	private ItemStack getHelmet() {
		Entity rider = getCart().riddenByEntity;
		if (rider != null && rider instanceof EntityLiving) {	
			return ((EntityLiving)rider).getCurrentItemOrArmor(4);
		}
		
		return null;
	}



	public boolean hasHelment() {
		ItemStack item = getHelmet();
		if (item != null) {
			if (item.getItem() instanceof ItemArmor) {
				if (((ItemArmor)item.getItem()).armorType == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ResourceLocation getHelmetResource(boolean isOverlay) {
		if (hasHelment()) {
			ItemStack item = getHelmet();
			
			if ((ItemArmor)item.getItem() == null) {
				return null;
			}	
				
			return RenderBiped.getArmorResource((AbstractClientPlayer)null, item, 0, (String)null);
			
			//old code left here for reference
			/*if (item.getItem() instanceof IArmorTextureProvider) {
				return ((IArmorTextureProvider)item.getItem()).getArmorTextureFile(item);
			}else{
				int index = ((ItemArmor)item.getItem()).renderIndex;
				
				if (index < 0 || index >= RenderPlayer.armorFilenamePrefix.length) {
					return null;
				}
				
				
				if (isOverlay) {
					return "/armor/" + RenderPlayer.armorFilenamePrefix[index] + "_1_b.png";
				}else{
					return "/armor/" + RenderPlayer.armorFilenamePrefix[index] + "_1.png";
				}
			}*/

		}
		return null;
	}
	
	public boolean hasHelmetColor(boolean isOverlay) {
		return getHelmetColor(isOverlay) != -1;
	}
	
	public int getHelmetColor(boolean isOverlay) {		
		if (hasHelment()) {
			ItemStack item = getHelmet();
			return ((ItemArmor)item.getItem()).getColorFromItemStack(item, isOverlay ? 1 : 0);
		}
		return -1;
	}	
	
	public boolean getHelmetMultiRender() {
		if (hasHelment()) {
			ItemStack item = getHelmet();
			return ((ItemArmor)item.getItem()).requiresMultipleRenderPasses();
		}	
		
		return false;
	}
	

	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(isMoving);
		}else{
			return 1;		
		}
	}	
	
}