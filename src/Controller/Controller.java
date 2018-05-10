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

import configure.ConfigureVO;
import model.Agent;
import model.Coordinate;
import model.Cop;
import model.Empty;
import model.Patch;
import utils.PrintUtil;
import utils.RandomUtil;

public class Controller {
	Board board;
	ConfigureVO configure;

	void init() {

		// ConfigureVO vo = new ConfigureVO();
		// vo.setGoverment_legitimacy(0.62);
		// vo.setInitial_agent_density(0.40);
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
		System.out.println();
		PrintUtil.getInstance().printBoard(board.getPatchs());
	}

	void move() {
		Patch[][][] patchs = board.getPatchs();
		Queue<Coordinate> emptyPatches = new LinkedList<Coordinate>();
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				if (patchs[1][i][j] instanceof Empty)
					emptyPatches.add(new Coordinate(i, j));

			}
		}
		moveOnePatch(emptyPatches, patchs);
	}

	private void moveOnePatch(Queue<Coordinate> emptyPatches, Patch[][][] patches) {
		while (!emptyPatches.isEmpty()) {
			Coordinate current = emptyPatches.poll();
			int currentX = current.getX();
			int currentY = current.getY();
			Empty empty = (Empty) patches[1][currentX][currentY];
			ArrayList<Patch> CopsOrAgents = new ArrayList<>();
			for (Coordinate temp : empty.getNeighbor()) {
				Patch patch = patches[1][temp.getX()][temp.getY()];
				if (configure.isMovement() || patch instanceof Cop) {
					CopsOrAgents.add(patch);
				}			
			}
			if (CopsOrAgents.size()>0) {
				int randomOne = RandomUtil.getRandomInt(CopsOrAgents.size());
				Patch patch = CopsOrAgents.get(randomOne);
				patch.setMoved();
				patch.setCoordinate(currentX, currentY);
				patches[1][currentX][currentY] = patch;
				int patchX = patch.getCoordinate().getX();
				int patchY = patch.getCoordinate().getY();
				Empty newEmpty = new Empty(patchX, patchY,configure.getVision()); 
				patches[1][patchX][patchY] = newEmpty;
				emptyPatches.add(newEmpty.getCoordinate());
			}
			
		}
	}

}
