package greed.solver;

import static org.junit.Assert.*;
import greed.solver.GreedSolver;
import greed.solver.ThreadedGreedSolver;
import greed.solver.dt.GreedGrid;
import greed.solver.dt.MalformedGridException;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * @author Gary Drocella
 *
 */
public class ThreadedGreedSolverTest {
	@Test
	public void test1() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			String gridData = "123123123121@123123123123";
			grid.parseGrid(gridData);
			System.out.println(grid);
			
			GreedSolver gs = new ThreadedGreedSolver("solver1", gridData, 5,5);
			gs.solve();
			
			System.out.println("Max Score: " + gs.getMaxScore());
			System.out.println("Directions: " + gs.getMaxScoreDirections());
			assertEquals(17, gs.getMaxScore());
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void test2() throws MalformedGridException, IOException {
		GreedGrid grid = new GreedGrid(7,7);
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("grid_7x7.dat").getFile());
		grid.parseGrid(f);
		System.out.println(grid);
		GreedSolver gs = new ThreadedGreedSolver("Threaded Solver", grid);
		
		
		gs.solve();
		
		
		System.out.println("Max Score: " + gs.getMaxScore());
		System.out.println("Directions: " + gs.getMaxScoreDirections());
		
		assertEquals(11, gs.getMaxScore());
	}
	
	@Test
	public void test3() throws MalformedGridException, IOException {
		GreedGrid grid = new GreedGrid(9,9);
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("grid_9x9.dat").getFile());
		grid.parseGrid(f);
		System.out.println(grid);
		GreedSolver gs = new ThreadedGreedSolver("Threaded Solver", grid);
		gs.solve();

		System.out.println("Max Score: " + gs.getMaxScore());
		System.out.println("Directions: " + gs.getMaxScoreDirections());
		
		assertEquals(7, gs.getMaxScore());
	}
	
	@Test
	public void test4() throws MalformedGridException, IOException {
		GreedGrid grid = new GreedGrid(12,12);
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("grid_12x12.dat").getFile());
		grid.parseGrid(f);
		System.out.println(grid);
		GreedSolver gs = new ThreadedGreedSolver("Threaded Solver", grid);
		long start = System.currentTimeMillis();
		gs.solve();
		long finished = System.currentTimeMillis();
		double delta = (finished-start)/1000.0;
		System.out.println("Time: " + delta);
		
		System.out.println("Max Score: " + gs.getMaxScore());
		System.out.println("Directions: " + gs.getMaxScoreDirections());
	}
	
	@Test
	public void test5() throws MalformedGridException, IOException {
		GreedGrid grid = new GreedGrid(16,16);
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("grid_16x16.dat").getFile());
		grid.parseGrid(f);
		System.out.println(grid);
		GreedSolver gs = new ThreadedGreedSolver("solver1", grid);
		long start = System.currentTimeMillis();
		gs.solve();
		long finished = System.currentTimeMillis();
		double delta = (finished-start)/1000.0;
		System.out.println("Time: " + delta);
		
		System.out.println("Max Score: " + gs.getMaxScore());
		System.out.println("Directions: " + gs.getMaxScoreDirections());
	}
	
	@Test
	public void testLoopWithDeadEnd() throws MalformedGridException {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("99999999119991199321999@9");
			System.out.println(grid);
			GreedSolver gs = new ThreadedGreedSolver("solver1", grid);
			long start = System.currentTimeMillis();
			gs.solve();
			long finished = System.currentTimeMillis();
			double delta = (finished-start)/1000.0;
			System.out.println("Time: " + delta);
			
			System.out.println("Max Score: " + gs.getMaxScore());
			System.out.println("Directions: " + gs.getMaxScoreDirections());
			
		}
		finally {
			
		}
	}
	
	/**
	 * 99999
	 * 91399
	 * 99991
	 * 12931
	 * 9@991
	 */
	
	@Test
	public void testLoopWithDeadEnd2() throws MalformedGridException {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("999999139999991129319@991");
			System.out.println(grid);
			GreedSolver gs = new ThreadedGreedSolver("solver1", grid);
			long start = System.currentTimeMillis();
			gs.solve();
			long finished = System.currentTimeMillis();
			double delta = (finished-start)/1000.0;
			System.out.println("Time: " + delta);
			
			System.out.println("Max Score: " + gs.getMaxScore());
			System.out.println("Directions: " + gs.getMaxScoreDirections());
			
		}
		finally {
			
		}
	}
}
