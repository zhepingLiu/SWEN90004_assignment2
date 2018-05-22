import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Controller {

    public final static int MAP_LENGTH_X = 40;
    public final static int MAP_HEIGHT_Y = 40;

    public final static double K = 2.3;
    public final static double THRESHOLD = 0.1;

    public final static boolean MOVEMENT = true;

    public final static double AGENT_DENSITY = 0.95;
    public final static double COP_DENSITY = 0.04;

    public final static int MAX_JAIL_TERM = 30;

    public final static double MAX_RISK_AVERSION = 1.0;
    public final static double MAX_PERCEIVED_HARDSHIP = 1.0;

    public static double INITIAL_GOVERNMENT_LEGITIMACY = 0.62;
    public static double GOVERNMENT_LEGITIMACY = INITIAL_GOVERNMENT_LEGITIMACY;
    public static int VISION = 7;

    private static Random randomGenerator = new Random();

    public static void main(String[] args) {

        ArrayList<ArrayList> data = new ArrayList<>();

        //create board
        Board board = new Board();

        ArrayList<Cop> cops = new ArrayList<>();
        ArrayList<Agent> agents = new ArrayList<>();

        //create cops
        int numberOfCops = (int)(COP_DENSITY * MAP_LENGTH_X * MAP_HEIGHT_Y);
        System.out.println(numberOfCops);

        for (int i=0;i<numberOfCops;i++) {

            int x = randomGenerator.nextInt(MAP_LENGTH_X);
            int y = randomGenerator.nextInt(MAP_HEIGHT_Y);

            while (board.retrievePatch(x, y).isOccupied()) {
                 x = randomGenerator.nextInt(MAP_LENGTH_X);
                 y = randomGenerator.nextInt(MAP_HEIGHT_Y);
            }

            Cop cop = new Cop(i, new Coordinate(x, y));
            cops.add(cop);
            board.retrievePatch(x, y).occupy(cop);
        }

        //create agents
        int numberOfAgents = (int)(AGENT_DENSITY * MAP_LENGTH_X * MAP_HEIGHT_Y);
        System.out.println(numberOfAgents);

        for (int i=0;i<numberOfAgents;i++) {
            int x = randomGenerator.nextInt(MAP_LENGTH_X);
            int y = randomGenerator.nextInt(MAP_HEIGHT_Y);

            while (board.retrievePatch(x, y).isOccupied()) {
                x = randomGenerator.nextInt(MAP_LENGTH_X);
                y = randomGenerator.nextInt(MAP_HEIGHT_Y);
            }

            Agent agent = new Agent(i, new Coordinate(x, y));
            agents.add(agent);
            board.retrievePatch(x, y).occupy(agent);
        }

        int tick = 0;

        //TODO: Step 1 : move cops and agents not in jail
        while (tick < 1000) {

            if (MOVEMENT) {
                Collections.shuffle(agents);
                for (int i=0;i<2;i++) {
                    for (Agent a : agents) {
                        ArrayList<Patch> candidates = new ArrayList<>();
                        //reset moved back to false at the beginning of every tick
                        a.setMoved(false);

                        if (!a.isJailed() && !a.isMoved()) {
                            int x = a.getPosition().getPositionX();
                            int y = a.getPosition().getPositionY();

                            for (Patch p : board.retrievePatch(x, y).
                                    getNeighbourhood()) {
                                if (!p.isOccupied()) {
                                    candidates.add(p);
                                }
                            }

                            if (!candidates.isEmpty()) {
                                int randomIndex = randomGenerator.
                                        nextInt(candidates.size());

                                a.move(candidates.get(randomIndex).
                                        getCoordinate());
                                board.retrievePatch(x, y).occupy(a);
                            }
                        }
                    }
                }
            }

            Collections.shuffle(cops);
            for (Cop c : cops) {
                ArrayList<Patch> candidates = new ArrayList<>();
                //reset moved back to false at the beginning of every tick
                c.setMoved(false);
                for (int i=0;i<2;i++) {

                    if (!c.isMoved()) {
                        int x = c.getPosition().getPositionX();
                        int y = c.getPosition().getPositionY();

                        for (Patch p : board.retrievePatch(x, y).
                                                getNeighbourhood())
                        {
                            if (!p.isOccupied()) {
                                candidates.add(p);
                            }
                        }

                        if (!candidates.isEmpty()) {
                            int randomIndex =
                                    randomGenerator.nextInt(candidates.size());

                            c.move(candidates.get(randomIndex).getCoordinate());
                            board.retrievePatch(x, y).occupy(c);
                        }
                    }
                }
            }

            //TODO: Step 2 : determine behaviour of all agents
            Collections.shuffle(agents);
            for (Agent a : agents) {
                if (!a.isJailed()) {
                    int copsCount = 0;
                    int activeCountNeighbour = 0;

                    int x = a.getPosition().getPositionX();
                    int y = a.getPosition().getPositionY();

                    for (Patch p : board.retrievePatch(x, y).
                            getNeighbourhood())
                    {
                        if (p.isOccupied()) {
                            //count number of cops and active agents
                            if (p.getCharacter() instanceof Cop)
                            {
                                copsCount++;
                            } else if (p.getCharacter() instanceof Agent &&
                                    ((Agent) p.getCharacter()).isActive())
                            {
                                activeCountNeighbour++;
                            }
                        }
                    }
                    a.determineBehaviour(copsCount, activeCountNeighbour);
                }
            }

            //TODO: Step 3 : all cops enforce
            Collections.shuffle(cops);
            for (Cop c : cops) {
                ArrayList<Character> activeAgentsInNeighbour =
                        new ArrayList<>();

                int x = c.getPosition().getPositionX();
                int y = c.getPosition().getPositionY();

                for (Patch p : board.retrievePatch(x, y).getNeighbourhood())
                {
                    if (p.isOccupied() &&
                            p.getCharacter() instanceof Agent &&
                            ((Agent) p.getCharacter()).isActive())
                    {
                        activeAgentsInNeighbour.add(p.getCharacter());
                    }
                }

                if (activeAgentsInNeighbour.size() > 0) {
                    int randomInt = randomGenerator.nextInt
                            (activeAgentsInNeighbour.size());

                    Agent target = (Agent) activeAgentsInNeighbour.
                            get(randomInt);

                    target.setJailTerm(randomGenerator.nextInt(MAX_JAIL_TERM));
                    target.setJailed(true);
                    target.deActive();

                    int targetX = target.getPosition().getPositionX();
                    int targetY = target.getPosition().getPositionY();

                    //empty the patch
                    board.retrievePatch(targetX, targetY).empty();
                    board.retrievePatch(targetX, targetY).increaseJailNumber();

                    //move to the jailed agent
                    c.move(target.getPosition());
                }
            }

            //TODO: Step 4 : update jail terms of all jailed agents
            for (Agent a : agents) {
                if (a.isJailed() && a.getJailTerm() > 0) {
                    a.decreaseJailTerm();
                }

                if (a.isJailed() && a.getJailTerm() == 0) {
                    a.setJailed(false);
                    a.getPosition();
                }
            }

            int quietCount = 0;
            int activeCount = 0;
            int jailedCount = 0;

            for (Agent a : agents) {
                if (a.isJailed()) {
                    jailedCount++;
                }
                else if (a.isActive()) {
                    activeCount++;
                }
                else {
                    quietCount++;
                }
            }

            //updateGovernmentLegitimacy(jailedCount, numberOfAgents);

            tick++;

            ArrayList<Integer> dataNumber = new ArrayList<>();

            dataNumber.add(tick);
            dataNumber.add(quietCount);
            dataNumber.add(activeCount);
            dataNumber.add(jailedCount);
            data.add(dataNumber);
        }
        printCSV(data, "rebellion.csv");
    }

    /**
     * Extension: Government legitimacy will increase proportionally as the
     * number of jailed agents increase
     * @param jailedCount number of jailed agents
     * @param numberOfAgents total number of agents
     */
    public static void updateGovernmentLegitimacy(int jailedCount,
                                                  int numberOfAgents)
    {
        //TODO: Update the government_legitimacy here
        double jailedRatio = (double) jailedCount / numberOfAgents;
        GOVERNMENT_LEGITIMACY = INITIAL_GOVERNMENT_LEGITIMACY +
                jailedRatio * (1 - INITIAL_GOVERNMENT_LEGITIMACY);
    }

    public static void printCSV(ArrayList<ArrayList> datas, String fileName) {
        //Delimiter used in CSV file
        final String NEW_LINE_SEPARATOR = "\n";

        //CSV file header
        final Object [] FILE_HEADER = {"tick","quiet","active","jailed"};

        FileWriter fileWriter = null;

        CSVPrinter csvFilePrinter = null;

        //Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat =
                CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(fileName);

            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            //Create CSV file header
            csvFilePrinter.printRecord(FILE_HEADER);

            //Write a new student object list to the CSV file
            for (ArrayList list : datas) {
                csvFilePrinter.printRecord(list);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing " +
                        "           fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }

    }
}
