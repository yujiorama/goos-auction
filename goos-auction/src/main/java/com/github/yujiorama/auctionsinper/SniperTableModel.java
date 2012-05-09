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
	private String state = AuctionStatus.JOINING.toString();
	private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0);
	private static final String[] STATUS_TEXT = {
		AuctionStatus.JOINING.toString(),
		AuctionStatus.BIDDING.toString()
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

	public void setStatusText(String newStatus) {
		this.state = newStatus;
		fireTableCellUpdated(0, 0);
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
			return state;
		}
		throw new IllegalArgumentException(String.format("No columns at %s", columnIndex));
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		snapshot = newSnapshot;
		state = STATUS_TEXT[newSnapshot.state.ordinal()];
		fireTableRowsUpdated(0, 0);
	}
	
	
}
