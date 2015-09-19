package greed.solver.dt;

/**
 * @author Gary Drocella
 *
 */
public class MalformedGridException extends Exception {
	public MalformedGridException(String err) {
		super("Malformed Grid Exception: " + err);
	}
}