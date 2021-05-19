package com.database.project;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class OrderTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Order ID", "Items Purchased", "Order Date", "Coupon", "Total"};
	private ArrayList<Order> data;

	public OrderTableModel() {
		data = new ArrayList<>(DBDriver.getOrderHistory(Main.getUser().getId(), 1, 100));
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	public Order getOrder(int index) {
		return data.get(index);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Order order = data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return order.getOrderID();
			case 1:
				String items = "";
				for (SoldItem item : order.getSoldItems()) {
					if (items.isEmpty()) {
						items = item.getItem().getName();
					} else {
						items = items + ", " + item.getItem().getName();
					}
				}
				return items;
			case 2:
				return order.getOrderDate();
			case 3:
				if (order.getCoupon() != null) {
					return order.getCoupon().getCode();
				}
				return "N/A";
			case 4:
				double total = 0.0;
				for (SoldItem item : order.getSoldItems()) {
					if (!item.getStatus().equals("Refunded")) {
						total += item.getItem().getPrice() * item.getItem().getQuantity();
					}
				}
				if (order.getCoupon() != null) {
					total = total * (1 - order.getCoupon().getDiscount());
				}
				return String.format("%,.2f", total);
		}
		return "";
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
}
