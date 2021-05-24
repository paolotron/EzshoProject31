package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicketModelTest {

    @Test
    public void testEntryModelTotal(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(0);
        assertEquals(20, entry.computeCost(), 0.01);
    }

    @Test
    public void testEntryModelTotalZero(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(1);
        assertEquals(0, entry.computeCost(), 0.01);
        entry.setAmount(2);
        entry.setPricePerUnit(-10.);
        entry.setDiscountRate(1);
        assertEquals(0, entry.computeCost(), 0.01);
        entry.setAmount(-2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(1);
        assertEquals(0, entry.computeCost(), 0.01);
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(-1);
        assertEquals(0, entry.computeCost(), 0.01);
    }

    @Test
    public void testEntryModelTotalDiscount(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(0.5);
        assertEquals(10, entry.computeCost(), 0.01);
    }

    @Test
    public void testEntryModelAddQuantity(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(10);
        assertTrue(entry.addAmount(2));
        assertEquals(12, entry.getAmount());
        entry.setAmount(5);
        assertFalse(entry.addAmount(-3));
        assertNotEquals(2, entry.getAmount());
    }

    @Test
    public void testEntryModelRemoveQuantity(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(10);
        assertTrue(entry.removeAmount(2));
        assertEquals(8, entry.getAmount());
        entry.setAmount(5);
        assertFalse(entry.removeAmount(6));
        assertNotEquals(-1, entry.getAmount());
        entry.setAmount(5);
        assertFalse(entry.removeAmount(-1));
        assertNotEquals(6, entry.getAmount());
    }
}
