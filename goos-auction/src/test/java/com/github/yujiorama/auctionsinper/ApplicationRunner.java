package com.github.yujiorama.auctionsinper;

public class ApplicationRunner {
	
	private static final String SNIPER_PASSWORD = "sniper";
	private static final String SNIPER_ID = "sniper";
	private static final boolean on = true;
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer auction) {
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
			}
		};
		thread.setDaemon(on);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.showSniperStatus(AuctionStatus.JOINING);
	}
	
	public void showsSniperHasLostAuction() {
		driver.showSniperStatus(AuctionStatus.LOST);
	}
	
	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
