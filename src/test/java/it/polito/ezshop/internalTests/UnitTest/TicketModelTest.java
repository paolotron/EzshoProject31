package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.TicketEntryModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketModelTest {

    @Test
    public void testEntryModelTotal(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(0);
        Assertions.assertEquals(20, entry.computeCost());
    }

    @Test
    public void testEntryModelTotalZero(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(1);
        Assertions.assertEquals(0, entry.computeCost());
        entry.setAmount(2);
        entry.setPricePerUnit(-10.);
        entry.setDiscountRate(1);
        Assertions.assertEquals(0, entry.computeCost());
        entry.setAmount(-2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(1);
        Assertions.assertEquals(0, entry.computeCost());
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(-1);
        Assertions.assertEquals(0, entry.computeCost());
    }

    @Test
    public void testEntryModelTotalDiscount(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(2);
        entry.setPricePerUnit(10.);
        entry.setDiscountRate(0.5);
        Assertions.assertEquals(10, entry.computeCost());
    }

    @Test
    public void testEntryModelAddQuantity(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(10);
        Assertions.assertTrue(entry.addAmount(2));
        Assertions.assertEquals(12, entry.getAmount());
        entry.setAmount(5);
        Assertions.assertFalse(entry.addAmount(-3));
        Assertions.assertNotEquals(2, entry.getAmount());
    }

    @Test
    public void testEntryModelRemoveQuantity(){
        TicketEntryModel entry = new TicketEntryModel();
        entry.setAmount(10);
        Assertions.assertTrue(entry.removeAmount(2));
        Assertions.assertEquals(8, entry.getAmount());
        entry.setAmount(5);
        Assertions.assertFalse(entry.removeAmount(6));
        Assertions.assertNotEquals(-1, entry.getAmount());
        entry.setAmount(5);
        Assertions.assertFalse(entry.removeAmount(-1));
        Assertions.assertNotEquals(6, entry.getAmount());
    }
}
