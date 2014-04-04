import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

public class Algoritm
{
	static Vector<Vector<String>> productions;
	static int inputSize;
	static final int OFFSET = 1;

	public Algoritm(TreeMap<Structure, List<Vector<Structure>>> SymbolTable)
	{
		productions = new Vector<Vector<String>>();
		for(Entry<Structure, List<Vector<Structure>>> entry : SymbolTable.entrySet()) {
			List<Vector<Structure>> value = entry.getValue();
			Structure key = entry.getKey();
			for (Vector<Structure> i : value) {
				Vector<String> toAdd = new Vector<String>();
				toAdd.add(key.name);
				toAdd.add(":=");
				for(Structure x:i)
				{
					toAdd.add(x.name);
				}
				productions.add(toAdd);
			}
		}
        System.out.println("These are the productions: " + productions.toString());

	}

	public boolean CYKparser(Vector<String> stringToProcess) 
	{
		inputSize = stringToProcess.size();
		//Tamanho do vector = summatory.
		//String[][] table = new String[(inputSize)][];

		Vector<ArrayList<Vector<String>>> table = new Vector<ArrayList<Vector<String>>>();

		for (int i = 0; i < inputSize; i++){

			ArrayList<Vector<String>> tableRow = new ArrayList<Vector<String>>(i);

			for (int j = 0; j <= i; j++){

				tableRow.add(new Vector<String>());
			}
			table.add(tableRow);
		}

		printTable(table);

		for(int level = inputSize-1; level >= 0; level--)
		{
			for(int tablePosition = 0; tablePosition <= level; tablePosition++)
			{
				Vector<String> possibleInputs;
				possibleInputs = new Vector<String>();

				int combinations =  (inputSize-OFFSET)-level;
				System.out.println("LEVEL: " + level + " TABLEPOS: " + tablePosition  + " COMBINATIONS: " + combinations);

				if(level == inputSize-1)
				{
					table.elementAt(inputSize-OFFSET).set(tablePosition, get1stKeys(stringToProcess, tablePosition));
				}
				else {
					for(int z = 0; z < combinations; z++) {

						System.out.println("1st is: " + (inputSize-z-OFFSET) + ", " + tablePosition);
						System.out.println("2nd is: " + (level+z+OFFSET) + ", " + (tablePosition+z+OFFSET));
						System.out.println("1st: " + table.elementAt(inputSize-z-OFFSET).get(tablePosition));
						System.out.println("2nd: " + table.elementAt(level+z+OFFSET).get(tablePosition+z+OFFSET));

						possibleInputs.addAll(makeInputs(table.elementAt(inputSize-z-OFFSET).get(tablePosition), table.elementAt(level+z+OFFSET).get(tablePosition+z+OFFSET)));
						System.out.println("Inputs: ");
						System.out.println(possibleInputs.toString());
					}

					table.elementAt(level).set(tablePosition, getKeys(possibleInputs));
					printTable(table);
				}
			}
			printTable(table);
		}


		if(table.elementAt(0).get(0).contains("START"))
			return true;
		else
			return false;
	}

	private static void printTable (Vector<ArrayList<Vector<String>>> t) {

		for (int i = 0; i < inputSize; i++){

			for (int j = 0; j <= i; j++){

				System.out.print("| I: " + i + " J: " + j + ": " + t.elementAt(i).get(j).toString() + "|");
			}

			System.out.println();
		}
		System.out.println("--- \\\\ --- ");
	}

	private static Vector<String> makeInputs(Vector<String> s0, Vector<String> s1) {

		Vector<String> ret = new Vector<String>();

		for(int i = 0;  i < s0.size(); i++)
		{
			for(int j = 0; j < s1.size(); j++)
			{
				if( !s0.elementAt(i).equals("-") && !s1.elementAt(j).equals("-"))
				{
					ret.add(s0.elementAt(i) + " " + s1.elementAt(j));
				}
			}
		}

		System.out.println("RET VEC IS: "+ ret.toString());
		return ret;
	}

	private static Vector<String> getKeys(Vector<String> svec)
	{
		Vector<String> rtrn = new Vector<String>();

		System.out.println("SVEC: " + svec.toString());

		for(int k = 0; k < svec.size(); k++)
		{
			String s = svec.elementAt(k);

			for (int i = 0; i < productions.size(); i++) {

				String rhs = getRHSProduction(productions.elementAt(i));
				String lhs = getLHSProduction(productions.elementAt(i));

				System.out.println( "Comparing: " + rhs + " with " + s + " /");

				if(rhs.equals(s) && !rtrn.contains(lhs))
				{
					rtrn.add(lhs);
				}
			}
		}

		if(rtrn.size() == 0)
			rtrn.add("-");

		System.out.println("Ret is: " + rtrn.toString());
		return rtrn;

	}

	private static Vector<String> get1stKeys(Vector<String> vs, int position)
	{

		Vector<String> rtrn = new Vector<String>();

		System.out.println("VS IS:" + vs);

		for(int j = 0; j < productions.size(); j++)
		{
			String rhs = getRHSProduction(productions.elementAt(j));
			String lhs = getLHSProduction(productions.elementAt(j));

			System.out.println( "Comparing: " + vs.elementAt(position) + " with "  + rhs + " /");

			if(rhs.toString().equals(vs.elementAt(position)) && !rtrn.contains(lhs))
			{
				rtrn.add(lhs);
			}
		}

		System.out.println("Ret is: " + rtrn.toString());
		return rtrn;

	}

	private static String getRHSProduction(Vector<String> production)
	{
		String rtrn = new String();

		for(int i = 2; i < production.size(); i++)
		{
			if(i == production.size()-1)
				rtrn += production.elementAt(i);
			else
				rtrn += production.elementAt(i) + " ";
		}

		return rtrn;

	}

	private static String getLHSProduction(Vector<String> production)
	{
		return production.elementAt(0);
	}

}
