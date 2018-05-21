package Controller;

import java.util.ArrayList;
import java.util.Random;

import configure.ConfigureVO;
import model.Agent;
import model.Coordinate;
import model.Cop;
import model.Empty;
import model.Patch;
import utils.Const;
import utils.PrintUtil;
import utils.RandomUtil;

public class Board {

	//TODO: add some comments about the three dimensional patch
	private Patch[][][] patches;

	// the number of cops and agents
	private int agentNum, copNum;

	// configuration of the model.
	private ConfigureVO configureVO;
	
	// The neighbourhood for every patch
	private ArrayList<Coordinate>[][] neighbourhood = 
		new ArrayList[Const.board_size][Const.board_size];
	
	// All the agents in the board
	private ArrayList<Agent> agents = new ArrayList<>();

	/**
	 * 
	 */
	public Board(ConfigureVO vo) {
		patches = new Patch[2][Const.board_size][Const.board_size];
		agentNum = (int) (Const.board_size * Const.board_size * 
							vo.getInitialAgentDensity());
		copNum = (int) (Const.board_size * Const.board_size * 
							vo.getInitialCopDensity());
		this.configureVO = vo;

		initPathces();

		initNeighbor();
		PrintUtil.getInstance().printBoard(patches);
	}
	

	public int getAgentNum() {
		return agentNum;
	}

	public int getCopNum() {
		return copNum;
	}
	
	/**
	 * Find the neighbourhood for every patch and store it in the arrays
	 */
	private void initNeighbor() {
		double vision = configureVO.getVision();
		int intVision = (int) configureVO.getVision();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				neighbourhood[i][j] = new ArrayList<>();
				for (int ii = -intVision; ii <= intVision; ii++) {
					for (int jj = -intVision; jj <= intVision; jj++) {
						if (Math.abs(ii) + Math.abs(jj) < vision && 
							i + ii >= 0 && i + ii < Const.board_size && 
							j + jj >= 0 && j + jj < Const.board_size) {
							neighbourhood[i][j].
								add(new Coordinate(i + ii, jj + j));
						}
					}
				} 
			}
		}
	}

	/**
	 * get the neighbourhood for the patch in (x,y) 
	 * @param x coordianate x
	 * @param y coordinate y
	 * @return an ArrayList of Coordinate contains all neighbourhood of (x, y)
	 */
	public ArrayList<Coordinate> getNeighbourhood(int x, int y) {
		return neighbourhood[x][y];
	}
	
	/**
	 * initial the first state of board, same as the setup function in the 
	 * interface of the Netlogo
	 */
	private void initPathces() {
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				patches[0][i][j] = new Empty(i,j,configureVO.getVision());
				patches[1][i][j] = new Empty(i,j,configureVO.getVision());
			}
		}

		for (int i = 0; i < agentNum; i++) {
			int x = RandomUtil.getRandomInt(Const.board_size);
			int y = RandomUtil.getRandomInt(Const.board_size);
			while (!(patches[1][x][y] instanceof Empty)) {
				if (y == Const.board_size -1  && x == Const.board_size -1) {
					x=0;
					y=0;
				} else if( y == Const.board_size -1) {
					y=0;
					x++;
				} else {
					y++;
				}
			}

			Agent newAgent = new  Agent(x,y,configureVO.getVision(), 
									configureVO.getGovermentLegitimacy());			
			agents.add(newAgent);
			patches[1][x][y] = newAgent;			
		}
		
		for (int i = 0; i < copNum; i++) {
			int x = RandomUtil.getRandomInt(Const.board_size);
			int y = RandomUtil.getRandomInt(Const.board_size);
			while (!(patches[1][x][y] instanceof Empty)) {
				if (y == Const.board_size -1 && x == Const.board_size -1) {
					x=0;
					y=0;
				} else if( y == Const.board_size -1) {
					y=0;
					x++;
				} else {
					y++;
				}
			}
			patches[1][x][y] = new Cop(x,y,configureVO.getVision());
			
		}
	}

	public Patch[][][] getPatchs() {
		return patches;
	}
	
	public ArrayList<Agent> getAgents() {
		return agents;
	}
}
