package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.axes.PathComponent;

import configure.ConfigureVO;
import model.Agent;
import model.Coordinate;
import model.Cop;
import model.Empty;
import model.Patch;
import sun.management.resources.agent;
import utils.Const;
import utils.PrintUtil;
import utils.RandomUtil;

public class Controller {
	Board board;
	ConfigureVO configure;

	void init() {

		// ConfigureVO vo = new ConfigureVO();
		// vo.setGoverment_legitimacy(0.62);
		// vo.setInitial_agent_density(0.Const.board_size);
		// vo.setInitial_cop_density(0.02);
		// vo.setMax_jail_term(10);
		// vo.setMovement(false);
		// vo.setVision(7.9);

		Gson gson = new Gson();
		FileReader reader = null;
		try {
			reader = new FileReader(new File("configure.json"));
			configure = gson.fromJson(reader, ConfigureVO.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		board = new Board(configure);

	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.init();
		controller.start();
	}

	void start() {
		move();
		determineBehaviour();
		// TODO
//		reduceJailTerm();
		enforce();
		
		System.out.println();
		PrintUtil.getInstance().printBoard(board.getPatchs());
	}

	private void enforce() {
		Patch[][][] patches = board.getPatchs();
		ArrayList<Patch> activeAgents = new ArrayList<>();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if(patches[1][i][j] instanceof Cop){
					for (Coordinate temp : board.getNeighbourhood(i, j)) {
						 if (patches[1][temp.getX()][temp.getY()] instanceof Agent && patches[1][temp.getX()][temp.getY()].getState() == Const.AGENT_ACTIVE) {
							activeAgents.add(patches[1][temp.getX()][temp.getY()]);
						}
					}
					int activeNum = activeAgents.size();
					if (activeNum >0) {
						Agent randomAgent = (Agent) activeAgents.get(RandomUtil.getRandomInt(activeNum));
						randomAgent.setJailTerm(RandomUtil.getRandomInt(configure.getMax_jail_term()));
						randomAgent.setState(Const.AGENT_JAILED);
						patches[0][i][j].putAgentInJail(randomAgent);
						patches[1][i][j] = new Empty(randomAgent.getCoordinate().getX(),randomAgent.getCoordinate().getY(),configure.getVision());
					}
				}
			}
		}
	}

	private void determineBehaviour() {	
		Patch[][][] patches = board.getPatchs();
		int copNumInNeighbour =0;
		int activeNumInNeighbour = 0;
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if(patches[1][i][j] instanceof Agent){
					
					
					for (Coordinate temp : board.getNeighbourhood(i, j)) {
						if (patches[1][temp.getX()][temp.getY()] instanceof Cop) {
							copNumInNeighbour ++ ;
						}else if (patches[1][temp.getX()][temp.getY()] instanceof Agent && patches[1][temp.getX()][temp.getY()].getState() == Const.AGENT_ACTIVE) {
							activeNumInNeighbour ++ ;
						}
					}
					
					((Agent)patches[1][i][j]).determineBehaviour(copNumInNeighbour, activeNumInNeighbour);
				}
			}
		}
		
        
	}

	private void move() {
		Patch[][][] patches = board.getPatchs();
		Queue<Coordinate> emptyPatches = new LinkedList<Coordinate>();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (patches[1][i][j] instanceof Empty)
					emptyPatches.add(new Coordinate(i, j));

			}
		}
		moveOnePatch(emptyPatches, patches);
		resetMoveState(patches);
	}

	private void resetMoveState(Patch[][][] patches) {
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				patches[1][i][j].setMoved();
			}
		}
	}

	private void moveOnePatch(Queue<Coordinate> emptyPatches, Patch[][][] patches) {
		int count = 0;
		ArrayList<Patch> CopsOrAgents = new ArrayList<>();
		while (!emptyPatches.isEmpty()) {
			Coordinate current = emptyPatches.poll();
			int currentX = current.getX();
			int currentY = current.getY();
			
			for (Coordinate temp : board.getNeighbourhood(currentX, currentY)) {
				Patch patch = patches[1][temp.getX()][temp.getY()];
				if ((configure.isMovement() && patch instanceof Agent) || patch instanceof Cop) {
					CopsOrAgents.add(patch);
				}
			}
//			System.out.println(emptyPatches.size()+" *");
//			System.out.println(CopsOrAgents.size());
			while (CopsOrAgents.size() > 0) {
				int randomOne = RandomUtil.getRandomInt(CopsOrAgents.size());
				Patch patch = CopsOrAgents.get(randomOne);
				if (!patch.getMove()) {
					int patchX = patch.getCoordinate().getX();
					int patchY = patch.getCoordinate().getY();
					patch.setMoved();
					patch.setCoordinate(currentX, currentY);
					patches[1][currentX][currentY] = patch;
					Empty newEmpty = new Empty(patchX, patchY, configure.getVision());
					patches[1][patchX][patchY] = newEmpty;
					emptyPatches.add(newEmpty.getCoordinate());
					break;
				}else{
					CopsOrAgents.remove(patch);
				}
			}
			CopsOrAgents.clear();
		}
	}

}
