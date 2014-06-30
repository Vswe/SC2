package vswe.stevesvehicles.module.common.attachment;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;

public class ModuleNote extends ModuleAttachment {

	private static final int MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT = 4;
	private static final int MAXIMUM_NOTES_PER_TRACK_BIT_COUNT = 12;
	private static final int VERY_LONG_TRACK_LIMIT = 1024;
	private static final int NOTES_IN_VIEW = 13;
	private static final int TRACKS_IN_VIEW = 5;

	private int[] instrumentColors = new int[] {0x404040, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0x00FFFF};
	private String[] pitchNames = new String[] {"F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#"};
	private ILocalizedText[] instrumentNames = new ILocalizedText[] {Localization.MODULES.ATTACHMENTS.PIANO, Localization.MODULES.ATTACHMENTS.BASS_DRUM, Localization.MODULES.ATTACHMENTS.SNARE_DRUM, Localization.MODULES.ATTACHMENTS.STICKS, Localization.MODULES.ATTACHMENTS.BASS_GUITAR};

	
	private ArrayList<Track> tracks;
	
	private static final int NOTE_MAP_X = 70;
	private static final int NOTE_MAP_Y = 40;
	private static final int TRACK_HEIGHT = 20;
	
	private ArrayList<Button> buttons;
	private ArrayList<Button> instrumentButtons;
	private int currentInstrument = -1;
	private Button createTrack;
	private Button removeTrack;
	private Button speedButton;
	
	private boolean isScrollingX;
	private boolean isScrollingXTune;
	private boolean isScrollingY;
	
	private int pixelScrollX;
	private int pixelScrollXTune;
	private int generatedScrollX;
	private int pixelScrollY;
	private int generatedScrollY;
	private static final int[] SCROLL_X_RECT = new int[] {NOTE_MAP_X +120, NOTE_MAP_Y - 20,100,16};
	private static final int[] SCROLL_Y_RECT = new int[] {NOTE_MAP_X +220, NOTE_MAP_Y,16,100};
	
	private final int maximumNotesPerTrack;
	private final int maximumTracksPerModule;	

	private int currentTick = 0;
	private int playProgress =0;
	private boolean tooLongTrack = false;
	private boolean tooTallModule = false;	
	private boolean veryLongTrack = false;
	private int speedSetting = 5;
	
	public ModuleNote(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		maximumNotesPerTrack = (int)Math.pow(2, MAXIMUM_NOTES_PER_TRACK_BIT_COUNT) -1;
		maximumTracksPerModule = (int)Math.pow(2, MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT) -1;


		
		tracks = new ArrayList<Track>();		
		if (getVehicle().getWorld().isRemote) {
			buttons = new ArrayList<Button>();
			createTrack = new Button(NOTE_MAP_X -60, NOTE_MAP_Y - 20);
			createTrack.text = Localization.MODULES.ATTACHMENTS.CREATE_TRACK.translate();
			createTrack.imageID = 0;
			removeTrack = new Button(NOTE_MAP_X -40, NOTE_MAP_Y - 20);
			removeTrack.text = Localization.MODULES.ATTACHMENTS.REMOVE_TRACK.translate();
			removeTrack.imageID = 1;
			speedButton = new Button(NOTE_MAP_X -20, NOTE_MAP_Y - 20);
			updateSpeedButton();
			instrumentButtons = new ArrayList<Button>();
			for (int i = 0 ; i < 6; i++) {
				Button tempButton = new Button(NOTE_MAP_X -20 + (i+1)*20, NOTE_MAP_Y - 20);
				instrumentButtons.add(tempButton);
				if (i > 0) {
					tempButton.text = Localization.MODULES.ATTACHMENTS.ACTIVATE_INSTRUMENT.translate(instrumentNames[i-1].translate());
				}else{
					tempButton.text = Localization.MODULES.ATTACHMENTS.DEACTIVATE_INSTRUMENT.translate();
				}
				tempButton.color = instrumentColors[i];
			}
		}
	}

	private void updateSpeedButton() {
		if (getVehicle().getWorld().isRemote) {
			speedButton.imageID = 14 - speedSetting;
			speedButton.text = Localization.MODULES.ATTACHMENTS.NOTE_DELAY.translate(String.valueOf(getTickDelay()));
		}
	}
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
		
		
		for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + TRACKS_IN_VIEW); i++) {
			Track track = tracks.get(i);
			
			for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + NOTES_IN_VIEW); j++) {
				Note note =  track.notes.get(j);
				
				note.drawText(gui, i - getScrollY(), j - getScrollX());
			}
		}
	}

	@Override
	public boolean hasSlots() {
		return false;
	}	

	@Override
	public boolean hasGui() {
		return true;
	}


	@Override
	public void activatedByRail(int x, int y, int z, boolean active) {
		if (active && !isPlaying()) {
			setPlaying(true);
		}
	}		


	
	private int getTickDelay() {
		switch (speedSetting) {
			case 6:
				return 1;
			case 5:
				return 2;
			case 4:
				return 3;
			case 3:
				return 5;
			case 2:
				return 7;
			case 1:
				return 11;
			case 0:
				return 13;
			default:
				return 0;
		}
	}

	
	@Override
	public void update() {
        super.update();

	
		if (getVehicle().getWorld().isRemote) {
						
			tooLongTrack = false;
			veryLongTrack = false;
			for (int i = 0; i < tracks.size(); i++) {
				Track track = tracks.get(i);
				if (track.notes.size() > NOTES_IN_VIEW) {
					tooLongTrack = true;
					if (track.notes.size() > VERY_LONG_TRACK_LIMIT) {
						veryLongTrack = true;
					}
				}		
				int trackPacketID = -1;
				if (track.addButton.down) {
					track.addButton.down = false;
					trackPacketID = 0;
				}else if (track.removeButton.down)  {
					track.removeButton.down = false;
					trackPacketID = 1;
				}else if(track.volumeButton.down) {
					track.volumeButton.down = false;
					trackPacketID = 2;		
				}
				if (trackPacketID != -1) {
					byte info = (byte)(i | (trackPacketID << MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT));
					sendPacket(1, info);		
				}
			
			}
			
			if (!tooLongTrack) {
				pixelScrollX = 0;
				isScrollingX = false;
			}
			if (!veryLongTrack) {
				pixelScrollXTune = 0;
				isScrollingXTune = false;				
			}
			tooTallModule = tracks.size() > TRACKS_IN_VIEW;
			if (!tooTallModule) {
				pixelScrollY = 0;
				isScrollingY = false;			
			}
			
			
			generateScrollX();
			generateScrollY();
			
			if (createTrack.down) {
				createTrack.down = false;
				sendPacket(0, (byte)0);
			}
			if (removeTrack.down) {
				removeTrack.down = false;
				sendPacket(0, (byte)1);
			}
			if (speedButton.down) {
				speedButton.down = false;
				sendPacket(0, (byte)2);
			}			
			
			for (int i = 0; i < instrumentButtons.size(); i++) {
				if (instrumentButtons.get(i).down && i != currentInstrument) {
					currentInstrument = i;
					break;
				}
			}
			for (int i = 0; i < instrumentButtons.size(); i++) {
				if (instrumentButtons.get(i).down && i != currentInstrument) {
					instrumentButtons.get(i).down = false;
				}
			}			
			if (currentInstrument != -1 && !instrumentButtons.get(currentInstrument).down) {
				currentInstrument = -1;
			}
		}
		
		if (isPlaying()){
			if (currentTick <= 0) {
			
				boolean found = false;
				for (Track track : tracks) {
					if (track.notes.size() > playProgress) {
						Note note = track.notes.get(playProgress);
						float volume;
						switch(track.volume) {
							case 0:
								volume = 0F;
								break;
							case 1:
								volume = 0.33F;
								break;
							case 2:
								volume = 0.67F;
								break;
							default:
								volume = 1F;						
						}
						note.play(volume);
						found = true;
					}
				}
				if (!found) {
					if (!getVehicle().getWorld().isRemote) {
						setPlaying(false);
					}
					
					playProgress = 0;
				}else {
					playProgress++;
				}
				currentTick = getTickDelay() - 1;
			}else{
				currentTick--;
			}
			
			
		}

    }


	
	@Override
	public int guiWidth() {
		return 310;
	}
	@Override
	public int guiHeight() {
		return 150;
	}


	private class TrackButton extends Button {
		private int trackID ;
		private int x;
		public TrackButton(int x, int trackID) {
			super(0,0);
			this.trackID = trackID;
			this.x = x;
		}
		
		public int[] getRect() {
			return new int[] {x, NOTE_MAP_Y + (trackID-getScrollY()) * TRACK_HEIGHT, 16,16};
		}	

		private boolean isValid() {
			return getScrollY() <= trackID && trackID < getScrollY() + TRACKS_IN_VIEW;
		}
		
		public void draw(GuiVehicle gui, int x, int y) {
			if (isValid()) {
				super.draw(gui, x, y);
			}
		}	

		public void overlay(GuiVehicle gui, int x, int y) {
			if (isValid()) {
				super.overlay(gui, x, y);
			}
		}
		
		public void clicked(int x, int y) {
			if (isValid()) {
				super.clicked(x, y);
			}
		}
		
	}
	
	private class Button {
		public int [] rect;
		public boolean down;
		public String text;
		public int color;
		public int imageID;
		
		public Button(int x, int y) {
			down = false;
			this.rect = new int[] {x,y, 16,16};
			color = 0x000000;
			imageID = -1;
			buttons.add(this);
		}
		
		public int[] getRect() {
			return rect;
		}
		
		public void overlay(GuiVehicle gui, int x, int y) {
			drawStringOnMouseOver(gui, text,x, y, getRect());
		}
		
		public void clicked(int x, int y) {
			if (inRect(x,y, getRect())) {
				down = !down;
			}	
		}
		
		public void draw(GuiVehicle gui, int x, int y) {
			if (!inRect(x,y, getRect())) {
				GL11.glColor4f((float)(color >> 16) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, 1F);
			}
			drawImage(gui, getRect(), 32, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int srcX = 0;
			int srcY = 16;
			if (down) {
				srcX += 16;
			}
			drawImage(gui, getRect(), srcX, srcY);
			
			if (imageID != -1) {
				drawImage(gui, getRect(), imageID*16, 32);				
			}
		}
	
	}
		
	private class Note {
		public int instrumentId;
		public int pitch;

		
		public Note(Track track) {
			track.notes.add(this);
		}
		
		public void drawText(GuiVehicle gui, int trackID, int noteID) {
			if (instrumentId == 0) {
				return;
			}
		
			int rect[] = getBounds(trackID, noteID);
		
			String str = String.valueOf(pitch);
			if (str.length() < 2) {
				str = "0" + str;
			}
		
			
			drawString(gui, str, rect[0] + 3, rect[1] +6, instrumentColors[instrumentId]);
		}
		
		public void draw(GuiVehicle gui, int x, int y, int trackID, int noteID) {
			int srcX = 0;
			if (instrumentId == 0) {
				srcX += 16;
			}
			
			int rect[] = getBounds(trackID, noteID);
			if (instrumentId != 0 && playProgress == noteID + getScrollX() && isPlaying()) {
				GL11.glColor4f(0.3F, 0.3F, 0.3F, 1.0F);
			}
			drawImage(gui, rect, srcX,0);	
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			if (inRect(x,y, rect)) {
				drawImage(gui, rect, 32,0);	
			}			
		}
		
		public int[] getBounds(int trackID, int noteID) {
			return new int[] {NOTE_MAP_X + noteID * 16, NOTE_MAP_Y + trackID * TRACK_HEIGHT, 16,16};
		}
		
		public short getInfo() {						
			short info = (short)0;
			info |= instrumentId;
			info |= pitch << 3;		

			return info;
		}
		
		public void setInfo(short val) {
			instrumentId = val & 7;
			pitch = (val & 248) >> 3;
		}
		
		
		public void play(float volume) {
			if (instrumentId == 0) {
				return;
			}
		
			if (!getVehicle().getWorld().isRemote) {
				if (volume > 0) {
					float calculatedPitch = (float)Math.pow(2.0D, (double)(pitch - 12) / 12.0D);
					String instrumentString = "harp";
					
					if (instrumentId == 2)
					{
						instrumentString = "bd";
					}else if (instrumentId == 3)
					{
						instrumentString = "snare";
					}else if (instrumentId == 4)
					{
						instrumentString = "hat";
					}else if (instrumentId == 5)
					{
						instrumentString = "bassattack";
					}

					getVehicle().getWorld().playSoundEffect((double) getVehicle().x() + 0.5D, (double) getVehicle().y() + 0.5D, (double) getVehicle().z() + 0.5D, "note." + instrumentString, volume, calculatedPitch);
				}
			}else{
				double offsetX = 0.0;
				double offsetZ = 0.0;
			
				if (getVehicle().getEntity().motionX != 0) {
					offsetX =  (getVehicle().getEntity().motionX > 0 ? -1 : 1);
				}

				if (getVehicle().getEntity().motionZ != 0) {
					offsetZ =  (getVehicle().getEntity().motionZ > 0 ? -1 : 1);
				}
			
				getVehicle().getWorld().spawnParticle("note", (double) getVehicle().x() + offsetZ * 1 + 0.5D, (double) getVehicle().y() + 1.2D, (double) getVehicle().z() + offsetX * 1 + 0.5D, (double) pitch / 24.0D, 0.0D,0D);
				getVehicle().getWorld().spawnParticle("note", (double) getVehicle().x() + offsetZ * -1 + 0.5D, (double) getVehicle().y() + 1.2D, (double) getVehicle().z() + offsetX * -1 + 0.5D, (double) pitch / 24.0D, 0.0D, 0.0D);
			}
		}
		
		public String toString() {			
			if (instrumentId == 0) {
				return "Unknown instrument";
			}else{
				return instrumentNames[instrumentId-1].translate() + " " + pitchNames[pitch];
			}
		}
		
		

	}
	
	private class Track {
		public ArrayList<Note> notes;
		public Button addButton;
		public Button removeButton;
		public Button volumeButton;
		public int volume;


		public Track() {
			notes = new ArrayList<Note>();
			volume = 3;
			if (getVehicle().getWorld().isRemote) {
				int ID = (tracks.size() + 1);
				addButton = new TrackButton(NOTE_MAP_X - 60, ID - 1);
				addButton.text = Localization.MODULES.ATTACHMENTS.ADD_NOTE.translate(String.valueOf(ID));
				addButton.imageID = 2;
				removeButton = new TrackButton(NOTE_MAP_X - 40, ID - 1);
				removeButton.text = Localization.MODULES.ATTACHMENTS.REMOVE_NOTE.translate(String.valueOf(ID));
				removeButton.imageID = 3;
				volumeButton = new TrackButton(NOTE_MAP_X - 20, ID - 1);
				volumeButton.text = getVolumeText();
				volumeButton.imageID = 4;				
			}
			tracks.add(this);			
		}

		
		private String getVolumeText() {
            return Localization.MODULES.ATTACHMENTS.VOLUME.translate(String.valueOf(volume));
		}
		
		public void unload() {
			buttons.remove(addButton);
			buttons.remove(removeButton);
			buttons.remove(volumeButton);
		}
		
		public short getInfo() {						
			short info = (short)0;
			info |= notes.size();
			info |= volume << MAXIMUM_NOTES_PER_TRACK_BIT_COUNT;
			return info;
		}
		
		public void setInfo(short val) {
			int numberOfNotes = val & maximumNotesPerTrack;
			while (notes.size() < numberOfNotes) {
				new Note(this);
			}
			while (notes.size() > numberOfNotes) {
				notes.remove(notes.size()-1);
			}		

			volume = (val & (~maximumNotesPerTrack)) >> MAXIMUM_NOTES_PER_TRACK_BIT_COUNT;
			if (getVehicle().getWorld().isRemote) {
				volumeButton.imageID = 4 + volume;	
				volumeButton.text = getVolumeText();
			}
		}		
		
	}
	

	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/note.png");


		for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + TRACKS_IN_VIEW); i++) {
			Track track = tracks.get(i);
			
			for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + NOTES_IN_VIEW); j++) {
				Note note =  track.notes.get(j);
				
				note.draw(gui, x, y, i - getScrollY(), j - getScrollX());
			}
		}
				
		for (Button button : buttons) {
			button.draw(gui,x,y);
		}
		
		if (tooLongTrack) {
			drawImage(gui, SCROLL_X_RECT, 48, 0);
			int[] marker = getMarkerX();
			drawImage(gui, marker, 148, 1);
			if (veryLongTrack) {
				marker = getMarkerXTune();
				drawImage(gui, marker, 153, 1);			
			}
		}else{
			drawImage(gui, SCROLL_X_RECT, 48, 16);
		}
		if (tooTallModule) {
			drawImage(gui, SCROLL_Y_RECT, 0, 48);
			int[] marker = getMarkerY();
			drawImage(gui, marker, 1, 148);
		}else{
			drawImage(gui, SCROLL_Y_RECT, 16, 48);
		}		
		
	}
	

	private int[] getMarkerX() {
		return generateMarkerX(pixelScrollX);
	}
	private int[] getMarkerXTune() {
		return generateMarkerX(pixelScrollXTune);
	}	
	private int[] generateMarkerX(int x) {
		return new int[] {SCROLL_X_RECT[0] + x, SCROLL_X_RECT[1] + 1 , 5, 14};
	}	
	
	private void setMarkerX(int x) {
		pixelScrollX = generateNewMarkerX(x);
	}
	private void setMarkerXTune(int x) {
		pixelScrollXTune = generateNewMarkerX(x);
	}	
	
	private int generateNewMarkerX(int x) {
		int temp = x - SCROLL_X_RECT[0];
		if (temp < 0) {
			temp = 0;
		}else if(temp > (SCROLL_X_RECT[2] - 5)) {
			temp = (SCROLL_X_RECT[2] - 5);
		}	
		return temp;
	}
	

	private int getScrollX() {
		return generatedScrollX;
	}
	private void generateScrollX() {
		if (tooLongTrack) {
			int maxNotes = -1;
            for (Track track : tracks) {
                maxNotes = Math.max(maxNotes, track.notes.size());
            }
			maxNotes -= NOTES_IN_VIEW;

			float widthOfBlockInScrollArea = (SCROLL_X_RECT[2] - 5) / (float)maxNotes;
			generatedScrollX = Math.round(pixelScrollX / widthOfBlockInScrollArea);
			if (veryLongTrack) {
				generatedScrollX += (pixelScrollXTune / (float)(SCROLL_X_RECT[2] - 5)) * 50;
			}
		}else{
			generatedScrollX = 0;
		}
	}

	
	private int[] getMarkerY() {
		return new int[] {SCROLL_Y_RECT[0] + 1, SCROLL_Y_RECT[1] + pixelScrollY ,14, 5};
	}
	
	private void setMarkerY(int y) {
		pixelScrollY = y - SCROLL_Y_RECT[1];
		if (pixelScrollY < 0) {
			pixelScrollY = 0;
		}else if(pixelScrollY > (SCROLL_Y_RECT[3] - 5)) {
			pixelScrollY = (SCROLL_Y_RECT[3] - 5);
		}	
	}
	


	private int getScrollY() {
		return generatedScrollY;
	}
	private void generateScrollY() {
		if (tooTallModule) {
			int maxTracks = tracks.size() - TRACKS_IN_VIEW;

			float heightOfBlockInScrollArea = (SCROLL_Y_RECT[3] - 5) / maxTracks;
			generatedScrollY = Math.round(pixelScrollY / heightOfBlockInScrollArea);
		}else{
			generatedScrollY = 0;
		}
	}	
	
	
	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + TRACKS_IN_VIEW); i++) {
			Track track = tracks.get(i);
			
			for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + NOTES_IN_VIEW); j++) {
				Note note =  track.notes.get(j);
				
				if (note.instrumentId != 0) {
					drawStringOnMouseOver(gui, note.toString(),x, y, note.getBounds(i-getScrollY(),j-getScrollX()));
				}
			}
		}	
		
		for (Button button : buttons) {
			if (button.text != null && button.text.length() > 0) {
				button.overlay(gui, x,y);
			}
		}		
		
	}	
	
	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if (isScrollingX) {
			setMarkerX(x);
				
			if (button != -1)
			{
				isScrollingX = false;
			}	
		}
		if (isScrollingXTune) {
			setMarkerXTune(x);
				
			if (button != -1)
			{
				isScrollingXTune = false;
			}	
		}		
		if (isScrollingY) {
			setMarkerY(y + getVehicle().getRealScrollY());
				
			if (button != -1)
			{
				isScrollingY = false;
			}	
		}			
	}	
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int buttonId) {
		if (buttonId == 0) {
			for (Button button : buttons) {
				button.clicked(x,y);
			}
			
			if (!isScrollingX && inRect(x,y, SCROLL_X_RECT)) {
				isScrollingX = true;
			}else if (!isScrollingY && inRect(x,y, SCROLL_Y_RECT)) {
				isScrollingY = true;
			}
		}else if ( buttonId == 1) {
			if (!isScrollingXTune && inRect(x,y, SCROLL_X_RECT)) {
				isScrollingXTune = true;
			}		
		}
		
		if (buttonId == 0 || buttonId == 1) {
			for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + TRACKS_IN_VIEW); i++) {
				Track track = tracks.get(i);
				
				for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + NOTES_IN_VIEW); j++) {
					Note note =  track.notes.get(j);
					if (inRect(x,y, note.getBounds(i-getScrollY(),j-getScrollX()))) {
						int instrumentInfo = currentInstrument;
						if (instrumentInfo == -1) {
							if (buttonId == 0) {
								instrumentInfo = 6;
							}else{
								instrumentInfo = 7;
							}
						}
						if (currentInstrument != -1 || note.instrumentId != 0) {
							byte info = (byte)i;
							info |= instrumentInfo << MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT;
							sendPacket(2, new byte[] {info,(byte)j});
						}
					}
				}
			}			
		}
	}



 	@Override
	public int numberOfGuiData() {
		return 1+(maximumNotesPerTrack+1)*maximumTracksPerModule;
	}

	@Override
	protected void checkGuiData(Object info[]) {
		short moduleHeader = (short)tracks.size();
		moduleHeader |= (short)(speedSetting << MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT);
		updateGuiData(info, 0, moduleHeader);	

		
		for (int i = 0; i < tracks.size();i++) {
			Track track = tracks.get(i);
			updateGuiData(info, 1+(maximumNotesPerTrack+1)*i,  track.getInfo());

			
			for (int j = 0; j < track.notes.size();j++) {
				Note note = track.notes.get(j);
				updateGuiData(info, 1+(maximumNotesPerTrack+1)*i+1+j, note.getInfo());
			}
		}			
	
	}
	


	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			int trackCount = data & maximumTracksPerModule;			
			speedSetting = (data & ~maximumTracksPerModule) >> MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT;
			updateSpeedButton();
			while (tracks.size() < trackCount) {
				new Track();
			}
			while (tracks.size() > trackCount) {
				tracks.get(tracks.size()-1).unload();
				tracks.remove(tracks.size()-1);
			}			
		}else{
			id--;
			int trackId = id / (maximumNotesPerTrack+1);
			int noteId = id % (maximumNotesPerTrack+1);
			Track track = tracks.get(trackId);
			if (noteId == 0) {
				track.setInfo(data);
			}else{
				noteId--;
				
				Note note = track.notes.get(noteId);
				note.setInfo(data);
			}
		
		}
	}

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0,0);
	}	
	
	private boolean isPlaying() {
        return !isPlaceholder() && (getDw(0) != 0 || playProgress > 0);
	}
	
	private void setPlaying(boolean val) {
		updateDw(0, val ? 1 : 0);
	}
	

	@Override
	public int numberOfPackets() {
		return 3;
	}
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			if (data[0] == 0) {
				if (tracks.size() < maximumTracksPerModule) {
					new Track();
				}
			}else if(data[0] == 1) {
				if (tracks.size() > 0) {
					tracks.remove(tracks.size() -1);
				}
			}else if(data[0] == 2) {
				speedSetting++;
				if (speedSetting >= 7) {
					speedSetting = 0;
				}
			}
		}else if(id == 1) {
			int trackID = data[0] & maximumTracksPerModule;
			int trackPacketID = ((data[0] & ~maximumTracksPerModule) >> MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT);
			if (trackID < tracks.size()) {
				Track track = tracks.get(trackID);
				
				if (trackPacketID == 0) {
					if (track.notes.size() < maximumNotesPerTrack) {
						new Note(track);
					}
				}else if(trackPacketID == 1){
					if (track.notes.size() > 0) {
						track.notes.remove(track.notes.size()-1);
					}				
				}else if (trackPacketID == 2) {
					track.volume = (track.volume + 1) % 4;
				}
			}
		}else if(id == 2) {
			byte info = data[0];
			byte noteID = data[1];
				
			byte trackID = (byte)(info & maximumTracksPerModule);
			byte instrumentInfo = (byte)(((byte)(info & ~(byte)maximumTracksPerModule)) >> (byte) MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT);
			
			if (trackID < tracks.size()) {
				Track track = tracks.get(trackID);
				if (noteID < track.notes.size()) {
					Note note = track.notes.get(noteID);
					
					if (instrumentInfo < 6) {
						note.instrumentId = instrumentInfo;
					}else if (instrumentInfo == 6){
						note.pitch+=1;
						if (note.pitch > 24) {
							note.pitch = 0;
						}
					}else{
						note.pitch-=1;
						if (note.pitch < 0) {
							note.pitch = 24;
						}					
					}
				}
			}			
		}
	}


	@Override
	protected void save(NBTTagCompound tagCompound) {
		short headerInfo = (short)tracks.size();
		headerInfo |= (short)(speedSetting << MAXIMUM_TRACKS_PER_MODULE_BIT_COUNT);

		tagCompound.setShort("Header", headerInfo);
		
		for (int i = 0; i < tracks.size();i++) {
			Track track = tracks.get(i);
			tagCompound.setShort("Track" + i, track.getInfo());
			
			for (int j = 0; j < track.notes.size();j++) {
				Note note = track.notes.get(j);
				tagCompound.setShort("Note" + i + ":" + j, note.getInfo());
			}
		}	
	
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		short headerInfo = tagCompound.getShort("Header");
		receiveGuiData(0, headerInfo);
		for (int i = 0; i < tracks.size(); i++) {
			short trackInfo = tagCompound.getShort("Track" + i);
			receiveGuiData(1+(maximumNotesPerTrack+1)*i, trackInfo);
			Track track = tracks.get(i);
			for (int j = 0; j < track.notes.size(); j++) {
				short noteInfo = tagCompound.getShort("Note" + i + ":" + j);
				
				receiveGuiData(1+(maximumNotesPerTrack+1)*i+1+j, noteInfo);
			}
		}
		
	}		

}