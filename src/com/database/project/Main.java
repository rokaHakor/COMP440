package com.database.project;

public class Main {

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);

		DBDriver.initializeDatabase();

		Address address = new Address("421 Brand", "Glendale", "CA", "US");

		DBDriver.addAddressFull(2, address);
	}
}
