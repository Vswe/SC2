package vswe.stevescarts.Modules.Storages.Chests;
import java.util.HashMap;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelExtractingChests;

public class ModuleExtractingChests extends ModuleChest {
	public ModuleExtractingChests(MinecartModular cart) {
		super(cart);
		startOffset = -14F;
		endOffset = -24.5F;
		chestOffset = -14F;
	}

	@Override
	protected int getInventoryWidth()
	{
		return 18;
	}
	@Override
	protected int getInventoryHeight() {
		return 4;
	}

	@Override
	protected float chestFullyOpenAngle() {
		return (float)Math.PI / 2F;
	}
	@Override
	protected void handleChest() {
		if (isChestActive() && lidClosed() && chestOffset > endOffset)
		{
			chestOffset -= 0.5F;
		}else if(!isChestActive() && lidClosed() && chestOffset < startOffset) {
			chestOffset += 0.5F;
		}else{
			super.handleChest();
		}
	}

	public float getChestOffset() {
		return chestOffset;
	}

	private final float startOffset;
	private final float endOffset;
	private float chestOffset;
}