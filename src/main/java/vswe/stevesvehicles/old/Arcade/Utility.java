package vswe.stevesvehicles.old.Arcade;

public class Utility extends Property {
	private int utilId;
	public Utility(ArcadeMonopoly game, PropertyGroup group, int utilId, String name) {
		super(game, group, name, 150);
		this.utilId = utilId;
	}
	
	@Override
	protected int getTextureId() {
		return 6 + utilId;
	}

	@Override
	protected int getTextY() {
		return 10;
	}

	@Override
	public int getRentCost() {
		return getRentCost(getOwnedInGroup());
	}
	

	public int getId() {
		return utilId;
	}
	
	public int getRentCost(int owned) {
		return game.getTotalDieEyes() * getMultiplier(owned);
	}

	public static int getMultiplier(int i) {
		switch (i) {
			default:
				return 0;
			case 1:
				return 6;
			case 2:
				return 15;
			case 3:
				return 50;
		}
	}
	
	
}
