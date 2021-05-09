package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class CartView {
	private JScrollPane scrollPane;
	private JPanel cartsPanel;
	private JButton browseButton;
	private JButton logoutButton;
	private JButton orderHistoryButton;
	@Getter
	private JPanel mainPanel;
	private JButton checkoutButton;
	private JLabel grandTotal;

	private final ArrayList<CartPanel> itemPanels = new ArrayList<>();

	public CartView(JFrame frame) {
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		checkoutButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new CartCheckout(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
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

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);

		Cart cart = DBDriver.getCart(Main.getUser().getId());

		for (Item item : cart.items()) {
			CartPanel cartPanel = new CartPanel(item);
			cartPanel.getQuantityCombo().addActionListener(e -> {
				int quantity = cartPanel.getQuantityCombo().getSelectedIndex();
				if (quantity == 0) {
					cart.remove(item);
					DBDriver.deleteCartItem_Single(Main.getUser().getId(), item.getItemID());
					SwingUtilities.invokeLater(() ->
					{
						frame.getContentPane().remove(frame.getContentPane());
						frame.setContentPane(new CartView(frame).getMainPanel());
						frame.revalidate();
						frame.repaint();
					});
					return;
				}

				if (quantity > item.getQuantity()) {
					cart.add(new Item(item, quantity - item.getQuantity()));
				} else {
					cart.remove(new Item(item, item.getQuantity() - quantity));
				}
				cartPanel.getTotalPriceLabel().setText("     Total: $" + String.format("%,.2f", (item.getQuantity() * item.getPrice())));
				grandTotal.setText("Grand Total: $" + String.format("%,.2f", cart.getTotalPrice()));
			});
			itemPanels.add(cartPanel);
			cartsPanel.add(cartPanel, c);
			c.gridy++;
		}

		grandTotal.setText("Grand Total: $" + String.format("%,.2f", cart.getTotalPrice()));

		cartsPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = (frame.getWidth() - 160);
				int height = (frame.getHeight()) / 3;
				for (JPanel itemPanel : itemPanels) {
					itemPanel.setPreferredSize(new Dimension(width, height));
					itemPanel.setSize(new Dimension(width, height));
				}
			}
		});
	}
}
