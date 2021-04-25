package com.database.project;

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DbDriver {

    // JDBC driver name and database URL
    private String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://localhost:3306/";
    private String DB_NAME = "mydb";

    //  Database credentials.
    private String USER = "username";
    private String PASS = "password";

    private Connection connection;
    private PreparedStatement statement;
    private Statement createStatement;      //Used for sending queries to MySql that create/create the main database and tables.

    //Constructor for the database driver.
    public DbDriver(){

        //First load up text config file in root directory.
        try {
            File fileObj = new File("Database Config.txt");
            Scanner reader = new Scanner(fileObj);

            while (reader.hasNextLine()){
                String line = reader.nextLine();

                String[] splitstr = line.split(":");

                //remove whitespace from the input area.
                if(splitstr.length > 1){
                    splitstr[1] = splitstr[1].replaceAll("\\s+","");

                    //Set the url if a custom url has been specified.
                    if(splitstr[0].contains("Database")){
                        DB_NAME = splitstr[1];
                    }

                    //Set the user name if a custom name has been specified.
                    else if(splitstr[0].contains("User")){
                        USER = splitstr[1];
                    }

                    //Set the user name if a custom name has been specified.
                    else if(splitstr[0].contains("Password")){
                        PASS = splitstr[1];
                    }
                }

            }//End of config read loop.
        }
        catch (FileNotFoundException e) {
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

        try{
            createStatement.close();
        }
        catch (SQLException e){

        }
    }

    //Connect to MySql localhost and create a new database if one doesn't exist.
    //Uses the Database Configuration text file in the main executable directory.
    private void createDatabase(){

        try{
            Class.forName("com.mysql.jdbc.Driver");                         //Register for JDBC driver
            connection = DriverManager.getConnection(DB_URL,USER,PASS);     //Connect to MySql server at localhost

            createStatement = connection.createStatement();
            String query = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            createStatement.executeUpdate(query);
        }

        catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }

        catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        finally{
            try{
                if(createStatement != null)
                    createStatement.close();
            }

            catch(SQLException se2){}
        }//end try
    }

    //Create the Users table if it doesn't exist
    private void createUsersTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Users`"
                +   "("
                +   "userId             INT NOT NULL AUTO_INCREMENT,"
                +   "firstName          varchar(10) NULL,"
                +   "lastName           varchar(25) NULL,"
                +   "accountName        varchar(25) NOT NULL,"
                +   "password           varchar(25) NOT NULL,"
                +   "PRIMARY KEY        (`userId`),"
                +   "UNIQUE INDEX       `accountName_UNIQUE`            (`accountName` ASC) VISIBLE"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Banks table if it doesn't exist
    private void createBanksTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Banks`"
                +   "("
                +   "bankId             INT NOT NULL AUTO_INCREMENT,"
                +   "name               varchar(50) NOT NULL,"
                +   "PRIMARY KEY        (`bankId`),"
                +   "UNIQUE INDEX       `name_UNIQUE`            (`name` ASC) VISIBLE"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Bank Accounts table if it doesn't exist
    private void createBankAccountsTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`BankAccounts`"
                +   "("
                +   "bankAccountId      INT NOT NULL AUTO_INCREMENT,"
                +   "accountNumber      INT(12) NOT NULL,"
                +   "Users_userId       INT NOT NULL,"
                +   "Banks_bankId       INT NOT NULL,"
                +   "PRIMARY KEY        (`bankAccountId`, `Users_userID`, `Banks_bankId`),"
                +   "INDEX              `fk_BankAccounts_Banks_idx`            (`Banks_bankId` ASC) VISIBLE,"
                +   "INDEX              `fk_BankAccounts_Users_idx`            (`Users_userId` ASC) VISIBLE,"

                +   "CONSTRAINT         `fk_BankAccounts_Users`"
                +       "FOREIGN KEY (`Users_userId`)"
                +       "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION ,"

                +   "CONSTRAINT         `fk_BankAccounts_Banks`"
                +       "FOREIGN KEY (`Banks_bankId`)"
                +       "REFERENCES `" + DB_NAME + "`.`Banks` (`bankId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION "
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Coupons table if it doesn't exist
    private void createCouponsTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`Coupons`"
                +   "("
                +   "couponId           INT NOT NULL AUTO_INCREMENT,"
                +   "code               varchar(25) NOT NULL,"
                +   "description        varchar(50),"
                +   "status             varchar(12) NOT NULL,"
                +   "discount           float NOT NULL,"
                +   "PRIMARY KEY        (`couponId`),"
                +   "UNIQUE INDEX       `code_UNIQUE`                           (`code` ASC) VISIBLE"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Order Numbers table if it doesn't exist
    private void createOrderNumbersTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`OrderNumbers`"
                +   "("
                +   "orderId            INT NOT NULL AUTO_INCREMENT,"
                +   "orderDate          DATETIME NOT NULL,"
                +   "Users_userId       INT NOT NULL,"
                +   "Coupons_couponId   INT NOT NULL,"
                +   "BankAccounts_bankAccountId     INT NOT NULL,"
                +   "PRIMARY KEY        (`orderId`, `Users_userId`, `Coupons_couponId`, `BankAccounts_bankAccountId`),"
                +   "INDEX              `fk_OrderNumbers_Users_idx`            (`Users_userId` ASC) VISIBLE,"
                +   "INDEX              `fk_OrderNumbers_Coupons_idx`          (`Coupons_couponId` ASC) VISIBLE,"
                +   "INDEX              `fl_OrderNumbers_BankAccounts_idx`     (`BankAccounts_bankAccountId` ASC) VISIBLE,"

                +   "CONSTRAINT         `fk_OrderNumbers_Users`"
                +       "FOREIGN KEY (`Users_userId`)"
                +       "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION ,"

                +   "CONSTRAINT         `fk_OrderNumbers_Coupons`"
                +       "FOREIGN KEY (`Coupons_couponId`)"
                +       "REFERENCES `" + DB_NAME + "`.`Coupons` (`couponId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION ,"

                +   "CONSTRAINT         `fl_OrderNumbers_BankAccounts`"
                +       "FOREIGN KEY (`BankAccounts_bankAccountId`)"
                +       "REFERENCES `" + DB_NAME + "`.`BankAccounts` (`bankAccountId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION "
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Retail Inventory table if it doesn't exist
    private void createRetailInventoryTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`RetailInventory`"
                +   "("
                +   "itemId             INT NOT NULL AUTO_INCREMENT,"
                +   "name               varchar(50) NOT NULL,"
                +   "price              DECIMAL(8,2) NOT NULL,"
                +   "description        varchar(200) NULL,"
                +   "quantity           int NOT NULL DEFAULT 0,"
                +   "PRIMARY KEY        (`itemId`)"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Sold Items table if it doesn't exist
    private void createSoldItemsTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`SoldItems`"
                +   "("
                +   "sellId                 INT NOT NULL AUTO_INCREMENT,"
                +   "quantity               INT NULL DEFAULT 1,"
                +   "status                 varchar(12) NOT NULL DEFAULT 'In Transit',"
                +   "expectedDelivery       DATETIME NOT NULL,"
                +   "OrderNumbers_orderId   int NOT NULL,"
                +   "RetailInventory_itemId int NOT NULL,"
                +   "PRIMARY KEY            (`sellId`, `OrderNumbers_orderId`, `RetailInventory_itemId`),"
                +   "INDEX                  `fk_SoldItems_OrderNumbers_idx`     (`OrderNumbers_orderId` ASC) VISIBLE,"
                +   "INDEX                  `fk_SoldItems_RetailInventory_idx`  (`RetailInventory_itemId` ASC) VISIBLE,"

                +   "CONSTRAINT `fk_SoldItems_OrderNumbers`"
                +       "FOREIGN KEY (`OrderNumbers_orderId`)"
                +       "REFERENCES `" + DB_NAME + "`.`OrderNumbers` (`orderId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION, "

                +   "CONSTRAINT `fk_SoldItems_RetailInventory`"
                +       "FOREIGN KEY (`RetailInventory_itemId`)"
                +       "REFERENCES `" + DB_NAME + "`.`RetailInventory` (`itemId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the User Cart table if it doesn't exist
    private void createUserCartTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`UserCart`"
                +   "("
                +   "cartItemId                 INT NOT NULL AUTO_INCREMENT,"
                +   "Users_userId               INT NOT NULL,"
                +   "RetailInventory_itemId     INT NOT NULL,"
                +   "quantity                   INT NOT NULL DEFAULT 1,"
                +   "PRIMARY KEY                (`cartItemId`, `Users_userId`, `RetailInventory_itemId`),"
                +   "INDEX                      `fk_UserCart_Users_userId_idx` (`Users_userId` ASC) VISIBLE,"
                +   "INDEX                      `fk_UserCart_RetailInventory_itemId_idx` (`RetailInventory_itemId` ASC) VISIBLE,"

                +   "CONSTRAINT `fk_UserCart_Users_userId`"
                +       "FOREIGN KEY (`Users_userId`)"
                +       "REFERENCES `" + DB_NAME + "`.`Users` (`userId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION, "

                +   "CONSTRAINT `fk_UserCart_RetailInventory_itemId`"
                +       "FOREIGN KEY (`RetailInventory_itemId`)"
                +       "REFERENCES `" + DB_NAME + "`.`RetailInventory` (`itemId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Create the Coupon Items table if it doesn't exist
    private void createCouponItemsTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`CouponItems`"
                +   "("
                +   "couponItemId               INT NOT NULL AUTO_INCREMENT,"
                +   "Coupons_couponId           INT NOT NULL,"
                +   "RetailInventory_itemId     INT NOT NULL,"
                +   "PRIMARY KEY                (`couponItemId`, `Coupons_couponId`, `RetailInventory_itemId`),"
                +   "INDEX                      `fk_CouponItems_Coupons_idx` (`Coupons_couponId` ASC) VISIBLE,"
                +   "INDEX                      `fk_CouponItems_RetailInventory_idx` (`RetailInventory_itemId` ASC) VISIBLE,"

                +   "CONSTRAINT `fk_CouponItems_Coupons`"
                +       "FOREIGN KEY (`Coupons_couponId`)"
                +       "REFERENCES `" + DB_NAME + "`.`Coupons` (`couponId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION, "

                +   "CONSTRAINT `fk_CouponItems_RetailInventory`"
                +       "FOREIGN KEY (`RetailInventory_itemId`)"
                +       "REFERENCES `" + DB_NAME + "`.`RetailInventory` (`itemId`) "
                +       "ON DELETE NO ACTION "
                +       "ON UPDATE NO ACTION"
                +   ")";

        try{
            createStatement = connection.createStatement();
            createStatement.execute(sqlCreate);
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    //Inserts a new user entry into the Users table based on the data obtained from the Sign Up view.
    public void addUser(User user){

        if (user.getAccountName().isEmpty() || user.getPassword().isEmpty()){
            System.out.println("Account name and/or password cannot be empty.");
            return;
        }

        String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Users` "
                +   "(firstName, lastName, accountName, password) "
                +   "VALUES (?,?,?,?)";

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
    public User signIn(String accountName, String password){

        if(accountName.isEmpty() || password.isEmpty()){
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
            if(!resultSet.next()){
                user = null;
            }

            else {

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

    public void updateUser(User user){

        if (user.getId() < 1){
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
    public void addInventoryItem(Item item){
        if (item == null || item.getName().isEmpty()){
            System.out.println("Item cannot be null.");
            return;
        }

        String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`RetailInventory` "
                +   "(name, price, description, quantity) "
                +   "VALUES (?,?,?,?)";

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
    public void updateItemStock(int id, int quantity){

        if (id < 1){
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
    public ArrayList<Item> getInventoryPage(int page, int pageSize){

        ArrayList<Item> itemlist = new ArrayList<Item>(pageSize);
        String sqlStatement = "SELECT * FROM `" + DB_NAME + "`.RetailInventory LIMIT " + String.valueOf((page - 1)*pageSize) + ", " + String.valueOf(pageSize);

        try {

            statement = connection.prepareStatement(sqlStatement);
            ResultSet results =  statement.executeQuery();

            while(results.next()){

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
    public void deleteInventoryItem(int id){

        if (id < 1){
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
    public void addBank(String name){
        if (name.isEmpty()){
            System.out.println("Bank name cannot be empty.");
            return;
        }

        String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`Banks` "
                +   "(name) "
                +   "VALUES (?)";

        try {

            statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, name);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Deletes a Bank row from the Bank table based on the given id.
    public void deleteBank(int id){

        if (id < 1){
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

    public void updateBank(Bank bank){

        if (bank.getId() < 1){
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
    public void addBankAccount(int userId, String bankName, int accountNumber){

        if (bankName.isEmpty()){
            System.out.println("Bank name cannot be empty.");
            return;
        }

        if (accountNumber < 1){
            System.out.println("Bank account number invalid.");
            return;
        }

        String sqlStatement = "INSERT INTO `" + DB_NAME + "`.`BankAccounts` "
                +   "(accountNumber, Users_userId, Banks_bankId) "
                +   "VALUES (?, ?, (SELECT bankId FROM `" + DB_NAME + "`.`Banks` " + " WHERE name = ?))";

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

    public void deleteBankAccount(int id){

        if (id < 1){
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

    public void updateBankAccount(int userId, BankAccount account){

        if (account.getBankAccountId() < 1){
            System.out.println("Given id is less than 1.  Id must be greater than 0.");
            return;
        }

        if (account.getBank() == null || account.getBank().getId() < 1){
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

}
