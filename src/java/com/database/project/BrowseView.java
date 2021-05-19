package com.database.project;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

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

	private final ArrayList<BrowseItemPanel> itemPanels = new ArrayList<>();
	private final GridBagConstraints c;

	private final Comparator<BrowseItemPanel> PRICE = Comparator.comparingDouble(item -> item.getItem().getPrice());
	private final Comparator<BrowseItemPanel> ALPHA = Comparator.comparing(item -> item.getItem().getName());

	public BrowseView(JFrame frame) {
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);

		Inventory inventory = DBDriver.getInventoryPage(1, 100);

		for (Item item : inventory.getAllItems()) {
			BrowseItemPanel itemPanel = new BrowseItemPanel(item);
			itemPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					frame.getContentPane().remove(frame.getContentPane());
					frame.setContentPane(new ItemView(frame, item).getMainPanel());
					frame.revalidate();
					frame.repaint();
					super.mouseClicked(e);
				}
			});
			itemPanels.add(itemPanel);
		}

		getSettingsAndSort();
		resizeComponents(frame);

		inventoryPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeComponents(frame);
			}
		});

		cartButton.addActionListener(e -> {
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new CartView(frame).getMainPanel());
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

		sortComboBox.addActionListener(e -> {
			getSettingsAndSort();
		});

		ascendingComboBox.addActionListener(e -> {
			getSettingsAndSort();
		});

		searchBar.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				getSettingsAndSort();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				getSettingsAndSort();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});

		inventoryPanel.grabFocus();
	}

	public void resizeComponents(Frame frame) {
		for (JPanel itemPanel : itemPanels) {
			int size = (frame.getWidth() - 160) / 5;
			itemPanel.setPreferredSize(new Dimension(size, size));
			itemPanel.setSize(new Dimension(size, size));
		}
	}

	public void getSettingsAndSort() {
		String text = searchBar.getText();
		boolean sortAlpha = true;
		boolean sortAscending = true;
		if (sortComboBox.getSelectedIndex() == 1) {
			sortAlpha = false;
		}
		if (ascendingComboBox.getSelectedIndex() == 1) {
			sortAscending = false;
		}
		searchSortItems(text, sortAlpha, sortAscending);
	}

	public void searchSortItems(String search, boolean alpha, boolean ascending) {
		c.gridx = 0;
		c.gridy = 0;
		inventoryPanel.removeAll();
		if (alpha) {
			if (ascending) {
				itemPanels.sort(ALPHA);
			} else {
				itemPanels.sort(ALPHA.reversed());
			}
		} else {
			if (ascending) {
				itemPanels.sort(PRICE);
			} else {
				itemPanels.sort(PRICE.reversed());
			}
		}
		for (BrowseItemPanel panel : itemPanels) {
			if (panel.getItemLabel().getText().toLowerCase().contains(search.toLowerCase())) {
				c.gridx++;
				if (c.gridx > 5) {
					c.gridx = 1;
					c.gridy++;
				}
				inventoryPanel.add(panel, c);
			}
		}
		inventoryPanel.revalidate();
		inventoryPanel.repaint();
	}
}
