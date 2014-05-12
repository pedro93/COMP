package gui;

import javax.swing.JOptionPane;
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
	private CNFParser parser=null;

	
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
		textArea.setLineWrap(true); //Auto down line if the line is too long
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		//textArea.setForeground(Color.cyan);
		JScrollPane scrollPane = new JScrollPane( textArea );
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		add( scrollPane,BorderLayout.CENTER );

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JButton btnCanel = new JButton("Cancel");
		btnCanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Console.clear();
				parent.showPanel(0);
			}
		});
		panel.add(btnCanel);

		btnRunAlgorithm = new JButton("Run Algorithm");
		btnRunAlgorithm.setEnabled(false);
		btnRunAlgorithm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Console.clear();
				parent.runCYK();
			}
		});
		
		JButton btnCompileGrammar = new JButton("Compile Grammar");
		btnCompileGrammar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runParser();
			}
		});
		panel.add(btnCompileGrammar);
		panel.add(btnRunAlgorithm);
		setVisible(true);     
	}

	private void runParser() {
		Console.redirectOutput( textArea );
		try {
			parser = new CNFParser(Window.filePath);
			parser.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(parser!=null)
			if(parser.isValid)
			{
				btnRunAlgorithm.setEnabled(true);
				Window.filePath=parser.newFilePath;
				return;
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Grammar is not valid, please check console output for more information");
			}
		//stay in this panel or return
	}

}
