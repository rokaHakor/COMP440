package com.database.project;

import com.database.project.HelperObjects.User;

public class Main {

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);

		DbDriver database = new DbDriver();

		//User user = new User("first", "last", "acct", "pass");
		//database.addUser(user);

		//User user = database.signIn("acct name", "not pass");
		//System.out.println(user.getAccountName() + " has signed in");
	}
}
