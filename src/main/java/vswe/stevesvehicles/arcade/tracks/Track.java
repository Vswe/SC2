package vswe.stevesvehicles.arcade.tracks;

import java.util.ArrayList;

import vswe.stevesvehicles.arcade.ArcadeGame;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.client.interfaces.GuiBase.RENDER_ROTATION;
import vswe.stevesvehicles.module.common.attachment.ModuleArcade;

public class Track  {
	private int x;
	private int y;
	private int v;
	private RENDER_ROTATION rotation;
	private TrackOrientation orientation;
	private TrackOrientation orientationBackup;

	
	public Track(int x, int y, TrackOrientation orientation) {
		this.x = x;
		this.y = y;
		
		setOrientation(orientation);
	}
	

	
	private void setV(int v) {
		this.v = v;
	}	
	
	private void setRotation(RENDER_ROTATION rotation) {
		this.rotation = rotation;
	}
	
	public void setOrientation(TrackOrientation orientation) {
		this.orientation = orientation;
		setV(orientation.getV());
		setRotation(orientation.getRotation());		
	}
	
	

	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getU() {
		return 0;
	}
	
	public int getV() {
		return v;
	}
	
	public RENDER_ROTATION getRotation() {
		return rotation;
	}

	public TrackOrientation getOrientation() {
		return orientation;
	}
	

	public void onClick(ArcadeTracks game) {
		flip();
	}
	
	public void onEditorClick(ArcadeTracks game) {
		if (orientation.getOpposite() != null && game.getEditorDetectorTrack() != null) {
			game.getEditorDetectorTrack().addTarget(getX(), getY());
		}
	}


	public void flip() {
		if (orientation.getOpposite() != null) {
			ArcadeGame.playSound("gearswitch", 1, 1);
			setOrientation(orientation.getOpposite());
		}
	}	
	
	public void saveBackup() {
		orientationBackup = orientation;
	}
	
	public void loadBackup() {
		setOrientation(orientationBackup);
	}
	
	public Track copy() {
		return new Track(x, y, orientation);
	}
	
	
	public void travel(ArcadeTracks game, Cart cart) {}
	public void drawOverlay(ModuleArcade module, GuiVehicle gui, int x, int y, boolean isRunning) {}
	
	
	public static void addTrack(ArrayList<Track> tracks, int x1, int y1, int x2, int y2) {
		
		if (x1 != x2 && y1 != y2) {

			//(x1, y1) -> (x2, y1)
			//(x2, y1) -> (x2, y2)

			TrackOrientation corner = getCorner(!(x1 < x2), y1 < y2);
			
			int x2h = x2;
			
			//Right
			if (x1 < x2) {
				x2h--;
				
			//Left
			}else{
				x2h++;
			}
			
			int y1v = y1;
			
			//Down
			if (y1 < y2) {
				y1v++;
				
			//Up
			}else{
				y1v--;
			}
						
			addHorizontalTrack(tracks, x1, x2h, y1);
			tracks.add(new Track(x2, y1, corner));
			addVerticalTrack(tracks, x2, y1v, y2);

		}else if(x1 != x2) {
			addHorizontalTrack(tracks, x1, x2, y1);
		}else{
			addVerticalTrack(tracks, x1, y1, y2);
		}
	}	
	
	private static TrackOrientation getCorner(boolean right, boolean down) {
		if (right) {
			if (down) {
				return TrackOrientation.CORNER_DOWN_RIGHT;
			}else{
				return TrackOrientation.CORNER_UP_RIGHT;
			}
		}else{
			if (down) {
				return TrackOrientation.CORNER_DOWN_LEFT;
			}else{
				return TrackOrientation.CORNER_UP_LEFT;
			}
		}
	}
	
	private static void addHorizontalTrack(ArrayList<Track> tracks, int x1, int x2, int y) {
		int temp;
		if (x1 > x2) {
			temp = x1;
			x1 = x2;
			x2 = temp;
		}		
		
		for (int x = x1; x <= x2; x++) {
			tracks.add(new Track(x, y, TrackOrientation.STRAIGHT_HORIZONTAL));
		}		
	}
	
	private static void addVerticalTrack(ArrayList<Track> tracks, int x, int y1, int y2) {
		int temp;
		if (y1 > y2) {
			temp = y1;
			y1 = y2;
			y2 = temp;
		}			
		
		for (int y = y1; y <= y2; y++) {
			tracks.add(new Track(x, y, TrackOrientation.STRAIGHT_VERTICAL));
		}		
	}







	public void setExtraInfo(byte[] data) {

	}

	public byte[] getExtraInfo() {
		return new byte[0];
	}
	
	
	
}