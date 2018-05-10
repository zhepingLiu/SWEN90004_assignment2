import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private final static int INITIAL_HEADING = 0;
    private final static int INITIAL_JAIL_TERM = 0;

    private final static double K = 2.3;
    private final static double THRESHOLD = 0.1;

    private int id;
    private Patch position;
    private int heading;

    private double riskAversion;
    private double perceivedHardship;
    private double grievance;
    private double estimatedArrestProbability;

    private boolean active;
    private int jailTerm;
    private boolean moved;

    private Random randomGenerator = new Random();

    public Agent(int id, Patch position) {
        this.id = id;
        this.position = position;
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

    public int getId() {
        return id;
    }

    public Patch getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public int getJailTerm() {
        return jailTerm;
    }

    public void setJailTerm(int jailTerm) {
        this.jailTerm = jailTerm;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = false;
    }

    public void move(Patch targetPosition) {
        this.position = targetPosition;
        this.moved = true;
    }

    public void reportGrievance() {
        this.grievance = this.perceivedHardship *
                (1 - Controller.GOVERNMENT_LEGITIMACY);
    }

    public void reportArrestProbability(int copsCount, int activeCount) {
        activeCount++;
        this.estimatedArrestProbability =
                1 - Math.exp(-K * (copsCount / activeCount));
    }

    public void determineBehaviour(int copsCount, int activeCount) {
        if (jailTerm == 0) {
            reportGrievance();
            reportArrestProbability(copsCount, activeCount);
            if (this.grievance - riskAversion * estimatedArrestProbability
                    > THRESHOLD) {
                this.active = true;
            } else {
                active = false;
            }
        } else {
            jailTerm--;
        }
    }
}
