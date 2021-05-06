package com.database.example;

import java.io.*;
import java.util.HashMap;

public class Main {

	public static final HashMap<Integer, Contact> contacts = new HashMap<>();
	public static final HashMap<Integer, Address> addresses = new HashMap<>();
	public static final HashMap<Integer, Phone> phones = new HashMap<>();
	public static int contactIndex = 0;
	public static int addressIndex = 0;
	public static int phoneIndex = 0;

	private static void loadDatabase() {
		File phoneFile = new File("Phone.txt");
		File addressFile = new File("Address.txt");
		File contactFile = new File("Contact.txt");
		try {
			if (phoneFile.createNewFile()) {
				System.out.println("File created: " + phoneFile.getName());
			}
			if (addressFile.createNewFile()) {
				System.out.println("File created: " + addressFile.getName());
			}
			if (contactFile.createNewFile()) {
				System.out.println("File created: " + contactFile.getName());
			}
			BufferedReader reader = new BufferedReader(new FileReader(contactFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("Index:")) {
					contactIndex = Integer.parseInt(line.replaceAll("\\D+", ""));
				} else {
					Contact contact = new Contact(line);
					contacts.put(contact.getId(), contact);
				}
			}
			reader.close();
			reader = new BufferedReader(new FileReader(addressFile));
			while ((line = reader.readLine()) != null) {
				if (line.contains("Index:")) {
					addressIndex = Integer.parseInt(line.replaceAll("\\D+", ""));
				} else {
					Address address = new Address(line);
					addresses.put(address.getId(), address);
					if (contacts.containsKey(address.getContactId())) {
						contacts.get(address.getContactId()).addresses.add(address.getId());
					}
				}
			}
			reader.close();
			reader = new BufferedReader(new FileReader(phoneFile));
			while ((line = reader.readLine()) != null) {
				if (line.contains("Index:")) {
					phoneIndex = Integer.parseInt(line.replaceAll("\\D+", ""));
				} else {
					Phone phone = new Phone(line);
					phones.put(phone.getId(), phone);
					if (contacts.containsKey(phone.getContactId())) {
						contacts.get(phone.getContactId()).phones.add(phone.getId());
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveDatabase() {
		File contactFile = new File("Contact.txt");
		File addressFile = new File("Address.txt");
		File phoneFile = new File("Phone.txt");
		try {
			if (contactFile.createNewFile()) {
				System.out.println("File created: " + contactFile.getName());
			}
			if (addressFile.createNewFile()) {
				System.out.println("File created: " + addressFile.getName());
			}
			if (phoneFile.createNewFile()) {
				System.out.println("File created: " + phoneFile.getName());
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(contactFile));
			writer.write("Index:" + contactIndex);
			for (Contact contact : contacts.values()) {
				writer.newLine();
				writer.write(contact.toString());
			}
			writer.close();
			writer = new BufferedWriter(new FileWriter(addressFile));
			writer.write("Index:" + addressIndex);
			for (Address address : addresses.values()) {
				writer.newLine();
				writer.write(address.toString());
			}
			writer.close();
			writer = new BufferedWriter(new FileWriter(phoneFile));
			writer.write("Index:" + phoneIndex);
			for (Phone phone : phones.values()) {
				writer.newLine();
				writer.write(phone.toString());
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main.loadDatabase();
		Main.saveDatabase();
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
}

/*
    Create Contact
    Edit Contact
    Search Contact
    Delete Contact
    Exit
 */