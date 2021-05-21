package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class loginTest {
    EZShopInterface model;
    final String username = "USER";
    final String password = "PSW";

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
    }
    @AfterEach
    void closeEzShop(){
        model.reset();
    }

    @Test
    void login() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        model.createUser("Omar","psw345","Cashier");
        Assertions.assertEquals( model.login("Omar", "psw346"),null);
        Assertions.assertEquals( model.login("Luca", password), null);
        User user = model.login(username,password);
        Assertions.assertEquals(user.getUsername(),username);
        Assertions.assertEquals(user.getPassword(),password);
    }

    @Test
    void invalidUsername() throws InvalidUsernameException{
        Assertions.assertThrows(InvalidUsernameException.class, () -> model.login("", password));
        Assertions.assertThrows(InvalidUsernameException.class, () -> model.login(null, password));
    }
    @Test
    void invalidPassword() throws InvalidPasswordException{
        Assertions.assertThrows(InvalidPasswordException.class, ()-> model.login(username, ""));
        Assertions.assertThrows(InvalidPasswordException.class, ()-> model.login(username, null));
    }


}
