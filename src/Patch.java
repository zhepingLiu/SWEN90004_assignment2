import java.util.ArrayList;

public class Patch {
    private Coordinate coordinate;
    private boolean occupied;
    private ArrayList<Patch> neighbourhood;


    public Patch(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.occupied = false;
        neighbourhood = null;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setNeighbourhood(ArrayList<Patch> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void empty() {
        this.occupied = false;
    }
}
