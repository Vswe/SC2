package vswe.stevescarts.old.Modules.Realtimers;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.modules.ModuleBase;

public class ModuleNote extends ModuleBase {

	private final int maximumTracksPerModuleBitCount = 4;
	private final int maximumNotesPerTrackBitCount = 12;
	private int veryLongTrackLimit = 1024;
	private int notesInView = 13;
	private int tracksInView = 5;

	private int[] instrumentColors = new int[] {0x404040, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0x00FFFF};
	private String[] pitchNames = new String[] {"F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#"};
	private Localization.MODULES.ATTACHMENTS [] instrumentNames = new Localization.MODULES.ATTACHMENTS[] {Localization.MODULES.ATTACHMENTS.PIANO, Localization.MODULES.ATTACHMENTS.BASS_DRUM, Localization.MODULES.ATTACHMENTS.SNARE_DRUM, Localization.MODULES.ATTACHMENTS.STICKS, Localization.MODULES.ATTACHMENTS.BASS_GUITAR};

	
	private ArrayList<Track> tracks;
	
	private int notemapX = 70;
	private int notemapY = 40;
	private int trackHeight = 20;
	
	private ArrayList<Button> buttons;
	private ArrayList<Button> instrumentbuttons;
	private int currentInstrument = -1;
	private Button createTrack;
	private Button removeTrack;
	private Button speedButton;
	
	private boolean isScrollingX;
	private boolean isScrollingXTune;	
	private int scrollX;
	private boolean isScrollingY;
	private int scrollY;		
	
	private int pixelScrollX;
	private int pixelScrollXTune;
	private int generatedScrollX;
	private int pixelScrollY;
	private int generatedScrollY;
	private int[] scrollXrect = new int[] {notemapX+120, notemapY - 20,100,16};
	private int[] scrollYrect = new int[] {notemapX+220, notemapY,16,100};	
	
	private final int maximumNotesPerTrack;
	private final int maximumTracksPerModule;	

	private int currentTick = 0;
	private int playProgress =0;
	private boolean tooLongTrack = false;
	private boolean tooTallModule = false;	
	private boolean veryLongTrack = false;
	private int speedSetting = 5;
	
	public ModuleNote(EntityModularCart cart) {
		super(cart);
		
		maximumNotesPerTrack = (int)Math.pow(2,maximumNotesPerTrackBitCount) -1;
		maximumTracksPerModule = (int)Math.pow(2,maximumTracksPerModuleBitCount) -1;


		
		tracks = new ArrayList<Track>();		
		if (getCart().worldObj.isRemote) {
			buttons = new ArrayList<Button>();
			createTrack = new Button(notemapX-60, notemapY - 20);
			createTrack.text = Localization.MODULES.ATTACHMENTS.CREATE_TRACK.translate();
			createTrack.imageID = 0;
			removeTrack = new Button(notemapX-40, notemapY - 20);
			removeTrack.text = Localization.MODULES.ATTACHMENTS.REMOVE_TRACK.translate();
			removeTrack.imageID = 1;
			speedButton = new Button(notemapX-20, notemapY - 20);
			updateSpeedButton();
			instrumentbuttons = new ArrayList<Button>();
			for (int i = 0 ; i < 6; i++) {
				Button tempButton = new Button(notemapX-20 + (i+1)*20, notemapY - 20);
				instrumentbuttons.add(tempButton);
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
		if (getCart().worldObj.isRemote) {
			speedButton.imageID = 14 - speedSetting;
			speedButton.text = Localization.MODULES.ATTACHMENTS.NOTE_DELAY.translate(String.valueOf(getTickDelay()));
		}
	}
	
	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
		
		
		for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + tracksInView); i++) {
			Track track = tracks.get(i);
			
			for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + notesInView); j++) {
				Note note =  track.notes.get(j);
				
				note.drawText(gui, i-getScrollY(), j-getScrollX());
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
	public void update()
    {
        super.update();

	
		if (getCart().worldObj.isRemote) {
						
			tooLongTrack = false;
			veryLongTrack = false;
			for (int i = 0; i < tracks.size(); i++) {
				Track track = tracks.get(i);
				if (track.notes.size() > notesInView) {
					tooLongTrack = true;
					if (track.notes.size() > veryLongTrackLimit) {
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
					byte info = (byte)(i | (trackPacketID << maximumTracksPerModuleBitCount));
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
			tooTallModule = tracks.size() > tracksInView;
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
			
			for (int i = 0; i < instrumentbuttons.size(); i++) {
				if (instrumentbuttons.get(i).down && i != currentInstrument) {
					currentInstrument = i;
					break;
				}
			}
			for (int i = 0; i < instrumentbuttons.size(); i++) {
				if (instrumentbuttons.get(i).down && i != currentInstrument) {
					instrumentbuttons.get(i).down = false;
				}
			}			
			if (currentInstrument != -1 && !instrumentbuttons.get(currentInstrument).down) {
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
					if (!getCart().worldObj.isRemote) {
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
			return new int[] {x,notemapY + (trackID-getScrollY()) * trackHeight, 16,16};
		}	

		private boolean isValid() {
			return getScrollY() <= trackID && trackID < getScrollY() + tracksInView;
		}
		
		public void draw(GuiMinecart gui, int x, int y) {
			if (isValid()) {
				super.draw(gui, x, y);
			}
		}	

		public void overlay(GuiMinecart gui, int x, int y) {
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
		
		public void overlay(GuiMinecart gui, int x, int y) {
			drawStringOnMouseOver(gui, text,x, y, getRect());
		}
		
		public void clicked(int x, int y) {
			if (inRect(x,y, getRect())) {
				down = !down;
			}	
		}
		
		public void draw(GuiMinecart gui, int x, int y) {
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
		
		public void drawText(GuiMinecart gui, int trackID, int noteID) {
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
		
		public void draw(GuiMinecart gui, int x, int y, int trackID, int noteID) {
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
			return new int[] {notemapX + noteID * 16, notemapY + trackID * trackHeight, 16,16};
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
		
			if (!getCart().worldObj.isRemote) {
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

					getCart().worldObj.playSoundEffect((double)getCart().x() + 0.5D, (double)getCart().y() + 0.5D, (double)getCart().z() + 0.5D, "note." + instrumentString, volume, calculatedPitch);
				}
			}else{
				double oX = 0.0;
				double oZ = 0.0;
			
				if (getCart().motionX != 0)
				{
					oX =  (getCart().motionX > 0 ? -1 : 1);
				}

				if (getCart().motionZ != 0)
				{
					oZ =  (getCart().motionZ > 0 ? -1 : 1);
				}
			
				getCart().worldObj.spawnParticle("note", (double)getCart().x() + oZ * 1 + 0.5D, (double)getCart().y() + 1.2D, (double)getCart().z() + oX * 1 + 0.5D, (double)pitch / 24.0D, 0.0D, 0.0D);			
				getCart().worldObj.spawnParticle("note", (double)getCart().x() + oZ * -1 + 0.5D, (double)getCart().y() + 1.2D, (double)getCart().z() + oX * -1 + 0.5D, (double)pitch / 24.0D, 0.0D, 0.0D);		
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
			if (getCart().worldObj.isRemote) {
				int ID = (tracks.size() + 1);
				addButton = new TrackButton(notemapX - 60, ID - 1);
				addButton.text = Localization.MODULES.ATTACHMENTS.ADD_NOTE.translate(String.valueOf(ID));
				addButton.imageID = 2;
				removeButton = new TrackButton(notemapX - 40, ID - 1);
				removeButton.text = Localization.MODULES.ATTACHMENTS.REMOVE_NOTE.translate(String.valueOf(ID));
				removeButton.imageID = 3;
				volumeButton = new TrackButton(notemapX - 20, ID - 1);
				volumeButton.text = getVolumeText();
				volumeButton.imageID = 4;				
			}
			tracks.add(this);			
		}
				
		public int lastNoteCount;
		
		
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
			info |= volume << maximumNotesPerTrackBitCount;	
			return info;
		}
		
		public void setInfo(short val) {
			int numberofNotes = val & maximumNotesPerTrack;
			while (notes.size() < numberofNotes) {
				new Note(this);
			}
			while (notes.size() > numberofNotes) {
				notes.remove(notes.size()-1);
			}		

			volume = (val & (~maximumNotesPerTrack)) >> maximumNotesPerTrackBitCount;	
			if (getCart().worldObj.isRemote) {
				volumeButton.imageID = 4 + volume;	
				volumeButton.text = getVolumeText();
			}
		}		
		
	}
	

	
	@Override
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/note.png");


		for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + tracksInView); i++) {
			Track track = tracks.get(i);
			
			for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + notesInView); j++) {
				Note note =  track.notes.get(j);
				
				note.draw(gui, x, y, i - getScrollY(), j - getScrollX());
			}
		}
				
		for (Button button : buttons) {
			button.draw(gui,x,y);
		}
		
		if (tooLongTrack) {
			drawImage(gui, scrollXrect, 48, 0);
			int[] marker = getMarkerX();
			drawImage(gui, marker, 148, 1);
			if (veryLongTrack) {
				marker = getMarkerXTune();
				drawImage(gui, marker, 153, 1);			
			}
		}else{
			drawImage(gui, scrollXrect, 48, 16);
		}
		if (tooTallModule) {
			drawImage(gui, scrollYrect, 0, 48);
			int[] marker = getMarkerY();
			drawImage(gui, marker, 1, 148);
		}else{
			drawImage(gui, scrollYrect, 16, 48);
		}		
		
	}
	

	private int[] getMarkerX() {
		return generateMarkerX(pixelScrollX);
	}
	private int[] getMarkerXTune() {
		return generateMarkerX(pixelScrollXTune);
	}	
	private int[] generateMarkerX(int x) {
		return new int[] {scrollXrect[0] + x, scrollXrect[1] + 1 , 5, 14};
	}	
	
	private void setMarkerX(int x) {
		pixelScrollX = generateNewMarkerX(x);
	}
	private void setMarkerXTune(int x) {
		pixelScrollXTune = generateNewMarkerX(x);
	}	
	
	private int generateNewMarkerX(int x) {
		int temp = x - scrollXrect[0];
		if (temp < 0) {
			temp = 0;
		}else if(temp > (scrollXrect[2] - 5)) {
			temp = (scrollXrect[2] - 5);
		}	
		return temp;
	}
	

	private int getScrollX() {
		return generatedScrollX;
	}
	private void generateScrollX() {
		if (tooLongTrack) {
			int maxNotes = -1;
			for(int i = 0; i < tracks.size(); i++) {
				maxNotes = Math.max(maxNotes, tracks.get(i).notes.size());
			}
			maxNotes -= notesInView;

			float widthOfBlockInScrollArea = (scrollXrect[2] - 5) / (float)maxNotes;	
			generatedScrollX = (int)Math.round(pixelScrollX / widthOfBlockInScrollArea);	
			if (veryLongTrack) {
				generatedScrollX += (pixelScrollXTune / (float)(scrollXrect[2] - 5)) * 50;
			}
		}else{
			generatedScrollX = 0;
		}
	}

	
	private int[] getMarkerY() {
		return new int[] {scrollYrect[0] + 1, scrollYrect[1] + pixelScrollY ,14, 5};
	}
	
	private void setMarkerY(int y) {
		pixelScrollY = y - scrollYrect[1];
		if (pixelScrollY < 0) {
			pixelScrollY = 0;
		}else if(pixelScrollY > (scrollYrect[3] - 5)) {
			pixelScrollY = (scrollYrect[3] - 5);
		}	
	}
	


	private int getScrollY() {
		return generatedScrollY;
	}
	private void generateScrollY() {
		if (tooTallModule) {
			int maxTracks = tracks.size() - tracksInView;

			float heightOfBlockInScrollArea = (scrollYrect[3] - 5) / maxTracks;	
			generatedScrollY = (int)Math.round(pixelScrollY / heightOfBlockInScrollArea);	
		}else{
			generatedScrollY = 0;
		}
	}	
	
	
	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + tracksInView); i++) {
			Track track = tracks.get(i);
			
			for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + notesInView); j++) {
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
	public void mouseMovedOrUp(GuiMinecart gui,int x, int y, int button) {
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
			setMarkerY(y+getCart().getRealScrollY());
				
			if (button != -1)
			{
				isScrollingY = false;
			}	
		}			
	}	
	
	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int buttonId) {	
		if (buttonId == 0) {
			for (Button button : buttons) {
				button.clicked(x,y);
			}
			
			if (!isScrollingX && inRect(x,y, scrollXrect)) {
				isScrollingX = true;
			}else if (!isScrollingY && inRect(x,y, scrollYrect)) {
				isScrollingY = true;
			}
		}else if ( buttonId == 1) {
			if (!isScrollingXTune && inRect(x,y, scrollXrect)) {
				isScrollingXTune = true;
			}		
		}
		
		if (buttonId == 0 || buttonId == 1) {
			for(int i = getScrollY(); i < Math.min(tracks.size(), getScrollY() + tracksInView); i++) {
				Track track = tracks.get(i);
				
				for(int j = getScrollX(); j < Math.min(track.notes.size(), getScrollX() + notesInView); j++) {
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
							info |= instrumentInfo << maximumTracksPerModuleBitCount;
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

	private short lastModuleHeader;
	@Override
	protected void checkGuiData(Object info[]) {
		short moduleHeader = (short)tracks.size();
		moduleHeader |= (short)(speedSetting << maximumTracksPerModuleBitCount);
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
			speedSetting = (data & ~maximumTracksPerModule) >> maximumTracksPerModuleBitCount;
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
		if (isPlaceholder()) {
			return false;
		}else{
			return getDw(0) != 0 || playProgress > 0;
		}
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
			int trackPacketID = ((data[0] & ~maximumTracksPerModule) >> maximumTracksPerModuleBitCount);
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
			byte instrumentInfo = (byte)(((byte)(info & ~(byte)maximumTracksPerModule)) >> (byte)maximumTracksPerModuleBitCount);
			
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
	protected void Save(NBTTagCompound tagCompound, int id) {	
		short headerInfo = (short)tracks.size();
		headerInfo |= (short)(speedSetting << maximumTracksPerModuleBitCount);

		tagCompound.setShort(generateNBTName("Header",id), headerInfo);
		
		for (int i = 0; i < tracks.size();i++) {
			Track track = tracks.get(i);
			tagCompound.setShort(generateNBTName("Track" + i,id), track.getInfo());
			
			for (int j = 0; j < track.notes.size();j++) {
				Note note = track.notes.get(j);
				tagCompound.setShort(generateNBTName("Note" + i + ":" + j,id), note.getInfo());
			}
		}	
	
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		short headerInfo = tagCompound.getShort(generateNBTName("Header",id));
		receiveGuiData(0, headerInfo);
		for (int i = 0; i < tracks.size(); i++) {
			short trackInfo = tagCompound.getShort(generateNBTName("Track" + i,id));
			receiveGuiData(1+(maximumNotesPerTrack+1)*i, trackInfo);
			Track track = tracks.get(i);
			for (int j = 0; j < track.notes.size(); j++) {
				short noteInfo = tagCompound.getShort(generateNBTName("Note" + i + ":" + j,id));
				
				receiveGuiData(1+(maximumNotesPerTrack+1)*i+1+j, noteInfo);
			}
		}
		
	}		

}