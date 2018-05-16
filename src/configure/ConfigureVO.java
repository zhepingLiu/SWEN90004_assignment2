package configure;

import com.google.gson.Gson;

import sun.launcher.resources.launcher;

public class ConfigureVO {
	/**
	 * initial cop density
	 */
	double initial_cop_density;
	/**
	 * initial agent density
	 */
	double initial_agent_density;
	/**
	 * the vision 
	 */
	double vision;
	/**
	 * the government legitimacy
	 */
	double goverment_legitimacy;
	/**
	 * the max jail term
	 */
	int max_jail_term;
	/**
	 * the tick time that the model will run
	 */
	int tick_time;
	/**
	 * agents can move or not
	 */
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
	

	public int getTick_time() {
		return tick_time;
	}
	public void setTick_time(int tick_time) {
		this.tick_time = tick_time;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
}
