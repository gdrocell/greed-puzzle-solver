package greed.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import greed.solver.ThreadedGreedSolver.GreedSolverJob;
import greed.solver.dt.GreedGrid;

/**
 * @author Gary Drocella
 *
 */
public class SimpleGreedSolver extends GreedSolver {

	public SimpleGreedSolver(String name, GreedGrid grid) {
		super(name,grid);
	}

	@Override
	public void solve() {
		Map<GreedGrid.Direction, Byte> moveMap = grid.getMoveMap();

		for(GreedGrid.Direction currDir : moveMap.keySet()) {
			solve(new GreedGrid(super.grid), currDir, 0, new LinkedList<GreedGrid.Direction>());
		}
		
	}

	public void solve(GreedGrid grid, GreedGrid.Direction takeDirection, long score, List<GreedGrid.Direction> directions) {
		Map<GreedGrid.Direction, Byte> moveMap = grid.getMoveMap();
		byte n = (Byte)moveMap.get(takeDirection);
		score += n;
		grid.moveDirectionN(takeDirection, n);
		directions.add(takeDirection);
		
		
		Map<GreedGrid.Direction,Byte> newMoveMap = grid.getMoveMap();
		
		if(isDeadEnd(newMoveMap)) {
			if(maxScore < score) {
				maxScore = score;
				maxScoreDirections = directions;
			}
		}
		else {
			for(GreedGrid.Direction currDir : newMoveMap.keySet()) {
				GreedGrid newGrid = new GreedGrid(grid);
				solve(newGrid, currDir, score, new LinkedList<GreedGrid.Direction>(directions));
			}
		}
	}
	
	public boolean isDeadEnd(Map<GreedGrid.Direction, Byte> m) {
		return m.size() == 0;
	}
}
