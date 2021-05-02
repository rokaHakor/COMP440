package database.com.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JFrame{
	@Getter
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
				frame.setContentPane(new LoginView(frame).mainPanel);
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
