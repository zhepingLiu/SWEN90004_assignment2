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
    }

    public HashMap<Coordinate, Patch> getPatches() {
        return patches;
    }

    /**
     * Return the neighbourhood patches of the given patch
     * @param patch
     * @return
     */
    public ArrayList<Patch> getNeighbourhood(Patch patch) {
        ArrayList<Patch> neighbourhood = new ArrayList<>();
        Coordinate targetCoordinate = patch.getCoordinate();
        int x = targetCoordinate.getPositionX();
        int y = targetCoordinate.getPositionY();

        for (int i=1;i<=Controller.VISION;i++) {
//            if (x + i >= lengthX) {
//                neighbourhood.add(patches.get(keys[x + i - lengthX][y]));
//            } else {
//                neighbourhood.add(patches.get(keys[x + i][y]));
//            }
//
//            if (x - i < 0) {
//                neighbourhood.add(patches.get(keys[x - i + lengthX][y]));
//            } else {
//                neighbourhood.add(patches.get(keys[x - i][y]));
//            }
//
//            if (y + i >= heightY) {
//                neighbourhood.add(patches.get(keys[x][y + i - heightY]));
//            } else {
//                neighbourhood.add(patches.get(keys[x][y + i]));
//            }
//
//            if (y - i < 0) {
//                neighbourhood.add(patches.get(keys[x][y - i + heightY]));
//            } else {
//                neighbourhood.add(patches.get(keys[x][y - i]));
//            }
//
//            if (x + i >= lengthX && y + i >= heightY) {
//                neighbourhood.add(patches.get(
//                        keys[x + i - lengthX][y + i - heightY]));
//            } else {
//                neighbourhood.add(patches.get(keys[x + i][y + i]));
//            }
//
//            if (x - i < 0 && y - i < 0) {
//                neighbourhood.add(patches.get(
//                        keys[x - i + lengthX][y - i + heightY]));
//            } else {
//                neighbourhood.add(patches.get(keys[x - i][y - i]));
//            }

            //TODO: Try Mod 40 to set X and Y
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
                if (patches.get(keys[i][j]).isOccupied()) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.print("\n");
        }
    }
}
