package com.github.yujiorama.auctionsinper;

public class AuctionSniper implements AuctionEventListener {

	public enum SniperState {
		JOINNING,
		BIDDING,
		WINNING,
		LOST,
		WON;
	}
	
	private final Auction auction;
	private final SniperListener sniperListener;
	private boolean isWinning;
	private final String itemId;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.itemId = itemId;
		this.auction = auction;
		this.sniperListener = sniperListener;
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
			this.sniperListener.sniperWinning();
		} else {
			final int bid = currentPrice + increment;
			this.auction.bid(bid);
			this.sniperListener.sniperStateChanged(
				new SniperSnapshot(itemId, currentPrice, bid, SniperState.BIDDING)
			);
		}
	}

}
