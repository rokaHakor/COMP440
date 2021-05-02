package database.com.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;

public class ContactTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Id", "First Name", "Last Name", "Gender"};
	private ArrayList<Contact> data;

	public ContactTableModel() {
		data = new ArrayList<>(Main.contacts.values());
	}

	@Override
	public int getRowCount() {
		return Main.contacts.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Arrays.stream(data.get(rowIndex).toString().split(",")).toArray()[columnIndex];
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
}
