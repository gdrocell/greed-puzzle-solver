package greed.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import greed.solver.dt.GreedGrid;

import org.junit.Test;

public class GreedyGreedSolverTest {
	@Test
	public void test1() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			String gridData = "123123123121@123123123123";
			grid.parseGrid(gridData);
			System.out.println(grid);
			
			GreedSolver gs = new GreedyGreedSolver("solver1", grid);
			gs.solve();
			
			System.out.println("Max Score: " + gs.getMaxScore());
			System.out.println("Directions: " + gs.getMaxScoreDirections());
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
