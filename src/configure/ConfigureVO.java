package configure;
import com.google.gson.Gson;

/**
 * 
 */
public class ConfigureVO {
	// initial cop density
	double initialCopDensity;
	// initial agent density
	double initialAgentDensity;
	// the vision agents and cops can view
	double vision;
	// the government legitimacy
	double govermentLegitimacy;
	// the max jail term
	int maxJailTerm;
	// the tick time that the model will run
	int tickTime;
	// agents can move or not
	boolean movement;
	// switch of the first extension
	boolean extention1;
	
	public boolean isExtention1() {
		return extention1;
	}
	public void setExtention1(boolean extention1) {
		this.extention1 = extention1;
	}

	public double getInitialCopDensity() {
		return initialCopDensity;
	}

	public void setInitialCopDensity(double initialCopDensity) {
		this.initialCopDensity = initialCopDensity;
	}

	public double getInitialAgentDensity() {
		return initialAgentDensity;
	}

	public void setInitialAgentDensity(double initialAgentDensity) {
		this.initialAgentDensity = initialAgentDensity;
	}

	public double getVision() {
		return vision;
	}

	public void setVision(double vision) {
		this.vision = vision;
	}

	public double getGovermentLegitimacy() {
		return govermentLegitimacy;
	}

	public void setGovermentLegitimacy(double govermentLegitimacy) {
		this.govermentLegitimacy = govermentLegitimacy;
	}

	public int getMaxJailTerm() {
		return maxJailTerm;
	}

	public void setMaxJailTerm(int maxJailTerm) {
		this.maxJailTerm = maxJailTerm;
	}

	public boolean isMovement() {
		return movement;
	}

	public void setMovement(boolean movement) {
		this.movement = movement;
	}

	public int getTickTime() {
		return tickTime;
	}

	public void setTickTime(int tickTime) {
		this.tickTime = tickTime;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
