package com.database.project;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
	@Getter
	private JPanel mainPanel;
	private JButton logoutButton;
	private JButton refundButton;
	private final CardLayout mainScreen = (CardLayout) panelContainer.getLayout();
	private final ArrayList<SoldItem> itemsToRefund = new ArrayList<>();
	private OrderTableModel orderTableModel;

	public OrderHistory(JFrame frame) {

		orderTableModel = new OrderTableModel();
		table1.setModel(orderTableModel);

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table1.getModel());
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

		refundButton.addActionListener(e -> {
			for (SoldItem item : itemsToRefund) {
				DBDriver.setOrderStatus(item.getItem().getItemID(), "Refunded");
			}
			itemsToRefund.clear();
			orderTableModel = new OrderTableModel();
			table1.setModel(orderTableModel);
			mainScreen.show(panelContainer, "Card1");
		});

		requestRefundButton.addActionListener(e -> {
			textFieldTest.setText(getRefundItems());
			itemRefundSelect.removeAll();
			itemRefundSelect.setLayout(new GridLayout(20, 1));

			int index = table1.getSelectedRow();
			if (index < 0) {
				mainScreen.show(panelContainer, "Card4");
				return;
			}

			Order order = orderTableModel.getOrder(table1.getSelectedRow());

			for (SoldItem item : order.getSoldItems()) {
				JCheckBox checkBox = new JCheckBox(item.getItem().getName());
				checkBox.addActionListener(e1 -> {
					if (checkBox.isSelected()) {
						itemsToRefund.add(item);
					} else {
						itemsToRefund.remove(item);
					}
					textFieldTest.setText(getRefundItems());
				});
				itemRefundSelect.add(checkBox);
			}

			itemRefundSelect.revalidate();
			itemRefundSelect.repaint();
			mainScreen.show(panelContainer, "Card3");
		});

		returnToOrderHistoryButton.addActionListener(e -> mainScreen.show(panelContainer, "Card1"));

		ReturnToMain.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new BrowseView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		returnButton.addActionListener(e -> mainScreen.show(panelContainer, "Card1"));

		table1.getSelectionModel().addListSelectionListener(event -> {
			if (!event.getValueIsAdjusting()) {
				int index = table1.getSelectedRow();
				if (index < 0) {
					return;
				}
				Order order = orderTableModel.getOrder(table1.getSelectedRow());
				table2.setModel(new SoldItemTableModel(order));
			}
		});

		logoutButton.addActionListener(e -> {
			Cart.getCart().clear();
			Main.setUser(null);
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new LoginView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		cartButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new CartView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
	}

	private String getRefundItems() {
		String items = "";
		for (SoldItem item : itemsToRefund) {
			if (items.isEmpty()) {
				items = item.getItem().getName();
			} else {
				items = items + ", " + item.getItem().getName();
			}
		}
		return items;
	}
}
