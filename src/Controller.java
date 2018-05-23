import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Controller {

    public final static int MAP_LENGTH_X = 40;
    public final static int MAP_HEIGHT_Y = 40;

    public final static double K = 2.3;
    public final static double THRESHOLD = 0.1;

    public final static boolean MOVEMENT = false;

    public final static double AGENT_DENSITY = 0.7;
    public final static double COP_DENSITY = 0.04;

    public final static int MAX_JAIL_TERM = 30;

    public final static double MAX_RISK_AVERSION = 1.0;
    public final static double MAX_PERCEIVED_HARDSHIP = 1.0;

    public static double INITIAL_GOVERNMENT_LEGITIMACY = 0.62;
    public static double GOVERNMENT_LEGITIMACY = INITIAL_GOVERNMENT_LEGITIMACY;
    public static int VISION = 7;

    public final static int MAX_TICK = 1000;

    private static Random randomGenerator = new Random();

    //Controller variables
    private static Board board;
    private static ArrayList<Cop> cops;
    private static ArrayList<Agent> agents;
    private static ArrayList<Character> chars;

    public static void main(String[] args) {

        // an ArrayList contains activeCount, quietCount,
        // jailedCount at each tick
        ArrayList<ArrayList> data = new ArrayList<>();

        init();

        int tick = 0;

        while (tick < MAX_TICK) {

            // Step 1 : move cops and agents not in jail
            move();

            // Step 2 : determine behaviour of all agents
            determineBehaviour();

            // Step 3 : all cops enforce
            enforce();

            // Step 4 : update jail terms of all jailed agents
            for (Agent a : agents) {
                if (a.isJailed() && a.getJailTerm() > 0) {
                    a.decreaseJailTerm();
                }

                if (a.isJailed() && a.getJailTerm() == 0) {
                    a.setJailed(false);

                    // reoccupy the patch before jailed
                    board.retrievePatch(a.getPosition().getPositionX(),
                            a.getPosition().getPositionY()).occupy(a);

                    // decrease the jailed number of that patch
                    board.retrievePatch(a.getPosition().getPositionX(),
                            a.getPosition().getPositionY()).
                            decreaseJailNumber();
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

            // Extension of the model
            //updateGovernmentLegitimacy(jailedCount, numberOfAgents);

            tick++;

            //TODO Count all people on the board
            int totalPeopleOnBoard = 0;
            int totalAgentOnBoard = 0;
            int totalCopOnBoard = 0;
            int totalJailCount = 0;
            for (Patch p : board.getPatches().values()) {
                for (Character c : p.getCharacter()) {
                    totalPeopleOnBoard++;
                    if (c instanceof Agent) {
                        totalAgentOnBoard++;
                    } else {
                        totalCopOnBoard++;
                    }
                }

                totalJailCount += p.getJailNumber();
            }

//            System.out.println("========================");
//            System.out.println("Tick : " + tick);
//            System.out.println("People " + totalPeopleOnBoard);
//            System.out.println("Agent " + totalAgentOnBoard);
//            System.out.println("Cop " + totalCopOnBoard);
//            System.out.println("Jail " + totalJailCount);

            ArrayList<Integer> dataLine = new ArrayList<>();

            dataLine.add(tick);
            dataLine.add(quietCount);
            dataLine.add(activeCount);
            dataLine.add(jailedCount);

            //TODO Testing
            dataLine.add(totalPeopleOnBoard);
            dataLine.add(totalAgentOnBoard);
            dataLine.add(totalCopOnBoard);
            dataLine.add(totalJailCount);

            data.add(dataLine);
        }
        printCSV(data, "rebellion.csv");
    }


    private static void init() {
        // create board
        board = new Board();
        cops = new ArrayList<>();
        agents = new ArrayList<>();
        chars = new ArrayList<>();

        LinkedList<Patch> emptyPatches = new LinkedList<>();
        emptyPatches.addAll(board.getPatches().values());

        // create cops
        int numberOfCops = (int)(COP_DENSITY * MAP_LENGTH_X * MAP_HEIGHT_Y);

        for (int i=0;i<numberOfCops;i++) {

            Patch targetPatch = emptyPatches.poll();

            Cop cop = new Cop(i, targetPatch.getCoordinate());
            cops.add(cop);
            board.retrievePatch(targetPatch.getCoordinate().getPositionX(),
                    targetPatch.getCoordinate().getPositionY()).occupy(cop);
        }

        // create agents
        int numberOfAgents = (int)(AGENT_DENSITY * MAP_LENGTH_X * MAP_HEIGHT_Y);

        for (int i=0;i<numberOfAgents;i++) {

            Patch targetPatch = emptyPatches.poll();

            Agent agent = new Agent(i, targetPatch.getCoordinate());
            agents.add(agent);
            board.retrievePatch(targetPatch.getCoordinate().getPositionX(),
                    targetPatch.getCoordinate().getPositionY()).occupy(agent);
        }

        // add all cops and agents into chars
        chars.addAll(cops);
        chars.addAll(agents);
    }

    private static void move() {

//        LinkedList<Patch> emptyPatches = new LinkedList<>();

//        for (Patch p : board.getPatches().values()) {
//            if (!p.isOccupied()) {
//                emptyPatches.add(p);
//            }
//        }

        for (Character c : chars) {
            c.setMoved(false);
        }

        ArrayList<Patch> candidates = new ArrayList<>();

        // when both agents and cops can move
        if (MOVEMENT) {
            // randomly shuffle the chars
            Collections.shuffle(chars);
            for (Character c : chars) {
                //reset moved back to false at the beginning of
                // every tick
                c.setMoved(false);

                // break the iteration if c is a jailed agent
                if (c instanceof Agent && ((Agent) c).isJailed()) {
                    break;
                }

                int x = c.getPosition().getPositionX();
                int y = c.getPosition().getPositionY();

                for (Patch p : board.retrievePatch(x, y).getNeighbourhood())
                {
                    if (!p.isOccupied()) {
                        candidates.add(p);
                    }
                }

                if (!candidates.isEmpty()) {
                    int randomIndex =
                            randomGenerator.nextInt(candidates.size());
                    Patch target = candidates.get(randomIndex);
                    Coordinate targetCoordinate = target.getCoordinate();

                    board.retrievePatch(x, y).empty(c);
                    c.move(targetCoordinate);
                    board.retrievePatch(targetCoordinate.getPositionX(),
                            targetCoordinate.getPositionY()).occupy(c);
                }
            }
        // when only cops can move (i.e. agents cannot move)
        } else {
            // randomly shuffle the cops
            Collections.shuffle(cops);
            for (Cop c : cops) {
                //reset moved back to false at the beginning of every tick
                c.setMoved(false);

                if (!c.isMoved()) {
                    int x = c.getPosition().getPositionX();
                    int y = c.getPosition().getPositionY();

                    for (Patch p : board.retrievePatch(x, y).getNeighbourhood())
                    {
                        if (!p.isOccupied()) {
                            candidates.add(p);
                        }
                    }

                    if (!candidates.isEmpty()) {
                        int randomIndex =
                                randomGenerator.nextInt(candidates.size());
                        Patch target = candidates.get(randomIndex);
                        Coordinate targetCoordinate = target.getCoordinate();

                        board.retrievePatch(x, y).empty(c);
                        c.move(targetCoordinate);
                        board.retrievePatch(targetCoordinate.getPositionX(),
                                targetCoordinate.getPositionY()).occupy(c);
                    }
                }
            }
        }
    }

    private static void determineBehaviour() {
        Collections.shuffle(agents);
        for (Agent a : agents) {
            if (!a.isJailed()) {
                int copsCount = 0;
                int activeCountNeighbour = 0;

                int x = a.getPosition().getPositionX();
                int y = a.getPosition().getPositionY();

                for (Patch p : board.retrievePatch(x, y).getNeighbourhood())
                {
                    if (p.isOccupied()) {
                        //count number of cops and active agents
                        for (Character c : p.getCharacter()) {
                            if (c instanceof Cop) {
                                copsCount++;
                            } else if (c instanceof Agent &&
                                    ((Agent) c).isActive()) {
                                activeCountNeighbour++;
                            }
                        }
                    }
                }
                a.determineBehaviour(copsCount, activeCountNeighbour);
            }
        }
    }

    private static void enforce() {
        Collections.shuffle(cops);
        for (Cop c : cops) {
            // an ArrayList store all active agents in the vision of current cop
            ArrayList<Character> activeAgentsInNeighbour =
                    new ArrayList<>();

            int x = c.getPosition().getPositionX();
            int y = c.getPosition().getPositionY();

            // iterate through all patches in vision
            for (Patch p : board.retrievePatch(x, y).getNeighbourhood())
            {
                // if there is character on that patch
                if (p.isOccupied()) {
                    // iterate through all characters on that patch
                    for (Character ch : p.getCharacter()) {
                        // if the character is an Agent and is active
                        if (ch instanceof Agent && ((Agent) ch).isActive()) {
                            // add this agent to the arraylist
                            activeAgentsInNeighbour.add(ch);
                        }
                    }
                }
            }

            // when there are active agents in the vision
            if (!activeAgentsInNeighbour.isEmpty()) {

                int randomInt = randomGenerator.nextInt
                        (activeAgentsInNeighbour.size());

                Agent target = (Agent) activeAgentsInNeighbour.
                        get(randomInt);

                target.setJailTerm(randomGenerator.nextInt(MAX_JAIL_TERM));
                target.setJailed(true);
                target.deActive();

                int targetX = target.getPosition().getPositionX();
                int targetY = target.getPosition().getPositionY();

                // remove target agent from the patch
                board.retrievePatch(targetX, targetY).empty(target);
                // increase the jail number
                board.retrievePatch(targetX, targetY).increaseJailNumber();
                // remove cops from the previous patch
                board.retrievePatch(x, y).empty(c);
                // move to the jailed agent (change the coordinate stored in character)
                c.move(target.getPosition());
                // occupy the target patch
                board.retrievePatch(targetX, targetY).occupy(c);
            }
        }
    }

    /**
     * Extension: Government legitimacy will increase proportionally as the
     * number of jailed agents increase
     * @param jailedCount number of jailed agents
     * @param numberOfAgents total number of agents
     */
    private static void updateGovernmentLegitimacy(int jailedCount,
                                                  int numberOfAgents)
    {
        // Update the government legitimacy here
        double jailedRatio = (double) jailedCount / numberOfAgents;
        GOVERNMENT_LEGITIMACY = INITIAL_GOVERNMENT_LEGITIMACY +
                jailedRatio * (1 - INITIAL_GOVERNMENT_LEGITIMACY);
    }

    public static void printCSV(ArrayList<ArrayList> datas, String fileName) {
        //Delimiter used in CSV file
        final String NEW_LINE_SEPARATOR = "\n";

        //CSV file header
        final Object [] FILE_HEADER = {"tick","quiet","active","jailed",
                "totalPeople","totalAgent","totalCop","totalJailCount"};

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

            System.out.println("CSV file was created successfully");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing " +
                        "           fileWriter/csvPrinter");
                e.printStackTrace();
            }
        }

    }
}
