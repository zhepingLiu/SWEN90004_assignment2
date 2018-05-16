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
        int quietCount = 0;
        int activeCount = 0;
        int jailedCount = 0;
        int copCount = 0;

        for (int i=0; i<lengthX; i++) {
            for (int j=0; j<heightY; j++) {

                if (patches.get(keys[i][j]).isJailed()) {
                    jailedCount++;
                }

                if (!patches.get(keys[i][j]).isOccupied()) {
                    if (!patches.get(keys[i][j]).isJailed()) {
                        System.out.print("E ");
                        emptyCount++;
                    } else {
                        System.out.print("J ");
                    }
                }
                else if (patches.get(keys[i][j]).getCharacter() instanceof Cop)
                {
                    System.out.print("C ");
                    copCount++;
                }
                else if (patches.get(keys[i][j]).getCharacter()
                        instanceof Agent) {
                    if (((Agent) patches.get(keys[i][j]).
                            getCharacter()).isActive()) {
                        System.out.print("A ");
                        activeCount++;
                    } else {
                        System.out.print("Q ");
                        quietCount++;
                    }
                }
            }

            System.out.print("\n");
        }

        System.out.println("Empty: " + emptyCount);
        System.out.println("Active: " + activeCount);
        System.out.println("Quiet: " + quietCount);
        System.out.println("Jailed: " + jailedCount);
        System.out.println("Cops: " + copCount);
    }
}
