package com.database.project;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

	public MainFrame() {
		setTitle("Database Management System");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) (screenSize.width * .6), (int) (screenSize.height * .6));
		setLocation((int) (screenSize.width * .1), (int) (screenSize.height * .1));
		setContentPane(new MainPanel().getPanel());
	}
}