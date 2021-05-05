package it.polito.ezshop.internalTests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.Assert;
import org.junit.Test;


public class UserModelTesting {
    @Test
    public void testLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        String username = "Paolo";
        String password = "Rabino";
        Integer id = ezShop.createUser(username, password, "cashier");
        Assert.assertEquals(ezShop.login(username, password).getId(), id);
        Assert.assertEquals(ezShop.login(username, password).getRole(), "Cashier");
        Assert.assertNull(ezShop.login("noone", "nopass"));

    }
}
