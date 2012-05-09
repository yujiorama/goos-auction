package com.github.yujiorama.auctionsinper;

import javax.swing.table.AbstractTableModel;

public class SniperTableModel extends AbstractTableModel {

	public enum Column {
		ITEM_IDENTIFIER,
		LAST_PRICE,
		LAST_BID,
		SNIPER_STATUS;
		
		public static Column at(int offset) { return values()[offset]; }
	}

	private static final long serialVersionUID = 1L;
	private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINNING);
	private static final String[] STATUS_TEXT = {
		AuctionStatus.JOINING.toString(),
		AuctionStatus.LOST.toString(),
		AuctionStatus.BIDDING.toString(),
		AuctionStatus.WON.toString(),
		AuctionStatus.WINNING.toString(),
	};
	private SniperSnapshot snapshot = STARTING_UP;

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (Column.at(columnIndex)) {
		case ITEM_IDENTIFIER:
			return snapshot.itemId;
		case LAST_PRICE:
			return snapshot.lastPrice;
		case LAST_BID:
			return snapshot.lastBid;
		case SNIPER_STATUS:
			return textFor(snapshot.state);
		}
		throw new IllegalArgumentException(String.format("No columns at %s", columnIndex));
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		snapshot = newSnapshot;
		fireTableRowsUpdated(0, 0);
	}
	
	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}
	
}
