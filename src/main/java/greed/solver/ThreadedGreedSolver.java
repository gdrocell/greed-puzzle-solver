package greed.solver;

import greed.solver.dt.GreedGrid;
import greed.solver.dt.GreedGrid.Direction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Gary Drocella
 */
public class ThreadedGreedSolver extends GreedSolver {
	
	public static final int DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors();
	private ThreadPoolExecutor exe;
	private LinkedBlockingQueue runningQueue;

	private static final Lock lock = new ReentrantLock();
	private final Object monitor = new Object();
	
	private AtomicInteger threadCount;
	
	
	public ThreadedGreedSolver(String name, String gridData) {
		this(name, gridData, GreedGrid.DEFAULT_LENGTH, GreedGrid.DEFAULT_WIDTH);
	}
	
	public ThreadedGreedSolver(String name, String gridData, int length, int width) {
		super(name, gridData, length, width);
		runningQueue = new LinkedBlockingQueue();
		exe = new ThreadPoolExecutor(DEFAULT_THREAD_COUNT,DEFAULT_THREAD_COUNT, 0, TimeUnit.SECONDS,runningQueue);
		threadCount = new AtomicInteger(0);
	}

	public ThreadedGreedSolver(String name, GreedGrid grid) {
		super(name, grid);
		runningQueue = new LinkedBlockingQueue();
		exe = new ThreadPoolExecutor(DEFAULT_THREAD_COUNT,DEFAULT_THREAD_COUNT, 0, TimeUnit.SECONDS,runningQueue);
		threadCount = new AtomicInteger(0);
	}

	protected class GreedSolverJob implements Runnable {
		/* A List of Directions this solver suggests to get score */
		protected List<GreedGrid.Direction> currentDirections;
		
		/* The Current Max Score of the Suggested Path */
		protected long currentScore;
		
		/* The Greed Grid from the perspective of this Greed Solver Job */
		protected GreedGrid gridRef;
		
		protected GreedGrid.Direction initialStep;
		/**
		 * Constructor
		 * @param gridRef - the Greed Grid
		 */
		protected GreedSolverJob(GreedGrid gridRef) {
			currentDirections = new LinkedList<GreedGrid.Direction>();
			currentScore = 0;
			this.gridRef = gridRef;
			synchronized(monitor) {
				threadCount.incrementAndGet();
			}
		}
		
		protected GreedSolverJob(GreedGrid gridRef, GreedGrid.Direction initialStep, long currentScore, List<GreedGrid.Direction> currentDirections) {
			this.currentDirections = currentDirections;
			this.currentScore = currentScore;
			this.gridRef = gridRef;
			this.initialStep = initialStep;
			synchronized(monitor) {
				threadCount.incrementAndGet();
			}
		}
		
		public void run() {
			
			solve(this.gridRef, this.initialStep, this.currentScore, new LinkedList<GreedGrid.Direction>(this.currentDirections));
			
			synchronized(monitor) {
				threadCount.decrementAndGet();
				monitor.notify();
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
				lock.lock();
				if(maxScore < score) {
					maxScore = score;
					maxScoreDirections = directions;
				}
				lock.unlock();
			}
			else {
				for(GreedGrid.Direction currDir : newMoveMap.keySet()) {
					GreedGrid newGrid = new GreedGrid(grid);
					LinkedList<GreedGrid.Direction> newDirections = new LinkedList<GreedGrid.Direction>(directions);
					if(exe.getActiveCount() < DEFAULT_THREAD_COUNT) {
						GreedSolverJob gsj = new GreedSolverJob(newGrid, currDir, score, newDirections);
						exe.execute(gsj);
						
					}
					else {
						solve(newGrid, currDir, score, newDirections);
					}
				}
			}
		}
		
		public boolean isDeadEnd(Map<GreedGrid.Direction, Byte> m) {
			return m.size() == 0;
		}
	}

	@Override
	public void solve() {
		Map<GreedGrid.Direction, Byte> moveMap = grid.getMoveMap();

		for(GreedGrid.Direction currDir : moveMap.keySet()) {
			exe.execute(new GreedSolverJob(new GreedGrid(grid), currDir, 0, new LinkedList()));
		}
		
		synchronized(monitor) {
			while(threadCount.get() > 0) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean finished() {
		return exe.getActiveCount() == 0;
	}
	
	public void shutdown() {
		exe.shutdown();
	}
}
