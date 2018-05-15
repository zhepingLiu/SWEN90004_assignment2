package model;

import utils.Const;
import utils.RandomUtil;

public class Agent extends Patch {
	// state 2 quite , 3 active, 4 jailed

	private double riskAversion;
	private double perceivedHardship;
	private double grievance;
	private double governmentLegitimacy;
	private double estimatedArrestProbability;
	private int jailTerm;

	public Agent(int x, int y, double vision, double governmentLegitimacy) {
		this.vision = vision;
		this.governmentLegitimacy = governmentLegitimacy;
		setCoordinate(x, y);
		init();
	}

	private void init() {
		super.setState(Const.AGENT_Quiet);
		riskAversion = RandomUtil.getRandomDouble(Const.MAX_RISK_AVERSION);
		perceivedHardship = RandomUtil.getRandomDouble(Const.MAX_PERCEIVED_HARDSHIP);
	}

	public void setJailTerm(int jailTerm) {
		this.jailTerm = jailTerm;
	}
	
	public void reportGrievance() {
		this.grievance = this.perceivedHardship * (1 - governmentLegitimacy);
	}

	public void reportArrestProbability(int copsCount, int activeCount) {
		activeCount++;
		this.estimatedArrestProbability = 1 - Math.exp(-Const.K * (copsCount / activeCount));
	}

	public void determineBehaviour(int copsCount, int activeCount) {
		reportGrievance();
		reportArrestProbability(copsCount, activeCount);
		if (this.grievance - riskAversion * estimatedArrestProbability > Const.THRESHOLD) {
			this.setState(Const.AGENT_ACTIVE);
		} else {
			this.setState(Const.AGENT_Quiet);
		}

	}

}
