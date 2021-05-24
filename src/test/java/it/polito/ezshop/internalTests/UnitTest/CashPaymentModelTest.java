package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.CashPaymentModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CashPaymentModelTest {
    @Test
    public void testComputeChange(){
        CashPaymentModel cashP = new CashPaymentModel();
        cashP.setCash(330.25);
        cashP.setAmount(230.10);
        assertEquals(cashP.computeChange(),100.15, 0.01);
        cashP.setCash(-10.70);
        cashP.setAmount(10.30);
        assertEquals(cashP.computeChange(), -1, 0.01);
        cashP.setCash(30.20);
        cashP.setAmount(-10.50);
        assertEquals(cashP.computeChange(), -1, 0.01);

    }
}
