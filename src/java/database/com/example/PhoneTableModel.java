package database.com.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PhoneTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Id", "Contact", "Type", "Phone Number"};
	private ArrayList<Phone> data;

	public PhoneTableModel() {
		data = new ArrayList<>(Main.phones.values());
	}

	@Override
	public int getRowCount() {
		return Main.phones.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Phone phone = data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return phone.getId();
			case 1:
				Contact contact = Main.contacts.get(phone.getContactId());
				if (contact != null) {
					return contact.getFirstName() + " " + contact.getLastName();
				}
				return "";
			case 2:
				return phone.getType();
			case 3:
				return phone.getPhoneNumber();
		}
		return "";
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
}
