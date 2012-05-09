package com.github.yujiorama.auctionsinper;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String APPLICATION_NAME = "Auction Sniper";
	public static final String MAIN_WINDOW_NAME = "auction-sniper";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String SNIPER_TABLE_NAME = "sniper table";
	private SniperTableModel sniperTableModel = new SniperTableModel();
	private JTable snipersTable = newTable(SNIPER_TABLE_NAME);

	public MainWindow() {
		super(APPLICATION_NAME);
		setName(MAIN_WINDOW_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(new Rectangle(new Point(50, 50), new Dimension(320, 240)));
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
		setVisible(true);
	}

	private JTable newTable(String name) {
		JTable table = new JTable(sniperTableModel);
		table.setName(name);
		return table;
	}

	public void showStatus(AuctionStatus auctionStatus) {
		sniperTableModel.setStatusText(auctionStatus.toString());
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		sniperTableModel.sniperStatusChanged(newSnapshot);
	}
}
