package vswe.stevesvehicles.arcade.invader;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;

public abstract class Unit {

	
	protected int x;
	protected int y;
	protected ArcadeInvaders game;
	protected boolean dead;	
	protected int health;
	
	public Unit(ArcadeInvaders game, int x, int y) {
		this.x = x;
		this.y = y;
		this.game = game;
		this.health = 1;
	}
	
	public abstract void draw(GuiVehicle gui);
	
	public UpdateResult update() {
		if (!dead) {
			hitCalculation();
		}
		
		return dead ? UpdateResult.DEAD : UpdateResult.DONE;
	}
	
	
	protected void hitCalculation() {
		for (Projectile projectile : game.projectiles) {
			if (!projectile.dead && (isObstacle() || projectile.playerProjectile != isPlayer())) {
				if (collidesWith(projectile)) {
					health--;
					if (health == 0) {
						this.dead = true;
					}
					projectile.dead = true;	
				}
			}
		}
	}

	
	protected boolean collidesWith(Unit unit) {
		return isUnitAInUnitB(this, unit) || isUnitAInUnitB(unit, this);
	}
	
	private boolean isUnitAInUnitB(Unit a, Unit b) {
		return ((a.x >= b.x && a.x <= b.x + b.getHitBoxWidth()) || (a.x + a.getHitBoxWidth() >= b.x && a.x + a.getHitBoxWidth() <= b.x + b.getHitBoxWidth())) &&
		((a.y >= b.y && a.y <= b.y + b.getHitBoxHeight()) || (a.y + a.getHitBoxHeight() >= b.y && a.y + a.getHitBoxHeight() <= b.y + b.getHitBoxHeight()));
	}

	protected boolean isPlayer() {
		return false;
	}
	
	protected boolean isObstacle() {
		return false;
	}

	protected abstract int getHitBoxWidth();
	protected abstract int getHitBoxHeight();
	

	public static enum UpdateResult {
		DONE,
		TURN_BACK,
		DEAD, 
		GAME_OVER, 
		TARGET
	}

}
