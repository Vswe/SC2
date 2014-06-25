package vswe.stevesvehicles.old.Arcade;

import java.util.ArrayList;

import vswe.stevesvehicles.client.interfaces.GuiBase.RENDER_ROTATION;

public abstract class TrackOrientation {	
	
	public static ArrayList<TrackOrientation> ALL = new ArrayList<TrackOrientation>();
	
	public static TrackOrientation JUNCTION_4WAY  = new TrackOrientation (5, RENDER_ROTATION.NORMAL) {
		public DIRECTION travel(DIRECTION in) {
			return in.getOpposite();
		}
	};
	
	public static TrackOrientation STRAIGHT_HORIZONTAL = new TrackOrientationStraight(1, RENDER_ROTATION.ROTATE_90, DIRECTION.RIGHT);
	public static TrackOrientation STRAIGHT_VERTICAL = new TrackOrientationStraight(1, RENDER_ROTATION.NORMAL, DIRECTION.DOWN);
	
	public static TrackOrientation CORNER_DOWN_RIGHT = new TrackOrientationCorner(0, RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.RIGHT);
	public static TrackOrientation CORNER_DOWN_LEFT = new TrackOrientationCorner(0, RENDER_ROTATION.ROTATE_90, DIRECTION.DOWN, DIRECTION.LEFT);
	public static TrackOrientation CORNER_UP_LEFT = new TrackOrientationCorner(0, RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.LEFT);
	public static TrackOrientation CORNER_UP_RIGHT = new TrackOrientationCorner(0, RENDER_ROTATION.ROTATE_270, DIRECTION.UP, DIRECTION.RIGHT);
	
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN = new TrackOrientation3Way(4, RENDER_ROTATION.NORMAL, DIRECTION.DOWN,  DIRECTION.DOWN.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_270, DIRECTION.RIGHT, DIRECTION.RIGHT.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.UP.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT = new TrackOrientation3Way(4,RENDER_ROTATION.ROTATE_90, DIRECTION.LEFT, DIRECTION.LEFT.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_DOWN = new TrackOrientation3Way(4, RENDER_ROTATION.FLIP_HORIZONTAL, DIRECTION.DOWN, DIRECTION.DOWN.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN);
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_RIGHT = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_270_FLIP, DIRECTION.RIGHT, DIRECTION.RIGHT.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_UP = new TrackOrientation3Way(4, RENDER_ROTATION.FLIP_VERTICAL, DIRECTION.UP, DIRECTION.UP.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP);
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_LEFT = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_90_FLIP, DIRECTION.LEFT, DIRECTION.LEFT.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT);
	
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(2, RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.DOWN.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.FLIP_HORIZONTAL, DIRECTION.DOWN, DIRECTION.DOWN.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.UP.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation3Way(2,RENDER_ROTATION.FLIP_VERTICAL , DIRECTION.UP, DIRECTION.UP.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_270_FLIP, DIRECTION.RIGHT, DIRECTION.RIGHT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_90, DIRECTION.LEFT, DIRECTION.LEFT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_90_FLIP, DIRECTION.LEFT, DIRECTION.LEFT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_270, DIRECTION.RIGHT, DIRECTION.RIGHT.getOpposite());				
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.RIGHT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.FLIP_HORIZONTAL, DIRECTION.DOWN, DIRECTION.LEFT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.LEFT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.FLIP_VERTICAL, DIRECTION.UP, DIRECTION.RIGHT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_270_FLIP, DIRECTION.RIGHT, DIRECTION.DOWN).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_90, DIRECTION.LEFT, DIRECTION.DOWN).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_90_FLIP, DIRECTION.LEFT, DIRECTION.UP).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_270, DIRECTION.RIGHT, DIRECTION.UP).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT);
	
	
	
	private int v;
	private RENDER_ROTATION rotation;
	private TrackOrientation opposite;
	private int val;
	TrackOrientation(int v, RENDER_ROTATION rotation) {
		this.v = v;
		this.rotation = rotation;	
		
		val = ALL.size();
		ALL.add(this);
	}
	
	protected TrackOrientation setOpposite(TrackOrientation opposite) {
		this.opposite = opposite;
		if (this.opposite.opposite != null) {
			this.opposite.opposite.opposite = this;
		}else{
			this.opposite.opposite = this;
		}
		
		return this;
	}
	
	
	public int getV() {
		return v;
	}
	
	public RENDER_ROTATION getRotation() {
		return rotation;
	}
	
	public TrackOrientation getOpposite() {
		return opposite;
	}
	
	public int toInteger() {
		return val;
	}	
	
	public abstract DIRECTION travel(DIRECTION in);
	
	private static class TrackOrientationStraight extends TrackOrientation {
		private DIRECTION base;
		TrackOrientationStraight(int v, RENDER_ROTATION rotation, DIRECTION base) {
			super(v, rotation);
			this.base = base;
		}
		
		@Override
		public DIRECTION travel(DIRECTION in) {
			if (in.equals(base)) {
				return base.getOpposite();
			}else{
				return base;
			}
		}		
	}	
	
	private static class TrackOrientationCorner extends TrackOrientation {
		private DIRECTION dir1;
		private DIRECTION dir2;
		TrackOrientationCorner(int v, RENDER_ROTATION rotation, DIRECTION dir1, DIRECTION dir2) {
			super(v, rotation);
			this.dir1 = dir1;
			this.dir2 = dir2;
		}
		
		@Override
		public DIRECTION travel(DIRECTION in) {
			if (in.equals(dir1)) {
				return dir2;
			}else if(in.equals(dir2)) {
				return dir1;
			}else{
				return in.getOpposite();
			}
		}		
	}
	
	private static class TrackOrientation3Way extends TrackOrientation {
		private DIRECTION entrance;
		private DIRECTION active;
		TrackOrientation3Way(int v, RENDER_ROTATION rotation, DIRECTION entrance, DIRECTION active) {
			super(v, rotation);
			this.entrance = entrance;
			this.active = active;
		}
		
		@Override
		public DIRECTION travel(DIRECTION in) {
			if (in.equals(entrance)) {
				return active;
			}else{
				return entrance;
			}
		}		
	}
		
	
	
	
	public static enum DIRECTION {		
		UP (0, 0, -1),
		DOWN (1, 0, 1),
		LEFT (2, -1, 0),
		RIGHT (3, 1, 0),
		STILL (-1, 0 , 0);
		
		ArrayList<DIRECTION> ALL;
		
		private int x;
		private int y;
		private int val;
		DIRECTION(int val, int x, int y) {
			this.val = val;
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public DIRECTION getOpposite() {
			switch(this) {
				case UP:
					return DOWN;
				case DOWN:
					return UP;
				case LEFT:
					return RIGHT;
				case RIGHT:
					return LEFT;	
				default:
					return STILL;
			}
		}
		
		public DIRECTION getLeft() {
			switch(this) {
				case UP:
					return RIGHT;
				case DOWN:
					return LEFT;
				case LEFT:
					return UP;
				case RIGHT:
					return DOWN;	
				default:
					return STILL;
			}
		}	
		
		public DIRECTION getRight() {
			switch(this) {
				case UP:
					return LEFT;
				case DOWN:
					return RIGHT;
				case LEFT:
					return DOWN;
				case RIGHT:
					return UP;	
				default:
					return STILL;
			}
		}		
		
		public RENDER_ROTATION getRenderRotation() {
			switch(this) {
				case UP:
					return RENDER_ROTATION.NORMAL;
				case RIGHT:
					return RENDER_ROTATION.ROTATE_90;
				case DOWN:
					return RENDER_ROTATION.ROTATE_180;
				case LEFT:
					return RENDER_ROTATION.ROTATE_270;	
				default:					
					return RENDER_ROTATION.NORMAL;
			}			
		}

		public int toInteger() {
			return val;
		}

		public static DIRECTION fromInteger(int i) {
			for (DIRECTION dir : values()) {
				if (dir.val == i) {
					return dir;
				}
			}
			return null;
		}
	}





}
