package vswe.stevescarts.Arcade;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.Arcade.Place.PLACE_STATE;
import vswe.stevescarts.Interfaces.GuiMinecart;

public abstract class Property extends Place {

	

	private String name;
	private int cost;
	private Piece owner;
	private PropertyGroup group;
	private boolean mortgaged;
	
	public Property(ArcadeMonopoly game, PropertyGroup group, String name, int cost) {
		super(game);
		this.group = group;
		group.add(this);
		this.name = name;
		this.cost = cost;
	}
	

	public void drawValue(GuiMinecart gui) {
		Note.drawValue(game, gui, 10, 103, 2, cost);
	}
	
	@Override
	public void drawText(GuiMinecart gui, EnumSet<PLACE_STATE> states) {
		game.getModule().drawSplitString(gui, name, 3 + gui.getGuiLeft(), getTextY() + gui.getGuiTop(), ArcadeMonopoly.PLACE_WIDTH - 6, true, 0x404040);
	}

	protected abstract int getTextY();

	public int getCost() {
		return cost;
	}
	
	public void setOwner(Piece val) {
		owner = val;
	}
	
	public Piece getOwner() {
		return owner;
	}
	
	public boolean hasOwner() {
		return owner != null;
	}
	
	@Override
	public boolean onPieceStop(Piece piece) {
		return owner == null || owner == piece || mortgaged;
	}

	public PropertyGroup getGroup() {
		return group;
	}
	
	public abstract int getRentCost();
	
	public int getMortgageValue() {
		return getCost() / 2;
	}	
	
	public int getOwnedInGroup() {
		int owned = 0;
		for (Property property : getGroup().getProperties()) {
			if (property.getOwner() == getOwner() && !property.isMortgaged()) {
				++owned;
			}
		}	
		return owned;
	}	
	
	public boolean isMortgaged() {
		return mortgaged;
	}


	public boolean canMortgage() {
		return true;
	}


	public void mortgage() {
		mortgaged = true;
	}


	public int getUnMortgagePrice() {
		return (int)(getMortgageValue() * 1.1F);
	}


	public void unMortgage() {
		mortgaged = false;
	}
}
