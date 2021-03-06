package com.database.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class DBDriver {

	// JDBC driver name and com.database URL
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static String DB_NAME = "mydb";

	//  Database credentials.
	private static String USER = "username";
	private static String PASS = "password";

	private static Connection connection;
	private static PreparedStatement statement;
	private static Statement createStatement;      //Used for sending queries to MySql that create the main com.database and tables.

	private DBDriver() {

	}

	public static void initializeDatabase() {
		try {
			File fileObj = new File("Database Config.txt");
			if (fileObj.createNewFile()) {
				System.out.println("Created Database Config.txt file");
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileObj));
				writer.write("Database Name: retaildb");
				writer.newLine();
				writer.write("User Name: ");
				writer.newLine();
				writer.write("Password: ");
				writer.close();
			}

			Scanner reader = new Scanner(fileObj);

			while (reader.hasNextLine()) {
				String line = reader.nextLine();

				String[] splitstr = line.split(":");

				//remove whitespace from the input area.
				if (splitstr.length > 1) {
					splitstr[1] = splitstr[1].replaceAll("\\s+", "");

					//Set the url if a custom url has been specified.
					if (splitstr[0].contains("Database")) {
						DB_NAME = splitstr[1];
					}

					//Set the user name if a custom name has been specified.
					else if (splitstr[0].contains("User")) {
						USER = splitstr[1];
					}

					//Set the user name if a custom name has been specified.
					else if (splitstr[0].contains("Password")) {
						PASS = splitstr[1];
					}
				}

			}//End of config read loop.
		} //End of config initialization.
		catch (IOException e) {
			e.printStackTrace();
		}

		createDatabase();
		createUsersTable();
		createBanksTable();
		createBankAccountsTable();
		createCouponsTable();
		createOrderNumbersTable();
		createRetailInventoryTable();
		createSoldItemsTable();
		createUserCartTable();
		createCouponItemsTable();
		createAddressesTable();
		createCitiesTable();
		createStatesTable();
		createCountriesTable();
		createUserAddressesTable();
		addDummyData();

		try {
			createStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Connect to MySql localhost and create a new com.database if one doesn't exist.
	//Uses the Database Configuration text file in the main executable directory.
	private static void createDatabase() {

		try {
			//Class.forName("com.mysql.jdbc.Driver");                         //Register for JDBC driver
			connection = DriverManager.getConnection(DB_URL, USER, PASS);     //Connect to MySql server at localhost

			createStatement = connection.createStatement();
			String query = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
			createStatement.executeUpdate(query);
		} catch (Exception se) {
			//Handle errors for JDBC
			se.printStackTrace();
		}//Handle errors for Class.forName
		finally {
			try {
				if (createStatement != null)
					createStatement.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
		}//end try
	}

	//Create the Users table if it doesn't exist
	private static void createUsersTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Users`"
				+ "("
				+ "userId             INT NOT NULL AUTO_INCREMENT,"
				+ "firstName          varchar(10) NULL,"
				+ "lastName           varchar(25) NULL,"
				+ "accountName        varchar(25) NOT NULL,"
				+ "password           varchar(25) NOT NULL,"
				+ "PRIMARY KEY        (`userId`),"
				+ "UNIQUE INDEX       `accountName_UNIQUE`            (`accountName` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Banks table if it doesn't exist
	private static void createBanksTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Banks`"
				+ "("
				+ "bankId             INT NOT NULL AUTO_INCREMENT,"
				+ "name               varchar(50) NOT NULL,"
				+ "PRIMARY KEY        (`bankId`),"
				+ "UNIQUE INDEX       `name_UNIQUE`            (`name` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Bank Accounts table if it doesn't exist
	private static void createBankAccountsTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`BankAccounts`"
				+ "("
				+ "bankAccountId      INT NOT NULL AUTO_INCREMENT,"
				+ "accountNumber      INT(12) NOT NULL,"
				+ "Users_userId       INT NOT NULL,"
				+ "Banks_bankId       INT NOT NULL,"
				+ "PRIMARY KEY        (`bankAccountId`, `Users_userID`, `Banks_bankId`),"
				+ "INDEX              `fk_BankAccounts_Banks_idx`            (`Banks_bankId` ASC) VISIBLE,"
				+ "INDEX              `fk_BankAccounts_Users_idx`            (`Users_userId` ASC) VISIBLE,"

				+ "CONSTRAINT         `fk_BankAccounts_Users`"
				+ "FOREIGN KEY (`Users_userId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION ,"

				+ "CONSTRAINT         `fk_BankAccounts_Banks`"
				+ "FOREIGN KEY (`Banks_bankId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Banks` (`bankId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION "
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Coupons table if it doesn't exist
	private static void createCouponsTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Coupons`"
				+ "("
				+ "couponId           INT NOT NULL AUTO_INCREMENT,"
				+ "code               varchar(25) NOT NULL,"
				+ "description        varchar(50),"
				+ "status             varchar(12) NOT NULL,"
				+ "discount           float NOT NULL,"
				+ "PRIMARY KEY        (`couponId`),"
				+ "UNIQUE INDEX       `code_UNIQUE`                           (`code` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Order Numbers table if it doesn't exist
	private static void createOrderNumbersTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`OrderNumbers`"
				+ "("
				+ "orderId            INT NOT NULL AUTO_INCREMENT,"
				+ "orderDate          DATETIME NOT NULL,"
				+ "Users_userId       INT NOT NULL,"
				+ "Coupons_couponId   INT,"
				+ "BankAccounts_bankAccountId     INT NOT NULL,"
				+ "PRIMARY KEY        (`orderId`, `Users_userId`, `BankAccounts_bankAccountId`),"
				+ "INDEX              `fk_OrderNumbers_Users_idx`            (`Users_userId` ASC) VISIBLE,"
				+ "INDEX              `fk_OrderNumbers_Coupons_idx`          (`Coupons_couponId` ASC) VISIBLE,"
				+ "INDEX              `fk_OrderNumbers_BankAccounts_idx`     (`BankAccounts_bankAccountId` ASC) VISIBLE,"

				+ "CONSTRAINT         `fk_OrderNumbers_Users`"
				+ "FOREIGN KEY (`Users_userId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION ,"

				+ "CONSTRAINT         `fk_OrderNumbers_Coupons`"
				+ "FOREIGN KEY (`Coupons_couponId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Coupons` (`couponId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION ,"

				+ "CONSTRAINT         `fk_OrderNumbers_BankAccounts`"
				+ "FOREIGN KEY (`BankAccounts_bankAccountId`)"
				+ "REFERENCES `" + DB_NAME + "`.`BankAccounts` (`bankAccountId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION "
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Retail Inventory table if it doesn't exist
	private static void createRetailInventoryTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`RetailInventory`"
				+ "("
				+ "itemId             INT NOT NULL AUTO_INCREMENT,"
				+ "name               varchar(50) NOT NULL,"
				+ "price              DECIMAL(8,2) NOT NULL,"
				+ "description        varchar(2000) NULL,"
				+ "quantity           int NOT NULL DEFAULT 0,"
				+ "PRIMARY KEY        (`itemId`)"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Sold Items table if it doesn't exist
	private static void createSoldItemsTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`SoldItems`"
				+ "("
				+ "sellId                 INT NOT NULL AUTO_INCREMENT,"
				+ "quantity               INT NULL DEFAULT 1,"
				+ "status                 varchar(12) NOT NULL DEFAULT 'In Transit',"
				+ "expectedDelivery       DATETIME NOT NULL,"
				+ "OrderNumbers_orderId   int NOT NULL,"
				+ "RetailInventory_itemId int NOT NULL,"
				+ "PRIMARY KEY            (`sellId`, `OrderNumbers_orderId`, `RetailInventory_itemId`),"
				+ "INDEX                  `fk_SoldItems_OrderNumbers_idx`     (`OrderNumbers_orderId` ASC) VISIBLE,"
				+ "INDEX                  `fk_SoldItems_RetailInventory_idx`  (`RetailInventory_itemId` ASC) VISIBLE,"

				+ "CONSTRAINT `fk_SoldItems_OrderNumbers`"
				+ "FOREIGN KEY (`OrderNumbers_orderId`)"
				+ "REFERENCES `" + DB_NAME + "`.`OrderNumbers` (`orderId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_SoldItems_RetailInventory`"
				+ "FOREIGN KEY (`RetailInventory_itemId`)"
				+ "REFERENCES `" + DB_NAME + "`.`RetailInventory` (`itemId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the User Cart table if it doesn't exist
	private static void createUserCartTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`UserCart`"
				+ "("
				+ "cartItemId                 INT NOT NULL AUTO_INCREMENT,"
				+ "Users_userId               INT NOT NULL,"
				+ "RetailInventory_itemId     INT NOT NULL,"
				+ "quantity                   INT NOT NULL DEFAULT 1,"
				+ "PRIMARY KEY                (`cartItemId`, `Users_userId`, `RetailInventory_itemId`),"
				+ "INDEX                      `fk_UserCart_Users_userId_idx` (`Users_userId` ASC) VISIBLE,"
				+ "INDEX                      `fk_UserCart_RetailInventory_itemId_idx` (`RetailInventory_itemId` ASC) VISIBLE,"

				+ "CONSTRAINT `fk_UserCart_Users_userId`"
				+ "FOREIGN KEY (`Users_userId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_UserCart_RetailInventory_itemId`"
				+ "FOREIGN KEY (`RetailInventory_itemId`)"
				+ "REFERENCES `" + DB_NAME + "`.`RetailInventory` (`itemId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Coupon Items table if it doesn't exist
	private static void createCouponItemsTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`CouponItems`"
				+ "("
				+ "couponItemId               INT NOT NULL AUTO_INCREMENT,"
				+ "Coupons_couponId           INT NOT NULL,"
				+ "RetailInventory_itemId     INT NOT NULL,"
				+ "PRIMARY KEY                (`couponItemId`, `Coupons_couponId`, `RetailInventory_itemId`),"
				+ "INDEX                      `fk_CouponItems_Coupons_idx` (`Coupons_couponId` ASC) VISIBLE,"
				+ "INDEX                      `fk_CouponItems_RetailInventory_idx` (`RetailInventory_itemId` ASC) VISIBLE,"

				+ "CONSTRAINT `fk_CouponItems_Coupons`"
				+ "FOREIGN KEY (`Coupons_couponId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Coupons` (`couponId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_CouponItems_RetailInventory`"
				+ "FOREIGN KEY (`RetailInventory_itemId`)"
				+ "REFERENCES `" + DB_NAME + "`.`RetailInventory` (`itemId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	private static void addDummyData() {
		Inventory databaseItems = getInventoryPage(1, 100);
		Item chair = new Item(1, "Chair", 10, 12.99, "One of the basic pieces of furniture, a chair is a type of seat.");
		Item ps5 = new Item(2, "Playstation 5", 0, 399.99, "Explore uncharted virtual territories and slay dragons with this sleek Sony PlayStation 5 gaming console.");
		Item keyboard = new Item(3, "Keyboard", 5, 22.99, "Leverage speed and precision to dominate your opponents with this Logitech G PRO mechanical gaming keyboard.");
		Item table = new Item(4, "Table", 8, 34.99, "A table is an item of furniture with a flat top and one or more legs, used as a surface for working at, eating from or on which to place things.");
		Item tv = new Item(5, "Television", 2, 119.99, "A reliable CRT TV.");
		Item piano = new Item(6, "Piano", 1, 1229.99, "A grand piano is a large piano whose strings are set horizontally to the ground. Grand pianos are used especially for giving concerts and making recordings.");
		Item xboxs = new Item(7, "Xbox Series S", 3, 299.99, "Play with friends and family near and far???sitting together on the sofa or around the world on Xbox Live, the fastest, most reliable gaming network.");
		Item xboxx = new Item(8, "Xbox Series X", 1, 499.99, "Introducing Xbox Series X. Play thousands of titles from four generations of consoles???all games look and play best on Xbox Series X.");
		Item rtx3080 = new Item(9, "NVIDIA GeForce RTX 3080", 1, 1199.99, "The GeForce RTX 3080 delivers the ultra performance that gamers crave, powered by Ampere???NVIDIA???s 2nd gen RTX architecture. It???s built with enhanced RT Cores and Tensor Cores, new streaming multiprocessors, and superfast G6X memory for an amazing gaming experience.");
		Item mouse = new Item(10, "Mouse", 12, 59.99, "Gain a competitive edge while gaming with this Razer Basilisk X hyperspeed wireless mouse.");
		Item headset = new Item(11, "Logitech Headset", 4, 89.99, "Communicate with your team or taunt your foes with this Logitech Pro X gaming headset.");


		if (!databaseItems.containsItem(chair)) {
			addInventoryItem(chair);
		}
		if (!databaseItems.containsItem(ps5)) {
			addInventoryItem(ps5);
		}
		if (!databaseItems.containsItem(keyboard)) {
			addInventoryItem(keyboard);
		}
		if (!databaseItems.containsItem(table)) {
			addInventoryItem(table);
		}
		if (!databaseItems.containsItem(tv)) {
			addInventoryItem(tv);
		}
		if (!databaseItems.containsItem(piano)) {
			addInventoryItem(piano);
		}
		if (!databaseItems.containsItem(xboxs)) {
			addInventoryItem(xboxs);
		}
		if (!databaseItems.containsItem(xboxx)) {
			addInventoryItem(xboxx);
		}
		if (!databaseItems.containsItem(rtx3080)) {
			addInventoryItem(rtx3080);
		}
		if (!databaseItems.containsItem(mouse)) {
			addInventoryItem(mouse);
		}
		if (!databaseItems.containsItem(headset)) {
			addInventoryItem(headset);
		}

		addCoupon(new Coupon(0, "SAVE20", "Save 20%", "Active", 0.2f));
	}

	private static void createAddressesTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Addresses`"
				+ "("
				+ "addressId					INT NOT NULL AUTO_INCREMENT,"
				+ "address           			varchar(100) NOT NULL,"
				+ "PRIMARY KEY                	(`addressId`),"
				+ "UNIQUE INDEX       			`address_UNIQUE`            (`address` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	private static void createCitiesTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Cities`"
				+ "("
				+ "cityId				INT NOT NULL AUTO_INCREMENT,"
				+ "city           		varchar(100) NOT NULL,"
				+ "PRIMARY KEY          (`cityId`),"
				+ "UNIQUE INDEX       	`city_UNIQUE`            (`city` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	private static void createStatesTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`States`"
				+ "("
				+ "stateId				INT NOT NULL AUTO_INCREMENT,"
				+ "state           		varchar(100) NOT NULL,"
				+ "PRIMARY KEY          (`stateId`),"
				+ "UNIQUE INDEX       	`state_UNIQUE`            (`state` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	private static void createCountriesTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Countries`"
				+ "("
				+ "countryId				INT NOT NULL AUTO_INCREMENT,"
				+ "country           		varchar(100) NOT NULL,"
				+ "PRIMARY KEY          	(`countryId`),"
				+ "UNIQUE INDEX       		`country_UNIQUE`            (`country` ASC) VISIBLE"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Create the Coupon Items table if it doesn't exist
	private static void createUserAddressesTable() {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`UserAddresses`"
				+ "("
				+ "userAddressId              INT NOT NULL AUTO_INCREMENT,"
				+ "Addresses_addressId        INT NOT NULL,"
				+ "Cities_cityId	          INT NOT NULL,"
				+ "States_stateId        	  INT NOT NULL,"
				+ "Countries_countryId        INT NOT NULL,"
				+ "Users_userId			      INT NOT NULL,"
				+ "PRIMARY KEY                (`userAddressId`, `Addresses_addressId`, `Cities_cityId`, `States_stateId`, `Countries_countryId`, `Users_userId`),"
				+ "INDEX                      `fk_UserAddress_Addresses_addressId_idx` (`Addresses_addressId` ASC) VISIBLE,"
				+ "INDEX                      `fk_UserAddress_Cities_cityId_idx` (`Cities_cityId` ASC) VISIBLE,"
				+ "INDEX                      `fk_UserAddress_States_stateId_idx` (`States_stateId` ASC) VISIBLE,"
				+ "INDEX                      `fk_UserAddress_Countries_countryId_idx` (`Countries_countryId` ASC) VISIBLE,"
				+ "INDEX                      `fk_UserAddress_Users_userId_idx` (`Users_userId` ASC) VISIBLE,"

				+ "CONSTRAINT `fk_UserAddress_Addresses_addressId_idx`"
				+ "FOREIGN KEY (`Addresses_addressId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Addresses` (`addressId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_UserAddress_Cities_cityId_idx`"
				+ "FOREIGN KEY (`Cities_cityId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Cities` (`cityId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_UserAddress_States_stateId_idx`"
				+ "FOREIGN KEY (`States_stateId`)"
				+ "REFERENCES `" + DB_NAME + "`.`States` (`stateId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_UserAddress_Countries_countryId_idx`"
				+ "FOREIGN KEY (`Countries_countryId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Countries` (`countryId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, "

				+ "CONSTRAINT `fk_UserAddresses_userId`"
				+ "FOREIGN KEY (`Users_userId`)"
				+ "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
				+ "ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION"
				+ ")";

		try {
			createStatement = connection.createStatement();
			createStatement.execute(sqlCreate);
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	//Inserts a new user entry into the Users table based on the data obtained from the Sign Up view.
	public static void addUser(User user) {

		if (user.getAccountName().isEmpty() || user.getPassword().isEmpty()) {
			System.out.println("Account name and/or password cannot be empty.");
			return;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Users` "
				+ "(firstName, lastName, accountName, password) "
				+ "VALUES (?,?,?,?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, user.getFirstName());
			statement.setString(2, user.getLastName());
			statement.setString(3, user.getAccountName());
			statement.setString(4, user.getPassword());

			statement.executeUpdate();
			System.out.println("Added user " + user.getAccountName() + " to database!");

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Query a select statement based on the account name and password of the user.  Used for signing in.
	public static User signIn(String accountName, String password) {

		if (accountName.isEmpty() || password.isEmpty()) {
			System.out.println("Account name and/or password cannot be empty.");
			return null;
		}

		User user = null;

		String sqlStatement = "SELECT * FROM `" + DB_NAME + "`.Users WHERE accountName=? AND password=?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, accountName);
			statement.setString(2, password);

			ResultSet resultSet = statement.executeQuery();

			//No user found, so return null user.  User typed invalid credentials.
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt("userId"));
				user.setFirstName(resultSet.getString("firstName"));
				user.setLastName(resultSet.getString("lastName"));
				user.setAccountName(resultSet.getString("accountName"));
				user.setPassword(resultSet.getString("password"));
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return user;
	}

	public static void updateUser(User user) {

		if (user.getId() < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "UPDATE `" + DB_NAME + "`.`Users` SET firstName = ? AND lastName = ? AND accountName = ? AND password = ? WHERE userId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, user.getFirstName());
			statement.setString(2, user.getLastName());
			statement.setString(3, user.getAccountName());
			statement.setString(4, user.getPassword());
			statement.setInt(5, user.getId());
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Adds a new item to the RetailInventory table with the information from the given Item object.
	public static void addInventoryItem(Item item) {
		if (item == null || item.getName().isEmpty()) {
			System.out.println("Item cannot be null.");
			return;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`RetailInventory` "
				+ "(name, price, description, quantity) "
				+ "VALUES (?,?,?,?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, item.getName());
			statement.setDouble(2, item.getPrice());
			statement.setString(3, item.getDescription());
			statement.setInt(4, item.getQuantity());
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Updates the quantity value for the selected item with the given id and how much to add/subtract by.  To reduce stock, give a negative number.
	public static void updateItemStock(int id, int quantity) {

		if (id < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "UPDATE `" + DB_NAME + "`.`RetailInventory` SET quantity = quantity + ? WHERE itemId = ? AND quantity + ? >= 0";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, quantity);
			statement.setInt(2, id);
			statement.setInt(3, quantity);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}

	//Gets back an Inventory object of a single page of data from the RetailInventory page.  Amount of items depends on page size given.
	public static Inventory getInventoryPage(int page, int pageSize) {

		Inventory itemlist = new Inventory();
		String sqlStatement = "SELECT * FROM `" + DB_NAME + "`.RetailInventory LIMIT " + (page - 1) * pageSize + ", " + pageSize;

		try {
			statement = connection.prepareStatement(sqlStatement);
			ResultSet results = statement.executeQuery();

			while (results.next()) {

				int id = results.getInt("itemId");
				String name = results.getString("name");
				double price = results.getDouble("price");
				String description = results.getString("description");
				int quantity = results.getInt("quantity");

				Item item = new Item(id, name, quantity, price, description);

				itemlist.addItem(item);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return itemlist;
	}

	//Deletes an item row from the RetailInventory table based on the given id.
	public static void deleteInventoryItem(int id) {

		if (id < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`RetailInventory` WHERE id = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Adds a new bank vendor to the Banks table.  Not used by the consumer, only the retail manager to keep a list of approved vendors.
	public static int addBank(String name) {
		if (name.isEmpty()) {
			System.out.println("Bank name cannot be empty.");
			return 0;
		}

		int bankId = 0;

		String sqlStatement = "INSERT IGNORE INTO `" + DB_NAME + "`.`Banks` "
				+ "(name) "
				+ "VALUES (?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, name);

			bankId = statement.executeUpdate();

		} catch (SQLException throwables) {

			sqlStatement = "SELECT * FROM `" + DB_NAME + "`.`Banks` WHERE name=?";

			try {
				statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, name);

				ResultSet resultSet = statement.executeQuery();

				if (!resultSet.next())
					return -1;

				else return resultSet.getInt("bankId");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return bankId;
	}

	//Deletes a Bank row from the Bank table based on the given id.
	public static void deleteBank(int id) {

		if (id < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`Bank` WHERE id = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void updateBank(Bank bank) {

		if (bank.getId() < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "UPDATE `" + DB_NAME + "`.`Banks` SET name = ?  WHERE bankId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, bank.getName());
			statement.setInt(2, bank.getId());
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static ArrayList<Bank> getBanks() {

		ArrayList<Bank> banks = new ArrayList<>();

		String sqlStatement = "SELECT * FROM " + DB_NAME + "`.`Banks` ";

		try {

			statement = connection.prepareStatement(sqlStatement);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				int id;
				String name;

				id = resultSet.getInt("bankId");
				name = resultSet.getString("name");

				Bank bank = new Bank(id, name);
				banks.add(bank);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return banks;
	}

	public static ArrayList<Bank> getBanksByName(String searchText) {

		ArrayList<Bank> banks = new ArrayList<>();

		if (searchText.isEmpty()) {
			System.out.println("Search parameter is empty.");
			return banks;
		}

		String sqlStatement = "SELECT * FROM " + DB_NAME + "`.`Banks` WHERE name = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, searchText);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				int id;
				String name;

				id = resultSet.getInt("bankId");
				name = resultSet.getString("name");

				Bank bank = new Bank(id, name);
				banks.add(bank);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return banks;
	}

	//Adds a bank account to the BankAccounts table and links it to the given user id and bank.
	public static int addBankAccount(int userId, String bankName, int accountNumber) {

		if (bankName.isEmpty()) {
			System.out.println("Bank name cannot be empty.");
			return -1;
		}

		if (accountNumber < 1) {
			System.out.println("Bank account number invalid.");
			return -1;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`BankAccounts` "
				+ "(accountNumber, Users_userId, Banks_bankId) "
				+ "VALUES (?, ?, (SELECT bankId FROM `" + DB_NAME + "`.`Banks` " + " WHERE name = ?))";

		try {

			statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, accountNumber);
			statement.setInt(2, userId);
			statement.setString(3, bankName);

			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		}

		//Either foreign key references doesn't exist, or another sql error occurred.
		//We don't want users to be able to add any random bank that isn't valid into the system, so do nothing and ignore their request.
		catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return -1;
	}

	public static void deleteBankAccount(int id) {

		if (id < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`BankAccounts` WHERE id = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void updateBankAccount(int userId, BankAccount account) {

		if (account.getBankAccountId() < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		if (account.getBank() == null || account.getBank().getId() < 1) {
			System.out.println("Associated bank is invalid.");
			return;
		}

		String sqlStatement = "UPDATE `" + DB_NAME + "`.`BankAccounts` SET accountNumber = ? AND Users_userId = ? AND Banks_bankId = ?  WHERE bankAccountId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, account.getAccountNumber());
			statement.setInt(2, userId);
			statement.setInt(3, account.getBank().getId());
			statement.setInt(4, account.getBankAccountId());
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Gets a list of all the items that belong in the users cart.
	public static ArrayList<BankAccount> getBankAccounts(int userId) {

		ArrayList<BankAccount> bankAccounts = new ArrayList<>();

		if (userId < 1) {
			System.out.println("User id cannot be less than 0");
			return bankAccounts;
		}

		String sqlStatement = "SELECT "
				+ "bankAccountId,"
				+ "accountNumber,"
				+ "Banks.bankId,"
				+ "Banks.name "
				+ "FROM `" + DB_NAME + "`.`Banks` "
				+ "JOIN `" + DB_NAME + "`.`BankAccounts` "
				+ "ON BankAccounts.Users_userId = ? AND BankAccounts.Banks_bankId = Banks.bankId";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, userId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				int bankAccountId;
				int accountNumber;
				int bankId;
				String bankName;

				bankAccountId = resultSet.getInt("bankAccountId");
				accountNumber = resultSet.getInt("accountNumber");
				bankId = resultSet.getInt("bankId");
				bankName = resultSet.getString("name");

				Bank newBank = new Bank(bankId, bankName);
				BankAccount acct = new BankAccount(bankAccountId, accountNumber, newBank);

				bankAccounts.add(acct);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return bankAccounts;
	}

	//Coupon management is only done through the backend, users do not have access to these functions.
	public static void addCoupon(Coupon coupon) {

		if (coupon == null || coupon.getCode().isEmpty()) {
			System.out.println("Coupon cannot be empty.");
			return;
		}

		String sqlStatement = "INSERT IGNORE INTO `" + DB_NAME + "`.`Coupons` "
				+ "(code, description, status, discount) "
				+ "VALUES (?,?,?,?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, coupon.getCode());
			statement.setString(2, coupon.getDescription());
			statement.setString(3, coupon.getStatus());
			statement.setFloat(4, coupon.getDiscount());

			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static Coupon checkCoupon(String code) {

		if (code == null || code.isEmpty()) {
			System.out.println("Coupon cannot be empty.");
			return null;
		}

		String sqlStatement = "SELECT * FROM `" + DB_NAME + "`.`Coupons` WHERE code=?";

		try {
			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, code);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {

				int id;
				String description;
				String status;
				float discount;

				id = resultSet.getInt("couponId");
				description = resultSet.getString("description");
				status = resultSet.getString("status");
				discount = resultSet.getFloat("discount");

				return new Coupon(id, code, description, status, discount);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return null;
	}

	public static void updateCoupon(Coupon coupon) {

		if (coupon == null || coupon.getCouponID() < 1) {
			System.out.println("Coupon cannot be empty.");
			return;
		}

		String sqlStatement = "UPDATE `" + DB_NAME + "`.`Coupons` SET code = ? AND description = ? AND status = ? AND discount = ?  WHERE couponId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, coupon.getCode());
			statement.setString(2, coupon.getDescription());
			statement.setString(3, coupon.getStatus());
			statement.setFloat(4, coupon.getDiscount());
			statement.setInt(5, coupon.getCouponID());
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void deleteCoupon(int id) {

		if (id < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`Coupon` WHERE id = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Adds a new order number using the users id, their bank account id, and an optional coupon id.
	//Pass in coupon = null if you want to leave this empty.
	public static int addOrderNumber(int userId, BankAccount bankAcc, Coupon coupon) {

		if (userId < 1 || bankAcc == null) {
			System.out.println("User/Bank id cannot be less than 1");
			return -1;
		}

		int bankAcctId = -1;
		ArrayList<BankAccount> bankAccounts = getBankAccounts(userId);

		for (BankAccount acct : bankAccounts) {
			if (acct.equals(bankAcc)) {
				bankAcctId = acct.getBankAccountId();
				break;
			}
		}

		if (bankAcctId == -1) {
			DBDriver.addBank(bankAcc.getBank().getName());
			bankAcctId = DBDriver.addBankAccount(Main.getUser().getId(), bankAcc.getBank().getName(), bankAcc.getAccountNumber());
			if (bankAcctId == -1) {
				System.out.println("Error adding new Bank Account");
				return -1;
			}
		}

		String sqlStatement;

		if (coupon == null) {
			sqlStatement = "INSERT INTO `" + DB_NAME + "`.`OrderNumbers` "
					+ "(orderDate, Users_userId, BankAccounts_bankAccountId) "
					+ "VALUES (?,?,?)";
		} else {
			sqlStatement = "INSERT INTO `" + DB_NAME + "`.`OrderNumbers` "
					+ "(orderDate, Users_userId, Coupons_couponId, BankAccounts_bankAccountId) "
					+ "VALUES (?,?,?,?)";
		}

		try {

			statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
			statement.setInt(2, userId);

			if (coupon != null) {
				statement.setInt(3, coupon.getCouponID());
				statement.setInt(4, bankAcctId);
			} else {
				statement.setInt(3, bankAcctId);
			}

			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return -1;
	}

	//Gets how much of an item is stocked in the retail inventory.
	public static int getItemStock(int id) {

		int amt = 0;

		String sqlStatement = "SELECT quantity FROM `" + DB_NAME + "`.`RetailInventory` WHERE itemId = ? ";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next())
				amt = resultSet.getInt(1);

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return amt;
	}

	public static void addSoldItem(int orderId, int itemId, int quantity, Date expectedDelivery) {

		if (orderId < 1 || itemId < 1 || quantity < 1) {
			System.out.println("Order id, item id, and quantity can't be less than 1.");
			return;
		}

		int stock = getItemStock(itemId);

		if (stock >= quantity) {
			String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`SoldItems` "
					+ "(quantity, status, expectedDelivery, OrderNumbers_orderId, RetailInventory_itemId) "
					+ "VALUES (?,?,?,?,?)";

			try {

				statement = connection.prepareStatement(sqlStatement);
				statement.setInt(1, quantity);
				statement.setString(2, "In Transit");
				statement.setDate(3, expectedDelivery);
				statement.setInt(4, orderId);
				statement.setInt(5, itemId);

				statement.executeUpdate();

			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}


	}

	//Sets the status of the given sold item to what is given.
	public static void setOrderStatus(int itemId, String status) {

		if (itemId < 1) {
			System.out.println("Id cannot be less than 1.");
			return;
		}

		String sqlStatement = "UPDATE `" + DB_NAME + "`.`SoldItems` SET status = ? WHERE sellId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, status);
			statement.setInt(2, itemId);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void addCartItem(int userId, int itemId, int quantity) {

		if (userId < 1 || itemId < 1 || quantity < 1) {
			System.out.println("User/Item/Quantity id cannot be less than 1");
			return;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`UserCart` "
				+ "(Users_userId, RetailInventory_itemId, quantity) "
				+ "VALUES (?,?,?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, userId);
			statement.setInt(2, itemId);
			statement.setInt(3, quantity);

			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Deletes a single item from the users cart using the id of the cart item.
	public static void deleteCartItem_Single(int userId, int cartItemId) {

		if (cartItemId < 1 || userId < 1) {
			System.out.println("Given ids are less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`UserCart` WHERE RetailInventory_itemId = ? AND Users_userId = ?";

		try {
			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, cartItemId);
			statement.setInt(2, userId);
			statement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Clears the users cart.  Only called when the user confirms a purchase.
	public static void deleteCartItem_All(int userId) {

		if (userId < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`UserCart` WHERE Users_userId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, userId);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//Adds a row to the Addresses table.
	private static int addAddressEntry(String address) {

		if (address.isEmpty()) {
			System.out.println("address cannot be empty.");
			return 0;
		}

		int addressKey = -1;
		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Addresses` (address) VALUES (?)";

		try {
			statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, address.toLowerCase());
			addressKey = statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next())
					return (int) generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in com.database.
		catch (SQLException throwables) {

			sqlStatement = "SELECT * FROM `" + DB_NAME + "`.`Addresses` WHERE address=?";

			try {
				statement = connection.prepareStatement(sqlStatement);
				statement.setString(1, address);

				ResultSet resultSet = statement.executeQuery();

				if (!resultSet.next())
					return -1;

				else return resultSet.getInt("addressId");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return addressKey;
	}

	//Adds a row to the City table.
	private static int addCity(String city) {

		if (city.isEmpty()) {
			System.out.println("city cannot be empty.");
			return 0;
		}

		int cityKey = -1;
		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Cities` (city) VALUES (?)";

		try {
			statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, city.toLowerCase());
			cityKey = statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next())
					return (int) generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in com.database.
		catch (SQLException throwables) {

			sqlStatement = "SELECT * FROM `" + DB_NAME + "`.`Cities` WHERE city=?";

			try {
				statement = connection.prepareStatement(sqlStatement);
				statement.setString(1, city);

				ResultSet resultSet = statement.executeQuery();

				if (!resultSet.next())
					return -1;

				else return resultSet.getInt("cityId");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cityKey;
	}

	//Adds a row to the States table.
	private static int addState(String state) {

		if (state.isEmpty()) {
			System.out.println("state cannot be empty.");
			return 0;
		}

		int stateKey = -1;
		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`States` (state) VALUES (?)";

		try {
			statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, state.toLowerCase());
			stateKey = statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next())
					return (int) generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in com.database.
		catch (SQLException throwables) {

			sqlStatement = "SELECT * FROM `" + DB_NAME + "`.`States` WHERE state=?";

			try {
				statement = connection.prepareStatement(sqlStatement);
				statement.setString(1, state);

				ResultSet resultSet = statement.executeQuery();

				if (!resultSet.next())
					return -1;

				else return resultSet.getInt("stateId");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return stateKey;
	}

	//Adds a row to the Country table.
	private static int addCountry(String country) {

		if (country.isEmpty()) {
			System.out.println("country cannot be empty.");
			return 0;
		}

		int countryKey = -1;
		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Countries` (country) VALUES (?)";

		try {
			statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, country.toLowerCase());
			countryKey = statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next())
					return (int) generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in com.database.
		catch (SQLException throwables) {

			sqlStatement = "SELECT * FROM `" + DB_NAME + "`.`Countries` WHERE country=?";

			try {
				statement = connection.prepareStatement(sqlStatement);
				statement.setString(1, country);

				ResultSet resultSet = statement.executeQuery();

				if (!resultSet.next())
					return -1;

				else return resultSet.getInt("countryId");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return countryKey;
	}

	public static void addAddressFull(int userId, Address address) {

		if (address == null || address.getAddress().isEmpty()) {
			System.out.println("address cannot be empty.");
			return;
		}

		if (userId < 1) {
			System.out.println("User id can't be less than 1.");
			return;
		}

		//Variables used to keep track of the auto generated keys for inserts (if they are necessary).
		int addressKey, cityKey, stateKey, countryKey;

		addressKey = addAddressEntry(address.getAddress());
		cityKey = addCity(address.getCity());
		stateKey = addState(address.getState());
		countryKey = addCountry(address.getCountry());


		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`UserAddresses` "
				+ "(Addresses_addressId, Cities_cityId, States_stateId, Countries_countryId, Users_userId) "
				+ "VALUES (?,?,?,?,?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, addressKey);
			statement.setInt(2, cityKey);
			statement.setInt(3, stateKey);
			statement.setInt(4, countryKey);
			statement.setInt(5, userId);

			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}


	}

	//Gets a list of all the items that belong in the users cart.
	public static Cart getCart(int userId) {

		Cart.getCart().clear();
		if (userId < 1) {
			System.out.println("User id cannot be less than 0");
			Cart.getCart().clear();
			return Cart.getCart();
		}

		String sqlStatement = "SELECT "
				+ "name,"
				+ "price,"
				+ "description,"
				+ "RetailInventory.itemId,"
				+ "UserCart.quantity "
				+ "FROM `" + DB_NAME + "`.`RetailInventory` "
				+ "JOIN `" + DB_NAME + "`.`UserCart` "
				+ "ON UserCart.RetailInventory_itemId = RetailInventory.itemId AND UserCart.Users_userId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, userId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				Item item;
				String name;
				double price;
				String description;
				int quantity;
				int cartId;

				name = resultSet.getString("name");
				price = resultSet.getDouble("price");
				description = resultSet.getString("description");
				quantity = resultSet.getInt("quantity");
				cartId = resultSet.getInt("itemId");

				item = new Item(cartId, name, quantity, price, description);
				Cart.getCart().add(item);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return Cart.getCart();
	}

	//Gets a list of all the items that belong in the users cart.
	public static ArrayList<Address> getAddresses(int userId) {

		ArrayList<Address> addresses = new ArrayList<>();

		if (userId < 1) {
			System.out.println("User id cannot be less than 0");
			Cart.getCart().clear();
			return addresses;
		}

		String sqlStatement = "SELECT "
				+ "Addresses.address,"
				+ "Cities.city,"
				+ "States.state,"
				+ "Countries.country,"
				+ "UserAddresses.userAddressId "
				+ "FROM `" + DB_NAME + "`.`UserAddresses` "
				+ "JOIN `" + DB_NAME + "`.`Addresses` "
				+ "ON UserAddresses.Addresses_addressId = Addresses.addressId AND UserAddresses.Users_userId = " + userId + " "
				+ "JOIN `" + DB_NAME + "`.`Cities` "
				+ "ON UserAddresses.Cities_cityId = Cities.cityId AND UserAddresses.Users_userId = " + userId + " "
				+ "JOIN `" + DB_NAME + "`.`States` "
				+ "ON UserAddresses.States_stateId = States.stateId AND UserAddresses.Users_userId = " + userId + " "
				+ "JOIN `" + DB_NAME + "`.`Countries` "
				+ "ON UserAddresses.Countries_countryId = Countries.countryId AND UserAddresses.Users_userId = " + userId + " ";

		try {

			statement = connection.prepareStatement(sqlStatement);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				Address address;
				String addressName = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String country = resultSet.getString("country");
				int userAddressId = resultSet.getInt("userAddressId");

				address = new Address(userAddressId, addressName, city, state, country);
				addresses.add(address);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return addresses;
	}

	//Gets back an Inventory object of a single page of data from the RetailInventory page.  Amount of items depends on page size given.
	public static ArrayList<Order> getOrderHistory(int userId, int page, int pageSize) {

		ArrayList<Order> orders = new ArrayList<>();
		String sqlStatement = "SELECT "
				+ "sellId,"
				+ "SoldItems.quantity,"
				+ "SoldItems.status,"
				+ "SoldItems.expectedDelivery,"
				+ "OrderNumbers.orderId,"
				+ "OrderNumbers.orderDate,"
				+ "RetailInventory.name,"
				+ "RetailInventory.price,"
				+ "RetailInventory.description,"
				+ "Coupons.couponId,"
				+ "Coupons.code,"
				+ "Coupons.description,"
				+ "Coupons.status,"
				+ "Coupons.discount "
				+ "FROM `" + DB_NAME + "`.SoldItems "
				+ "INNER JOIN `" + DB_NAME + "`.`OrderNumbers` "
				+ "ON SoldItems.OrderNumbers_orderId = OrderNumbers.orderId AND OrderNumbers.Users_userId = " + userId + " "

				+ "LEFT JOIN `" + DB_NAME + "`.`RetailInventory` "
				+ "ON SoldItems.RetailInventory_itemId = RetailInventory.itemId "

				+ "LEFT JOIN `" + DB_NAME + "`.`Coupons` "
				+ "ON OrderNumbers.Coupons_couponId = Coupons.couponId "

				+ "LEFT JOIN `" + DB_NAME + "`.`CouponItems` "
				+ "ON Coupons.couponId = CouponItems.Coupons_couponId AND CouponItems.RetailInventory_itemId = RetailInventory.itemId "

				+ "ORDER BY OrderNumbers.orderDate DESC, RetailInventory.name ASC "

				+ "LIMIT " + (page - 1) * pageSize + ", " + pageSize;

		try {
			statement = connection.prepareStatement(sqlStatement);
			ResultSet results = statement.executeQuery();

			while (results.next()) {

				//SoldItem variables
				int soldItemId = results.getInt("sellId");
				int quantity = results.getInt("SoldItems.quantity");
				String status = results.getString("SoldItems.status");
				Date expectedDelivery = results.getDate("SoldItems.expectedDelivery");

				//OrderNumbers variables
				int orderId = results.getInt("OrderNumbers.orderId");
				Date orderDate = results.getDate("OrderNumbers.orderDate");

				//RetailInventory variables
				String itemName = results.getString("RetailInventory.name");
				double price = results.getDouble("RetailInventory.price");
				String description = results.getString("RetailInventory.description");

				//Coupon variables
				int couponId = results.getInt("Coupons.couponId");
				String couponCode = results.getString("Coupons.code");
				String couponDescription = results.getString("Coupons.description");
				String couponStatus = results.getString("Coupons.status");
				float couponDiscount = (float) results.getDouble("Coupons.discount");

				Coupon coupon = new Coupon(couponId, couponCode, couponDescription, couponStatus, couponDiscount);
				Item item = new Item(soldItemId, itemName, quantity, price, description);
				SoldItem soldItem = new SoldItem(item, expectedDelivery, status);

				int i = 0;
				while (i < orders.size()) {
					int indexId = orders.get(i).getOrderID();

					if (indexId == orderId) {
						orders.get(i).addItem(soldItem);
						break;
					}
					i++;
				}

				if (orders.size() < 1 || i == orders.size()) {
					List<SoldItem> items = new ArrayList<>();
					items.add(soldItem);
					orders.add(new Order(orderId, orderDate, coupon, items));
				}

			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return orders;
	}

}
