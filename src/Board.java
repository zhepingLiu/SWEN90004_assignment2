import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public HashMap<Coordinate, Patch> getPatches() {
        return patches;
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
    public Set<Patch> getNeighbourhood(Patch patch) {

        Set<Patch> neighbourhood = new HashSet<>();
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

            int j = (int) Math.sqrt(i);
            xPlusI = (x + j) % lengthX;
            xMinusI = (x - j + lengthX) % lengthX;
            yPlusI = (y + j) % heightY;
            yMinusI = (y - j + heightY) % heightY;

            neighbourhood.add(patches.get(keys[xPlusI][yPlusI]));
            neighbourhood.add(patches.get(keys[xMinusI][yMinusI]));
            neighbourhood.add(patches.get(keys[xPlusI][yMinusI]));
            neighbourhood.add(patches.get(keys[xMinusI][yPlusI]));
        }
        return neighbourhood;
    }

    public void printBoard() {

        int emptyCount = 0;

        for (int i=0; i<lengthX; i++) {
            for (int j=0; j<heightY; j++) {

                if (!patches.get(keys[i][j]).isOccupied()) {
                    if (!patches.get(keys[i][j]).isJailed()) {
                        System.out.print("E ");
                        emptyCount++;
                    } else {
                        System.out.print("J ");
                    }
                } else {
                    for (Character c : patches.get(keys[i][j]).getCharacter()) {
                        if (c instanceof Cop) {
                            System.out.print("C ");
                        } else if (c instanceof Agent) {
                            if (((Agent) c).isActive()) {
                                System.out.print("A ");
                            } else {
                                System.out.print("Q ");
                            }
                        }
                    }
                }
            }
            System.out.print("\n");
        }

        System.out.println("Empty: " + emptyCount);

    }
}
