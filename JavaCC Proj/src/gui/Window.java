package gui;

import java.awt.EventQueue;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;


public class Window extends JFrame {

	/**/
	private static final long serialVersionUID = 7813054481987747538L;
	private Vector<JPanel> panels;

	/*Launch the application.*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
				    UIManager.getSystemLookAndFeelClassName());
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*Create the frame*/
	public Window() {
		super();
	    
		createPanels();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnLoad = new JMenu("Load");
		mnFile.add(mnLoad);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New Grammar");
		mnLoad.add(mntmNewMenuItem);
		
		JMenuItem mntmGrammar = new JMenuItem("Pre-validated Grammar");
		mnLoad.add(mntmGrammar);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmGetHelp = new JMenuItem("Get Help!!!");
		mnHelp.add(mntmGetHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		showPanel(1);
		runFile("cenas.grm");
	}

	public void showPanel(int i) {
		setContentPane(panels.get(i));
	}

	private void createPanels() {
		panels = new Vector<JPanel>();
		panels.add(new StartPnl(this));
		panels.add(new ParsePnl(this));
	}
	
	public void runFile(String filePath){
		String extension = "";

		int i = filePath.lastIndexOf('.');
		if (i >= 0) {
		    extension = filePath.substring(i+1);
		}
		if(extension.equals("grm")) //grammar file to be tested
		{
			showPanel(1);
			((ParsePnl) panels.get(1)).runParser();
		}
	}

	public void runCYK() {
		//shows CYK panel, enter string -> run algorithm with animation;		
	}

}
