package greed.solver.dt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Gary Drocella
 *
 */
public class GreedGrid extends AbstractGrid<Integer> {
	public final Byte marker = (byte)'@';
	protected Byte[][] grid;
	protected Point markerPos;
	
	public enum Direction { NORTH, EAST, SOUTH, WEST }
	
	public static final int DEFAULT_LENGTH = 22;
	public static final int DEFAULT_WIDTH = 79;
	
	public GreedGrid() throws MalformedGridException {
		this(DEFAULT_WIDTH,DEFAULT_LENGTH);
	}
	
	public GreedGrid(int width, int length) throws MalformedGridException {
		super(width, length);
		
		if(length < 0 || width < 0)
			throw new MalformedGridException("Invalid Grid Dimension " + length + "x" + width);
			
		grid = new Byte[length][width];
	}
	
	/**
	 * Creates a copy of the greed grid
	 */
	public GreedGrid(GreedGrid grid) {
		super(grid.getWidth(), grid.getLength());
		this.markerPos = new Point(grid.markerPos);
		this.grid = new Byte[length][width];
		
		for(int i= 0;i < grid.length; i++) {
			for(int j =0 ; j < this.grid[0].length; j++) {
				this.grid[i][j] = grid.grid[i][j];
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[][] getGrid() {
		return (T[][])grid;
	}
	
	public void setValueAtPoint(Point p, Byte v) {
		if(isValidPoint(p)) {
			grid[(int)p.getX()][(int)p.getY()] = v;
		}
	}
	
	
	
	public int getValueAtPoint(Point p)  {
		int value = -1;
		
		if(isValidPoint(p)) {
			int x = (int)p.getX(), y = (int)p.getY();
			try {
				value = Integer.valueOf(String.valueOf((char)grid[x][y].byteValue()));
			}
			catch(NumberFormatException e) {
				value = 0;
			}
		}
		else {
			throw new IllegalArgumentException("Point is invalid.");
		}
		
		return value;
	}
	
	/**
	 * Description: Determines if a path between two points is traversable, i.e. there is
	 * no cut in the path.
	 * @param start - start point
	 * @param dest - destination point
	 * @param dir - direction to travel
	 * @return true if path is traversable, false otherwise
	 */
	public boolean isPathTraversable(Point start, Point dest, Direction dir, int val) {
		boolean isTraversable = false;
		Point currPoint = start;
		int spacesMoved = 0;
		
		switch(dir) {
			case NORTH:
					currPoint = traverseNorth(currPoint, spacesMoved, val);
				break;
			case EAST:
					currPoint = traverseEast(currPoint, spacesMoved, val);
				break;
			case SOUTH:
					currPoint = traverseSouth(currPoint, spacesMoved, val);
				break;
			case WEST:
					currPoint = traverseWest(currPoint, spacesMoved, val);
				break;
			
		}
		
		if(getValueAtPoint(dest) > (byte)0 && currPoint.equals(dest)) {
			isTraversable = true;
		}
		
		return isTraversable;
	}
	
	private Point traverseNorth(Point currPoint, int spacesMoved, int val) {
		do {
			currPoint = new Point((int)currPoint.getX()-1,(int)currPoint.getY());
			
			if(getValueAtPoint(currPoint) == 0) {
				break;
			}

			spacesMoved++;
		}
		while(spacesMoved < val);
		
		return currPoint;
	}
	
	private Point traverseEast(Point currPoint, int spacesMoved, int val) {
		do {
			currPoint = new Point((int)currPoint.getX(),(int)currPoint.getY()+1);
			
			if(getValueAtPoint(currPoint) == 0) {
				break;
			}

			spacesMoved++;
		}
		while(spacesMoved < val);

		return currPoint;
	}
	
	private Point traverseSouth(Point currPoint, int spacesMoved, int val) {
		do {
			currPoint = new Point((int)currPoint.getX()+1,(int)currPoint.getY());
			
			if(getValueAtPoint(currPoint) == 0) {
				break;
			}
	
			spacesMoved++;
		}
		while(spacesMoved < val);
		
		return currPoint;
	}
	
	private Point traverseWest(Point currPoint, int spacesMoved, int val) {
		do {
			currPoint = new Point((int)currPoint.getX(),(int)currPoint.getY()-1);
			
			if(getValueAtPoint(currPoint) == 0) {
				break;
			}

			spacesMoved++;
		}
		while(spacesMoved < val);
		
		return currPoint;
	}
	
	public Map<Direction, Byte> getMoveMap() {
		Map<Direction, Byte> moveMap = new HashMap<Direction, Byte>();
		
		if(markerPos != null) {
			Point northPos = new Point((int)markerPos.getX() - 1, (int)markerPos.getY());
			Point eastPos = new Point((int)markerPos.getX(), (int)markerPos.getY()+1);
			Point southPos = new Point((int)markerPos.getX()+1, (int)markerPos.getY());
			Point westPos = new Point((int)markerPos.getX(), (int)markerPos.getY()-1);
			Point destPos = null;
			
			if(isValidPoint(northPos)) {
				int val = getValueAtPoint(northPos);
	
				if(val > 0) {
					destPos = new Point((int)markerPos.getX()-val, (int)markerPos.getY());

					if(isValidPoint(destPos) && isPathTraversable(markerPos, destPos, Direction.NORTH, val)) {
						moveMap.put(Direction.NORTH, new Byte((byte)val));
					}
				}
					
			}
			
			if(isValidPoint(eastPos)) {
				int val = getValueAtPoint(eastPos);
	
				if(val > 0) {
					destPos = new Point((int)markerPos.getX(), (int)markerPos.getY()+val);
				
					if(isValidPoint(destPos) && isPathTraversable(markerPos, destPos, Direction.EAST, val)) {
						moveMap.put(Direction.EAST, new Byte((byte)val));
					}
				}
			}
			
			if(isValidPoint(southPos)) {
				int val = getValueAtPoint(southPos);
				
				if(val > 0) {
					destPos = new Point((int)markerPos.getX()+val, (int)markerPos.getY());
				
					if(isValidPoint(destPos) && isPathTraversable(markerPos, destPos, Direction.SOUTH, val)) {
						moveMap.put(Direction.SOUTH, new Byte((byte)val));
					}
				}
			}
			
			if(isValidPoint(westPos)) {
				int val = getValueAtPoint(westPos);
				
				if(val > 0) {
					destPos = new Point((int)markerPos.getX(), (int)markerPos.getY()-val);
				
					if(isValidPoint(destPos) && isPathTraversable(markerPos, destPos, Direction.WEST, val)) {
						moveMap.put(Direction.WEST, new Byte((byte)val));
					}
				}
			}
		}
		
		return moveMap;
	}
	
	private Point findMarkerPos() throws MalformedGridException {
		
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				if(Byte.compare(marker, grid[i][j]) == 0) {
					return new Point(i,j);
				}
			}
		}
		
		throw new MalformedGridException("No Marker Found on Grid");
	}
	
	public void parseGrid(String gData) throws MalformedGridException {
		int count = 0;
		
		if(gData.length() > length*width) 
			throw new MalformedGridException("Grid Data does not match dimensions");
	
		
		for(int i = 0 ;i < length; i++) {
			for(int j = 0; j < width; j++) {
				grid[i][j] = (byte)gData.charAt(count);
				count++;
			}
		}
		
		markerPos = findMarkerPos();
	}
	
	public void parseGrid(File f) throws IOException, MalformedGridException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = null;
		String gridData = "";
		
		while((line = reader.readLine()) != null) {
			gridData += line.trim();
		}
		
		parseGrid(gridData);
	}
	
	public String toString() {
		String out = "";
		
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				out += String.valueOf((char)grid[i][j].byteValue());
			}
			out += "\n";
		}
		
		return out;
	}
	
	/**
	 * Moves the marker Position n spaces in the d direction.
	 * Checks to make sure is allowed to make the requested move.
	 * @param d - direction
	 * @param n - number of spaces to move
	 */
	public void moveDirectionN(Direction d, int n) {
		int offX =0, offY = 0;
		Point destPos = markerPos, currPoint = markerPos;
		
		switch(d) {
			case NORTH:
				offX = -1;
				destPos = new Point((int)markerPos.getX()-n, (int)markerPos.getY());
				break;
				
			case EAST:
				offY = 1;
				destPos = new Point((int)markerPos.getX(), (int)markerPos.getY()+n);
				break;
				
			case SOUTH:
				offX = 1;
				destPos = new Point((int)markerPos.getX()+n, (int)markerPos.getY());
				break;
				
			case WEST:
				offY = -1;
				destPos = new Point((int)markerPos.getX(), (int)markerPos.getY()-n);
				break;
		}
		
		if(!isPathTraversable(markerPos, destPos, d, n)) {
			return;
		}
		
		for(int i = 0; i < n; i++) {
			this.setValueAtPoint(currPoint, (byte)0);
			currPoint = new Point((int)currPoint.getX()+offX, (int)currPoint.getY()+offY);
		}
		
		markerPos = destPos;
		this.setValueAtPoint(markerPos, (byte)'@');
	}
	
}
