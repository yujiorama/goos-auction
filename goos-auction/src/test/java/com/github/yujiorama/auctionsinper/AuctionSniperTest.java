package com.github.yujiorama.auctionsinper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.yujiorama.auctionsinper.AuctionEventListener.PriceSource;

@RunWith(JMock.class)
public class AuctionSniperTest {

	private Mockery context = new Mockery();

	@Test
	public void report_lost_when_auction_closed_immediately() {
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			oneOf(sniperListener).sniperLost();
		}});
		AuctionSniper sniper = new AuctionSniper(sniperListener);
		sniper.auctionClosed();
	}
	
	@Test
	public void bids_higher_and_reports_bidding_when_new_price_arrives() {
		final Auction auction = context.mock(Auction.class);
		final SniperListener sniperListener = context.mock(SniperListener.class);
		final int price = 1001;
		final int increment = 25;
		context.checking(new Expectations(){{
			oneOf(auction).bid(price + increment);
			atLeast(1).of(sniperListener).sniperBidding(new SniperState(Main.ITEM_ID_AS_LOGIN, price, price + increment));
		}});
		AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test
	public void report_iswinning_when_current_price_comes_from_sniper() {
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			atLeast(1).of(sniperListener).sniperWinning();
		}});
		AuctionSniper sniper = new AuctionSniper(sniperListener);
		sniper.currentPrice(200, 10, PriceSource.FromSniper);
	}
	
	@Test
	public void report_lost_when_auction_closed_when_bidding() {
		final States sniperState = context.states("sniper state");
		final Auction auction = context.mock(Auction.class);
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperBidding(null);
				then(sniperState.is(AuctionStatus.BIDDING.toString()));
			oneOf(sniperListener).sniperLost();
				when(sniperState.is(AuctionStatus.BIDDING.toString()));
		}});
		AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	@Test
	public void report_won_when_auction_closed_when_winning() {
		final States sniperState = context.states("sniper state");
		final Auction auction = context.mock(Auction.class);
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperWinning();
				then(sniperState.is(AuctionStatus.WINNING.toString()));
			oneOf(sniperListener).sniperWon();
				when(sniperState.is(AuctionStatus.WINNING.toString()));
		}});
		AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}
	
	@Test
	public void report_won_when_auction_closed_when_any_state() {
		final States sniperState = context.states("sniper state");
		final Auction auction = context.mock(Auction.class);
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperBidding(null);
				then(sniperState.is(AuctionStatus.BIDDING.toString()));
			allowing(sniperListener).sniperWinning();
				then(sniperState.is(AuctionStatus.WINNING.toString()));
			allowing(sniperListener).sniperBidding(null);
				then(sniperState.is(AuctionStatus.BIDDING.toString()));
			allowing(sniperListener).sniperWinning();
				then(sniperState.is(AuctionStatus.WINNING.toString()));
			atLeast(1).of(sniperListener).sniperWon();
				when(sniperState.is(AuctionStatus.WINNING.toString()));
		}});
		AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
		sniper.currentPrice(678, 90, PriceSource.FromOtherBidder);
		sniper.currentPrice(768, 10, PriceSource.FromSniper);
		sniper.currentPrice(778, 10, PriceSource.FromOtherBidder);
		sniper.currentPrice(788, 10, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

}
