package com.database.project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JFrame{
	private JPanel mainPanel;
	//private JPanel SplashPanel;
	private JLabel retailApplicationLabel;
	private JButton continueButton;
	private JButton exitButton;

	public MainPanel(JFrame frame) {
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new LoginView(frame).panel1);
				frame.revalidate();
				frame.repaint();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	public JPanel getPanel(){
		return mainPanel;
	}
}
