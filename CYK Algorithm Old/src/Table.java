import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;


public class Table extends JPanel {
    public Table() {
        setBorder(new LineBorder(new Color(0, 0, 0)));
    }

    public static void func()
    {
        JFrame frame = new JFrame("Grid Layout");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,400);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 6, 4, 4));

        //Ele preenche primeiro verticalmente
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++)
            {
                if(j <= i) {
                    JPanel cellPanel = new JPanel();
                    cellPanel.setBackground(Color.LIGHT_GRAY);
                    cellPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
                    String labelName = "Cell " + i + j;
                    JLabel cellLabel = new JLabel(labelName);
                    cellPanel.add(cellLabel);
                    panel.add(cellPanel);
                }
                else{
                    JPanel cellPanel = new JPanel();
                    cellPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
                    panel.add(cellPanel);
                }
            }
        }

        frame.getContentPane().add(panel);
    }

}
