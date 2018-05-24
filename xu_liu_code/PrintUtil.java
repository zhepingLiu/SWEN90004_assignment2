
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;



/**
 * This class outputs some informations of the model,
 * some for testing, some for analysis.
 */
public class PrintUtil {
	private static PrintUtil printer;

	public static PrintUtil getInstance() {
		if (printer == null) {
			printer = new PrintUtil();
		}
		return printer;
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
		System.out.println("Quiet :  " + quietCount);
		System.out.println("Active : " + activeCount);
		System.out.println("Jailed : " + jailedCount);
		System.out.println(
				"sum:     " + (quietCount + activeCount + jailedCount));

	}

	public void printCount(Board board) {
		int quietCount = 0;
		int activeCount = 0;
		int jailedCount = 0;
		int copCount = 0;

		for (int i = 0; i < Const.board_size; i++) {
			for (int j = 0; j < Const.board_size; j++) {
				Patch agent = board.getPatchs()[i][j];
				if (agent instanceof Agent) {
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
				} else if (agent instanceof Cop) {
					copCount++;
				}
			}
		}

		System.out.println("Quiet :  " + quietCount);
		System.out.println("Active : " + activeCount);
		System.out.println("Jailed : " + jailedCount);
		System.out.println("cop :    " + copCount);
		System.out.println(
				"sum:     " + (quietCount + activeCount + jailedCount));
		System.out.println();

	}

	/**
	 * Output the CSV file
	 * 
	 * @param datas
	 * @param fileName
	 */
	public void printCSV(ArrayList<ArrayList> datas, String fileName) {
		// Delimiter used in CSV file
		final String NEW_LINE_SEPARATOR = "\n";

		// CSV file header
		final Object[] FILE_HEADER = { "quiet", "active", "jailed" };
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;

		// Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormat = CSVFormat.DEFAULT
				.withRecordSeparator(NEW_LINE_SEPARATOR);
		try {
			// initialize FileWriter object
			fileWriter = new FileWriter(fileName);
			// initialize CSVPrinter object
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			// Create CSV file header
			csvFilePrinter.printRecord(FILE_HEADER);
			// Write a new student object list to the CSV file
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
