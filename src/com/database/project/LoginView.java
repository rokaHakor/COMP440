package com.database.project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView {
    public JPanel panel1;
    private JButton registerButton;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private String username;
    private String password;

    public LoginView(JFrame frame){

        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            public void focusLost(FocusEvent e) {
                username = usernameField.getText();
            }
        });
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            public void focusLost(FocusEvent e){
                password = String.valueOf(passwordField.getPassword());
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(username + " " + password);
            }
        });
    }
}
