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
	
	@Test
	public void sniper_makes_a_higer_bid_but_loses() throws Exception {
		FakeAuctionServer auction = new FakeAuctionServer("item-54321");
		auction.startSellingItem();
		ApplicationRunner application = new ApplicationRunner();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding();
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
}
