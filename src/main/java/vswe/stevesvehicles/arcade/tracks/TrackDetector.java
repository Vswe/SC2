package vswe.stevesvehicles.arcade.tracks;

import java.util.ArrayList;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.common.attachment.ModuleArcade;


public class TrackDetector extends Track {
	public TrackDetector(int x, int y, TrackOrientation orientation) {
		super(x, y, orientation);
		
		targets = new ArrayList<TrackCoordinate>(); 
	}
	
	private ArrayList<TrackCoordinate> targets;
	
	@Override
	public Track copy() {
		TrackDetector newTrack = new TrackDetector(getX(), getY(), getOrientation());
		newTrack.targets = targets;
		return newTrack;
	}	
	
	public TrackDetector addTarget(int x, int y) {
		if ((int)Math.ceil(targets.size() * (9F / 8)) == 63) {
			return this;
		}
		
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).getX() == x && targets.get(i).getY() == y) {
				targets.remove(i);
				return this;
			}
		}
		
		targets.add(new TrackCoordinate(x, y));
		
		return this;
	}
	


	@Override
	public void setExtraInfo(byte[] data) {
		int startPosition = 0;
		short content = 0;

        for (byte b : data) {
            short val = b;
            if (val < 0) {
                val += 256;
            }

            content |= (val & ((int) Math.pow(2, Math.min(8, 9 - startPosition)) - 1)) << startPosition;

            if (startPosition == 0) {
                startPosition = 8;
            } else {
                addTarget(content & 31, (content & 480) >> 5);


                content = (short) ((val & (((int) Math.pow(2, (startPosition - 1)) - 1) << (9 - startPosition))) >> (9 - startPosition));
                startPosition = (startPosition + 8) % 9;

            }


        }
	}

	@Override
	public byte[] getExtraInfo() {
		byte[] ret = new byte[(int)Math.ceil(targets.size() * (9F / 8))];
				
		int currentByte = 0;
		int startPosition = 0;
        for (TrackCoordinate target : targets) {
            short data = (short) target.getX(); // 5 bits
            data |= target.getY() << 5; // 4 bits

            ret[currentByte] |= (byte) ((data & ((int) Math.pow(2, 8 - startPosition) - 1)) << startPosition);
            currentByte++;
            ret[currentByte] = (byte) ((data & (((int) Math.pow(2, 1 + startPosition) - 1) << (8 - startPosition))) >> (8 - startPosition));
            startPosition = (startPosition + 1) % 8;
            if (startPosition == 0) {
                currentByte++;
            }
        }
		
		
		return ret;
	}	
	
	@Override
	public int getU() {
		return 1;
	}
	
	@Override
	public void travel(ArcadeTracks game, Cart cart) {
		for (TrackCoordinate target : targets) {
			Track track = game.getTrackMap()[target.getX()][target.getY()];
			if (track != null) {
				track.flip();
			}
		}
	}	
	
	@Override
	public void drawOverlay(ModuleArcade module, GuiVehicle gui, int x, int y, boolean isRunning) {
		if (!isRunning && module.inRect(x, y, ArcadeTracks.getTrackArea(getX(), getY()))) {
			for (TrackCoordinate target : targets) {
				module.drawImage(gui, ArcadeTracks.getTrackArea(target.getX(), target.getY()), 0, 128);
			}			
		}
	}	
	
	@Override
	public void onEditorClick(ArcadeTracks game) {
		game.setEditorDetectorTrack(this);
	}	
	
	private static class TrackCoordinate {
		private int x;
		private int y;
		public TrackCoordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
	}
}
