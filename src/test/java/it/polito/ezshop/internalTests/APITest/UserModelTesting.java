package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class UserModelTesting {
    EZShopInterface ezShop;

    @Before
    public void start(){
        ezShop = new it.polito.ezshop.data.EZShop();
    }

    @After
    public void end(){
        ezShop.reset();
    }

    @Test
    public void testLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {

        String username = "Paolo";
        String password = "Rabino";
        Integer id = ezShop.createUser(username, password, "cashier");
        assertEquals(ezShop.login(username, password).getId(), id);
        assertEquals(ezShop.login(username, password).getRole(), "Cashier");

    }
    @Test
    public void testLoginFail() throws InvalidPasswordException, InvalidUsernameException {
        assertNull(ezShop.login("no one", "pass"));
        assertThrows(InvalidUsernameException.class, () -> ezShop.login("", "pass"));
        assertThrows(InvalidPasswordException.class, () -> ezShop.login("us", ""));
    }
}
