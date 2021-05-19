package com.database.project;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SoldItemTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Items Purchased", "Quantity", "Delivery Date", "Status", "Total"};
	private ArrayList<SoldItem> data;
	private Coupon coupon;

	public SoldItemTableModel(Order order) {
		this.data = new ArrayList<>(order.getSoldItems());
		this.coupon = order.getCoupon();
	}

	public SoldItemTableModel(ArrayList<SoldItem> items, Coupon coupon) {
		this.data = items;
		this.coupon = coupon;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		SoldItem item = data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return item.getItem().getName();
			case 1:
				return item.getItem().getQuantity();
			case 2:
				return item.getDeliveryDate();
			case 3:
				return item.getStatus();
			case 4:
				if (coupon != null) {
					return String.format("%,.2f", (item.getItem().getPrice() * item.getItem().getQuantity()) * (1 - coupon.getDiscount()));
				}
				return String.format("%,.2f", (item.getItem().getPrice() * item.getItem().getQuantity()));
		}
		return "";
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
}

