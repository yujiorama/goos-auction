package com.github.yujiorama.auctionsinper;


import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class AuctionMessageTranslatorTest {

	@Test
	public void notify_auction_closed_when_close_message_received() {
		Mockery context = new Mockery();
		Expectations expectations = new Expectations();
		AuctionEventListener listener = context.mock(AuctionEventListener.class);
		expectations.oneOf(listener).auctionClosed();
		context.checking(expectations);
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		AuctionMessageTranslator translator = new AuctionMessageTranslator(listener);
		translator.processMessage(null, message);
	}

}
