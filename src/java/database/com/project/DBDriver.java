package database.com.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
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

	//Gets back a single page of data from the RetailInventory page.  Amount of items depends on page size given.
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

	public static void deleteCartItem(int id) {

		if (id < 1) {
			System.out.println("Given id is less than 1.  Id must be greater than 0.");
			return;
		}

		String sqlStatement = "DELETE FROM `" + DB_NAME + "`.`UserCart` WHERE id = ?";

		try {

			statement = connection.prepareStatement(sqlStatement);
			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
