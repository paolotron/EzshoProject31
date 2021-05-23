package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.model.*;

import org.junit.Before;
import org.junit.Test;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BalanceTest {
/*    BalanceModel b = new BalanceModel();
    BalanceOperationModel credit;
    BalanceOperationModel debit;
    SaleTransactionModel sale;*/
    String pC1 = "0123456789";
    String pC2 = "0123456189";
    OrderModel om1;
    OrderModel om2;
    ArrayList<TicketEntryModel> productList = new ArrayList<>();
    SaleModel sm1 = new SaleModel();
    SaleModel sm2 = new SaleModel();
    HashMap<String, ProductTypeModel> map = new HashMap<>();


    @Before
    public void startBalance(){
        om1 = new OrderModel(pC1, 100, 1.);
        om1.setStatus("ISSUED");
        om2 = new OrderModel(pC2, 500, 2.);
        om2.setStatus("ISSUED");
        productList.add(new TicketEntryModel(pC1, "desc", 10, 5.));
        productList.add(new TicketEntryModel(pC2, "desc", 10, 9.));
        sm1.setProductList(productList);
        sm2.setProductList(productList);
        map.put(pC1, new ProductTypeModel(1,"desc",pC1, 5., "note"));
        map.put(pC2, new ProductTypeModel(2,"desc",pC2, 9., "note"));
    }

/*
    @Test
    public void getTransactionByIdTest(){
        Optional<BalanceOperationModel> result = b.getTransactionById(credit.getBalanceId());
        Assert.assertEquals(credit, result.orElse(null));
        result = b.getTransactionById(debit.getBalanceId());
        Assert.assertEquals(debit, result.orElse(null));
        result = b.getTransactionById(sale.getBalanceId());
        Assert.assertEquals(sale, result.orElse(null));
        result = b.getTransactionById(100);
        Assertions.assertNull(result.orElse(null));
        result = b.getTransactionById(-10);
        Assertions.assertNull(result.orElse(null));
    }
*/

    @Test
    public void test(){
        OrderTransactionModel otm1 = new OrderTransactionModel(om1);
        OrderTransactionModel otm2 = new OrderTransactionModel(om2);

        SaleTransactionModel stm1 = new SaleTransactionModel(sm1);
        SaleTransactionModel stm2 = new SaleTransactionModel(sm2);

        BalanceModel b = new BalanceModel();
        b.addOrderTransaction(otm1);
        b.addOrderTransaction(otm2);
        b.addSaleTransactionModel(stm1.getBalanceId(),stm1);
        b.addSaleTransactionModel(stm2.getBalanceId(), stm2);


        assertEquals(2, b.getOrderTransactionMap().size(), 0);
        assertEquals(2, b.getSaleTransactionMap().size());
        assertEquals(4, b.getAllBalanceOperations().size());
        assertEquals(otm1, b.getOrderTransactionById(otm1.getBalanceId()));
        assertEquals(stm2, b.getSaleTransactionById(stm2.getBalanceId()));

        assertEquals(-1100, b.computeBalance(), 0.1);
        stm1.setTicketPayment(new CashPaymentModel(stm1.computeCost(), false, 150));
        stm2.setTicketPayment(new CashPaymentModel(stm1.computeCost(), false, 140));
        assertEquals(-1100+280, b.computeBalance(), 0.01);

        ReturnModel rm1 = new ReturnModel(stm1);
        ReturnModel rm2 = new ReturnModel(stm2);
        rm1.getProductList().add(new TicketEntryModel(pC1, "desc", 5, 5.));
        rm2.getProductList().add(new TicketEntryModel(pC2, "desc", 3, 9.));

        rm1.commit(map);
        ReturnTransactionModel rtm1 = new ReturnTransactionModel(rm1);
        b.addReturnTransactionModel(rtm1.getBalanceId(), rtm1);
        rtm1.setPayment(new CashPaymentModel(rtm1.getMoney(), true, rtm1.getMoney()));
        assertEquals(-1100+280-25, b.computeBalance(), 0.01);

        rm2.commit(map);
        ReturnTransactionModel rtm2 = new ReturnTransactionModel(rm2);
        b.addReturnTransactionModel(rtm2.getBalanceId(), rtm2);
        rtm2.setPayment(new CashPaymentModel(rtm2.getMoney(), true, rtm2.getMoney()));
        assertEquals(-1100+280-25-27-25, b.computeBalance(), 0.01);

    }



}
