package com.database.project;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);

		DbDriver database = new DbDriver();

		//User user = new User("first", "last", "acct", "pass");
		//database.addUser(user);

		//User user = database.signIn("acct name", "not pass");
		//System.out.println(user.getAccountName() + " has signed in");

		//Item item = new Item(0, "trash", 50000, 0.00, "");
		//database.addInventoryItem(item);

		//update tests
		//database.updateItemStock(1, -100000);	//should fail because this would make stock go to negatives, which isn't allowed.
		//database.updateItemStock(1, 5);			//Should pass since it doesn't cause negative supply.

		ArrayList<Item> itemList = new ArrayList<Item>(database.getInventoryPage(2,5));

	}
}
