package database.com.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class BrowseView {

	@Getter
	private JPanel mainPanel;
	private JTextField searchBar;
	private JPanel inventoryPanel;
	private JScrollPane scrollPane;
	private JComboBox<String> sortComboBox;
	private JComboBox<String> ascendingComboBox;
	private JButton cartButton;
	private JButton logoutButton;
	private JButton orderHistoryButton;

	private final ArrayList<JPanel> itemPanels = new ArrayList<>();

	public BrowseView(JFrame frame) {
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);

		for (int x = 0; x < 100; x++) {
			c.gridx++;
			if (c.gridx > 5) {
				c.gridx = 1;
				c.gridy++;
			}
			Item item = new Item(1, "Chair", 1, 12.99, "");
			BrowseItemPanel itemPanel = new BrowseItemPanel(item);
			itemPanels.add(itemPanel);
			inventoryPanel.add(itemPanel, c);
		}

		inventoryPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				for (JPanel itemPanel : itemPanels) {
					int size = (frame.getWidth() - 160) / 5;
					itemPanel.setPreferredSize(new Dimension(size, size));
					itemPanel.setSize(new Dimension(size, size));
				}
			}
		});
	}
}
