package it.polito.ezshop.internalTests.ScenarioTest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.*;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiTests {
    EZShopInterface model;
    String username = "Paolo";
    String password = "pass";
    String barcode = "6291041500213";
    String creditCard = "5265807692";

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    @BeforeClass
    public static void CreditCardFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;1000");
        writer.close();
    }

    @AfterClass
    public static void cleanupFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.close();
    }

    @Before
    public void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException, InvalidLocationException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        login();
        Integer id = model.createProductType("test product", barcode , 10, "");
        model.updatePosition(id, "23-ABC-2");
        model.updateQuantity(id, 10);
        login();
        model.logout();
    }

    @Test
    public void testCompleteTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException {
        login();
        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, "6291041500213", 2));
        assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        assertTrue(model.endSaleTransaction(id));
        assertEquals(0.,model.computeBalance(), 0.01);
        assertTrue(model.receiveCreditCardPayment(id, creditCard));
        assertEquals(20,model.computeBalance(), 0.01);
        assertEquals(2, model.computePointsForSale(id));
    }

    @Test
    public void testCompleteReturn() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException {
        login();
        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, barcode, 2));
        assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        model.endSaleTransaction(id);
        assertEquals(0.,model.computeBalance(), 0.01);
        assertTrue(model.receiveCreditCardPayment(id, creditCard));
        assertEquals(20, model.computeBalance(), 0.01);
        Integer rId = model.startReturnTransaction(id);
        model.returnProduct(rId, barcode, 1);
        assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        assertTrue(model.endReturnTransaction(rId, true));
        assertEquals(9, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        assertEquals(10, model.returnCreditCardPayment(rId, creditCard), 0.01);
        assertEquals(10, model.computeBalance(), 0.01);
    }

    @Test
    public void testCompleteOrder() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidOrderIdException {
        login();
        int id = model.issueOrder(barcode, 2, 2.);
        assertEquals(id, -1);
        model.recordBalanceUpdate(100);
        id = model.issueOrder(barcode, 2, 20.);
        assertEquals("ISSUED", model.getAllOrders().get(0).getStatus());
        assertTrue(model.payOrder(id));
        assertEquals("PAYED", model.getAllOrders().get(0).getStatus());
        assertTrue(model.recordOrderArrival(id));
        assertEquals("COMPLETED", model.getAllOrders().get(0).getStatus());
        assertEquals(60, model.computeBalance(), 0.01);
        id = model.payOrderFor(barcode, 2, 10);
        assertEquals("PAYED", model.getAllOrders().get(1).getStatus());
        assertTrue(model.recordOrderArrival(id));
        assertEquals(model.computeBalance(), 40, 0.01);
        assertEquals("COMPLETED", model.getAllOrders().get(1).getStatus());
    }

    @After
    public void cleanup(){
        model.reset();
    }
}
