package vswe.stevesvehicles.old.Arcade;


public class Villager extends CardPlace {

	public Villager(ArcadeMonopoly game) {
		super(game);
	}

	@Override
	protected int getTextureId() {
		return 9;
	}

	@Override
	public Card getCard() {
		return CardVillager.cards.get(game.getModule().getCart().rand.nextInt(CardVillager.cards.size()));
	}
	
}
