public class Patch {
    private Coordinate coordinate;
    private boolean occupied;

    public Patch(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.occupied = false;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void empty() {
        this.occupied = false;
    }
}
