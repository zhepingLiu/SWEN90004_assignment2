package utils;

import Controller.Board;
import model.Patch;

public class PrintUtil {
	private static PrintUtil printer;
	
	public static PrintUtil getInstance(){
		if (printer == null) {
			printer = new PrintUtil();
		}
		return printer;
	}
	
	public void printBoard(Patch[][] patchs){
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				System.out.print( patchs[i][j].toString()+" ");
			}
			System.out.println();
		}
	}
}
