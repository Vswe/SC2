package vswe.stevesvehicles.arcade.invader;

import vswe.stevesvehicles.client.gui.screen.GuiVehicle;

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
	protected int getHitBoxWidth() {
		return 16;
	}

	@Override
	protected int getHitBoxHeight() {
		return 16;
	}
	
	@Override
	protected boolean isObstacle() {
		return true;
	}

	
	@Override
	public UpdateResult update() {
		if (super.update() == UpdateResult.DEAD) {
			return UpdateResult.DEAD;
		}
		
		for (Unit invader : game.invaders) {
			if (!invader.dead && collidesWith(invader)) {
				dead = true;
				health = 0;
				return UpdateResult.DEAD;
			}
		}
		

		return UpdateResult.DONE;
	}	
	
}
