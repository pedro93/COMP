import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import parser.CNFParser;


public class Main {

	public static void main(String[] args) {
		try {
			PrintStream x =new PrintStream(new File("output.txt")); 
			System.setOut(x);
			System.setErr(x);
			System.err.println("[ERROR] teste");
			CNFParser myParser = new CNFParser("grammar.txt");
			myParser.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
