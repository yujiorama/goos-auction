package com.github.yujiorama.auctionsinper;

public class AuctionSniper implements AuctionEventListener {

	private final SniperListener sniperListener;

	public AuctionSniper(SniperListener sniperListener) {
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionClosed() {
		this.sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int currentPrice, int increment) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
