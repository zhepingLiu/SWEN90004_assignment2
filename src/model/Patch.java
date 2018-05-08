package model;

public class Patch {
	protected int state;
	protected double vision;
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
