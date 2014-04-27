package vswe.stevescarts.Arcade;

public class TrackHeavy extends Track {
	public TrackHeavy(int x, int y, TrackOrientation orientation) {
		super(x, y, orientation);
	}
	
	@Override
	public void onClick(ArcadeTracks game) {
		//do nothing
	}

	@Override
	public Track copy() {
		return new TrackHeavy(getX(), getY(), getOrientation());
	}		
	
	@Override
	public int getU() {
		return 2;
	}
	
}
