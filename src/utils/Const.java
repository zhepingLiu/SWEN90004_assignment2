package utils;

/**
 * @author Zewen Xu
 *
 * Define the constant value of the model
 */
public class Const {

	// the board is 40 X 40
	public final static int board_size = 40;

	// default value for K
	public final static double K = 2.3;

	// default value for Threshold
    public final static double THRESHOLD = 0.1;

    public final static double MAX_RISK_AVERSION = 1.0;
    public final static double MAX_PERCEIVED_HARDSHIP = 1.0;
    
    //state 2 quiet , 3 active, 4 jailed
    public final static int AGENT_QUIET = 2;
    public final static int AGENT_ACTIVE = 3;
    public final static int AGENT_JAILED = 4;
}
