package com.github.yujiorama.auctionsinper;

public enum Column {

	ITEM_IDENTIFIER("Item") {
		@Override public Object valueIn(SniperSnapshot snapshot) { return snapshot.itemId; }
	},
	
	LAST_PRICE("Last Price") {
		@Override public Object valueIn(SniperSnapshot snapshot) { return snapshot.lastPrice; }
	},
	
	LAST_BID("Last Bid") {
		@Override public Object valueIn(SniperSnapshot snapshot) { return snapshot.lastBid; }
	},
	
	SNIPER_STATUS("State") {
		@Override public Object valueIn(SniperSnapshot snapshot) {
			return SniperTableModel.textFor(snapshot.state);
		}
	}
	;
	
	public final String label;

	abstract public Object valueIn(SniperSnapshot snapshot);
	
	public static Column at(int offset) { return values()[offset]; }
	
	private Column(String label) {
		this.label = label;
	}
}
