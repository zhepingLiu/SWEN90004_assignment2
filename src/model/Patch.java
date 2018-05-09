package model;

import java.util.ArrayList;

public class Patch {
	protected int state;
	protected double vision;
	protected boolean moved;
	protected ArrayList<Agent> JailedList = new ArrayList<>();
	
	public boolean isAnyAgentsJailed() {
		return !JailedList.isEmpty();
	}
	
	public boolean isMove() {
		return moved;
	}
	public void setMoved() {
		this.moved = !this.moved;
	}
	protected void setState(int state){
		this.state = state;
	}
	protected int getState (){
		return state;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+this.state;
	}
}
