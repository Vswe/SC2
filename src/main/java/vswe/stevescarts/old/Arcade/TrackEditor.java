package vswe.stevescarts.old.Arcade;

public class TrackEditor extends Track {
	public TrackEditor(TrackOrientation orientation) {
		super(0, 0, orientation);
	}
	
	
	private int type = 0;
		
	@Override
	public Track copy() {
		TrackEditor newTrack = new TrackEditor(getOrientation());
		newTrack.type = type;
		return newTrack;
	}
	

	public Track getRealTrack(int x, int y) {
		return getRealTrack(x, y, type, getOrientation());
	}
	
	public static Track getRealTrack(int x, int y, int type, TrackOrientation orientation) {
		switch (type) {
			case 1:
				return new TrackDetector(x, y, orientation);
			case 2:
				return new TrackHeavy(x, y, orientation);
			default:
				return new Track(x, y, orientation);
		}
	}	
	

	@Override
	public int getU() {
		return type;
	}
	
	
	public int getType() {
		return type;
	}
	
	public void setType(int val) {
		type = val;
	}

	public void nextType() {
		type = (type + 1) % 3;
	}
	
}
