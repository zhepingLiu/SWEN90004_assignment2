import java.util.Random;

public class Agent implements Character {

    private final static int INITIAL_HEADING = 0;
    private final static int INITIAL_JAIL_TERM = 0;

    private int id;
    private Coordinate position;
    private int heading;

    private double riskAversion;
    private double perceivedHardship;
    private double grievance;
    private double estimatedArrestProbability;

    private boolean active;
    private boolean jailed;
    private int jailTerm;
    private boolean moved;

    private Random randomGenerator = new Random();

    public Agent(int id, Coordinate position) {
        this.id = id;
        this.position = position;
        //this.position.occupy(this);
        this.heading = INITIAL_HEADING;
        this.riskAversion =
                randomGenerator.nextDouble() *
                        Controller.MAX_RISK_AVERSION;
        this.perceivedHardship =
                randomGenerator.nextDouble() *
                        Controller.MAX_PERCEIVED_HARDSHIP;
        this.active = false;
        this.jailTerm = INITIAL_JAIL_TERM;
        this.moved = false;
    }

    public Coordinate getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public void deActive() {
        this.active = false;
    }

    public boolean isJailed() {
        return jailed;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    public int getJailTerm() {
        return jailTerm;
    }

    public void setJailTerm(int jailTerm) {
        this.jailTerm = jailTerm;
    }

    public void decreaseJailTerm() {
        if (jailTerm > 0) {
            jailTerm--;
        }
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

    public void reportGrievance() {
        this.grievance = this.perceivedHardship *
                (1 - Controller.GOVERNMENT_LEGITIMACY);
        //TODO: Extend the model so that government legitimacy increases
        //TODO: as number of jailed agents increases
    }

    public void reportArrestProbability(int copsCount, int activeCount) {
        activeCount++;
        this.estimatedArrestProbability =
                1 - Math.exp(-Controller.K * (copsCount / activeCount));
    }

    public void determineBehaviour(int copsCount, int activeCount) {

        reportGrievance();
        reportArrestProbability(copsCount, activeCount);

        if (this.grievance - riskAversion * estimatedArrestProbability
                > Controller.THRESHOLD) {
            this.active = true;
        } else {
            active = false;
        }
    }
}
