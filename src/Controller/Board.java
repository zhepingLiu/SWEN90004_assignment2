package Controller;

import java.util.Random;

import com.sun.org.apache.xml.internal.serialize.Printer;

import configure.ConfigureVO;
import model.Agent;
import model.Cop;
import model.Empty;
import model.Patch;
import utils.PrintUtil;
import utils.RandomUtil;

public class Board {
	Patch[][][] patches;
	int agentNum, copNum;
	ConfigureVO configureVO;

	public Board(ConfigureVO vo) {
		patches = new Patch[2][40][40];
		agentNum = (int) (40 * 40 * vo.getInitial_agent_density());
		copNum = (int) (40 * 40 * vo.getInitial_cop_density());
		this.configureVO = vo;
		init();
		PrintUtil.getInstance().printBoard(patches);
	}

	void init() {
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				patches[0][i][j] = new Empty(i,j,configureVO.getVision());
				patches[1][i][j] = new Empty(i,j,configureVO.getVision());
			}
		}

		for (int i = 0; i < agentNum; i++) {
			int x = RandomUtil.getRandomInt(40);
			int y = RandomUtil.getRandomInt(40);
			while (!(patches[1][x][y] instanceof Empty)) {
				if (y == 39 && x == 39){
					x=0;
					y=0;
				}else if( y == 39){
					y=0;
					x++;
				}else {
					y++;
				}
			}
			patches[1][x][y] = new Agent(x,y,configureVO.getVision());
			
		}
		
		for (int i = 0; i < copNum; i++) {
			int x = RandomUtil.getRandomInt(40);
			int y = RandomUtil.getRandomInt(40);
			while (!(patches[1][x][y] instanceof Empty)) {
				if (y == 39 && x == 39){
					x=0;
					y=0;
				}else if( y == 39){
					y=0;
					x++;
				}else {
					y++;
				}
			}
			patches[1][x][y] = new Cop();
			
		}
	}

	public Patch[][][] getPatchs() {
		return patches;
	}
	
	
}
