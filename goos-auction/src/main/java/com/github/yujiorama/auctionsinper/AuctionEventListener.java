package com.github.yujiorama.auctionsinper;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener {
	public enum PriceSource {
		FromOtherBidder, FromSniper
		
	}

	public void auctionClosed();

	public void currentPrice(int currentPrice, int increment, PriceSource priceSource);
}

