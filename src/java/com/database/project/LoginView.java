package com.database.project;

import lombok.Getter;

import javax.swing.*;

public class LoginView extends JFrame {
	@Getter
	public JPanel mainPanel;
	private JPanel signUpPanel;
	private JButton registerButton;
	private JButton loginButton;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JLabel invalidLabel;

	public LoginView(JFrame frame) {
		invalidLabel.setVisible(false);

		loginButton.addActionListener(e -> {
			Main.setUser(DBDriver.signIn(usernameField.getText(), String.valueOf(passwordField.getPassword())));
			if (Main.getUser() != null) {
				DBDriver.getCart(Main.getUser().getId());
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new BrowseView(frame).getMainPanel());
				frame.revalidate();
				frame.repaint();
			} else {
				invalidLabel.setVisible(true);
			}
		});
		registerButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new SignUp(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});
	}
}
