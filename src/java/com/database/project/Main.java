package com.database.project;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Main {

	@Getter
	@Setter
	private static User user = null;

	public static void main(String[] args) {
		DBDriver.initializeDatabase();

		MainFrame frame = new MainFrame();
		frame.setVisible(true);



	}
}
