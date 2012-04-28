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

}
