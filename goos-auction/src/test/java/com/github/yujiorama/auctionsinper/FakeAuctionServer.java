package com.github.yujiorama.auctionsinper;

import static org.hamcrest.CoreMatchers.equalTo;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class FakeAuctionServer {

	public static final String XMPP_HOSTNAME = "localhost";
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
		connection.login(String.format(Main.ITEM_ID_AS_LOGIN, getItemId()), Main.AUCTION_PASSWORD, Main.AUCTION_RESOURCE);
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

	public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
		messageListener.receiveAMessage(equalTo(Main.XMPP_COMMAND_JOIN));
		org.hamcrest.MatcherAssert.assertThat(currentChat.getParticipant(), equalTo(sniperId));
	}

	public void reportPrice(int currentPrice, int increment, String sniperId) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void hasReceivedBid(int biddedPrice, String sniperXmppId) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
}
