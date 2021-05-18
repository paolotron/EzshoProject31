package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BalanceOperationModelTest {

    @Test
    public void isReturnTest(){
        SaleTransactionModel s = new SaleTransactionModel(new SaleModel());
        Assertions.assertFalse(s.isReturn());
        ReturnTransactionModel r = new ReturnTransactionModel(new ReturnModel(s));
        Assertions.assertTrue(r.isReturn());
        OrderTransactionModel o = new OrderTransactionModel(new OrderModel(), LocalDate.now());
        Assertions.assertFalse(o.isReturn());
    }
}
