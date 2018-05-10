public class Coordinate {

    private int positionX;
    private int positionY;

    public Coordinate(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void increamentPositionX() {
        if (this.positionX++ <= Controller.MAP_LENGTH_X) {
            this.positionX++;
        } else {
            this.positionX = 0;
        }
    }

    public void incrementPositionY() {
        if (this.positionY++ <= Controller.MAP_HEIGHT_Y) {
            this.positionY++;
        } else {
            this.positionY = 0;
        }
    }
}
