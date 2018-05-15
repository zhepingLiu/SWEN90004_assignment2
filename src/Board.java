import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private int lengthX;
    private int heightY;

    private Coordinate[][] keys;
    private HashMap<Coordinate, Patch> patches;

    public Board() {
        this.lengthX = Controller.MAP_LENGTH_X;
        this.heightY = Controller.MAP_HEIGHT_Y;

        patches = new HashMap<>();
        keys = new Coordinate[lengthX][heightY];

        for (int i=0; i<lengthX; i++) {
            for (int j=0; j<heightY; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                keys[i][j] = coordinate;
                Patch patch = new Patch(coordinate);
                patches.put(coordinate, patch);
            }
        }

        //Cache all neighbourhood patches for each patch
        for (Patch p : patches.values()) {
            p.setNeighbourhood(getNeighbourhood(p));
        }
    }

    public Patch retrievePatch(int x, int y) {
        return patches.get(keys[x][y]);
    }

    /**
     * Return the neighbourhood patches of the given patch
     * @param patch the given patch
     * @return neighbourhood an ArrayList contains all neighbourhood
     *         patches around the given patch
     */
    public ArrayList<Patch> getNeighbourhood(Patch patch) {
        ArrayList<Patch> neighbourhood = new ArrayList<>();
        Coordinate targetCoordinate = patch.getCoordinate();

        int x = targetCoordinate.getPositionX();
        int y = targetCoordinate.getPositionY();
        neighbourhood.add(patches.get(keys[x][y]));

        for (int i=1;i<=Controller.VISION;i++) {
            int xPlusI = (x + i) % lengthX;
            int xMinusI = (x - i + lengthX) % lengthX;
            int yPlusI = (y + i) % heightY;
            int yMinusI = (y - i + heightY) % heightY;

            neighbourhood.add(patches.get(keys[xPlusI][y]));
            neighbourhood.add(patches.get(keys[xMinusI][y]));
            neighbourhood.add(patches.get(keys[x][yPlusI]));
            neighbourhood.add(patches.get(keys[x][yMinusI]));
            neighbourhood.add(patches.get(keys[xPlusI][yPlusI]));
            neighbourhood.add(patches.get(keys[xMinusI][yMinusI]));
            neighbourhood.add(patches.get(keys[xPlusI][yMinusI]));
            neighbourhood.add(patches.get(keys[xMinusI][yPlusI]));
        }
        return neighbourhood;
    }

    public void printBoard() {
        for (int i=0; i<lengthX; i++) {
            for (int j=0; j<heightY; j++) {
                if (!patches.get(keys[i][j]).isOccupied()) {
                    System.out.print("0 ");
                }
                else if (patches.get(keys[i][j]).getCharacter() instanceof Cop) {
                    System.out.print("2 ");
                }
                else if (patches.get(keys[i][j]).getCharacter()
                        instanceof Agent) {
                    System.out.print("1 ");
                }
            }
            System.out.print("\n");
        }
    }
}
