import java.util.Set;
import java.util.ArrayList;

public class Patch {

    private static final int EMPTY_JAIL = 0;

    private Coordinate coordinate;
    private int jailNumber;
    private Set<Patch> neighbourhood;
    private ArrayList<Character> characters;


    public Patch(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.jailNumber = EMPTY_JAIL;
        neighbourhood = null;
        characters = new ArrayList<>();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isOccupied() {
        return !characters.isEmpty();
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

    public ArrayList<Character> getCharacter() {
        return characters;
    }

    public void setNeighbourhood(Set<Patch> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Set<Patch> getNeighbourhood() {
        return neighbourhood;
    }

    public void occupy(Character character) {
        this.characters.add(character);
    }

    public void empty(Character character) {
        this.characters.remove(character);
    }
}
