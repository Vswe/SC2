package vswe.stevesvehicles.old.Arcade;


public class Chance extends CardPlace {

	public Chance(ArcadeMonopoly game) {
		super(game);
	}

	@Override
	protected int getTextureId() {
		return 0;
	}

	@Override
	public Card getCard() {
		return CardChance.cards.get(game.getModule().getVehicle().getRandom().nextInt(CardChance.cards.size()));
	}
	
}
