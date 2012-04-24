package com.github.yujiorama.auctionsinper;

import static org.hamcrest.CoreMatchers.equalTo;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class AuctionSniperDriver extends JFrameDriver {

	public AuctionSniperDriver(long timeoutMillis) {
		super(new GesturePerformer(),
				JFrameDriver.topLevelFrame(named(Main.MAIN_WINDOW_NAME), showingOnScreen()),
				new AWTEventQueueProber(timeoutMillis, 100));
	}

	public void showSniperStatus(AuctionStatus auctionStatus) {
		new JLabelDriver(this, named(Main.MAIN_WINDOW_NAME)).hasText(equalTo(auctionStatus.toString()));
	}
}