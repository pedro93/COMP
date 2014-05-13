package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AboutPnl extends JPanel {

	private static final long serialVersionUID = -3028984241375915071L;
	private Window par;
	
	public AboutPnl(Window parent) {
		par=parent;
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("About this program");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(6, 3, 0, 0));
		
		JLabel lblDeveloped = new JLabel("  Developed by:");
		panel_1.add(lblDeveloped);
		lblDeveloped.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblNewLabel_1 = new JLabel("Carlos Matias");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Luis Abreu");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_2);
		
		JLabel lblPavel = new JLabel("Pavel Alexeenko");
		lblPavel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblPavel);
		
		JLabel lblPedroSilva = new JLabel("Pedro Silva");
		lblPedroSilva.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblPedroSilva);
		
		JLabel lblNewLabel_3 = new JLabel("As a project in the compiler course\r\n MIEIC FEUP 2012/2014 Porto,Portugal");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_3);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnBack = new JButton("Main Menu");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				par.showPanel(0);
			}
		});
		panel.add(btnBack);

	}
}
