package greed.solver;

/**
 * @author Gary Drocella
 *
 */
public abstract class AbstractSolver {
	private String name;
	
	public AbstractSolver(String name) {
		this.name = name;
	}
	
	public abstract void solve();
}
