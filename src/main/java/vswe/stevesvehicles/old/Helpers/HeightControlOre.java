package vswe.stevesvehicles.old.Helpers;
import java.util.ArrayList;

public class HeightControlOre {
	public final String name;
	public final boolean useDefaultTexture;
	public final String specialTexture;
	public final int srcX;
	public final int srcY;
	public final int spanHighest;
	public final int spanLowest;
	public final int bestHighest;
	public final int bestLowest;

	public static final ArrayList<HeightControlOre> ores = new ArrayList<HeightControlOre>();

	static {
		new HeightControlOre("Diamond", 0, 15, 12, 5);
		new HeightControlOre("Redstone", 1, 16 ,13, 5);
		new HeightControlOre("Gold", 2, 33, 30, 5);
		new HeightControlOre("Lapis Lazuli", 3, 32, 18, 11);
		new HeightControlOre("Iron", 4, 67, 41, 5);
		new HeightControlOre("Coal", 5, 131, 40, 5);
		new HeightControlOre("Emerald", 6, 32, 29, 5);
	}

	public HeightControlOre(String name, int textureId, int spanHighest, int bestHighest, int bestLowest) {
		this.name = name;
		this.useDefaultTexture = true;
		this.specialTexture = "";
		this.spanHighest = spanHighest;
		this.spanLowest = 1;
		this.bestHighest = bestHighest;
		this.bestLowest = bestLowest;
		this.srcX = 1;
		this.srcY = 1 + (textureId * 2 + 1) * 5;

		ores.add(this);
	}

	public HeightControlOre(String name, String texture, int srcX, int srcY, int spanHighest, int spanLowest, int bestHighest, int bestLowest) {
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