package com.database.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class DBDriver {

	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static String DB_NAME = "mydb";

	//  Database credentials.
	private static String USER = "username";
	private static String PASS = "password";

	private static Connection connection;
	private static PreparedStatement statement;
	private static Statement createStatement;      //Used for sending queries to MySql that create the main database and tables.

	private DBDriver() {

	}

	public static void initializeDatabase() {
		try {
			File fileObj = new File("Database Config.txt");
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}//End of config initialization.

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

		try {
			createStatement.close();
		} catch (SQLException e) {

		}
	}

	//Connect to MySql localhost and create a new database if one doesn't exist.
	//Uses the Database Configuration text file in the main executable directory.
	private static void createDatabase() {

		try {
			Class.forName("com.mysql.jdbc.Driver");                         //Register for JDBC driver
			connection = DriverManager.getConnection(DB_URL, USER, PASS);     //Connect to MySql server at localhost

			createStatement = connection.createStatement();
			String query = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
			createStatement.executeUpdate(query);
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (createStatement != null)
					createStatement.close();
			} catch (SQLException se2) {
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
				+ "Coupons_couponId   INT NOT NULL,"
				+ "BankAccounts_bankAccountId     INT NOT NULL,"
				+ "PRIMARY KEY        (`orderId`, `Users_userId`, `Coupons_couponId`, `BankAccounts_bankAccountId`),"
				+ "INDEX              `fk_OrderNumbers_Users_idx`            (`Users_userId` ASC) VISIBLE,"
				+ "INDEX              `fk_OrderNumbers_Coupons_idx`          (`Coupons_couponId` ASC) VISIBLE,"
				+ "INDEX              `fl_OrderNumbers_BankAccounts_idx`     (`BankAccounts_bankAccountId` ASC) VISIBLE,"

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

				+ "CONSTRAINT         `fl_OrderNumbers_BankAccounts`"
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
				+ "description        varchar(200) NULL,"
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
			if (!resultSet.next()) {
				user = null;
			} else {

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

	//Gets back an array list of a single page of data from the RetailInventory page.  Amount of items depends on page size given.
	public static ArrayList<Item> getInventoryPage(int page, int pageSize) {

		ArrayList<Item> itemlist = new ArrayList<Item>(pageSize);
		String sqlStatement = "SELECT * FROM `" + DB_NAME + "`.RetailInventory LIMIT " + String.valueOf((page - 1) * pageSize) + ", " + String.valueOf(pageSize);

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

				itemlist.add(item);
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
	public static void addBank(String name) {
		if (name.isEmpty()) {
			System.out.println("Bank name cannot be empty.");
			return;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Banks` "
				+ "(name) "
				+ "VALUES (?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, name);

			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
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

	//Adds a bank account to the BankAccounts table and links it to the given user id and bank.
	public static void addBankAccount(int userId, String bankName, int accountNumber) {

		if (bankName.isEmpty()) {
			System.out.println("Bank name cannot be empty.");
			return;
		}

		if (accountNumber < 1) {
			System.out.println("Bank account number invalid.");
			return;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`BankAccounts` "
				+ "(accountNumber, Users_userId, Banks_bankId) "
				+ "VALUES (?, ?, (SELECT bankId FROM `" + DB_NAME + "`.`Banks` " + " WHERE name = ?))";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, accountNumber);
			statement.setInt(2, userId);
			statement.setString(3, bankName);

			statement.executeUpdate();

		}

		//Either foreign key references doesn't exist, or another sql error occurred.
		//We don't want users to be able to add any random bank that isn't valid into the system, so do nothing and ignore their request.
		catch (SQLException throwables) {
			throwables.printStackTrace();
		}

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

	//Coupon management is only done through the backend, users do not have access to these functions.
	public static void addCoupon(Coupon coupon) {

		if (coupon == null || coupon.getCode().isEmpty()) {
			System.out.println("Coupon cannot be empty.");
			return;
		}

		String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Coupons` "
				+ "(code, description, status, discount) "
				+ "VALUES (?,?,?,?)";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setString(1, coupon.getCode());
			statement.setString(2, coupon.getDescription());
			statement.setString(3, coupon.getStatus());
			statement.setFloat(4, coupon.getValue());

			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
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
			statement.setFloat(4, coupon.getValue());
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
	//Pass in coupon id < 1 if you want to leave this empty.
	public static void addOrderNumber(int userId, int bankAcctId, int couponId) {

		if (userId < 1 || bankAcctId < 1) {
			System.out.println("User/Bank id cannot be less than 1");
			return;
		}

		String sqlStatement;

		if (couponId < 1) {
			sqlStatement = "INSERT INTO `" + DB_NAME + "`.`OrderNumbers` "
					+ "(orderDate, Users_userId, BankAccounts_bankAccountId) "
					+ "VALUES (?,?,?)";
		} else {
			sqlStatement = "INSERT INTO `" + DB_NAME + "`.`OrderNumbers` "
					+ "(orderDate, Users_userId, Coupons_couponId, BankAccounts_bankAccountId) "
					+ "VALUES (?,?,?,?)";
		}

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
			statement.setInt(2, userId);

			if (couponId > 0) {
				statement.setInt(3, couponId);
				statement.setInt(4, bankAcctId);
			} else {
				statement.setInt(3, bankAcctId);
			}

			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void addSoldItem(int orderId, int itemId, int quantity, Date expectedDelivery) {

		if (orderId < 1 || itemId < 1 || quantity < 1) {
			System.out.println("Order id, item id, and quantity can't be less than 1.");
			return;
		}

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
	public static void deleteCartItem_Single(int cartItemId, int userId) {

		if (cartItemId < 1 || userId < 1) {
			System.out.println("Given ids are less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`UserCart` WHERE cartItemId = ? AND Users_userId = ?";

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
	private static void deleteCartItem_All(int userId) {

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
	private static int addAddressEntry(String address){

		if (address.isEmpty()){
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
					return (int)generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in database.
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
	private static int addCity(String city){

		if (city.isEmpty()){
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
					return (int)generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in database.
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
	private static int addState(String state){

		if (state.isEmpty()){
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
					return (int)generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in database.
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
	private static int addCountry(String country){

		if (country.isEmpty()){
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
					return (int)generatedKeys.getLong(1);
			}
		}

		//Given parameter already exists in database.
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

	public static void addAddressFull(int userId, Address address){

		if(address == null || address.getAddress().isEmpty()){
			System.out.println("address cannot be empty.");
			return;
		}

		if (userId < 1){
			System.out.println("User id can't be less than 1.");
			return;
		}

		//Variables used to keep track of the auto generated keys for inserts (if they are necessary).
		int addressKey = -1, cityKey = -1, stateKey = -1, countryKey = -1;

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
	public static ArrayList<CartItem> getCart(int userId){

		ArrayList<CartItem> cart = new ArrayList<CartItem>();

		if (userId < 1){
			System.out.println("User id cannot be less than 0");
			return null;
		}

		String sqlStatement = "SELECT "
				+ 	"name,"
				+ 	"price,"
				+ 	"description,"
				+ 	"UserCart.cartItemId,"
				+ 	"UserCart.quantity "
				+	"FROM `" + DB_NAME + "`.`RetailInventory` "
				+	"JOIN `" + DB_NAME + "`.`UserCart` "
				+		"ON UserCart.RetailInventory_itemId = RetailInventory.itemId AND UserCart.Users_userId = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, userId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				CartItem item;
				String name;
				double price;
				String description;
				int quantity;
				int cartId;

				name = resultSet.getString("name");
				price = resultSet.getDouble("price");
				description = resultSet.getString("description");
				quantity = resultSet.getInt("quantity");
				cartId = resultSet.getInt("cartItemId");

				item = new CartItem(cartId, quantity, name, price, description);
				cart.add(item);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return cart;
	}


}
