package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.GridLayout;

public class HelpPnl extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1544900768397134308L;
	/**
	 * Create the panel.
	 */
	Window par;
	
	public HelpPnl(Window parent) {
		par=parent;
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblHowThisProgram = new JLabel("How this program works");
		lblHowThisProgram.setHorizontalAlignment(SwingConstants.CENTER);
		lblHowThisProgram.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(lblHowThisProgram, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Main Menu");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				par.showPanel(0);
			}
		});
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.append("This program is used to validate grammars in described written in files and posteriorly used to validate a string, using the grammar selected.\n\n");
		textArea.append("There are 2 types of files:\n.grm are files describing a grammar using the program's convention (See README.txt).\n.ser are previsously compiled files, already validated by the program and ready to be used for string validation.");
		textArea.append("\n\nFor aditional information please see the README file.");
		textArea.setBackground(null);
		JScrollPane scrollPane = new JScrollPane(textArea);

		panel_1.add(scrollPane);
	}
}
