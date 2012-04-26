package com.github.yujiorama.auctionsinper;

import org.junit.Test;

public class AuctionSniperEndToEndTest {

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		FakeAuctionServer auction = new FakeAuctionServer("item-54321");
		auction.startSellingItem();
		ApplicationRunner application = new ApplicationRunner();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
}
