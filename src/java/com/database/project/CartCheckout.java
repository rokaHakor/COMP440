package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CartCheckout extends JFrame{
    private JTextField couponCode;
    private JTextField addressText;
    private JTextField bankAccountNumber;
    private JButton submitButton;
    @Getter
	private JPanel mainPanel;
	private String coupon;
    private String address;
    private String bankAccount;

    public CartCheckout(JFrame frame){

        couponCode.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                coupon = couponCode.getText();
            }
        });
        addressText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                address = addressText.getText();
            }
        });
        bankAccountNumber.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                bankAccount = bankAccountNumber.getText();
            }

        });
    }
}
