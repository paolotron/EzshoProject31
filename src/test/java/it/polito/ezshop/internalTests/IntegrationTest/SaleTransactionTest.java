package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SaleTransactionTest {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String c_username = "cname";
    final String c_password = "cname";
    final String f_username = "fname";
    final String f_password = "fname";


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(f_username, f_password);
    }

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        model.createProductType("test product", "6291041500213", 10, "");
        model.updateQuantity(1, 3);
        login();
        model.logout();
    }

    @Test
    void correctStartSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        Assertions.assertTrue(model.startSaleTransaction() > 0, "saleTransactionId must be >= 0");
    }

    @Test
    void unauthorizedStartSale() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()->model.startSaleTransaction());
    }

    @Test
    void badAddProductToSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        login();
        int id = model.startSaleTransaction();
        Assertions.assertThrows(InvalidTransactionIdException.class, ()->model.addProductToSale(-1, "1", 1));
        Assertions.assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, "100", 1));
        Assertions.assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, "01", 1));
        Assertions.assertThrows(InvalidQuantityException.class, ()->model.addProductToSale(id, "6291041500213", -1));
    }

    @Test
    void badDeleteProductFromSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        int id = model.startSaleTransaction();
        Assertions.assertThrows(InvalidTransactionIdException.class, ()->model.deleteProductFromSale(-1, "1", 1));
        Assertions.assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, "100", 1));
        Assertions.assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, "01", 1));
        Assertions.assertThrows(InvalidQuantityException.class, ()->model.deleteProductFromSale(id, "6291041500213", -1));
    }



}
