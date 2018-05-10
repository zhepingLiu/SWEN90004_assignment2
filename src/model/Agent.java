package model;

import utils.RandomUtil;

public class Agent extends Patch{
	// state 2 quite , 3 active, 4 jailed
	
	double risk_aversion ;
	double perceived_hardship;
	int jail_term;
	
	
	
	public Agent(int x, int y,double vision) {
		this.vision = vision;
		setCoordinate(x, y);
		init();
	}
	
	void init(){
		super.setState(2);
		risk_aversion = RandomUtil.getRandomDouble(1);
		perceived_hardship = RandomUtil.getRandomDouble(1);
	}

	

	
}
