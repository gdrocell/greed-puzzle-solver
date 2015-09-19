package greed.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import greed.solver.dt.GreedGrid;

/**
 * @author Gary Drocella
 * Implementation of a greedy algorithm to solve a greed puzzle.  Each move pick the direction 
 * yielding the local maximum number of spaces that can be moved.  Note: Obviously greedy algorithms
 * are not applicable to the greed puzzle, but it has been implemented for amusement and observation.
 */
public class GreedyGreedSolver extends GreedSolver {

	public GreedyGreedSolver(String name, GreedGrid grid) {
		super(name, grid);
	}


	@Override
	public void solve() {
		GreedGrid theGrid = new GreedGrid(grid);
		
		Map<GreedGrid.Direction, Byte> moveMap = theGrid.getMoveMap();
		GreedGrid.Direction maxDirection = null;
		byte maxLocalMove;
		
		long score = 0;
		List<GreedGrid.Direction> directions = new ArrayList<GreedGrid.Direction>();
		
		while(!isDeadEnd(moveMap)){
			maxLocalMove = 0;
			for(GreedGrid.Direction currDir : moveMap.keySet()) {
				byte currLocalMove = moveMap.get(currDir);
				if(currLocalMove > maxLocalMove) {
					maxLocalMove = currLocalMove;
					maxDirection = currDir;
				}
				else if(currLocalMove == maxLocalMove && currDir.ordinal() > maxDirection.ordinal()) {
					maxLocalMove = currLocalMove;
					maxDirection = currDir;
				}
			}
			
			score += maxLocalMove;
			directions.add(maxDirection);
			theGrid.moveDirectionN(maxDirection, maxLocalMove);
			
			moveMap = theGrid.getMoveMap();
		}
		
		super.maxScore = score;
		super.maxScoreDirections = directions;
	}

	public boolean isDeadEnd(Map<GreedGrid.Direction, Byte> map) {
		return map.size() == 0;
	}
}
