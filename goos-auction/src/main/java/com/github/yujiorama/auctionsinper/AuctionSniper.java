package com.github.yujiorama.auctionsinper;

public class AuctionSniper implements AuctionEventListener {

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
		switch (priceSource) {
		case FromOtherBidder:
			this.auction.bid(currentPrice + increment);
			this.sniperListener.sniperBidding(new SniperState(itemId, currentPrice, currentPrice + increment));
			this.isWinning = false;
			break;
		case FromSniper:
			this.sniperListener.sniperWinning();
			this.isWinning = true;
			break;
		}
	}

}
