package model;

import utils.RandomUtil;

public class Agent extends Patch{
	// state 2 quite , 3 active, 4 jailed
	
	private double risk_aversion ;
	private double perceived_hardship;
	private int jail_term;
	
	public Agent() {
		init();
	}
	
	private void init(){
		super.setState(2);
		risk_aversion = RandomUtil.getRandomDouble(1);
		perceived_hardship = RandomUtil.getRandomDouble(1);
	}

}
