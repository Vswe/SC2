package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

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
	
	public abstract void draw(GuiMinecart gui);
	
	public UPDATE_RESULT update() {
		if (!dead) {
			hitCalculation();
		}
		
		return dead ? UPDATE_RESULT.DEAD : UPDATE_RESULT.DONE;
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
		return isUnitAinUnitB(this, unit) || isUnitAinUnitB(unit, this);
	}
	
	private boolean isUnitAinUnitB(Unit a, Unit b) {
		return ((a.x >= b.x && a.x <= b.x + b.getHitboxWidth()) || (a.x + a.getHitboxWidth() >= b.x && a.x + a.getHitboxWidth() <= b.x + b.getHitboxWidth())) &&
		((a.y >= b.y && a.y <= b.y + b.getHitboxHeight()) || (a.y + a.getHitboxHeight() >= b.y && a.y + a.getHitboxHeight() <= b.y + b.getHitboxHeight()));
			
	}

	protected boolean isPlayer() {
		return false;
	}
	
	protected boolean isObstacle() {
		return false;
	}

	protected abstract int getHitboxWidth();
	protected abstract int getHitboxHeight();	
	

	public static enum UPDATE_RESULT {
		DONE,
		TURN_BACK,
		DEAD, 
		GAME_OVER, 
		TARGET
	}

}
