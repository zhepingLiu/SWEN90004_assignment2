package utils;

import Controller.Board;
import model.Empty;
import model.Patch;

public class PrintUtil {
	private static PrintUtil printer;
	
	public static PrintUtil getInstance(){
		if (printer == null) {
			printer = new PrintUtil();
		}
		return printer;
	}
	
	public void printBoard(Patch[][][] patchs){
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (!(patchs[0][i][j] instanceof Empty)) {
					System.out.print(patchs[0][i][j].toString());
				}
				System.out.print( patchs[1][i][j].toString()+" ");
			}
			System.out.println();
		}
	}
}
