package com.github.yujiorama.auctionsinper;

public enum SniperState {
	JOINNING {
		@Override public SniperState whenAuctionClosed() { return LOST; }
	},
	LOST,
	BIDDING {
		@Override public SniperState whenAuctionClosed() { return LOST; }
	},
	WON,
	WINNING {
		@Override public SniperState whenAuctionClosed() { return WON; }
	}
	;

	public SniperState whenAuctionClosed() {
		throw new IllegalStateException("Auction is already closed");
	}
}
