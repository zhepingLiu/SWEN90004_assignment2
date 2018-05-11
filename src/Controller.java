import java.util.ArrayList;
import java.util.Random;

public class Controller {

    public final static int MAP_LENGTH_X = 40;
    public final static int MAP_HEIGHT_Y = 40;

    public final static double K = 2.3;
    public final static double THRESHOLD = 0.1;

    public final static double AGENT_DENSITY = 0.8;
    public final static double COP_DENSITY = 0.1;

    public final static double MAX_RISK_AVERSION = 1.0;
    public final static double MAX_PERCEIVED_HARDSHIP = 1.0;

    public static double GOVERNMENT_LEGITIMACY = 0.9;
    public static int VISION = 7;

    private static Random randomGenerator = new Random();

    public static void main(String[] args) {
        //create board
        Board board = new Board();

        ArrayList<Cop> cops = new ArrayList<>();
        ArrayList<Agent> agents = new ArrayList<>();

        //create cops
        int numberOfCops = (int)(COP_DENSITY * MAP_LENGTH_X * MAP_HEIGHT_Y);

        for (int i=0;i<numberOfCops;i++) {
            int x = randomGenerator.nextInt(MAP_LENGTH_X);
            int y = randomGenerator.nextInt(MAP_HEIGHT_Y);

            while (board.retrievePatch(x, y).isOccupied()) {
                 x = randomGenerator.nextInt(MAP_LENGTH_X);
                 y = randomGenerator.nextInt(MAP_HEIGHT_Y);
            }

            Cop cop = new Cop(i, board.retrievePatch(x, y));
            cops.add(cop);
        }

        //create agents
        int numberOfAgents = (int)(AGENT_DENSITY * MAP_LENGTH_X * MAP_HEIGHT_Y);

        for (int i=0;i<numberOfAgents;i++) {
            int x = randomGenerator.nextInt(MAP_LENGTH_X);
            int y = randomGenerator.nextInt(MAP_HEIGHT_Y);
            while (board.retrievePatch(x, y).isOccupied()) {
                x = randomGenerator.nextInt(MAP_LENGTH_X);
                y = randomGenerator.nextInt(MAP_HEIGHT_Y);
            }

            Agent agent = new Agent(i, board.retrievePatch(x, y));
            agents.add(agent);
        }

        //TODO: to GO
        //TODO: update the state and behaviour of agents and cops

    }
}
