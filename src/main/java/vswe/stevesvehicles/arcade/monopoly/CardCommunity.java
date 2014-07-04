package vswe.stevesvehicles.arcade.monopoly;

import java.util.ArrayList;

public abstract class CardCommunity extends Card {

	public CardCommunity(String message) {
		super(message);
	}

	@Override
	public int getBackgroundV() {
		return 1;
	}
	
	public static ArrayList<CardCommunity> cards;
	static {
		cards = new ArrayList<CardCommunity>();
		
		cards.add(new CardCommunity("You just found a ton of buckets in the dungeon.") {		
			@Override
			public void doStuff(ArcadeMonopoly game, Piece piece) {
				piece.addMoney(Note.IRON, 9, true);
			}	
			
			@Override
			public int getNoteCount() {			
				return 9;
			}
			
			@Override
			public Note getNote() {
				return Note.IRON;
			}
			
			@Override
			public String getMoneyPrefix() {
				return "Collect";
			}
		});
		
		cards.add(new CardCommunity("D2") {		
			@Override
			public void doStuff(ArcadeMonopoly game, Piece piece) {
				
			}			
		});		
		
		cards.add(new CardCommunity("D3") {		
			@Override
			public void doStuff(ArcadeMonopoly game, Piece piece) {
				
			}			
		});	
	}

}
