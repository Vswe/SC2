package vswe.stevesvehicles.old.Arcade;

import java.util.ArrayList;

public abstract class CardVillager extends Card {

	public CardVillager(String message) {
		super(message);
	}

	@Override
	public int getBackgroundV() {
		return 2;
	}

	public static ArrayList<CardVillager> cards;
	static {
		cards = new ArrayList<CardVillager>();
		
		cards.add(new CardVillager("No, I'm a helicopter.") {		
			@Override
			public void doStuff(ArcadeMonopoly game, Piece piece) {
				
			}			
		});
			
	}	
	
}
