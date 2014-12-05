package gui;

import java.awt.EventQueue;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class Window extends JFrame {

	/**/
	private static final long serialVersionUID = 7813054481987747538L;
	static final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
	static String filePath; 
	private Vector<JPanel> panels;
	static FileFilter grammarFilter=null;
	static FileFilter validatedFilter=null;

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
		filePath = new String();
		configureFileFilters();
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
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.setFileFilter(Window.grammarFilter);
				int returnVal = Window.fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Window.filePath = Window.fc.getSelectedFile().getPath();
					revalidate();
				}
			}
		});
		mnLoad.add(mntmNewMenuItem);

		JMenuItem mntmGrammar = new JMenuItem("Pre-validated Grammar");
		mntmGrammar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.setFileFilter(Window.validatedFilter);
				int returnVal = Window.fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Window.filePath = Window.fc.getSelectedFile().getPath();
					revalidate();
				}
			}
		});
		mnLoad.add(mntmGrammar);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmGetHelp = new JMenuItem("Help content");
		mntmGetHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPanel(4);
			}
		});
		mnHelp.add(mntmGetHelp);

		JMenuItem mntmAbout = new JMenuItem("About this program");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPanel(3);
			}
		});
		mnHelp.add(mntmAbout);
		showPanel(0);
	}

	private void configureFileFilters() {
		grammarFilter = new FileFilter() {
			public String getDescription() {
				return "Grammar Documents (*.grm)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".grm");
				}
			}
		};
		
		validatedFilter = new FileFilter() {
			public String getDescription() {
				return "Pre-validatd Grammar Documents (*.ser)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".ser");
				}
			}
		};
	}

	public void showPanel(int i) {
		getContentPane().hide();
		panels.get(i).show();
		setContentPane(panels.get(i));
		revalidate();
	}

	private void createPanels() {
		panels = new Vector<JPanel>();
		panels.add(new StartPnl(this));
		panels.add(new ParsePnl(this));
		panels.add(new CYKPnl(this));
		panels.add(new AboutPnl(this));
		panels.add(new HelpPnl(this));
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
			//((ParsePnl) panels.get(1)).runParser();
		}
		else if(extension.equals("ser"))
		{
			showPanel(2);
			//((CYKPnl) panels.get(2)).run();
		}
	}

	public void runCYK() {
		showPanel(2);
	}

}
