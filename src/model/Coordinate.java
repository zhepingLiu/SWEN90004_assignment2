package model;

/**
 * @author Zewen Xu
 *
 * the Coordinate of every patch on the board
 */
public class Coordinate {
	int x ;
	int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return this.x == ((Coordinate)obj).getX() & this.y == 
						 ((Coordinate)obj).getY();
	}
}
