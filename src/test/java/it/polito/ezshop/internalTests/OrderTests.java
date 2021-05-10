package it.polito.ezshop.internalTests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTests {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String c_username = "c_Dummy";
    final String c_password = "c_Dummy";
    final String productCode = "1234";


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(c_username, c_password);
    }

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        model.createProductType("A Test Product", productCode, 0.5, "This is a test Note");
        model.logout();
    }

    @Test
    void correctIssue() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        Assertions.assertTrue(id  > 0, "Returned id: "+ id +" must be > 0");
        Assertions.assertEquals(model.getAllOrders().get(0).getStatus(), "ISSUED");
    }

    @Test
    void InvalidProduct() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidProductCodeException.class, ()->model.issueOrder("", 10, 2));
        Assertions.assertThrows(InvalidProductCodeException.class, ()->model.issueOrder(null, 10, 2));
    }

    @Test
    void InvalidQuantity() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidQuantityException.class, ()->model.issueOrder(productCode, 0, 2));
        Assertions.assertThrows(InvalidQuantityException.class, ()->model.issueOrder(productCode, -1, 2));
        Assertions.assertThrows(InvalidQuantityException.class, ()->model.issueOrder(productCode, -1, 2));
    }

    @Test
    void InvalidPricePerUnit() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidPricePerUnitException.class, ()->model.issueOrder(productCode, 10, 0));
        Assertions.assertThrows(InvalidPricePerUnitException.class, ()->model.issueOrder(productCode, 10, -1));
    }

    @Test
    void invalidLogin() throws InvalidPasswordException, InvalidUsernameException {
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.issueOrder(productCode, 10, 2));
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.issueOrder(productCode, 10, 2));
    }

    @Test
    void correctPayOrder() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException {
        login();
        Integer quantity = 10;
        Double price = 2.;
        Integer id = model.issueOrder(productCode, quantity, price);
        Assertions.assertTrue(model.payOrder(id));
        Order order = model.getAllOrders().get(0);
        Assertions.assertEquals(order.getStatus(), "ORDERED");
        Assertions.assertEquals(order.getProductCode(), productCode);
        Assertions.assertEquals(order.getQuantity(), quantity);
        Assertions.assertEquals(order.getPricePerUnit(), price);
        Assertions.assertEquals(order.getTotalPrice(), price*quantity);
    }

    @Test
    void incorrectPayOrderNotExistent() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidOrderIdException {
        login();
        Assertions.assertFalse(model.payOrder(500));
    }

    @Test
    void incorrectPayOrderAlreadyPaid() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.payOrder(id);
        Assertions.assertFalse(model.payOrder(id), "Duplicate payment should not be possible");
    }

    @Test
    void PayOrderInvalidId() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidOrderIdException.class, ()->model.payOrder(0));
        Assertions.assertThrows(InvalidOrderIdException.class, ()->model.payOrder(-50));
    }

    @Test
    void PayOrderUnauthorized() throws InvalidPasswordException, InvalidUsernameException {
        Assertions.assertThrows(UnauthorizedException.class, ()->model.payOrder(2));
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()->model.payOrder(2));
    }

    @Test
    void CorrectPayOrderFor() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        model.recordBalanceUpdate(100);
        Integer id = model.payOrderFor(productCode, 5, 10);
        Assertions.assertTrue(id > 0, "Id Should be > 0");
    }

    @Test
    void CorrectPayOrderForExistenceCheck() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        Integer quantity = 5;
        Double price = 10.;
        model.recordBalanceUpdate(100);
        Integer id = model.payOrderFor(productCode, quantity, price);
        Assertions.assertTrue(id > 0, "Id Should be > 0");
        Order order =  model.getAllOrders().get(0);
        Assertions.assertEquals(order.getOrderId(), id);
        Assertions.assertEquals(order.getQuantity(), quantity);
        Assertions.assertEquals(order.getPricePerUnit(), price);
        Assertions.assertEquals(order.getTotalPrice(), price*quantity);
        Assertions.assertEquals(order.getStatus(), "ORDERED");
    }

    @Test
    void PayOrderForNoBalance() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        Assertions.assertEquals(model.payOrderFor(productCode, 10, 5), -1);
        model.recordBalanceUpdate(100);
        Assertions.assertEquals(model.payOrderFor(productCode, 10, 50), -1);
        Assertions.assertTrue(model.payOrderFor(productCode, 10, 5) != -1);
        Assertions.assertEquals(model.payOrderFor(productCode, 10, 5), -1);
    }

    @Test
    void PayOrderForNoProduct() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertEquals(model.payOrderFor("stupid", 10, 5), -1);
    }

    @Test
    void PayOrderForInvalidProduct() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidProductCodeException.class, ()-> model.payOrderFor("", 10, 10));
        Assertions.assertThrows(InvalidProductCodeException.class, ()-> model.payOrderFor(null, 10, 10));
    }

    @Test
    void PayOrderForInvalidQuantity() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidQuantityException.class, ()-> model.payOrderFor(productCode, 0, 10));
        Assertions.assertThrows(InvalidQuantityException.class, ()-> model.payOrderFor(productCode, -1, 10));
    }

    @Test
    void PayOrderForInvalidPrice() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidPricePerUnitException.class, ()-> model.payOrderFor(productCode, 2, 0));
        Assertions.assertThrows(InvalidPricePerUnitException.class, ()-> model.payOrderFor(productCode, 2, -1));
    }

    @Test
    void PayOrderForUnauthorized() throws InvalidPasswordException, InvalidUsernameException {
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.payOrderFor(productCode, 2, 10));
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.payOrderFor(productCode, 2, 10));
    }

    @Test
    void CorrectRecordOrderArrival() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException, InvalidLocationException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.getAllProductTypes().get(0).setLocation("nowhere");
        model.payOrder(id);
        Assertions.assertTrue(model.recordOrderArrival(id));
        Assertions.assertEquals(model.getAllOrders().get(0).getStatus(), "COMPLETED");
    }

    @Test
    void IncorrectRecordOrderArrival() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.getAllProductTypes().get(0).setLocation("nowhere");
        Assertions.assertFalse(model.recordOrderArrival(id));
    }

    @Test
    void NonexistentOrderArrival() throws InvalidPasswordException, InvalidUsernameException, InvalidLocationException, UnauthorizedException, InvalidOrderIdException {
        login();
        Assertions.assertFalse(model.recordOrderArrival(10));
    }

    @Test
    void RecordOrderArrivalInvalidId() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidOrderIdException.class, ()->model.recordOrderArrival(-1));
        Assertions.assertThrows(InvalidOrderIdException.class, ()->model.recordOrderArrival(0));
        Assertions.assertThrows(InvalidOrderIdException.class, ()->model.recordOrderArrival(null));
    }

    @Test
    void RecordOrderArrivalInvalidLocation() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.payOrder(id);
        model.getProductTypeByBarCode(productCode).setLocation(null);
        Assertions.assertThrows(InvalidLocationException.class, ()->model.recordOrderArrival(id));
    }

    @Test
    void RecordOrderArrivalUnauthorized() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException {
        login();
        Integer id = model.issueOrder(productCode, 10, 2);
        model.payOrder(id);
        model.getProductTypeByBarCode(productCode).setLocation("nowhere");
        model.logout();
        Assertions.assertThrows(UnauthorizedException.class, ()->model.recordOrderArrival(id));
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()->model.recordOrderArrival(id));
    }

    @Test
    void CorrectGetAllOrders() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        login();
        Integer[] quantities = {10, 2, 4};
        Double[] priceperunits = {5., 1., 10.};
        for (int i = 0; i < 3; i++){
            model.issueOrder(productCode, quantities[i], priceperunits[i]);
        }
        Assertions.assertEquals(model.getAllOrders().stream().map(Order::getQuantity).toArray(Integer[]::new), quantities);
        Assertions.assertEquals(model.getAllOrders().stream().map(Order::getPricePerUnit).toArray(Double[]::new), priceperunits);
    }

    @Test
    void UnauthorizedGetAllOrders() throws InvalidPasswordException, InvalidUsernameException {
        Assertions.assertThrows(UnauthorizedException.class, ()->model.getAllOrders());
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()->model.getAllOrders());
    }

}
