package com.database.example;

import javax.swing.*;

public class CreateForm {
	public JPanel panel1;
	private JPanel createPanel;
	private JTabbedPane tabbedPane1;
	private JButton doneButton;
	private JButton cancelButton;
	private JTextField firstName;
	private JTextField lastName;
	private JTextField gender;
	private JTextField addressOne;
	private JTextField addressTwo;
	private JTextField apt;
	private JTextField city;
	private JTextField state;
	private JTextField country;
	private JComboBox<Object> addressContacts;
	private JTextField phoneNumber;
	private JTextField type;
	private JComboBox<Object> phoneContacts;

	public CreateForm(JFrame frame) {
		addressContacts.setModel(new DefaultComboBoxModel<>(Main.contacts.values().toArray()));
		phoneContacts.setModel(new DefaultComboBoxModel<>(Main.contacts.values().toArray()));

		cancelButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});

		doneButton.addActionListener(e -> {
			int x = tabbedPane1.getSelectedIndex();
			if (x == 0) {
				Main.contactIndex++;
				Main.contacts.put(Main.contactIndex, new Contact(Main.contactIndex, firstName.getText(), lastName.getText(), gender.getText()));
				Main.saveDatabase();
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new ContactListGUI(frame).mainPanel);
				frame.revalidate();
				frame.repaint();
			}
			if (x == 1) {
				int contactId = ((Contact) addressContacts.getSelectedItem()).getId();
				Main.addressIndex++;
				Main.addresses.put(Main.addressIndex, new Address(Main.addressIndex, addressOne.getText(), addressTwo.getText(),
						Integer.parseInt(apt.getText().replaceAll("[^0-9]", "")), city.getText(), state.getText(),
						country.getText(), contactId));
				Main.contacts.get(contactId).addresses.add(Main.addressIndex);
				Main.saveDatabase();
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new ContactListGUI(frame).mainPanel);
				frame.revalidate();
				frame.repaint();
			}
			if (x == 2) {
				int contactId = ((Contact) phoneContacts.getSelectedItem()).getId();
				Main.phoneIndex++;
				Main.phones.put(Main.phoneIndex, new Phone(Main.phoneIndex, type.getText(), phoneNumber.getText(), contactId));
				Main.contacts.get(contactId).phones.add(Main.phoneIndex);
				Main.saveDatabase();
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new ContactListGUI(frame).mainPanel);
				frame.revalidate();
				frame.repaint();
			}
		});
	}

	public CreateForm(JFrame frame, Contact contact) {
		tabbedPane1.remove(1);
		tabbedPane1.remove(1);

		firstName.setText(contact.getFirstName());
		lastName.setText(contact.getLastName());
		gender.setText(contact.getGender());

		cancelButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});

		doneButton.addActionListener(e -> {
			Main.contacts.put(contact.getId(), new Contact(contact.getId(), firstName.getText(), lastName.getText(), gender.getText()));
			Main.saveDatabase();
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});
	}

	public CreateForm(JFrame frame, Address address) {
		tabbedPane1.remove(0);
		tabbedPane1.remove(1);
		addressContacts.setModel(new DefaultComboBoxModel<>(Main.contacts.values().toArray()));

		addressOne.setText(address.getAddressOne());
		addressTwo.setText(address.getAddressTwo());
		apt.setText("" + address.getApt());
		city.setText(address.getCity());
		state.setText(address.getState());
		country.setText(address.getCountry());
		addressContacts.setSelectedItem(Main.contacts.get(address.getContactId()));

		cancelButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});

		doneButton.addActionListener(e -> {
			int contactId = ((Contact) addressContacts.getSelectedItem()).getId();
			Main.addresses.put(address.getId(), new Address(address.getId(), addressOne.getText(), addressTwo.getText(),
					Integer.parseInt(apt.getText().replaceAll("[^0-9]", "")), city.getText(), state.getText(),
					country.getText(), contactId));
			Main.contacts.get(address.getContactId()).addresses.remove(Integer.valueOf(address.getId()));
			Main.contacts.get(contactId).addresses.add(Main.addressIndex);
			Main.saveDatabase();
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});
	}

	public CreateForm(JFrame frame, Phone phone) {
		tabbedPane1.remove(0);
		tabbedPane1.remove(0);
		phoneContacts.setModel(new DefaultComboBoxModel<>(Main.contacts.values().toArray()));

		type.setText(phone.getType());
		phoneNumber.setText(phone.getPhoneNumber());
		phoneContacts.setSelectedItem(Main.contacts.get(phone.getContactId()));

		cancelButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});

		doneButton.addActionListener(e -> {
			int contactId = ((Contact) phoneContacts.getSelectedItem()).getId();
			Main.phones.put(phone.getId(), new Phone(phone.getId(), type.getText(), phoneNumber.getText(), contactId));
			Main.contacts.get(phone.getContactId()).phones.remove(Integer.valueOf(phone.getId()));
			Main.contacts.get(contactId).phones.add(Main.phoneIndex);
			Main.saveDatabase();
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new ContactListGUI(frame).mainPanel);
			frame.revalidate();
			frame.repaint();
		});
	}
}
