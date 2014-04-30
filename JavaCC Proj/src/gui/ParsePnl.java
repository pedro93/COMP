package gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import parser.CNFParser;

import java.awt.Component;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ParsePnl extends JPanel {

	/**/
	private static final long serialVersionUID = -6212467859906377049L;
	/* Create the panel */
	private Window parent;
	private JTextArea textArea;
	private JButton btnRunAlgorithm;
	public ParsePnl(Window par) {
		super();
		this.parent = par;

		setLayout(new BorderLayout(0, 0));

		JLabel lblConsoleOutput = new JLabel("Console Output");
		lblConsoleOutput.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblConsoleOutput.setVerticalAlignment(SwingConstants.TOP);
		lblConsoleOutput.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblConsoleOutput, BorderLayout.NORTH);

		textArea= new JTextArea(20,35);
		textArea.setEnabled(false);
		textArea.setLineWrap(true); //Auto down line if the line is too long
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane( textArea );
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		add( scrollPane,BorderLayout.CENTER );

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JButton btnCanel = new JButton("Cancel");
		btnCanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.showPanel(0);
			}
		});
		panel.add(btnCanel);

		btnRunAlgorithm = new JButton("Run Algorithm");
		btnRunAlgorithm.setEnabled(false);
		btnRunAlgorithm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.runCYK();
			}
		});
		panel.add(btnRunAlgorithm);
		setVisible(true);     
	}

	public void runParser() {
		Console.redirectOutput( textArea );
		CNFParser parser=null;
		try {
			parser = new CNFParser(parent.filePath);
			parser.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(parser!=null)
			if(parser.isValid)
			{
				parser.saveGrammarToFile();
				btnRunAlgorithm.setEnabled(true); 
				return; //enabled next button
			}

		//stay in this panel or return
	}

}
