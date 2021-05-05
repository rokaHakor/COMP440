package database.com.project;

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

	static {
		Cart cart = Cart.getCart();
		cart.add(new Item(1, "Chair", 1, 12.99, ""));
		cart.add(new Item(2, "Chair", 1, 13.99, ""));
		cart.add(new Item(3, "Chair", 1, 14.99, ""));
		cart.add(new Item(4, "Chair", 1, 15.99, ""));
		cart.add(new Item(5, "Chair", 1, 16.99, ""));
	}

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

		Cart cart = Cart.getCart();

		for (Item item : cart.items()) {
			CartPanel cartPanel = new CartPanel(item);
			cartPanel.getQuantityCombo().addActionListener(e -> {
				int quantity = cartPanel.getQuantityCombo().getSelectedIndex();
				if (quantity == 0) {
					cart.remove(item);
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
				cartPanel.getTotalPriceLabel().setText("     Total: $" + (item.getQuantity() * item.getPrice()));
				grandTotal.setText("Grand Total: $" + cart.getTotalPrice());
			});
			itemPanels.add(cartPanel);
			cartsPanel.add(cartPanel, c);
			c.gridy++;
		}

		grandTotal.setText("Grand Total: $" + cart.getTotalPrice());

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
