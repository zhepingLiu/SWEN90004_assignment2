import java.util.ArrayList;

public class Patch {
    private Coordinate coordinate;
    private boolean occupied;
    private ArrayList<Patch> neighbourhood;
    private Character character;


    public Patch(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.occupied = false;
        neighbourhood = null;
        character = null;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setNeighbourhood(ArrayList<Patch> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public ArrayList<Patch> getNeighbourhood() {
        return neighbourhood;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void empty() {
        this.occupied = false;
    }
}
