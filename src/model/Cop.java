package model;

public class Cop extends Patch{
	// cops of the model, initial state is 1
	public Cop(int x, int y,double vision) {
		super.setState(1);
		this.vision = vision;
		super.setCoordinate(x, y);
	}
}
