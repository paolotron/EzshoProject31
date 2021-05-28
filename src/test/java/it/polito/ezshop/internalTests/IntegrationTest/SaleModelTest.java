package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.model.SaleModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class SaleModelTest {
    final String barCode = "6291041500213";
    final String barCode2 = "6291042500213";

    @Test
    public void TestAddProduct(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t1 = new TicketEntryModel(barCode, "desc", 15, 10);
        TicketEntryModel t2 = new TicketEntryModel(barCode, "desc", 20, 10);
        TicketEntryModel t3 = new TicketEntryModel(barCode, "desc", -1, 10);
        TicketEntryModel t4 = new TicketEntryModel(barCode, "desc", 0, 10);
        assertFalse(sale.addProduct(null));
        assertFalse(sale.addProduct(t4));
        assertFalse(sale.addProduct(t3));
        assertTrue(sale.addProduct(t1));
        assertEquals(15, sale.getProductList().get(0).getAmount());
        assertTrue(sale.addProduct(t2));
        assertEquals(35, sale.getProductList().get(0).getAmount());

        assertEquals(1, sale.getProductList().size());
        assertTrue(sale.removeProduct(barCode,35));
        assertEquals(0, sale.getProductList().size());
        assertFalse(sale.removeProduct(barCode,5));
        assertFalse(sale.removeProduct(barCode,1));
    }
    @Test
    public void TestRemoveProduct(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t = new TicketEntryModel();
        t.setBarCode(barCode);
        t.setAmount(100);
        sale.addProduct(t);
        assertFalse(sale.removeProduct("dummy", 10));
        assertFalse(sale.removeProduct(barCode, 120));
        assertFalse(sale.removeProduct(barCode, 101));
        assertTrue(sale.removeProduct(barCode, 10));
        assertEquals(90, sale.getProductList().get(0).getAmount());
        assertTrue(sale.removeProduct(barCode, 90));
        assertEquals(0, sale.getProductList().size());
    }

    @Test
    public void TestComputeCost(){
        SaleModel sale = new SaleModel();
        TicketEntryModel t1 = new TicketEntryModel(barCode,"dummy",10,2);
        TicketEntryModel t2 = new TicketEntryModel(barCode2,"dummy",10,3);
        sale.addProduct(t1);
        sale.addProduct(t2);
        assertEquals(50, sale.computeCost(), 0.01);
        sale.addProduct(t1);
        assertEquals(70, sale.computeCost(), 0.01);
    }
}
