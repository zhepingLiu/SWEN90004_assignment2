
import java.util.ArrayList;

/**
 * This class represents the patch in the board.
 *
 */
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
	 * the coordinate of the patch
	 */
	protected Coordinate coordinate;

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public boolean getMove() {
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
