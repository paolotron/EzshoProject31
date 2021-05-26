package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.EzShopModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class FR4_OrderTests {
    EZShop model;
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

    @After
    public void reset(){
        model.model.reset();
    }



    @Test
    public void correctCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidQuantityException {
        login();
        ProductType p = model.model.createProduct("desc", "6291041500213", 10.0, null);
        model.model.createUser("Andrea", "lol", "Administrator");
        model.model.recordBalanceUpdate(100);
        int id = model.model.createOrder(p.getBarCode(), 3, p.getPricePerUnit());
        assertTrue("newOrderId should be > 0", id > 0);
    }

    @Test
    public void wrongProductCodeCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = model.model.createProduct("desc", "6291041500213", 10.0, null);
        model.model.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidProductCodeException.class, ()->model.model.createOrder("", 1, p.getPricePerUnit()));
        assertThrows(InvalidProductCodeException.class, ()->model.model.createOrder(null, 1, p.getPricePerUnit()));
        assertEquals("returned value should be -1 since product doesn't exist", -1, model.model.createOrder("6291041500214", 1, p.getPricePerUnit()), 0);
    }

    @Test
    public void wrongQuantityCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = model.model.createProduct("desc", "6291041500213", 10.0, null);
        model.model.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidQuantityException.class, ()->model.model.createOrder("6291041500213", 0, p.getPricePerUnit()));
        assertThrows(InvalidQuantityException.class, ()->model.model.createOrder("6291041500213", -1, p.getPricePerUnit()));
    }

    @Test
    public void wrongPriceCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = model.model.createProduct("desc", "6291041500213", 10.0, null);
        model.model.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidPricePerUnitException.class, ()->model.model.createOrder("6291041500213", 1, -1.0));
        assertThrows(InvalidPricePerUnitException.class, ()->model.model.createOrder("6291041500213", 1, 0));
    }

    @Test
    public void correctUpdateQuantity() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException {
        login();
        String barCode = "6291041500213";
        ProductType p = model.model.createProduct("desc", barCode, 10.0, null);
        assertTrue(model.updatePosition(p.getId(), "123-123-123"));
        assertTrue(model.updateQuantity(p.getId(), 10));
        assertTrue(model.updatePosition(p.getId(), null));
        assertEquals(10, p.getQuantity(), 0);
    }

    @Test
    public void wrongUpdateQuantity() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidProductIdException, InvalidLocationException {
        login();
        String barCode = "6291041500213";
        ProductType p = model.model.createProduct("desc", barCode, 10.0, null);
        assertFalse(model.updateQuantity(p.getId(), 10));
        assertTrue(model.updatePosition(p.getId(), "123-123-123"));
        assertFalse(model.updateQuantity(p.getId(), -10));
        assertTrue(model.updateQuantity(p.getId(), 10));
        assertTrue(model.updateQuantity(p.getId(), -5));
        assertFalse(model.updateQuantity(p.getId(), -6));
        assertEquals(p.getQuantity(), 5, 0);
    }

    @Test
    public void invalidUpdateQuantity(){
        assertThrows(UnauthorizedException.class, ()->model.updateQuantity(1, 10));
        assertThrows(InvalidProductIdException.class, ()->model.updateQuantity(0, 10));
        assertThrows(InvalidProductIdException.class, ()->model.updateQuantity(-1, 10));
        assertThrows(InvalidProductIdException.class, ()->model.updateQuantity(null, 10));
    }

}
