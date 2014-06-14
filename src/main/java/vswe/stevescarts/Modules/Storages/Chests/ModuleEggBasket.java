package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Carts.MinecartModular;
import net.minecraft.item.ItemStack;
public class ModuleEggBasket extends ModuleChest {
	public ModuleEggBasket(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getInventoryWidth()
	{
		return 6;
	}
	@Override
	protected int getInventoryHeight() {
		return 4;
	}

	@Override
	protected boolean playChestSound() {
		return false;
	}	
	
	protected float getLidSpeed() {
		return (float)(Math.PI / 150);
	}	
		
	protected float chestFullyOpenAngle() {
		return (float)Math.PI / 8F;
	}	
	
	@Override
	public byte getExtraData() {
		return (byte)0; //empty, sorry :)
	}
	
	@Override
	public boolean hasExtraData() {
		return true;
	}
	
	@Override
	public void setExtraData(byte b) {
		if (b == 0) {
			return; //empty, sorry :)
		}
		
		java.util.Random rand = getCart().rand;
		int eggs = 1 + rand.nextInt(4) + rand.nextInt(4); 		
		ItemStack easterEgg = ComponentTypes.PAINTED_EASTER_EGG.getItemStack(eggs);
		setStack(0, easterEgg);
		
		
	}	
}