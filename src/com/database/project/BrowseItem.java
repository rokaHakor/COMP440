package com.database.project;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class BrowseItem {

	@Getter
	private JPanel mainPanel;
	private JButton imageButton;
	private JLabel nameLabel;
	@Getter
	private JPanel otherPanel;

	public BrowseItem(Item item){
		otherPanel.setPreferredSize(new Dimension(50,50));
		nameLabel.setText(item.getName());
	}
}
