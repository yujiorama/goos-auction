package com.github.yujiorama.auctionsinper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.github.yujiorama.auctionsinper.AuctionEventListener.PriceSource;

public class AuctionMessageTranslator implements MessageListener {

	private String bidder;
	private AuctionEventListener listener;
	
	public AuctionMessageTranslator(String bidder, AuctionEventListener listener) {
		this.bidder = bidder;
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		AuctionEvent event = AuctionEvent.from(message.getBody());
		String type = event.type();
		if ("CLOSE".equals(type)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(type)) {
			listener.currentPrice(event.currentPrice() , event.increment(), priceSourceFrom(event));
		}
	}

	private PriceSource priceSourceFrom(AuctionEvent event) {
		if (bidder.equals(event.bidder())) {
			return PriceSource.FromSniper;
		} else {
			return PriceSource.FromOtherBidder;
		}
	}
}
