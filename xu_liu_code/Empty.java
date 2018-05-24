

/**
 * This class represents the empty patch.
 *
 */
public class Empty extends Patch{
	// empty patch of the model, use 0 to represent
	public Empty(int x, int y,double vision) {
		this.vision = vision;
		super.setState(0);
		super.setCoordinate(x, y);
		
	}
}
