package com.database.project;

public class Main {

	public static void main(String[] args) {
		DBDriver.initializeDatabase();

		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
}
