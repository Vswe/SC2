package vswe.stevesvehicles.arcade.tracks;

import java.util.ArrayList;

import vswe.stevesvehicles.client.gui.GuiBase.RENDER_ROTATION;

public abstract class TrackOrientation {	
	
	public static ArrayList<TrackOrientation> ALL = new ArrayList<TrackOrientation>();
	
	public static TrackOrientation JUNCTION_4WAY  = new TrackOrientation (5, RENDER_ROTATION.NORMAL) {
		public Direction travel(Direction in) {
			return in.getOpposite();
		}
	};
	
	public static TrackOrientation STRAIGHT_HORIZONTAL = new TrackOrientationStraight(1, RENDER_ROTATION.ROTATE_90, Direction.RIGHT);
	public static TrackOrientation STRAIGHT_VERTICAL = new TrackOrientationStraight(1, RENDER_ROTATION.NORMAL, Direction.DOWN);
	
	public static TrackOrientation CORNER_DOWN_RIGHT = new TrackOrientationCorner(0, RENDER_ROTATION.NORMAL, Direction.DOWN, Direction.RIGHT);
	public static TrackOrientation CORNER_DOWN_LEFT = new TrackOrientationCorner(0, RENDER_ROTATION.ROTATE_90, Direction.DOWN, Direction.LEFT);
	public static TrackOrientation CORNER_UP_LEFT = new TrackOrientationCorner(0, RENDER_ROTATION.ROTATE_180, Direction.UP, Direction.LEFT);
	public static TrackOrientation CORNER_UP_RIGHT = new TrackOrientationCorner(0, RENDER_ROTATION.ROTATE_270, Direction.UP, Direction.RIGHT);
	
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN = new TrackOrientation3Way(4, RENDER_ROTATION.NORMAL, Direction.DOWN,  Direction.DOWN.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_270, Direction.RIGHT, Direction.RIGHT.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_180, Direction.UP, Direction.UP.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT = new TrackOrientation3Way(4,RENDER_ROTATION.ROTATE_90, Direction.LEFT, Direction.LEFT.getRight());
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_DOWN = new TrackOrientation3Way(4, RENDER_ROTATION.FLIP_HORIZONTAL, Direction.DOWN, Direction.DOWN.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN);
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_RIGHT = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_270_FLIP, Direction.RIGHT, Direction.RIGHT.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_UP = new TrackOrientation3Way(4, RENDER_ROTATION.FLIP_VERTICAL, Direction.UP, Direction.UP.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP);
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_LEFT = new TrackOrientation3Way(4, RENDER_ROTATION.ROTATE_90_FLIP, Direction.LEFT, Direction.LEFT.getLeft()).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT);
	
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(2, RENDER_ROTATION.NORMAL, Direction.DOWN, Direction.DOWN.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.FLIP_HORIZONTAL, Direction.DOWN, Direction.DOWN.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_180, Direction.UP, Direction.UP.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation3Way(2,RENDER_ROTATION.FLIP_VERTICAL , Direction.UP, Direction.UP.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_270_FLIP, Direction.RIGHT, Direction.RIGHT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_90, Direction.LEFT, Direction.LEFT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_90_FLIP, Direction.LEFT, Direction.LEFT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation3Way(2, RENDER_ROTATION.ROTATE_270, Direction.RIGHT, Direction.RIGHT.getOpposite());
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.NORMAL, Direction.DOWN, Direction.RIGHT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.FLIP_HORIZONTAL, Direction.DOWN, Direction.LEFT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_180, Direction.UP, Direction.LEFT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.FLIP_VERTICAL, Direction.UP, Direction.RIGHT).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_270_FLIP, Direction.RIGHT, Direction.DOWN).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_90, Direction.LEFT, Direction.DOWN).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_90_FLIP, Direction.LEFT, Direction.UP).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT);
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation3Way(3, RENDER_ROTATION.ROTATE_270, Direction.RIGHT, Direction.UP).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT);
	
	
	
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
	
	public abstract Direction travel(Direction in);
	
	private static class TrackOrientationStraight extends TrackOrientation {
		private Direction base;
		TrackOrientationStraight(int v, RENDER_ROTATION rotation, Direction base) {
			super(v, rotation);
			this.base = base;
		}
		
		@Override
		public Direction travel(Direction in) {
			if (in.equals(base)) {
				return base.getOpposite();
			}else{
				return base;
			}
		}		
	}	
	
	private static class TrackOrientationCorner extends TrackOrientation {
		private Direction dir1;
		private Direction dir2;
		TrackOrientationCorner(int v, RENDER_ROTATION rotation, Direction dir1, Direction dir2) {
			super(v, rotation);
			this.dir1 = dir1;
			this.dir2 = dir2;
		}
		
		@Override
		public Direction travel(Direction in) {
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
		private Direction entrance;
		private Direction active;
		TrackOrientation3Way(int v, RENDER_ROTATION rotation, Direction entrance, Direction active) {
			super(v, rotation);
			this.entrance = entrance;
			this.active = active;
		}
		
		@Override
		public Direction travel(Direction in) {
			if (in.equals(entrance)) {
				return active;
			}else{
				return entrance;
			}
		}		
	}
		
	
	
	
	public static enum Direction {
		UP (0, 0, -1),
		DOWN (1, 0, 1),
		LEFT (2, -1, 0),
		RIGHT (3, 1, 0),
		STILL (-1, 0 , 0);


		private int x;
		private int y;
		private int val;
		Direction(int val, int x, int y) {
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
		
		public Direction getOpposite() {
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
		
		public Direction getLeft() {
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
		
		public Direction getRight() {
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

		public static Direction fromInteger(int i) {
			for (Direction dir : values()) {
				if (dir.val == i) {
					return dir;
				}
			}
			return null;
		}
	}





}
