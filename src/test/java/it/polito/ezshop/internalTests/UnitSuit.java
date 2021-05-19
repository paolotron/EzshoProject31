package it.polito.ezshop.internalTests;

import it.polito.ezshop.internalTests.UnitTest.*;
import it.polito.ezshop.model.*;
import org.junit.runners.Suite;


@Suite.SuiteClasses({
        CashPaymentModelTest.class,
        EzShopModelTest.class,
        OrderModelTest.class,
        OrderTransactionModel.class,
        CreditCardPaymentTest.class,
        ProductTypeModel.class,
        ReturnModelTest.class,
        ReturnTransactionModel.class,
        SaleTransactionModel.class,
        TicketModelTest.class,
        UserModelTest.class
})

public class UnitSuit {
}
