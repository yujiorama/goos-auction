package com.github.yujiorama.auctionsinper;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.CoreMatchers.equalTo;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

@SuppressWarnings("unchecked")
public class AuctionSniperDriver extends JFrameDriver {

	public AuctionSniperDriver(long timeoutMillis) {
		super(new GesturePerformer(),
				JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
				new AWTEventQueueProber(timeoutMillis, 100));
	}

	public void showSniperStatus(AuctionStatus auctionStatus) {
		new JTableDriver(this).hasCell(withLabelText(equalTo(auctionStatus.toString())));
	}

	public void showSniperStatus(String itemId, int lastPrice, int lastBid, AuctionStatus auctionStatus) {
		JTableDriver table = new JTableDriver(this);
		table.hasRow(matching(
			withLabelText(itemId),
			withLabelText(String.valueOf(lastPrice)),
			withLabelText(String.valueOf(lastBid)),
			withLabelText(auctionStatus.toString())));
	}
}
