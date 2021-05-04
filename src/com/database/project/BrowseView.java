package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
			JPanel itemPanel = new JPanel(new BorderLayout());
			int size = (frame.getWidth() - 160) / 5;
			itemPanel.setPreferredSize(new Dimension(size, size));
			JButton itemButton = new JButton();
			JLabel itemLabel = new JLabel(item.getName() + "  -  $" + item.getPrice());
			itemLabel.setHorizontalAlignment(SwingConstants.CENTER);
			itemPanel.add(itemButton, BorderLayout.CENTER);
			itemPanel.add(itemLabel, BorderLayout.SOUTH);
			inventoryPanel.add(itemPanel, c);
		}

		orderHistoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().remove(frame.getContentPane());
				frame.setContentPane(new OrderHistory(frame).getPanel());
				frame.revalidate();
				frame.repaint();
			}
		});
	}

}
