package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


public class FR6_SaleTransactionTest {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String c_username = "cname";
    final String c_password = "cname";
    final String f_username = "fname";
    final String f_password = "fname";
    Integer productId;


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(f_username, f_password);
    }

    @Before
    public void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        productId = model.createProductType("test product", "6291041500213", 10, "");
        model.updateQuantity(productId, 3);
        login();
        model.logout();
    }

    @Test
    public void correctStartSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        assertTrue(model.startSaleTransaction() > 0);
    }

    @Test
    public void unauthorizedStartSale() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()->model.startSaleTransaction());
    }

    @Test
    public void badAddProductToSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        login();
        int id = model.startSaleTransaction();
        assertThrows(InvalidTransactionIdException.class, ()->model.addProductToSale(-1, "1", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, "100", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, "01", 1));
        assertThrows(InvalidQuantityException.class, ()->model.addProductToSale(id, "6291041500213", -1));
    }

    @Test
    public void badDeleteProductFromSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        int id = model.startSaleTransaction();
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteProductFromSale(-1, "1", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, "100", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, "01", 1));
        assertThrows(InvalidQuantityException.class, ()->model.deleteProductFromSale(id, "6291041500213", -1));
    }



}
