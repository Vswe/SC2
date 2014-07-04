package vswe.stevesvehicles.arcade.monopoly;

import java.util.ArrayList;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class Piece {

	private ArcadeMonopoly game;
	private int pos;
	private int u;
	private int extended;
	private int[] money;
	private ControlledBy control;
	private ArrayList<NoteAnimation> animationNotes;
	private ArrayList<NoteAnimation> oldnotes;
	private boolean bankrupt;
	private int turnsInJail;
	
	public Piece(ArcadeMonopoly game, int u, ControlledBy control) {
		this.game = game;
		this.pos = 0;
		this.u = u;
		this.money = new int[] {30, 30, 30, 30, 30, 30, 30};
		this.control = control;
		animationNotes = new ArrayList<NoteAnimation>();
		oldnotes = new ArrayList<NoteAnimation>();
		turnsInJail = -1;
	}
	
	public void move(int dif) {
		pos = (pos + dif) % (ArcadeMonopoly.BOARD_WIDTH * 2 + ArcadeMonopoly.BOARD_HEIGHT * 2);
	}

	public int getPosition() {
		return pos;
	}

	
	public int getV() {
		return u;
	}
	
	public int[] getNoteCount() {
		return money;
	}
	

	
	public int getNoteCount(Note note) {
		int money = this.money[note.getId()];
				
		for (int i = 0; i < oldnotes.size(); i++) {
			if (note == oldnotes.get(i).getNote()) {
				money -= 1;
			}
		}

		return money;
	}	
	
	public int getTotalMoney() {
		int money = 0;
		
		for (int i = 0; i < Note.notes.size(); i++){
			money += Note.notes.get(i).getUnits() * this.money[i];
		}

        for (NoteAnimation oldNote : oldnotes) {
            money -= oldNote.getNote().getUnits();
        }
		
		return money;
	}
	
	public void addMoney(int money, boolean useAnimation) {
		for (int i = Note.notes.size() - 1; i >= 0; i--) {
			Note note = Note.notes.get(i);
			
			int notesToAdd = money / note.getUnits();
			if (notesToAdd > 0) {
				addMoney(note, notesToAdd, true);
				money -= notesToAdd * note.getUnits();
			}
			
			if (money == 0) {
				return;
			}
		}		
	}
	
	public void addMoney(Note note, int amount, boolean useAnimation) {
		if (useAnimation) {
			int min = 10;
			for (NoteAnimation animation : animationNotes) {
				if (animation.getAnimation() < min) {
					min = animation.getAnimation();
				}
			}
			
			for (int i = 0; i < amount; i++) {
				animationNotes.add(0, new NoteAnimation(note, min - 10, true));
				min -=10;
			}
			
		}else{
			money[note.getId()] += amount;
		}
	}
	
	public void removeNewNoteAnimation(int i) {
		if (animationNotes.get(i).isNew()) {
			addMoney(animationNotes.get(i).getNote(), 1, false);
		}else{
			Note note = animationNotes.get(i).getNote();
			for (int j = oldnotes.size() - 1; j >= 0; j--) {
				if (note == oldnotes.get(j).getNote()) {
					oldnotes.remove(j);
					break;
				}
			}
			removeMoney(note, 1, false);
		}
		animationNotes.remove(i);
	}
	
	public ArrayList<NoteAnimation> getAnimationNotes() {
		return animationNotes;
	}
	
	public boolean removeMoney(int money, boolean useAnimation) {
		int [] noteCounts = new int[Note.notes.size()];
		int [] moneyBelowThisLevel = new int[Note.notes.size()];
		
		int totalmoney = 0;
		for (int i = 0; i < noteCounts.length; i++) {
			noteCounts[i] = getNoteCount(Note.notes.get(i));
			moneyBelowThisLevel[i] = totalmoney;
			totalmoney += noteCounts[i] * Note.notes.get(i).getUnits();
		}
		
		if (totalmoney >= money) {		
			for (int i = Note.notes.size() - 1; i >= 0; i--) {
				Note note = Note.notes.get(i);
				int notesToRemove = money / note.getUnits();
				notesToRemove = Math.min(notesToRemove, noteCounts[i]);

				removeMoney(note, notesToRemove, useAnimation);
				money -= note.getUnits() * notesToRemove;
				if(money == 0) {
					return true;
				}else if (moneyBelowThisLevel[i] < money) {
					removeMoney(note, 1, useAnimation);
					money -= note.getUnits();
					
					addMoney(-money, useAnimation);
					
					return true;
				} 
				
			}
		}
		
		return false;
	}
	
	private void removeMoney(Note note, int amount, boolean useAnimation) {
		if (useAnimation) {
			int min = 10;
			for (NoteAnimation animation : animationNotes) {
				if (animation.getAnimation() < min) {
					min = animation.getAnimation();
				}
			}
			
			for (int i = 0; i < amount; i++) {
				NoteAnimation animation = new NoteAnimation(note, min - 10, false);
				animationNotes.add(0, animation);
				oldnotes.add(0, animation);
				min -=10;
			}	
		}else{
			money[note.getId()] -= amount;
		}
	}
	
	
	public int[] getMenuRect(int i) {
		int w = 50 + extended;
		
		
		return new int[] {VehicleBase.MODULAR_SPACE_WIDTH - w, 10 + i * 30, w, 30};
	}

	public int[] getPlayerMenuRect(int i) {
		int[] menu = getMenuRect(i);
		return new int[] {menu[0] + 19, menu[1] + 3, 24, 24};
	}

	public void updateExtending(boolean inRect) {
		if (inRect && extended < 175) {
			extended = Math.min(175, extended + 20);
		}else if(!inRect && extended > 0){
			extended = Math.max(0, extended - 50); 
		}
	}
	
	public static enum ControlledBy {
		PLAYER,
		COMPUTER,
		OTHER
	}
	
	public ControlledBy getController() {
		return control;
	}

	public boolean showProperties() {
		return this == game.getCurrentPiece();
	}

	public boolean canAffordProperty(Property property) {	
		return getTotalMoney() >= property.getCost();
	}
	

	public void purchaseProperty(Property property) {
		if (removeMoney(property.getCost(), true)) {
			property.setOwner(this);
		}else{
			System.out.println("Couldn't remove the resources, this is very weird :S");
		}
	}

	public void bankrupt(Piece owesMoneyToThis) {
		int money = getTotalMoney();
		removeMoney(money, true);
		if (owesMoneyToThis != null) {
			owesMoneyToThis.addMoney(money, true);
		}
		for (Place place : game.getPlaces()) {
			if (place instanceof Property) {
				Property property = (Property)place;
				if (property.getOwner() == this) {
					property.setOwner(owesMoneyToThis);
				}
			}
		}
		bankrupt = true;
	}

	public boolean canAffordRent(Property property) {
		return getTotalMoney() >= property.getRentCost();
	}

	public void payPropertyRent(Property property) {
		if (removeMoney(property.getRentCost(), true)) {
			property.getOwner().addMoney(property.getRentCost(), true);
		}else{
			System.out.println("Couldn't remove the resources, this is very weird :S");
		}
	}
	
	public boolean isBankrupt() {
		return bankrupt;
	}

	public boolean canAffordStructure(Street street) {
		return getTotalMoney() >= street.getStructureCost();
	}

	public void buyStructure(Street street) {
		if (removeMoney(street.getStructureCost(), true)) {
			street.increaseStructure();
		}else{
			System.out.println("Couldn't remove the resources, this is very weird :S");
		}		
	}
	
	public boolean isInJail() {
		return turnsInJail >= 0;
	}

	public void goToJail() {
		turnsInJail = 0;
		pos = ArcadeMonopoly.BOARD_WIDTH;
	}
	
	public void releaseFromJail() {
		turnsInJail = -1;
	}

	public void increaseTurnsInJail() {
		++turnsInJail;	
	}

	public int getTurnsInJail() {
		return turnsInJail;
	}

	
	public void payFine() {
		if (removeMoney(50, true)) {
			releaseFromJail();
		}else{
			System.out.println("Couldn't remove the resources, this is very weird :S");
		}	
	}

	public boolean canAffordFine() {
		return getTotalMoney() >= 50;
	}

	public void getMoneyFromMortgage(Property selectedPlace) {
		addMoney(selectedPlace.getMortgageValue(), true);
		selectedPlace.mortgage();
	}

	public boolean canAffordUnMortgage(Property selectedPlace) {
		return getTotalMoney() >= selectedPlace.getUnMortgagePrice();
	}

	public void payUnMortgage(Property selectedPlace) {
		if (removeMoney(selectedPlace.getUnMortgagePrice(), true)) {
			selectedPlace.unMortgage();
		}else{
			System.out.println("Couldn't remove the resources, this is very weird :S");
		}	
	}

	public void sellStructure(Street selectedPlace) {
		addMoney(selectedPlace.getStructureSellPrice(), true);
		selectedPlace.decreaseStructures();		
	}
}
