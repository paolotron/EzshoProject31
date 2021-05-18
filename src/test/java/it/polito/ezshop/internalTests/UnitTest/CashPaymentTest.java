package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.CashPayment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CashPaymentTest {
    @Test
    void testComputeChange(){
        CashPayment cashP = new CashPayment();
        cashP.setCash(330.25);
        cashP.setAmount(230.10);
        Assertions.assertEquals(cashP.computeChange(),100.15);
        cashP.setCash(-10.70);
        cashP.setAmount(10.30);
        Assertions.assertEquals(cashP.computeChange(), -1);
        cashP.setCash(30.20);
        cashP.setAmount(-10.50);
        Assertions.assertEquals(cashP.computeChange(), -1);

    }
}
