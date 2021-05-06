package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ItemView {
	private JPanel inventoryPanel;
	private JButton logoutButton;
	private JButton orderHistoryButton;
	private JButton cartButton;
	@Getter
	private JPanel mainPanel;
	private JButton browseButton;
	private JLabel itemName;
	private JLabel itemImage;
	private JLabel itemDescription;
	private JLabel itemStock;
	private JSpinner addQuantity;
	private JButton addToCartButton;

	private final Image image;

	public ItemView(JFrame frame, Item item) {
		logoutButton.addActionListener(e -> {
			Cart.getCart().clear();
			Main.setUser(null);
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new LoginView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
		orderHistoryButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new OrderHistory(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
		cartButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new CartView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
		browseButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new BrowseView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});

		URL imageURL = this.getClass().getResource(item.getName() + ".jpg");
		Toolkit tk = mainPanel.getToolkit();
		this.image = tk.getImage(imageURL);
		itemImage.setText("");
		itemImage.setIcon(new ImageIcon(image));
		itemName.setText(item.getName());
		itemDescription.setText(item.getDescription());
		itemStock.setText("Current Stock: " + item.getQuantity());

		resizeComponents(frame);
		inventoryPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeComponents(frame);
			}
		});

		((SpinnerNumberModel) addQuantity.getModel()).setMinimum(0);
		((SpinnerNumberModel) addQuantity.getModel()).setMaximum(item.getQuantity());

		addToCartButton.addActionListener(e -> {
			Item addCartItem = new Item(item, (Integer) addQuantity.getValue());
			Cart.getCart().add(addCartItem);
			DBDriver.addCartItem(Main.getUser().getId(), addCartItem.getItemID(), addCartItem.getQuantity());
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new BrowseView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
	}

	private void resizeComponents(JFrame frame) {
		int width = (frame.getWidth() - 160);
		int height = (frame.getHeight()) / 2;
		setSize(new Dimension(width, height));
	}


	private void setSize(Dimension d) {
		int size = Math.min(d.width, d.height);
		BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, size, size, null);
		graphics2D.dispose();
		itemImage.setIcon(new ImageIcon(resizedImage));
	}
}
