package vswe.stevescarts.old.Arcade;


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
		return CardChance.cards.get(game.getModule().getCart().rand.nextInt(CardChance.cards.size()));
	}
	
}
