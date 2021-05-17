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
    void testCompleteTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException {
        login();
        Integer id = model.startSaleTransaction();
        Assertions.assertTrue(model.addProductToSale(id, "6291041500213", 2));
        model.endSaleTransaction(id);
        Assertions.assertEquals(model.computeBalance(), 0.);
        Assertions.assertTrue(model.receiveCreditCardPayment(id, creditCard));
        Assertions.assertEquals(model.computeBalance(), 20);
    }

    @AfterEach
    void cleanup(){
        model.reset();
    }
}
