package utils;

public class Printer {
	static Printer printer;
	
	static Printer getInstance(){
		if (printer == null) {
			printer = new Printer();
		}
		return printer;
	}
}
