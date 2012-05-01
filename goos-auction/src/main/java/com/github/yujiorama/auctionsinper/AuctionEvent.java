package com.github.yujiorama.auctionsinper;

import java.util.HashMap;
import java.util.Map;

public class AuctionEvent {

	private static final String FIELD_KEY_VALUE_SEPARATOR = ":";
	private static final String MESSAGE_FIELD_SEPARATOR = ";";
	public static AuctionEvent from(String messageBody) {
		AuctionEvent event = new AuctionEvent();
		for (String field: fieldsIn(messageBody)) {
			event.addField(field);
		}
		return event;
	}

	private static String[] fieldsIn(String messageBody) {
		return messageBody.split(MESSAGE_FIELD_SEPARATOR);
	}

	private Map<String, String> fields = new HashMap<String, String>();
	
	private void addField(String field) {
		String[] ar = field.split(FIELD_KEY_VALUE_SEPARATOR);
		fields.put(ar[0].trim(), ar[1].trim());
	}

	public String type() {
		return fields.get("Event");
	}

	public int currentPrice() {
		return Integer.parseInt(fields.get("CurrentPrice"));
	}

	public int increment() {
		return Integer.parseInt(fields.get("Increment"));
	}

	public String bidder() {
		return fields.get("Bidder");
	}

}
