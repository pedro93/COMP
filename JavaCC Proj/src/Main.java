import java.io.FileNotFoundException;


public class Main {

	public static void main(String[] args) {
		try {
			CNFParser myParser = new CNFParser("grammar.txt");
			myParser.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
