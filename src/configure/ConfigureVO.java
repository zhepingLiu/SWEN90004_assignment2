package configure;

import com.google.gson.Gson;

import sun.launcher.resources.launcher;

public class ConfigureVO {
	double initial_cop_density;
	double initial_agent_density;
	double vision;
	double goverment_legitimacy;
	int max_jail_term;
	boolean movement ;
	public double getInitial_cop_density() {
		return initial_cop_density;
	}
	public void setInitial_cop_density(double initial_cop_density) {
		this.initial_cop_density = initial_cop_density;
	}
	public double getInitial_agent_density() {
		return initial_agent_density;
	}
	public void setInitial_agent_density(double initial_agent_density) {
		this.initial_agent_density = initial_agent_density;
	}
	public double getVision() {
		return vision;
	}
	public void setVision(double vision) {
		this.vision = vision;
	}
	public double getGoverment_legitimacy() {
		return goverment_legitimacy;
	}
	public void setGoverment_legitimacy(double goverment_legitimacy) {
		this.goverment_legitimacy = goverment_legitimacy;
	}
	public int getMax_jail_term() {
		return max_jail_term;
	}
	public void setMax_jail_term(int max_jail_term) {
		this.max_jail_term = max_jail_term;
	}
	public boolean isMovement() {
		return movement;
	}
	public void setMovement(boolean movement) {
		this.movement = movement;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
}
