package com.github.yujiorama.auctionsinper;

public class ApplicationRunner {
	
	public static final String SNIPER_XMPP_ID;
	public static final String SNIPER_ID = "sniper";
	
	private static final String SNIPER_PASSWORD = "sniper";
	private static final boolean on = true;
	private AuctionSniperDriver driver;
	private String itemId;

	static {
		SNIPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionServer.XMPP_HOSTNAME + "/" + Main.AUCTION_RESOURCE;
	}
	
	public void startBiddingIn(final FakeAuctionServer... auctions) {
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				try {
					Main.main(parseAuctions(auctions));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		thread.setDaemon(on);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.APPLICATION_NAME);
		driver.hasColumnTitles();
		driver.showSniperStatus(AuctionStatus.JOINING);
		itemId = "";
	}
	
	protected static String[] parseAuctions(FakeAuctionServer[] auctions) {
		String[] result = new String[auctions.length + 3];
		result[0] = FakeAuctionServer.XMPP_HOSTNAME;
		result[1] = SNIPER_ID;
		result[2] = SNIPER_PASSWORD;
		for (int i = 0; i < auctions.length; i++) {
			result[i+3] = auctions[i].getItemId();
		}
		return result;
	}
	
	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
	
	public void showsSniperHasLostAuction() {
		driver.showSniperStatus(AuctionStatus.LOST);
	}

	public void showsSniperHasWonAuction() {
		driver.showSniperStatus(AuctionStatus.WON);
	}
	
	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, AuctionStatus.BIDDING);
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showSniperStatus(auction.getItemId(), winningBid, winningBid, AuctionStatus.WINNING);
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
		driver.showSniperStatus(auction.getItemId(), lastPrice, lastPrice, AuctionStatus.WON);
	}
}
