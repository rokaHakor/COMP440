package com.database.project;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.sql.Date;
import java.time.Instant;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartCheckout extends JFrame {
	@Getter
	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private JPanel formPanel;
	private JButton browseButton;
	private JButton logoutButton;
	private JButton orderHistoryButton;
	private JButton cartButton;
	private JLabel grandTotal;
	private JTextField countryText;
	private JTextField bankText;
	private JTextField accountText;
	private JButton submitButton;
	private JTextField couponText;
	private JTextField stateText;
	private JButton saveAddressButton;
	private JComboBox<BankAccount> savedBanks;
	private JButton saveBankButton;
	private JTextField cityText;
	private JTextField addressText;
	private JComboBox<Address> savedAddresses;
	private JLabel errorLabel;
	private JButton applyButton;

	private final Vector<Address> addresses;
	private final Vector<BankAccount> banks;

	public CartCheckout(JFrame frame) {
		errorLabel.setVisible(false);
		grandTotal.setText("Grand Total: $" + String.format("%,.2f", Cart.getCart().getTotalPrice()));

		addresses = new Vector<>(DBDriver.getAddresses(Main.getUser().getId()));
		DefaultComboBoxModel<Address> addressModel = new DefaultComboBoxModel<>(addresses);
		savedAddresses.setModel(addressModel);

		banks = new Vector<>(DBDriver.getBankAccounts(Main.getUser().getId()));
		DefaultComboBoxModel<BankAccount> bankModel = new DefaultComboBoxModel<>(banks);
		savedBanks.setModel(bankModel);

		accountText.setColumns(10);
		PlainDocument doc = (PlainDocument) accountText.getDocument();
		doc.setDocumentFilter(new DocumentFilter() {
			final Pattern regEx = Pattern.compile("\\d*");

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				Matcher matcher = regEx.matcher(text);
				if (!matcher.matches()) {
					return;
				}
				super.replace(fb, offset, length, text, attrs);
			}
		});

		browseButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new BrowseView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		orderHistoryButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new OrderHistory(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		logoutButton.addActionListener(e -> {
			Cart.getCart().clear();
			Main.setUser(null);
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new LoginView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		cartButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new CartView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		submitButton.addActionListener(e -> {
			if(Cart.getCart().items().isEmpty()){
				errorLabel.setText("Cart is empty!");
				errorLabel.setVisible(true);
				return;
			}
			
			int orderID = DBDriver.addOrderNumber(Main.getUser().getId(), getBank(), Cart.getCart().getCoupon());
			for (Item item : Cart.getCart().items()) {
				Date date = new Date(Instant.now().toEpochMilli() + (3 * 24 * 60 * 60 * 1000));
				DBDriver.addSoldItem(orderID, item.getItemID(), item.getQuantity(), date);
				DBDriver.updateItemStock(item.getItemID(), item.getQuantity());
			}
			DBDriver.deleteCartItem_All(Main.getUser().getId());
			Cart.getCart().clear();
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new BrowseView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		applyButton.addActionListener(e -> {
			Coupon coupon = DBDriver.checkCoupon(couponText.getText());
			if (coupon != null) {
				Cart.getCart().setCoupon(coupon);
				errorLabel.setText("Coupon Saving " + Math.round(coupon.getDiscount() * 100) + "%");
				errorLabel.setVisible(true);
				grandTotal.setText("Grand Total: $" + String.format("%,.2f", Cart.getCart().getTotalPrice()));
			} else {
				errorLabel.setText("Invalid Coupon!");
				errorLabel.setVisible(true);
			}
		});

		saveAddressButton.addActionListener(e -> {
			Address newAddress = getAddress();
			if (newAddress != null) {
				DBDriver.addAddressFull(Main.getUser().getId(), newAddress);
				addresses.add(newAddress);
				savedAddresses.setSelectedItem(newAddress);
			}
			savedAddresses.revalidate();
		});

		saveBankButton.addActionListener(e -> {
			BankAccount newBank = getBank();
			if (newBank != null) {
				DBDriver.addBank(newBank.getBank().getName());
				newBank.setBankAccountId(DBDriver.addBankAccount(Main.getUser().getId(),
						newBank.getBank().getName(), newBank.getAccountNumber()));
				banks.add(newBank);
				savedBanks.setSelectedItem(newBank);
			}
			savedBanks.revalidate();
		});

		savedAddresses.addActionListener(e -> {
			Address selectedAddress = (Address) savedAddresses.getSelectedItem();
			if (selectedAddress != null) {
				addressText.setText(selectedAddress.getAddress());
				cityText.setText(selectedAddress.getCity());
				stateText.setText(selectedAddress.getState());
				countryText.setText(selectedAddress.getCountry());
			}
		});

		savedBanks.addActionListener(e -> {
			BankAccount selectedBank = (BankAccount) savedBanks.getSelectedItem();
			if (selectedBank != null) {
				bankText.setText(selectedBank.getBank().getName());
				accountText.setText("" + selectedBank.getAccountNumber());
			}
		});
	}

	private Address getAddress() {
		String address = addressText.getText();
		String city = cityText.getText();
		String state = stateText.getText();
		String country = countryText.getText();

		if (address == null || city == null || state == null || country == null) {
			errorLabel.setText("Invalid Address Field");
			errorLabel.setVisible(true);
			return null;
		}

		return new Address(0, address, city, state, country);
	}

	private BankAccount getBank() {
		String bank = bankText.getText();
		String account = accountText.getText();

		if (bank == null || account == null) {
			errorLabel.setText("Invalid Bank Field");
			errorLabel.setVisible(true);
			return null;
		}

		return new BankAccount(0, Integer.parseInt(account), new Bank(0, bank));
	}
}
