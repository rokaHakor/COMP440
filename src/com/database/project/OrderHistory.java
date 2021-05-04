package com.database.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;

public class OrderHistory {
    private JTable table1;
    private JPanel orderHistoryPanel;
    private JTextField OrderHistorySearch;
    private JPanel panelContainer;
    private JScrollPane scrollPane1;
    private JButton ReturnToMain;
    private JButton requestRefundButton;
    private JPanel refundFound;
    private JPanel refundNotFound;
    private JPanel itemRefundSelect;
    private JButton returnToOrderHistoryButton;
    private JButton returnButton;
    private JTextField textFieldTest;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton cartButton;
    private JButton orderHistoryButton;
    private JPanel mainPanel;
    private JButton logoutButton;
    private CardLayout mainScreen = (CardLayout) panelContainer.getLayout();
    private String itemsToRefund = "";
    public OrderHistory(JFrame frame) {
        table1.setDefaultEditor(Object.class, null);
        table2.setDefaultEditor(Object.class, null);
        Object[][] data = {
                {1, "Wallet, Headphones", "01/19/2021", "280"},
                {2, "GPU", "01/30/2021", "32"},
                {3, "Power Supply, Camera", "02/12/2021", "60"},
                {4, "Microphone, Headset", "02/15/2021", "83"},
                {5, "Popcorn, Paper, Pens", "03/12/2021", "60"},
                {6, "Printer", "03/21/2021", "4.5"},
                {7, "USB Stick", "04/05/2021", "1046"}
        };
        String[] columnNames
                = {"Order  ID", "Items Purchased", "Order Placed", "Total"};
        String[] columnNames2
                = {"Items Purchased", "Price"};
        DefaultTableModel MyTablemodel = new DefaultTableModel(data, columnNames);
        table1.setModel(MyTablemodel);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter(table1.getModel());
        table1.setRowSorter(rowSorter);

        OrderHistorySearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                String text = OrderHistorySearch.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        requestRefundButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                itemsToRefund = "";
                itemRefundSelect.removeAll();
                itemRefundSelect.setLayout(new java.awt.GridLayout(20, 1));
                try {
                    String[] Items = MyTablemodel.getValueAt(table1.getSelectedRow(), 1).toString().split(",", 0);
                    for (int i = 0; i < Items.length; i++){
                        JCheckBox checkBox = new JCheckBox(Items[i]);
                        checkBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (!itemsToRefund.contains(checkBox.getText())){
                                    itemsToRefund += checkBox.getText() + " ";
                                }
                                else {
                                    itemsToRefund = itemsToRefund.replaceAll(checkBox.getText(), "");
                                }
                                itemsToRefund = itemsToRefund.replaceAll("  ", " ");
                                textFieldTest.setText(itemsToRefund);
                            }
                        });
                    itemRefundSelect.add(checkBox);
                    }
                    itemRefundSelect.revalidate();
                    itemRefundSelect.repaint();
                    mainScreen.show(panelContainer, "Card3");
                }
                catch (Exception x){
                    mainScreen.show(panelContainer, "Card4");
                }
            }
        });
        returnToOrderHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.show(panelContainer, "Card1");
            }
        });
        ReturnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().remove(frame.getContentPane());
                frame.setContentPane(new BrowseView(frame).getMainPanel());
                frame.revalidate();
                frame.repaint();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.show(panelContainer, "Card1");
            }
        });
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    String[] Items = MyTablemodel.getValueAt(table1.getSelectedRow(), 1).toString().split(",", 0);
                Object[][] data2 = new Object[Items.length][2];
                    System.out.println(Items.length);
                    for (int i = 0; i < Items.length; i++) {
                        Items[i] = Items[i].replaceAll(" ", "");
                        data2[i][0] = Items[i];
                        data2[i][1] = "100" + i;
                    }
                DefaultTableModel MyTablemodel2 = new DefaultTableModel(data2,columnNames2);
                table2.setModel(MyTablemodel2);
                }
            }
        });
    }

    public JPanel getPanel(){
        return mainPanel;
    }

}

