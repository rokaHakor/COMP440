package com.database.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class AddressTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Id", "Contact", "Address One", "Address Two", "Apt", "City", "State", "Country"};
	private ArrayList<Address> data;

	public AddressTableModel() {
		data = new ArrayList<>(Main.addresses.values());
	}

	@Override
	public int getRowCount() {
		return Main.addresses.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Address address = data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return address.getId();
			case 1:
				Contact contact = Main.contacts.get(address.getContactId());
				if (contact != null) {
					return contact.getFirstName() + " " + contact.getLastName();
				}
				return "";
			case 2:
				return address.getAddressOne();
			case 3:
				return address.getAddressTwo();
			case 4:
				return address.getApt();
			case 5:
				return address.getCity();
			case 6:
				return address.getState();
			case 7:
				return address.getCountry();
		}
		return "";
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
}
