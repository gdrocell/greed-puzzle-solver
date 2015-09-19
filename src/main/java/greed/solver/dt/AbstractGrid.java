package greed.solver.dt;

import java.awt.Point;

/**
 * @author Gary Drocella
 */
public abstract class AbstractGrid<T> {
	protected int width;
	protected int length;
	
	public AbstractGrid(int width, int length) {
		this.width = width;
		this.length = length;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public boolean isValidPoint(Point p) {
		
		if(p != null) {
			int x = (int)p.getX();
			int y = (int)p.getY();
			return (x >= 0 && x < length) && (y >= 0 && y < width);
		}
		
		return false;
	}
	
	public abstract <T> T[][] getGrid();
}
