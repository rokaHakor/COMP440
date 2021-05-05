package database.com.project;

public class Main {

	public static void main(String[] args) {
		DBDriver.initializeDatabase();

		MainFrame frame = new MainFrame();
		frame.setVisible(true);

		//User user = new User("first", "last", "acct", "pass");
		//database.addUser(user);

		//User user = database.signIn("acct name", "not pass");
		//System.out.println(user.getAccountName() + " has signed in");

		//Item item = new Item(0, "trash", 50000, 0.00, "");
		//database.addInventoryItem(item);

		//Testing adding/removing supply from a single item's stock in the RetailInventory table.
		//database.updateItemStock(1, -100000);		//should fail because this would make stock go to negatives, which isn't allowed.
		//database.updateItemStock(1, 5);			//Should pass since it doesn't cause negative supply.

		//Testing getting a page of items from RetailInventory
		//ArrayList<Item> itemList = new ArrayList<Item>(database.getInventoryPage(2,5));

		//database.addUser(new User("fake account", "not password"));

		//database.addBankAccount(1, "Bank of America", 15523578);
	}
}