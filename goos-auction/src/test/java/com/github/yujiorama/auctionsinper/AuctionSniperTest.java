package com.github.yujiorama.auctionsinper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class AuctionSniperTest {

	private Mockery context = new Mockery();

	@Test
	public void report_lost_when_auction_closed() {
		Expectations expectations = new Expectations();
		SniperListener sniperListener = context.mock(SniperListener.class);
		expectations.oneOf(sniperListener).sniperLost();
		context.checking(expectations);
		AuctionSniper sniper = new AuctionSniper(sniperListener);
		sniper.auctionClosed();
	}
	
	@Test
	public void bids_higher_and_reports_bidding_when_new_price_arrives() {
		Auction auction = context.mock(Auction.class);
		SniperListener sniperListener = context.mock(SniperListener.class);
		Expectations expectations = new Expectations();
		int price = 1001;
		int increment = 25;
		expectations.oneOf(auction).bid(price + increment);
		expectations.atLeast(1).of(sniperListener).sniperBidding();
		context.checking(expectations);
		AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
		sniper.currentPrice(price, increment);
	}

}
