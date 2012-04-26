package com.github.yujiorama.auctionsinper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final String MAIN_WINDOW_NAME = "auction-sniper";
	public static final String SNIPER_STATUS_NAME = "sniper status";

	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(new Rectangle(new Point(50, 50), new Dimension(320, 240)));
		add(newLabel(AuctionStatus.JOINING));
		setVisible(true);
	}

	private JLabel newLabel(AuctionStatus auctionStatus) {
		JLabel label = new JLabel(auctionStatus.toString());
		label.setName(SNIPER_STATUS_NAME);
		label.setBorder(new LineBorder(Color.BLACK));
		return label;
	}
}
