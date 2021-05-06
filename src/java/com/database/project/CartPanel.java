package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class CartPanel extends JPanel {

	@Getter
	private final Item item;

	@Getter
	private JComboBox<Integer> quantityCombo;

	@Getter
	private final JLabel totalPriceLabel;

	private final JLabel imageLabel;
	private final Image image;

	public CartPanel(Item item) {
		this.item = item;
		this.setLayout(new FlowLayout());

		this.setAlignmentY(0);
		URL imageURL = this.getClass().getResource(item.getName() + ".jpg");
		Toolkit tk = this.getToolkit();
		this.image = tk.getImage(imageURL);
		this.imageLabel = new JLabel(new ImageIcon(image));
		add(this.imageLabel, BorderLayout.CENTER);

		JPanel other = new JPanel(new BorderLayout());
		JLabel nameAndPrice = new JLabel(item.getName() + " - $" + item.getPrice() + "               ");
		other.add(nameAndPrice, BorderLayout.NORTH);

		JLabel quantityLabel = new JLabel("Quantity: ");
		other.add(quantityLabel, BorderLayout.WEST);

		Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		quantityCombo = new JComboBox<>(numbers);
		quantityCombo.setSelectedItem(item.getQuantity());
		quantityCombo.setAlignmentY(Component.CENTER_ALIGNMENT);
		other.add(quantityCombo, BorderLayout.CENTER);

		this.totalPriceLabel = new JLabel("     Total: $" + (item.getQuantity() * item.getPrice()));
		this.totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		other.add(this.totalPriceLabel, BorderLayout.EAST);
		add(other);
	}

	@Override
	public void setSize(Dimension d) {
		int size = Math.min(d.width, d.height);
		BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, size, size, null);
		graphics2D.dispose();
		imageLabel.setIcon(new ImageIcon(resizedImage));
		super.setSize(d);
	}
}
