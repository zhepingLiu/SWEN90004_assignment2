import java.util.Set;

public class Patch {

    private static final int EMPTY_JAIL = 0;

    private Coordinate coordinate;
    private boolean occupied;
    private int jailNumber;
    private Set<Patch> neighbourhood;
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

    public int getJailNumber() {
        return jailNumber;
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

    public void setNeighbourhood(Set<Patch> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Set<Patch> getNeighbourhood() {
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
