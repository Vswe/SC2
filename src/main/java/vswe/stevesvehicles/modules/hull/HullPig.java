package vswe.stevesvehicles.modules.hull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.vehicles.VehicleBase;

public class HullPig extends ModuleHull {
	private int oinkTimer;

	public HullPig(VehicleBase vehicle) {
		super(vehicle);
		oinkTimer = getRandomTimer();
	}

	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(isMoving);
		} else {
			return 1;
		}
	}

	@Override
	public void update() {
		if (oinkTimer <= 0) {
			oink();
			oinkTimer = getRandomTimer();
		} else {
			oinkTimer--;
		}
	}

	private void oink() {
		getVehicle().getWorld().playSoundAtEntity(
				getVehicle().getEntity(), "mob.pig.say", 1.0F,
				(getVehicle().getRandom().nextFloat() - getVehicle().getRandom().nextFloat()) * 0.2F + 1.0F
		);
	}

	private int getRandomTimer() {
		return oinkTimer = getVehicle().getRandom().nextInt(900) + 300;
	}

	private ItemStack getHelmet() {
		Entity rider = getVehicle().getEntity().riddenByEntity;
		if (rider != null && rider instanceof EntityLivingBase) {
			return ((EntityLivingBase) rider).getEquipmentInSlot(4);
		}
		return null;
	}

	public boolean hasHelmet() {
		ItemStack item = getHelmet();
		if (item != null) {
			if (item.getItem() instanceof ItemArmor) {
				if (((ItemArmor) item.getItem()).armorType == 0) {
					return true;
				}
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getHelmetResource(boolean isOverlay) {
		if (hasHelmet()) {
			ItemStack item = getHelmet();
			if (item.getItem() == null) {
				return null;
			}
			return RenderBiped.getArmorResource(null, item, 0, isOverlay ? "overlay" : null);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasHelmetColor(boolean isOverlay) {
		return getHelmetColor(isOverlay) != -1;
	}

	@SideOnly(Side.CLIENT)
	public int getHelmetColor(boolean isOverlay) {
		if (hasHelmet()) {
			ItemStack item = getHelmet();
			return (item.getItem()).getColorFromItemStack(item, isOverlay ? 1 : 0);
		}
		return -1;
	}

	@SideOnly(Side.CLIENT)
	public boolean getHelmetMultiRender() {
		if (hasHelmet()) {
			ItemStack item = getHelmet();
			return (item.getItem()).requiresMultipleRenderPasses();
		}
		return false;
	}
}