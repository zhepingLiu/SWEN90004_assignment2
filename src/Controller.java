import java.util.ArrayList;
import java.util.Random;

public class Controller {

    public final static int MAP_LENGTH_X = 5;
    public final static int MAP_HEIGHT_Y = 5;

    public final static double K = 2.3;
    public final static double THRESHOLD = 0.1;

    public final static boolean MOVEMENT = true;

    public final static double AGENT_DENSITY = 0.8;
    public final static double COP_DENSITY = 0.1;

    public final static int MAX_JAIL_TERM = 5;

    public final static double MAX_RISK_AVERSION = 1.0;
    public final static double MAX_PERCEIVED_HARDSHIP = 1.0;

    public static double GOVERNMENT_LEGITIMACY = 0.7;
    public static int VISION = 4;

    private static Random randomGenerator = new Random();

    public static void main(String[] args) throws InterruptedException {

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

        int tick = 0;

        //print the board at tick 0
        System.out.println("This is tick : " + tick);
        board.printBoard();

        //TODO: Step 1 : move cops and agents not in jail
        while (true) {

            for (Cop c : cops) {
                //reset moved back to false at the beginning of every tick
                c.setMoved(false);
                for (Patch p : c.getPosition().getNeighbourhood()) {
                    if (!p.isOccupied()) {
                        c.move(p);
                        break;
                    }
                }
            }

            if (MOVEMENT) {
                for (Agent a : agents) {
                    //reset moved back to false at the beginning of every tick
                    a.setMoved(false);
                    if (!a.isJailed()) {
                        //occupy the patch in case the agents are just released
                        //from the jail
                        a.getPosition().occupy(a);
                        for (Patch p : a.getPosition().getNeighbourhood()) {
                            if (!p.isOccupied()) {
                                a.move(p);
                                break;
                            }
                        }
                    }
                }
            }

            //TODO: Step 2 : determine behaviour of all agents
            for (Agent a : agents) {
                int copsCount = 0;
                int activeCount = 0;

                for (Patch p : a.getPosition().getNeighbourhood()) {
                    if (p.isOccupied()) {
                        //count number of cops and active agents
                        if (p.getCharacter() instanceof Cop) {
                            copsCount++;
                        } else if (p.getCharacter() instanceof Agent &&
                                ((Agent) p.getCharacter()).isActive()) {
                            activeCount++;
                        }
                    }
                }

                a.determineBehaviour(copsCount, activeCount);
            }

            //TODO: Step 3 : all cops enforce
            for (Cop c : cops) {
                ArrayList<Character> activeAgentsInNeighbour =
                        new ArrayList<>();

                for (Patch p : c.getPosition().getNeighbourhood()) {
                    if (p.isOccupied() &&
                            p.getCharacter() instanceof Agent &&
                            ((Agent) p.getCharacter()).isActive()) {

                        activeAgentsInNeighbour.add(p.getCharacter());
                    }
                }

                if (activeAgentsInNeighbour.size() > 0) {
                    int randomInt = randomGenerator.nextInt
                            (activeAgentsInNeighbour.size());

                    Agent target = (Agent) activeAgentsInNeighbour.
                            get(randomInt);

                    target.setJailTerm
                            (randomGenerator.nextInt(MAX_JAIL_TERM));
                    //empty the patch
                    target.getPosition().empty();
                    target.getPosition().increaseJailNumber();

                    //move to the jailed agent
                    c.move(target.getPosition());
                }
            }

            tick++;
            System.out.println("This is tick : " + tick);
            board.printBoard();
            Thread.sleep(1000);
        }
    }
}
