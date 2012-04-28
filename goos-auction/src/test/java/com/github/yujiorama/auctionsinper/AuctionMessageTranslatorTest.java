package com.github.yujiorama.auctionsinper;


import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {

	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);

	@Test
	public void notify_auction_closed_when_close_message_received() {
		context.checking(new Expectations(){{
			oneOf(listener).auctionClosed();
		}});
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		AuctionMessageTranslator translator = new AuctionMessageTranslator(listener);
		translator.processMessage(null, message);
	}

	@Test
	public void notify_bid_details_when_current_price_message_received() {
		context.checking(new Expectations(){{
			exactly(1).of(listener).currentPrice(192, 7);
		}});
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		AuctionMessageTranslator translator = new AuctionMessageTranslator(listener);
		translator.processMessage(null, message);
	}
}
