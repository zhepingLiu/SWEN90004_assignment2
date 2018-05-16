import java.util.ArrayList;

public class Patch {

    private static final int EMPTY_JAIL = 0;

    private Coordinate coordinate;
    private boolean occupied;
    private int jailNumber;
    private ArrayList<Patch> neighbourhood;
    private Character character;


    public Patch(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.occupied = false;
        this.jailNumber = EMPTY_JAIL;
        neighbourhood = null;
        character = null;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isJailed() {
        return jailNumber > EMPTY_JAIL;
    }

    public void increaseJailNumber() {
        this.jailNumber++;
    }

    public void decreaseJailNumber() {
        this.jailNumber--;
    }

    public Character getCharacter() {
        return character;
    }

    public void setNeighbourhood(ArrayList<Patch> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public ArrayList<Patch> getNeighbourhood() {
        return neighbourhood;
    }

    public void occupy(Character character) {
        this.occupied = true;
        this.character = character;
    }

    public void empty() {
        this.occupied = false;
        this.character = null;
    }
}
