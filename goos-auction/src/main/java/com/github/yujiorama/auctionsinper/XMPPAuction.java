package com.github.yujiorama.auctionsinper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {

	private final Chat chat;

	public XMPPAuction(Chat chat) {
		this.chat = chat;
	}
	
	@Override
	public void bid(int amount) {
		try {
			chat.sendMessage(String.format(Main.XMPP_COMMAND_BID, amount));
		} catch (XMPPException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void join() {
		try {
			chat.sendMessage(Main.XMPP_COMMAND_JOIN);
		} catch (XMPPException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
