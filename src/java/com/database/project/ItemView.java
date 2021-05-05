package com.database.project;

import lombok.Getter;

import javax.swing.*;

public class ItemView {
    private JScrollPane scrollPane;
    private JPanel inventoryPanel;
    private JButton logoutButton;
    private JButton orderHistoryButton;
    private JButton cartButton;
    @Getter
    private JPanel mainPanel;
    private JButton returnButton;
    private JSpinner spinner1;
    private JButton addToCartButton;

    public ItemView(JFrame frame){

    }
}
