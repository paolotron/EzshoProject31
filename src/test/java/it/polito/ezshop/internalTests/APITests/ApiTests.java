package it.polito.ezshop.internalTests.APITests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ApiTests {
    EZShopInterface model;
    String username = "Paolo";
    String password = "pass";
    String barcode = "6291041500213";
    String creditCard = "5265807692";

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    @BeforeAll
    static void CreditCardFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;1000");
        writer.close();
    }

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException, InvalidLocationException {
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
    void testCompleteTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException, InvalidCustomerCardException {
        login();
        Integer id = model.startSaleTransaction();
        Assertions.assertTrue(model.addProductToSale(id, "6291041500213", 2));
        Assertions.assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity());
        Assertions.assertTrue(model.endSaleTransaction(id));
        Assertions.assertEquals(0.,model.computeBalance());
        Assertions.assertTrue(model.receiveCreditCardPayment(id, creditCard));
        Assertions.assertEquals(20,model.computeBalance());
        Assertions.assertEquals(2, model.computePointsForSale(id));
    }

    @Test
    void testCompleteReturn() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException {
        login();
        Integer id = model.startSaleTransaction();
        Assertions.assertTrue(model.addProductToSale(id, barcode, 2));
        Assertions.assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity());
        model.endSaleTransaction(id);
        Assertions.assertEquals(0.,model.computeBalance());
        Assertions.assertTrue(model.receiveCreditCardPayment(id, creditCard));
        Assertions.assertEquals(20, model.computeBalance());
        Integer rId = model.startReturnTransaction(id);
        model.returnProduct(rId, barcode, 1);
        Assertions.assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity());
        Assertions.assertTrue(model.endReturnTransaction(rId, true));
        Assertions.assertEquals(9, model.getProductTypeByBarCode(barcode).getQuantity());
        Assertions.assertEquals(10, model.returnCreditCardPayment(rId, creditCard));
        Assertions.assertEquals(10, model.computeBalance());
    }

    @Test
    void testCompleteOrder() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(barcode, 2, 2.);
        Assertions.assertEquals(id, -1);
        model.recordBalanceUpdate(100);
        id = model.issueOrder(barcode, 2, 20.);
        Assertions.assertEquals("ISSUED", model.getAllOrders().get(0).getStatus());
        Assertions.assertTrue(model.payOrder(id));
        Assertions.assertEquals("PAYED", model.getAllOrders().get(0).getStatus());
        Assertions.assertTrue(model.recordOrderArrival(id));
        Assertions.assertEquals("COMPLETED", model.getAllOrders().get(0).getStatus());
        Assertions.assertEquals(model.computeBalance(), 60);
        id = model.payOrderFor(barcode, 2, 10);
        Assertions.assertEquals("PAYED", model.getAllOrders().get(1).getStatus());
        Assertions.assertTrue(model.recordOrderArrival(id));
        Assertions.assertEquals(model.computeBalance(), 40);
        Assertions.assertEquals("COMPLETED", model.getAllOrders().get(1).getStatus());
    }

    @AfterEach
    void cleanup(){
        model.reset();
    }
}
