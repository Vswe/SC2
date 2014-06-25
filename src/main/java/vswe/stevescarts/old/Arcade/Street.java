package vswe.stevescarts.old.Arcade;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.client.interfaces.GuiVehicle;

public class Street extends Property {

	private float[] color;	
	private int structures;
	private int baseRent;
	
	public Street(ArcadeMonopoly game, StreetGroup group, String name, int cost, int baseRent) {
		super(game, group, name, cost);
		this.color = group.getColor(); 
		this.baseRent = baseRent;
	}
	
	@Override
	public void draw(GuiVehicle gui, EnumSet<PLACE_STATE> states) {
		super.draw(gui, states);
		
		GL11.glColor4f(color[0], color[1], color[2], 1.0F);		
		game.getModule().drawImage(gui, 0, 0, ArcadeMonopoly.PLACE_WIDTH, 0, ArcadeMonopoly.PLACE_WIDTH, 22);
		
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		if (structures > 0 && structures < 5) {	
			for (int i = 0; i < structures; i++) {
				game.getModule().drawImage(gui, 3 + i * 18, 3, 76, 22, 16, 16);
			}
		}else if(structures == 5) {
			game.getModule().drawImage(gui, 3, 3, 92, 22, 16, 16);
		}	
		
		drawValue(gui);
	}
	
	public void increaseStructure() {
		structures++;	
	}

	@Override
	protected int getTextY() {
		return 30;
	}


	public int getRentCost(int structureCount) {
		switch (structureCount) {
			default:
				return baseRent;
			case 1:
				return baseRent * 5;
			case 2:
				return baseRent * 15;
			case 3:
				return baseRent * 40;
			case 4:
				return baseRent * 70;
			case 5:
				return baseRent * 100;	
		}
	}
	
	public int getRentCost(boolean ownsAll) {
		if (ownsAll) {
			return baseRent * 2;
		}else{
			return baseRent;
		}
	}	
	
	@Override
	public int getRentCost() {
		if (structures == 0) {
			return getRentCost(ownsAllInGroup(getOwner()));
		}else{
			return getRentCost(structures);
		}
	}

	public int getStructureCount() {
		return structures;
	}

	public int getStructureCost() {
		return ((StreetGroup)getGroup()).getStructureCost();
	}

	public boolean ownsAllInGroup(Piece currentPiece) {
		for (Property property : getGroup().getProperties()) {
			if (property.getOwner() != currentPiece || property.isMortgaged()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canMortgage() {
		return super.canMortgage() && structures == 0;
	}

	public int getStructureSellPrice() {
		return getStructureCost() / 2;
	}

	public void decreaseStructures() {
		--structures;
	}
	
}
