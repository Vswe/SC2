package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCakeDynamite;

public class ModuleCakeServerDynamite extends ModuleCakeServer {

	private int dynamiteCount;
	
	private int getMaxDynamiteCount() {
		return Math.min(StevesCarts.instance.maxDynamites, 25);
	}
	
	public ModuleCakeServerDynamite(MinecartModular cart) {
		super(cart);
	}

	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotCakeDynamite(getCart(),slotId,8+x*18,38+y*18);
	}	
	
	@Override
	public boolean dropOnDeath() {
		return dynamiteCount == 0;
	}

	@Override
    public void onDeath() {
		if (dynamiteCount > 0) {
			explode();
		}
    }
	
	private void explode() {		
		getCart().worldObj.createExplosion(null, getCart().posX, getCart().posY, getCart().posZ, dynamiteCount * 0.8F, true);
	}

	@Override
	public void update() {
		super.update();
		
		if (!getCart().worldObj.isRemote) {
			ItemStack item = getStack(0);
			if (item != null && item.getItem().equals(StevesCarts.component) && item.getItemDamage() == 6 && dynamiteCount < getMaxDynamiteCount()) {
				int count = Math.min(getMaxDynamiteCount() - dynamiteCount, item.stackSize);
				dynamiteCount += count;
				item.stackSize -= count;
				if (item.stackSize == 0) {
					setStack(0, null);
				}
			}
		}
	}
	
	@Override
	public boolean onInteractFirst(EntityPlayer entityplayer) {
		if (dynamiteCount > 0) {
			explode();
			getCart().setDead();
			return true;
		}else{
			return super.onInteractFirst(entityplayer);
		}
	}
}
