package vswe.stevesvehicles.old.Arcade;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;

public class Building extends Unit {

	public Building(ArcadeInvaders game, int x, int y) {
		super(game, x, y);
		this.health = 10;
	}

	@Override
	public void draw(GuiVehicle gui) {
		game.getModule().drawImage(gui, x, y, 32 + (10 - this.health) * 16, 16, 16, 16);
	}

	@Override
	protected int getHitboxWidth() {
		return 16;
	}

	@Override
	protected int getHitboxHeight() {
		return 16;
	}
	
	@Override
	protected boolean isObstacle() {
		return true;
	}

	
	@Override
	public UPDATE_RESULT update() {
		if (super.update() == UPDATE_RESULT.DEAD) {
			return UPDATE_RESULT.DEAD;
		}
		
		for (Unit invader : game.invaders) {
			if (!invader.dead && collidesWith(invader)) {
				dead = true;
				health = 0;
				return UPDATE_RESULT.DEAD;
			}
		}
		

		return UPDATE_RESULT.DONE;
	}	
	
}
