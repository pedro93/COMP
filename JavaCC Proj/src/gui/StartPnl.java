package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class StartPnl extends JPanel {

	/**/
	private static final long serialVersionUID = -7566939539152235393L;
	private Window parent;
	private JComboBox<String> comboBox;
	/*Create the panel.*/
	public StartPnl(Window parent) {
		this.parent = parent;
		loadGrammars();
		setLayout(null);
		
		JLabel lblChooseGrammar = new JLabel("Choose grammar:");
		lblChooseGrammar.setBounds(29, 48, 113, 14);
		add(lblChooseGrammar);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(171, 45, 123, 20);
		add(comboBox);
		
		JLabel lblTestNewGrammar = new JLabel("Test new Grammar:");
		lblTestNewGrammar.setBounds(29, 118, 113, 14);
		add(lblTestNewGrammar);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(171, 114, 123, 23);
		add(btnBrowse);
		
		JButton btnRun = new JButton("Ok");
		btnRun.setBounds(368, 211, 45, 23);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(290, 211, 65, 23);
		add(btnCancel);
		add(btnRun);

	}
	
	/*AQUI MATIAS*/
	private void loadGrammars() {
		
	}

}
