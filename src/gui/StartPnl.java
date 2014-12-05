package gui;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;

import common.ComboItem;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class StartPnl extends JPanel {

	/**/
	private static final long serialVersionUID = -7566939539152235393L;
	private Window parent;
	private JComboBox<ComboItem> comboBox;
	JButton btnBrowse;
	boolean fileSelected;

	/*Create the panel.*/
	public StartPnl(Window par) {
		super();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				Object item = comboBox.getSelectedItem();
				String comboxBoxItem =((ComboItem)item).getValue();
				if(!comboxBoxItem.equals(""))
					Window.filePath = comboxBoxItem;
				else if(!btnBrowse.getText().equals(""))
					Window.filePath = btnBrowse.getText();
				else
					Window.filePath = "";
			}
		});
		this.parent = par;
		fileSelected=false;
		setLayout(null);

		JLabel lblChooseGrammar = new JLabel("Choose grammar:");
		lblChooseGrammar.setBounds(10, 48, 132, 14);
		add(lblChooseGrammar);

		comboBox = new JComboBox<ComboItem>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object item = comboBox.getSelectedItem();
				Window.filePath = ((ComboItem)item).getValue();
			}
		});
		comboBox.setBounds(152, 45, 261, 20);
		add(comboBox);

		JLabel lblTestNewGrammar = new JLabel("Browse for new Grammar:");
		lblTestNewGrammar.setBounds(10, 118, 132, 14);
		add(lblTestNewGrammar);

		btnBrowse = new JButton("Browse");
		if(Window.filePath.equals(""))
			btnBrowse.setText("No file selected");
		else
			btnBrowse.setText(Window.filePath);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.fc.setFileFilter(Window.grammarFilter);
				int returnVal = Window.fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Window.filePath = Window.fc.getSelectedFile().getPath();
					btnBrowse.setText(Window.filePath);
					revalidate();
				}
			}});
		btnBrowse.setBounds(152, 114, 261, 23);
		add(btnBrowse);

		JButton btnRun = new JButton("Ok");
		btnRun.setBounds(368, 211, 45, 23);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				parent.runFile(Window.filePath);
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(290, 211, 65, 23);
		add(btnCancel);
		add(btnRun);
		loadGrammars();

	}

	/*AQUI MATIAS*/
	private void loadGrammars() {
		
		File dir = new File(".");
		File [] files = dir.listFiles(new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".ser")|name.endsWith(".grm");
		    }
		});

		comboBox.addItem(new ComboItem("", ""));
		for (File file : files) {
			comboBox.addItem(new ComboItem(new String(file.getName().toCharArray()), new String(file.getAbsolutePath().toCharArray())));
		}
	}
}
