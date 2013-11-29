package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class NoteAnimation {

	private Note note;
	private int animation;
	private boolean isNew;
	
	
	public NoteAnimation(Note note, int start, boolean isNew) {
		this.note = note;
		this.animation = start;
		this.isNew = isNew;
	}

	public boolean draw(ArcadeMonopoly game, GuiMinecart gui, int x, int y) {
		if (animation >= 0) {
			if (isNew) {
				note.draw(game, gui, x, y - 10 + animation / 2);
			}else{
				note.draw(game, gui, x, y + animation);
			}
			
		}
		return ++animation > 20;
	}
	
	public Note getNote() {
		return note;
	}	
	
	public int getAnimation() {
		return animation;
	}
	
	public boolean isNew() {
		return isNew;
	}
	
}
