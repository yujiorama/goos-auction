package com.github.yujiorama.auctionsinper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Main {

	public static final String XMPP_COMMAND_JOIN = "SOLVersion: 1.1; Command: JOIN;";
	public static final String XMPP_COMMAND_BID = "SOLVersion: 1.1; Command: BID; Price: %s;";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_PASSWORD = "auction";
	public static final String AUCTION_RESOURCE = "Auction";

	private static final String AUCITON_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/%s";

	private MainWindow ui;
	@SuppressWarnings("unused") private Chat notToBeGCd;
	
	public Main() throws Exception {
		startUserInterface();
	}
	
	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(
				new Runnable() {

					@Override
					public void run() {
						ui = new MainWindow();
					}
				}
			);
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String ... args) throws Exception {
		Main main = new Main();
		main.joinAuction(connection(args[0], args[1], args[2]), args[3]);
	}

	private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
		disconnectWhenUIClosed(connection);
		final Chat aChat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
		
		this.notToBeGCd = aChat;
		Auction auction = new XMPPAuction(aChat);
		aChat.addMessageListener(
			new AuctionMessageTranslator(
				connection.getUser(), new AuctionSniper(itemId, auction, new SniperStateDisplayer())));
		auction.join();
	}

	private void disconnectWhenUIClosed(final XMPPConnection connection) {
		ui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				connection.disconnect();
				super.windowClosed(e);
			}
		});
	}

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCITON_ID_FORMAT, itemId, connection.getServiceName(), AUCTION_RESOURCE);
	}

	private static XMPPConnection connection(String serviceName, String userName, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(serviceName);
		connection.connect();
		connection.login(userName, password, AUCTION_RESOURCE);
		return connection;
	}

	public class SniperStateDisplayer implements SniperListener {

		@Override
		public void sniperLost() {
			showStatus(AuctionStatus.LOST);
		}
	
		@Override
		public void sniperWon() {
			showStatus(AuctionStatus.WON);
		}
		
		@Override
		public void sniperStateChanged(final SniperSnapshot state) {
			SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						ui.sniperStatusChanged(state);
					}
				}
			);
		}
		
		@Override
		public void sniperWinning() {
			showStatus(AuctionStatus.WINNING);
		}
		
		private void showStatus(final AuctionStatus auctionStatus) {
			SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						ui.showStatus(auctionStatus);
					}
				}
			);
		}
	}
}
