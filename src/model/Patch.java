package model;

import java.util.ArrayList;

public class Patch {
	protected int state;
	protected double vision;
	protected boolean moved = false;
	protected ArrayList<Agent> JailedList = new ArrayList<>();
	protected Coordinate coordinate;

	protected ArrayList<Coordinate> neighbor = new ArrayList<>();

	public boolean isAnyAgentsJailed() {
		return !JailedList.isEmpty();
	}
	
	public void putAgentInJail(Agent agent){
		JailedList.add(agent);
	}

	public boolean isMove() {
		return moved;
	}

	public void setMoved() {
		this.moved = !this.moved;
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
