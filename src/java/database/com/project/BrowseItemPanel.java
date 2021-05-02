package database.com.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class BrowseItemPanel extends JPanel {

	@Getter
	private final Item item;

	@Getter
	private final JLabel itemLabel;

	private final JLabel imageLabel;
	private final Image image;

	public BrowseItemPanel(Item item) {
		this.item = item;
		this.setLayout(new BorderLayout());
		URL imageURL = this.getClass().getResource(item.getName() + ".jpg");
		Toolkit tk = this.getToolkit();
		this.image = tk.getImage(imageURL);
		this.imageLabel = new JLabel(new ImageIcon(image));
		this.itemLabel = new JLabel(item.getName() + "  -  $" + item.getPrice());
		itemLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.imageLabel, BorderLayout.CENTER);
		add(this.itemLabel, BorderLayout.SOUTH);
	}

	@Override
	public void setSize(Dimension d) {
		BufferedImage resizedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, d.width, d.height, null);
		graphics2D.dispose();
		imageLabel.setIcon(new ImageIcon(resizedImage));
		super.setSize(d);
	}
}
