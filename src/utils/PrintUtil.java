package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import model.Agent;
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
	
	/**
	 * print all patches as a 40 X 40 table in the console
	 * 
	 * @param patchs
	 */
	public void printBoard(Patch[][][] patchs){
		int countError = 0;
		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				if (patchs[1][i][j] instanceof Empty && 
					patchs[0][i][j].isAnyAgentsJailed()) {
					System.out.print(Const.AGENT_JAILED+" ");
				}else {
					if(patchs[1][i][j] instanceof Agent && 
						patchs[1][i][j].getState()== Const.AGENT_JAILED)
						countError++;
					System.out.print( patchs[1][i][j].toString()+" ");
				}
				
			}
			System.out.println();
		}
		System.out.println(countError);
	}

	/**
	 * Print current number and state of all agents
	 * 
	 * @param agents
	 */
	public void printAgents(ArrayList<Agent> agents) {
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
		System.out.println("Quiet :  "+ quietCount);
		System.out.println("Active : "+ activeCount);
		System.out.println("Jailed : "+ jailedCount);
		System.out.println("sum:     "+ (quietCount+activeCount+jailedCount));
		
	}
	

	/**
	 * Output the CSV file
	 * 
	 * @param datas
	 * @param fileName
	 */
	public void printCSV(ArrayList<ArrayList> datas,String fileName) {
		//Delimiter used in CSV file
		final String NEW_LINE_SEPARATOR = "\n";
		
		//CSV file header
		final Object [] FILE_HEADER = {"quiet", "active", "jailed"};	
			FileWriter fileWriter = null;
			CSVPrinter csvFilePrinter = null;
			
			//Create the CSVFormat object with "\n" as a record delimiter
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.
				withRecordSeparator(NEW_LINE_SEPARATOR);
			try {
				//initialize FileWriter object
				fileWriter = new FileWriter(fileName);
				//initialize CSVPrinter object 
		        csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		        //Create CSV file header
		        csvFilePrinter.printRecord(FILE_HEADER);
				//Write a new student object list to the CSV file
				for (ArrayList list : datas) {
		            csvFilePrinter.printRecord(list);
				}
				
			} catch (Exception e) {
				System.out.println("Error in CsvFileWriter!!!");
				e.printStackTrace();
			} finally {
				try {
					fileWriter.flush();
					fileWriter.close();
					csvFilePrinter.close();
				} catch (IOException e) {
	                e.printStackTrace();
				}
			}
		
	}
}
