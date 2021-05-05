package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView extends JFrame{
    @Getter
    public JPanel mainPanel;
    private JPanel signUpPanel;
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
                //System.out.println(username + " " + password);
//                DBDriver com.database = null;
//                User user = com.database.signIn(username, password);
//                System.out.println(user.getAccountName() + " " + user.getPassword());


            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().remove(frame.getContentPane());
                frame.setContentPane(new SignUp(frame).signUpPanel);
                frame.revalidate();
                frame.repaint();

            }
        });
    }
}
