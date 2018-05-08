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
	Patch[][] patchs;
	int agentNum, copNum;

	public Board(ConfigureVO vo) {
		patchs = new Patch[40][40];
		agentNum = (int) (40 * 40 * vo.getInitial_agent_density());
		copNum = (int) (40 * 40 * vo.getInitial_cop_density());
		init();
		PrintUtil.getInstance().printBoard(patchs);
	}

	void init() {
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				patchs[i][j] = new Empty();
			}
		}

		for (int i = 0; i < agentNum; i++) {
			int x = RandomUtil.getRandomNum(40);
			int y = RandomUtil.getRandomNum(40);
			while (!(patchs[x][y] instanceof Empty)) {
				if (y == 39){
					x++;
					y=0;
				}else {
					y++;
				}
			}
			patchs[x][y] = new Agent();
			
		}
		
		for (int i = 0; i < copNum; i++) {
			int x = RandomUtil.getRandomNum(40);
			int y = RandomUtil.getRandomNum(40);
			while (!(patchs[x][y] instanceof Empty)) {
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
			patchs[x][y] = new Cop();
			
		}
	}
	
	
}
