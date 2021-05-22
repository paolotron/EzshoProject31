package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class OrderTests {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String c_username = "c_Dummy";
    final String c_password = "c_Dummy";
    final String productCode = "6291041500213";


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(c_username, c_password);
    }

    @Before
    public void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        model.createProductType("A Test Product", productCode, 0.5, "This is a test Note");
        model.recordBalanceUpdate(1000);
        model.logout();
    }

    @After
    public void closeEzShop(){
        model.reset();
    }

    @Test
    public void correctIssue() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();

        Integer id = model.issueOrder(productCode, 10, 2);
        assertTrue(id  > 0);
        assertEquals(model.getAllOrders().get(0).getStatus(), "ISSUED");
    }

    @Test
    public void InvalidProduct() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidProductCodeException.class, ()->model.issueOrder("", 10, 2));
        assertThrows(InvalidProductCodeException.class, ()->model.issueOrder(null, 10, 2));
    }

    @Test
    public void InvalidQuantity() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidQuantityException.class, ()->model.issueOrder(productCode, 0, 2));
        assertThrows(InvalidQuantityException.class, ()->model.issueOrder(productCode, -1, 2));
        assertThrows(InvalidQuantityException.class, ()->model.issueOrder(productCode, -1, 2));
    }

    @Test
    public void InvalidPricePerUnit() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidPricePerUnitException.class, ()->model.issueOrder(productCode, 10, 0));
        assertThrows(InvalidPricePerUnitException.class, ()->model.issueOrder(productCode, 10, -1));
    }

    @Test
    public void invalidLogin() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()-> model.issueOrder(productCode, 10, 2));
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()-> model.issueOrder(productCode, 10, 2));
    }

    @Test
    public void correctPayOrder() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException {
        login();
        Integer quantity = 10;
        Double price = 2.;
        Integer id = model.issueOrder(productCode, quantity, price);
        assertTrue(model.payOrder(id));

        Order order = model.getAllOrders().get(0);
        assertEquals(order.getStatus(), "PAYED");
        assertEquals(order.getProductCode(), productCode);
        assertEquals(order.getQuantity(), quantity, 0);
        assertEquals(order.getPricePerUnit(), price, 0);
        assertEquals(order.getTotalPrice(), price*quantity, 0.01);
    }

    @Test
    public void incorrectPayOrderNotExistent() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidOrderIdException {
        login();
        assertFalse(model.payOrder(500));
    }

    @Test
    public void incorrectPayOrderAlreadyPaid() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.payOrder(id);
        assertFalse("Duplicate payment should not be possible", model.payOrder(id));
    }

    @Test
    public void PayOrderInvalidId() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidOrderIdException.class, ()->model.payOrder(0));
        assertThrows(InvalidOrderIdException.class, ()->model.payOrder(-50));
    }

    @Test
    public void PayOrderUnauthorized() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()->model.payOrder(2));
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()->model.payOrder(2));
    }

    @Test
    public void CorrectPayOrderFor() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        model.recordBalanceUpdate(100);
        Integer id = model.payOrderFor(productCode, 5, 10);
        assertTrue("Id Should be > 0", id > 0);
    }

    @Test
    public void CorrectPayOrderForExistenceCheck() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        Integer quantity = 5;
        Double price = 10.;
        model.recordBalanceUpdate(100);
        Integer id = model.payOrderFor(productCode, quantity, price);
        assertTrue("Id Should be > 0", id > 0);
        Order order =  model.getAllOrders().get(0);
        assertEquals(order.getOrderId(), id);
        assertEquals(order.getQuantity(), quantity, 0);
        assertEquals(order.getPricePerUnit(), price, 0.01);
        assertEquals(order.getTotalPrice(), price*quantity, 0.01);
        assertEquals(order.getStatus(), "PAYED");
    }

    @Test
    public void PayOrderForNoBalance() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        model.recordBalanceUpdate(-1000);
        assertEquals(model.payOrderFor(productCode, 10, 5), -1, 0);
        model.recordBalanceUpdate(100);
        assertEquals(model.payOrderFor(productCode, 10, 50), -1, 0);
        assertTrue(model.payOrderFor(productCode, 10, 5) != -1);
    }

    @Test
    public void PayOrderForNoProduct() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        assertEquals(model.payOrderFor("stupid", 10, 5), -1, 0);
    }

    @Test
    public void PayOrderForInvalidProduct() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidProductCodeException.class, ()-> model.payOrderFor("", 10, 10));
        assertThrows(InvalidProductCodeException.class, ()-> model.payOrderFor(null, 10, 10));
    }

    @Test
    public void PayOrderForInvalidQuantity() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidQuantityException.class, ()-> model.payOrderFor(productCode, 0, 10));
        assertThrows(InvalidQuantityException.class, ()-> model.payOrderFor(productCode, -1, 10));
    }

    @Test
    public void PayOrderForInvalidPrice() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidPricePerUnitException.class, ()-> model.payOrderFor(productCode, 2, 0));
        assertThrows(InvalidPricePerUnitException.class, ()-> model.payOrderFor(productCode, 2, -1));
    }

    @Test
    public void PayOrderForUnauthorized() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()-> model.payOrderFor(productCode, 2, 10));
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()-> model.payOrderFor(productCode, 2, 10));
    }

    @Test
    public void CorrectRecordOrderArrival() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException, InvalidLocationException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.getAllProductTypes().get(0).setLocation("nowhere");
        model.payOrder(id);
        assertTrue(model.recordOrderArrival(id));
        assertEquals(model.getAllOrders().get(0).getStatus(), "COMPLETED");
    }

    @Test
    public void IncorrectRecordOrderArrival() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.getAllProductTypes().get(0).setLocation("nowhere");
        assertFalse(model.recordOrderArrival(id));
    }

    @Test
    public void NonexistentOrderArrival() throws InvalidPasswordException, InvalidUsernameException, InvalidLocationException, UnauthorizedException, InvalidOrderIdException {
        login();
        assertFalse(model.recordOrderArrival(10));
    }

    @Test
    public void RecordOrderArrivalInvalidId() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidOrderIdException.class, ()->model.recordOrderArrival(-1));
        assertThrows(InvalidOrderIdException.class, ()->model.recordOrderArrival(0));
        assertThrows(InvalidOrderIdException.class, ()->model.recordOrderArrival(null));
    }

    @Test
    public void RecordOrderArrivalInvalidLocation() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.payOrder(id);
        model.getProductTypeByBarCode(productCode).setLocation(null);
        assertThrows(InvalidLocationException.class, ()->model.recordOrderArrival(id));
    }

    @Test
    public void RecordOrderArrivalUnauthorized() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.payOrder(id);
        model.getProductTypeByBarCode(productCode).setLocation("nowhere");
        model.logout();
        assertThrows(UnauthorizedException.class, ()->model.recordOrderArrival(id));
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()->model.recordOrderArrival(id));
    }

    @Test
    public void CorrectGetAllOrders() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        Integer[] quantities = {10, 2, 4};
        Double[] priceperunits = {5., 1., 10.};
        for (int i = 0; i < 3; i++){
            model.issueOrder(productCode, quantities[i], priceperunits[i]);
        }
        assertArrayEquals(model.getAllOrders().stream().map(Order::getQuantity).toArray(Integer[]::new), quantities);
        assertArrayEquals(model.getAllOrders().stream().map(Order::getPricePerUnit).toArray(Double[]::new), priceperunits);
    }

    @Test
    public void UnauthorizedGetAllOrders() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()->model.getAllOrders());
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()->model.getAllOrders());
    }

}
