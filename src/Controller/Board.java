package Controller;

import java.util.ArrayList;
import java.util.Random;

import com.sun.org.apache.xml.internal.serialize.Printer;

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

	private Patch[][][] patches;
	private int agentNum, copNum;

	private ConfigureVO configureVO;
	private ArrayList<Coordinate>[][] neighbourhood = new ArrayList[Const.board_size][Const.board_size];
	
	private ArrayList<Agent> agents = new ArrayList<>();

	public Board(ConfigureVO vo) {
		patches = new Patch[2][Const.board_size][Const.board_size];
		agentNum = (int) (Const.board_size * Const.board_size * vo.getInitial_agent_density());
		copNum = (int) (Const.board_size * Const.board_size * vo.getInitial_cop_density());
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
	

	private void initNeighbor() {
		double vision = configureVO.getVision();
		int intVision = (int) configureVO.getVision();
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				neighbourhood[i][j] = new ArrayList<>();
				for (int ii = -intVision; ii <= intVision; ii++) {
					for (int jj = -intVision; jj <= intVision; jj++) {
						if (Math.abs(ii) + Math.abs(jj) < vision && i + ii >= 0 && i + ii < Const.board_size
								&& j + jj >= 0 && j + jj < Const.board_size) {
							neighbourhood[i][j].add(new Coordinate(i + ii, jj + j));
						}
					}
				} 
			}
		}
	}

	public ArrayList<Coordinate> getNeighbourhood(int x, int y){
		return neighbourhood[x][y];
	}
	
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
				if (y == Const.board_size -1  && x == Const.board_size -1){
					x=0;
					y=0;
				}else if( y == Const.board_size -1){
					y=0;
					x++;
				}else {
					y++;
				}
			}
			Agent newAgent = new  Agent(x,y,configureVO.getVision(),configureVO.getGoverment_legitimacy());			
			agents.add(newAgent);
			patches[1][x][y] = newAgent;			
		}
		
		for (int i = 0; i < copNum; i++) {
			int x = RandomUtil.getRandomInt(Const.board_size);
			int y = RandomUtil.getRandomInt(Const.board_size);
			while (!(patches[1][x][y] instanceof Empty)) {
				if (y == Const.board_size -1 && x == Const.board_size -1){
					x=0;
					y=0;
				}else if( y == Const.board_size -1){
					y=0;
					x++;
				}else {
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
