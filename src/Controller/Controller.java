package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;

import configure.ConfigureVO;
import model.Agent;
import model.Coordinate;
import model.Cop;
import model.Empty;
import model.Patch;
import utils.Const;
import utils.PrintUtil;
import utils.RandomUtil;

/**
 * @author Francis
 *
 */
public class Controller {
	Board board;
	ConfigureVO configure;

	/**
	 * Initl the system with configuration in configure.json file.
	 */
	void init() {
		Gson gson = new Gson();
		FileReader reader = null;
		try {
			reader = new FileReader(new File("configure.json"));
			configure = gson.fromJson(reader, ConfigureVO.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		board = new Board(configure);
	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.init();
		controller.go();
	}

	/**
	 * Go function, just like go in Netlogo.
	 */
	private void go() {
		int count = 0;
		ArrayList<ArrayList> datas = new ArrayList<>();

		// Tick_time in configure.json will control how many ticks the model
		// will run
		while (count++ < configure.getTickTime()) {

			// Move to a random site within your vision
			move();
			// Determine if each agent should be active or quiet
			determineBehaviour();			
			// Cops arrest a random active agent within their radius
			enforce();
			// Jailed agents get their term reduced at the end of each clock
			// tick
			reduceJailTerm();

			// Display the all patches. It has been commended.
			// 0 refers to empty, 1 refers to cop
			// 2 refers to quiet agent
			// 3 refers to active agent
			// 4 refers to jailed agent
			// System.out.println();
			 PrintUtil.getInstance().printBoard(board);
			 System.out.println("****************************");

			// Output the data to CSV file
			datas.add(generateData(count, board.getAgents()));
		}
		PrintUtil.getInstance().printCSV(datas, "data.csv");
	}

	/**
	 * calculate the numbers of active quiet and jailed agent
	 * 
	 * @param id
	 * @param agents
	 * @return
	 */
	ArrayList<String> generateData(int id, ArrayList<Agent> agents) {
		int quietCount = 0;
		int activeCount = 0;
		int jailedCount = 0;
		for (Agent agent : agents) {
			switch (agent.getState()) {
			case Const.AGENT_ACTIVE:
				activeCount++;
				break;
			case Const.AGENT_QUIET:
				quietCount++;
				break;
			case Const.AGENT_JAILED:
				jailedCount++;
				break;
			default:
				break;
			}
		}
		ArrayList<String> oneLine = new ArrayList<>();
		// oneLine.add(""+id);
		oneLine.add("" + quietCount);
		oneLine.add("" + activeCount);
		oneLine.add("" + jailedCount);
		return oneLine;
	}

	/**
	 * Reduce jail term of every jailed agent.
	 */
	private void reduceJailTerm() {
		HashMap<String, ArrayList<Agent>> jailedAgents = board.getJailedAgents();
		for (ArrayList<Agent> agents : jailedAgents.values()) {
			if (agents.size() > 0) {
				for (Agent agent : agents) {
					agent.reduceJailTerm();
				}
			}
		}
	}

	/**
	 * Cops arrest a random active agent within their radius and move to the
	 * patch of that agent.
	 */
	private void enforce() {
		Patch[][] patches = board.getPatchs();
		ArrayList<Cop> cops = board.getCops();
		ArrayList<Patch> activeAgents = new ArrayList<>();
		
		Collections.shuffle(cops);

		for (Cop cop : cops) {
			int xCop = cop.getCoordinate().getX();
			int yCop = cop.getCoordinate().getY();

			// Cops find all active agents in their vision.
			for (Coordinate temp : board.getNeighbourhood(xCop, yCop)) {
				if (patches[temp.getX()][temp.getY()] instanceof Agent
						&& patches[temp.getX()][temp.getY()].getState() == Const.AGENT_ACTIVE) {
					activeAgents.add(patches[temp.getX()][temp.getY()]);
				}
			}
			int activeNum = activeAgents.size();
			// if there is active agents
			if (activeNum > 0) {
				// Arrest a random one
				Agent randomAgent = (Agent) activeAgents.get(RandomUtil.getRandomInt(activeNum));
				// Initial Jailterm based on configuration
				randomAgent.setJailTerm(RandomUtil.getRandomInt(configure.getMaxJailTerm()));
				// Set the agent as jailed
				randomAgent.setState(Const.AGENT_JAILED);
				int agentx = randomAgent.getCoordinate().getX();
				int agenty = randomAgent.getCoordinate().getY();
				// Put the agent into jail
				board.putAgentInJail(randomAgent);
				// Cop will move to the patch of that agent.
				cop.setCoordinate(agentx, agenty);
				patches[agentx][agenty] = cop;
				// The original patch of the cop will be empty.
				patches[xCop][yCop] = new Empty(xCop, yCop, configure.getVision());
				activeAgents.clear();
			}
		}
	}

	/**
	 * Jailed agents get their term reduced at the end of each clock tick
	 */
	private void determineBehaviour() {
		Patch[][] patches = board.getPatchs();
		// number for cops around
		int copNumInNeighbour = 0;
		// number for active agents around
		int activeNumInNeighbour = 0;
		
		ArrayList<Agent> agents = board.getAgents();
		// make the determine behaviour randomly.
		Collections.shuffle(agents);
		
		for (Agent agent : agents) {
			if (agent.getState() != Const.AGENT_JAILED) {
				int xAgent = agent.getCoordinate().getX();
				int yAgent = agent.getCoordinate().getY();
				
				for (Coordinate temp : board.getNeighbourhood(xAgent, yAgent)) {
					if (patches[temp.getX()][temp.getY()] instanceof Cop) {
						copNumInNeighbour++;
					} else if (patches[temp.getX()][temp.getY()] instanceof Agent
							&& patches[temp.getX()][temp.getY()].getState() == Const.AGENT_ACTIVE) {
						activeNumInNeighbour++;
					}
				}
				// Agent will determine their behaviour on the number of
				// cops and active agents around.
				agent.determineBehaviour(copNumInNeighbour, activeNumInNeighbour);
			}
		}
	}

	/**
	 * Move to a random site within your vision
	 */
	private void move() {
		Patch[][] patches = board.getPatchs();
		ArrayList<Coordinate> emptyPatches = new ArrayList<Coordinate>();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (patches[i][j] instanceof Empty)
					// Gather all patches which are empty
					emptyPatches.add(new Coordinate(i, j));

			}
		}
		// move once in one tick time
		moveOneTick(emptyPatches, patches);
		resetMoveState(patches);
	}

	/**
	 * Set the move state of all agent and cops as false for the next tick time
	 * to move.
	 * 
	 * @param patches
	 */
	private void resetMoveState(Patch[][] patches) {
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				patches[i][j].setMoved(false);
			}
		}
	}

	/**
	 * Step 1 : Move jailed agent firstly if the agent has just been released.
	 * Step 2 : Then move a random cop or agent in the vision of the empty patch
	 * to the patch. Step 3 : Store the new empty patch to the list and repeat
	 * from step 1 until there is no more empty patch in the list
	 * 
	 * @param emptyPatches
	 * @param patches
	 */
	private void moveOneTick(ArrayList<Coordinate> emptyPatches, Patch[][] patches) {
		int count = 0;
		ArrayList<Patch> CopsOrAgents = new ArrayList<>();
		// if the no more empty patch can be used then stop the loop
		while (!emptyPatches.isEmpty()) {
			// get a random empty patch from the list.
			Coordinate current = emptyPatches.get(RandomUtil.getRandomInt(emptyPatches.size()));
			int currentX = current.getX();
			int currentY = current.getY();
			Agent agentReleased = null;
			// find all neighbourhood within the patch
			for (Coordinate temp : board.getNeighbourhood(currentX, currentY)) {
				Patch patch = patches[temp.getX()][temp.getY()];
				// if agent cannot move and the jailed agent will be released,
				// then the agent need be
				// revealed in the patch
				if (!configure.isMovement() && patches[temp.getX()][temp.getY()] instanceof Empty) {
					agentReleased = board.getReleasedAgent(temp);
					// Agents can move, just find a jailed agent who will be
					// released.
				} else if (configure.isMovement()) {
					agentReleased = board.getReleasedAgent(temp);
				}
				// If there is a jailed agent will be released, then break the
				// loop
				if (agentReleased != null) {
					break;
				}
				// If there is no such agent can be found then add all agents
				// and cops within the neighbourhood
				// to the CopsOrAgents list
				if ((configure.isMovement() && patch instanceof Agent) || patch instanceof Cop) {
					CopsOrAgents.add(patch);
				}

			}
			// System.out.println(emptyPatches.size()+" *");
			// System.out.println(CopsOrAgents.size());
			// if any agent is just released from the jail and agent cannot
			// move, reveal it in the board
			if (!configure.isMovement() && agentReleased != null) {
				((Agent) agentReleased).setState(Const.AGENT_QUIET);
				patches[agentReleased.getCoordinate().getX()][agentReleased.getCoordinate().getY()] = agentReleased;
				removeEmptyPatchInList(emptyPatches, agentReleased.getCoordinate().getX(),
						agentReleased.getCoordinate().getY());
				continue;
			}
			// if any agent is just released from the jail and agents can move,
			// then move it to the empty patch.
			if (configure.isMovement() && agentReleased != null) {
				((Agent) agentReleased).setState(Const.AGENT_QUIET);
				agentReleased.setMoved(true);
				agentReleased.setCoordinate(currentX, currentY);
				patches[currentX][currentY] = agentReleased;
				continue;
			}
			// Move a random cop or agent in CopsOrAgents list to the empty
			// patch
			while (CopsOrAgents.size() > 0) {
				int randomOne = RandomUtil.getRandomInt(CopsOrAgents.size());
				Patch patch = CopsOrAgents.get(randomOne);
				// If the agent or the cop has not moved yet, then move it.
				if (!patch.getMove()) {
					int patchX = patch.getCoordinate().getX();
					int patchY = patch.getCoordinate().getY();
					patch.setMoved(true);
					patch.setCoordinate(currentX, currentY);
					patches[currentX][currentY] = patch;
					// The original patch will be empty after the agent or the
					// cop moved,
					// add the new empty patch to the list waiting for another
					// one to move in.
					Empty newEmpty = new Empty(patchX, patchY, configure.getVision());
					patches[patchX][patchY] = newEmpty;
					emptyPatches.add(newEmpty.getCoordinate());
					break;
					// If the agent or cop has moved once already, remove it
					// from the list
				} else {
					CopsOrAgents.remove(patch);
				}
			}
			emptyPatches.remove(current);
			CopsOrAgents.clear();
		}
	}

	/**
	 * If the jailed agent has been released in the board, the no one can move
	 * into the patch anymore.
	 * 
	 * @param emptyPatches
	 * @param x
	 * @param y
	 */
	private void removeEmptyPatchInList(ArrayList<Coordinate> emptyPatches, int x, int y) {
		Iterator<Coordinate> iterable = emptyPatches.iterator();
		while (iterable.hasNext()) {
			Coordinate temp = iterable.next();
			if (temp.equals(new Coordinate(x, y))) {
				emptyPatches.remove(temp);
				return;
			}
		}

	}

}
