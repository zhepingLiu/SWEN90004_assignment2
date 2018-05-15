package model;

public class Cop extends Patch{
	public Cop(int x, int y,double vision) {
		super.setState(1);
		this.vision = vision;
		super.setCoordinate(x, y);
	}
}
