package vswe.stevesvehicles.old.Arcade;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class Player extends Unit {

	
	
	public Player(ArcadeInvaders game, int x, int y) {
		super(game, x, y);
	}
	
	public Player(ArcadeInvaders game) {
		this(game, 200, 150);
		ready = true;
	}

	@Override
	public void draw(GuiVehicle gui) {
		if (ready || targetY == y) {
			game.drawImageInArea(gui, x, y, 16, 16, 16, 16);
		}else{
			game.drawImageInArea(gui, x, y, 16, 16, 16, 16, 3, 0, 1000, 1000);
		}
	}
	
	protected boolean ready;
	private int targetX;
	private int targetY;
	protected void setTarget(int x, int y) {
		targetX = x;
		targetY = y;
	}
	
	@Override
	public UPDATE_RESULT update() {
		if (!ready) {
			
			if (targetY == this.y && targetX == this.x) {
				ready = true;
			}else if (targetY == this.y) {
				this.x = Math.min(targetX, this.x + 8);
			}else if (this.x == -15) {
				this.y = Math.max(targetY, this.y - 8);	
			}else{
				this.x = Math.max(-15, this.x - 8);
			}
			
			
		}else{
			if (super.update() == UPDATE_RESULT.DEAD) {
				return UPDATE_RESULT.DEAD;
			}
		}
		
		

		return UPDATE_RESULT.DONE;
	}	
	
	public void move(int dir) {
		this.x += dir * 5;
		if (x < 10) {
			x = 10;
		}else if(x > VehicleBase.MODULAR_SPACE_WIDTH - 10 - 16) {
			x = VehicleBase.MODULAR_SPACE_WIDTH - 10 - 16;
		}
	}
	
	@Override
	protected boolean isPlayer() {
		return true;
	}

	@Override
	protected int getHitboxWidth() {
		return 16;
	}

	@Override
	protected int getHitboxHeight() {
		return 16;
	}	
}
