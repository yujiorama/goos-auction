package com.github.yujiorama.auctionsinper;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.github.yujiorama.auctionsinper.AuctionSniper.SniperState;
import com.github.yujiorama.auctionsinper.SniperTableModel.Column;

public class SniperTableModelTest {
	private final Mockery context = new Mockery();
	private SniperTableModel model = new SniperTableModel();
	private TableModelListener listener = context.mock(TableModelListener.class);
	
	@Before
	public void attachListenerToModel() {
		model.addTableModelListener(listener);
	}
	
	@Test
	public void hasEnoughColumns() {
		assertThat(model.getColumnCount(), equalTo(SniperTableModel.Column.values().length));
	}
	
	@Test
	public void set_sniper_values_in_columns() {
		context.checking(new Expectations(){{
			oneOf(listener).tableChanged(with(aRowChangedEvent()));
		}});
		model.sniperStatusChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));
		
		assertEqualsOf(Column.ITEM_IDENTIFIER, "item id");
		assertEqualsOf(Column.LAST_PRICE, 555);
		assertEqualsOf(Column.LAST_BID, 666);
		assertEqualsOf(Column.SNIPER_STATUS, AuctionStatus.BIDDING.toString());
	}

	private <T> void assertEqualsOf(Column column, T expected) {
		int rowIndex = 0;
		int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}

	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}
}
