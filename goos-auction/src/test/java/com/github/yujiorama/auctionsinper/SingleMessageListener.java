package com.github.yujiorama.auctionsinper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class SingleMessageListener implements MessageListener {

	private ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

	@Override
	public void processMessage(Chat chat, Message message) {
		messages.add(message);
	}

	public void receiveAMessage() throws InterruptedException {
		assertThat("Message", messages.poll(5, TimeUnit.SECONDS), is(notNullValue()));
	}
	
	public void receiveAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
		Message message = messages.poll(5, TimeUnit.SECONDS);
		assertThat("Message", message, is(notNullValue()));
		assertThat(message.getBody(), messageMatcher);
	}

}
