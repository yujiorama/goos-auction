package com.github.yujiorama.auctionsinper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class SniperTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static final String[] STATUS_TEXT = {
		AuctionStatus.JOINNING.toString(),
		AuctionStatus.LOST.toString(),
		AuctionStatus.BIDDING.toString(),
		AuctionStatus.WON.toString(),
		AuctionStatus.WINNING.toString(),
	};
	private List<SniperSnapshot> sniperSnapshots = new ArrayList<SniperSnapshot>();

	@Override
	public int getRowCount() {
		return sniperSnapshots.size();
	}

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public String getColumnName(int column) {
		return Column.at(column).label;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(sniperSnapshots.get(rowIndex));
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		for (int i = 0; i < sniperSnapshots.size(); i++) {
			SniperSnapshot shot = sniperSnapshots.get(i);
			if (newSnapshot.itemId.equals(shot.itemId)) {
				sniperSnapshots.set(i, newSnapshot);
				fireTableRowsUpdated(i, i);
			}
		}
	}
	
	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	public void addSniper(SniperSnapshot snapshot) {
		this.sniperSnapshots.add(snapshot);
		fireTableRowsUpdated(this.sniperSnapshots.size()-1, this.sniperSnapshots.size()-1);
	}
	
}
