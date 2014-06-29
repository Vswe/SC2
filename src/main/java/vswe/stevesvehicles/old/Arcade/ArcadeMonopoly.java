package vswe.stevesvehicles.old.Arcade;

import java.util.ArrayList;
import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.Arcade.Piece.CONTROLLED_BY;
import vswe.stevesvehicles.old.Arcade.Place.PLACE_STATE;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArcadeMonopoly extends ArcadeGame {

	private Die die;
	private Die die2;
	
	private ArrayList<Piece> pieces;
	private int currentPiece;
	
	private Place[] places;
	private int selectedPlace = -1;
	
	protected Place getSelectedPlace() {
		return selectedPlace == -1 ? null : places[selectedPlace];
	}
	
	private int diceTimer;
	private int diceCount;
	private int diceDelay;	
	
	private ArrayList<Button> buttons;
	private Button roll;
	private Button end;
	private Button purchase;
	private Button rent;
	private Button bankrupt;
	private Button bed;
	private Button card;
	private Button jail;
	private Button mortgage;
	private Button unmortgage;
	private Button sellbed;
	
	private boolean rolled;
	private boolean controllable;
	private boolean endable;
	private boolean openedCard;
	
	protected Piece getCurrentPiece() {
		return pieces.get(currentPiece);
	}
	
	private Card currentCard;
	private float cardScale;
	private int cardRotation;
	
	public static final int PLACE_WIDTH = 76;
	public static final int PLACE_HEIGHT = 122;
	public static final int BOARD_WIDTH = 14;
	public static final int BOARD_HEIGHT = 10;
	public static final float SCALE = 0.17F;
	
	public ArcadeMonopoly(ModuleArcade module) {
		super(module, Localization.ARCADE.MADNESS);
		
		pieces = new ArrayList<Piece>();
		pieces.add(new Piece(this, 0, Piece.CONTROLLED_BY.PLAYER));
		pieces.add(new Piece(this, 1, Piece.CONTROLLED_BY.COMPUTER));
		pieces.add(new Piece(this, 2, Piece.CONTROLLED_BY.COMPUTER));
		pieces.add(new Piece(this, 3, Piece.CONTROLLED_BY.COMPUTER));
		pieces.add(new Piece(this, 4, Piece.CONTROLLED_BY.COMPUTER));
	
		
		StreetGroup streetGroup1 = new StreetGroup(50, new int[] {89, 12, 56});
		StreetGroup streetGroup2 = new StreetGroup(50, new int[] {102, 45, 145});
		StreetGroup streetGroup3 = new StreetGroup(50, new int[] {135, 166, 213});
		
		StreetGroup streetGroup4 = new StreetGroup(100, new int[] {239, 56, 120});
		StreetGroup streetGroup5 = new StreetGroup(100, new int[] {245, 128, 45});
		
		StreetGroup streetGroup6 = new StreetGroup(150, new int[] {238, 58, 35});
		StreetGroup streetGroup7 = new StreetGroup(150, new int[] {252, 231, 4});
		
		StreetGroup streetGroup8 = new StreetGroup(200, new int[] {19, 165, 92});
		StreetGroup streetGroup9 = new StreetGroup(200, new int[] {40, 78, 161});
		
		PropertyGroup stationGroup = new PropertyGroup();
		PropertyGroup utilGroup = new PropertyGroup();
		
		places = new Place[] 
		{
			new Go(this),
			new Street(this, streetGroup1, "Soaryn Chest", 30, 2),
			new Community(this),
			new Street(this, streetGroup1, "Eddie's Cobble Stairs", 30, 2),
			new Place(this),
			new Utility(this, utilGroup, 0, "Test"),
			new Street(this, streetGroup2, "Ecu's Eco Escape", 60, 4),
			new Station(this, stationGroup, 0, "Wooden Station"),
			new Street(this, streetGroup2, "Test", 60, 4),
			new Villager(this),			
			new Street(this, streetGroup3, "Direwolf's 9x9", 100, 6),
			new Chance(this),
			new Street(this, streetGroup3, "Greg's Forest", 100, 6),
			new Street(this, streetGroup3, "Alice's Tunnel", 110, 8),
			new Jail(this),
			new Street(this, streetGroup4, "Flora's Alveary", 140, 10),
			new Utility(this, utilGroup, 1, "Test"),
			new Street(this, streetGroup4, "Sengir's Greenhouse", 140, 10),
			new Street(this, streetGroup4, "Test", 160, 12),
			new Station(this, stationGroup, 1, "Standard Station"),
			new Street(this, streetGroup5, "Muse's Moon Base", 200, 14),
			new Community(this),
			new Street(this, streetGroup5, "Algorithm's Crafting CPU", 200, 14),
			new Street(this, streetGroup5, "Pink Lemmingaide Stand", 240, 16),
			new CornerPlace(this, 2),
			new Street(this, streetGroup6, "Covert's Railyard", 250, 18),
			new Chance(this),
			new Street(this, streetGroup6, "Test", 250, 18),
			new Street(this, streetGroup6, "Test", 270, 20), 
			new Community(this),
			new Street(this, streetGroup6, "Test", 270, 20),
			new Station(this, stationGroup, 2, "Reinforced Station"),
			new Street(this, streetGroup7, "Player's Industrial Warehouse", 320, 22),			
			new Villager(this),
			new Street(this, streetGroup7, "Dan's Computer Repair", 320, 22),
			new Street(this, streetGroup7, "iChun's Hat Shop", 350, 24),
			new Utility(this, utilGroup, 2, "Test"),
			new Street(this, streetGroup7, "Lex's Forge", 350, 24),
			new GoToJail(this),
			new Street(this, streetGroup8, "Morvelaira's Pretty Wall", 400, 26),
			new Street(this, streetGroup8, "Rorax's Tower of Doom", 400, 26),
			new Community(this),
			new Street(this, streetGroup8, "Jaded's Crash Lab", 440, 30),
			new Station(this, stationGroup, 3, "Galgadorian Station"),
			new Chance(this), 
			new Street(this, streetGroup9, "Test", 500, 40),
			new Place(this),
			new Street(this, streetGroup9, "Vswe's Redstone Tower", 600, 50)	
		};
		
		((Property)places[1]).setOwner(pieces.get(0));
		((Property)places[3]).setOwner(pieces.get(0));
		
		/*for (int i = 0; i < pieces.size(); i++) {
			pieces.get(i).goToJail();
			pieces.get(i).releaseFromJail();
		}*/
		
		
		die = new Die(this, 0);
		die2 = new Die(this, 1);
		
		buttons = new ArrayList<Button>();
		buttons.add(roll = new Button() {
			@Override
			public String getName() {
				return "Roll";
			}
			
			@Override
			public boolean isVisible() {
				return true;
			}
			
			@Override
			public boolean isEnabled() {
				return diceCount == 0 && diceTimer == 0 && !rolled;
			}
			
			@Override
			public void onClick() {
				rolled = true;
				throwDice();
			}
			
		});
		
		buttons.add(end = new Button() {
			@Override
			public String getName() {
				return "End Turn";
			}
			
			@Override
			public boolean isVisible() {
				return true;
			}
			
			@Override
			public boolean isEnabled() {
				return controllable && endable;
			}
			
			@Override
			public void onClick() {
				rolled = false;
				controllable = false;
				nextPiece();
				endable = false;
				openedCard = false;
				if (useAI()) {
					roll.onClick();
				}
			}
			
		});	
		
			buttons.add(purchase = new Button() {
			@Override
			public String getName() {
				return "Purchase";
			}
			
			@Override
			public boolean isVisible() {
				return controllable && places[getCurrentPiece().getPosition()] instanceof Property && !((Property)places[getCurrentPiece().getPosition()]).hasOwner();
			}
			
			@Override
			public boolean isEnabled() {
				Property property = (Property)places[getCurrentPiece().getPosition()];
				return  getCurrentPiece().canAffordProperty(property);
			}
			
			@Override
			public boolean isVisibleForPlayer() {
				return getSelectedPlace() == null;
			}			
			
			@Override
			public void onClick() {
				getCurrentPiece().purchaseProperty((Property)places[getCurrentPiece().getPosition()]);
			}
			
		});
			
			buttons.add(rent = new Button() {
				@Override
				public String getName() {
					return "Pay Rent";
				}
				
				@Override
				public boolean isVisible() {
					if (controllable && places[getCurrentPiece().getPosition()] instanceof Property) {
						Property property = (Property)places[getCurrentPiece().getPosition()];
						return property.hasOwner() && property.getOwner() != getCurrentPiece() && !property.isMortgaged();
					}else{
						return false;
					}
				}
				
				@Override
				public boolean isEnabled() {
					Property property = (Property)places[getCurrentPiece().getPosition()];
					return  !endable && getCurrentPiece().canAffordRent(property);
				}
				
				@Override
				public boolean isVisibleForPlayer() {
					return getSelectedPlace() == null;
				}					
				
				@Override
				public void onClick() {
					getCurrentPiece().payPropertyRent((Property)places[getCurrentPiece().getPosition()]);
					endable = true;
				}
				
			});	
			
			buttons.add(bankrupt = new Button() {
				@Override
				public String getName() {
					return "Go Bankrupt";
				}
				
				@Override
				public boolean isVisible() {
					return !endable && rent.isVisible() && !rent.isEnabled();
				}
				
				@Override
				public boolean isEnabled() {
					return true;
				}
				
				@Override
				public boolean isVisibleForPlayer() {
					return getSelectedPlace() == null;
				}					
				
				@Override
				public void onClick() {
					getCurrentPiece().bankrupt(((Property)places[getCurrentPiece().getPosition()]).getOwner());
					endable = true;
				}
				
			});	
			
			buttons.add(bed = new Button() {
				@Override
				public String getName() {
					return "Buy Bed";
				}
				
				@Override
				public boolean isVisible() {
					return getSelectedPlace() != null && getSelectedPlace() instanceof Street;
				}
				
				@Override
				public boolean isEnabled() {
					Street street = (Street)getSelectedPlace();
					
					return controllable && street.ownsAllInGroup(getCurrentPiece()) && street.getStructureCount() < 5 && getCurrentPiece().canAffordStructure(street) && !street.isMortgaged();
				}
				

				@Override
				public void onClick() {
					getCurrentPiece().buyStructure((Street)getSelectedPlace());
				}
				
			});				
		
		
			buttons.add(card = new Button() {
				@Override
				public String getName() {
					return "Pick a Card";
				}
				
				@Override
				public boolean isVisible() {
					return controllable && places[getCurrentPiece().getPosition()] instanceof CardPlace && (!openedCard || currentCard != null);
				}
				
				@Override
				public boolean isEnabled() {
					return !openedCard;
				}
				
				@Override
				public boolean isVisibleForPlayer() {
					return getSelectedPlace() == null;
				}					
				
				@Override
				public void onClick() {
					openCard(((CardPlace)places[getCurrentPiece().getPosition()]).getCard());
				}
				
			});		
			
			buttons.add(jail = new Button() {
				@Override
				public String getName() {
					return "Pay for /tpx";
				}
				
				@Override
				public boolean isVisible() {
					return controllable && getCurrentPiece().isInJail();
				}
				
				@Override
				public boolean isEnabled() {
					return getCurrentPiece().canAffordFine();
				}
				
				@Override
				public boolean isVisibleForPlayer() {
					return getSelectedPlace() == null;
				}					
				
				@Override
				public void onClick() {
					getCurrentPiece().payFine();
					endable = true;
				}
				
			});		
			
			buttons.add(mortgage = new Button() {
				@Override
				public String getName() {
					return "Mortgage";
				}
				
				@Override
				public boolean isVisible() {
					return controllable && getSelectedPlace() != null && getSelectedPlace() instanceof Property && ((Property)getSelectedPlace()).getOwner() == getCurrentPiece() && !((Property)getSelectedPlace()).isMortgaged();
				}
				
				@Override
				public boolean isEnabled() {
					return ((Property)getSelectedPlace()).canMortgage();
				}			
				
				@Override
				public void onClick() {
					getCurrentPiece().getMoneyFromMortgage((Property)getSelectedPlace());
				}
				
			});	
			
			buttons.add(unmortgage = new Button() {
				@Override
				public String getName() {
					return "Unmortage";
				}
				
				@Override
				public boolean isVisible() {
					return controllable && getSelectedPlace() != null && getSelectedPlace() instanceof Property && ((Property)getSelectedPlace()).getOwner() == getCurrentPiece()  && ((Property)getSelectedPlace()).isMortgaged();
				}
				
				@Override
				public boolean isEnabled() {
					return getCurrentPiece().canAffordUnMortgage((Property)getSelectedPlace());
				}			
				
				@Override
				public void onClick() {
					getCurrentPiece().payUnMortgage((Property)getSelectedPlace());
				}
				
			});	
			
			buttons.add(sellbed = new Button() {
				@Override
				public String getName() {
					return "Sell Bed";
				}
				
				@Override
				public boolean isVisible() {
					return getSelectedPlace() != null && getSelectedPlace() instanceof Street;
				}
				
				@Override
				public boolean isEnabled() {
					Street street = (Street)getSelectedPlace();
					
					return controllable && street.getStructureCount() > 0;
				}
				

				@Override
				public void onClick() {
					getCurrentPiece().sellStructure((Street)getSelectedPlace());
				}
				
			});				
			
		if (getCurrentPiece().getController() == CONTROLLED_BY.COMPUTER) {
			roll.onClick();
		}
	}
	
	private boolean useAI() {
		return getCurrentPiece().getController() == CONTROLLED_BY.COMPUTER;
	}
	
	private void nextPiece() {
		currentPiece = (currentPiece + 1) % pieces.size();
		if (getCurrentPiece().isBankrupt()) {
			nextPiece();
		}
	}

	
	private void throwDice() {
		if (diceCount == 0) {
			if (diceTimer == 0) {
				diceTimer = 20;
			}
			die.randomize();
			die2.randomize();
		}
	}
	
	public void movePiece(int steps) {
		diceCount = steps;
	}
	
	public int getTotalDieEyes() {
		return die.getNumber() + die2.getNumber();		
	}
	
	public boolean hasDoubleDice() {
		return die.getNumber() == die2.getNumber();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void update() {
		super.update();
		
		if (diceDelay == 0) {
			if (diceTimer > 0) {
				throwDice();
				if (--diceTimer == 0) {
					if (getCurrentPiece().isInJail()) {
						controllable = true;
						if (hasDoubleDice()) {
							getCurrentPiece().releaseFromJail();						
							endable = true;
							
							if (useAI()) {
								end.onClick();
							}
						}else{
							getCurrentPiece().increaseTurnsInJail();
							if (getCurrentPiece().getTurnsInJail() < 3) {
								endable = true;
								if (useAI()) {
									end.onClick();									
								}
							}else if (useAI()) {
								if (jail.isVisible() && jail.isEnabled()) {
									jail.onClick();
								}else{
									//TODO: Allow the bot to sell things to avoid going bankrupt
									bankrupt.onClick();
								}
								end.onClick();
							}							
						}
					}else{
						movePiece(getTotalDieEyes());
					}
				}			
			}else if(diceCount != 0) {
				if (diceCount > 0) {
					getCurrentPiece().move(1);
					places[getCurrentPiece().getPosition()].onPiecePass(getCurrentPiece());
					--diceCount;
				}else{
					getCurrentPiece().move(-1);
					++diceCount;
				}
				
				if (diceCount == 0) {
					if (places[getCurrentPiece().getPosition()].onPieceStop(getCurrentPiece())) {
						endable = true;		
					}
					controllable = true;
					if (useAI()) {						
						if (purchase.isVisible() && purchase.isEnabled()) {
							purchase.onClick();
						}else if(card.isVisible() && card.isEnabled()) {
							card.onClick();
						}else if(rent.isVisible()) {
							if (rent.isEnabled()) {
								rent.onClick();
							}else{
								//TODO: Allow the bot to sell things to avoid going bankrupt
								bankrupt.onClick();
							}
						}
						if (end.isVisible() && end.isEnabled()) {
							end.onClick();
						}
					}					
				}
			}
			diceDelay = 3;
		}else{
			diceDelay--;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		loadTexture(gui, 1);
		die.draw(gui, 20, 20);
		die2.draw(gui, 50, 20);
		
		float smallgridX = x / SCALE - ((VehicleBase.MODULAR_SPACE_WIDTH / SCALE) - (PLACE_HEIGHT * 2 + (BOARD_WIDTH - 1) * PLACE_WIDTH)) / 2;
		float smallgridY = y / SCALE - ((VehicleBase.MODULAR_SPACE_HEIGHT / SCALE) - (PLACE_HEIGHT * 2 + (BOARD_HEIGHT - 1) * PLACE_WIDTH)) / 2;

		boolean foundHover = false;
		
		if (selectedPlace != -1) {
			drawPropertyOnBoardWithPositionRotationAndScale(gui, places[selectedPlace], selectedPlace, true, false, (int)((VehicleBase.MODULAR_SPACE_WIDTH / 0.75 - ((getId(selectedPlace) == 0) ? PLACE_HEIGHT : PLACE_WIDTH)) / 2),  (int)((VehicleBase.MODULAR_SPACE_HEIGHT / 0.75 - PLACE_HEIGHT) / 2 ), 0, 0.75F);
		} 
		
		for (int i = 0; i < places.length; i++) {

			if (!foundHover && getModule().inRect((int)smallgridX, (int)smallgridY, getSmallgridPlaceArea(i))) {
				if (selectedPlace == -1) {
					drawPropertyOnBoardWithPositionRotationAndScale(gui, places[i], i, true, false, (int)((VehicleBase.MODULAR_SPACE_WIDTH / 0.75 - ((getId(i) == 0) ? PLACE_HEIGHT : PLACE_WIDTH)) / 2),  (int)((VehicleBase.MODULAR_SPACE_HEIGHT / 0.75 - PLACE_HEIGHT) / 2 ), 0, 0.75F);
				}
				
				foundHover = true;
				
				drawPropertyOnBoard(gui, places[i], i, getSide(i), getId(i), true);
			}else{
				drawPropertyOnBoard(gui, places[i], i, getSide(i), getId(i), false);
			}
		}
		

		
		for (int i =  0; i < pieces.size() ; i++) {
			Piece piece = pieces.get(i);
			loadTexture(gui, 1);
			
			int[] menu = piece.getMenuRect(i);
			
			getModule().drawImage(gui, menu, 0, PLACE_HEIGHT);

			for (int j = 0; j < 3 ; j++) {
				int v = 0;
				
				switch (j) {
					case 0:
						v = piece.getController() == Piece.CONTROLLED_BY.PLAYER ? 0 : piece.getController() == Piece.CONTROLLED_BY.COMPUTER ? 1 : 2;
						break;
					case 1:
						v = pieces.get(i).isBankrupt() ? 4 : currentPiece == i ? diceCount == 0 ? diceTimer > 0 ? 3 : 2 : 1 : 0;
						break;
					case 2:
						v = getSelectedPlace() != null && getSelectedPlace() instanceof Property && ((Property)getSelectedPlace()).getOwner() == pieces.get(i) ? ((Property)getSelectedPlace()).isMortgaged() ? 2 : 1 : 0;
						break;
				}
				
				getModule().drawImage(gui, menu[0] + 3, menu[1] + 3 + j * 9, j * 12, PLACE_HEIGHT + 30 + 6 * v, 12, 6);
			}
		
			int[] player = piece.getPlayerMenuRect(i);
			getModule().drawImage(gui, player, 256 - 24, 24 * piece.getV());
			
			Note.drawPlayerValue(this, gui, menu[0] + 50, menu[1] + 2, piece.getNoteCount());	
			
			for (int j = piece.getAnimationNotes().size() - 1; j >= 0; j--) {
				NoteAnimation animation = piece.getAnimationNotes().get(j);
				int animX = menu[0] + 50 + (6- animation.getNote().getId()) * 20;
				
				if (animX + 16 > VehicleBase.MODULAR_SPACE_WIDTH) {
					animX = player[0] + (player[2] - 16) / 2;
				}
				
				if (animation.draw(this, gui, animX , menu[1] + 2)) {
					piece.removeNewNoteAnimation(j);
				}
			}
					
			
			piece.updateExtending(getModule().inRect(x, y, menu));
		}
		
		loadTexture(gui, 1);
		int id = 0;
		for (Button button : buttons) {
			if (button.isReallyVisible(this)) {
				int[] rect = getButtonRect(id++);
				
				int v = 0;
				if (!button.isReallyEnabled(this)) {
					v = 1;
				}else if(getModule().inRect(x, y, rect)) {
					v = 2;
				}
				
				getModule().drawImage(gui, rect, PLACE_WIDTH * 2, v * 18);
			}
		}
		
		
		if (getSelectedPlace() != null) {
			if (getSelectedPlace() instanceof Street) {
				Street street = (Street)getSelectedPlace();
				
				getModule().drawImage(gui, 32, 185, PLACE_WIDTH, 22, 16, 16);
				
				if (street.getOwner() != null && !street.isMortgaged()) {
					if (street.getStructureCount() == 0) {
						getModule().drawImage(gui, 7, street.ownsAllInGroup(street.getOwner()) ? 241: 226, PLACE_WIDTH + 48, 22, 5, 10);
					}else{
						getModule().drawImage(gui, 323, 172 + (street.getStructureCount() - 1) * 17, PLACE_WIDTH + 48, 22, 5, 10);
					}
				}
				
				
				for (int i = 1; i <= 5; i++) {
					drawStreetRent(gui, street, i);
				}
									
				Note.drawValue(this, gui, 62, 170, 3, street.getMortgageValue());
				Note.drawValue(this, gui, 62, 185, 3, street.getStructureCost());
				
				
				Note.drawValue(this, gui, 62, 222, 3, street.getRentCost(false));
				Note.drawValue(this, gui, 62, 237, 3, street.getRentCost(true));
			}else if(getSelectedPlace() instanceof Station) {
				Station station = (Station)getSelectedPlace(); 
				
				if (station.getOwner() != null && !station.isMortgaged()) {
					getModule().drawImage(gui, 323, 184 + (station.getOwnedInGroup() - 1) * 17, PLACE_WIDTH + 48, 22, 5, 10);
				}
				
				Note.drawValue(this, gui, 62, 170, 3, station.getMortgageValue());
				
				for (int i = 1; i <= 4; i++) {
					drawStationRent(gui, station, i);
				}
			}else if(getSelectedPlace() instanceof Utility) {
				Utility utility = (Utility)getSelectedPlace(); 
							
				if (utility.getOwner() != null && !utility.isMortgaged()) {
					getModule().drawImage(gui, 323, 184 + (utility.getOwnedInGroup() - 1) * 17, PLACE_WIDTH + 48, 22, 5, 10);
				}	
				
				Note.drawValue(this, gui, 62, 170, 3, utility.getMortgageValue());
				
				for (int i = 1; i <= 3; i++) {
					drawUtilityRent(gui, utility, i);
				}
			}
		}
		
		
		if (currentCard != null) {
			cardScale = Math.min(cardScale + 0.02F, 1F);
			cardRotation = Math.max(0, cardRotation - 6);	
			
		
			drawCard(gui, true);
			drawCard(gui, false);		
			
			if (cardScale == 1 && useAI()) {
				removeCard();
			}		
		}

	}
	
	private void openCard(Card card) {
		openedCard = true;
		currentCard = card;
		cardScale = 0;
		cardRotation = 540;
	}
	
	
	private static final int CARD_WIDTH = 142;
	private static final int CARD_HEIGHT = 80;
	
	private void drawCard(GuiVehicle gui, boolean isFront) {
		GL11.glPushMatrix();
		

		
		int x = (VehicleBase.MODULAR_SPACE_WIDTH - CARD_WIDTH) / 2;
		int y = (VehicleBase.MODULAR_SPACE_HEIGHT - CARD_HEIGHT) / 2;
		
		float s = cardScale; 
		
		float posX = gui.getGuiLeft() + CARD_WIDTH / 2; 
		float posY = gui.getGuiTop() + CARD_HEIGHT / 2;		
		GL11.glTranslatef(0F,0F, 100);
		GL11.glTranslatef(posX + x, posY + y, 0);
		GL11.glScalef(s, s, 1F);
		GL11.glRotatef(cardRotation + (isFront ? 0 : 180), 0, 1, 0);
		GL11.glTranslatef(-posX, -posY, 0);
		
		loadTexture(gui, 0);
		int rect[] = new int[] {0, 0, CARD_WIDTH, CARD_HEIGHT};
		currentCard.render(this, gui, rect, isFront);
	
		
		GL11.glPopMatrix();
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
		int id = 0;
		for (Button button : buttons) {
			if (button.isReallyVisible(this)) {
				getModule().drawString(gui, button.getName(), getButtonRect(id++), 0x404040);
			}
		}
		if (getSelectedPlace() != null) {
			if (getSelectedPlace() instanceof Street) {
				getModule().drawString(gui, "Mortgage", 10, 175, 0x404040);
				getModule().drawString(gui, "Buy", 10, 190, 0x404040);
				
				getModule().drawString(gui, "Rents", 10, 215, 0x404040);
				getModule().drawString(gui, "Normal", 14, 227, 0x404040);
				getModule().drawString(gui, "Group", 14, 242, 0x404040);
			}else if(getSelectedPlace() instanceof Station) {
				getModule().drawString(gui, "Mortgage", 10, 175, 0x404040);
				getModule().drawString(gui, "Rents", 330, 170, 0x404040);
			}else if(getSelectedPlace() instanceof Utility) {
				getModule().drawString(gui, "Mortgage", 10, 175, 0x404040);
				getModule().drawSplitString(gui, "The rent depends on the eye count of the dice, if you own one Utility it's " + Utility.getMultiplier(1) + "x the eye count, if you own two it's " + Utility.getMultiplier(2) + "x and if you own them all it's " + Utility.getMultiplier(3) + "x.", 10, 195, 145, 0x404040);
				getModule().drawString(gui, "Rents", 330, 170, 0x404040);
			}
		}
		
	}
	
	private void drawStreetRent(GuiVehicle gui, Street street, int structures) {
		loadTexture(gui, 1);
		
		int graphicalStructures = structures;
		int u = 0;
		if (graphicalStructures == 5) {
			graphicalStructures = 1;
			u = 1;
		}
		
		int yPos = 169 + (structures - 1) * 17;
		
		for (int i = 0; i < graphicalStructures; i++) {
			getModule().drawImage(gui, 330 + i * 6, yPos, PLACE_WIDTH + u * 16, 22, 16, 16);
		}
		
		Note.drawValue(this, gui, 370, yPos, 3, street.getRentCost(structures));
	}
	
	private void drawStationRent(GuiVehicle gui, Station station, int ownedStations) {
		loadTexture(gui, 1);
		
		int yPos = 181 + (ownedStations - 1) * 17;
		
		for (int i = 0; i < ownedStations; i++) {
			getModule().drawImage(gui, 330 + i * 16, yPos, PLACE_WIDTH + i * 16, 70, 16, 16);
		}	
		
		Note.drawValue(this, gui, 410, yPos, 2, station.getRentCost(ownedStations));
	}
	
	private void drawUtilityRent(GuiVehicle gui, Utility utility, int utils) {
		loadTexture(gui, 1);
		
		int yPos = 181 + (utils - 1) * 17;
		
		for (int i = 0; i < utils; i++) {
			getModule().drawImage(gui, 330 + i * 16, yPos, PLACE_WIDTH + i * 16, 86, 16, 16);
		}	
		
		Note.drawValue(this, gui, 400, yPos, 2, utility.getRentCost(utils));
	}	
	
	
	private int[] getButtonRect(int i) {
		return new int[] {10, 50 + i * 22, 80, 18};
	}

	private int getSide(int i) {
		if (i < BOARD_WIDTH) {
			return 0;
		}else if(i < BOARD_WIDTH + BOARD_HEIGHT) {
			return 1;
		}else if(i < BOARD_WIDTH * 2 + BOARD_HEIGHT) {
			return 2;
		}else{
			return 3;
		}	
	}	
	
	private int getId(int i) {
		if (i < BOARD_WIDTH) {
			return i;
		}else if(i < BOARD_WIDTH + BOARD_HEIGHT) {
			return i - BOARD_WIDTH;
		}else if(i < BOARD_WIDTH * 2 + BOARD_HEIGHT) {
			return i - (BOARD_WIDTH + BOARD_HEIGHT);
		}else{
			return i - (BOARD_WIDTH * 2 + BOARD_HEIGHT);
		}
	}
	
	private int[] getSmallgridPlaceArea(int id) {
		int side = getSide(id);
		int i = getId(id);
		
		if (i == 0) {
			switch(side) {
				case 0:
					return new int[] {PLACE_HEIGHT + PLACE_WIDTH * (BOARD_WIDTH - 1), PLACE_HEIGHT + PLACE_WIDTH * (BOARD_HEIGHT - 1), PLACE_HEIGHT, PLACE_HEIGHT};
				case 1:
					return new int[] {0, PLACE_HEIGHT + PLACE_WIDTH * (BOARD_HEIGHT - 1), PLACE_HEIGHT, PLACE_HEIGHT};
				case 2:
					return new int[] {0, 0, PLACE_HEIGHT, PLACE_HEIGHT};
				default:
				case 3:
					return new int[] {PLACE_HEIGHT + PLACE_WIDTH * (BOARD_WIDTH - 1), 0, PLACE_HEIGHT, PLACE_HEIGHT};		
			}
			
		}else{
			--i;
				
			switch (side) {
				case 0:
					return new int[] {PLACE_HEIGHT + ((BOARD_WIDTH - 1) - i) * PLACE_WIDTH - PLACE_WIDTH, PLACE_HEIGHT + (BOARD_HEIGHT - 1) * PLACE_WIDTH, PLACE_WIDTH, PLACE_HEIGHT};		
				case 1:
					return new int[] {0, PLACE_HEIGHT + ((BOARD_HEIGHT - 1)-i) * PLACE_WIDTH  - PLACE_WIDTH, PLACE_HEIGHT, PLACE_WIDTH};
				case 2:
					return new int[] {PLACE_HEIGHT + i * PLACE_WIDTH, 0, PLACE_WIDTH, PLACE_HEIGHT};
				default:
				case 3:
					return new int[] {PLACE_HEIGHT + (BOARD_WIDTH - 1) * PLACE_WIDTH, PLACE_HEIGHT + i * PLACE_WIDTH, PLACE_HEIGHT,  PLACE_WIDTH};
			}
		}	
		
	}
	
	
	private void drawPropertyOnBoard(GuiVehicle gui, Place place, int id, int side, int i, boolean hover) {
		int offX;
		int offY;	
		int rotation;
		
		if (i == 0) {
			switch(side) {
				case 0:
					offX = PLACE_HEIGHT + PLACE_WIDTH * (BOARD_WIDTH - 1);
					offY = PLACE_HEIGHT + PLACE_WIDTH * (BOARD_HEIGHT - 1);
					rotation = 0;
					break;
				case 1:
					offX = PLACE_HEIGHT;
					offY = PLACE_HEIGHT + PLACE_WIDTH * (BOARD_HEIGHT - 1);
					rotation = 90;
					break;
				case 2:
					offX = PLACE_HEIGHT;
					offY = PLACE_HEIGHT;
					rotation = 180;
					break;
				default:
				case 3:
					offX = PLACE_HEIGHT + PLACE_WIDTH * (BOARD_WIDTH - 1);
					offY = PLACE_HEIGHT;
					rotation = 270;
					break;			
			}
			
		}else{
			--i;
				
			switch (side) {
				case 0:
					offX = PLACE_HEIGHT + ((BOARD_WIDTH - 1) - i) * PLACE_WIDTH - PLACE_WIDTH;
					offY = PLACE_HEIGHT + (BOARD_HEIGHT - 1) * PLACE_WIDTH;
					rotation = 0;
					break;			
				case 1:
					offX = PLACE_HEIGHT;
					offY = PLACE_HEIGHT + ((BOARD_HEIGHT - 1)-i) * PLACE_WIDTH  - PLACE_WIDTH;	
					rotation = 90;
					break;
				case 2:
					offX = PLACE_HEIGHT + i * PLACE_WIDTH + PLACE_WIDTH;
					offY = PLACE_HEIGHT;
					rotation = 180;
					break;
				default:
				case 3:
					offX = PLACE_HEIGHT + (BOARD_WIDTH - 1) * PLACE_WIDTH;
					offY = PLACE_HEIGHT + i * PLACE_WIDTH + PLACE_WIDTH;
					rotation = 270;
					break;	
			}
		}
		
		
	
		
		
		
		offX += ((int)(VehicleBase.MODULAR_SPACE_WIDTH / SCALE) - (PLACE_HEIGHT * 2 + (BOARD_WIDTH - 1) * PLACE_WIDTH)) / 2;
		offY += ((int)(VehicleBase.MODULAR_SPACE_HEIGHT / SCALE) - (PLACE_HEIGHT * 2 + (BOARD_HEIGHT - 1) * PLACE_WIDTH)) / 2;
		
		
		drawPropertyOnBoardWithPositionRotationAndScale(gui, place, id, false, hover, offX, offY, rotation, SCALE);

	}
	
	private void drawPropertyOnBoardWithPositionRotationAndScale(GuiVehicle gui, Place place, int id,  boolean zoom, boolean hover, int x, int y, int r, float s) {
		GL11.glPushMatrix();
		
		EnumSet<PLACE_STATE> states = EnumSet.noneOf(PLACE_STATE.class);
		if (zoom) {
			states.add(PLACE_STATE.ZOOMED);
		}else if(hover) {
			states.add(PLACE_STATE.HOVER);
		}
		
		if (selectedPlace == id) {
			states.add(PLACE_STATE.SELECTED);
		}
		
		if (place instanceof Property) {
			Property property = (Property)place;
			
			if (property.hasOwner() && property.getOwner().showProperties()) {
				states.add(PLACE_STATE.MARKED);
			}
		}
		
		
		float posX = gui.getGuiLeft(); 
		float posY = gui.getGuiTop();
		
		GL11.glTranslatef(posX + x * s, posY + y * s, 0);
		GL11.glScalef(s, s, 1F);
		GL11.glRotatef(r, 0, 0, 1);
		GL11.glTranslatef(-posX, -posY, 0);
		place.draw(gui, states);
		int[] total = new int[place.getPieceAreaCount()];
		for (int i = 0; i < pieces.size(); i++) {
			if (!pieces.get(i).isBankrupt() && pieces.get(i).getPosition() == id) {
				total[place.getPieceAreaForPiece(pieces.get(i))]++;
			}
		}		
		
		
		int[] pos = new int[place.getPieceAreaCount()];
		for (int i = 0; i < pieces.size(); i++) {
			if (!pieces.get(i).isBankrupt() && pieces.get(i).getPosition() == id) {
				loadTexture(gui, 1);
				int area = place.getPieceAreaForPiece(pieces.get(i));
				place.drawPiece(gui, pieces.get(i), total[area], pos[area]++, area, states);
			}
		}
		
		place.drawText(gui, states);
		GL11.glPopMatrix();		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int b) {
		float smallgridX = x / SCALE - ((VehicleBase.MODULAR_SPACE_WIDTH / SCALE) - (PLACE_HEIGHT * 2 + (BOARD_WIDTH - 1) * PLACE_WIDTH)) / 2;
		float smallgridY = y / SCALE - ((VehicleBase.MODULAR_SPACE_HEIGHT / SCALE) - (PLACE_HEIGHT * 2 + (BOARD_HEIGHT - 1) * PLACE_WIDTH)) / 2;

		
		for (int i = 0; i < places.length; i++) {
			if (getModule().inRect((int)smallgridX, (int)smallgridY, getSmallgridPlaceArea(i))) {
				if (places[i] instanceof Property) {
					if (i == selectedPlace) {
						selectedPlace = -1;
					}else{
						selectedPlace = i;
					}
					
					return;
				}else{
					break;
				}
			}
		}
		
		int id = 0;
		for (Button button : buttons) {
			if (button.isReallyVisible(this)) {
				if (getModule().inRect(x, y, getButtonRect(id++))) {
					if(button.isReallyEnabled(this)) {
						button.onClick();
					}
					
					return;
				}
			}
		}
		
		if (currentCard != null && cardScale == 1F) {
			int[] rect = new int[] {(VehicleBase.MODULAR_SPACE_WIDTH - CARD_WIDTH) / 2, (VehicleBase.MODULAR_SPACE_HEIGHT - CARD_HEIGHT) / 2, CARD_WIDTH, CARD_HEIGHT};
			if (getModule().inRect(x, y, rect)) {
				removeCard();
			}
		}
		
		selectedPlace = -1;
		
		
	}
	
	private static String[] textures;
	static {
		textures = new String[5];
		for (int i = 0; i < textures.length; i++) {
			textures[i] = "/gui/monopoly_" + i + ".png";
		}
		
	}
	
	

	public void loadTexture(GuiVehicle gui, int number) {
		ResourceHelper.bindResource(textures[number]);
		GL11.glColor4f(1F, 1F, 1F, 1F);	
	}

	public Place[] getPlaces() {
		return places;
	}
	
	private void removeCard() {

		currentCard.doStuff(this, getCurrentPiece());
		currentCard = null;
		endable = true;
		if (diceCount == 0 && useAI()) {
			end.onClick();
		}	
		
	}
	
}
