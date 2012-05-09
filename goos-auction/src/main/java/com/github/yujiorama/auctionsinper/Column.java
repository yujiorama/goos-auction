package com.github.yujiorama.auctionsinper;

public enum Column {

	ITEM_IDENTIFIER {
		@Override public Object valueIn(SniperSnapshot snapshot) { return snapshot.itemId; }
	},
	
	LAST_PRICE {
		@Override public Object valueIn(SniperSnapshot snapshot) { return snapshot.lastPrice; }
	},
	
	LAST_BID {
		@Override public Object valueIn(SniperSnapshot snapshot) { return snapshot.lastBid; }
	},
	
	SNIPER_STATUS {
		@Override public Object valueIn(SniperSnapshot snapshot) {
			return SniperTableModel.textFor(snapshot.state);
		}
	}
	;
	
	abstract public Object valueIn(SniperSnapshot snapshot);
	
	public static Column at(int offset) { return values()[offset]; }
}
