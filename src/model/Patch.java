package model;

import java.util.ArrayList;

public class Patch {
	/**
	 * state for the child class, cop agent and empty
	 */
	protected int state;
	/**
	 * the vision of the patch
	 */
	protected double vision;
	/**
	 * the agent or the cop initialized as not moved
	 */
	protected boolean moved = false;
	/**
	 * the jailed agents will be put into the list
	 */
	protected ArrayList<Agent> JailedList = new ArrayList<>();
	/**
	 * the coordinate of the patch
	 */
	protected Coordinate coordinate;

	/**
	 * @return is there any agents jailed in the patch
	 */
	public boolean isAnyAgentsJailed() {
		return !JailedList.isEmpty();
	}
	
	public int getJailedSize(){
		return JailedList.size();
	}
	
	public void putAgentInJail(Agent agent){
		JailedList.add(agent);
	}
	/**
	 * find out a agent who is going to be released
	 * @return
	 */
	public Agent getReleasedAgent(){
		Agent returnAgent = null;
		if (isAnyAgentsJailed()) {
			
			for (Agent agent : JailedList) {
				if (agent.getJailTerm() == 0) {
					returnAgent = agent;
					break;
				}
			}
			// then remove the agent in the jailed list
			JailedList.remove(returnAgent);	
		}		
		return returnAgent;
	}

	/**
	 * reduce the jailed term for every jailed agent
	 */
	public void reduceJailTerm() {
		for (Agent agent : JailedList) {
			agent.reduceJailTerm();
		}
		
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	public boolean getMove(){
		return this.moved;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	/**
	 * set the coordinate of the patch
	 * 
	 * @param x
	 * @param y
	 */
	public void setCoordinate(int x, int y) {
		if (this.coordinate == null) {
			this.coordinate = new Coordinate(x, y);
		} else {
			this.coordinate.setX(x);
			this.coordinate.setY(y);
		}
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "" + this.state;
	}

}
