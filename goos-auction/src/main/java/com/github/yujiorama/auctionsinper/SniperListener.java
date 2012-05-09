package com.github.yujiorama.auctionsinper;

import java.util.EventListener;

public interface SniperListener extends EventListener {

	public void sniperStateChanged(SniperSnapshot newSnapshot);

}
