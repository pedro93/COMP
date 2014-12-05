package gui;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Vector;

import algorithm.CYK;

public class CYKPnl extends JPanel {

	private static final long serialVersionUID = 8168510640830905489L;
	private JTextField textField;
	private JPanel AlgPnl;
	private Vector<String> toProcess;
	Vector<JPanel> panels;
	private CYK algorithm;
	private Window parent;
	private boolean pause,cancel;
	private JButton btnPause;
	private JButton runButton;

	public CYKPnl(Window window) {
		pause=true;
		cancel=false;
		this.parent = window;
		toProcess = new Vector<String>();
		panels = new Vector<JPanel>();

		setLayout(new BorderLayout(0, 0));

		AlgPnl = new JPanel();
		add(AlgPnl, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblInputString = new JLabel("  Input String : ");
		lblInputString.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblInputString, BorderLayout.WEST);

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.EAST);

		btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if(pause)
				{
					btnPause.setText("Resume");
					pause=false;
					revalidate();
					algorithm.suspend();
				}
				else
				{
					btnPause.setText("Pause");
					pause=true;
					revalidate();
					algorithm.resume();
				}
			}
		});
		btnPause.setEnabled(false);
		panel_1.add(btnPause);

		runButton = new JButton("Run");
		panel_1.add(runButton);
		runButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if(!cancel)
				{
					if(algorithm==null||!algorithm.isAlive())
					{
						String inputString = textField.getText();
						toProcess = CYK.splitString(inputString);
						createGridPanels();
						run();
					}
					runButton.setText("Cancel");
					cancel=true;
					revalidate();
				}
				else
				{
					btnPause.setEnabled(false);
					algorithm.stop();
					cancel=true;
					clear();
					parent.showPanel(0);
				}

			}
		});
		runButton.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblNewLabel = new JLabel("CYK Algorithm Animation");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);
	}

	protected void createGridPanels() {
		AlgPnl.setLayout(new GridLayout(toProcess.size(), toProcess.size(), 4, 4));
		for(int i = 0; i < toProcess.size(); i++){
			for(int j = 0; j < toProcess.size(); j++)
			{
				JPanel cellPanel = new JPanel();
				//cellPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
				panels.add(cellPanel);
				AlgPnl.add(cellPanel);
			}
		}
		revalidate();
	}

	public void run() {
		btnPause.setEnabled(true);
		algorithm = new CYK(this, 200);
		algorithm.loadGrammar(Window.filePath);
		algorithm.setup(toProcess,AlgPnl,panels);
		algorithm.start();
	}

	public void showOutput() {
		int n;
		if(algorithm.isSuccessful)
			n = JOptionPane.showConfirmDialog(this, "GREAT SUCCESS\nInput String is valid using the grammar choosen!\nReturn to main menu?","Algorithm output", JOptionPane.YES_NO_OPTION);
		else
			n = JOptionPane.showConfirmDialog(this, "Nope, not valid\nReturn to main menu?","Algorithm output",JOptionPane.YES_NO_OPTION);

		if(n==JOptionPane.YES_OPTION)
		{
			clear();
			parent.showPanel(0);
		}
	}

	private void clear()
	{
		for(JPanel pan:panels)
			AlgPnl.remove(pan);
		AlgPnl.revalidate();
		toProcess.clear();
		panels.clear();
		algorithm.interrupt();
		algorithm.reset();
		textField.setText("");
		runButton.setText("Run");
		btnPause.setText("Pause");
		btnPause.setEnabled(false);
		cancel = false;
		pause=true;
		algorithm=null;
		System.gc();
	}
}
