package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
 * Controller class controls the whole model
 */
public class Controller {
	Board board;
	ConfigureVO configure;

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.init();
		controller.go();
	}

	/**
	 * Initl the system with configuration in configure.json file. 
	 */
	private void init() {
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
	
	/**
	 * Go function, just like go in Netlogo.
	 */
	private void go() {

		int tick = 0;
		ArrayList<ArrayList> datas = new ArrayList<>();
		
		// tickTime in configure.json will control how many ticks the model will 
		// run
		while (tick++ < configure.getTickTime()) {

			// Move to a random site within your vision
			move();
			// Determine if each agent should be active or quiet
			determineBehaviour();
			//Cops arrest a random active agent within their radius
			enforce();
			//Jailed agents get their term reduced at the end of each clock tick
			reduceJailTerm();
			
			// Display the all patches. It has been commended.
			// 0 refers to empty, 1 refers to cop
			// 2 refers to quiet agent
			// 3 refers to active agent
			// 4 refers to jailed agent
//			System.out.println();
//			PrintUtil.getInstance().printBoard(board.getPatchs());
//			System.out.println("****************************");

			// Output the data to CSV file
			datas.add(generateData(tick, board.getAgents()));
		}
		PrintUtil.getInstance().printCSV(datas, "data.csv");
	}

	/**
	 * calculate the numbers of active, quiet and jailed agent
	 * @param agents
	 * @return
	 */
	ArrayList<String> generateData(int tick, ArrayList<Agent> agents) {
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

			//TODO: What's the default case? By Zheping
			default:
				break;
			}
		}

		ArrayList<String> oneLine = new ArrayList<>();

		oneLine.add("" + tick);
		oneLine.add("" + quietCount);
		oneLine.add("" + activeCount);
		oneLine.add("" + jailedCount);

		return oneLine;
	}

	/**
	 * Reduce jail term of every jailed agent.
	 */
	private void reduceJailTerm() {
		Patch[][][] patches = board.getPatchs();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				Patch patch = patches[0][i][j];
				if (patch.isAnyAgentsJailed()) {
					patch.reduceJailTerm();
				}
			}
		}
	}

	/**
	 * Cops arrest a random active agent within their radius
	 * and move to the patch of that agent.
	 */
	private void enforce() {
		Patch[][][] patches = board.getPatchs();
		ArrayList<Patch> activeAgents = new ArrayList<>();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (patches[1][i][j] instanceof Cop) {
					Cop cop = (Cop) patches[1][i][j];
					// Cops find all active agents in their vision.
					for (Coordinate temp : board.getNeighbourhood(i, j)) {
						if (patches[1][temp.getX()][temp.getY()] 
							instanceof Agent && 
							patches[1][temp.getX()][temp.getY()].getState() 
								== Const.AGENT_ACTIVE) 
						{
							activeAgents.
								add(patches[1][temp.getX()][temp.getY()]);
						}
					}

					int activeNum = activeAgents.size();

					// if there is active agents
					if (activeNum > 0) {
						// Arrest a random one
						Agent randomAgent = (Agent) activeAgents.
									get(RandomUtil.getRandomInt(activeNum));
						// Initial Jailterm based on configuration
						randomAgent.setJailTerm(RandomUtil.
									getRandomInt(configure.getMaxJailTerm()));
						// Set the agent as jailed
						randomAgent.setState(Const.AGENT_JAILED);
						int agentx = randomAgent.getCoordinate().getX();
						int agenty = randomAgent.getCoordinate().getY();

						// Put the agent into jail
						patches[0][agentx][agenty].putAgentInJail(randomAgent);
						
						// Cop will move to the patch of that agent.
						cop.setCoordinate(agentx, agenty);

						patches[1][agentx][agenty] = cop;

						// The original patch of the cop will be empty.
						patches[1][i][j] = 
							new Empty(i, j, configure.getVision());

						activeAgents.clear();
					}
				}
			}
		}
	}

	/**
	 * Jailed agents get their term reduced at the end of each clock tick
	 */
	private void determineBehaviour() {
		Patch[][][] patches = board.getPatchs();
		// number for cops around
		int copNumInNeighbour = 0;
		// number for active agents around
		int activeNumInNeighbour = 0;
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (patches[1][i][j] instanceof Agent) {
					for (Coordinate temp : board.getNeighbourhood(i, j)) {
						if (patches[1][temp.getX()][temp.getY()] instanceof Cop) 
						{
							copNumInNeighbour++;
						} else if (patches[1][temp.getX()][temp.getY()] 
							instanceof Agent && 
							patches[1][temp.getX()][temp.getY()].getState() 
								== Const.AGENT_ACTIVE) 
						{
							activeNumInNeighbour++;
						}
					}

					// Agent will determine their behaviour on the number of 
					// cops and active agents around.
					((Agent) patches[1][i][j]).
					determineBehaviour(copNumInNeighbour, activeNumInNeighbour);
				}
			}
		}

	}

	/**
	 *  Move to a random site within your vision
	 */
	private void move() {
		Patch[][][] patches = board.getPatchs();
		ArrayList<Coordinate> emptyPatches = new ArrayList<Coordinate>();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (patches[1][i][j] instanceof Empty)
					//Gather all patches which are empty
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
	 * @param patches
	 */
	private void resetMoveState(Patch[][][] patches) {
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				patches[1][i][j].setMoved(false);
			}
		}
	}

	/**
	 * Step 1 : Move jailed agent firstly if the agent has just been released.
	 * Step 2 : Then move a random cop or agent in the vision of the empty patch 
	 * to the patch.
	 * Step 3 : Store the new empty patch to the list and repeat from step 1 
	 * until there is no more empty patch in the lists
	 * @param emptyPatches an ArrayList contains all empty patches on the board
	 * @param patches
	 */
	private void moveOneTick(ArrayList<Coordinate> emptyPatches, 
								Patch[][][] patches) {
		int count = 0;
		ArrayList<Patch> CopsOrAgents = new ArrayList<>();
		// if the no more empty patch can be used then stop the loop
		while (!emptyPatches.isEmpty()) {
			// get a random empty patch from the list.
			Coordinate current = emptyPatches.
						get(RandomUtil.getRandomInt(emptyPatches.size()));
			emptyPatches.remove(current);
			int currentX = current.getX();
			int currentY = current.getY();
			Patch agentReleased = null;
			// find all neighbourhood within the patch
			for (Coordinate temp : board.getNeighbourhood(currentX, currentY)) {
				Patch patch = patches[1][temp.getX()][temp.getY()];
				// if agent cannot move and the jailed agent will be released, 
				// then the agent need be revealed in the patch
				if (!configure.isMovement() && 
						patches[1][temp.getX()][temp.getY()] instanceof Empty) {
					agentReleased = 
						patches[0][temp.getX()][temp.getY()].getReleasedAgent();
				// If agents can move, first find all jailed agents who will be 
				// released in this tick
				//TODO: All jailed agents need to be released after the move
				// phase, which is at the end of every tick.
				} else if (configure.isMovement()) {
					agentReleased = 
					patches[0][temp.getX()][temp.getY()].getReleasedAgent();
				}

				// If there is a jailed agent will be released, 
				// then break the loop
				if (agentReleased != null) {
					break;
				}

				// If there is no such agent can be found then add all agents 
				// and cops within the neighbourhood to the CopsOrAgents list
				if ((configure.isMovement() && patch instanceof Agent) || 
						patch instanceof Cop) {
					CopsOrAgents.add(patch);
					if (patch.getState() == Const.AGENT_JAILED) {
						System.out.println("Wrong");
					}
				}
			}

			// if any agent is just released from the jail and agent cannot
			// move, reveal it in the board
			if (!configure.isMovement() && agentReleased != null) {
				((Agent) agentReleased).setState(Const.AGENT_QUIET);
				patches[1][agentReleased.getCoordinate().getX()]
					[agentReleased.getCoordinate().getY()] = agentReleased;
				removeEmptyPatchInList(emptyPatches, 
					agentReleased.getCoordinate().getX(), 
					agentReleased.getCoordinate().getY());

				continue;
			}

			// if any agent is just released from the jail and agent can move,
			// then move it to the empty patch.
			if (configure.isMovement() && agentReleased != null) {
				((Agent) agentReleased).setState(Const.AGENT_QUIET);
				agentReleased.setMoved(true);
				agentReleased.setCoordinate(currentX, currentY);
				patches[1][currentX][currentY] = agentReleased;
				continue;
			}
			// Move a random cop or agent in CopsOrAgents list to the 
			// empty patch						
			while (CopsOrAgents.size() > 0) {
				int randomOne = RandomUtil.getRandomInt(CopsOrAgents.size());
				Patch patch = CopsOrAgents.get(randomOne);
				// If the agent or the cop has not moved yet, then move it.
				if (!patch.getMove()) {
					int patchX = patch.getCoordinate().getX();
					int patchY = patch.getCoordinate().getY();
					patch.setMoved(true);
					patch.setCoordinate(currentX, currentY);
					patches[1][currentX][currentY] = patch;
					// The original patch will be empty after the agent or the 
					// cop moved, add the new empty patch to the list waiting 
					// for another one to move in.
					Empty newEmpty = new Empty(patchX, patchY, 
										configure.getVision());
					patches[1][patchX][patchY] = newEmpty;
					emptyPatches.add(newEmpty.getCoordinate());
					break;
				// If the agent or cop has moved once already, 
				// remove it from the list
				} else {
					CopsOrAgents.remove(patch);
				}
			}
			CopsOrAgents.clear();
		}
	}

	/**
	 * If the jailed agent has been released in the board, the no one can move 
	 * into the patch anymore.
	 * @param emptyPatches
	 * @param x
	 * @param y
	 */
	private void removeEmptyPatchInList(ArrayList<Coordinate> emptyPatches, 
										int x, int y) {
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
