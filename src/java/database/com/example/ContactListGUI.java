package database.com.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ContactListGUI {
	public JPanel mainPanel;
	private JTable contactList;
	private JTabbedPane tabbedPane1;
	private JTable addressList;
	private JTable phoneList;
	private JTextField searchField;
	private JButton createButton;
	private JButton deleteButton;
	private JButton editButton;
	private final TableRowSorter<TableModel> contactRowSorter;
	private final TableRowSorter<TableModel> addressRowSorter;
	private final TableRowSorter<TableModel> phoneRowSorter;

	public ContactListGUI(JFrame frame) {
		contactList.setModel(new ContactTableModel());
		addressList.setModel(new AddressTableModel());
		phoneList.setModel(new PhoneTableModel());

		createButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new CreateForm(frame).panel1);
			frame.revalidate();
			frame.repaint();
		});

		editButton.addActionListener(e -> {
			int x = tabbedPane1.getSelectedIndex();
			if (x == 0) {
				if(contactList.getSelectedRow() < 0){
					return;
				}
				Contact contact = Main.contacts.get(Integer.parseInt("" + contactList.getValueAt(contactList.getSelectedRow(), 0)));
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new CreateForm(frame, contact).panel1);
				frame.revalidate();
				frame.repaint();
			}
			if (x == 1) {
				if(addressList.getSelectedRow() < 0){
					return;
				}
				Address address = Main.addresses.get(Integer.parseInt("" + addressList.getValueAt(addressList.getSelectedRow(), 0)));
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new CreateForm(frame, address).panel1);
				frame.revalidate();
				frame.repaint();
			}
			if (x == 2) {
				if(phoneList.getSelectedRow() < 0){
					return;
				}
				Phone phone = Main.phones.get(Integer.parseInt("" + phoneList.getValueAt(phoneList.getSelectedRow(), 0)));
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new CreateForm(frame, phone).panel1);
				frame.revalidate();
				frame.repaint();
			}
		});

		deleteButton.addActionListener(e -> {
			int x = tabbedPane1.getSelectedIndex();
			if (x == 0) {
				if(contactList.getSelectedRow() < 0){
					return;
				}
				Contact contact = Main.contacts.get(Integer.parseInt("" + contactList.getValueAt(contactList.getSelectedRow(), 0)));
				for(int addressId: contact.addresses){
					Main.addresses.remove(addressId);
				}
				for(int phoneId: contact.phones){
					Main.phones.remove(phoneId);
				}
				Main.contacts.remove(Integer.parseInt("" + contactList.getValueAt(contactList.getSelectedRow(), 0)));
				contactList.setModel(new ContactTableModel());
				addressList.setModel(new AddressTableModel());
				phoneList.setModel(new PhoneTableModel());
			}
			if (x == 1) {
				if(addressList.getSelectedRow() < 0){
					return;
				}
				Main.addresses.remove(Integer.parseInt("" + addressList.getValueAt(addressList.getSelectedRow(), 0)));
				addressList.setModel(new AddressTableModel());
			}
			if (x == 2) {
				if(phoneList.getSelectedRow() < 0){
					return;
				}
				Main.phones.remove(Integer.parseInt("" + phoneList.getValueAt(phoneList.getSelectedRow(), 0)));
				phoneList.setModel(new PhoneTableModel());
			}
			Main.saveDatabase();
		});


		contactRowSorter = new TableRowSorter<>(contactList.getModel());
		addressRowSorter = new TableRowSorter<>(addressList.getModel());
		phoneRowSorter = new TableRowSorter<>(phoneList.getModel());
		contactList.setRowSorter(contactRowSorter);
		addressList.setRowSorter(addressRowSorter);
		phoneList.setRowSorter(phoneRowSorter);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = searchField.getText();
				if (text.trim().length() == 0) {
					contactRowSorter.setRowFilter(null);
					addressRowSorter.setRowFilter(null);
					phoneRowSorter.setRowFilter(null);
				} else {
					int x = tabbedPane1.getSelectedIndex();
					if (x == 0) {
						contactRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					if (x == 1) {
						addressRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					if (x == 2) {
						phoneRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = searchField.getText();
				if (text.trim().length() == 0) {
					contactRowSorter.setRowFilter(null);
					addressRowSorter.setRowFilter(null);
					phoneRowSorter.setRowFilter(null);
				} else {
					int x = tabbedPane1.getSelectedIndex();
					if (x == 0) {
						contactRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					if (x == 1) {
						addressRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					if (x == 2) {
						phoneRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
	}
}
