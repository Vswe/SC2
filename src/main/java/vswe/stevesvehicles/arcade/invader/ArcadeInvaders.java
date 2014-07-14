package vswe.stevesvehicles.arcade.invader;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import vswe.stevesvehicles.arcade.ArcadeGame;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.arcade.tracks.TrackStory;
import vswe.stevesvehicles.localization.entry.arcade.LocalizationInvaders;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.common.attachment.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArcadeInvaders extends ArcadeGame {

	public ArcadeInvaders(ModuleArcade module) {
		super(module, LocalizationInvaders.TITLE);
		
		invaders = new ArrayList<Unit>();
		buildings = new ArrayList<Unit>();
		lives = new ArrayList<Player>();
		projectiles = new ArrayList<Projectile>();
		start();
	}
	
	
	private void start() {		
		buildings.clear();
		lives.clear();
		projectiles.clear();
		
		
		player = new Player(this);
		for (int i = 0; i < 3; i++) {
			lives.add(new Player(this, 10 + i * 20, 190));
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				buildings.add(new Building(this, 48 + i * 96 + j * 16, 120));
			}
		}		
		
		moveSpeed = 0;
		fireDelay = 0;
		score = 0;
		canSpawnPahiGhast = false;
		newHighscore = false;
		spawnInvaders();
	}
	
	private void spawnInvaders() {
		invaders.clear();
		hasPahiGhast = false;
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 14; i++) {
				invaders.add(new InvaderGhast(this, 20 + i * 20, 10 + 25 * j));
			}
		}	
		moveSpeed++;
		moveDirection = 1;
		moveDown = 0;
	}
	
	protected ArrayList<Unit> invaders;
	private ArrayList<Player> lives;
	private ArrayList<Unit> buildings;
	protected ArrayList<Projectile> projectiles;
	private Player player;
	protected int moveDirection;
	protected int moveSpeed;
	protected int moveDown;
	private int fireDelay;
	private int score;
	private int highscore;
	protected boolean hasPahiGhast;
	protected boolean canSpawnPahiGhast;
	private boolean newHighscore;
	private int gameOverCounter;
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void update() {
		super.update();
		
		if (player != null ) {
			if (player.ready) {
				boolean flag = false;
				boolean flag2 = false;
				for (int i = invaders.size() - 1; i >= 0; i--) {
					Unit invader = invaders.get(i);
					
					Unit.UpdateResult result = invader.update();
					if (result == Unit.UpdateResult.DEAD) {
						if (((InvaderGhast)invader).isPahiGhast) {
							hasPahiGhast = false;
						}
						playDefaultSound("mob.ghast.scream", 0.03F, 1);
						invaders.remove(i);
						score += 1;
					}else if(result == Unit.UpdateResult.TURN_BACK) {
						flag = true;
					}else if(result == Unit.UpdateResult.GAME_OVER) {
						flag2 = true;
					}
				}
				
				if (moveDown > 0) {
					--moveDown;
				}
				
				if (flag) {
					moveDirection *= -1;
					moveDown = 5;
				}

				
				if (invaders.size() == 0 || (hasPahiGhast && invaders.size() == 1)) {
					score += hasPahiGhast ? 200 : 50;
					canSpawnPahiGhast = true;
					spawnInvaders();
				}
				
				if (flag2) {
					lives.clear();
					projectiles.clear();
					player = null;
					newHighScore();
					return;
				}
				
				for (int i = buildings.size() - 1; i >= 0; i--) {
					if (buildings.get(i).update() == Unit.UpdateResult.DEAD) {
						buildings.remove(i);
					}
				}	
				
				
				for (int i = projectiles.size() - 1; i >= 0; i--) {
					if (projectiles.get(i).update() == Unit.UpdateResult.DEAD) {
						projectiles.remove(i);
					}
				}		
		
			
			
			
				if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
					player.move(-1);
				}else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
					player.move(1);
				}
				
				if(fireDelay == 0 && Keyboard.isKeyDown(Keyboard.KEY_W)) {
					projectiles.add(new Projectile(this, player.x + 8 - 2, player.y - 15, true));	
					playDefaultSound("random.bow", 0.8F, 1.0F / (getModule().getVehicle().getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
					fireDelay = 10;
				}else if(fireDelay > 0) {
					fireDelay--;
				}
			}
			
			if (player.update() == Unit.UpdateResult.DEAD) {
				projectiles.clear();
				playSound("hit", 1, 1);
					
				if (lives.size() != 0) {
					lives.get(0).setTarget(player.x, player.y);
					player = lives.get(0);	
					lives.remove(0);
				}else{
					player = null;
					newHighScore();
				}
				
				
			}
			
		}else if (gameOverCounter == 0) {
			boolean flag = false;
			for (int i = invaders.size() - 1; i >= 0; i--) {
				Unit invader = invaders.get(i);
				if (invader.update() == Unit.UpdateResult.TARGET) {
					flag = true;
				}
			}
			
			if (!flag) {
				gameOverCounter = 1;
			}
		}else if (newHighscore && gameOverCounter < 5) {
			gameOverCounter++;
			if (gameOverCounter == 5) {
				playSound("highscore", 1, 1);
			}
		}
		
	}
	
	private static final String TEXTURE = "/gui/invaders.png";
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);
		
		
		for (int i = 0; i < 27; i++) {
			getModule().drawImage(gui, 5 + i * 16, 150, 16, 32, 16, 16);
		}
		
		for (int i = 0; i < 5; i++) {
			getModule().drawImage(gui, 3 + i * 16, 190, 16, 32, 16, 16);
		}		
			
		for (Unit invader : invaders) {
			invader.draw(gui);
		}
	
		if (player != null) {
			player.draw(gui);
		}
		for (Unit player: lives) {
			player.draw(gui);
		}
				
		for (Unit projectile : projectiles) {
			projectile.draw(gui);
		}	
		
		
		for (Unit building : buildings) {
			building.draw(gui);
		}
	}

	

	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
		getModule().drawString(gui, LocalizationInvaders.LIVES.translate() + ":", 10, 180, 0x404040);
		getModule().drawString(gui, LocalizationInvaders.HIGH_SCORE.translate(String.valueOf(highscore)), 10, 210, 0x404040);
		getModule().drawString(gui, LocalizationInvaders.SCORE.translate(String.valueOf(score)), 10, 220, 0x404040);
		
		getModule().drawString(gui, "W - " + LocalizationInvaders.SHOOT.translate(), 330, 180, 0x404040);
		getModule().drawString(gui, "A - " + LocalizationInvaders.LEFT.translate(), 330, 190, 0x404040);
		getModule().drawString(gui, "D - " + LocalizationInvaders.RIGHT.translate(), 330, 200, 0x404040);
		getModule().drawString(gui, "R - " + LocalizationInvaders.RESTART.translate(), 330, 220, 0x404040);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {
		if (Character.toLowerCase(character) == 'r') {
			start();
		}
	}
	
	
	
	private final static String[][] numbers = new String[][] 
	{
		{
			"XXXX",
			"X  X",
			"X  X",
			"X  X",
			"X  X",
			"X  X",
			"XXXX"		
		},{
			"   X",
			"   X",
			"   X",
			"   X",
			"   X",
			"   X",
			"   X"				
		},{
			"XXXX",
			"   X",
			"   X",
			"XXXX",
			"X   ",
			"X   ",
			"XXXX"			
		},{
			"XXXX",
			"   X",
			"   X",
			"XXXX",
			"   X",
			"   X",
			"XXXX"			
		},{
			"X  X",
			"X  X",
			"X  X",
			"XXXX",
			"   X",
			"   X",
			"   X"	
		},{
			"XXXX",
			"X   ",
			"X   ",
			"XXXX",
			"   X",
			"   X",
			"XXXX"	
		},{
			"XXXX",
			"X   ",
			"X   ",
			"XXXX",
			"X  X",
			"X  X",
			"XXXX"	
		},{
			"XXXX",
			"   X",
			"   X",
			"   X",
			"   X",
			"   X",
			"   X"				
		},{
			"XXXX",
			"X  X",
			"X  X",
			"XXXX",
			"X  X",
			"X  X",
			"XXXX"					
		},{
			"XXXX",
			"X  X",
			"X  X",
			"XXXX",
			"   X",
			"   X",
			"XXXX"		
		}
	};
	
	private void newHighScore() {
		buildings.clear();
		
		int digits; 
		if (score == 0) {
			digits = 1;
		}else{
			digits = (int)Math.floor(Math.log10(score)) + 1;
		}


		canSpawnPahiGhast = false;
		int currentGhast = 0;
		for (int i = 0; i < digits; i++) {
			int digit = (score / (int)Math.pow(10, (digits - i - 1))) % 10;		
			String[] number = numbers[digit];
			
			for (int j = 0; j < number.length; j++) {
				String line = number[j];
				
				for (int k = 0; k < line.length(); k++) {
					if (line.charAt(k) == 'X') {
						
						
						int x = (VehicleBase.MODULAR_SPACE_WIDTH - (digits * 90 - 10)) / 2 + i * 90 + k * 20;
						int y = 5 + j * 20;
						
						InvaderGhast ghast;
						if (currentGhast >= invaders.size()) {
							invaders.add(ghast = new InvaderGhast(this, x, -20));
							currentGhast++;
						}else{
							ghast = (InvaderGhast)invaders.get(currentGhast++);
						}
						
						 
						
						ghast.setTarget(x, y);
					}
				}
			}			
		}
		
		for (int i = currentGhast; i < invaders.size(); i++) {
			InvaderGhast ghast = (InvaderGhast)invaders.get(i);
			ghast.setTarget(ghast.x, -25);
		}

		
		gameOverCounter = 0;
		if (score > highscore) {
			newHighscore = true;			
			int val = score;

            DataWriter dw = getDataWriter();
            dw.writeShort(val);
			sendPacketToServer(dw);
		}
		
	}
	
	@Override
	public void receivePacket(DataReader dr, EntityPlayer player) {
	    highscore = dr.readShort();
	}	
	
	
	@Override
	public void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short) (highscore));
	}


    @Override
    public int numberOfGuiData() {
        return 1;
    }

    @Override
	public void receiveGuiData(int id, short data) {
        highscore = data;
	}	
	
	@Override
	public void save(NBTTagCompound tagCompound) {
		tagCompound.setShort("Highscore", (short)highscore);
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		highscore = tagCompound.getShort("Highscore");
	}	
	
	
}
