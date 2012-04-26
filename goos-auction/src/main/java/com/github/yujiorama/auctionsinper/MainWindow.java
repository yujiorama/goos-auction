package com.github.yujiorama.auctionsinper;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	public static final String MAIN_WINDOW_NAME = "auction-sniper";

	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
