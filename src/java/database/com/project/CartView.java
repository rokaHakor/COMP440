package database.com.project;

import lombok.Getter;

import javax.swing.*;

public class CartView {
	private JScrollPane scrollPane;
	private JPanel inventoryPanel;
	private JButton browseButton;
	private JButton logoutButton;
	private JButton orderHistoryButton;
	@Getter
	private JPanel mainPanel;

	public CartView(JFrame frame) {


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
			frame.getContentPane().remove(frame.getContentPane());
			frame.setContentPane(new LoginView(frame).getMainPanel());
			frame.revalidate();
			frame.repaint();
		});
	}
}
