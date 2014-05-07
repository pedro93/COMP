import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by Carlos on 19-03-14.
 */
public class CYK {

    static Vector<Vector<String>> productions;
    static int inputSize;
    static final int OFFSET = 1;

    static final boolean LOG = false;

    public  void loadGrammar(String filename) throws IOException
    {
        productions = new Vector<Vector<String>>();

        BufferedReader buffer;
        String line;

        try {
            buffer = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            return;
        }

        while((line = buffer.readLine()) != null ) {

            Vector<String> toAdd = splitString(line);
            System.out.println();

            productions.add(toAdd);

        }

        if(LOG)
            System.out.println("These are the productions: " + productions.toString());
    }

    private Vector<String> splitString(String line) {
        Vector<String> toAdd = new Vector<String>();

        String split_line[] = line.split("\\s+");

        for(int i = 0; i < split_line.length; i++)
        {
            if(LOG)
                System.out.print(i + ": " + split_line[i] + "  ");

            toAdd.add(split_line[i]);

        }
        return toAdd;
    }

        public boolean CYKparser(Vector<String> toProcess, String input) {

        //Window Functions

        JFrame frame = new JFrame("Cyk Parser");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800,600);

        //Cell Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(toProcess.size(), toProcess.size(), 4, 4));

        //BottomPanel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        bottomPanel.setPreferredSize(new Dimension(frame.getHeight(), 40));
        bottomPanel.setLayout(new BorderLayout());
        JLabel inputStringLabel = new JLabel("  Input String: " + input);

        //BtnPanel
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        JButton runBtn = new JButton("Run");
        inputStringLabel.setHorizontalAlignment(SwingConstants.LEFT);
        btnPanel.add(runBtn);

        bottomPanel.add(inputStringLabel);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        frame.add(panel);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        Vector<JPanel> panels = new Vector<JPanel>();
        initCellPanels(toProcess, panels, panel);

        //Algorithm

        Vector<ArrayList<Vector<String>>> table = new Vector<ArrayList<Vector<String>>>();

        for (int i = 0; i < inputSize; i++){

            ArrayList<Vector<String>> tableRow = new ArrayList<Vector<String>>(i);

            for (int j = 0; j <= i; j++){

                tableRow.add(new Vector<String>());
            }
            table.add(tableRow);
        }

        if(LOG)
            printTable(table);

        for(int level = inputSize-1; level >= 0; level--)
        {
            for(int tablePosition = 0; tablePosition <= level; tablePosition++)
            {
                Vector<String> possibleInputs;
                possibleInputs = new Vector<String>();

                int combinations =  (inputSize-OFFSET)-level;

                if(LOG)
                    System.out.println("LEVEL: " + level + " TABLEPOS: " + tablePosition  + " COMBINATIONS: " + combinations);

                if(level == inputSize-1)
                {
                    fillPanel(0, panels, table, level, tablePosition, panel);

                    table.elementAt(inputSize-OFFSET).set(tablePosition, get1stKeys(toProcess, tablePosition));

                    catchSomeZs();

                    fillPanel(2, panels, table, level, tablePosition, panel);

                    catchSomeZs();
                }
                else {
                    fillPanel(0, panels, table, level, tablePosition, panel);

                    for(int z = 0; z < combinations; z++) {

                        if(LOG) {
                            System.out.println("1st is: " + (inputSize - z - OFFSET) + ", " + tablePosition);
                            System.out.println("2nd is: " + (level + z + OFFSET) + ", " + (tablePosition + z + OFFSET));
                            System.out.println("1st: " + table.elementAt(inputSize - z - OFFSET).get(tablePosition));
                            System.out.println("2nd: " + table.elementAt(level + z + OFFSET).get(tablePosition + z + OFFSET));
                        }

                        fillPanel(1, panels, table, inputSize - z - OFFSET, tablePosition, panel);
                        fillPanel(1, panels, table, level + z + OFFSET, tablePosition + z + OFFSET, panel);
                        catchSomeZs();

                        possibleInputs.addAll(makeInputs(table.elementAt(inputSize - z - OFFSET).get(tablePosition), table.elementAt(level + z + OFFSET).get(tablePosition + z + OFFSET)));

                        fillPanel(2, panels, table, inputSize - z - OFFSET, tablePosition, panel);
                        fillPanel(2, panels, table, level + z + OFFSET, tablePosition + z + OFFSET, panel);
                        catchSomeZs();

                        if(LOG) {
                            System.out.println("Inputs: ");
                            System.out.println(possibleInputs.toString());
                        }
                    }

                    table.elementAt(level).set(tablePosition, getKeys(possibleInputs));
                    fillPanel(2, panels, table, level, tablePosition, panel);

                    if(LOG)
                        printTable(table);

                }
            }

            if(LOG)
                printTable(table);
        }


        if(table.elementAt(0).get(0).contains(productions.elementAt(0).elementAt(0)))
            return true;
        else
            return false;
    }

    private void catchSomeZs() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Mode 0 = Filling this square, Mode 1 = Comparing these squares, Mode 2 = done
    private void fillPanel(int mode, Vector<JPanel> panels, Vector<ArrayList<Vector<String>>> table, int level, int tablePosition, JPanel panel) {

        switch(mode) {
            case 0:
                panels.elementAt((inputSize * level) + tablePosition).setBackground(Color.CYAN);
                String labelName = "Analysing";
                JLabel cellLabel = new JLabel(labelName);
                panels.elementAt((inputSize * level) + tablePosition).add(cellLabel);
                break;
            case 1:
                panels.elementAt((inputSize * level) + tablePosition).setBackground(Color.RED);
                break;
            case 2:
                panels.elementAt((inputSize * level) + tablePosition).setBackground(Color.LIGHT_GRAY);
                String labelName1 = table.elementAt(level).get(tablePosition).toString();
                JLabel cellLabel1 = new JLabel(labelName1);
                panels.elementAt((inputSize * level) + tablePosition).removeAll();
                panels.elementAt((inputSize * level) + tablePosition).add(cellLabel1);
                break;

        }
        panels.elementAt((inputSize * level) + tablePosition).revalidate();
        panel.revalidate();

    }

    private void initCellPanels(Vector<String> toProcess, Vector<JPanel> panels, JPanel panel) {

        for(int i = 0; i < toProcess.size(); i++){
            for(int j = 0; j < toProcess.size(); j++)
            {
                JPanel cellPanel = new JPanel();
                cellPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
                panels.add(cellPanel);
                panel.add(cellPanel);
            }
        }
    }

    private void printTable (Vector<ArrayList<Vector<String>>> t) {

        for (int i = 0; i < inputSize; i++){

            for (int j = 0; j <= i; j++){

                System.out.print("| I: " + i + " J: " + j + ": " + t.elementAt(i).get(j).toString() + "|");
            }

            System.out.println();
        }
        System.out.println("--- \\\\ --- ");
    }

    private Vector<String> makeInputs(Vector<String> s0, Vector<String> s1) {

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

        if(LOG)
            System.out.println("RET VEC IS: "+ ret.toString());

        return ret;
    }

    private Vector<String> getKeys(Vector<String> svec)
    {
        Vector<String> rtrn = new Vector<String>();

        if(LOG)
            System.out.println("SVEC: " + svec.toString());

        for(int k = 0; k < svec.size(); k++)
        {
            String s = svec.elementAt(k);

            for (int i = 0; i < productions.size(); i++) {

                String rhs = getRHSProduction(productions.elementAt(i));
                String lhs = getLHSProduction(productions.elementAt(i));

                if(LOG)
                    System.out.println( "Comparing: " + rhs + " with " + s + " /");

                if(rhs.equals(s) && !rtrn.contains(lhs))
                {
                    rtrn.add(lhs);
                }
            }
        }

        if(rtrn.size() == 0)
            rtrn.add("-");

        if(LOG)
            System.out.println("Ret is: " + rtrn.toString());

        return rtrn;

    }

    private Vector<String> get1stKeys(Vector<String> vs, int position)
    {

        Vector<String> rtrn = new Vector<String>();

        if(LOG)
            System.out.println("VS IS:" + vs);

        for(int j = 0; j < productions.size(); j++)
        {
            String rhs = getRHSProduction(productions.elementAt(j));
            String lhs = getLHSProduction(productions.elementAt(j));

            if(LOG)
                System.out.println( "Comparing: " + vs.elementAt(position) + " with "  + rhs + " /");

            if(rhs.toString().equals(vs.elementAt(position)) && !rtrn.contains(lhs))
            {
                rtrn.add(lhs);
            }
        }

        if(LOG)
            System.out.println("Ret is: " + rtrn.toString());

        return rtrn;

    }

    private String getRHSProduction(Vector<String> production)
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

    private String getLHSProduction(Vector<String> production)
    {
        return production.elementAt(0);
    }

    public static void main(String args[]) throws IOException{

        CYK algorithm = new CYK();
        algorithm.loadGrammar("C:\\Users\\Pedro\\GIT\\COMP\\CYK Algorithm\\src\\grammar2");

        String input = "she eats a fish with a fork";
        //String input = "b b a b a a";

        Vector<String> toProcess = algorithm.splitString(input);

        inputSize = toProcess.size();

        if(LOG)
            System.out.println("this " + inputSize);

        if(algorithm.CYKparser(toProcess, input))
        {
            System.out.println("Great Success");
        }
        else
        {
            System.out.println("LOL NOT");
        }
    }



}
