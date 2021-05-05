package it.polito.ezshop.internalTests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class UserModelTesting {
    EZShopInterface ezShop;

    UserModelTesting(){
        ezShop = new it.polito.ezshop.data.EZShop();
    }

    @Test
    public void testLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {

        String username = "Paolo";
        String password = "Rabino";
        Integer id = ezShop.createUser(username, password, "cashier");
        Assertions.assertEquals(ezShop.login(username, password).getId(), id);
        Assertions.assertEquals(ezShop.login(username, password).getRole(), "Cashier");

    }
    @Test
    public void testLoginFail() throws InvalidPasswordException, InvalidUsernameException {
        Assertions.assertNull(ezShop.login("no one", "pass"));
        Assertions.assertThrows(InvalidUsernameException.class, () -> ezShop.login("", "pass"));
        Assertions.assertThrows(InvalidPasswordException.class, () -> ezShop.login("us", ""));
    }
}
