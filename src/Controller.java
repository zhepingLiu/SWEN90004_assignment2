public class Controller {

    public final static int MAP_LENGTH_X = 40;
    public final static int MAP_HEIGHT_Y = 40;

    public final static double K = 2.3;
    public final static double THRESHOLD = 0.1;

    public final static double MAX_RISK_AVERSION = 1.0;
    public final static double MAX_PERCEIVED_HARDSHIP = 1.0;

    public static double GOVERNMENT_LEGITIMACY = 0.9;
    public static int VISION = 7;

    public static void main(String[] args) {

        Board board = new Board();
        board.printBoard();

    }
}
