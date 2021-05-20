package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.OrderModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderModelTest {
    @Test
    void testConstructor(){
        OrderModel o = new OrderModel("6291041500213", 2,2.0);
        Assertions.assertTrue(o.getOrderId() > 0, "orderId should be > 0");
    }

    @Test
    void testGetTotalPrice(){
        OrderModel o = new OrderModel("6291041500213", 3,2.0);
        Assertions.assertEquals(3*2.0, o.getTotalPrice());
    }

}
