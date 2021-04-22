
-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Users` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(10) NULL,
  `lastName` VARCHAR(25) NULL,
  `accountName` VARCHAR(25) NOT NULL,
  `password` VARCHAR(25) NOT NULL,
  `cartId` INT NOT NULL,
  PRIMARY KEY (`userId`, `cartId`),
  UNIQUE INDEX `accountName_UNIQUE` (`accountName` ASC) VISIBLE,
  UNIQUE INDEX `cartId_UNIQUE` (`cartId` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Banks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Banks` (
  `bankId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`bankId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`BankAccounts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`BankAccounts` (
  `bankAccountId` INT NOT NULL AUTO_INCREMENT,
  `accountNumber` INT(12) NOT NULL,
  `Users_userId` INT NOT NULL,
  `Banks_bankId` INT NOT NULL,
  PRIMARY KEY (`bankAccountId`, `Users_userId`, `Banks_bankId`),
  INDEX `fk_BankAccounts_Banks_idx` (`Banks_bankId` ASC) VISIBLE,
  INDEX `fk_BankAccounts_Users_idx` (`Users_userId` ASC) VISIBLE,
  CONSTRAINT `fk_BankAccounts_Users`
    FOREIGN KEY (`Users_userId`)
    REFERENCES `mydb`.`Users` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BankAccounts_Banks`
    FOREIGN KEY (`Banks_bankId`)
    REFERENCES `mydb`.`Banks` (`bankId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Coupons`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Coupons` (
  `couponId` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(25) NOT NULL,
  `description` VARCHAR(50) NULL,
  `status` VARCHAR(12) NOT NULL,
  `discount` FLOAT NOT NULL,
  PRIMARY KEY (`couponId`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`OrderNumbers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`OrderNumbers` (
  `orderId` INT NOT NULL AUTO_INCREMENT,
  `orderDate` DATETIME NOT NULL,
  `Users_userId` INT NOT NULL,
  `Coupons_couponId` INT NULL,
  `BankAccounts_bankAccountId` INT NOT NULL,
  PRIMARY KEY (`orderId`, `Users_userId`, `Coupons_couponId`, `BankAccounts_bankAccountId`),
  INDEX `fk_OrderNumbers_Users_idx` (`Users_userId` ASC) VISIBLE,
  INDEX `fk_OrderNumbers_Coupons_idx` (`Coupons_couponId` ASC) VISIBLE,
  INDEX `fl_OrderNumbers_BankAccounts_idx` (`BankAccounts_bankAccountId` ASC) VISIBLE,
  CONSTRAINT `fk_OrderNumbers_Users`
    FOREIGN KEY (`Users_userId`)
    REFERENCES `mydb`.`Users` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrderNumbers_Coupons`
    FOREIGN KEY (`Coupons_couponId`)
    REFERENCES `mydb`.`Coupons` (`couponId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fl_OrderNumbers_BankAccounts`
    FOREIGN KEY (`BankAccounts_bankAccountId`)
    REFERENCES `mydb`.`BankAccounts` (`bankAccountId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`RetailInventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`RetailInventory` (
  `itemId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `price` DECIMAL(8,2) NOT NULL,
  `description` VARCHAR(200) NULL,
  `quantity` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`itemId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`SoldItems`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`SoldItems` (
  `sellId` INT NOT NULL AUTO_INCREMENT,
  `quantity` INT NOT NULL DEFAULT 1,
  `status` VARCHAR(12) NOT NULL DEFAULT 'In Transit',
  `expectedDelivery` DATETIME NOT NULL,
  `OrderNumbers_orderId` INT NOT NULL,
  `RetailInventory_itemId` INT NOT NULL,
  PRIMARY KEY (`sellId`, `OrderNumbers_orderId`, `RetailInventory_itemId`),
  INDEX `fk_SoldItems_OrderNumbers_idx` (`OrderNumbers_orderId` ASC) VISIBLE,
  INDEX `fk_SoldItems_RetailInventory_idx` (`RetailInventory_itemId` ASC) VISIBLE,
  CONSTRAINT `fk_SoldItems_OrderNumbers`
    FOREIGN KEY (`OrderNumbers_orderId`)
    REFERENCES `mydb`.`OrderNumbers` (`orderId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SoldItems_RetailInventory`
    FOREIGN KEY (`RetailInventory_itemId`)
    REFERENCES `mydb`.`RetailInventory` (`itemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`UserCart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`UserCart` (
  `cartItemId` INT NOT NULL AUTO_INCREMENT,
  ` Users_cartId` INT NOT NULL,
  `RetailInventory_itemId` INT NOT NULL,
  `quantity` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`cartItemId`, ` Users_cartId`, `RetailInventory_itemId`),
  INDEX `fk_UserCart_Users_cartId_idx` (` Users_cartId` ASC) VISIBLE,
  INDEX `fk_UserCart_RetailInventory_itemId_idx` (`RetailInventory_itemId` ASC) VISIBLE,
  CONSTRAINT `fk_UserCart_Users_cartId`
    FOREIGN KEY (` Users_cartId`)
    REFERENCES `mydb`.`Users` (`cartId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserCart_RetailInventory_itemId`
    FOREIGN KEY (`RetailInventory_itemId`)
    REFERENCES `mydb`.`RetailInventory` (`itemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CouponItems`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`CouponItems` (
  `couponItemId` INT NOT NULL AUTO_INCREMENT,
  `Coupons_couponId` INT NOT NULL,
  `RetailInventory_itemId` INT NOT NULL,
  PRIMARY KEY (`couponItemId`, `Coupons_couponId`, `RetailInventory_itemId`),
  INDEX `fk_CouponItems_Coupons_idx` (`Coupons_couponId` ASC) VISIBLE,
  INDEX `fk_CouponItems_RetailInventory_idx` (`RetailInventory_itemId` ASC) VISIBLE,
  CONSTRAINT `fk_CouponItems_Coupons`
    FOREIGN KEY (`Coupons_couponId`)
    REFERENCES `mydb`.`Coupons` (`couponId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CouponItems_RetailInventory`
    FOREIGN KEY (`RetailInventory_itemId`)
    REFERENCES `mydb`.`RetailInventory` (`itemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

