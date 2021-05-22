package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.OrderModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class OrderModelTest {
    @Test
    public void testConstructor(){
        OrderModel o = new OrderModel("6291041500213", 2,2.0);
        assertTrue("orderId should be > 0", o.getOrderId() > 0);
    }

    @Test
    public void testGetTotalPrice(){
        OrderModel o = new OrderModel("6291041500213", 3,2.0);
        assertEquals(3*2.0, o.getTotalPrice(), 0.01);
    }

}
