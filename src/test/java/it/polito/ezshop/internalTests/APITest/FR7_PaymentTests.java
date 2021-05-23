package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.EzShopModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class FR7_PaymentTests {
    EZShopInterface model;
    final String admin_username = "Admin";
    final String admin_psw = "password12345678";
    final String productCode = "6291041500213";

    @Before
    public void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(admin_username, admin_psw, "Administrator");
        model.login(admin_username, admin_psw);
        Integer productId = model.createProductType("desc", productCode, 5., "note");
        model.updateQuantity(productId, 1000);
        model.logout();
    }
    @After
    public void closeEzShop(){
        model.reset();
    }

    @Test
    public void invalidTransactionIdTest() throws InvalidPasswordException, InvalidUsernameException {
        model.login(admin_username, admin_psw);

        assertThrows(InvalidTransactionIdException.class, ()->model.receiveCashPayment(0, 50));
        assertThrows(InvalidTransactionIdException.class, ()->model.receiveCashPayment(-12, 50));
        assertThrows(InvalidTransactionIdException.class, ()->model.receiveCashPayment(null, 50));

        assertThrows(InvalidTransactionIdException.class, ()->model.receiveCreditCardPayment(0, "4485370086510891"));
        assertThrows(InvalidTransactionIdException.class, ()->model.receiveCreditCardPayment(-12, "4485370086510891"));
        assertThrows(InvalidTransactionIdException.class, ()->model.receiveCreditCardPayment(null, "4485370086510891"));

        assertThrows(InvalidTransactionIdException.class, ()->model.returnCashPayment(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.returnCashPayment(-12));
        assertThrows(InvalidTransactionIdException.class, ()->model.returnCashPayment(null));

        assertThrows(InvalidTransactionIdException.class, ()->model.returnCreditCardPayment(0, "4485370086510891"));
        assertThrows(InvalidTransactionIdException.class, ()->model.returnCreditCardPayment(-12, "4485370086510891"));
        assertThrows(InvalidTransactionIdException.class, ()->model.returnCreditCardPayment(null, "4485370086510891"));
    }

    @Test
    public void unauthorizedTest(){
        assertThrows(UnauthorizedException.class, ()->model.receiveCashPayment(1, 50));
        assertThrows(UnauthorizedException.class, ()->model.receiveCreditCardPayment(1, "4485370086510891"));
        assertThrows(UnauthorizedException.class, ()->model.returnCashPayment(1));
        assertThrows(UnauthorizedException.class, ()->model.returnCreditCardPayment(1, "4485370086510891"));
    }

    @Test
    public void invalidPaymentTest() throws InvalidPasswordException, InvalidUsernameException {
        model.login(admin_username, admin_psw);

        assertThrows(InvalidPaymentException.class, ()->model.receiveCashPayment(1, -50));
        assertThrows(InvalidPaymentException.class, ()->model.receiveCashPayment(1, 0));
    }

    @Test
    public void invalidCreditCard() throws InvalidPasswordException, InvalidUsernameException {
        model.login(admin_username, admin_psw);

        assertThrows(InvalidCreditCardException.class, ()->model.receiveCreditCardPayment(1, ""));
        assertThrows(InvalidCreditCardException.class, ()->model.receiveCreditCardPayment(1, null));
        assertThrows(InvalidCreditCardException.class, ()->model.receiveCreditCardPayment(1, "111111111111111"));

        assertThrows(InvalidCreditCardException.class, ()->model.returnCreditCardPayment(1, ""));
        assertThrows(InvalidCreditCardException.class, ()->model.returnCreditCardPayment(1, null));
        assertThrows(InvalidCreditCardException.class, ()->model.returnCreditCardPayment(1, "111111111111111"));
    }

    @Test
    public void badReceiveCashPaymentTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException,InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        model.login(admin_username, admin_psw);

        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, productCode, 10));
        assertTrue(model.endSaleTransaction(id));

        assertEquals("If transaction does not exist, returned value should be -1" ,-1, model.receiveCashPayment(23, 100), 0.01);
        assertEquals("If amount passed is not enough to pay returned value should be -1", -1, model.receiveCashPayment(id, 10), 0.01);
    }

    @Test
    public void goodReceiveCashPayment() throws UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        model.login(admin_username, admin_psw);

        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, productCode, 10));
        assertTrue(model.endSaleTransaction(id));

        assertEquals(50.,model.receiveCashPayment(id, 150), 0.01);
    }

    @Test
    public void badReceiveCreditCardPayment() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException, IOException {
        model.login(admin_username, admin_psw);

        Integer id = model.startSaleTransaction();
        model.addProductToSale(id, productCode, 10);
        model.endSaleTransaction(id);
        assertTrue(model.addProductToSale(id, productCode, 10));
        assertTrue(model.endSaleTransaction(id));

        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n4485370086510891;30");
        writer.close();

        assertFalse("sale not exists", model.receiveCreditCardPayment(23, "4485370086510891"));
        assertFalse("not enough money",model.receiveCreditCardPayment(id, "4485370086510891"));

        writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("");
        writer.close();
        assertFalse("credit card is not present", model.receiveCreditCardPayment(id, "4485370086510891"));
    }

    @Test
    public void goodReceiveCreditCardPayment() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, IOException, InvalidCreditCardException {
        model.login(admin_username, admin_psw);

        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, productCode, 10));
        assertTrue(model.endSaleTransaction(id));

        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n4485370086510891;300");
        writer.close();

        assertTrue(model.receiveCreditCardPayment(id, "4485370086510891"));

        writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("");
        writer.close();
    }

}
