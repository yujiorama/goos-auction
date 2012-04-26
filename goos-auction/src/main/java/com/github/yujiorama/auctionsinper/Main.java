package com.github.yujiorama.auctionsinper;

import javax.swing.SwingUtilities;

public class Main {

	private MainWindow ui;
	
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
	}
}
