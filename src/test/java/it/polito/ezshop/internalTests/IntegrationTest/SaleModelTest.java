package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.model.SaleModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SaleModelTest {
    final String barCode = "6291041500213";
    final String barCode2 = "6291042500213";

    @Test
    void TestAddProduct(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t = new TicketEntryModel();
        t.setBarCode(barCode);
        Assertions.assertFalse(sale.addProduct(null));
        t.setAmount(0);
        Assertions.assertFalse(sale.addProduct(t));
        t.setAmount(-1);
        Assertions.assertFalse(sale.addProduct(t));
        t.setAmount(15);
        Assertions.assertTrue(sale.addProduct(t));
        Assertions.assertEquals(15, sale.getProductList().get(0).getAmount());
        t.setAmount(20);
        Assertions.assertTrue(sale.addProduct(t));
        Assertions.assertEquals(35, sale.getProductList().get(0).getAmount());

        //TODO myTest -> cancellare
        Assertions.assertTrue(sale.removeProduct(barCode,35));
        Assertions.assertTrue(sale.removeProduct(barCode,5));
        Assertions.assertFalse(sale.removeProduct(barCode,1));
    }
    @Test
    void TestRemoveProduct(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t = new TicketEntryModel();
        t.setBarCode(barCode);
        t.setAmount(100);
        sale.addProduct(t);
        Assertions.assertFalse(sale.removeProduct("dummy", 10));
        Assertions.assertFalse(sale.removeProduct(barCode, 120));
        Assertions.assertFalse(sale.removeProduct(barCode, 101));
        Assertions.assertTrue(sale.removeProduct(barCode, 10));
        Assertions.assertTrue(sale.removeProduct(barCode, 90));
        Assertions.assertFalse(sale.removeProduct(barCode, 1));
    }

    @Test
    void TestComputeCost(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t1 = new TicketEntryModel(barCode,"dummy",10,2);
        TicketEntryModel t2 = new TicketEntryModel(barCode2,"dummy",10,3);
        sale.addProduct(t1);
        sale.addProduct(t2);
        Assertions.assertEquals(sale.computeCost(), 50);
        sale.addProduct(t1);
        Assertions.assertEquals(sale.computeCost(), 70);


    }
}
