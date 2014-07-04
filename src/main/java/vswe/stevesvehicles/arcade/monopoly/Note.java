package vswe.stevesvehicles.arcade.monopoly;

import java.util.ArrayList;

import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class Note {

	
	public static final ArrayList<Note> notes = new ArrayList<Note>();
	public static final Note COAL = new Note(0, 1, 0, 0);
	public static final Note IRON = new Note(1, 5, 1, 0);
	public static final Note REDSTONE = new Note(2, 10, 2, 0);
	public static final Note GOLD = new Note(3, 20, 3, 0);
	public static final Note LAPIS = new Note(4, 50, 0, 1);
	public static final Note DIAMOND = new Note(5, 100, 1, 1);
	public static final Note EMERALD = new Note(6, 500, 2, 1);
	
	private int units;
	private int u;
	private int v;
	private int id;
	
	public Note(int id, int units, int u, int v) {
		this.id = id;
		this.units = units;
		this.u = u;
		this.v = v;
		
		notes.add(this);
	}
	
	public int getId() {
		return id;
	}
	
	public void draw(ArcadeMonopoly game, GuiVehicle gui, int x, int y) {
		game.loadTexture(gui, 1);
		game.getModule().drawImage(gui, x, y, ArcadeMonopoly.PLACE_WIDTH + u * 16, 22 + 16 + v * 16, 16, 16);		
	}
	
	public void draw(ArcadeMonopoly game, GuiVehicle gui, int x, int y, int amount) {
		draw(game, gui, x, y, amount, 0x404040);
	}
	
	public void draw(ArcadeMonopoly game, GuiVehicle gui, int x, int y, int amount, int color) {
		draw(game, gui, x + 10, y);
		game.getModule().drawString(gui, amount + "x ", new int[] {x + gui.getGuiLeft(), y + gui.getGuiTop(), 10, 16}, color);		
	}
	
	public void drawPlayer(ArcadeMonopoly game, GuiVehicle gui, int x, int y, int amount) {
		game.loadTexture(gui, 1);
		game.drawImageInArea(gui, x, y, ArcadeMonopoly.PLACE_WIDTH + u * 16, 22 + 16 + v * 16, 16, 16);
		if (x + 16 < VehicleBase.MODULAR_SPACE_WIDTH) {
			game.getModule().drawString(gui, String.valueOf(amount), x + gui.getGuiLeft(), y + 17 + gui.getGuiTop(), 16, true, 0x404040);
		}
	}	
	
	public static int drawValue(ArcadeMonopoly game, GuiVehicle gui, int x, int y, int maxNoteCount, int value) {
		int id = 0;
		for (int i = notes.size() - 1; i >= 0; i--) {
			if (value >= notes.get(i).units && (maxNoteCount != 1 || value % notes.get(i).units == 0)) {
				int amount = value / notes.get(i).units;
				value -= amount * notes.get(i).units;
				notes.get(i).draw(game, gui, x + id * 34, y, amount);
				id++;
				maxNoteCount--;
			}
		}
		return id;
	}
	
	public static void drawPlayerValue(ArcadeMonopoly game, GuiVehicle gui, int x, int y, int[] values) {
		
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).drawPlayer(game, gui, x + (6 - i) * 20, y, values[i]);
		}
	}	
	
	
	public int getUnits() {
		return units;
	}
}
