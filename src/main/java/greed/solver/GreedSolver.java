package greed.solver;

import java.util.List;

import greed.solver.dt.GreedGrid;
import greed.solver.dt.MalformedGridException;

/**
 * @author Gary Drocella
 *
 */
public abstract class GreedSolver extends AbstractSolver {
	protected GreedGrid grid;
	protected long maxScore;
	protected List<GreedGrid.Direction> maxScoreDirections;
	
	public GreedSolver(String name, String gridData) {
		this(name, gridData,GreedGrid.DEFAULT_LENGTH,GreedGrid.DEFAULT_WIDTH);
	}
	
	public GreedSolver(String name, String gridData, int length, int width) {
		super(name);
		try {
			grid = new GreedGrid(length,width);
			grid.parseGrid(gridData);
		}
		catch(MalformedGridException e) {
			System.err.println("Error: " + e);
		}
	}
	
	public GreedSolver(String name, GreedGrid grid) {
		super(name);
		this.grid = grid;
	}
	
	public long getMaxScore() {
		return this.maxScore;
	}
	
	public List<GreedGrid.Direction> getMaxScoreDirections() {
		return this.maxScoreDirections;
	}
}
