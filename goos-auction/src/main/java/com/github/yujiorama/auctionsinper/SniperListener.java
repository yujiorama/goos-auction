package com.github.yujiorama.auctionsinper;

import java.util.EventListener;

public interface SniperListener extends EventListener {

	public void sniperLost();

	public void sniperWinning();

	public void sniperWon();

	public void sniperStateChanged(SniperSnapshot newSnapshot);

}
