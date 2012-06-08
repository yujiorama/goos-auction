package com.github.yujiorama.auctionsinper;

import org.junit.Before;
import org.junit.Test;

public class AuctionSniperEndToEndTest {
	FakeAuctionServer auction;
	FakeAuctionServer auction2;
	ApplicationRunner application;
	
	@Before
	public void before() {
		auction = new FakeAuctionServer("item-54321");
		auction2 = new FakeAuctionServer("item-65432");
		application = new ApplicationRunner();
	}

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
	
	@Test
	public void sniper_makes_a_higer_bid_but_loses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098);
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
	
	@Test
	public void sniper_wins_an_auction_by_bidding_higher() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098);
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(auction, 1098);
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(auction, 1098);
	}
	
	@Test
	public void sniper_bids_for_multiple_items() throws Exception {
		auction.startSellingItem();
		auction2.startSellingItem();
		
		application.startBiddingIn(auction, auction2);
		
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction2.reportPrice(500, 21, "some bidder");
		auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1098, 72, ApplicationRunner.SNIPER_XMPP_ID);
		auction2.reportPrice(521, 45, ApplicationRunner.SNIPER_XMPP_ID);
		
		application.hasShownSniperIsWinning(auction, 1098);
		application.hasShownSniperIsWinning(auction2, 521);
		
		auction.announceClosed();
		auction2.announceClosed();
		
		application.showsSniperHasWonAuction(auction, 1098);
		application.showsSniperHasWonAuction(auction2, 521);
		
		
	}
}
