package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FR3_ProductTypeTest {
    EZShopInterface model;
    String user = "Paolo";
    String pass = "pass";
    String ProductCode = "6291041500213";

    @Before
    public void setup() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new EZShop();
        model.reset();
        model.createUser(user, pass, "Administrator");
    }

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(user, pass);
    }

    @Test
    public void correctCreateProductType() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.createProductType("Test", ProductCode ,10., "A small note");
        assertTrue(id > 0);
    }

    @Test
    public void incorrectCreateProductType() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class,()->model.createProductType("Test", ProductCode ,10., "A small note"));
        login();
        assertThrows(InvalidProductDescriptionException.class,()->model.createProductType("", ProductCode ,10., ""));
        assertThrows(InvalidProductDescriptionException.class,()->model.createProductType(null, ProductCode ,10., ""));
        assertThrows(InvalidProductCodeException.class,()->model.createProductType("Test", "0123" ,10., "note"));
        assertThrows(InvalidProductCodeException.class,()->model.createProductType("Test", "" ,10., "note"));
        assertThrows(InvalidProductCodeException.class,()->model.createProductType("Test", null ,10., "note"));
        assertThrows(InvalidPricePerUnitException.class,()->model.createProductType("Test", ProductCode ,-1, "note"));
    }

}
