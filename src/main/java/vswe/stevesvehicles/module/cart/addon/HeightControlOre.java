package vswe.stevesvehicles.module.cart.addon;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class HeightControlOre {
	public final String name;
	public final boolean useDefaultTexture;
	public final ResourceLocation specialTexture;
	public final int srcX;
	public final int srcY;
	public final int spanHighest;
	public final int spanLowest;
	public final int bestHighest;
	public final int bestLowest;

	public static final List<HeightControlOre> ores = new ArrayList<HeightControlOre>();

	static {
		new HeightControlOre("diamond", 0, 15, 12, 5);
		new HeightControlOre("redstone", 1, 16 ,13, 5);
		new HeightControlOre("gold", 2, 33, 30, 5);
		new HeightControlOre("lapis_lazuli", 3, 32, 18, 11);
		new HeightControlOre("iron", 4, 67, 41, 5);
		new HeightControlOre("coal", 5, 131, 40, 5);
		new HeightControlOre("emerald", 6, 32, 29, 5);
	}

	public HeightControlOre(String name, int textureId, int spanHighest, int bestHighest, int bestLowest) {
		this.name = name;
		this.useDefaultTexture = true;
		this.specialTexture = null;
		this.spanHighest = spanHighest;
		this.spanLowest = 1;
		this.bestHighest = bestHighest;
		this.bestLowest = bestLowest;
		this.srcX = 1;
		this.srcY = 1 + (textureId * 2 + 1) * 5;

		ores.add(this);
	}

	@SuppressWarnings("UnusedDeclaration")
    public HeightControlOre(String name, ResourceLocation texture, int srcX, int srcY, int spanHighest, int spanLowest, int bestHighest, int bestLowest) {
		this.name = name;
		this.useDefaultTexture = false;
		this.specialTexture = texture;
		this.spanHighest = spanHighest;
		this.spanLowest = spanLowest;
		this.bestHighest = bestHighest;
		this.bestLowest = bestLowest;
		this.srcX = srcX;
		this.srcY = srcY;

		ores.add(this);
	}
}