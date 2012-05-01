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
	private String statusText = AuctionStatus.JOINING.toString();
	private final static SniperState STARTING_UP = new SniperState("", 0, 0);
	private SniperState sniperState = STARTING_UP;

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	public void setStatusText(String newStatus) {
		this.statusText = newStatus;
		fireTableCellUpdated(0, 0);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (Column.at(columnIndex)) {
		case ITEM_IDENTIFIER:
			return sniperState.itemId;
		case LAST_PRICE:
			return sniperState.lastPrice;
		case LAST_BID:
			return sniperState.lastBid;
		case SNIPER_STATUS:
			return statusText;
		}
		throw new IllegalArgumentException(String.format("No columns at %s", columnIndex));
	}

	public void sniperStatusChanged(SniperState newState, AuctionStatus newStatus) {
		sniperState = newState;
		statusText = newStatus.toString();
		fireTableRowsUpdated(0, 0);
	}
	
	
}
