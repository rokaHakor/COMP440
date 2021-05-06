package com.database.project;

import lombok.Getter;

import javax.swing.*;

public class SignUp {
	@Getter
	public JPanel mainPanel;
	private JTextField usernameField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JPasswordField passwordField;
	private JButton signUpButton;
	private JButton backButton;

	public SignUp(JFrame frame) {
		backButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new LoginView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		signUpButton.addActionListener(e -> {
			User user = new User(firstNameField.getText(), lastNameField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword()));
			DBDriver.addUser(user);
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new LoginView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
	}
}
