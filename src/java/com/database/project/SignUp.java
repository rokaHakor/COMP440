package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SignUp {
    @Getter
    public JPanel mainPanel;
    private JTextField usernameField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    public SignUp(JFrame frame) {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                username = usernameField.getText();
            }
        });
        firstNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                firstName = firstNameField.getText();
            }
        });
        lastNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                lastName = lastNameField.getText();
            }


        });
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                password = String.valueOf(passwordField.getPassword());
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                User user = new User(firstName, lastName, username, password);
//                DBDriver.addUser(user);
//                System.out.println(user.getAccountName() + " has signed in");
            }
        });
    }
}
