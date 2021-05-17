package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.CashPayment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class CashPaymentTest {
    @Test
    void testComputeChange(){
        CashPayment cashP = new CashPayment();
        cashP.setCash(330);
        cashP.setAmount(230);
        Assertions.assertEquals(cashP.computeChange(),100.);
        cashP.setCash(10);
        cashP.setAmount(30);
        Assertions.assertEquals(cashP.computeChange(), -1);
        cashP.setCash(-10);
        cashP.setAmount(10);
        Assertions.assertEquals(cashP.computeChange(), -1);
        cashP.setCash(30);
        cashP.setAmount(-10);
        Assertions.assertEquals(cashP.computeChange(), -1);

    }
}
