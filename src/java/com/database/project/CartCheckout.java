package com.database.project;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
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

		});

		saveAddressButton.addActionListener(e -> {
			Address newAddress = getAddress();
			if (newAddress != null) {
				DBDriver.addAddressFull(Main.getUser().getId(), newAddress);
				addresses.add(newAddress);
				savedAddresses.setSelectedItem(newAddress);
			}
		});

		saveBankButton.addActionListener(e -> {
			BankAccount newBank = saveBank();
			if (newBank != null) {
				banks.add(newBank);
				savedBanks.setSelectedItem(newBank);
			}
		});

		savedAddresses.addItemListener(e -> {
			Address selectedAddress = (Address) savedAddresses.getSelectedItem();
			if (selectedAddress != null) {
				addressText.setText(selectedAddress.getAddress());
				cityText.setText(selectedAddress.getCity());
				stateText.setText(selectedAddress.getState());
				countryText.setText(selectedAddress.getCountry());
			}
		});

		savedBanks.addItemListener(e -> {
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

	private BankAccount saveBank() {
		String bank = bankText.getText();
		String account = accountText.getText();

		if (bank == null || account == null) {
			errorLabel.setText("Invalid Bank Field");
			errorLabel.setVisible(true);
			return null;
		}

		DBDriver.addBank(bank);
		DBDriver.addBankAccount(Main.getUser().getId(), bank, Integer.parseInt(account));
		return new BankAccount(0, Integer.parseInt(account), new Bank(0, bank));
	}
}
