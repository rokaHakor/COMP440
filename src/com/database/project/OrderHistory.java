package com.database.project;

import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OrderHistory {
    private JTable table1;
    private JScrollPane scrollPane1;
    private JPanel OrderHistoryPanel;
    private JButton ReturnToMain;
    private JTextField OrderHisttorySearch;


    public OrderHistory() {
        OrderHisttorySearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

//                ReturnToMain.setText(OrderHisttorySearch.getText());
            }
        });
    }

    public JPanel getPanel(){
        init();
        return OrderHistoryPanel;
    }

    public void init(){
        DefaultTableModel MyTablemodel = new DefaultTableModel();
        MyTablemodel.addColumn("Order  ID");
        MyTablemodel.addColumn("Order Placed");
        MyTablemodel.addColumn("Total");
        table1.setModel(MyTablemodel);
        return;
    }

}

