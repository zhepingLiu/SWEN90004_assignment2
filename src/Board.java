import java.util.ArrayList;

public class Board {

    private int lengthX;
    private int heightY;

    //Two dimensional ArrayList
    private ArrayList<ArrayList<Patch>> patches;

    public Board() {
        this.lengthX = Controller.MAP_LENGTH_X;
        this.heightY = Controller.MAP_HEIGHT_Y;

        patches = new ArrayList<>();

        for (int i=0; i<lengthX; i++) {
            //Creating a new row of patches
            ArrayList<Patch> newRow = new ArrayList<>();
            for (int j=0; j<heightY; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                Patch patch = new Patch(coordinate);
                newRow.add(patch);
            }
            //Add the new row to patches
            patches.add(newRow);
        }
    }

    public ArrayList<ArrayList<Patch>> getPatches() {
        return patches;
    }

    public void printBoard() {
        for (int i=0; i<lengthX; i++) {
            for (int j=0; j<heightY; j++) {
                if ((patches.get(i)).get(j).isOccupied()) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.print("\n");
        }
    }
}
