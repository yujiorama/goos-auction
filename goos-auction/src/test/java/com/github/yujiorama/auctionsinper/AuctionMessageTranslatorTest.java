package com.github.yujiorama.auctionsinper;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.yujiorama.auctionsinper.AuctionEventListener.PriceSource;

@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {
	private static final Chat UNUSED_CHAT = null;
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);

	@Test
	public void notify_auction_closed_when_close_message_received() {
		context.checking(new Expectations(){{
			oneOf(listener).auctionClosed();
		}});
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		AuctionMessageTranslator translator = new AuctionMessageTranslator(null, listener);
		translator.processMessage(UNUSED_CHAT, message);
	}

	@Test
	public void notify_bid_details_when_current_price_message_received_from_other_bidder() {
		context.checking(new Expectations(){{
			exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromOtherBidder);
		}});
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		AuctionMessageTranslator translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener);
		translator.processMessage(UNUSED_CHAT, message);
	}
	@Test
	public void notify_bid_details_when_current_price_message_received_from_sniper() {
		context.checking(new Expectations(){{
			exactly(1).of(listener).currentPrice(234, 5, PriceSource.FromSniper);
		}});
		
		Message message = new Message();
		message.setBody(String.format("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: %s;", ApplicationRunner.SNIPER_ID));
		AuctionMessageTranslator translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener);
		translator.processMessage(UNUSED_CHAT, message);
	}
	
}
