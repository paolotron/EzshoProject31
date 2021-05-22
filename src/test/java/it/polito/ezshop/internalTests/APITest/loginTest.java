package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class loginTest {
    EZShopInterface model;
    final String username = "USER";
    final String password = "PSW";

    @Before
    public void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
    }
    @After
    public void closeEzShop(){
        model.reset();
    }

    @Test
    public void login() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        model.createUser("Omar","psw345","Cashier");
        assertNull(model.login("Omar", "psw346"));
        assertNull(model.login("Luca", password));
        User user = model.login(username,password);
        assertEquals(user.getUsername(),username);
        assertEquals(user.getPassword(),password);
    }

    @Test
    public void invalidUsername() {
        assertThrows(InvalidUsernameException.class, () -> model.login("", password));
        assertThrows(InvalidUsernameException.class, () -> model.login(null, password));
    }
    @Test
    public void invalidPassword() {
        assertThrows(InvalidPasswordException.class, ()-> model.login(username, ""));
        assertThrows(InvalidPasswordException.class, ()-> model.login(username, null));
    }


}
