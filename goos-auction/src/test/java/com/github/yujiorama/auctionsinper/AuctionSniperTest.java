package com.github.yujiorama.auctionsinper;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.yujiorama.auctionsinper.AuctionEventListener.PriceSource;
import com.github.yujiorama.auctionsinper.AuctionSniper.SniperState;

@RunWith(JMock.class)
public class AuctionSniperTest {

	private static final String ANY_ITEM_ID = "ANY_ITEM_ID";
	private static final Auction NULL_AUCTION = null;
	private Mockery context = new Mockery();

	@Test
	public void report_lost_when_auction_closed_immediately() {
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			oneOf(sniperListener).sniperLost();
		}});
		AuctionSniper sniper = new AuctionSniper(ANY_ITEM_ID, NULL_AUCTION, sniperListener);
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
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ANY_ITEM_ID, price, price + increment));
		}});
		AuctionSniper sniper = new AuctionSniper(ANY_ITEM_ID, auction, sniperListener);
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test
	public void report_iswinning_when_current_price_comes_from_sniper() {
		final States sniperState = context.states("sniper state");
		final Auction auction = context.mock(Auction.class);
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(
				with(aSniperThatIs(SniperState.BIDDING)));
					then(sniperState.is(AuctionStatus.BIDDING.toString()));
			atLeast(1).of(sniperListener).sniperStateChanged(
				new SniperSnapshot(ANY_ITEM_ID, 135, 135, SniperState.WINNING));
				with(sniperState.is(AuctionStatus.WINNING.toString()));
		}});
		AuctionSniper sniper = new AuctionSniper(ANY_ITEM_ID, auction, sniperListener);
		sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
	}
	
	@Test
	public void report_lost_when_auction_closed_when_bidding() {
		final States sniperState = context.states("sniper state");
		final Auction auction = context.mock(Auction.class);
		final SniperListener sniperListener = context.mock(SniperListener.class);
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
				then(sniperState.is(AuctionStatus.BIDDING.toString()));
			oneOf(sniperListener).sniperLost();
				when(sniperState.is(AuctionStatus.BIDDING.toString()));
		}});

		AuctionSniper sniper = new AuctionSniper(ANY_ITEM_ID, auction, sniperListener);
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
			atLeast(1).of(sniperListener).sniperStateChanged(
				new SniperSnapshot(ANY_ITEM_ID, 123, 0, SniperState.WINNING));
				then(sniperState.is(AuctionStatus.WINNING.toString()));
			oneOf(sniperListener).sniperWon();
				when(sniperState.is(AuctionStatus.WINNING.toString()));
		}});
		AuctionSniper sniper = new AuctionSniper(ANY_ITEM_ID, auction, sniperListener);
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
			atLeast(2).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));//new SniperSnapshot(ANY_ITEM_ID, 678, 768, SniperState.BIDDING));
				then(sniperState.is(AuctionStatus.BIDDING.toString()));
			atLeast(2).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WINNING)));//new SniperSnapshot(ANY_ITEM_ID, 778, 768, SniperState.WINNING));
				then(sniperState.is(AuctionStatus.WINNING.toString()));
			atLeast(1).of(sniperListener).sniperWon();
				when(sniperState.is(AuctionStatus.WINNING.toString()));
		}});
		AuctionSniper sniper = new AuctionSniper(ANY_ITEM_ID, auction, sniperListener);
		sniper.currentPrice(678, 90, PriceSource.FromOtherBidder);
		sniper.currentPrice(768, 10, PriceSource.FromSniper);
		sniper.currentPrice(778, 10, PriceSource.FromOtherBidder);
		sniper.currentPrice(788, 10, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

	private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(
				equalTo(state), "sniper that is ", "was") {
			@Override
			protected SniperState featureValueOf(SniperSnapshot actual) {
				return actual.state;
			}
		};
	}
}
