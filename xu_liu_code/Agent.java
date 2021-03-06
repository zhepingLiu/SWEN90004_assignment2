/**
 * This class represents the behavior of 
 * Agent.
 *
 */
public class Agent extends Patch {

	/**
	 * Risk Aversion
	 */
	private double riskAversion;
	/**
	 * Perceived Hardship
	 */
	private double perceivedHardship;
	/**
	 * Grievance
	 */
	private double grievance;
	/**
	 * Government legitimacy
	 */
	private double governmentLegitimacy;
	/**
	 * Estimate arrest probability
	 */
	private double estimatedArrestProbability;
	/**
	 * Jailed term
	 */
	private int jailTerm;

	public Agent(int x, int y, double vision, double governmentLegitimacy) {
		this.vision = vision;
		this.governmentLegitimacy = governmentLegitimacy;
		setCoordinate(x, y);
		init();
	}

	/**
	 * initial the agent with quiet state
	 */
	private void init() {
		//
		super.setState(Const.AGENT_QUIET);
		riskAversion = RandomUtil.getRandomDouble(Const.MAX_RISK_AVERSION);
		perceivedHardship = RandomUtil
				.getRandomDouble(Const.MAX_PERCEIVED_HARDSHIP);
	}

	public void setJailTerm(int jailTerm) {
		this.jailTerm = jailTerm;
	}

	/*
	 * reduce the jaild term of the agent
	 */
	public void reduceJailTerm() {
		if (jailTerm > 0) {
			this.jailTerm--;
		}
	}

	public int getJailTerm() {
		return jailTerm;
	}

	public void setGovernmentLegitimacy(double governmentLegitimacy) {
		this.governmentLegitimacy = governmentLegitimacy;
	}

	/**
	 * report grievance of the agent, same as the code in Netlogo model
	 */
	public void reportGrievance() {
		this.grievance = this.perceivedHardship * (1 - governmentLegitimacy);
	}

	/**
	 * report ArrestProbability of the agent, same as the code in Netlogo model
	 */
	public void reportArrestProbability(int copsCount, int activeCount) {
		activeCount++;
		this.estimatedArrestProbability = 1.0
				- Math.exp(-Const.K * (copsCount / activeCount));
	}

	/**
	 * determine whether the agent will active, same as the code in Netlogo
	 * model
	 */
	public void determineBehaviour(int copsCount, int activeCount) {
		reportGrievance();
		reportArrestProbability(copsCount, activeCount);
		if (this.grievance
				- riskAversion * estimatedArrestProbability > Const.THRESHOLD) {
			this.setState(Const.AGENT_ACTIVE);
		} else {
			this.setState(Const.AGENT_QUIET);
		}

	}

	/* for test
	 * @see model.Patch#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "grievance : " + this.grievance + "\n"
				+ "estimatedArrestProbability : "
				+ this.estimatedArrestProbability + "\nriskAversion : "
				+ this.riskAversion + "\n";
	}

}
