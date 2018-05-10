package model;

import java.util.ArrayList;

public class Patch {
	protected int state;
	protected double vision;
	protected boolean moved;
	protected ArrayList<Agent> JailedList = new ArrayList<>();
	protected Coordinate coordinate;

	protected ArrayList<Coordinate> neighbor = new ArrayList<>();

	public boolean isAnyAgentsJailed() {
		return !JailedList.isEmpty();
	}

	public boolean isMove() {
		return moved;
	}

	public void setMoved() {
		this.moved = !this.moved;
	}

	protected void setState(int state) {
		this.state = state;
	}

	protected int getState() {
		return state;
	}

	protected void updateNeighborhood(Coordinate coordinate) {
		int intVision = (int) vision;
		for (int i = -intVision; i <= intVision; i++) {
			for (int j = -intVision; j <= intVision; j++) {
				if (Math.abs(i) + Math.abs(j) < vision && coordinate.getX() + i >= 0 && coordinate.getX() + i < 40
						&& coordinate.getY() + j >= 0 && coordinate.getY() + j < 40) {
					neighbor.add(new Coordinate(coordinate.getX() + i, coordinate.getY() + j));
				}
			}
		}
	}

	public void setCoordinate(int x, int y) {
		if (this.coordinate == null) {
			this.coordinate = new Coordinate(x, y);
		} else {
			this.coordinate.setX(x);
			this.coordinate.setY(y);
		}
		updateNeighborhood(this.coordinate);
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public ArrayList<Coordinate> getNeighbor() {
		return neighbor;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "" + this.state;
	}
}
