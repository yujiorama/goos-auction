package com.github.yujiorama.auctionsinper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class FakeAuctionServer {

	public static final String XMPP_HOSTNAME = "localhost";
	private static final String ITEM_ID_AS_LOGIN = "auction-%d";
	private static final String AUCTION_PASSWORD = "auction";
	private static final String AUCTION_RESOURCE = "Auction";
	private XMPPConnection connection;
	private Chat currentChat;
	private String itemId;
	private SingleMessageListener messageListener = new SingleMessageListener();
	
	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}
	
	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(String.format(ITEM_ID_AS_LOGIN, getItemId()), AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(
			new ChatManagerListener() {

				@Override
				public void chatCreated(Chat chat, boolean createdLocally) {
					currentChat = chat;
					chat.addMessageListener(messageListener);
				}
			});
	}
	
	public String getItemId() {
		return this.itemId;
	}
	
	public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
		messageListener.receiveAMessage();
	}
	
	public void announceClosed() throws XMPPException {
		currentChat.sendMessage(new Message());
	}
	
	public void stop() {
		connection.disconnect();
	}
	
	public class SingleMessageListener implements MessageListener {

		private ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

		@Override
		public void processMessage(Chat chat, Message message) {
			messages.add(message);
		}

		public void receiveAMessage() throws InterruptedException {
			assertThat("Message", messages.poll(5, TimeUnit.SECONDS), is(notNullValue()));
		}

	}
}
