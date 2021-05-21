package it.polito.ezshop.internalTests.APITest;

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
        TicketEntryModel t1 = new TicketEntryModel(barCode, "desc", 15, 10);
        TicketEntryModel t2 = new TicketEntryModel(barCode, "desc", 20, 10);
        TicketEntryModel t3 = new TicketEntryModel(barCode, "desc", -1, 10);
        TicketEntryModel t4 = new TicketEntryModel(barCode, "desc", 0, 10);
        Assertions.assertFalse(sale.addProduct(null));
        Assertions.assertFalse(sale.addProduct(t4));
        Assertions.assertFalse(sale.addProduct(t3));
        Assertions.assertTrue(sale.addProduct(t1));
        Assertions.assertEquals(15, sale.getProductList().get(0).getAmount());
        Assertions.assertTrue(sale.addProduct(t2));
        Assertions.assertEquals(35, sale.getProductList().get(0).getAmount());

        Assertions.assertEquals(1, sale.getProductList().size());
        Assertions.assertTrue(sale.removeProduct(barCode,35));
        Assertions.assertEquals(0, sale.getProductList().size());
        Assertions.assertFalse(sale.removeProduct(barCode,5));
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
        Assertions.assertEquals(90, sale.getProductList().get(0).getAmount());
        Assertions.assertTrue(sale.removeProduct(barCode, 90));
        Assertions.assertEquals(0, sale.getProductList().size());
    }

    @Test
    void TestComputeCost(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t1 = new TicketEntryModel(barCode,"dummy",10,2);
        TicketEntryModel t2 = new TicketEntryModel(barCode2,"dummy",10,3);
        sale.addProduct(t1);
        sale.addProduct(t2);
        Assertions.assertEquals(50, sale.computeCost());
        sale.addProduct(t1);
        Assertions.assertEquals(70, sale.computeCost());


    }
}
