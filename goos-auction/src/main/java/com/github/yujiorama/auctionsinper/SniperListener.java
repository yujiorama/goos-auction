package com.github.yujiorama.auctionsinper;

import java.util.EventListener;

public interface SniperListener extends EventListener {

	public void sniperLost();

	public void sniperBidding();

	public void sniperWinning();

}
