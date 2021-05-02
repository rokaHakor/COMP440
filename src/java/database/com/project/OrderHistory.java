package database.com.project;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OrderHistory {


    private JTable table1;
    private JPanel orderHistoryPanel;
    private JTextField OrderHistorySearch;
    @Getter
    private JPanel mainPanel;
    private JScrollPane scrollPane1;
    private JButton ReturnToMain;
    private JPanel refundToOrderHistoryPanel;
    private JButton requestRefundButton;
    private JTextField refundSearch;
    private JButton returnToOrderHistoryPanel;
    private JButton refundSearchButton;
    private JPanel refundFound;
    private JPanel refundNotFound;
    private JTextField textField1;
    private JPanel itemRefundSelect;
    private CardLayout mainScreen = (CardLayout) mainPanel.getLayout();

    public OrderHistory(JFrame frame) {
        table1.setDefaultEditor(Object.class, null);
//        table1.setRowSelectionAllowed(false);
//        table1.setEnabled(false);
        Object[][] data = {
                {1, "Wallet, Headphones", "Washington, DC", 280},
                {2, "GPU", "Ottawa", 32},
                {3, "Power Supply, Camera", "London", 60},
                {4, "Microphone, Headset", "Berlin", 83},
                {5, "Popcorn, Paper, Pens", "Paris", 60},
                {6, "Printer", "Oslo", 4.5},
                {7, "USB Stick", "New Delhi", 1046}
        };
        String[] columnNames
                = {"Order  ID", "Items Purchased", "Order Placed", "Total"};
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
                mainScreen.show(mainPanel, "Card2");
            }
        });
        returnToOrderHistoryPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.show(mainPanel, "Card1");
            }
        });
        refundSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemRefundSelect.setLayout(new java.awt.GridLayout(4, 1));
                String text = refundSearch.getText();
                int orderID = Integer.parseInt(refundSearch.getText())-1;
                Boolean test2;
                try {
                    String[] Items = MyTablemodel.getValueAt(orderID, 1).toString().split(",", 0);
                    for (int i = 0; i < Items.length; i++){
                        JCheckBox checkBox = new JCheckBox(Items[i]);
                    itemRefundSelect.add(checkBox);
//                        refundFound.revalidate();
//                        refundFound.repaint();
                    }
                    mainScreen.show(mainPanel, "Card3");
                }
                catch (Exception x){
                    mainScreen.show(mainPanel, "Card4");
                }
//                for (int i = 0; i < data.length ; i++){
//                    if (text.equals(MyTablemodel.getValueAt(i, 0).toString())){
//                        mainScreen.show(mainPanel, "Card3");
//                        return;
//                    }
//                }

            }
        });
    }

    public JPanel getPanel(){
        return mainPanel;
    }

}

