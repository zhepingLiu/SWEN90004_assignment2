package model;

public class Empty extends Patch{
	public Empty(int x, int y,double vision) {
		this.vision = vision;
		super.setState(0);
		super.setCoordinate(x, y);
		
	}
}
