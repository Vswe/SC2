package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Carts.MinecartModular;

public class Projectile extends Unit {

	
	protected boolean playerProjectile;
	
	public Projectile(ArcadeInvaders game, int x, int y, boolean playerProjectile) {
		super(game, x, y);
		
		this.playerProjectile = playerProjectile;
	}

	@Override
	public void draw(GuiMinecart gui) {
		if (playerProjectile) {
			game.getModule().drawImage(gui, x, y, 38, 0, 5, 16);
		}else{
			game.getModule().drawImage(gui, x, y, 32, 0, 6, 6);
		}
	}
	
	@Override
	protected void hitCalculation() {
		//don't do anything
	}	

	@Override
	public UPDATE_RESULT update() {
		if (super.update() == UPDATE_RESULT.DEAD) {
			return UPDATE_RESULT.DEAD;
		}
		
		this.y += playerProjectile ? -5 : 5;
		if (this.y < 0 || this.y > MinecartModular.MODULAR_SPACE_HEIGHT) {
			this.dead = true;
			return UPDATE_RESULT.DEAD;
		}
		
		return UPDATE_RESULT.DONE;
	}

	@Override
	protected int getHitboxWidth() {
		return playerProjectile ? 5 : 6;
	}

	@Override
	protected int getHitboxHeight() {
		return playerProjectile ? 16 : 6;
	}
	
	
	
}
