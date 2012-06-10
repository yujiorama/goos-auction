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
		SniperSnapshot joinning = SniperSnapshot.joining("item id");
		SniperSnapshot bidding = joinning.bidding(555, 666);
		context.checking(new Expectations(){{
			allowing(listener).tableChanged(with(anyInsertionEvent()));
			oneOf(listener).tableChanged(with(aChangeInRow(0)));
		}
		});
		model.addSniper(joinning);
		model.sniperStatusChanged(bidding);
		
		assertRowMatchesSnapshot(0, bidding);
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
	
	@Test
	public void holds_snper_in_addtion_order() {
		context.checking(new Expectations(){{
			ignoring(listener);
		}});
		
		model.addSniper(SniperSnapshot.joining("aaa"));
		model.addSniper(SniperSnapshot.joining("bbb"));
		
		assertEquals("aaa", cellValue(0, Column.ITEM_IDENTIFIER));
		assertEquals("bbb", cellValue(1, Column.ITEM_IDENTIFIER));
	}
	
	private Object cellValue(int rowIndex, Column col) {
		return model.getValueAt(rowIndex, col.ordinal());
	}

	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		assertEquals(model.getValueAt(row, Column.ITEM_IDENTIFIER.ordinal()), snapshot.itemId);
		assertEquals(model.getValueAt(row, Column.LAST_PRICE.ordinal()), snapshot.lastPrice);
		assertEquals(model.getValueAt(row, Column.LAST_BID.ordinal()), snapshot.lastBid);
		assertEquals(model.getValueAt(row, Column.SNIPER_STATUS.ordinal()), snapshot.state.toString());
	}
	
	private Matcher<TableModelEvent> anyInsertionEvent() {
		return hasProperty("type", equalTo(TableModelEvent.INSERT));
	}
	
	private Matcher<TableModelEvent> aChangeInRow(int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}
	
	private Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row));
	}
}
