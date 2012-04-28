package com.github.yujiorama.auctionsinper;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {

	private AuctionEventListener listener;
	
	public AuctionMessageTranslator(AuctionEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		Map<String, String> event = unpackEventFrom(message);
		String type = event.get("Event");
		if ("CLOSE".equals(type)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(type)) {
			int currentPrice = Integer.parseInt(event.get("CurrentPrice"));
			int increment = Integer.parseInt(event.get("Increment"));
			listener.currentPrice(currentPrice , increment );
		}
	}

	private Map<String, String> unpackEventFrom(Message message) {
		HashMap<String, String> event = new HashMap<String, String>();
		for (String kv: message.getBody().split(";")) {
			String[] ar = kv.split(":");
			event.put(ar[0].trim(), ar[1].trim());
		}
		return event;
	}

}
