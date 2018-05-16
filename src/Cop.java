public class Cop implements Character{

    private int id;
    private Coordinate position;
    private boolean moved;

    public Cop(int id, Coordinate position) {
        this.id = id;
        this.position = position;
        //this.position.occupy(this);
        this.moved = false;
    }

    public int getId() {
        return id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void move(Coordinate targetPosition) {
        //this.position.empty();
        this.position = targetPosition;
        //this.position.occupy(this);
        this.moved = true;
    }
}
