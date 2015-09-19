package greed.app;

import greed.solver.GreedSolver;
import greed.solver.GreedyGreedSolver;
import greed.solver.SimpleGreedSolver;
import greed.solver.ThreadedGreedSolver;
import greed.solver.dt.GreedGrid;
import greed.solver.dt.MalformedGridException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Gary Drocella
 *
 */
public class CmdClient {
	
	protected enum SolveType {
		SIMPLE(SimpleGreedSolver.class.getCanonicalName()),
		THREADED(ThreadedGreedSolver.class.getCanonicalName()),
		GREEDY(GreedyGreedSolver.class.getCanonicalName());
		
		private String value;
		
		private SolveType(String value) {
			this.value = value;
		}
		
		String getValue() {
			return this.value;
		}
		
		public static SolveType value(String v) {
			for(SolveType st : values()) {
				if(st.getValue().equals(v)) {
					return st;
				}
			}
			throw new IllegalArgumentException();
		}
	}
	
	public static void main(String[] args) throws IOException, MalformedGridException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		if(args.length < 4) {
			System.out.println("java -jar greed-puzzle-solver-0.0.1-SNAPSHOT-jar-with-dependencies.jar <solve type> <file name> <# rows> <# cols>");
			return;
		}
		
		System.out.println(SimpleGreedSolver.class.getCanonicalName());
		String solveType = args[0];
		String fname = args[1];
		SolveType st = SolveType.value(solveType.trim());
		int rows = Integer.parseInt(args[2]);
		int cols = Integer.parseInt(args[3]);
		GreedGrid grid = createGreedGrid(fname, cols, rows);
		GreedSolver gs = createGreedSolver(st, grid);
		
		long start = System.currentTimeMillis();
		gs.solve();
		long stop = System.currentTimeMillis();
		double delta = (stop-start)/1000.0;
		
		System.out.println("Solved Grid:\n" + grid);
		System.out.println("Time: " + delta + " seconds");
		System.out.println("Max Score: " + gs.getMaxScore());
		System.out.println("Directions: " + gs.getMaxScoreDirections());
		
		if(gs instanceof ThreadedGreedSolver) {
			((ThreadedGreedSolver)gs).shutdown();
		}
	}
	
	public static GreedSolver createGreedSolver(SolveType solveType,GreedGrid grid) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		GreedSolver gs = (GreedSolver) Class.forName(solveType.getValue()).getConstructor(String.class, GreedGrid.class)
				.newInstance(solveType.getValue(),grid);
		return gs;
	}
	
	public static GreedGrid createGreedGrid(String fname, int cols, int rows) throws IOException, MalformedGridException {
		File f = new File(fname);
		GreedGrid grid = new GreedGrid(cols,rows);
		grid.parseGrid(f);
		return grid;
	}
}
