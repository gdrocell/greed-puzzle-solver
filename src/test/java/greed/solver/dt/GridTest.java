package greed.solver.dt;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Map;
import java.util.Set;

import greed.solver.dt.GreedGrid;

import org.junit.Test;



public class GridTest {
	
	@Test
	public void testGetValueAtPoint1() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("123456789162@456789212345");

			int v = grid.getValueAtPoint(new Point(0,0));
			assertTrue(v == 1);
		}
		catch(Exception e) {
			//System.err.println("Error " + e);
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetValueAtPoint2() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("123456789162@456789212345");
			int v = grid.getValueAtPoint(new Point(4,4));
			assertTrue(v == 5);
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetValueAtPoint3() {
		boolean exception = false;
		
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("123456789162@456789212345");
			int v = grid.getValueAtPoint(new Point(-1,-1));
		}
		catch(Exception e) {
			exception = true;
		}
		finally {
			assertTrue(exception);
		}
	}
	
	@Test
	public void testIsPathTraversable1() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("123456702162@456729212345");
			assertFalse(grid.isPathTraversable(new Point(2,2), new Point(0,2), GreedGrid.Direction.NORTH, 2));
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testIsPathTraversable2() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("123456799162@456789212345");
			System.out.println(grid.toString());
			
			assertTrue(grid.isPathTraversable(new Point(2,2), new Point(4,2), GreedGrid.Direction.SOUTH, 2));
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetMoveMap1() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("123456709160@056709012345");
			System.out.println(grid.toString());
			
			Map<?,?> moveMap = grid.getMoveMap();
			System.out.println(moveMap);
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testMoveDirectionN() {
		try {
			GreedGrid grid = new GreedGrid(5,5);
			grid.parseGrid("111111111111@122222222222");
			
			
			Map<?,?> moveMap = grid.getMoveMap();
			
			byte n = (Byte)moveMap.get(GreedGrid.Direction.SOUTH);
			grid.moveDirectionN(GreedGrid.Direction.SOUTH, n);
			System.out.println(grid.toString());
			
			System.out.println(grid.getMoveMap());
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	

}
