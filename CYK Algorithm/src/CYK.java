import sun.plugin.perf.PluginRollup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;


/**
 * Created by Carlos on OFFSET9-03-20OFFSET4.
 */
public class CYK {

    static Vector<String> productions;
    static int wordSize;
    static final int OFFSET = 1;

    public static void loadGrammar(String filename) throws IOException
    {
        productions = new Vector<String>();

        BufferedReader buffer;
        String line;

        try {
            buffer = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            return;
        }

        while((line = buffer.readLine()) != null ) {

            String toAdd = "";

            String split_line[] = line.split("\\s+");

            for(int i = 0; i < split_line.length; i++)
            {
                System.out.print(i + ": " + split_line[i] + "  ");

                if(i < split_line.length - OFFSET)
                    toAdd += split_line[i] + " ";
                else
                    toAdd += split_line[i];

            }
            System.out.println();

            productions.add(toAdd);

        }

        System.out.println("These are the productions: " + productions.toString());
    }

    public static boolean CYKparser(String input) {

        //Tamanho do vector = summatory.
        String[][] table = new String[(wordSize)][];

        System.out.println("Sum = " + summatory(wordSize));

        for (int i = 0; i < wordSize; i++){

            table[i] = new String[i+OFFSET];

            for (int j = 0; j <= i; j++){

                table[i][j] = "";
                System.out.print("| I: " + i + " J: " + j + "|");

            }
            System.out.println(); // making new line
        }

        System.out.println(Arrays.deepToString(table));

        //Analizar tabela


        for(int j = 0; j < wordSize; j++)
        {
            table[wordSize-OFFSET][j] = getOFFSETstKeys(input, j);
        }

        System.out.println(Arrays.deepToString(table));


        for(int j = 0; j < wordSize-OFFSET; j++)
        {
            //System.out.println("Making Inputs for: " + table[wordSize-OFFSET][j] + " - " + table[wordSize-OFFSET][j+OFFSET]);
            Vector<String> possibleInputs = makeInputs(table[wordSize-OFFSET][j], table[wordSize-OFFSET][j+OFFSET]);
            table[wordSize-2][j] = getKeys(possibleInputs);
        }

        System.out.println(Arrays.deepToString(table));


        System.out.println("SEE FROM HERE");
        System.out.println("SEE FROM HERE");
        System.out.println("SEE FROM HERE");
        System.out.println("SEE FROM HERE");

        for(int level = wordSize-3; level >= 0; level--)
        {
            for(int tablePosition = 0; tablePosition <= level; tablePosition++)
            {
                Vector<String> possibleInputs;
                possibleInputs = new Vector<String>();

                int combinations =  (wordSize-OFFSET)-level;
                System.out.println("LEVEL: " + level + " TABLEPOS: " + tablePosition  + " COMBINATIONS: " + combinations);

                for(int z = 0; z < combinations; z++) {

                    System.out.println("OFFSETst is: " + (wordSize-z-OFFSET) + ", " + tablePosition);
                    System.out.println("2nd is: " + (level+z+OFFSET) + ", " + (tablePosition+z+OFFSET));
                    System.out.println("OFFSETst: " + table[wordSize-z-OFFSET][tablePosition]);
                    System.out.println("2nd: " + table[level+z+OFFSET][tablePosition+z+OFFSET]);


                    possibleInputs.addAll(makeInputs(table[wordSize-z-OFFSET][tablePosition], table[level+z+OFFSET][tablePosition+z+OFFSET]));
                    System.out.println("Inputs: ");
                    System.out.println(possibleInputs.toString());
                }

                table[level][tablePosition] = getKeys(possibleInputs);
                System.out.println(Arrays.deepToString(table));
            }
        }

        System.out.println(Arrays.deepToString(table));

        return false;
    }

    private static Vector<String> makeInputs(String s0, String sOFFSET) {

        String[] s0inputs = splitInput(s0);
        String[] sOFFSETinputs = splitInput(sOFFSET);

        Vector<String> ret = new Vector<String>();

        for(int i = 0;  i < s0inputs.length; i++)
        {
            for(int j = 0; j < sOFFSETinputs.length; j++)
            {
                if( !s0inputs[i].equals("-") && !sOFFSETinputs[j].equals("-"))
                    ret.add(s0inputs[i] + sOFFSETinputs[j]);
            }
        }

        System.out.println("RET VEC IS: "+ ret.toString());
        return ret;
    }

    private static String getKeys(Vector<String> svec)
    {

        String ret = "";

        //System.out.println("SVEC: " + svec.toString());

        for(int k = 0; k < svec.size(); k++)
        {
            String s = svec.elementAt(k);

            for (int i = 0; i < productions.size(); i++) {

                String rhs = getRHSProduction(productions.elementAt(i));
                String lhs = getLHSProduction(productions.elementAt(i));

                //System.out.println( "Comparing: " + rhs + " with " + s + " /");

                if(rhs.equals(s))
                {
                    if(!ret.contains(lhs+"|")) //ISTO PODE DAR ERROS
                    {
                        ret += lhs;
                        ret += "|";
                    }
                }
            }
        }

        if(ret.length() == 0)
            ret += "-";

        System.out.println("Ret is: " + ret);
        return ret;

    }

    private static String getOFFSETstKeys(String s, int p)
    {

        String ret = "";

        for (int i = 0; i < productions.size(); i++) {

            String rhs = getRHSProduction(productions.elementAt(i));
            String lhs = getLHSProduction(productions.elementAt(i));

            //System.out.println( "Comparing: " + rhs + " with "  + s.substring(p, p+OFFSET) + " /");

            if(rhs.equals(s.substring(p, p+OFFSET)))
            {
                ret += lhs;
                ret += "|";
            }
        }

        System.out.println("Ret is: " + ret);
        return ret;

    }

    private static String getRHSProduction(String production)
    {
        return production.substring(production.indexOf("-")+2, production.length());
    }

    private static String getLHSProduction(String production)
    {
        return production.substring(0, production.indexOf("-")-OFFSET);
    }

    private static int summatory(int length) {

        int sum = 0;

        while(length > 0)
        {
            sum += length;
            length--;
        }

        return sum;
    }


    private static String[] splitInput(String input) {
        String []split_msg = input.split("\\|+");

        /*
        for(int i = 0; i < split_msg.length; i++)
        {
            System.out.println("Splitted " + i + ": " + split_msg[i]);
        }
        */
        return split_msg;
    }

    public static void main(String args[]) throws IOException{
        loadGrammar("C:\\Users\\Pedro\\GIT\\COMP\\CYK Algorithm\\src\\grammar");

        String input = "bbabaa";

        boolean word = false;
        int endOfLine = input.length() - 1;

        for (int i = 0; i < input.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(input.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(input.charAt(i)) && word) {
                wordSize++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(input.charAt(i)) && i == endOfLine) {
                wordSize++;
            }
        }

        wordSize=input.length();
        System.out.println("this " + wordSize);

        CYKparser(input);
    }

}
