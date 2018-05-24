package Exception;

/**
 * @author Francis
 * 
 * When the sum of Initial cop density and initial agent density greater 
 * than 100, then throw the exception.
 *
 */
public class DensityException extends Exception {
	public DensityException() {
		super("The sum of INITIAL-COP-DENSITY and "
				+ "INITIAL-AGENT-DENSITY should not be greater than 100.");
	}
}
