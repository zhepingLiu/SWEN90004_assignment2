public class Cop {

    private int id;
    private Patch position;
    private boolean moved;

    public Cop(int id, Patch position) {
        this.id = id;
        this.position = position;
        this.position.occupy();
        this.moved = false;
    }

    public int getId() {
        return id;
    }

    public Patch getPosition() {
        return position;
    }

    public boolean isMoved() {
        return moved;
    }

    public void move(Patch targetPosition) {
        this.position.empty();
        this.position = targetPosition;
        this.position.occupy();
        this.moved = true;
    }
}
