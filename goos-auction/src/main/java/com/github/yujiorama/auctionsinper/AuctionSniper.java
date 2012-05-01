package com.github.yujiorama.auctionsinper;

public class AuctionSniper implements AuctionEventListener {

	private final Auction auction;
	private final SniperListener sniperListener;

	public AuctionSniper(SniperListener sniperListener) {
		this.auction = null;
		this.sniperListener = sniperListener;
	}

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionClosed() {
		this.sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
		switch (priceSource) {
		case FromOtherBidder:
			this.auction.bid(currentPrice + increment);
			this.sniperListener.sniperBidding();
			break;
		case FromSniper:
			this.sniperListener.sniperWinning();
			break;
		}
	}

}
