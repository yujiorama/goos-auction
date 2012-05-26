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
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}
	
	@Test
	public void setsUpColumnHeadings() {
		for (Column column : Column.values()) {
			assertEquals(column.label, model.getColumnName(column.ordinal()));
		}
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
	
	@Test
	public void notify_to_listeners_when_adding_sniper() {
		SniperSnapshot joining = SniperSnapshot.joining("item-77789");
		context.checking(new Expectations(){{
			oneOf(listener).tableChanged(with(anInsertionAtRow(0)));
		}});
		
		assertEquals(0, model.getRowCount());
		model.addSniper(joining);
		assertEquals(1, model.getRowCount());
		assertRowMatchesSnapshot(0, joining);
	}

	private <T> void assertEqualsOf(Column column, T expected) {
		int rowIndex = 0;
		int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}

	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		assertEquals(model.getValueAt(row, Column.ITEM_IDENTIFIER.ordinal()), snapshot.itemId);
		assertEquals(model.getValueAt(row, Column.LAST_PRICE.ordinal()), snapshot.lastPrice);
		assertEquals(model.getValueAt(row, Column.LAST_BID.ordinal()), snapshot.lastBid);
		assertEquals(model.getValueAt(row, Column.SNIPER_STATUS.ordinal()), snapshot.state);
	}
	
	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}
	
	private Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row));
	}
}
