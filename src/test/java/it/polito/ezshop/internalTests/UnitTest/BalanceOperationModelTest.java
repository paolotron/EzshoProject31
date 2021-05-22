package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BalanceOperationModelTest {

    @Test
    public void isReturnTest(){
        SaleTransactionModel s = new SaleTransactionModel(new SaleModel());
        assertFalse(s.isReturn());
        ReturnTransactionModel r = new ReturnTransactionModel(new ReturnModel(s));
        assertTrue(r.isReturn());
        OrderTransactionModel o = new OrderTransactionModel(new OrderModel(), LocalDate.now());
        assertFalse(o.isReturn());
    }
}
