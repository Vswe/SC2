package vswe.stevescarts.old.Arcade;

import vswe.stevescarts.client.interfaces.GuiVehicle;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class InvaderGhast extends Unit {

	private int tentacleTextureId;
	private int shooting;
	protected boolean isPahighast;
	
	private boolean hasTarget;
	private int targetX;
	private int targetY;
	
	public InvaderGhast(ArcadeInvaders game, int x, int y) {
		super(game, x, y);
		tentacleTextureId = game.getModule().getCart().rand.nextInt(4);
		shooting = -10;
		if (game.canSpawnPahighast && !game.hasPahighast && game.getModule().getCart().rand.nextInt(1000) == 0) {
			isPahighast = true;
			game.hasPahighast = true;
		}
	}

	@Override
	public void draw(GuiVehicle gui) {
		if (isPahighast) {
			game.drawImageInArea(gui, x, y, 32, 32, 16, 16);
		}else{
			game.drawImageInArea(gui, x, y, shooting > -10 ? 16 : 0, 0, 16, 16);
		}
		game.drawImageInArea(gui, x, y + 16, 0, 16 + 8 * tentacleTextureId, 16, 8);

	}
	
	@Override
	public UPDATE_RESULT update() {
		if (hasTarget) {
			boolean flag = false;
			if (this.x != targetX) {
				if (this.x > targetX) {
					this.x = Math.max(targetX, this.x - 4);
				}else{
					this.x = Math.min(targetX, this.x + 4);
				}
				flag = true;
			}
			
			if (this.y != targetY) {
				if (this.y > targetY) {
					this.y = Math.max(targetY, this.y - 4);
				}else{
					this.y = Math.min(targetY, this.y + 4);
				}
				flag = true;
			}
			
			
			return flag ? UPDATE_RESULT.TARGET : UPDATE_RESULT.DONE;
		}else{
			if (super.update() == UPDATE_RESULT.DEAD) {
				return UPDATE_RESULT.DEAD;
			}		
			
			if (shooting > -10) {
				if (shooting == 0) {
					game.playDefaultSound("mob.ghast.fireball", 0.1F, 1);
					game.projectiles.add(new Projectile(game, x + 8 - 3, y + 8 - 3, false));
				}
				shooting--;
			}
			
			
			if (game.moveDown > 0) {
				this.y += 1; //game.moveSpeed;
			}else{
				this.x += game.moveDirection * game.moveSpeed;
				
				if (y > 130) {
					return UPDATE_RESULT.GAME_OVER;
				}else if (this.x > EntityModularCart.MODULAR_SPACE_WIDTH - 10 - 16 || this.x < 10) {
					return UPDATE_RESULT.TURN_BACK;
				}
			}
			
			
			if (!isPahighast && shooting == -10 && game.getModule().getCart().rand.nextInt(300) == 0) {
				shooting = 10;
			}
		}

		
		return UPDATE_RESULT.DONE;
	}

	@Override
	protected int getHitboxWidth() {
		return 16;
	}

	@Override
	protected int getHitboxHeight() {
		return 24;
	}

	
	public void setTarget(int x, int y) {
		hasTarget = true;
		targetX = x;
		targetY = y;
	}

}
