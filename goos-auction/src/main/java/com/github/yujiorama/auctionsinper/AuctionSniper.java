package com.github.yujiorama.auctionsinper;

public class AuctionSniper implements AuctionEventListener {

	public enum SniperState {
		JOINNING,
		LOST,
		BIDDING,
		WON,
		WINNING;
	}
	
	private final Auction auction;
	private final SniperListener sniperListener;
	private boolean isWinning;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = SniperSnapshot.joining(itemId);
	}

	@Override
	public void auctionClosed() {
		if (isWinning) {
			this.sniperListener.sniperWon();
		} else {
			this.sniperListener.sniperLost();
		}
	}

	@Override
	public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper;
		if (isWinning) {
			this.snapshot = snapshot.winning(currentPrice);
		} else {
			final int bid = currentPrice + increment;
			this.auction.bid(bid);
			this.snapshot = snapshot.bidding(currentPrice, bid);
		}
		this.sniperListener.sniperStateChanged(this.snapshot);
	}

}
