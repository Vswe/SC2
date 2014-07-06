package vswe.stevesvehicles.arcade.invader;

import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class Projectile extends Unit {

	protected boolean playerProjectile;
	
	public Projectile(ArcadeInvaders game, int x, int y, boolean playerProjectile) {
		super(game, x, y);
		
		this.playerProjectile = playerProjectile;
	}

	@Override
	public void draw(GuiVehicle gui) {
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
	public UpdateResult update() {
		if (super.update() == UpdateResult.DEAD) {
			return UpdateResult.DEAD;
		}
		
		this.y += playerProjectile ? -5 : 5;
		if (this.y < 0 || this.y > VehicleBase.MODULAR_SPACE_HEIGHT) {
			this.dead = true;
			return UpdateResult.DEAD;
		}
		
		return UpdateResult.DONE;
	}

	@Override
	protected int getHitBoxWidth() {
		return playerProjectile ? 5 : 6;
	}

	@Override
	protected int getHitBoxHeight() {
		return playerProjectile ? 16 : 6;
	}
	
	
	
}
